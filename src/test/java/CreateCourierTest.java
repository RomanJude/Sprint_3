import api.client.CourierClient;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.hamcrest.Matchers.*;

public class CreateCourierTest {

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

     @Test @DisplayName("Check containing of word and statusCode of creation " +
             "the courier with all needed fields")
    public void creationCourierTest() {
         CourierClient courierClient = new CourierClient();
         Response createCourierResponse = courierClient.creationCourier(json);
         createCourierResponse.then().assertThat().body("ok", equalTo(true))
                .and()
                .statusCode(201);
    }

    @Test @DisplayName("Check containing of word and statusCode of creation courier with the same fields " +
            "of the courier, who was created before")

    public void createSameCourier() {
        CourierClient courierClient = new CourierClient();
        courierClient.creationCourier(json);
        Response courierWithTheSameFieldsResponse = courierClient.creationCourier(json);
        courierWithTheSameFieldsResponse.then().assertThat().body("message", equalTo("Этот логин уже используется")) // текст предупреждения не соответствует документации
                .and()
                .statusCode(409);
    }

    @After
    public void cleanUp() {
        CourierClient courierClient = new CourierClient();
        Response loginCourierResponse = courierClient.loginCourier(json);
        String id = loginCourierResponse.body().asString().replace("\"id\":", "").
                trim().replace("{", "").replace("}", "");
        String json3 = "{\"id\": " + id + "}";
        courierClient.deleteCourier(json3, id);
    }
}


