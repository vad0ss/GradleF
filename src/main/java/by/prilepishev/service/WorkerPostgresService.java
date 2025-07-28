package by.prilepishev.service;

import by.prilepishev.model.Worker;
import by.prilepishev.repository.WorkerRepository;

import javax.inject.Inject;
import javax.inject.Named;
import java.sql.SQLException;
import java.util.List;

public class WorkerPostgresService {

    private final WorkerRepository repository;

    @Inject
    public WorkerPostgresService(@Named("PWorker") WorkerRepository repository) {
        this.repository = repository;
    }

    public void add(Worker worker) throws SQLException {
        repository.add(worker);
    }

    public List<Worker> findAll() throws SQLException {
        return repository.findAll();
    }

    public void saveAll(List<Worker> workerList) throws SQLException {
        repository.saveAll(workerList);
    }
}
