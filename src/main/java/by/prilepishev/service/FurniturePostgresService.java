package by.prilepishev.service;

import by.prilepishev.model.Furniture;
import by.prilepishev.model.Type;
import by.prilepishev.repository.FurnitureRepository;

import javax.inject.Inject;
import javax.inject.Named;
import java.sql.SQLException;
import java.util.List;

public class FurniturePostgresService {

    private final FurnitureRepository repository;

    @Inject
    public FurniturePostgresService(@Named("Postgres") FurnitureRepository repository) {
        this.repository = repository;
    }

    public void add(Furniture furniture) throws SQLException {
        repository.add(furniture);
    }

    public List<Furniture> findAll() throws SQLException {
        return repository.findAll();
    }

    public void saveAll(List<Furniture> furnitureList) throws SQLException {
        repository.saveAll(furnitureList);
    }

    public List<Furniture> getByType (Type type) throws SQLException {
        return repository.getByType(type);
    }

}
