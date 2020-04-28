package com.github.kaltura.automation.KalturaCompatibilityService.model.KalturaClass;

import com.fasterxml.jackson.annotation.JsonProperty;

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
    public static class KalturaClass {

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


        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlRootElement(name = "property")
        public static class ClassProperty {

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
        }
    }
}
