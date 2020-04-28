package com.github.kaltura.automation.KalturaCompatibilityService.utils.serialization;

import com.fasterxml.jackson.module.jsonSchema.JsonSchema;
import com.fasterxml.jackson.module.jsonSchema.types.*;
import com.github.kaltura.automation.KalturaCompatibilityService.XmlConverter;
import com.github.kaltura.automation.KalturaCompatibilityService.model.KalturaClass.KalturaClasses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author andrey.dodon - 27/04/2020
 */
@Component
public class KalturaClassesUtils {


    @Autowired
    private XmlConverter xmlConverter;
    private Map<String, KalturaClasses.KalturaClass> kalturaClassesMap;


    private void populateHashMapWithClasses(URL xmlUrl) throws IOException {
        kalturaClassesMap = xmlConverter.xmlToObject(xmlUrl).
                getKalturaClasses().getKalturaClass().stream().collect(Collectors.toMap(KalturaClasses.KalturaClass::getClassName, c -> c));
    }


    public ObjectSchema getObjectSchema(KalturaClasses.KalturaClass c) throws IOException {
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
                    try {
                        objectSchema.addSchemaDependency(p.getPropertyType(),
                                getObjectSchema(kalturaClassesMap.get(p.getPropertyType())));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
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


}
