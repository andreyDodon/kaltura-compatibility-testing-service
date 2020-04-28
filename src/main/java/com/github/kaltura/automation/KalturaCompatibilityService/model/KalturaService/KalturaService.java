package com.github.kaltura.automation.KalturaCompatibilityService.model.KalturaService;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.xml.bind.annotation.*;
import java.util.List;

/**
 * @author andrey.dodon - 27/04/2020
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "service")
public class KalturaService {

    @JsonProperty(required = true)
    @XmlAttribute(name = "name", required = true)
    private String serviceName;

    @JsonProperty(required = true)
    @XmlAttribute(name = "id", required = true)
    private String serviceId;

    @XmlElement(name = "action")
    private List<ServiceAction> serviceActions;

    public List<ServiceAction> getServiceActions() {
        return serviceActions;
    }

    public void setServiceActions(List<ServiceAction> serviceActions) {
        this.serviceActions = serviceActions;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }
}
