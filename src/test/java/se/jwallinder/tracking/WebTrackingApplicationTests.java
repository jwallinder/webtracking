package se.jwallinder.tracking;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class WebTrackingApplicationTests {

	@Autowired
	private ImageController imageController;

	@Autowired
	private ReportController reportController;


	@Test
	void contextLoads() {
		//sanity checks, make sure they are loaded
		assertNotNull(imageController);
		assertNotNull(reportController);
	}

}
