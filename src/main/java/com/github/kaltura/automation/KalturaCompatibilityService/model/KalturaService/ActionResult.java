package com.github.kaltura.automation.KalturaCompatibilityService.model.KalturaService;

import com.fasterxml.jackson.annotation.JsonProperty;
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
 * @author andrey.dodon - 27/04/2020
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "result")
public class ActionResult implements Diffable<ActionResult> {

    @JsonProperty(required = true)
    @XmlAttribute(name = "type", required = true)
    private String resultType = "";

    public String getResultType() {
        return resultType;
    }

    public void setResultType(String resultType) {
        this.resultType = resultType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ActionResult that = (ActionResult) o;
        return resultType.equals(that.resultType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(resultType);
    }

    @Override
    public DiffResult diff(ActionResult other) {
        DiffBuilder db = new DiffBuilder(this, other, ToStringStyle.SHORT_PREFIX_STYLE);
        if (!this.resultType.equals(other.resultType)) {
            db.append(String.format("WARNING - service result %s type is different than expected - %s",
                    other.resultType,
                    this.resultType), this.resultType, other.resultType);
        }
        return db.build();
    }
}
