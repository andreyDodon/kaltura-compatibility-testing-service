package com.github.kaltura.automation.KalturaCompatibilityService.model.serviceController.response;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author andrey.dodon - 27/04/2020
 */
@JsonPropertyOrder({"previousApiVersion", "currentApiVersion", "red", "yellow", "redDetails", "yellowDetails"})
public class CompareClientXmlResponse {

    private  List<Differences> redDetails = new ArrayList<>();
    private  List<Differences> yellowDetails = new ArrayList<>();
    private AtomicInteger yellow = new AtomicInteger(0);
    private AtomicInteger red = new AtomicInteger(0);
    private String currentApiVersion;
    private String previousApiVersion;


    public AtomicInteger getYellow() {
        return yellow;
    }

    public void setYellow(AtomicInteger yellow) {
        this.yellow = yellow;
    }

    public AtomicInteger getRed() {
        return red;
    }

    public void setRed(AtomicInteger red) {
        this.red = red;
    }

    public String getCurrentApiVersion() {
        return currentApiVersion;
    }

    public void setCurrentApiVersion(String currentApiVersion) {
        this.currentApiVersion = currentApiVersion;
    }

    public String getPreviousApiVersion() {
        return previousApiVersion;
    }

    public CompareClientXmlResponse setPreviousApiVersion(String previousApiVersion) {
        this.previousApiVersion = previousApiVersion;
        return this;
    }


    public List<Differences> getRedDetails() {
        return redDetails;
    }

    public void setRedDetails(List<Differences> redDetails) {
        this.redDetails = redDetails;
    }

    public List<Differences> getYellowDetails() {
        return yellowDetails;
    }

    public void setYellowDetails(List<Differences> yellowDetails) {
        this.yellowDetails = yellowDetails;
    }
}
