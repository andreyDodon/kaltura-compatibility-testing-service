package com.github.kaltura.automation.KalturaCompatibilityService.model.KalturaEnum;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Objects;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "const")
public class EnumConst {

    @JsonProperty(required = true)
    @XmlAttribute(name = "name", required = true)
    private String constName;

    @JsonProperty(required = true)
    @XmlAttribute(name = "value", required = true)
    private String constValue;

    public String getConstName() {
        return constName;
    }

    public void setConstName(String constName) {
        this.constName = constName;
    }

    public String getConstValue() {
        return constValue;
    }

    public void setConstValue(String constValue) {
        this.constValue = constValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EnumConst)) return false;
        EnumConst other = (EnumConst) o;
        return this.constName.equals(other.constName) && this.constValue.equals(other.constValue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(constName, constValue);
    }
}