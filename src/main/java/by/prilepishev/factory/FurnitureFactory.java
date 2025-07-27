package by.prilepishev.factory;

import by.prilepishev.model.Furniture;
import by.prilepishev.model.Type;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class FurnitureFactory {
    private static final Random random = new Random();
    private static final Type[] types = Type.values();
    private static final String[] materials = {"WOOD",
            "METAL",
            "PLASTIC",
            "GLASS",
            "LEATHER"
    };
    private static final String[] colors = {"RED",
            "GREEN",
            "WHITE",
            "YELLOW",
            "BLACK"
    };

    private static Furniture next() {
        Type randomType = types[random.nextInt(types.length)];
        String randomMaterial = materials[random.nextInt(materials.length)];
        String randomColor = colors[random.nextInt(colors.length)];
        int randomPrice = random.nextInt(1000) + 100; // Цена от 100 до 1099

        return new Furniture(randomType, randomMaterial, randomPrice, randomColor);
    }

    public static List<Furniture> furnitureGenerate(int size) {
        List<Furniture> furnitures = new ArrayList<>();

        for (int i = 0; i < size; i++) {
            furnitures.add(next());
        }
        return furnitures;
    }

}
