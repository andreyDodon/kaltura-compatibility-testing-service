package com.github.kaltura.automation.KalturaCompatibilityService.model.KalturaEnum;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.DiffBuilder;
import org.apache.commons.lang3.builder.DiffResult;
import org.apache.commons.lang3.builder.Diffable;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.xml.bind.annotation.*;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "enum")
public class KalturaEnum implements Diffable<KalturaEnum> {

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


    @Override
    public DiffResult diff(KalturaEnum other) {
        DiffBuilder db = new DiffBuilder(this, other, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("enum.name", this.enumName, other.enumName)
                .append("enum.type", this.enumType, other.enumType);
        if (other.getEnumConsts().size() > this.getEnumConsts().size()) {
            db.append(String.format("WARNING - enum %s contains more constants - (%d) than expected - (%d)",
                    other.enumName,
                    other.enumConsts.size(),
                    this.enumConsts.size() ),
                    this.enumConsts.stream().map(EnumConst::getConstName).collect(Collectors.joining(",")),
                    other.enumConsts.stream().map(EnumConst::getConstName).collect(Collectors.joining(",")));
        } else if (other.getEnumConsts().size() < this.getEnumConsts().size()) {
            db.append(String.format("ERROR - enum %s contains less constants - (%d) than expected - (%d)",
                    other.enumName,
                    other.enumConsts.size(),
                    this.enumConsts.size() ),
                    this.enumConsts.stream().map(EnumConst::getConstName).collect(Collectors.joining(",")),
                    other.enumConsts.stream().map(EnumConst::getConstName).collect(Collectors.joining(",")));
        } else if (this.getEnumConsts().size() == other.getEnumConsts().size()) {
            for (int i = 0; i < this.getEnumConsts().size(); i++) {
                db.append("ERROR - enum.const", this.getEnumConsts().get(i).diff(other.getEnumConsts().get(i)));
            }
        }
        return db.build();
    }
}