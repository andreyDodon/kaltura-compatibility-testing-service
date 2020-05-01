package com.github.kaltura.automation.KalturaCompatibilityService.db.repository;

import com.github.kaltura.automation.KalturaCompatibilityService.db.model.Enums;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EnumsRepository extends CrudRepository<Enums, Long> {

    @Query(value = "select e.DATA from ENUMS e where e.name = :name", nativeQuery = true)
    List<Enums> findByName(@Param("name") String chars);

}