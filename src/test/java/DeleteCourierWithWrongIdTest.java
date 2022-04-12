import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class DeleteCourierWithWrongIdTest {

    private final String login = "vasya7";
    private final String password = "1237";
    private final String firstName = "max";
    private final String json = "{\"login\": \"" + login + "\", \"password\": \"" + password +
            "\", \"firstName\": \"" + firstName + "\"}";
    private final String json2 = "{\"login\": \"" + login + "\", \"password\": \"" + password + "\"}";

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
    }

    @Test
    public void deleteCourierWithWrongId() {
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(json)
                        .when()
                        .post("/api/v1/courier");
        response.then().assertThat().body("ok", equalTo(true))
                .and()
                .statusCode(201);
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
                        .delete("/api/v1/courier/" + 5);
        response3.then().assertThat().body("message", equalTo("Курьера с таким id нет"))//Согласно документации в тексте предупреждения отсутствует точка в конце
                .and()
                .statusCode(404);
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
