package by.prilepishev;

import by.prilepishev.config.FurnitureModule;
import by.prilepishev.factory.FurnitureFactory;
import by.prilepishev.factory.WorkerFactory;
import by.prilepishev.model.Furniture;
import by.prilepishev.model.Type;
import by.prilepishev.model.Worker;
import by.prilepishev.repository.WorkerRepository;
import by.prilepishev.service.FurnitureAggregateService;
import by.prilepishev.service.FurniturePostgresService;
import by.prilepishev.service.FurnitureStreamService;
import by.prilepishev.service.WorkerPostgresService;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.OptionalInt;

public class FurnitureApplication {

    private static final Logger logger = LogManager.getLogger(FurnitureApplication.class);

    public static void main(String[] args) {
        logger.info("Запуск...");

        Injector injector = Guice.createInjector(new FurnitureModule());
        FurnitureAggregateService aggregateService = injector.getInstance(FurnitureAggregateService.class);
        FurniturePostgresService postgresService = injector.getInstance(FurniturePostgresService.class);
        WorkerPostgresService workerPostgresService = injector.getInstance(WorkerPostgresService.class);
        FurnitureStreamService furnitureStreamService = injector.getInstance(FurnitureStreamService.class);
        List<Furniture> furnitureList;
        List<Worker> workerList;

        logger.info("Добавление 5000 записей в PostgreSQL...");

        furnitureList = FurnitureFactory.furnitureGenerate(50);
        workerList = WorkerFactory.workerGenerate(500);

        try {
            postgresService.saveAll(furnitureList);
            logger.info("Все 5000 записей успешно добавлены в PostgresSQL.");
        } catch (SQLException e) {
            logger.error("ОШИБКА при добавлении в PostgresSQL: ", e);
            return;
        }

        try {
            workerPostgresService.saveAll(workerList);
            logger.info("Все 500 рабочих добавлены в PostgresSQL");
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

        try {
            List<Furniture> furnituresByType = furnitureStreamService.getByType(Type.CHAIR);
            for (Furniture f : furnituresByType) {
                logger.info(f);
            }
        } catch (SQLException e) {
            logger.error("ОШИБКА при получении данных: ", e);
        }

        try {
            List<Furniture> furnituresByColor = furnitureStreamService.getByColor("GREEN");
            for (Furniture f : furnituresByColor) {
                logger.info(f);
            }
        } catch (SQLException e) {
            logger.error("ОШИБКА при получении данных: ", e);
        }

        try {
            long workerCount = furnitureStreamService.getYoungWorker(22);
            logger.info("Количество молодых рабочих = " + workerCount);
        } catch (SQLException e) {
            logger.error("ОШИБКА при получении данных: ", e);
        }

        try {
            long workerCount = furnitureStreamService.getYoungWorker(22);
            logger.info("Количество молодых рабочих = " + workerCount);
        } catch (SQLException e) {
            logger.error("ОШИБКА при получении данных: ", e);
        }

        try {
            OptionalInt maxPrice = furnitureStreamService.maxPrice().get();

            logger.info("Максимальная цена" + maxPrice);
        } catch (SQLException e) {
            logger.error("ОШИБКА при получении данных: ", e);
        }

        try {
            List<String> names = furnitureStreamService.getAllNames();

            for (String name : names) {
                logger.info("Имя: = " + name);
            }

        } catch (SQLException e) {
            logger.error("ОШИБКА при получении данных: ", e);
        }

        try {
            Map<Type, Long> typeCounter = furnitureStreamService.countByType();

            typeCounter.forEach((type, count) -> logger.info("Тип: = " + type + " Количество := " + count));

        } catch (SQLException e) {
            logger.error("ОШИБКА при получении данных: ", e);
        }

        try {
            Map<String, Double>  avgPrice = furnitureStreamService.avgPriceByMaterial(Type.CHAIR);

            avgPrice.forEach((material, price) -> logger.info("Материал: = " + material + " Средняя цена := " + price));

        } catch (SQLException e) {
            logger.error("ОШИБКА при получении данных: ", e);
        }

        try {
            List<Worker> sortetWorkersList = furnitureStreamService.workerSortList();

            for (Worker w : sortetWorkersList) {
                logger.info(w);
            }

            Map<String, Integer> priceByColor = furnitureStreamService.totalPriceByColor();

            priceByColor.forEach((color, price) -> logger.info("Цвет: = " + color + " Сумма := " + price));

        } catch (SQLException e) {
            logger.error("ОШИБКА при получении данных: ", e);
        }

        logger.info("Работа приложения завершена.");
    }
}
