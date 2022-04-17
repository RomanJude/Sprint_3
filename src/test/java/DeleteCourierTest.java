import api.client.CourierClient;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.*;

@DisplayName("Delete the Courier with right id test")
public class DeleteCourierTest {

    private final String login = "vasya7";
    private final String password = "1237";
    private final String firstName = "max";
    private final String createCourierBody = "{\"login\": \"" + login + "\", \"password\": \"" + password +
            "\", \"firstName\": \"" + firstName + "\"}";
    private final String loginCourierBody = "{\"login\": \"" + login + "\", \"password\": \"" + password + "\"}";

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
    }

    @Test
    @DisplayName("Check deleting the courier with right id")
    public void deleteCourierWithRightId() {
        CourierClient courierClient = new CourierClient();
        courierClient.creationCourier(createCourierBody);
        Response loginCourierResponse = courierClient.loginCourier(loginCourierBody);
        String id = loginCourierResponse.body().asString().replace("\"id\":", "").
                trim().replace("{", "").replace("}", "");
        String idBody = "{\"id\": " + id + "}";
        Response deleteCourier = courierClient.deleteCourier(idBody, id);
        deleteCourier.then().statusCode(200).and().assertThat().body("ok", equalTo(true));
    }
}
