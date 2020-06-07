package com.github.kaltura.automation.KalturaCompatibilityService.model.KalturaClass;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.kaltura.automation.KalturaCompatibilityService.model.KalturaEnum.EnumConst;
import org.apache.commons.lang3.builder.DiffBuilder;
import org.apache.commons.lang3.builder.DiffResult;
import org.apache.commons.lang3.builder.Diffable;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author andrey.dodon - 22/04/2020
 */
@XmlRootElement(name = "classes")
@XmlAccessorType(XmlAccessType.FIELD)
public class KalturaClasses {

    @XmlElement(name = "class")
    private List<KalturaClass> kalturaClass = null;

    public List<KalturaClass> getKalturaClass() {
        return kalturaClass;
    }

    public void setKalturaClass(List<KalturaClass> kalturaClass) {
        this.kalturaClass = kalturaClass;
    }








}
