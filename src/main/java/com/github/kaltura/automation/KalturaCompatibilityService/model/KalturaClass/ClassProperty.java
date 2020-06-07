package com.github.kaltura.automation.KalturaCompatibilityService.model.KalturaClass;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.DiffBuilder;
import org.apache.commons.lang3.builder.DiffResult;
import org.apache.commons.lang3.builder.Diffable;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Objects;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "property")
public class ClassProperty implements Diffable<ClassProperty> {

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