import api.client.CourierClient;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertEquals;

public class CourierAuthorisationTest {

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

    @Test @DisplayName("Check authorisation the courier and its statusCode ")
    public void authorisationCourier(){
        CourierClient courierClient = new CourierClient();
        courierClient.creationCourier(json);
        Response loginCourierResponse = courierClient.loginCourier(json);
        loginCourierResponse.then().assertThat().statusCode(200);
    }

    @Test @DisplayName("Check returning id and statusCode after authorisation the courier")
    public void authorisationCourierReturnID(){
        CourierClient courierClient = new CourierClient();
        courierClient.creationCourier(json);
        Response loginCourierResponse = courierClient.loginCourier(json);
        Boolean expected = loginCourierResponse.body().asString().contains("id");
        assertEquals(expected, true);
        loginCourierResponse.then().assertThat().statusCode(200);
    }

    @Test @DisplayName("Check message and statusCode of authorisation the courier without the login")
    public void authorisationCourierWithoutLogin(){
        CourierClient courierClient = new CourierClient();
        courierClient.creationCourier(json);
        String json2 = "{\"password\": \"" + password + "\"}";
        Response loginCourierResponse = courierClient.loginCourier(json2);
        loginCourierResponse.then().assertThat().body("message", equalTo("Недостаточно данных для входа"))
                .and()
                .statusCode(400);
    }

    @Test @DisplayName("Check message and statusCode of authorisation the courier with wrong login")
    public void authorisationCourierWithWrongLogin(){
        CourierClient courierClient = new CourierClient();
        courierClient.creationCourier(json);
        String json2 = "{\"login\": \"vasyugan\",\"password\": \"1237\"}";
        Response loginCourierResponse = courierClient.loginCourier(json2);
        loginCourierResponse.then().assertThat().body("message", equalTo("Учетная запись не найдена"))
                .and()
                .statusCode(404);
    }

    @Test @DisplayName("Check message and statusCode of authorisation the courier with wrong password")
    public void authorisationCourierWithWrongPassword(){
        CourierClient courierClient = new CourierClient();
        courierClient.creationCourier(json);
        String json2 = "{\"login\": \"vasya7\",\"password\": \"1230\"}";
        Response loginCourierResponse = courierClient.loginCourier(json2);
        loginCourierResponse.then().assertThat().body("message", equalTo("Учетная запись не найдена"))
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
