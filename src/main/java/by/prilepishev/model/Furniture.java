package by.prilepishev.model;

public class Furniture {

    private int id;
    public Type type;
    private String material;
    private int price;
    private String color;

    public Furniture(int id, Type type, String material, int price, String color) {
        this.id = id;
        this.type = type;
        this.material = material;
        this.price = price;
        this.color = color;
    }

    public Furniture(Type type, String material, int price, String color) {
        this(0, type, material, price, color);
    }


    public Type getType() {
        return type;
    }

    public int getId() { return id; }

    public String getMaterial() {
        return material;
    }

    public int getPrice() {
        return price;
    }

    public String getColor() {
        return color;
    }

    @Override
    public String toString() {
        return "Furniture{" +
                "type=" + type +
                ", material='" + material + '\'' +
                ", price=" + price +
                ", color='" + color + '\'' +
                '}';
    }
}
