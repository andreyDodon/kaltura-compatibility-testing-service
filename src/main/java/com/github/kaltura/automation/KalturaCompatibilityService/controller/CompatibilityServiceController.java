package com.github.kaltura.automation.KalturaCompatibilityService.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.jsonSchema.types.ObjectSchema;
import com.github.kaltura.automation.KalturaCompatibilityService.db.model.Classes;
import com.github.kaltura.automation.KalturaCompatibilityService.db.model.Enums;
import com.github.kaltura.automation.KalturaCompatibilityService.db.service.ClassesService;
import com.github.kaltura.automation.KalturaCompatibilityService.db.service.EnumsService;
import com.github.kaltura.automation.KalturaCompatibilityService.model.KalturaClass.KalturaClasses;
import com.github.kaltura.automation.KalturaCompatibilityService.model.KalturaEnum.KalturaEnum;
import com.github.kaltura.automation.KalturaCompatibilityService.model.KalturaService.KalturaService;
import com.github.kaltura.automation.KalturaCompatibilityService.model.KalturaXml;
import com.github.kaltura.automation.KalturaCompatibilityService.model.serviceController.CompareClientXmlRequest;
import com.github.kaltura.automation.KalturaCompatibilityService.model.serviceController.CompareClientXmlResponse;
import com.github.kaltura.automation.KalturaCompatibilityService.utils.XmlConverter;
import com.github.kaltura.automation.KalturaCompatibilityService.utils.serialization.KalturaClassesSerializationUtils;
import com.google.common.collect.MapDifference;
import com.google.common.collect.Maps;
import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * @author andrey.dodon - 22/04/2020
 */


@RestController
@RequestMapping(value = "/clientlib/tester")
public class CompatibilityServiceController {

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


    @PostMapping(path = "/parseClientXml", consumes = "application/json", produces = "application/json")
    public void parseClientXmlAndSaveToDb(@RequestBody CompareClientXmlRequest compareClientXmlRequest) throws IOException {
        xmlConverter.xmlToObjectSaveToDb(compareClientXmlRequest.getClientXmlUrl());
    }

    @PostMapping(path = "/compareXml", consumes = "application/json", produces = "application/json")
    @ResponseBody
    public CompareClientXmlResponse compareXml(@RequestBody CompareClientXmlRequest compareClientXmlRequest) throws IOException {
        CompareClientXmlResponse resp = new CompareClientXmlResponse();
        KalturaXml kalturaXml = xmlConverter.xmlToObject(compareClientXmlRequest.getClientXmlUrl());

        Map<String, MapDifference.ValueDifference<KalturaEnum>> enumDifferences =
                findDifferencesBetweenEnums(compareClientXmlRequest.getClientXmlUrl(), compareClientXmlRequest.getCompatibleToUrl());

        resp.getRed().addAndGet(enumDifferences.size());
        resp.setDetailsList(getEnumDetails(enumDifferences));

        Map<String, MapDifference.ValueDifference<KalturaClasses.KalturaClass>> classDifferences =
                kalturaClassesSerializationUtils.findDifferencesBetweenClasses(compareClientXmlRequest.getClientXmlUrl(),
                        compareClientXmlRequest.getCompatibleToUrl());

        resp.getRed().addAndGet(classDifferences.size());
        resp.setDetailsList(kalturaClassesSerializationUtils.getClassDetails(classDifferences));


        xmlConverter.saveKalturaServices(kalturaXml.getKalturaServices().getKalturaServices());

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
                differences.setOldValue(d.getLeft().toString());
                differences.setNewValue(d.getRight().toString());
                differencesList.add(differences);
            });
            details.setDifferencesList(differencesList);
            detailsList.add(details);
        });
        return detailsList;
    }




    private Map<String, MapDifference.ValueDifference<KalturaEnum>> findDifferencesBetweenEnums(URL xmlUrl1, URL xmlUrl2) throws IOException {
        MapDifference<String, KalturaEnum> diff = Maps.difference(getKalturaEnumsMap(xmlUrl1), getKalturaEnumsMap(xmlUrl2));
        return diff.entriesDiffering();
    }

    private Map<String, KalturaEnum> getKalturaEnumsMap(URL url) throws IOException {
        return xmlConverter.xmlToObject(url).
                getKalturaEnums().getKalturaEnums().stream().collect(Collectors.toMap(KalturaEnum::getEnumName, e -> e));
    }

    private Map<String, KalturaService> getKalturaServicesMap(URL url) throws IOException {
        return xmlConverter.xmlToObject(url).
                getKalturaServices().getKalturaServices().stream().collect(Collectors.toMap(KalturaService::getServiceName, s -> s));
    }


    @GetMapping(path = "/getEnumCount")
    public Long getEnumCount() {
        return enumsService.count();
    }

    @GetMapping(path = "/getClassCount")
    public Long getClassCount() {
        return classesService.count();
    }

    @GetMapping(path = "/getEnumList")
    public List<Enums> getEnumList() {
        return enumsService.findAll();
    }

    @GetMapping(path = "/getClassList")
    public List<Classes> getClassList() {
        return classesService.findAll();
    }


    @GetMapping(path = "/getClassByName")
    public List<String> getClassByName(@RequestParam String name) {
        return classesService.findByName(name);
    }


}
