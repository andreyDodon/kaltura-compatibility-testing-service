package com.github.kaltura.automation.KalturaCompatibilityService.adapter;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class BooleanAdapter extends XmlAdapter<String, Boolean> {
    @Override
    public Boolean unmarshal(String s) {
        return s != null && s.equals("1");
    }

    @Override
    public String marshal(Boolean c) {
        return c == null ? null : c ? "1" : "0";
    }
}