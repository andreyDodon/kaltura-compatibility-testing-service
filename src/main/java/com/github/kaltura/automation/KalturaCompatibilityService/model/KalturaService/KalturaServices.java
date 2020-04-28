package com.github.kaltura.automation.KalturaCompatibilityService.model.KalturaService;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * @author andrey.dodon - 27/04/2020
 */
@XmlRootElement(name = "services")
@XmlAccessorType(XmlAccessType.FIELD)
public class KalturaServices {

    @XmlElement(name = "service")
    private List<KalturaService> kalturaServices;

    public List<KalturaService> getKalturaServices() {
        return kalturaServices;
    }

    public void setKalturaServices(List<KalturaService> kalturaServices) {
        this.kalturaServices = kalturaServices;
    }
}
