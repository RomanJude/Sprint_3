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

@DisplayName("Create the order test")
public class CreateOrderTest {

    private List<String> colors = new ArrayList<String>();
    private Order order = new Order("Naruto", "Uchiha", "Konoha, 142 apt.", 4, "+7 800 355 35 35", 5,
            "2022-06-06", "Saske, come back to Konoha", colors);
    private String orderTrackNumber;

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
    }

    private String findTrackNumberInBody(String body) {
        return body.replace("\"track\":", "").
                trim().replace("{", "").replace("}", "");
    }

    @Test
    @DisplayName("Check creation the order with grey and black color")
    public void creationOrderTest() {
        colors.add("BLACK");
        colors.add("GREY");
        OrdersClient ordersClient = new OrdersClient();
        Response orderCreateResponse = ordersClient.creationOrder(order);
        orderTrackNumber = findTrackNumberInBody(orderCreateResponse.body().asString());
        orderCreateResponse.then().statusCode(201).and().assertThat().body("track", notNullValue());
    }

    @Test
    @DisplayName("Check creation the order with black color")
    public void creationOrderBlackTest() {
        colors.add("BLACK");
        OrdersClient ordersClient = new OrdersClient();
        Response orderCreateResponse = ordersClient.creationOrder(order);
        orderTrackNumber = findTrackNumberInBody(orderCreateResponse.body().asString());
        orderCreateResponse.then().statusCode(201).and().assertThat().body("track", notNullValue());
    }

    @Test
    @DisplayName("Check creation the order with grey color")
    public void creationOrderGreyTest() {
        colors.add("GREY");
        OrdersClient ordersClient = new OrdersClient();
        Response orderCreateResponse = ordersClient.creationOrder(order);
        orderTrackNumber = findTrackNumberInBody(orderCreateResponse.body().asString());
        orderCreateResponse.then().statusCode(201).and().assertThat().body("track", notNullValue());
    }

    @Test
    @DisplayName("Check creation the order without color")
    public void creationOrderWithoutColorTest() {
        OrdersClient ordersClient = new OrdersClient();
        Response orderCreateResponse = ordersClient.creationOrder(order);
        orderTrackNumber = findTrackNumberInBody(orderCreateResponse.body().asString());
        orderCreateResponse.then().statusCode(201).and().assertThat().body("track", notNullValue());
    }

    @After
    public void cleanUp() {
        OrdersClient ordersClient = new OrdersClient();
        String cancellingOrder = "{\"track\": " + orderTrackNumber + "}";
        ordersClient.cancelOrder(cancellingOrder);
    }
}

