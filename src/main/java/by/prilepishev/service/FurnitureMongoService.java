package by.prilepishev.service;

import by.prilepishev.model.Furniture;
import by.prilepishev.repository.FurnitureRepository;

import javax.inject.Inject;
import javax.inject.Named;
import java.sql.SQLException;

public class FurnitureMongoService {

    private final FurnitureRepository repository;

    @Inject
    public FurnitureMongoService(@Named("Mongo") FurnitureRepository repository) {
        this.repository = repository;
    }

    public void add(Furniture furniture) throws SQLException {
        repository.add(furniture);
    }
}