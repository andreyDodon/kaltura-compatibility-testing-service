package com.github.kaltura.automation.KalturaCompatibilityService.utils.serialization;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.jsonSchema.JsonSchema;
import com.fasterxml.jackson.module.jsonSchema.JsonSchemaGenerator;
import com.fasterxml.jackson.module.jsonSchema.types.*;
import com.github.kaltura.automation.KalturaCompatibilityService.db.model.Classes;
import com.github.kaltura.automation.KalturaCompatibilityService.db.service.ClassesService;
import com.github.kaltura.automation.KalturaCompatibilityService.model.KalturaClass.ClassProperty;
import com.github.kaltura.automation.KalturaCompatibilityService.model.KalturaClass.KalturaClass;
import com.github.kaltura.automation.KalturaCompatibilityService.model.KalturaClass.KalturaClasses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author andrey.dodon - 27/04/2020
 */
@Component
public class KalturaClassesSerializationUtils extends KalturaSerialization {

    @Autowired
    private ClassesService classesService;


    private Map<String, KalturaClass> kalturaClassesMap;

    public Map<String, KalturaClass> getKalturaClasses(URL xmlUrl) throws IOException {
        kalturaClassesMap = getKalturaXmlObject(xmlUrl).
                getKalturaClasses().stream().collect(Collectors.toMap(KalturaClass::getClassName, c -> c));
        return kalturaClassesMap;
    }


    public Map<String, ObjectSchema> getKalturaClassObjectSchemas(Map<String, KalturaClass> kalturaClassesMap) {
        Map<String, ObjectSchema> classSchemasMap = new HashMap<>();
        kalturaClassesMap.forEach((k, v) -> {
            classSchemasMap.put(k, getKalturaClassObjectSchema(v));
        });
        return classSchemasMap;
    }


    public void saveKalturaClasses(List<KalturaClass> kalturaClassList) {
        classesService.deleteAll();
        kalturaClassList.forEach(c -> {
            Classes classes = new Classes();
            classes.setName(c.getClassName());
            classes.setDescription(c.getClassDescription());
            classes.setData(writeValueAsString(c));
            classesService.saveOrUpdate(classes);
        });
    }


    public void saveKalturaClassObjectSchemas(Map<String, KalturaClass> kalturaClassesMap) {
        classesService.deleteAll();
        kalturaClassesMap.forEach((k, v) -> {
            ObjectSchema objectSchema = getKalturaClassObjectSchema(v);
            saveKalturaClassSchema(k, v.getClassDescription(), objectSchema);
        });
    }


    public void saveKalturaClassSchema(String className, String classDescription, ObjectSchema objectSchema) {
        Classes classes = new Classes();
        classes.setName(className);
        classes.setDescription(classDescription);
        classes.setData(writeValueAsString(objectSchema));
        classesService.saveOrUpdate(classes);
    }


    private ObjectSchema getKalturaClassObjectSchema(KalturaClass c) {
        ObjectSchema objectSchema = new ObjectSchema();
        final Map<String, JsonSchema> classSchemasMap = new HashMap<>();

        c.getClassProperties().forEach(p -> {
            if (p.getPropertyType().equals(PROPERTY_TYPE.INT.asLowerCase()) || p.getPropertyType().equals(PROPERTY_TYPE.BIGINT.asLowerCase())) {
                classSchemasMap.put(p.getPropertyName(), getSchema(IntegerSchema.class, p));
            }
            if (p.getPropertyType().equals(PROPERTY_TYPE.STRING.asLowerCase())) {
                classSchemasMap.put(p.getPropertyName(), getSchema(new StringSchema(), p));
            }
            if (p.getPropertyType().equals(PROPERTY_TYPE.BOOL.asLowerCase())) {
                classSchemasMap.put(p.getPropertyName(), getSchema(new BooleanSchema(), p));
            }
            if (p.getPropertyType().equals(PROPERTY_TYPE.FLOAT.asLowerCase())) {
                classSchemasMap.put(p.getPropertyName(), getSchema(new NumberSchema(), p));
            }
            if (p.getPropertyType().equals(PROPERTY_TYPE.ARRAY.asLowerCase())) {
                classSchemasMap.put(p.getPropertyName(), getSchema(new ArraySchema(), p));
            }
            if (p.getPropertyType().startsWith(PROPERTY_TYPE.KALTURA.asLowerCase())) {
                if (!p.getPropertyType().equals(c.getClassName())) {
                    objectSchema.addSchemaDependency(p.getPropertyType(),
                            getKalturaClassObjectSchema(kalturaClassesMap.get(p.getPropertyType())));
                }
                JsonSchema schema = getSchema(new ObjectSchema(), p);
                schema.set$ref("#/dependencies/" + p.getPropertyType());
                classSchemasMap.put(p.getPropertyName(), schema);
            }

        });
        objectSchema.setId(c.getClassName());
        objectSchema.setDescription(c.getClassDescription());
        objectSchema.setProperties(classSchemasMap);
        return objectSchema;
    }


    private JsonSchema getSchema(JsonSchema jsonSchema, ClassProperty p) {
        jsonSchema.setDescription(p.getPropertyDescription());
        jsonSchema.setReadonly(Boolean.getBoolean(p.getPropertyReadOnly()));
        return jsonSchema;
    }

    public JsonSchema getSchema(Class<?> type, ClassProperty p) {
        JsonSchemaGenerator schemaGen = new JsonSchemaGenerator(new ObjectMapper());
        JsonSchema jsonSchema = null;
        try {
            jsonSchema = schemaGen.generateSchema(type);
            jsonSchema.setDescription(p.getPropertyDescription());
            jsonSchema.setReadonly(Boolean.getBoolean(p.getPropertyReadOnly()));
        } catch (JsonMappingException e) {
            e.printStackTrace();
        }
        return jsonSchema;
    }


    private enum PROPERTY_TYPE {
        INT {
            @Override
            public String asLowerCase() {
                return INT.toString().toLowerCase();
            }
        },
        STRING {
            @Override
            public String asLowerCase() {
                return STRING.toString().toLowerCase();
            }
        },
        BOOL {
            @Override
            public String asLowerCase() {
                return BOOL.toString().toLowerCase();
            }
        },
        FLOAT {
            @Override
            public String asLowerCase() {
                return FLOAT.toString().toLowerCase();
            }
        },
        ARRAY {
            @Override
            public String asLowerCase() {
                return ARRAY.toString().toLowerCase();
            }
        },
        KALTURA {
            @Override
            public String asLowerCase() {
                return KALTURA.toString().toLowerCase();
            }
        },
        BIGINT {
            @Override
            public String asLowerCase() {
                return BIGINT.toString().toLowerCase();
            }
        };

        public abstract String asLowerCase();
    }


}
