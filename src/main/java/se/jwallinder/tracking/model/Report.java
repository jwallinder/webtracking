package se.jwallinder.tracking.model;

import java.util.List;

public class Report {

    private  List<TrackingReportData> data;


    public Report(List<TrackingReportData> data) {
        this.data = data;
    }

    public List<TrackingReportData> getData() {
        return data;
    }

    public boolean add(TrackingReportData trackingReportData) {
        return data.add(trackingReportData);
    }
}
