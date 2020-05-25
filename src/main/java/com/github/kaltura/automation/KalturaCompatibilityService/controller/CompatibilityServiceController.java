package com.github.kaltura.automation.KalturaCompatibilityService.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.jsonSchema.types.ObjectSchema;
import com.github.kaltura.automation.KalturaCompatibilityService.db.service.ClassesService;
import com.github.kaltura.automation.KalturaCompatibilityService.db.service.EnumsService;
import com.github.kaltura.automation.KalturaCompatibilityService.model.KalturaClass.KalturaClasses;
import com.github.kaltura.automation.KalturaCompatibilityService.model.KalturaEnum.KalturaEnum;
import com.github.kaltura.automation.KalturaCompatibilityService.model.KalturaError.KalturaError;
import com.github.kaltura.automation.KalturaCompatibilityService.model.KalturaService.KalturaService;
import com.github.kaltura.automation.KalturaCompatibilityService.model.KalturaXml;
import com.github.kaltura.automation.KalturaCompatibilityService.model.serviceController.CompareClientXmlRequest;
import com.github.kaltura.automation.KalturaCompatibilityService.model.serviceController.CompareClientXmlResponse;
import com.github.kaltura.automation.KalturaCompatibilityService.utils.XmlConverter;
import com.github.kaltura.automation.KalturaCompatibilityService.utils.excel.ExcelUtils;
import com.github.kaltura.automation.KalturaCompatibilityService.utils.serialization.KalturaClassesSerializationUtils;
import com.google.common.collect.MapDifference;
import com.google.common.collect.Maps;
import com.vdurmont.semver4j.Semver;
import io.swagger.v3.oas.models.OpenAPI;
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
@RequestMapping(value = "/clientlib/tester")
public class CompatibilityServiceController {

    private static Logger logger = LoggerFactory.getLogger(CompatibilityServiceController.class);
    Map<String, KalturaClasses.KalturaClass> kalturaClassesMap = new HashMap<>();
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

    @PostMapping(path = "/compareXml", consumes = "application/json", produces = "application/json")
    @ResponseBody
    public CompareClientXmlResponse compareXml(@RequestBody CompareClientXmlRequest compareClientXmlRequest) throws IOException {
        CompareClientXmlResponse resp = new CompareClientXmlResponse();
        KalturaXml previousXml = xmlConverter.xmlToObject(compareClientXmlRequest.getPreviousXmlUrl());
        KalturaXml currentXml = xmlConverter.xmlToObject(compareClientXmlRequest.getCurrentXmlUrl());
        Semver previousCandidate = new Semver(previousXml.getApiVersion());
        if (previousCandidate.isGreaterThan(currentXml.getApiVersion())) {
            KalturaXml temp = previousXml;
            previousXml = currentXml;
            currentXml = temp;

        }

        resp.setPreviousApiVersion(previousXml.getApiVersion());
        resp.setCurrentApiVersion(currentXml.getApiVersion());

        Map<String, MapDifference.ValueDifference<KalturaError>> errorDifferences =
                findDifferencesBetweenErrors(
                        getKalturaErrorsMap(previousXml.getKalturaErrors().getKalturaErrors()),
                        getKalturaErrorsMap(currentXml.getKalturaErrors().getKalturaErrors()));

        resp.getRed().addAndGet(errorDifferences.size());
        resp.setDetailsList(getErrorDetails(errorDifferences));

        Map<String, MapDifference.ValueDifference<KalturaEnum>> enumDifferences =
                findDifferencesBetweenEnums(
                        getKalturaEnumsMap(previousXml.getKalturaEnums().getKalturaEnums()),
                        getKalturaEnumsMap(currentXml.getKalturaEnums().getKalturaEnums()));

        resp.getRed().addAndGet(enumDifferences.size());
        resp.setDetailsList(getEnumDetails(enumDifferences));


        Map<String, MapDifference.ValueDifference<KalturaClasses.KalturaClass>> classDifferences =
                kalturaClassesSerializationUtils.findDifferencesBetweenClasses(
                        getKalturaClassesMap(previousXml.getKalturaClasses().getKalturaClass()),
                        getKalturaClassesMap(currentXml.getKalturaClasses().getKalturaClass()));

        resp.getRed().addAndGet(classDifferences.size());
        resp.setDetailsList(kalturaClassesSerializationUtils.getClassDetails(classDifferences));


        xmlConverter.saveKalturaServices(previousXml.getKalturaServices().getKalturaServices());
        xmlConverter.saveKalturaErrors(previousXml.getKalturaErrors().getKalturaErrors());

        return resp;

    }


