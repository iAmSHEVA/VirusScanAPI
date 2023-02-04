package org.baraa;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public class VirusInfo {
    private String scans;

    private String McAfee;
    private String detected;
    private String result;

    public String getScans() {
        return scans;
    }

    public void setScans(String scans) {
        this.scans = scans;
    }

    public String getMcAfee() {
        return McAfee;
    }

    public void setMcAfee(String city) {
        this.McAfee = city;
    }

    public String getDetected() {
        return detected;
    }

    public void setDetected(String detected) {
        this.detected = detected;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    @SuppressWarnings("unchecked")
    @JsonProperty("scans")
    private void unpackNested(Map<String, Object> scans) {
        this.McAfee = (String) scans.get("McAfee");
    }

    @SuppressWarnings("unchecked")
    @JsonProperty("McAfee")
    private void unnpackNested(Map<String, Object> McAfee) {
        this.detected = (String) McAfee.get("detected");
        this.result = (String) McAfee.get("result");
    }


    @Override
    public String toString() {
        return "Virus scan using McAfee:\n" +
                "Detected= " + detected +
                "\nVirus type=" + result.replace("\'", "");
    }
}