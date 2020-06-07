package com.github.kaltura.automation.KalturaCompatibilityService.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.jsonSchema.types.ObjectSchema;
import com.github.kaltura.automation.KalturaCompatibilityService.db.service.ClassesService;
import com.github.kaltura.automation.KalturaCompatibilityService.db.service.EnumsService;
import com.github.kaltura.automation.KalturaCompatibilityService.model.KalturaClass.KalturaClass;
import com.github.kaltura.automation.KalturaCompatibilityService.model.KalturaEnum.KalturaEnum;
import com.github.kaltura.automation.KalturaCompatibilityService.model.KalturaError.KalturaError;
import com.github.kaltura.automation.KalturaCompatibilityService.model.KalturaService.KalturaService;
import com.github.kaltura.automation.KalturaCompatibilityService.model.KalturaXml;
import com.github.kaltura.automation.KalturaCompatibilityService.model.serviceController.CompareClientXmlRequest;
import com.github.kaltura.automation.KalturaCompatibilityService.model.serviceController.response.CompareClientXmlResponse;
import com.github.kaltura.automation.KalturaCompatibilityService.model.serviceController.response.Differences;
import com.github.kaltura.automation.KalturaCompatibilityService.utils.XmlConverter;
import com.github.kaltura.automation.KalturaCompatibilityService.utils.excel.ExcelUtils;
import com.github.kaltura.automation.KalturaCompatibilityService.utils.serialization.KalturaClassesSerializationUtils;
import com.google.common.collect.Maps;
import com.vdurmont.semver4j.Semver;
import de.danielbechler.diff.ObjectDifferBuilder;
import de.danielbechler.diff.node.DiffNode;
import de.danielbechler.diff.node.Visit;
import io.swagger.v3.oas.models.OpenAPI;
import org.apache.commons.lang3.builder.Diffable;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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


    @PostMapping(path = "/compareClients", consumes = "application/json", produces = "application/json")
    @ResponseBody
    public CompareClientXmlResponse compareClients(@RequestBody CompareClientXmlRequest compareClientXmlRequest) throws IOException {
        KalturaXml previousXml = xmlConverter.xmlToObject(compareClientXmlRequest.getPreviousXmlUrl());
        KalturaXml currentXml = xmlConverter.xmlToObject(compareClientXmlRequest.getCurrentXmlUrl());
        Semver previousCandidate = new Semver(previousXml.getApiVersion());
        if (previousCandidate.isGreaterThan(currentXml.getApiVersion())) {
            KalturaXml temp = previousXml;
            previousXml = currentXml;
            currentXml = temp;
        }

        xmlConverter.saveKalturaServices(previousXml.getKalturaServices());
        xmlConverter.saveKalturaErrors(previousXml.getKalturaErrors());
        xmlConverter.saveKalturaEnums(previousXml.getKalturaEnums());
        xmlConverter.saveKalturaClasses(previousXml.getKalturaClasses());
        return compare(previousXml, currentXml);

    }


    public CompareClientXmlResponse compare(KalturaXml previousXml, KalturaXml currentXml) throws IOException {
        CompareClientXmlResponse resp = new CompareClientXmlResponse();
        resp.setPreviousApiVersion(previousXml.getApiVersion());
        resp.setCurrentApiVersion(currentXml.getApiVersion());

        analyze(
                getKalturaErrorsMap(previousXml.getKalturaErrors()),
                getKalturaErrorsMap(currentXml.getKalturaErrors()), resp);
        analyze(
                getKalturaEnumsMap(previousXml.getKalturaEnums()),
                getKalturaEnumsMap(currentXml.getKalturaEnums()), resp);
        analyze(
                getKalturaClassesMap(previousXml.getKalturaClasses()),
                getKalturaClassesMap(currentXml.getKalturaClasses()), resp);

//        Map<String, KalturaService> prevServices = previousXml.getKalturaServices()
//                .stream().collect(Collectors.toMap(KalturaService::getServiceName, s -> s));
//        Map<String, KalturaService> currServices = currentXml.getKalturaServices()
//                .stream().collect(Collectors.toMap(KalturaService::getServiceName, s -> s));
//
//        currServices.forEach((k,v) -> {
//            DiffNode diff = ObjectDifferBuilder.buildDefault().compare(v, prevServices.get(k));
//
//            if (diff.hasChanges()) {
//                diff.visit(new DiffNode.Visitor() {
//                    public void node(DiffNode node, Visit visit)
//                    {
//                        if (!node.hasChildren()) { // Only print if the property has no child
//                            final Object oldValue = node.canonicalGet(v);
//                            final Object newValue = node.canonicalGet(prevServices.get(k));
//
//                            final String message = k + " "+node.getPropertyName() + " changed from " +
//                                    oldValue + " to " + newValue;
//                            System.out.println(message);
//                        }
//                    }
//                });
//            } else {
//                System.out.println("No differences");
//            }
//        });
//
//
//        analyze(
//                getKalturaServicesMap(previousXml.getKalturaServices()),
//                getKalturaServicesMap(currentXml.getKalturaServices()), resp);
        return resp;
    }


    public void analyze(Map<String, Diffable> map1, Map<String, Diffable> map2,
                        CompareClientXmlResponse response) {
        Maps.difference(map1, map2).entriesDiffering().forEach((k, v) -> {
            v.leftValue().diff(v.rightValue()).forEach(d -> {
                Differences differences = new Differences();
                differences.setDescription(d.getFieldName());
                differences.setPreviousValue(d.getLeft().toString());
                differences.setCurrentValue(d.getRight().toString());
                if (d.getFieldName().contains("WARNING")) {
                    response.getYellow().addAndGet(1);
                    response.getYellowDetails().add(differences);
                } else if (d.getFieldName().contains("ERROR")) {
                    response.getRed().addAndGet(1);
                    response.getRedDetails().add(differences);
                }
            });
        });
    }


    private Map<String, Diffable> getKalturaEnumsMap(List<KalturaEnum> kalturaEnums) throws IOException {
        return kalturaEnums.stream().collect(Collectors.toMap(KalturaEnum::getEnumName, e -> e));
    }

    private Map<String, Diffable> getKalturaServicesMap(List<KalturaService> kalturaServices) throws IOException {
        return kalturaServices.stream().collect(Collectors.toMap(KalturaService::getServiceName, s -> s));
    }

    private Map<String, Diffable> getKalturaClassesMap(List<KalturaClass> kalturaClasses) throws IOException {
        return kalturaClasses.stream().collect(Collectors.toMap(KalturaClass::getClassName, c -> c));
    }

    private Map<String, Diffable> getKalturaErrorsMap(List<KalturaError> kalturaErrors) throws IOException {
        return kalturaErrors.stream().collect(Collectors.toMap(KalturaError::getErrorName, e -> e));
    }


    @GetMapping(path = "/convertXmlToExcel")
    public ResponseEntity<InputStreamResource> getExcel(@RequestParam URL url) throws IOException {
        KalturaXml kalturaXml = xmlConverter.xmlToObject(url);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=clientXml_" + kalturaXml.getApiVersion() + ".xlsx")
                .body(new InputStreamResource(excelUtils.parseXmlToExcel(kalturaXml)));

    }

}
