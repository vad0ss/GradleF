package by.prilepishev.repository;

import by.prilepishev.FurnitureApplication;
import by.prilepishev.model.Furniture;
import by.prilepishev.model.Type;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;
import javax.inject.Named;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FurniturePostgresRepository implements FurnitureRepository {

    private final Connection connection;
    private static final Logger logger = LogManager.getLogger(FurniturePostgresRepository.class);

    @Inject
    public FurniturePostgresRepository(@Named("PostgresConnection") Connection connection)
    {
        this.connection = connection;
    }

    @Override
    public void add(Furniture furniture) throws SQLException {
        String sql = "INSERT INTO furniture (type, material, price, color) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, furniture.getType().toString());
            ps.setString(2, furniture.getMaterial());
            ps.setInt(3, furniture.getPrice());
            ps.setString(4, furniture.getColor());
            ps.executeUpdate();
        }
    }

    @Override
    public List<Furniture> findAll() throws SQLException {
        List<Furniture> furnitureList = new ArrayList<>();
        String sql = "SELECT type, material, price, color FROM furniture";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            while (resultSet.next()) {
                Type type = Type.fromDisplayName(resultSet.getString("type"));
                String material = resultSet.getString("material");
                int price = resultSet.getInt("price");
                String color = resultSet.getString("color");
                furnitureList.add(new Furniture(type, material, price, color));
            }
        }
        return furnitureList;
    }

    @Override
    public List<Furniture> getByType(Type type) throws SQLException {
        List<Furniture> furnitureList = new ArrayList<>();
        String sql = "SELECT type, material, price, color FROM furniture WHERE type = ?";
        logger.info("Type = : " + Type.fromDisplayName(type.getDisplayName()));

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, type.name());
            ResultSet resultSet = ps.executeQuery();

            while (resultSet.next()) {
                Type iType = Type.fromDisplayName(resultSet.getString("type"));
                String material = resultSet.getString("material");
                int price = resultSet.getInt("price");
                String color = resultSet.getString("color");
                furnitureList.add(new Furniture(iType, material, price, color));
            }
        }
        return furnitureList;
    }

    @Override
    public void saveAll(List<Furniture> furnitureList) throws SQLException {
        String sql = "INSERT INTO furniture (type, material, price, color) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            for (Furniture f : furnitureList) {
                ps.setString(1, f.getType().toString());
                ps.setString(2, f.getMaterial());
                ps.setInt(3, f.getPrice());
                ps.setString(4, f.getColor());
                ps.addBatch();
            }

            ps.executeBatch();
        }
    }
}

