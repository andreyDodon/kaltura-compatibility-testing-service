package com.github.kaltura.automation.KalturaCompatibilityService.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.jsonSchema.JsonSchema;
import com.fasterxml.jackson.module.jsonSchema.JsonSchemaGenerator;
import com.github.kaltura.automation.KalturaCompatibilityService.db.model.Classes;
import com.github.kaltura.automation.KalturaCompatibilityService.db.model.Enums;
import com.github.kaltura.automation.KalturaCompatibilityService.db.model.Errors;
import com.github.kaltura.automation.KalturaCompatibilityService.db.model.Services;
import com.github.kaltura.automation.KalturaCompatibilityService.db.service.ClassesService;
import com.github.kaltura.automation.KalturaCompatibilityService.db.service.EnumsService;
import com.github.kaltura.automation.KalturaCompatibilityService.db.service.ErrorsService;
import com.github.kaltura.automation.KalturaCompatibilityService.db.service.ServiceService;
import com.github.kaltura.automation.KalturaCompatibilityService.model.KalturaClass.KalturaClasses;
import com.github.kaltura.automation.KalturaCompatibilityService.model.KalturaEnum.EnumConst;
import com.github.kaltura.automation.KalturaCompatibilityService.model.KalturaEnum.KalturaEnum;
import com.github.kaltura.automation.KalturaCompatibilityService.model.KalturaEnum.KalturaEnums;
import com.github.kaltura.automation.KalturaCompatibilityService.model.KalturaError.KalturaError;
import com.github.kaltura.automation.KalturaCompatibilityService.model.KalturaService.KalturaService;
import com.github.kaltura.automation.KalturaCompatibilityService.model.KalturaXml;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.stereotype.Component;

import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * @author andrey.dodon - 19/04/2020
 */
@Component
public class XmlConverter {

    private static Logger logger = LoggerFactory.getLogger(XmlConverter.class);

    @Autowired
    private Jaxb2Marshaller jaxb2Marshaller;
    @Autowired
    private EnumsService enumsService;
    @Autowired
    private ClassesService classesService;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private ServiceService serviceService;
    @Autowired
    private ErrorsService errorsService;


    public void saveKalturaEnums(List<KalturaEnum> kalturaEnumList) {
        enumsService.deleteAll();
        kalturaEnumList.forEach(e -> {
            Enums enums = new Enums();
            enums.setName(e.getEnumName());
            enums.setType(e.getEnumType());
            enums.setData(writeValueAsString(e));
            enumsService.saveOrUpdate(enums);
        });
    }


    public void saveKalturaClasses(List<KalturaClasses.KalturaClass> kalturaClassList) {
        classesService.deleteAll();
        kalturaClassList.forEach(c -> {
            Classes classes = new Classes();
            classes.setName(c.getClassName());
            classes.setDescription(c.getClassDescription());
            classes.setData(writeValueAsString(c));
            classesService.saveOrUpdate(classes);
        });
    }


    public void saveKalturaServices(List<KalturaService> kalturaServices) {
        serviceService.deleteAll();
        kalturaServices.forEach(s -> {
            Services services = new Services();
            services.setName(s.getServiceName());
            services.setDescription(s.getServiceId());
            services.setData(writeValueAsString(s));
            serviceService.saveOrUpdate(services);
        });
    }


    public void saveKalturaErrors(List<KalturaError> kalturaErrors) {
        errorsService.deleteAll();
        kalturaErrors.forEach(e -> {
            Errors errors = new Errors();
            errors.setName(e.getErrorName());
            errors.setCode(Integer.valueOf(e.getErrorCode()));
            errors.setDescription(e.getErrorDescription());
            errors.setData(writeValueAsString(e));
            errorsService.saveOrUpdate(errors);
        });
    }


    public String writeValueAsString(Object value) {
        try {
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(value);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }


    public KalturaXml xmlToObject(URL url) throws IOException {
        return (KalturaXml) jaxb2Marshaller.unmarshal(new StreamSource(url.openStream()));
    }

    public void xmlToObjectSaveToDb(URL url) throws IOException {
        KalturaXml kalturaXml = (KalturaXml) jaxb2Marshaller.unmarshal(new StreamSource(url.openStream()));
        saveKalturaEnums(kalturaXml.getKalturaEnums().getKalturaEnums());
        saveKalturaClasses(kalturaXml.getKalturaClasses().getKalturaClass());
    }


    public JsonSchema getJsonSchema(Class<?> type) throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonSchemaGenerator schemaGen = new JsonSchemaGenerator(mapper);
        return schemaGen.generateSchema(type);
    }

    public void objectToXml(Object graph, String fileName) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(fileName)) {
            jaxb2Marshaller.marshal(graph, new StreamResult(fos));
        }
    }


    public KalturaXml getKalturaXmlObjectForTest() {
        KalturaXml kalturaXml = new KalturaXml();
        kalturaXml.setApiVersion("5.3.4.27857");
        kalturaXml.setGeneratedDate("1587046068");
        KalturaEnums test = new KalturaEnums();
        test.setKalturaEnums(new ArrayList<>());
        KalturaEnum kEnum = new KalturaEnum();
        kEnum.setEnumConsts(new ArrayList<>());
        kEnum.getEnumConsts().add(new EnumConst());
        test.getKalturaEnums().add(kEnum);
        kalturaXml.setKalturaEnums(test);
        return kalturaXml;
    }


}
