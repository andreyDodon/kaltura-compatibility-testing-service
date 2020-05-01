package com.github.kaltura.automation.KalturaCompatibilityService.model.KalturaClass;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.DiffBuilder;
import org.apache.commons.lang3.builder.DiffResult;
import org.apache.commons.lang3.builder.Diffable;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author andrey.dodon - 22/04/2020
 */
@XmlRootElement(name = "classes")
@XmlAccessorType(XmlAccessType.FIELD)
public class KalturaClasses {

    @XmlElement(name = "class")
    private List<KalturaClass> kalturaClass = null;

    public List<KalturaClass> getKalturaClass() {
        return kalturaClass;
    }

    public void setKalturaClass(List<KalturaClass> kalturaClass) {
        this.kalturaClass = kalturaClass;
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlRootElement(name = "class")
    public static class KalturaClass implements Diffable<KalturaClass> {

        @JsonProperty(required = true)
        @XmlAttribute(name = "name", required = true)
        private String className;

        @JsonProperty(required = true)
        @XmlAttribute(name = "description", required = true)
        private String classDescription;

        @JsonProperty
        @XmlAttribute(name = "abstract")
        //@XmlJavaTypeAdapter(BooleanAdapter.class)
        private String isAbstract = "0";

        @JsonProperty
        @XmlAttribute(name = "base")
        private String base = "";

        @XmlElement(name = "property")
        private List<ClassProperty> classProperties = new ArrayList<>();

        public String getClassName() {
            return className;
        }

        public void setClassName(String className) {
            this.className = className;
        }

        public String getClassDescription() {
            return classDescription;
        }

        public void setClassDescription(String classDescription) {
            this.classDescription = classDescription;
        }

        public List<ClassProperty> getClassProperties() {
            return classProperties;
        }

        public void setClassProperties(List<ClassProperty> classProperties) {
            this.classProperties = classProperties;
        }

        public String getIsAbstract() {
            return isAbstract;
        }

        public void setIsAbstract(String isAbstract) {
            this.isAbstract = isAbstract;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            KalturaClass kalturaClass = (KalturaClass) o;
            return Objects.equals(className, kalturaClass.className) &&
                    Objects.equals(classDescription, kalturaClass.classDescription) &&
                    Objects.equals(classProperties, kalturaClass.classProperties);
        }

        @Override
        public int hashCode() {
            return Objects.hash(className, classDescription, classProperties);
        }

        public String getBase() {
            return base;
        }

        public void setBase(String base) {
            this.base = base;
        }

        @Override
        public DiffResult diff(KalturaClass other) {

            DiffBuilder db = new DiffBuilder(this, other, ToStringStyle.SHORT_PREFIX_STYLE)
                    .append("class.name", this.className, other.className)
                    .append("class.description", this.classDescription, other.classDescription)
                    .append("class.abstract", this.isAbstract, other.isAbstract)
                    .append("class.base", this.base, other.base);
            if (other.getClassProperties().size() > this.getClassProperties().size()) {
                db.append("class properties more than expected", this.classProperties.size(), other.classProperties.size());
            } else if (other.getClassProperties().size() < this.getClassProperties().size()) {
                db.append("class properties less than expected", this.classProperties.size(), other.classProperties.size());
            } else if (this.getClassProperties().size() == other.getClassProperties().size()) {
                for (int i = 0; i < this.getClassProperties().size(); i++) {
                    db.append("class.properties", this.getClassProperties().get(i).diff(other.getClassProperties().get(i)));
                }
            }

            return db.build();
        }

//        public void checkCompatibility(KalturaClass other) {
//            Map<String, Integer> compatibilityResult = Map.of("RED",0, "YELLOW", 0);
//            DiffBuilder db = new DiffBuilder(this, other, ToStringStyle.SHORT_PREFIX_STYLE)
//                    .append("ClassName", this.className, other.className)
//                    .append("ClassDescription", this.classDescription, other.classDescription)
//                    .append("IsAbstract", this.isAbstract, other.isAbstract)
//                    .append("Base", this.base, other.base);
//            if (this.classProperties.size() > other.classProperties.size()) {
//                compatibilityResult.merge("RED",1, Integer::sum);
//            }
//            if (other.classProperties.size() > this.classProperties.size()) {
//                compatibilityResult.merge("YELLOW",1, Integer::sum);
//            }
//            if(!other.classProperties.containsAll(this.classProperties)){
//                classProperties.forEach(cp -> {
//                    db.append("", cp.propertyName, )
//                });
//            }
//
//        }


        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlRootElement(name = "property")
        public static class ClassProperty implements Diffable<ClassProperty> {

            @JsonProperty(required = true)
            @XmlAttribute(name = "name", required = true)
            private String propertyName;

            @JsonProperty(required = true)
            @XmlAttribute(name = "type", required = true)
            private String propertyType;

            @JsonProperty(required = true)
            @XmlAttribute(name = "description", required = true)
            private String propertyDescription;

            @JsonProperty
            @XmlAttribute(name = "readOnly")
            private String propertyReadOnly = "";

            @JsonProperty
            @XmlAttribute(name = "insertOnly")
            private String propertyInsertOnly = "";

            @JsonProperty
            @XmlAttribute(name = "valuesMinValue")
            private String valuesMinValue = "";

            public String getPropertyName() {
                return propertyName;
            }

            public void setPropertyName(String propertyName) {
                this.propertyName = propertyName;
            }

            public String getPropertyType() {
                return propertyType;
            }

            public void setPropertyType(String propertyType) {
                this.propertyType = propertyType;
            }

            public String getPropertyDescription() {
                return propertyDescription;
            }

            public void setPropertyDescription(String propertyDescription) {
                this.propertyDescription = propertyDescription;
            }

            public String getPropertyReadOnly() {
                return propertyReadOnly;
            }

            public void setPropertyReadOnly(String propertyReadOnly) {
                this.propertyReadOnly = propertyReadOnly;
            }

            public String getPropertyInsertOnly() {
                return propertyInsertOnly;
            }

            public void setPropertyInsertOnly(String propertyInsertOnly) {
                this.propertyInsertOnly = propertyInsertOnly;
            }

            @Override
            public boolean equals(Object o) {
                if (this == o) return true;
                if (o == null || getClass() != o.getClass()) return false;
                ClassProperty classProperty = (ClassProperty) o;
                return Objects.equals(propertyName, classProperty.propertyName) &&
                        Objects.equals(propertyDescription, classProperty.propertyDescription) &&
                        Objects.equals(propertyInsertOnly, classProperty.propertyInsertOnly) &&
                        Objects.equals(propertyReadOnly, classProperty.propertyReadOnly) &&
                        Objects.equals(propertyType, classProperty.propertyType);
            }

            @Override
            public int hashCode() {
                return Objects.hash(propertyName, propertyDescription, propertyInsertOnly, propertyReadOnly, propertyType);
            }


            public String getValuesMinValue() {
                return valuesMinValue;
            }

            public void setValuesMinValue(String valuesMinValue) {
                this.valuesMinValue = valuesMinValue;
            }

            @Override
            public DiffResult diff(ClassProperty other) {
                DiffBuilder db = new DiffBuilder(this, other, ToStringStyle.SHORT_PREFIX_STYLE)
                        .append(this.propertyName, this.propertyName, other.propertyName)
                        .append(this.propertyName + ".description", this.propertyDescription, other.propertyDescription)
                        .append(this.propertyName + ".insertOnly", this.propertyInsertOnly, other.propertyInsertOnly)
                        .append(this.propertyName + ".readOnly", this.propertyReadOnly, other.propertyReadOnly)
                        .append(this.propertyName + ".type", this.propertyType, other.propertyType)
                        .append(this.propertyName + ".valuesMinValue", this.valuesMinValue, other.valuesMinValue);
                return db.build();
            }
        }
    }
}
