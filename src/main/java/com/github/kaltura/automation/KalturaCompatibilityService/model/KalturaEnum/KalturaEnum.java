package com.github.kaltura.automation.KalturaCompatibilityService.model.KalturaEnum;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.xml.bind.annotation.*;
import java.util.List;
import java.util.Objects;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "enum")
public class KalturaEnum {

    @JsonProperty(required = true)
    @XmlAttribute(name = "name", required = true)
    private String enumName;

    @JsonProperty(required = true)
    @XmlAttribute(required = true)
    private String enumType;

    @XmlElement(name = "const")
    private List<EnumConst> enumConsts = null;

    public String getEnumName() {
        return enumName;
    }

    public void setEnumName(String enumName) {
        this.enumName = enumName;
    }

    public String getEnumType() {
        return enumType;
    }

    public void setEnumType(String enumType) {
        this.enumType = enumType;
    }

    public List<EnumConst> getEnumConsts() {
        return enumConsts;
    }

    public void setEnumConsts(List<EnumConst> enumConsts) {
        this.enumConsts = enumConsts;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof KalturaEnum)) return false;
        KalturaEnum other = (KalturaEnum) o;
        boolean enumConstsEquals =
                (this.enumConsts == null && other.enumConsts == null)
                        || (this.enumConsts != null && this.enumConsts.equals(other.enumConsts));
        return this.enumName.equals(other.enumName) && this.enumType.equals(other.enumType) && enumConstsEquals;
    }

    @Override
    public int hashCode() {
        return Objects.hash(enumName, enumType, enumConsts);
    }


}