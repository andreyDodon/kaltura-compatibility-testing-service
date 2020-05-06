package com.github.kaltura.automation.KalturaCompatibilityService.model.serviceController;

import java.net.URL;

/**
 * @author andrey.dodon - 22/04/2020
 */
public class CompareClientXmlRequest {

    private URL previousXmlUrl;
    private URL currentXmlUrl;

    public URL getPreviousXmlUrl() {
        return previousXmlUrl;
    }

    public void setPreviousXmlUrl(URL previousXmlUrl) {
        this.previousXmlUrl = previousXmlUrl;
    }

    public URL getCurrentXmlUrl() {
        return currentXmlUrl;
    }

    public void setCurrentXmlUrl(URL currentXmlUrl) {
        this.currentXmlUrl = currentXmlUrl;
    }
}
