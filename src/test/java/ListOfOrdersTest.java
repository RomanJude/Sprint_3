import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;
import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;

public class ListOfOrdersTest {

    private final String login = "vasya7";
    private final String password = "1237";
    private final String firstName = "max";
    private final String json = "{\"login\": \"" + login + "\", \"password\": \"" + password +
            "\", \"firstName\": \"" + firstName + "\"}";
    private final String json2 = "{\"login\": \"" + login + "\", \"password\": \"" + password + "\"}";
    List<String> colors = new ArrayList<String>();
    Order order = new Order("Naruto", "Uchiha", "Konoha, 142 apt.", 4, "+7 800 355 35 35", 5,
            "2022-06-06", "Saske, come back to Konoha", colors);

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
    }

    @Test
    public void listOfOrdersTest() {
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(json)
                        .when()
                        .post("/api/v1/courier");

        colors.add(" ");
        Response response2 =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(order)
                        .when()
                        .post("/api/v1/orders");
        Response response3 =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(json2)
                        .when()
                        .post("/api/v1/courier/login");
        String idCourier = response3.body().asString().replace("\"id\":", "").
                trim().replace("{", "").replace("}", "");
        String idOrder = response2.body().asString().replace("\"track\":", "").
                trim().replace("{", "").replace("}", "");
        String json4 = "{idOrder + idCourier}";
        Response response4 =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(json4)
                        .when()
                        .put("/api/v1/orders/accept/" + idOrder + "?courierId=" + idCourier);
        Response response5 =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .when()
                        .get("/api/v1/orders?courierId=" + idCourier);
        Boolean expected = response5.body().asString().contains("orders");
        assertEquals(expected, true);
        response5.then().assertThat().statusCode(200);
    }

    @After
    public void cleanUp() {
        Response response2 =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(json2)
                        .when()
                        .post("/api/v1/courier/login");
        String id = response2.body().asString().replace("\"id\":", "").
                trim().replace("{", "").replace("}", "");
        String json3 = "{\"id\": " + id + "}";
        Response response3 =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(json3)
                        .when()
                        .delete("/api/v1/courier/" + id);
    }
}


