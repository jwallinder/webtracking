package se.jwallinder.tracking.model;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Builder
@ToString
public class WebTracking {

    private LocalDateTime timestamp;
    private String url;
    private String userid;


}
