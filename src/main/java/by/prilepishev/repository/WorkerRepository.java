package by.prilepishev.repository;

import by.prilepishev.model.Worker;

import java.sql.SQLException;
import java.util.List;

public interface WorkerRepository {

    void add(Worker worker) throws SQLException;
    void saveAll(List<Worker> workerList) throws SQLException;

    List<Worker> findAll() throws SQLException;
}
