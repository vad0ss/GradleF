package by.prilepishev.config;

import by.prilepishev.FurnitureApplication;
import by.prilepishev.repository.*;
import by.prilepishev.service.FurnitureAggregateService;
import by.prilepishev.service.FurnitureMongoService;
import by.prilepishev.service.FurniturePostgresService;
import by.prilepishev.service.WorkerPostgresService;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.name.Names;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.Document;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import javax.inject.Named;

public class FurnitureModule extends AbstractModule {

    private static final Logger logger = LogManager.getLogger(FurnitureModule.class);
    private final Properties properties;

    public FurnitureModule() {
        this.properties = loadProperties();
    }

    @Override
    protected void configure() {
        bind(FurnitureRepository.class)
                .annotatedWith(Names.named("Postgres"))
                .to(FurniturePostgresRepository.class);

        bind(WorkerRepository.class)
                .annotatedWith(Names.named("PWorker"))
                .to(WorkerPostgresRepository.class);

        bind(FurnitureRepository.class)
                .annotatedWith(Names.named("Mongo"))
                .to(FurnitureMongoRepository.class);

        bind(FurniturePostgresService.class);
        bind(FurnitureMongoService.class);
        bind(FurnitureAggregateService.class);
        bind(WorkerPostgresService.class);
    }

    @Provides
    @Named("PostgresConnection")
    Connection providePostgresConnection() throws SQLException {
        String url = properties.getProperty("postgres.url");
        String user = properties.getProperty("postgres.user");
        String password = properties.getProperty("postgres.password");
        return DriverManager.getConnection(url, user, password);
    }

    @Provides
    @Named("MongoCollection")
    MongoCollection<Document> provideMongoConnection() throws SQLException {
        String connectionString = properties.getProperty("mongo.url");
        String dbName = properties.getProperty("mongo.dbname");
        String collectionName = properties.getProperty("mongo.collection");

        MongoClient mongoClient = MongoClients.create(connectionString);
        MongoDatabase database = mongoClient.getDatabase(dbName);
        return database.getCollection(collectionName);
    }

    private Properties loadProperties() {
        Properties props = new Properties();
        try (InputStream input = getClass()
                .getClassLoader()
                .getResourceAsStream("application.properties")) {
            if (input == null) {
                logger.error("Не удалось найти файл application.properties");
                throw new RuntimeException("Файл application.properties не найден");
            }
            props.load(input);
            logger.info("Файл application.properties успешно загружен");
        } catch (IOException e) {
            logger.error("Ошибка при загрузке файла application.properties", e);
            throw new RuntimeException("Ошибка при загрузке файла application.properties");
        }
        return props;
    }
}
