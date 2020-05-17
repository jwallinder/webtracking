package se.jwallinder.tracking.model;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("singleton") // change to prototype
public class UserIdGenerator {

    private static final int STARTVALUE =12345;
    private  int current = STARTVALUE;


    public String getNextUserId() {
        return Integer.toString(current++);
    }

}
