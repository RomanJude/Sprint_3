import api.client.CourierClient;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.*;

@DisplayName("Courier authorisation test")
public class CourierAuthorisationTest {

    private final String login = "vasya7";
    private final String password = "1237";
    private final String firstName = "max";
    private final String createCourierBody = "{\"login\": \"" + login + "\", \"password\": \"" + password + "\", \"firstName\": \"" + firstName + "\"}";
    private final String loginCourierBody = "{\"login\": \"" + login + "\", \"password\": \"" + password + "\"}";
    private final String messageNotFound = "Учетная запись не найдена";
    private final String messageNotEnoughData = "Недостаточно данных для входа";

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
    }

    @Test
    @DisplayName("Check returning id and statusCode after authorisation the courier")
    public void authorisationCourierReturnID() {
        CourierClient courierClient = new CourierClient();
        courierClient.creationCourier(createCourierBody);
        Response loginCourierResponse = courierClient.loginCourier(loginCourierBody);
        loginCourierResponse.then().statusCode(200).and().assertThat().body("id", notNullValue());
    }

    @Test
    @DisplayName("Check message and statusCode of authorisation the courier without the login")
    public void authorisationCourierWithoutLogin() {
        CourierClient courierClient = new CourierClient();
        courierClient.creationCourier(createCourierBody);
        String courierWithoutLoginBody = "{\"password\": \"" + password + "\"}";
        Response loginCourierResponse = courierClient.loginCourier(courierWithoutLoginBody);
        loginCourierResponse.then().statusCode(400).and().assertThat().body("message", equalTo(messageNotEnoughData));
    }

    @Test
    @DisplayName("Check message and statusCode of authorisation the courier with wrong login")
    public void authorisationCourierWithWrongLogin() {
        CourierClient courierClient = new CourierClient();
        courierClient.creationCourier(createCourierBody);
        String courierWithWrongLoginBody = "{\"login\": \"vasyugan\",\"password\": \"1237\"}";
        Response loginCourierResponse = courierClient.loginCourier(courierWithWrongLoginBody);
        loginCourierResponse.then().statusCode(404).and().assertThat().body("message", equalTo(messageNotFound));
    }

    @Test
    @DisplayName("Check message and statusCode of authorisation the courier with wrong password")
    public void authorisationCourierWithWrongPassword() {
        CourierClient courierClient = new CourierClient();
        courierClient.creationCourier(createCourierBody);
        String courierWithWrongPasswordBody = "{\"login\": \"vasya7\",\"password\": \"1230\"}";
        Response loginCourierResponse = courierClient.loginCourier(courierWithWrongPasswordBody);
        loginCourierResponse.then().statusCode(404).and().assertThat().body("message", equalTo(messageNotFound));
    }

    @Test
    @DisplayName("Check message for authorisation courier without password")
    public void authorisationCourierWithoutPassword() {
        CourierClient courierClient = new CourierClient();
        courierClient.creationCourier(createCourierBody);
        String courierWithoutPasswordBody = "{\"login\": \"" + login + "\"}";
        Response loginCourierResponse = courierClient.loginCourier(courierWithoutPasswordBody);
        loginCourierResponse.then().statusCode(400)
                .and().assertThat().body("message", equalTo(messageNotEnoughData));
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
