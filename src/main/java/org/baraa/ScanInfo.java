package org.baraa;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public class ScanInfo {
    private String verbose_msg;

    private String permalink;
    private String scan_id;

    public String getVerbose_msg() {
        return verbose_msg;
    }

    public void setVerbose_msg(String verbose_msg) {
        this.verbose_msg = verbose_msg;
    }

    public String getPermalink() {
        return permalink;
    }

    public void setPermalink(String permalink) {
        this.permalink = permalink;
    }

    public String getScan_id() {
        return scan_id;
    }

    public void setScan_id(String scan_id) {
        this.scan_id = scan_id;
    }

    @SuppressWarnings("unchecked")
    @JsonProperty("scan_id")
    private void unpackNested(Map<String, Object> scan_id) {
        this.scan_id = (String) scan_id.get("scan_id");
    }

    @SuppressWarnings("unchecked")
    @JsonProperty("permalink")
    private void unnpackNested(Map<String, Object> permalink) {
        this.permalink = (String) permalink.get("permalink");
    }

    @SuppressWarnings("unchecked")
    @JsonProperty("verbose_msg")
    private void unnnpackNested(Map<String, Object> verbose_msg) {
        this.verbose_msg = (String) verbose_msg.get("verbose_msg");
    }


    @Override
    public String toString() {
        return  verbose_msg.replace("\"","") +
                "\nReport result is here: " + permalink.replace("\"","") +
                "\nScan id= " + scan_id.replace("\"","");
    }

}
