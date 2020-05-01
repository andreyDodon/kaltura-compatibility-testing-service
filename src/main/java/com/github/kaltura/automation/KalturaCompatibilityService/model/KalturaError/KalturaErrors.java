package com.github.kaltura.automation.KalturaCompatibilityService.model.KalturaError;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * @author andrey.dodon - 01/05/2020
 */
@XmlRootElement(name = "errors")
@XmlAccessorType(XmlAccessType.FIELD)
public class KalturaErrors {

    @XmlElement(name = "error")
    private List<KalturaError> kalturaErrors = null;

    public List<KalturaError> getKalturaErrors() {
        return kalturaErrors;
    }

    public void setKalturaErrors(List<KalturaError> kalturaErrors) {
        this.kalturaErrors = kalturaErrors;
    }
}
