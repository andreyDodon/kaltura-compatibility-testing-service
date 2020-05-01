package com.github.kaltura.automation.KalturaCompatibilityService.model.KalturaError;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author andrey.dodon - 01/05/2020
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "error")
public class KalturaError {

    @JsonProperty(required = true)
    @XmlAttribute(name = "name", required = true)
    private String errorName;

    @JsonProperty(required = true)
    @XmlAttribute(name = "code", required = true)
    private String errorCode;

    @JsonProperty
    @XmlAttribute(name = "description")
    private String errorDescription;

    @JsonProperty
    @XmlAttribute(name = "message")
    private String errorMessage;

    @XmlElement(name = "parameter")
    private List<ErrorParameter> errorParameters = new ArrayList<>();

    public String getErrorName() {
        return errorName;
    }

    public void setErrorName(String errorName) {
        this.errorName = errorName;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorDescription() {
        return errorDescription;
    }

    public void setErrorDescription(String errorDescription) {
        this.errorDescription = errorDescription;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public List<ErrorParameter> getErrorParameters() {
        return errorParameters;
    }

    public void setErrorParameters(List<ErrorParameter> errorParameters) {
        this.errorParameters = errorParameters;
    }
}
