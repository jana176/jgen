import java.sql.Connection;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RestController;

import database.DatabaseConnection;
import database.DatabaseMetadata;


@RestController
@EnableAutoConfiguration
public class Application {
	
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	
			Connection connection = DatabaseConnection.getConnection();
			DatabaseMetadata.getDatabaseMetadata(connection);
		
	}

}
