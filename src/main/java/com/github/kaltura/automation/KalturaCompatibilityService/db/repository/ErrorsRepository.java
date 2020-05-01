package com.github.kaltura.automation.KalturaCompatibilityService.db.repository;

import com.github.kaltura.automation.KalturaCompatibilityService.db.model.Errors;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ErrorsRepository extends CrudRepository<Errors, Long> {

    @Query(value = "select e.DATA from ERRORS e where e.name = :name", nativeQuery = true)
    List<String> findByName(@Param("name") String chars);

}
