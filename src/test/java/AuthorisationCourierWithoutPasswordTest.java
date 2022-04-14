import api.client.CourierClient;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.hamcrest.Matchers.*;

public class AuthorisationCourierWithoutPasswordTest {

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

    @Test @DisplayName("Check message for authorisation courier without password")
    public void authorisationCourierWithoutPassword(){
        CourierClient courierClient = new CourierClient();
        Response createCourierResponse = courierClient.creationCourier(json);
        String json2 = "{\"login\": \"" + login + "\"}";
        Response loginCourierResponse = courierClient.loginCourier(json2);
        String message = "Недостаточно данных для входа";
        loginCourierResponse.then().assertThat().body("message", equalTo(message))//неверно срабатывает приложение: выдаёт "Service unavailable"
                .and()
                .statusCode(400);
    }

    @After
    public void cleanUp() {
        CourierClient courierClient = new CourierClient();
        Response loginCourierResponse = courierClient.loginCourier(json);
        String id = loginCourierResponse.body().asString().replace("\"id\":", "").
                trim().replace("{", "").replace("}", "");
        String json3 = "{\"id\": " + id + "}";
        Response deleteCourierResponse = courierClient.deleteCourier(json3, id);
    }
}
