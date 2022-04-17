import api.client.CourierClient;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.*;

@DisplayName("Create a courier with right fields or create the same courier test")
public class CreateCourierTest {

    private final String login = "vasya7";
    private final String password = "1237";
    private final String firstName = "max";
    private final String createCourierBody = "{\"login\": \"" + login + "\", \"password\": \"" + password + "\", \"firstName\": \"" + firstName + "\"}";
    private final String loginCourierBody = "{\"login\": \"" + login + "\", \"password\": \"" + password + "\"}";

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
    }

    @Test
    @DisplayName("Check containing of word and statusCode of creation " + "the courier with all needed fields")
    public void creationCourierTest() {
        CourierClient courierClient = new CourierClient();
        Response createCourierResponse = courierClient.creationCourier(createCourierBody);
        createCourierResponse.then().statusCode(201).and().assertThat().body("ok", equalTo(true));
    }

    @Test
    @DisplayName("Check containing of word and statusCode of creation courier with the same fields " +
            "of the courier, who was created before")
    public void createSameCourier() {
        String message = "Этот логин уже используется";
        CourierClient courierClient = new CourierClient();
        courierClient.creationCourier(createCourierBody);
        Response courierWithTheSameFieldsResponse = courierClient.creationCourier(createCourierBody);
        courierWithTheSameFieldsResponse.then().statusCode(409).and().assertThat().
                body("message", equalTo(message)); // текст предупреждения не соответствует документации
    }

    @After
    public void cleanUp() {
        CourierClient courierClient = new CourierClient();
        Response loginCourierResponse = courierClient.loginCourier(loginCourierBody);
        String id = loginCourierResponse.body().asString().replace("\"id\":", "").
                trim().replace("{", "").replace("}", "");
        String deleteCourierBody = "{\"id\": " + id + "}";
        courierClient.deleteCourier(deleteCourierBody, id);
    }
}


