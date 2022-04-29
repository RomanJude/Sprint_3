import api.client.CourierClient;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.*;

@DisplayName("Delete the Courier without or wrong id test")
public class DeleteCourierWithoutIdTest {

    private final String login = "vasya7";
    private final String password = "1237";
    private final String firstName = "max";
    private final String createCourierBody = "{\"login\": \"" + login + "\", \"password\": \"" + password +
            "\", \"firstName\": \"" + firstName + "\"}";
    private final String loginCourierBody = "{\"login\": \"" + login + "\", \"password\": \"" + password + "\"}";
    private final String messageMissingData = "Недостаточно данных для удаления курьера";
    private final String messageNoId = "Курьера с таким id нет";

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
    }

    private String findCourierIdInBody(String body) {
        return body.replace("\"id\":", "").replace("{", "").replace("}", "");
    }

    @Test
    @DisplayName("Check deleting the courier without id")
    public void deleteCourierWithoutId() {
        CourierClient courierClient = new CourierClient();
        courierClient.creationCourier(createCourierBody);
        Response loginCourierResponse = courierClient.loginCourier(loginCourierBody);
        String id = findCourierIdInBody(loginCourierResponse.body().asString());
        String idBody = "{\"id\": " + id + "}";
        Response deleteCourierResponse = courierClient.deleteCourier(idBody, "");
        deleteCourierResponse.then().statusCode(400)
                .and().assertThat().body("message", equalTo(messageMissingData));//Текст предупреждения не соответствует спецификации;
    }

    @Test
    @DisplayName("Check deleting the courier with wrong id")
    public void deleteCourierWithWrongId() {
        CourierClient courierClient = new CourierClient();
        courierClient.creationCourier(createCourierBody);
        Response loginCourierResponse = courierClient.loginCourier(loginCourierBody);
        String id = findCourierIdInBody(loginCourierResponse.body().asString());
        String idBody = "{\"id\": " + id + "}";
        Response deleteCourierResponse = courierClient.deleteCourier(idBody, "5");
        deleteCourierResponse.then().statusCode(404)
                .and().assertThat().body("message", equalTo(messageNoId)); //Согласно документации в тексте предупреждения отсутствует точка в конце;
    }

    @After
    public void cleanUp() {
        CourierClient courierClient = new CourierClient();
        Response loginCourierResponse = courierClient.loginCourier(loginCourierBody);
        String id = findCourierIdInBody(loginCourierResponse.body().asString());
        String deleteCourierBody = "{\"id\": " + id + "}";
        courierClient.deleteCourier(deleteCourierBody, id);
    }
}
