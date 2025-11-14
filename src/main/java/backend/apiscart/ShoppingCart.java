package backend.apiscart;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;

import java.util.TimeZone;


@SpringBootApplication
@Configuration
public class ShoppingCart {

	/** 
	 * @param args main args
	 */
	public static void main(String[] args) {
	    TimeZone.setDefault(TimeZone.getTimeZone("GMT-6:00"));
		SpringApplication.run(ShoppingCart.class, args);
	}
}
