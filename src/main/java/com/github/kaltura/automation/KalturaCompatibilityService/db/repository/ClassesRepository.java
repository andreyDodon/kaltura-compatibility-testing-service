package com.github.kaltura.automation.KalturaCompatibilityService.db.repository;

import com.github.kaltura.automation.KalturaCompatibilityService.db.model.Classes;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author andrey.dodon - 22/04/2020
 */
public interface ClassesRepository extends CrudRepository<Classes, Long> {

    @Query(value = "select c.DATA from CLASSES c where c.name = ?1", nativeQuery = true)
    List<String> findByName(@Param("name") String chars);

}
