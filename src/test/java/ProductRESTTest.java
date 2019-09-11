import controller.ProductREST;
import domain.Product;
import io.vertx.core.Vertx;
import io.vertx.core.json.Json;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.client.predicate.ResponsePredicate;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(VertxUnitRunner.class)
public class ProductRESTTest {
    private Vertx vertx;
    private WebClient client;

    @Before
    public void setUp(TestContext context) {
        vertx = Vertx.vertx();
        vertx.deployVerticle(ProductREST.class.getName());
        client = WebClient.create(vertx);
    }

    @After
    public void tearDown(TestContext context) {
        vertx.close(context.asyncAssertSuccess());
    }

    @Test
    public void handleGetProductTest(TestContext context) {
        final Async async = context.async();

        client.get(8080, "localhost", "/products/prod3568")
                .expect(ResponsePredicate.SC_SUCCESS)
                .send(ar -> {
                    if (ar.succeeded()) {
                        System.out.println("Received response with status code: " + ar.result().statusCode() + " with body: " + ar.result().bodyAsString());
                        Product prod = Json.decodeValue(ar.result().bodyAsString(), Product.class);
                        context.assertNotNull(prod.getId());
                        context.assertEquals(prod.getName(), "Tea");
                    } else {
                        System.out.println("Something went wrong: " + ar.cause().getMessage());
                    }
                    async.complete();
                });
    }

    @Test
    public void handleListProductsTest(TestContext context) {
        final Async async = context.async();

        client.get(8080, "localhost", "/products")
                .expect(ResponsePredicate.SC_SUCCESS)
                .send(ar -> {
                    if (ar.succeeded()) {
                        System.out.println("Received response with status code: " + ar.result().statusCode()
                                + " with body: " + ar.result().bodyAsString());
                        context.assertEquals(ar.result().bodyAsJsonObject().size(), 2);
                    } else {
                        System.out.println("Something went wrong: " + ar.cause().getMessage());
                    }
                    async.complete();
                });
    }

    @Test
    public void handleAddProductTest(TestContext context) {
        Async async = context.async();
        Product prod = new Product("prod1234", "Sugar", 29f);

        client.put(8080, "localhost", "/products/prod1234")
                .expect(ResponsePredicate.SC_SUCCESS)
                .sendJson(prod, ar -> {
                    if (ar.succeeded()) {
                        System.out.println("Received response with status code: " + ar.result().statusCode()
                                + " with body: " + ar.result().bodyAsString());
                        context.assertEquals(ar.result().statusCode(), 200);
                    } else {
                        System.out.println("Something went wrong: " + ar.cause().getMessage());
                    }
                });

        // check total products has increased by one
        client.get(8080, "localhost", "/products")
                .expect(ResponsePredicate.SC_SUCCESS)
                .send(ar -> {
                    if (ar.succeeded()) {
                        System.out.println("Received response with status code: " + ar.result().statusCode()
                                + " with body: " + ar.result().bodyAsString());
                        context.assertEquals(ar.result().bodyAsJsonObject().size(), 3);
                    } else {
                        System.out.println("Something went wrong: " + ar.cause().getMessage());
                    }
                    async.complete();
                });
    }
}
