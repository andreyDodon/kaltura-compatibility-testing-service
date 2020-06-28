package com.github.kaltura.automation.KalturaCompatibilityService.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.jsonSchema.types.ObjectSchema;
import com.github.kaltura.automation.KalturaCompatibilityService.db.service.ClassesService;
import com.github.kaltura.automation.KalturaCompatibilityService.db.service.EnumsService;
import com.github.kaltura.automation.KalturaCompatibilityService.model.KalturaClass.KalturaClass;
import com.github.kaltura.automation.KalturaCompatibilityService.model.KalturaEnum.KalturaEnum;
import com.github.kaltura.automation.KalturaCompatibilityService.model.KalturaError.KalturaError;
import com.github.kaltura.automation.KalturaCompatibilityService.model.KalturaService.ActionResult;
import com.github.kaltura.automation.KalturaCompatibilityService.model.KalturaService.KalturaService;
import com.github.kaltura.automation.KalturaCompatibilityService.model.KalturaService.ServiceAction;
import com.github.kaltura.automation.KalturaCompatibilityService.model.KalturaXml;
import com.github.kaltura.automation.KalturaCompatibilityService.model.serviceController.CompareClientXmlRequest;
import com.github.kaltura.automation.KalturaCompatibilityService.model.serviceController.response.CompareClientXmlResponse;
import com.github.kaltura.automation.KalturaCompatibilityService.model.serviceController.response.Differences;
import com.github.kaltura.automation.KalturaCompatibilityService.utils.XmlConverter;
import com.github.kaltura.automation.KalturaCompatibilityService.utils.excel.ExcelUtils;
import com.github.kaltura.automation.KalturaCompatibilityService.utils.serialization.KalturaClassesSerializationUtils;
import com.google.common.collect.Maps;
import com.vdurmont.semver4j.Semver;
import io.swagger.v3.oas.models.OpenAPI;
import org.apache.commons.lang3.builder.Diffable;
import org.javers.core.Javers;
import org.javers.core.JaversBuilder;
import org.javers.core.diff.Diff;
import org.javers.core.diff.changetype.ValueChange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

import static org.javers.core.diff.ListCompareAlgorithm.LEVENSHTEIN_DISTANCE;

/**
 * @author andrey.dodon - 22/04/2020
 */


@RestController
@RequestMapping(value = "/compatibility")
public class CompatibilityServiceController {

    private static Logger logger = LoggerFactory.getLogger(CompatibilityServiceController.class);
    Map<String, KalturaClass> kalturaClassesMap = new HashMap<>();
    HashMap<String, KalturaEnum> kalturaEnumHashMap = new HashMap<>();
    List<ObjectSchema> objectSchemas = new ArrayList<>();
    List<String> warnings = new ArrayList<>();

    @Autowired
    private OpenAPI openAPI;
    @Autowired
    private XmlConverter xmlConverter;
    @Autowired
    private EnumsService enumsService;
    @Autowired
    private ClassesService classesService;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private KalturaClassesSerializationUtils kalturaClassesSerializationUtils;
    @Autowired
    private ExcelUtils excelUtils;
    private CompareClientXmlResponse resp;


    @PostMapping(path = "/compareClients", consumes = "application/json", produces = "application/json")
    @ResponseBody
    public CompareClientXmlResponse compareClients(@RequestBody CompareClientXmlRequest compareClientXmlRequest) throws IOException {
        resp = new CompareClientXmlResponse();
        KalturaXml previousXml = xmlConverter.xmlToObject(compareClientXmlRequest.getPreviousXmlUrl());
        KalturaXml currentXml = xmlConverter.xmlToObject(compareClientXmlRequest.getCurrentXmlUrl());
        Semver previousCandidate = new Semver(previousXml.getApiVersion());
        if (previousCandidate.isGreaterThan(currentXml.getApiVersion())) {
            KalturaXml temp = previousXml;
            previousXml = currentXml;
            currentXml = temp;
        }
        saveToDb(currentXml);
        return compare(previousXml, currentXml);

    }


    @GetMapping(path = "/convertXmlToExcel")
    public ResponseEntity<InputStreamResource> getExcel(@RequestParam URL url) throws IOException {
        KalturaXml kalturaXml = xmlConverter.xmlToObject(url);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=clientXml_" + kalturaXml.getApiVersion() + ".xlsx")
                .body(new InputStreamResource(excelUtils.parseXmlToExcel(kalturaXml)));

    }


    private void saveToDb(KalturaXml kalturaXml) {
        xmlConverter.saveKalturaServices(kalturaXml.getKalturaServices());
        xmlConverter.saveKalturaErrors(kalturaXml.getKalturaErrors());
        xmlConverter.saveKalturaEnums(kalturaXml.getKalturaEnums());
        xmlConverter.saveKalturaClasses(kalturaXml.getKalturaClasses());
    }


    public CompareClientXmlResponse compare(KalturaXml previousXml, KalturaXml currentXml) throws IOException {
        resp.setPreviousApiVersion(previousXml.getApiVersion());
        resp.setCurrentApiVersion(currentXml.getApiVersion());
        analyzeXmls(previousXml, currentXml);
        return resp;
    }

