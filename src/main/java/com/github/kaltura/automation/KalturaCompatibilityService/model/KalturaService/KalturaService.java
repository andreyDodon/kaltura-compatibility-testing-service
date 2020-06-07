package com.github.kaltura.automation.KalturaCompatibilityService.model.KalturaService;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.kaltura.automation.KalturaCompatibilityService.model.KalturaEnum.EnumConst;
import org.apache.commons.lang3.builder.DiffBuilder;
import org.apache.commons.lang3.builder.DiffResult;
import org.apache.commons.lang3.builder.Diffable;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.xml.bind.annotation.*;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author andrey.dodon - 27/04/2020
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "service")
public class KalturaService implements Diffable<KalturaService> {

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

    @Override
    public DiffResult diff(KalturaService other) {
        DiffBuilder db = new DiffBuilder(this, other, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("service.id", this.getServiceId(), other.getServiceId())
                .append("service.name", this.getServiceName(), other.getServiceName());
        if (!this.serviceName.equals(other.serviceName)) {
            db.append(String.format("WARNING - service result %s type is different than expected - %s",
                    other.serviceName,
                    this.serviceName), this.serviceName, other.serviceName);
        }
        else if(other.serviceActions.size() < this.serviceActions.size()){
            db.append(String.format("ERROR - service %s actions in current client is less (%d) than expected - (%d))",
                    other.serviceName,
                    other.serviceActions.size(), this.serviceActions.size()),
                    other.serviceActions.stream().map(ServiceAction::getActionName).collect(Collectors.joining(",")),
                    this.serviceActions.stream().map(ServiceAction::getActionName).collect(Collectors.joining(",")));
            //other.enumConsts.stream().map(EnumConst::getConstName).collect(Collectors.joining(",")))
        }
        return db.build();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        KalturaService that = (KalturaService) o;
        return serviceName.equals(that.serviceName) &&
                serviceId.equals(that.serviceId) &&
                Objects.equals(serviceActions, that.serviceActions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(serviceName, serviceId, serviceActions);
    }
}
