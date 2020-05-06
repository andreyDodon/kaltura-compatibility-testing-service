package com.github.kaltura.automation.KalturaCompatibilityService.model.serviceController;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author andrey.dodon - 27/04/2020
 */
public class CompareClientXmlResponse {

    private AtomicInteger yellow = new AtomicInteger(0);
    private AtomicInteger red = new AtomicInteger(0);
    private List<Details> detailsList = new ArrayList<>();
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

    public List<Details> getDetailsList() {
        return detailsList;
    }

    public void setDetailsList(List<Details> detailsList) {
        this.detailsList.addAll(detailsList);
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

    public void setPreviousApiVersion(String previousApiVersion) {
        this.previousApiVersion = previousApiVersion;
    }

    public static class Details {

        private String objectName;
        private List<Differences> differencesList = new ArrayList<>();


        public String getObjectName() {
            return objectName;
        }

        public void setObjectName(String objectName) {
            this.objectName = objectName;
        }

        public List<Differences> getDifferencesList() {
            return differencesList;
        }

        public void setDifferencesList(List<Differences> differencesList) {
            this.differencesList = differencesList;
        }

        public static class Differences {

            private String fieldName;
            private String previousValue;
            private String currentValue;

            public String getFieldName() {
                return fieldName;
            }

            public void setFieldName(String fieldName) {
                this.fieldName = fieldName;
            }

            public String getPreviousValue() {
                return previousValue;
            }

            public void setPreviousValue(String previousValue) {
                this.previousValue = previousValue;
            }

            public String getCurrentValue() {
                return currentValue;
            }

            public void setCurrentValue(String currentValue) {
                this.currentValue = currentValue;
            }
        }
    }
}
