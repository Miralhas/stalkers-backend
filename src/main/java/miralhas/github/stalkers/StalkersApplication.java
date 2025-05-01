package miralhas.github.stalkers;

import com.github.slugify.Slugify;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.cache.annotation.EnableCaching;

import java.util.Locale;
import java.util.TimeZone;

@EnableCaching
@SpringBootApplication
@ConfigurationPropertiesScan
public class StalkersApplication {

	public static final Slugify SLG = Slugify.builder().
			lowerCase(true)
			.customReplacement("'", "")
			.locale(Locale.ENGLISH).build();

	public static void main(String[] args) {
		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
		SpringApplication.run(StalkersApplication.class, args);
	}
}
