package com.github.kaltura.automation.KalturaCompatibilityService.model.KalturaService;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.xml.bind.annotation.*;
import java.util.List;
import java.util.Objects;

/**
 * @author andrey.dodon - 27/04/2020
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "action")
public class ServiceAction {

    @JsonProperty(required = true)
    @XmlAttribute(name = "name", required = true)
    private String actionName;

    @JsonProperty(required = true)
    @XmlAttribute(name = "enableInMultiRequest", required = true)
    private String enableInMultiRequest;

    @JsonProperty(required = true)
    @XmlAttribute(name = "supportedRequestFormats", required = true)
    private String supportedRequestFormats;

    @JsonProperty(required = true)
    @XmlAttribute(name = "supportedResponseFormats", required = true)
    private String supportedResponseFormats;

    @JsonProperty
    @XmlAttribute(name = "description")
    private String description;

    @JsonProperty
    @XmlAttribute(name = "sessionRequired")
    private String sessionRequired;

    @JsonProperty
    @XmlElement(name = "param")
    private List<ActionParam> actionParams;

    @JsonProperty
    @XmlElement(name = "throws")
    private List<ActionThrows> actionThrows;

    @JsonProperty
    @XmlElement(name = "result")
    private ActionResult actionResults;


    public String getActionName() {
        return actionName;
    }

    public void setActionName(String actionName) {
        this.actionName = actionName;
    }

    public String getEnableInMultiRequest() {
        return enableInMultiRequest;
    }

    public void setEnableInMultiRequest(String enableInMultiRequest) {
        this.enableInMultiRequest = enableInMultiRequest;
    }

    public String getSupportedRequestFormats() {
        return supportedRequestFormats;
    }

    public void setSupportedRequestFormats(String supportedRequestFormats) {
        this.supportedRequestFormats = supportedRequestFormats;
    }

    public String getSupportedResponseFormats() {
        return supportedResponseFormats;
    }

    public void setSupportedResponseFormats(String supportedResponseFormats) {
        this.supportedResponseFormats = supportedResponseFormats;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSessionRequired() {
        return sessionRequired;
    }

    public void setSessionRequired(String sessionRequired) {
        this.sessionRequired = sessionRequired;
    }

    public List<ActionParam> getActionParams() {
        return actionParams;
    }

    public void setActionParams(List<ActionParam> actionParams) {
        this.actionParams = actionParams;
    }

    public ActionResult getActionResults() {
        return actionResults;
    }

    public void setActionResults(ActionResult actionResults) {
        this.actionResults = actionResults;
    }

    public List<ActionThrows> getActionThrows() {
        return actionThrows;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ServiceAction that = (ServiceAction) o;
        return actionName.equals(that.actionName) &&
                Objects.equals(enableInMultiRequest, that.enableInMultiRequest) &&
                Objects.equals(supportedRequestFormats, that.supportedRequestFormats) &&
                Objects.equals(supportedResponseFormats, that.supportedResponseFormats) &&
                Objects.equals(description, that.description) &&
                Objects.equals(sessionRequired, that.sessionRequired) &&
                Objects.equals(actionParams, that.actionParams) &&
                Objects.equals(actionThrows, that.actionThrows) &&
                Objects.equals(actionResults, that.actionResults);
    }

    @Override
    public int hashCode() {
        return Objects.hash(actionName, enableInMultiRequest, supportedRequestFormats, supportedResponseFormats, description, sessionRequired, actionParams, actionThrows, actionResults);
    }

    public void setActionThrows(List<ActionThrows> actionThrows) {
        this.actionThrows = actionThrows;
    }
}
