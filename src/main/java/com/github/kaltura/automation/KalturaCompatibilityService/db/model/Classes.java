package com.github.kaltura.automation.KalturaCompatibilityService.db.model;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.Objects;

/**
 * @author andrey.dodon - 22/04/2020
 */
@Entity
public class Classes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Size(max = 500)
    @Column(unique = true)
    private String name;
    @Size(max = 500)
    private String description;
    @Size(max = 60000)
    private String data;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Classes enums = (Classes) o;
        return Objects.equals(id, enums.id) &&
                Objects.equals(name, enums.name) &&
                Objects.equals(description, enums.description) &&
                Objects.equals(data, enums.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, data);
    }


}
