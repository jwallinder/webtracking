package se.jwallinder.tracking;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import se.jwallinder.tracking.model.Report;
import se.jwallinder.tracking.model.WebTracking;
import se.jwallinder.tracking.repository.TrackingRepo;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;


@RestController
public class ReportController {

    private final TrackingRepo repo;

    public ReportController(TrackingRepo repo) {
        this.repo = repo;
    }

    @GetMapping("/summary")
    public Report getSummary(@RequestParam(required = false) LocalDateTime after, @RequestParam(required = false) LocalDateTime before) {
        if (after == null && before == null) {
            return repo.getSummary();
        }

        return repo.getSummary(after, before);
    }


    @GetMapping("/allVisits")
    public List<WebTracking> getFullReport(@RequestParam(required = false) String after, @RequestParam(required = false) String before) {
        if (after == null && before == null) {
            return repo.getAll();
        }

        LocalDateTime afterLDT = after == null ? null : LocalDateTime.parse(after);
        LocalDateTime beforeLDT = before == null ? null : LocalDateTime.parse(before);
        return repo.getAll(afterLDT, beforeLDT);
    }


}
