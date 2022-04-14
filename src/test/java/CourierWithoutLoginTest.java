import api.client.CourierClient;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Test;
import io.restassured.RestAssured;
import org.junit.Before;
import static org.hamcrest.Matchers.*;

public class CourierWithoutLoginTest {

    private final String login = "vasya7";
    private final String password = "1237";
    private final String firstName = "max";

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
    }

    @Test @DisplayName("Check message and statusCode of creation the courier without login")
    public void createCourierWithoutLogin(){
        CourierClient courierClient = new CourierClient();
        String json = "{\"password\": \"" + password + "\", \"firstName\": \"" + firstName + "\"}";
        Response createCourierResponse = courierClient.creationCourier(json);
        createCourierResponse.then().assertThat().body("message", equalTo("Недостаточно данных для создания учетной записи"))
                .and()
                .statusCode(400);
    }
    @Test @DisplayName("Check message and statusCode of creation the courier without password")
    public void createCourierWithoutPassword(){
        CourierClient courierClient = new CourierClient();
        String json = "{\"login\": \"" + login + "\", \"firstName\": \"" + firstName + "\"}";
        Response createCourierResponse = courierClient.creationCourier(json);
        createCourierResponse.then().assertThat().body("message", equalTo("Недостаточно данных для создания учетной записи"))
                .and()
                .statusCode(400);
    }
}
