package controller;

import domain.Product;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.Json;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import service.ProductService;

import java.util.HashMap;
import java.util.Map;

public class ProductREST extends AbstractVerticle {

    private ProductService service;

    @Override
    public void start() {
        service = new ProductService();

        Router router = Router.router(vertx);
        router.route().handler(BodyHandler.create());

        router.get("/").handler(context -> {
            context.response().end(
                    "The API allows you to get all products, retrieve details for a particular product and to add a new product.\n" +
                    "\n" +
                    "    List all products :: GET /products\n" +
                    "    Get a product :: GET /products/<product_id>\n" +
                    "    Add a product :: PUT /products/<product_id>");
        });

        router.get("/products").handler(this::handleListProducts);
        router.get("/products/:productID").handler(this::handleGetProduct);
        router.put("/products/:productID").handler(this::handleAddProduct);

        vertx.createHttpServer().requestHandler(router).listen(8080);
    }

    private void handleListProducts(RoutingContext routingContext) {
        routingContext.response()
                .putHeader("content-type", "application/json")
                .end(Json.encodePrettily(service.getProducts()));
    }

    private void handleGetProduct(RoutingContext routingContext) {
        String productId = routingContext.request().getParam("productID");
        HttpServerResponse response = routingContext.response();
        if (productId == null) {
            sendError(400, response);
        } else {
            Product product = service.getProduct(productId);
            if (product == null) {
                sendError(404, response);
            } else {
                response.putHeader("content-type", "application/json").end(Json.encodePrettily(product));
            }
        }
    }

    private void handleAddProduct(RoutingContext routingContext) {
        String productID = routingContext.request().getParam("productID");
        HttpServerResponse response = routingContext.response();
        if (productID == null) {
            sendError(400, response);
        } else {
            Product product =  Json.decodeValue(routingContext.getBodyAsString(), Product.class);
            if (product == null) {
                sendError(400, response);
            } else {
                service.addProduct(productID, product);
                response.end();
            }
        }
    }

    private void sendError(int statusCode, HttpServerResponse response) {
        response.setStatusCode(statusCode).end();
    }
}
