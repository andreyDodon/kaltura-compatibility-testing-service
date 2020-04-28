package com.github.kaltura.automation.KalturaCompatibilityService.db.repository;

import com.github.kaltura.automation.KalturaCompatibilityService.db.model.Services;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ServicesRepository extends CrudRepository<Services, Long> {

    @Query(value = "select c.DATA from SERVICES c where c.name = ?1", nativeQuery = true)
    List<String> findByName(@Param("name") String chars);

}
