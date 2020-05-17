package se.jwallinder.tracking;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseBody;
import se.jwallinder.tracking.model.UserIdGenerator;
import se.jwallinder.tracking.model.WebTracking;
import se.jwallinder.tracking.repository.TrackingRepo;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.time.LocalDateTime;


@Controller
public class ImageController {

    private final UserIdGenerator userIdGenerator;

    private TrackingRepo trackingRepo;

    @Value("classpath:static/tracking.gif")
    private Resource resourceFile;

    public ImageController(UserIdGenerator userIdGenerator, TrackingRepo trackingRepo) {
        this.userIdGenerator = userIdGenerator;
        this.trackingRepo = trackingRepo;
    }


    @GetMapping(
            value = "/image.gif",
            produces = MediaType.IMAGE_GIF_VALUE)
    @ResponseBody
    public Resource getTrackingImage(@CookieValue(name = "trackingCookie", required = false) String cookie,
                                     @RequestHeader(value = "referer", required = false) final String referer,
                                     HttpServletResponse response) {

        String userId;

        //set cookie if not present
        if (cookie == null) {
            userId = userIdGenerator.getNextUserId();
            Cookie newCookie = new Cookie("trackingCookie", userId);
            response.addCookie(newCookie);
        } else {
            userId = cookie;
        }


        String path = "?";
        try {
            path = new URI(referer).getPath();
        } catch (Exception e) {
            //tough luck, but path has a default value :)
        }

        WebTracking tracking = WebTracking.builder()
                .timestamp(LocalDateTime.now())
                .url(path)
                .userid(userId)
                .build();

        trackingRepo.add(tracking);

        return resourceFile;
    }

}
