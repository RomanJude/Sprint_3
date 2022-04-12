import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import static io.restassured.RestAssured.given;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.assertEquals;

public class CreateOrderTest {

    List<String> colors = new ArrayList<String>();
    Order order  = new Order("Naruto", "Uchiha", "Konoha, 142 apt.", 4, "+7 800 355 35 35", 5,
            "2022-06-06", "Saske, come back to Konoha", colors);

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
    }

    @Test
    public void creationOrderTest(){
        colors.add("BLACK");
        colors.add("GREY");
       Response response =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(order)
                        .when()
                        .post("/api/v1/orders");
        String track= response.body().asString().replace("\"track\":", "").
                trim().replace("{", "").replace("}", "");
        String json3 = "{\"track\": " + track + "}";
        Response response3 =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(json3)
                        .when()
                        .delete("/api/v1/orders/cancel");
    }

    @Test
    public void creationOrderBlackTest(){
        colors.add("BLACK");
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(order)
                        .when()
                        .post("/api/v1/orders");
        String track= response.body().asString().replace("\"track\":", "").
                trim().replace("{", "").replace("}", "");
        String json3 = "{\"track\": " + track + "}";
        Response response3 =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(json3)
                        .when()
                        .delete("/api/v1/orders/cancel");
    }

    @Test
    public void creationOrderGreyTest(){
       colors.add("GREY");
       Response response =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(order)
                        .when()
                        .post("/api/v1/orders");
        String track= response.body().asString().replace("\"track\":", "").
                trim().replace("{", "").replace("}", "");
        String json3 = "{\"track\": " + track + "}";
        Response response3 =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(json3)
                        .when()
                        .delete("/api/v1/orders/cancel");
    }

    @Test
    public void creationOrderWithoutColorTest(){
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(order)
                        .when()
                        .post("/api/v1/orders");
        String track= response.body().asString().replace("\"track\":", "").
                trim().replace("{", "").replace("}", "");
        String json3 = "{\"track\": " + track + "}";
        Response response3 =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(json3)
                        .when()
                        .delete("/api/v1/orders/cancel");
    }

    @Test
    public void creationOrderContainsTrackTest() {
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(order)
                        .when()
                        .post("/api/v1/orders");
        Boolean expected = response.body().asString().contains("track");
        assertEquals(expected, true);
        response.then().assertThat().statusCode(201);
    }

}

