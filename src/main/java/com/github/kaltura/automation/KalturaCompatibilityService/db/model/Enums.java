package com.github.kaltura.automation.KalturaCompatibilityService.db.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Size;
import java.util.Objects;

@Entity
public class Enums {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Size(max = 500)
    private String name;
    private String type;
    @Size(max = 30000)
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

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Enums enums = (Enums) o;
        return Objects.equals(id, enums.id) &&
                Objects.equals(name, enums.name) &&
                Objects.equals(type, enums.type) &&
                Objects.equals(data, enums.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, type, data);
    }


}