    private void analyzeXmls(KalturaXml previousXml, KalturaXml currentXml) throws IOException {
        //checkNoMisses(previousXml, currentXml);
        analyze(
                getKalturaErrorsMap(previousXml.getKalturaErrors()),
                getKalturaErrorsMap(currentXml.getKalturaErrors()));
        analyze(
                getKalturaEnumsMap(previousXml.getKalturaEnums()),
                getKalturaEnumsMap(currentXml.getKalturaEnums()));
        analyze(
                getKalturaClassesMap(previousXml.getKalturaClasses()),
                getKalturaClassesMap(currentXml.getKalturaClasses()));
        checkServices(previousXml, currentXml);
    }

    private void checkServices(KalturaXml previousXml, KalturaXml currentXml) {
        Map<String, KalturaService> prevServices = previousXml.getKalturaServices()
                .stream().collect(Collectors.toMap(KalturaService::getServiceName, s -> s));
        Map<String, KalturaService> currServices = currentXml.getKalturaServices()
                .stream().collect(Collectors.toMap(KalturaService::getServiceName, s -> s));


        prevServices.forEach((k, v) -> {


            Map<String, ActionResult> prevServiceActionResults = v.getServiceActions().stream()
                    .collect(Collectors.toMap(ServiceAction::getActionName, ServiceAction::getActionResults));

            Map<String, ActionResult> currServiceActionResults = currServices.get(k) == null ? Collections.emptyMap() :
                    currServices.get(k).getServiceActions().stream()
                            .collect(Collectors.toMap(ServiceAction::getActionName, ServiceAction::getActionResults));

            checkServiceResponseTypeCompatibility(k, prevServiceActionResults, currServiceActionResults);

        });
    }


    private void checkServiceResponseTypeWasntModified(String serviceName, Map<String, ActionResult> currServiceActionResults) {
        currServiceActionResults.forEach((k, v) -> {
            if (warnings.contains(v.getResultType())) {
                Differences differences = new Differences();
                resp.getRed().addAndGet(1);
                differences.setDescription(
                        String.format("Service %s action %s is no longer backward compatible since the result type is modified", serviceName, k));
                differences.setObjectName(serviceName + "." + k);
                differences.setObjectType("service");
                differences.setPreviousValue(v.getResultType());
                differences.setCurrentValue(v.getResultType());
                resp.getRedDetails().add(differences);
            }
        });
    }

    private void checkServiceResponseTypeCompatibility(String serviceName,
                                                       Map<String, ActionResult> oldVersion, Map<String, ActionResult> currentVersion) {

        Javers javers = JaversBuilder.javers().withListCompareAlgorithm(LEVENSHTEIN_DISTANCE).build();

        oldVersion.forEach((k, v) -> {
            Diff diff = javers.compare(v, currentVersion.get(k));
            diff.getChangesByType(ValueChange.class).forEach(c -> {
                Differences differences = new Differences();
                resp.getRed().addAndGet(1);
                differences.setDescription(
                        String.format("Service '%s.%s' result type changed from '%s' to '%s'", serviceName, k, c.getLeft(), c.getRight()));
                differences.setObjectName(c.getLeft().toString());
                differences.setObjectType("service");
                differences.setPreviousValue(c.getLeft().toString());
                differences.setCurrentValue(c.getRight().toString());
                resp.getRedDetails().add(differences);
            });
        });

        checkServiceResponseTypeWasntModified(serviceName, currentVersion);
    }


    public void analyze(Map<String, Diffable> map1, Map<String, Diffable> map2) {
        Maps.difference(map1, map2).entriesDiffering().forEach((k, v) -> {
            v.leftValue().diff(v.rightValue()).forEach(d -> {
                Differences differences = new Differences();
                differences.setDescription(d.getFieldName());
                differences.setPreviousValue(d.getLeft().toString());
                differences.setCurrentValue(d.getRight().toString());
                if (d.getFieldName().contains("WARNING")) {
                    resp.getYellow().addAndGet(1);
                    differences.setObjectType(d.getFieldName().split("\\s+")[2]);
                    differences.setObjectName(d.getFieldName().split("\\s+")[3]);
                    resp.getYellowDetails().add(differences);
                    warnings.add(d.getFieldName().split("\\s+")[3]);
                } else if (d.getFieldName().contains("ERROR")) {
                    resp.getRed().addAndGet(1);
                    differences.setObjectType(d.getFieldName().split("\\s+")[2]);
                    differences.setObjectName(d.getFieldName().split("\\s+")[3]);
                    resp.getRedDetails().add(differences);
                }
            });
        });
    }


    private Map<String, Diffable> getKalturaEnumsMap(List<KalturaEnum> kalturaEnums) throws IOException {
        return kalturaEnums.stream().collect(Collectors.toMap(KalturaEnum::getEnumName, e -> e));
    }

    private Map<String, Diffable> getKalturaClassesMap(List<KalturaClass> kalturaClasses) throws IOException {
        return kalturaClasses.stream().collect(Collectors.toMap(KalturaClass::getClassName, c -> c));
    }

    private Map<String, Diffable> getKalturaErrorsMap(List<KalturaError> kalturaErrors) throws IOException {
        return kalturaErrors.stream().collect(Collectors.toMap(KalturaError::getErrorName, e -> e));
    }


}
