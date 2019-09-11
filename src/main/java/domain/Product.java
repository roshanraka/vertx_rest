package domain;

public class Product {

    private String id;
    private String name;
    private Float price;

    public Product() {
    }

    public Product(String id, String name, Float price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Float getPrice() {
        return price;
    }
}
