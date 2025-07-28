package by.prilepishev.repository;

import by.prilepishev.model.Furniture;
import by.prilepishev.model.Type;

import java.sql.SQLException;
import java.util.List;

public interface FurnitureRepository {

    void add(Furniture furniture) throws SQLException;
    void saveAll(List<Furniture> furnitureList) throws SQLException;

    List<Furniture> findAll() throws SQLException;
    List<Furniture> getByType (Type type) throws SQLException;

}
