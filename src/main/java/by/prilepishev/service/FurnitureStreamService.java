package by.prilepishev.service;

import by.prilepishev.model.Furniture;
import by.prilepishev.model.Type;
import by.prilepishev.model.Worker;
import by.prilepishev.repository.FurniturePostgresRepository;
import by.prilepishev.repository.FurnitureRepository;
import by.prilepishev.repository.WorkerRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;
import javax.inject.Named;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

public class FurnitureStreamService {

    private final FurnitureRepository furnitureRepository;
    private final WorkerRepository workerRepository;
    private static final Logger logger = LogManager.getLogger(FurniturePostgresRepository.class);
    private List<Furniture> furnitures = new ArrayList<>();
    private List<Worker> workers = new ArrayList<>();

    @Inject
    public FurnitureStreamService(@Named("Postgres") FurnitureRepository repository,
                                  @Named("PWorker") WorkerRepository workerRepository) {
        this.furnitureRepository = repository;
        this.workerRepository = workerRepository;
    }

    public List<Furniture> getByType(Type type) throws SQLException {

        if (furnitures.isEmpty()) {
            furnitures = furnitureRepository.findAll();
        }

        List<Furniture> result = new ArrayList<>();

        result = furnitures.stream()
                .filter(f -> f.getType() == type)
                .toList();

        logger.info("Получение мебели по типу");
        return result;
    }

    public List<Furniture> getByColor(String color) throws SQLException {
        if (furnitures.isEmpty()) {
            furnitures = furnitureRepository.findAll();
        }

        List<Furniture> result = new ArrayList<>();

        result = furnitures.stream()
                .filter(f -> f.getColor().equals(color))
                .toList();

        logger.info("Получение мебели по цвету");
        return result;
    }

    public long getYoungWorker(int age) throws SQLException {
        if (workers.isEmpty()) {
            workers = workerRepository.findAll();
        }

        List<Worker> result = new ArrayList<>();

        result = workers.stream()
                .filter(w -> w.getAge() < age)
                .toList();

        logger.info("Получение работников младше заданного возраста");
        return result.size();
    }

    public Optional<Integer> maxPrice() throws SQLException {
        if (furnitures.isEmpty()) {
            furnitures = furnitureRepository.findAll();
        }

        Optional<Integer> price = furnitures.stream()
                .map(Furniture::getPrice).max(Comparator.naturalOrder());

        return price;
    }

    public List<String> getAllNames() throws SQLException {
        if (workers.isEmpty()) {
            workers = workerRepository.findAll();
        }

        List<String> result = workers.stream()
                .map(Worker::getName)
                .toList();
        logger.info("Имена рабочих" + result);
        return result;
    }


   public Map<Type, Long> countByType() throws SQLException {
       if (furnitures.isEmpty()) {
           furnitures = furnitureRepository.findAll();
       }

       Map<Type, Long> result = furnitures.stream()
               .collect(Collectors.groupingBy(f -> f.getType(), Collectors.counting()));

      return result;
   }

   public Map<String, Double> avgPriceByMaterial(Type type) throws SQLException {
       List<Furniture> furnitureByType = this.getByType(type);


       Map<String, Double> result = furnitureByType.stream()
               .collect(Collectors.groupingBy(Furniture::getMaterial,
                       Collectors.averagingInt(Furniture::getPrice)));

       return result;
   }

   public List<Worker> workerSortList() throws SQLException {
       if (workers.isEmpty()) {
           workers = workerRepository.findAll();
       }

       List<Worker> result = workers.stream()
               .sorted(Comparator.comparingInt(Worker::getAge)
               .thenComparing(Worker::getName))
               .collect(Collectors.toList());
       return result;
   }

   public Map<String, Integer> totalPriceByColor() throws SQLException {
       if (furnitures.isEmpty()) {
           furnitures = furnitureRepository.findAll();
       }

       Map<String, Integer> result = furnitures.stream()
               .collect(Collectors.groupingBy(Furniture::getColor,
                       Collectors.summingInt(Furniture::getPrice)));

       return result;
   }

}
