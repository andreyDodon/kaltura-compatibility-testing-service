package com.github.kaltura.automation.KalturaCompatibilityService.utils.json;

public interface JsonComparator {

    boolean compareValues(Object expected, Object actual);

    boolean compareFields(String expected, String actual);
}