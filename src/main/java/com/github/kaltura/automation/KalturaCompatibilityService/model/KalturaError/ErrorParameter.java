package com.github.kaltura.automation.KalturaCompatibilityService.model.KalturaError;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.kaltura.automation.KalturaCompatibilityService.model.KalturaEnum.EnumConst;
import org.apache.commons.lang3.builder.DiffBuilder;
import org.apache.commons.lang3.builder.DiffResult;
import org.apache.commons.lang3.builder.Diffable;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Objects;

/**
 * @author andrey.dodon - 01/05/2020
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "parameter")
public class ErrorParameter implements Diffable<ErrorParameter> {

    @JsonProperty(required = true)
    @XmlAttribute(name = "name", required = true)
    private String parameterName;

    public String getParameterName() {
        return parameterName;
    }

    public void setParameterName(String parameterName) {
        this.parameterName = parameterName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ErrorParameter)) return false;
        ErrorParameter other = (ErrorParameter) o;
        return this.parameterName.equals(other.parameterName) ;
    }

    @Override
    public int hashCode() {
        return Objects.hash(parameterName);
    }

    @Override
    public DiffResult diff(ErrorParameter other) {
        DiffBuilder db = new DiffBuilder(this, other, ToStringStyle.SHORT_PREFIX_STYLE)
                .append(this.parameterName, this.parameterName, other.parameterName);
        return db.build();
    }
}
