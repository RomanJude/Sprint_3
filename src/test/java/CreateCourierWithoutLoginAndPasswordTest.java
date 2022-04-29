import api.client.CourierClient;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Test;
import io.restassured.RestAssured;
import org.junit.Before;

import static org.hamcrest.Matchers.*;

@DisplayName("Create a Courier without login or password test")
public class CreateCourierWithoutLoginAndPasswordTest {

    private final String login = "vasya7";
    private final String password = "1237";
    private final String firstName = "max";
    private final String message = "Недостаточно данных для создания учетной записи";
    private final String courierWithoutLoginBody = "{\"password\": \"" + password + "\", \"firstName\": \"" + firstName + "\"}";
    private final String courierWithoutPasswordBody = "{\"login\": \"" + login + "\", \"firstName\": \"" + firstName + "\"}";

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
    }

    @Test
    @DisplayName("Check message and statusCode of creation the courier without login")
    public void createCourierWithoutLogin() {
        CourierClient courierClient = new CourierClient();
        Response createCourierResponse = courierClient.creationCourier(courierWithoutLoginBody);
        createCourierResponse.then().statusCode(400)
                .and().assertThat().body("message", equalTo(message));
    }

    @Test
    @DisplayName("Check message and statusCode of creation the courier without password")
    public void createCourierWithoutPassword() {
        CourierClient courierClient = new CourierClient();
        Response createCourierResponse = courierClient.creationCourier(courierWithoutPasswordBody);
        createCourierResponse.then().statusCode(400)
                .and().assertThat().body("message", equalTo(message));
    }
}
