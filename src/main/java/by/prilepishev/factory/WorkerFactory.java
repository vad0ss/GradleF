package by.prilepishev.factory;

import by.prilepishev.model.Worker;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WorkerFactory {
    private static final Random random = new Random();
    private static final String[] names = {"Василий",
            "Петр",
            "Александр",
            "Дмитрий",
            "Михаил"
    };

    private static Worker next() {
        String name = names[random.nextInt(names.length)];
        int age = random.nextInt(18, 65);

        return new Worker(0, name, age);
    }

    public static List<Worker> workerGenerate(int size) {
        List<Worker> workers = new ArrayList<>();

        for (int i = 0; i < size; i++) {
            workers.add(next());
        }
        return workers;
    }

}
