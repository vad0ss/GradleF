package by.prilepishev.repository;

import by.prilepishev.FurnitureApplication;
import by.prilepishev.model.Worker;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;
import javax.inject.Named;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class WorkerPostgresRepository implements WorkerRepository {

    private final Connection connection;
    private static final Logger logger = LogManager.getLogger(WorkerPostgresRepository.class);

    @Inject
    public WorkerPostgresRepository(@Named("PostgresConnection") Connection connection) {
        this.connection = connection;
    }

    @Override
    public void add(Worker worker) throws SQLException {
        String sql = "INSERT INTO worker (name, age) VALUES (?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, worker.getName().toString());
            ps.setInt(2, worker.getAge());
            ps.executeUpdate();
        }
    }

    @Override
    public void saveAll(List<Worker> workerList) throws SQLException {
        String sql = "INSERT INTO worker (name, age) VALUES (?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            for (Worker w : workerList) {
                ps.setString(1, w.getName().toString());
                ps.setInt(2, w.getAge());
                ps.addBatch();
            }

            ps.executeBatch();
        }
    }


    @Override
    public List<Worker> findAll() throws SQLException {
        List<Worker> workerList = new ArrayList<>();
        String sql = "SELECT name, age FROM worker";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                int age = resultSet.getInt("age");
                workerList.add(new Worker(id, name, age));
            }
        }
        return workerList;
    }
}
