import api.client.CourierClient;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import static org.hamcrest.Matchers.*;

public class DeleteCourierTest {

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

    @Test @DisplayName("Check deleting the courier with right id")
    public void deleteCourierWithRightId() {
        CourierClient courierClient = new CourierClient();
        courierClient.creationCourier(json);
        Response loginCourier = courierClient.loginCourier(json2);
        String id = loginCourier.body().asString().replace("\"id\":", "").
                trim().replace("{", "").replace("}", "");
        String json3 = "{\"id\": " + id + "}";
        Response deleteCourier =courierClient.deleteCourier(json3, id);
        deleteCourier.then().assertThat().body("ok", equalTo(true))
                .and()
                .statusCode(200);
    }

}
