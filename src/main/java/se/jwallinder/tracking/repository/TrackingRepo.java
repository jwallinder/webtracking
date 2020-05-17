package se.jwallinder.tracking.repository;

import org.springframework.stereotype.Component;
import se.jwallinder.tracking.model.Report;
import se.jwallinder.tracking.model.TrackingReportData;
import se.jwallinder.tracking.model.WebTracking;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class TrackingRepo {

    List<WebTracking> repo;

    public TrackingRepo(List<WebTracking> repo) {
        this.repo = repo;
    }

    public TrackingRepo() {
        repo = new ArrayList<WebTracking>();
    }

    public boolean add(WebTracking tracking) {
        return repo.add(tracking);
    }

    public List<WebTracking> getAll() {
        return repo;
    }

    public List<WebTracking> getAll(LocalDateTime after, LocalDateTime before) {
        return repo.stream()
                .filter(tracking -> before == null || tracking.getTimestamp().isBefore(before))
                .filter(tracking -> after == null || tracking.getTimestamp().isAfter(after))
                .collect(Collectors.toList());
    }

    public Report getSummary(LocalDateTime after, LocalDateTime before) {

        //group by url
        Map<String, List<WebTracking>> groupByUrl = repo.stream()
                .filter(tracking -> before == null || tracking.getTimestamp().isBefore(before))
                .filter(tracking -> after == null || tracking.getTimestamp().isAfter(after))
                .collect(Collectors.groupingBy(WebTracking::getUrl));


        List<TrackingReportData> summary = groupByUrl.entrySet().stream()
                .map(byUrl -> {
                    int views = byUrl.getValue().size();
                    int users = (int) byUrl.getValue().stream().map(item -> item.getUserid()).distinct().count();
                    return new TrackingReportData(byUrl.getKey(), views, users);
                })
                .collect(Collectors.toList());

        return new Report(summary);
    }

    public Report getSummary() {
        return this.getSummary(null, null);

    }
}
