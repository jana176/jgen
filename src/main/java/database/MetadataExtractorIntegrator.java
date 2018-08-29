package database;

import org.hibernate.boot.Metadata;
import org.hibernate.boot.model.relational.Database;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.integrator.spi.Integrator;
import org.hibernate.service.spi.SessionFactoryServiceRegistry;

import lombok.Getter;

public class MetadataExtractorIntegrator implements Integrator {

	public static final MetadataExtractorIntegrator INSTANCE = new MetadataExtractorIntegrator();

	@Getter
	private Database database;

	public void disintegrate(SessionFactoryImplementor sessionFactory, SessionFactoryServiceRegistry serviceRegistry) {

	}

	public void integrate(Metadata metadata, SessionFactoryImplementor sessionFactory,
			SessionFactoryServiceRegistry serviceRegistry) {

		database = metadata.getDatabase();

	}

}
