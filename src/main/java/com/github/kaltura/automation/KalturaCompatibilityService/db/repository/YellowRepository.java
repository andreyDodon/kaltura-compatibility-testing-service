package com.github.kaltura.automation.KalturaCompatibilityService.db.repository;

import com.github.kaltura.automation.KalturaCompatibilityService.db.model.Yellow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface YellowRepository extends JpaRepository<Yellow, Long> {

    @Query(value = "select y.description from YELLOW y where y.name = ?1", nativeQuery = true)
    List<String> findByName(@Param("name") String chars);

}
