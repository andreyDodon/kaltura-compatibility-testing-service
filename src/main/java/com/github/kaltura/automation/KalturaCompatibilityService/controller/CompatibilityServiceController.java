package com.github.kaltura.automation.KalturaCompatibilityService.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.jsonSchema.JsonSchema;
import com.fasterxml.jackson.module.jsonSchema.types.*;
import com.github.kaltura.automation.KalturaCompatibilityService.XmlConverter;
import com.github.kaltura.automation.KalturaCompatibilityService.db.model.Classes;
import com.github.kaltura.automation.KalturaCompatibilityService.db.model.Enums;
import com.github.kaltura.automation.KalturaCompatibilityService.db.service.ClassesService;
import com.github.kaltura.automation.KalturaCompatibilityService.db.service.EnumsService;
import com.github.kaltura.automation.KalturaCompatibilityService.model.KalturaClass.KalturaClasses;
import com.github.kaltura.automation.KalturaCompatibilityService.model.KalturaEnum.KalturaEnum;
import com.github.kaltura.automation.KalturaCompatibilityService.model.KalturaService.KalturaService;
import com.github.kaltura.automation.KalturaCompatibilityService.model.KalturaXml;
import com.github.kaltura.automation.KalturaCompatibilityService.model.serviceController.CompareClientXmlRequest;
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


    @PostMapping(path = "/parseClientXml", consumes = "application/json", produces = "application/json")
    public void parseClientXmlAndSaveToDb(@RequestBody CompareClientXmlRequest compareClientXmlRequest) throws IOException {
        xmlConverter.xmlToObjectSaveToDb(compareClientXmlRequest.getClientXmlUrl());
    }

    @PostMapping(path = "/compareXml", consumes = "application/json", produces = "application/json")
    public Map<String, MapDifference.ValueDifference<KalturaEnum>> compareXml(@RequestBody CompareClientXmlRequest compareClientXmlRequest) throws IOException {

        Map<String, MapDifference.ValueDifference<KalturaEnum>> enumDiff = findDifferencesBetweenEnums(compareClientXmlRequest.getClientXmlUrl(), compareClientXmlRequest.getCompatibleToUrl());

        KalturaXml kalturaXml = xmlConverter.xmlToObject(compareClientXmlRequest.getClientXmlUrl());
        xmlConverter.saveKalturaServices(kalturaXml.getKalturaServices().getKalturaServices());

        classesService.deleteAll();
        getKalturaClassesMap(compareClientXmlRequest.getClientXmlUrl()).forEach((k, v) -> {
            ObjectSchema objectSchema = getObjectSchema(v);
            xmlConverter.saveKalturaClassSchema(k, v.getClassDescription(), objectSchema);
            objectSchemas.add(objectSchema);
        });

        return enumDiff;

    }

    private Map<String, KalturaClasses.KalturaClass> getKalturaClassesMap(URL xmlUrl) throws IOException {
        kalturaClassesMap.clear();
        kalturaClassesMap = xmlConverter.xmlToObject(xmlUrl).
                getKalturaClasses().getKalturaClass().stream().collect(Collectors.toMap(KalturaClasses.KalturaClass::getClassName, c -> c));
        return kalturaClassesMap;
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

    private ObjectSchema getObjectSchema(KalturaClasses.KalturaClass c) {
        ObjectSchema objectSchema = new ObjectSchema();
        final Map<String, JsonSchema> objectProperties = new HashMap<>();

        c.getClassProperties().forEach(p -> {
            if (p.getPropertyType().equals("int") || p.getPropertyType().equals("bigint")) {
                objectProperties.put(p.getPropertyName(), getSchema(new IntegerSchema(), p));
            }
            if (p.getPropertyType().equals("string")) {
                objectProperties.put(p.getPropertyName(), getSchema(new StringSchema(), p));
            }
            if (p.getPropertyType().equals("bool")) {
                objectProperties.put(p.getPropertyName(), getSchema(new BooleanSchema(), p));
            }
            if (p.getPropertyType().equals("float")) {
                objectProperties.put(p.getPropertyName(), getSchema(new NumberSchema(), p));
            }
            if (p.getPropertyType().equals("array")) {
                objectProperties.put(p.getPropertyName(), getSchema(new ArraySchema(), p));
            }
            if (p.getPropertyType().startsWith("Kaltura")) {
                if (!p.getPropertyType().equals(c.getClassName())) {
                    objectSchema.addSchemaDependency(p.getPropertyType(),
                            getObjectSchema(kalturaClassesMap.get(p.getPropertyType())));
                }
                JsonSchema schema = getSchema(new ObjectSchema(), p);
                schema.set$ref("#/dependencies/" + p.getPropertyType());
                objectProperties.put(p.getPropertyName(), schema);
            }


        });
        objectSchema.setId(c.getClassName());
        objectSchema.setDescription(c.getClassDescription());
        objectSchema.setProperties(objectProperties);
        return objectSchema;
    }


    private JsonSchema getSchema(JsonSchema jsonSchema, KalturaClasses.KalturaClass.ClassProperty p) {
        jsonSchema.setDescription(p.getPropertyDescription());
        jsonSchema.setReadonly(Boolean.getBoolean(p.getPropertyReadOnly()));
        return jsonSchema;
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