    public List<CompareClientXmlResponse.Details> getEnumDetails(Map<String, MapDifference.ValueDifference<KalturaEnum>> valueDifferenceMap) {
        List<CompareClientXmlResponse.Details> detailsList = new ArrayList<>();
        valueDifferenceMap.forEach((k, v) -> {
            CompareClientXmlResponse.Details details = new CompareClientXmlResponse.Details();
            details.setObjectName("(enum) " + k);
            List<CompareClientXmlResponse.Details.Differences> differencesList = new ArrayList<>();
            v.leftValue().diff(v.rightValue()).forEach(d -> {
                CompareClientXmlResponse.Details.Differences differences = new CompareClientXmlResponse.Details.Differences();
                differences.setFieldName(d.getFieldName());
                differences.setPreviousValue(d.getLeft().toString());
                differences.setCurrentValue(d.getRight().toString());
                differencesList.add(differences);
            });
            details.setDifferencesList(differencesList);
            detailsList.add(details);
        });
        return detailsList;
    }


    public List<CompareClientXmlResponse.Details> getErrorDetails(Map<String, MapDifference.ValueDifference<KalturaError>> valueDifferenceMap) {
        List<CompareClientXmlResponse.Details> detailsList = new ArrayList<>();
        valueDifferenceMap.forEach((k, v) -> {
            CompareClientXmlResponse.Details details = new CompareClientXmlResponse.Details();
            details.setObjectName("(error) " + k);
            List<CompareClientXmlResponse.Details.Differences> differencesList = new ArrayList<>();
            v.leftValue().diff(v.rightValue()).forEach(d -> {
                CompareClientXmlResponse.Details.Differences differences = new CompareClientXmlResponse.Details.Differences();
                differences.setFieldName(d.getFieldName());
                differences.setPreviousValue(d.getLeft().toString());
                differences.setCurrentValue(d.getRight().toString());
                differencesList.add(differences);
            });
            details.setDifferencesList(differencesList);
            detailsList.add(details);
        });
        return detailsList;
    }


    private Map<String, MapDifference.ValueDifference<KalturaEnum>> findDifferencesBetweenEnums(
            Map<String, KalturaEnum> stringKalturaEnumMap1, Map<String, KalturaEnum> stringKalturaEnumMap2) throws IOException {
        MapDifference<String, KalturaEnum> diff = Maps.difference(stringKalturaEnumMap1, stringKalturaEnumMap2);
        return diff.entriesDiffering();
    }

    private Map<String, MapDifference.ValueDifference<KalturaError>> findDifferencesBetweenErrors(
            Map<String, KalturaError> stringKalturaErrorMap1, Map<String, KalturaError> stringKalturaErrorMap2) throws IOException {
        MapDifference<String, KalturaError> diff = Maps.difference(stringKalturaErrorMap1, stringKalturaErrorMap2);
        return diff.entriesDiffering();
    }

    private Map<String, KalturaEnum> getKalturaEnumsMap(List<KalturaEnum> kalturaEnums) throws IOException {
        return kalturaEnums.stream().collect(Collectors.toMap(KalturaEnum::getEnumName, e -> e));
    }

    private Map<String, KalturaService> getKalturaServicesMap(List<KalturaService> kalturaServices) throws IOException {
        return kalturaServices.stream().collect(Collectors.toMap(KalturaService::getServiceName, s -> s));
    }

    private Map<String, KalturaClasses.KalturaClass> getKalturaClassesMap(List<KalturaClasses.KalturaClass> kalturaClasses) throws IOException {
        return kalturaClasses.stream().collect(Collectors.toMap(KalturaClasses.KalturaClass::getClassName, c -> c));
    }

    private Map<String, KalturaError> getKalturaErrorsMap(List<KalturaError> kalturaErrors) throws IOException {
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
//
//    @GetMapping(path = "/getClassCount")
//    public Long getClassCount() {
//        return classesService.count();
//    }
//
//    @GetMapping(path = "/getEnumList")
//    public List<Enums> getEnumList() {
//        return enumsService.findAll();
//    }
//
//    @GetMapping(path = "/getClassList")
//    public List<Classes> getClassList() {
//        return classesService.findAll();
//    }
//
//
//    @GetMapping(path = "/getClassByName")
//    public List<String> getClassByName(@RequestParam String name) {
//        return classesService.findByName(name);
//    }


}
