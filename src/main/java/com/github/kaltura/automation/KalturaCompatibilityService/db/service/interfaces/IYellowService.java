package com.github.kaltura.automation.KalturaCompatibilityService.db.service.interfaces;

import java.util.List;

/**
 * @author andrey.dodon - 05/06/2020
 */
public interface IYellowService {
    List<String> findByName(String name);
}
