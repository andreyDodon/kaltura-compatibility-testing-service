package com.github.kaltura.automation.KalturaCompatibilityService.db.service;

import com.github.kaltura.automation.KalturaCompatibilityService.db.model.Classes;

import java.util.List;

public interface IClassService {
    List<String> findByName(String name);

}
