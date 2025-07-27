package by.prilepishev;

import by.prilepishev.config.FurnitureModule;
import by.prilepishev.factory.FurnitureFactory;
import by.prilepishev.model.Furniture;
import by.prilepishev.model.Type;
import by.prilepishev.service.FurnitureAggregateService;
import by.prilepishev.service.FurniturePostgresService;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FurnitureApplication {

    private static final Logger logger = LogManager.getLogger(FurnitureApplication.class);

    public static void main(String[] args) {
        logger.info("Запуск...");

        Injector injector =  Guice.createInjector(new FurnitureModule());
        FurnitureAggregateService aggregateService = injector.getInstance(FurnitureAggregateService.class);
        FurniturePostgresService postgresService = injector.getInstance(FurniturePostgresService.class);
        List<Furniture> furnitureList;

        logger.info("Добавление 5000 записей в PostgreSQL...");

        furnitureList = FurnitureFactory.furnitureGenerate(20);

        try {
            postgresService.saveAll(furnitureList);
            logger.info("Все 5000 записей успешно добавлены в PostgreSQL.");
        } catch (SQLException e) {
            logger.error("ОШИБКА при добавлении в PostgreSQL: ", e);
            return;
        }

        logger.info("Перенос 5000 записей в Mongo...");
        try {
            aggregateService.transferData();
        } catch (SQLException e) {
            logger.error("ОШИБКА при переносе данных: ", e);
        }

        try {
            aggregateService.transferDataByType(Type.CHAIR);
        } catch (SQLException e) {
            logger.error("ОШИБКА при получении данных: ", e);
        }

        logger.info("Работа приложения завершена.");
    }
}
