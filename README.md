# Vert.x REST Example

A simple example for creating a REST client using Vert.X Java

## APIs

The API allows you to get all products, retrieve details for a particular product and to add a new product.

    List all products :: GET /products
    Get a product :: GET /products/<product_id>
    Add a product :: PUT /products/<product_id>

## Package and Run

    mvn clean install -Dmaven.test.skip=true
    java -jar target/vertx_rest-1.0-fat.jar 