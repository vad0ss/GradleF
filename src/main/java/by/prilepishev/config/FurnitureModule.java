package by.prilepishev.config;

import by.prilepishev.repository.FurnitureMongoRepository;
import by.prilepishev.repository.FurniturePostgresRepository;
import by.prilepishev.repository.FurnitureRepository;
import by.prilepishev.service.FurnitureAggregateService;
import by.prilepishev.service.FurnitureMongoService;
import by.prilepishev.service.FurniturePostgresService;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.name.Names;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.inject.Named;

public class FurnitureModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(FurnitureRepository.class)
                .annotatedWith(Names.named("Postgres"))
                .to(FurniturePostgresRepository.class);

        bind(FurnitureRepository.class)
                .annotatedWith(Names.named("Mongo"))
                .to(FurnitureMongoRepository.class);

        bind(FurniturePostgresService.class);
        bind(FurnitureMongoService.class);
        bind(FurnitureAggregateService.class);
    }

    @Provides
    @Named("PostgresConnection")
    Connection providePostgresConnection() throws SQLException {
         String url = "jdbc:postgresql://localhost:5432/furniture_db";
         String user = "user";
         String password = "1234";

         return DriverManager.getConnection(url, user, password);
    }

    @Provides
    @Named("MongoCollection")
    MongoCollection<Document> provideMongoConnection() throws SQLException {
        String connectionString = "mongodb://admin:admin123@localhost:27017/?authSource=admin&authMechanism=SCRAM-SHA-1";
        String dbName = "furniture_db";
        String collectionName = "furniture";

        MongoClient mongoClient = MongoClients.create(connectionString);
        MongoDatabase database = mongoClient.getDatabase(dbName);

        return database.getCollection(collectionName);
    }

}
