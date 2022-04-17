import api.client.CourierClient;
import api.client.Order;
import api.client.OrdersClient;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.notNullValue;

@DisplayName("Test to create the List of the Orders")
public class ListOfOrdersTest {

    private final String login = "vasya7";
    private final String password = "1237";
    private final String firstName = "max";
    private final String createCourierBody = "{\"login\": \"" + login + "\", \"password\": \"" + password +
            "\", \"firstName\": \"" + firstName + "\"}";
    private final String courierLogInBody = "{\"login\": \"" + login + "\", \"password\": \"" + password + "\"}";
    private List<String> colors = new ArrayList<String>();
    private Order order = new Order("Naruto", "Uchiha", "Konoha, 142 apt.", 4, "+7 800 355 35 35", 5,
            "2022-06-06", "Saske, come back to Konoha", colors);
    private String orderTrackNumber;

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
    }

    private String findCourierIdInBody(String body) {
        return body.replace("\"id\":", "").replace("{", "").replace("}", "");
    }

    private String findOrderTrackNumberInBody(String body) {
        return body.replace("\"track\":", "").replace("{", "").replace("}", "");
    }

    @Test
    @DisplayName("Get the list of the created orders test")
    public void listOfOrdersTest() {
        CourierClient courierClient = new CourierClient();
        courierClient.creationCourier(createCourierBody);
        colors.add(" ");
        OrdersClient ordersClient = new OrdersClient();
        Response orderCreateResponse = ordersClient.creationOrder(order);
        Response loginCourierResponse = courierClient.loginCourier(courierLogInBody);
        String idCourier = findCourierIdInBody(loginCourierResponse.body().asString());
        orderTrackNumber = findOrderTrackNumberInBody(orderCreateResponse.body().asString());
        String acceptOrder = "{idOrder + idCourier}";
        ordersClient.acceptOfTheOrder(acceptOrder, orderTrackNumber, idCourier);
        Response listOrdersResponse = ordersClient.listOrders(idCourier);
        listOrdersResponse.then().statusCode(200).and().assertThat().body("orders", notNullValue());
    }

    @After
    public void cleanUp() {
        CourierClient courierClient = new CourierClient();
        Response loginCourierResponse = courierClient.loginCourier(courierLogInBody);
        String id = findCourierIdInBody(loginCourierResponse.body().asString());
        String deleteCourierBody = "{\"id\": " + id + "}";
        courierClient.deleteCourier(deleteCourierBody, id);
        OrdersClient ordersClient = new OrdersClient();
        String cancellingOrder = "{\"track\": " + orderTrackNumber + "}";
        ordersClient.cancelOrder(cancellingOrder);
    }
}
