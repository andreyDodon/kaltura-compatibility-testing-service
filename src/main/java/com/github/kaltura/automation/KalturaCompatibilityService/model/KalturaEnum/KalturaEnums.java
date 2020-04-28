package com.github.kaltura.automation.KalturaCompatibilityService.model.KalturaEnum;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "enums")
@XmlAccessorType(XmlAccessType.FIELD)
public class KalturaEnums {
    @XmlElement(name = "enum")
    private List<KalturaEnum> kalturaEnums = null;

    public List<KalturaEnum> getKalturaEnums() {
        return kalturaEnums;
    }

    public void setKalturaEnums(List<KalturaEnum> kalturaEnums) {
        this.kalturaEnums = kalturaEnums;
    }


}