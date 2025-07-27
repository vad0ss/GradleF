package by.prilepishev.service;

import by.prilepishev.model.Furniture;
import by.prilepishev.model.Type;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;
import java.sql.SQLException;
import java.util.List;

public class FurnitureAggregateService {

    private static final Logger logger = LogManager.getLogger(FurnitureAggregateService.class);
    private final FurniturePostgresService postgresService;
    private final FurnitureMongoService mongoService;

    @Inject
    public FurnitureAggregateService(FurniturePostgresService postgresService, FurnitureMongoService mongoService) {
        this.postgresService = postgresService;
        this.mongoService = mongoService;
    }

    public void transferData() throws SQLException {
        logger.info("Начинаю перенос данных из PostgreSQL в MongoDB.");

        List<Furniture> furnitureList = postgresService.findAll();
        logger.info("Извлечено " + furnitureList.size() + " записей из PostgreSQL.");

        for (Furniture furniture : furnitureList) {
            mongoService.add(furniture);
        }
        logger.info("Все " + furnitureList.size() + " записей успешно перенесены в MongoDB.");
    }

    public void transferDataByType(Type type) throws SQLException {
        logger.info("Начинаю перенос данных по типу из PostgreSQL в MongoDB.");

        List<Furniture> furnitureList = postgresService.getByType(type);
        logger.info("Извлечено " + furnitureList.size() + " записей из PostgreSQL.");

        for (Furniture furniture : furnitureList) {
            mongoService.add(furniture);
        }
        logger.info("Все " + furnitureList.size() + " записей успешно перенесены в MongoDB.");
    }

}