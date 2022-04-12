import io.restassured.response.Response;
import org.junit.Test;
import static io.restassured.RestAssured.given;
import io.restassured.RestAssured;
import org.junit.Before;
import static org.hamcrest.Matchers.*;

public class CourierWithoutLoginTest {

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
    public void createCourierWithoutLogin(){
        String json = "{\"password\": \"" + password + "\", \"firstName\": \"" + firstName + "\"}";
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(json)
                        .when()
                        .post("/api/v1/courier");
        response.then().assertThat().body("message", equalTo("Недостаточно данных для создания учетной записи"))
                .and()
                .statusCode(400);
    }
    @Test
    public void createCourierWithoutPassword(){
        String json = "{\"login\": \"" + login + "\", \"firstName\": \"" + firstName + "\"}";
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(json)
                        .when()
                        .post("/api/v1/courier");
        response.then().assertThat().body("message", equalTo("Недостаточно данных для создания учетной записи"))
                .and()
                .statusCode(400);
    }
}
