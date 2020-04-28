package com.github.kaltura.automation.KalturaCompatibilityService.model.serviceController;

import java.net.URL;

/**
 * @author andrey.dodon - 22/04/2020
 */
public class CompareClientXmlRequest {

    private URL clientXmlUrl;
    private URL compatibleToUrl;

    public URL getClientXmlUrl() {
        return clientXmlUrl;
    }

    public void setClientXmlUrl(URL clientXmlUrl) {
        this.clientXmlUrl = clientXmlUrl;
    }

    public URL getCompatibleToUrl() {
        return compatibleToUrl;
    }

    public void setCompatibleToUrl(URL compatibleToUrl) {
        this.compatibleToUrl = compatibleToUrl;
    }
}
