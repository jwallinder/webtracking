package se.jwallinder.tracking.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import se.jwallinder.tracking.model.Report;
import se.jwallinder.tracking.model.TrackingReportData;
import se.jwallinder.tracking.model.WebTracking;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RepositoryTest {

    private TrackingRepo repo = new TrackingRepo();

    @BeforeEach
    void setUp() {
        WebTracking t0 = WebTracking.builder().timestamp(LocalDateTime.parse("2013-09-01T09:00:00")).url("/contact.html").userid("12345").build();
        WebTracking t1 = WebTracking.builder().timestamp(LocalDateTime.parse("2013-09-01T09:00:00")).url("/contact.html").userid("12346").build();
        WebTracking t2 = WebTracking.builder().timestamp(LocalDateTime.parse("2013-09-01T10:00:00")).url("/contact.html").userid("12345").build();
        WebTracking t3 = WebTracking.builder().timestamp(LocalDateTime.parse("2013-09-01T10:01:00")).url("/about.html").userid("12347").build();
        WebTracking t4 = WebTracking.builder().timestamp(LocalDateTime.parse("2013-09-01T11:00:00")).url("/contact.html").userid("12347").build();


        repo.add(t0);
        repo.add(t1);
        repo.add(t2);
        repo.add(t3);
        repo.add(t4);
    }

    @Test
    public void testGetAll() {
        List<WebTracking> trackings = repo.getAll();

        assertEquals(5, trackings.size());
    }

    @Test
    public void testGetAllFromNoData() {
        TrackingRepo emptyRepo = new TrackingRepo();

        assertEquals(0, emptyRepo.getAll().size());
        assertEquals(0, emptyRepo.getAll(LocalDateTime.parse("2013-09-01T09:01:00"), LocalDateTime.parse("2013-09-01T10:59:59")).size());
    };

    @Test
    public void testGetAllAfterAndBefore() {
        List<WebTracking> trackings = repo.getAll(LocalDateTime.parse("2013-09-01T09:01:00"), LocalDateTime.parse("2013-09-01T10:59:59"));

        assertEquals(2, trackings.size());
    }

    @Test
    public void testGetAllAfter() {
        List<WebTracking> trackings = repo.getAll(LocalDateTime.parse("2013-09-01T09:01:00"), null);

        assertEquals(3, trackings.size());
    }

    @Test
    public void testGetAllBefore() {
        List<WebTracking> trackings = repo.getAll(null, LocalDateTime.parse("2013-09-01T10:59:59"));

        assertEquals(4, trackings.size());
    }



    @Test
    void getSummary() {
        Report report = repo.getSummary();

        // figure out where our contacts data and about data are
        TrackingReportData contacts = report.getData().get(0);
        TrackingReportData about = report.getData().get(1);
        if (report.getData().get(1).getUrl().equalsIgnoreCase("/contact.html")) {
            contacts = report.getData().get(1);
            about = report.getData().get(0);
        }

        assertEquals(2, report.getData().size());

        assertEquals(4, contacts.getPageViews());
        assertEquals(3, contacts.getVisitors());

        assertEquals(1, about.getPageViews());
        assertEquals(1, about.getVisitors());

    }
    @Test
    public void testGetSummaryFromNoData() {
        TrackingRepo emptyRepo = new TrackingRepo();

        assertEquals(0, emptyRepo.getSummary().getData().size());
        assertEquals(0, emptyRepo.getSummary(LocalDateTime.parse("2013-09-01T09:01:00"), LocalDateTime.parse("2013-09-01T10:59:59")).getData().size());
    };
    @Test
    public void testSummaryAfterBefore() {
        Report report = repo.getSummary(LocalDateTime.parse("2013-09-01T09:01:00"), LocalDateTime.parse("2013-09-01T10:59:59"));

        // figure out where our contacts data and about data are
        TrackingReportData contacts = report.getData().get(0);
        TrackingReportData about = report.getData().get(1);
        if (report.getData().get(1).getUrl().equalsIgnoreCase("/contact.html")) {
            contacts = report.getData().get(1);
            about = report.getData().get(0);
        }

        assertEquals(2, report.getData().size());

        assertEquals(1, contacts.getPageViews());
        assertEquals(1, contacts.getVisitors());

        assertEquals(1, about.getPageViews());
        assertEquals(1, about.getVisitors());
    }

    @Test
    public void testSummaryAfter() {
        Report report = repo.getSummary(LocalDateTime.parse("2013-09-01T09:01:00"), null);

        // figure out where our contacts data and about data are
        TrackingReportData contacts = report.getData().get(0);
        TrackingReportData about = report.getData().get(1);
        if (report.getData().get(1).getUrl().equalsIgnoreCase("/contact.html")) {
            contacts = report.getData().get(1);
            about = report.getData().get(0);
        }

        assertEquals(2, report.getData().size());

        assertEquals(2, contacts.getPageViews());
        assertEquals(2, contacts.getVisitors());

        assertEquals(1, about.getPageViews());
        assertEquals(1, about.getVisitors());
    }

    @Test
    public void testSummaryBefore() {
        Report report = repo.getSummary(null, LocalDateTime.parse("2013-09-01T10:59:59"));

        // figure out where our contacts data and about data are
        TrackingReportData contacts = report.getData().get(0);
        TrackingReportData about = report.getData().get(1);
        if (report.getData().get(1).getUrl().equalsIgnoreCase("/contact.html")) {
            contacts = report.getData().get(1);
            about = report.getData().get(0);
        }

        assertEquals(2, report.getData().size());

        assertEquals(3, contacts.getPageViews());
        assertEquals(2, contacts.getVisitors());

        assertEquals(1, about.getPageViews());
        assertEquals(1, about.getVisitors());
    }


}
