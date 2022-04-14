import api.client.CourierClient;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
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

    @Test @DisplayName("Check deleting the courier with wrong id")
    public void deleteCourierWithWrongId() {
        CourierClient courierClient = new CourierClient();
        courierClient.creationCourier(json);
        Response loginCourierResponse = courierClient.loginCourier(json2);
        String id = loginCourierResponse.body().asString().replace("\"id\":", "").
                trim().replace("{", "").replace("}", "");
        String json3 = "{\"id\": " + id + "}";
        Response deleteCourierResponse = courierClient.deleteCourier(json3, "5");
        deleteCourierResponse.then().assertThat().body("message", equalTo("Курьера с таким id нет"))//Согласно документации в тексте предупреждения отсутствует точка в конце
                .and()
                .statusCode(404);
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
