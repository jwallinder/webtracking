package se.jwallinder.tracking.model;

import lombok.Data;

@Data
public class TrackingReportData {
    private String url;
    private int pageViews;
    private int visitors;

    public TrackingReportData(String url, int pageViews, int visitors) {
        this.url = url;
        this.pageViews = pageViews;
        this.visitors = visitors;
    }
}
