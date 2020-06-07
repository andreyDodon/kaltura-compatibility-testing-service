package com.github.kaltura.automation.KalturaCompatibilityService.model.serviceController.response;

public class Differences {

            private String description;
            private String previousValue;
            private String currentValue;

            public String getDescription() {
                return description;
            }

            public void setDescription(String description) {
                this.description = description;
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