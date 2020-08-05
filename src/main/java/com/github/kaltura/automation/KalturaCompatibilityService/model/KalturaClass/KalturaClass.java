package com.github.kaltura.automation.KalturaCompatibilityService.model.KalturaClass;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.DiffBuilder;
import org.apache.commons.lang3.builder.DiffResult;
import org.apache.commons.lang3.builder.Diffable;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "class")
public class KalturaClass implements Diffable<KalturaClass> {

    @JsonProperty(required = true)
    @XmlAttribute(name = "name", required = true)
    private String className;

    @JsonProperty(required = true)
    @XmlAttribute(name = "description", required = true)
    private String classDescription;

    @JsonProperty
    @XmlAttribute(name = "abstract")
    //@XmlJavaTypeAdapter(BooleanAdapter.class)
    private String isAbstract = "0";

    @JsonProperty
    @XmlAttribute(name = "base")
    private String base = "";

    @XmlElement(name = "property")
    private List<ClassProperty> classProperties = new ArrayList<>();

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getClassDescription() {
        return classDescription;
    }

    public void setClassDescription(String classDescription) {
        this.classDescription = classDescription;
    }

    public List<ClassProperty> getClassProperties() {
        return classProperties;
    }

    public void setClassProperties(List<ClassProperty> classProperties) {
        this.classProperties = classProperties;
    }

    public String getIsAbstract() {
        return isAbstract;
    }

    public void setIsAbstract(String isAbstract) {
        this.isAbstract = isAbstract;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        KalturaClass kalturaClass = (KalturaClass) o;
        return Objects.equals(className, kalturaClass.className) &&
                Objects.equals(classDescription, kalturaClass.classDescription) &&
                Objects.equals(classProperties, kalturaClass.classProperties);
    }

    @Override
    public int hashCode() {
        return Objects.hash(className, classDescription, classProperties);
    }

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }

    @Override
    public DiffResult diff(KalturaClass other) {

        DiffBuilder db = new DiffBuilder(this, other, ToStringStyle.SHORT_PREFIX_STYLE)
                .append(String.format("ERROR - class %s name is missing ",
                        other.className), this.className, other.className)
                .append(String.format("ERROR - class %s description is missing ",
                        other.classDescription), this.classDescription, other.classDescription)
                .append("class.abstract", this.isAbstract, other.isAbstract)
                .append("class.base", this.base, other.base);
        if (other.getClassProperties().size() > this.getClassProperties().size()) {
            db.append(String.format("WARNING - class %s contains more properties - (%d) than expected - (%d)",
                    other.className,
                    other.classProperties.size(),
                    this.classProperties.size()),
                    this.classProperties.stream().map(ClassProperty::getPropertyName).collect(Collectors.joining(",")),
                    other.classProperties.stream().map(ClassProperty::getPropertyName).collect(Collectors.joining(",")));
        } else if (other.getClassProperties().size() < this.getClassProperties().size()) {
            db.append(String.format("ERROR - class %s contains less properties - (%d) than expected - (%d)",
                    other.className,
                    other.classProperties.size(),
                    this.classProperties.size()),
                    this.classProperties.stream().map(ClassProperty::getPropertyName).collect(Collectors.joining(",")),
                    other.classProperties.stream().map(ClassProperty::getPropertyName).collect(Collectors.joining(",")));
        } else if (this.getClassProperties().size() == other.getClassProperties().size()) {
            for (int i = 0; i < this.getClassProperties().size(); i++) {
                db.append(String.format("WARNING - class %s property is modified", other.className),
                        this.getClassProperties().get(i).diff(other.getClassProperties().get(i)));
            }
        }

        return db.build();
    }
}