package com.github.kaltura.automation.KalturaCompatibilityService.db.model;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.Objects;

/**
 * @author andrey.dodon - 05/06/2020
 */
@Entity
public class Yellow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long yid;
    @Size(max = 500)
    @Column(unique = true)
    private String name;
    @Column(unique = true)
    private String type;
    @Size(max = 500)
    private String description;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getYid() {
        return yid;
    }

    public void setYid(Long yid) {
        this.yid = yid;
    }
}
