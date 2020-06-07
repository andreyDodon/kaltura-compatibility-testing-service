package com.github.kaltura.automation.KalturaCompatibilityService.model;

import com.github.kaltura.automation.KalturaCompatibilityService.model.KalturaClass.KalturaClass;
import com.github.kaltura.automation.KalturaCompatibilityService.model.KalturaClass.KalturaClasses;
import com.github.kaltura.automation.KalturaCompatibilityService.model.KalturaEnum.KalturaEnum;
import com.github.kaltura.automation.KalturaCompatibilityService.model.KalturaEnum.KalturaEnums;
import com.github.kaltura.automation.KalturaCompatibilityService.model.KalturaError.KalturaError;
import com.github.kaltura.automation.KalturaCompatibilityService.model.KalturaError.KalturaErrors;
import com.github.kaltura.automation.KalturaCompatibilityService.model.KalturaService.KalturaService;
import com.github.kaltura.automation.KalturaCompatibilityService.model.KalturaService.KalturaServices;

import javax.xml.bind.annotation.*;
import java.util.List;

@XmlRootElement(name = "xml")
@XmlAccessorType(XmlAccessType.FIELD)
public class KalturaXml {


    @XmlAttribute(name = "apiVersion")
    private String apiVersion;

    @XmlAttribute(name = "generatedDate")
    private String generatedDate;

    @XmlElement(name = "enums")
    private KalturaEnums kalturaEnums;

    @XmlElement(name = "classes")
    private KalturaClasses kalturaClasses;

    @XmlElement(name = "services")
    private KalturaServices kalturaServices;

    @XmlElement(name = "errors")
    private KalturaErrors kalturaErrors;


    public String getApiVersion() {
        return apiVersion;
    }

    public void setApiVersion(String apiVersion) {
        this.apiVersion = apiVersion;
    }


    public String getGeneratedDate() {
        return generatedDate;
    }

    public void setGeneratedDate(String generatedDate) {
        this.generatedDate = generatedDate;
    }


    public List<KalturaEnum> getKalturaEnums() {
        return kalturaEnums.getKalturaEnums();
    }

    public void setKalturaEnums(KalturaEnums kalturaEnums) {
        this.kalturaEnums = kalturaEnums;
    }

    public List<KalturaClass> getKalturaClasses() {
        return kalturaClasses.getKalturaClass();
    }

    public void setKalturaClasses(KalturaClasses kalturaClasses) {
        this.kalturaClasses = kalturaClasses;
    }

    public List<KalturaService> getKalturaServices() {
        return kalturaServices.getKalturaServices();
    }

    public void setKalturaServices(KalturaServices kalturaServices) {
        this.kalturaServices = kalturaServices;
    }

    public List<KalturaError> getKalturaErrors() {
        return kalturaErrors.getKalturaErrors();
    }

    public void setKalturaErrors(KalturaErrors kalturaErrors) {
        this.kalturaErrors = kalturaErrors;
    }
}