package com.github.kaltura.automation.KalturaCompatibilityService.model.KalturaError;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author andrey.dodon - 01/05/2020
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "parameter")
public class ErrorParameter {

    @JsonProperty(required = true)
    @XmlAttribute(name = "name", required = true)
    private String parameterName;

    public String getParameterName() {
        return parameterName;
    }

    public void setParameterName(String parameterName) {
        this.parameterName = parameterName;
    }
}
