package com.github.kaltura.automation.KalturaCompatibilityService.model.KalturaService;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Objects;

/**
 * @author andrey.dodon - 27/04/2020
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "param")
public class ActionParam {

    @JsonProperty(required = true)
    @XmlAttribute(name = "name", required = true)
    private String paramName;

    @JsonProperty(required = true)
    @XmlAttribute(name = "type", required = true)
    private String paramType;

    @JsonProperty
    @XmlAttribute(name = "default")
    private String paramDefault;

    @JsonProperty
    @XmlAttribute(name = "description")
    private String paramDescription;

    @JsonProperty
    @XmlAttribute(name = "optional")
    private String optional;


    public String getParamName() {
        return paramName;
    }

    public void setParamName(String paramName) {
        this.paramName = paramName;
    }

    public String getParamType() {
        return paramType;
    }

    public void setParamType(String paramType) {
        this.paramType = paramType;
    }

    public String getParamDescription() {
        return paramDescription;
    }

    public void setParamDescription(String paramDescription) {
        this.paramDescription = paramDescription;
    }

    public String getOptional() {
        return optional;
    }

    public void setOptional(String optional) {
        this.optional = optional;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ActionParam that = (ActionParam) o;
        return paramName.equals(that.paramName) &&
                paramType.equals(that.paramType) &&
                Objects.equals(paramDefault, that.paramDefault) &&
                Objects.equals(paramDescription, that.paramDescription) &&
                Objects.equals(optional, that.optional);
    }

    @Override
    public int hashCode() {
        return Objects.hash(paramName, paramType, paramDefault, paramDescription, optional);
    }
}
