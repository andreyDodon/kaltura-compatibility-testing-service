package com.github.kaltura.automation.KalturaCompatibilityService.model.KalturaError;

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
 * @author andrey.dodon - 01/05/2020
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "error")
public class KalturaError implements Diffable<KalturaError> {

    @JsonProperty(required = true)
    @XmlAttribute(name = "name", required = true)
    private String errorName;

    @JsonProperty(required = true)
    @XmlAttribute(name = "code", required = true)
    private String errorCode;

    @JsonProperty
    @XmlAttribute(name = "description")
    private String errorDescription="";

    @JsonProperty
    @XmlAttribute(name = "message")
    private String errorMessage="";

    @XmlElement(name = "parameter")
    private List<ErrorParameter> errorParameters = new ArrayList<>();

    public String getErrorName() {
        return errorName;
    }

    public void setErrorName(String errorName) {
        this.errorName = errorName;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorDescription() {
        return errorDescription;
    }

    public void setErrorDescription(String errorDescription) {
        this.errorDescription = errorDescription;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public List<ErrorParameter> getErrorParameters() {
        return errorParameters;
    }

    public void setErrorParameters(List<ErrorParameter> errorParameters) {
        this.errorParameters = errorParameters;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof KalturaError)) return false;
        KalturaError other = (KalturaError) o;
        boolean errorParametersEquals =
                (this.errorParameters == null && other.errorParameters == null)
                        || (this.errorCode != null && this.errorCode.equals(other.errorCode));
        return this.errorDescription.equals(other.errorDescription)
                && this.errorMessage.equals(other.errorMessage)
                && this.errorName.equals(((KalturaError) o).errorName)
                && errorParametersEquals;
    }

    @Override
    public int hashCode() {
        return Objects.hash(errorName, errorCode, errorDescription, errorMessage, errorParameters);
    }

    @Override
    public DiffResult diff(KalturaError other) {
        DiffBuilder db = new DiffBuilder(this, other, ToStringStyle.SHORT_PREFIX_STYLE)
                .append(String.format("ERROR - error %s name is missing ",
                        other.errorName), this.errorName, other.errorName)
                .append(String.format("ERROR - error %s code is missing ",
                        other.errorName), this.errorCode, other.errorCode)
                .append(String.format("ERROR - error %s description is missing ",
                        other.errorName), this.errorDescription, other.errorDescription)
                .append(String.format("ERROR - error %s message is missing ",
                        other.errorName), this.errorMessage, other.errorMessage);
        if (other.getErrorParameters().size() > this.getErrorParameters().size()) {
            db.append(String.format("WARNING - error %s contains more parameters - (%d) than expected - (%d)",
                    other.errorName,
                    other.errorParameters.size(),
                    this.errorParameters.size() ),
                    this.errorParameters.stream().map(ErrorParameter::getParameterName).collect(Collectors.joining(",")),
                    other.errorParameters.stream().map(ErrorParameter::getParameterName).collect(Collectors.joining(",")));
        } else if (other.getErrorParameters().size() < this.getErrorParameters().size()) {
            db.append(String.format("ERROR - error %s contains less parameters - (%d) than expected - (%d)",
                    other.errorName,
                    other.errorParameters.size(),
                    this.errorParameters.size() ),
                    this.errorParameters.stream().map(ErrorParameter::getParameterName).collect(Collectors.joining(",")),
                    other.errorParameters.stream().map(ErrorParameter::getParameterName).collect(Collectors.joining(",")));
        } else if (this.getErrorParameters().size() == other.getErrorParameters().size()) {
            for (int i = 0; i < this.getErrorParameters().size(); i++) {
                db.append(String.format("ERROR - error %s parameter is missing ",
                        other.errorName),
                        this.getErrorParameters().get(i).diff(other.getErrorParameters().get(i)));
            }
        }

        return db.build();
    }
}
