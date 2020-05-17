package se.jwallinder.tracking;

import org.hamcrest.CoreMatchers;
import org.hamcrest.Matcher;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import se.jwallinder.tracking.model.WebTracking;
import se.jwallinder.tracking.repository.TrackingRepo;

import javax.servlet.http.Cookie;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ImageControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private TrackingRepo trackingRepo;

    @Test
    void testTrackingForSubseqentRequest() throws Exception {


        //make call twice to ImageController with cookie
        this.mockMvc.perform(get("/image.gif"))
                //.andDo(print())
                .andExpect(status().isOk());

        this.mockMvc.perform(get("/image.gif"))
                //.andDo(print())
                .andExpect(status().isOk());

        verify(trackingRepo, times(2)).add(any(WebTracking.class));
    }

    @Test
    void testTrackingDataForRequest() throws Exception {

        ArgumentCaptor<WebTracking> argumentCaptor = ArgumentCaptor.forClass(WebTracking.class);
        WebTracking tracking;

        Cookie cookie = new Cookie("trackingCookie", "userid-12345");


        //make call to ImageController with cookie
        this.mockMvc.perform(get("/image.gif").cookie(cookie).header("Referer", "http://domain.com/path/page.html"))
                //.andDo(print())
                .andExpect(status().isOk());

        //verify repo.add() was called with the correct data
        verify(trackingRepo).add(argumentCaptor.capture());

        tracking = argumentCaptor.getValue();
        assertEquals("/path/page.html", tracking.getUrl());
        assertEquals("userid-12345", tracking.getUserid());

    }


    @Test
    void testImageIsDelivered() throws Exception {
        this.mockMvc.perform(get("/image.gif"))
                //.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("image/gif"));
        //hard to test content length in a simple way... but it should be 48 bytes
    }

    @Test
    void testCookieIsSetForFirstRequest() throws Exception {

        //make a call to ImageController wo cookie
        //assert response has a cookie, id=12345
        this.mockMvc.perform(get("/image.gif"))
                //.andDo(print())
                .andExpect(status().isOk()) // sanity, tested in testImageIsDelivered
                .andExpect(content().contentType("image/gif")) // sanity, tested in testImageIsDelivered
                .andExpect(cookie().exists("trackingCookie"));

    }

    @Test
    void testCookieIsSetForDifferentUser() throws Exception {

        //make a call to ImageController wo cookie
        //assert response has a cookie, userid=12345
        MvcResult firstreq = this.mockMvc.perform(get("/image.gif"))
                //.andDo(print())
                .andExpect(status().isOk()) // sanity, tested in testImageIsDelivered
                .andExpect(content().contentType("image/gif")) // sanity, tested in testImageIsDelivered
                .andExpect(cookie().exists("trackingCookie"))
                .andReturn();
        Cookie cookie1 = firstreq.getResponse().getCookie("trackingCookie");

        //make a new call to ImageController wo cookie
        //assert response has a cookie, userid != 12345
        this.mockMvc.perform(get("/image.gif"))
                //.andDo(print())
                .andExpect(status().isOk()) // sanity, tested in testImageIsDelivered
                .andExpect(content().contentType("image/gif")) // sanity, tested in testImageIsDelivered
                .andExpect(cookie().exists("trackingCookie"))
                .andExpect(cookie().value("trackingCookie", doesNotContainString(cookie1.getValue())));

    }

    @Test
    void testCookieIsSetOnceForSameUser() throws Exception {

        //make a call to ImageController wo cookie
        //assert response has a cookie, userid=12345
        MvcResult firstreq = this.mockMvc.perform(get("/image.gif"))
                //.andDo(print())
                .andExpect(status().isOk()) // sanity, tested in testImageIsDelivered
                .andExpect(content().contentType("image/gif")) // sanity, tested in testImageIsDelivered
                .andExpect(cookie().exists("trackingCookie"))
                .andReturn();
        Cookie cookie1 = firstreq.getResponse().getCookie("trackingCookie");

        //make a new call to ImageController with received cookie
        //assert response wo a cookie
        this.mockMvc.perform(get("/image.gif").cookie(cookie1))
                //.andDo(print())
                .andExpect(status().isOk()) // sanity, tested in testImageIsDelivered
                .andExpect(content().contentType("image/gif")) // sanity, tested in testImageIsDelivered
                .andExpect(cookie().doesNotExist("trackingCookie"));


    }

    /**
     * https://stackoverflow.com/questions/56657018/spring-mockmvc-dont-expect-content
     */
    private Matcher<String> doesNotContainString(String s) {
        return CoreMatchers.not(containsString(s));
    }
}
