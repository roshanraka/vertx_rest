package service;

import domain.Product;

import java.util.HashMap;
import java.util.Map;

public class ProductService {
    private Map<String, Product> products = new HashMap<>();

    public ProductService() {
        setUpInitialData();
    }

    public Map<String, Product> getProducts() {
        return products;
    }

    public Product getProduct(String productId) {
        return products.get(productId);
    }

    public void addProduct(String productID, Product product) {
        products.put(productID, product);
    }

    private void setUpInitialData() {
        addProduct(new Product("prod3568", "Tea", 129f));
        addProduct(new Product("prod7340", "Coffee", 159f));
    }

    private void addProduct(Product product) {
        products.put(product.getId(), product);
    }
}
