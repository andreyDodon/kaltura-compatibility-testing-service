package com.github.kaltura.automation.KalturaCompatibilityService.db.service.interfaces;

import com.github.kaltura.automation.KalturaCompatibilityService.db.model.Enums;

import java.util.List;

/**
 * @author andrey.dodon - 22/04/2020
 */
public interface IEnumService {

    List<Enums> findByName(String name);

}
