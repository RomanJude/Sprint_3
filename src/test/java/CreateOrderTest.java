import api.client.Order;
import api.client.OrdersClient;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.assertEquals;

public class CreateOrderTest {

    List<String> colors = new ArrayList<String>();
    Order order  = new Order("Naruto", "Uchiha", "Konoha, 142 apt.", 4, "+7 800 355 35 35", 5,
            "2022-06-06", "Saske, come back to Konoha", colors);

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
    }

    @Test @DisplayName("Check creation the order with grey and black color")
    public void creationOrderTest(){
        colors.add("BLACK");
        colors.add("GREY");
        OrdersClient ordersClient = new OrdersClient();
        Response orderCreateResponse = ordersClient.creationOrder(order);
        String track= orderCreateResponse.body().asString().replace("\"track\":", "").
                trim().replace("{", "").replace("}", "");
        String json3 = "{\"track\": " + track + "}";
        ordersClient.cancelOrder(json3);
    }

    @Test  @DisplayName("Check creation the order with black color")
    public void creationOrderBlackTest(){
        colors.add("BLACK");
        OrdersClient ordersClient = new OrdersClient();
        Response orderCreateResponse = ordersClient.creationOrder(order);
        String track= orderCreateResponse.body().asString().replace("\"track\":", "").
                trim().replace("{", "").replace("}", "");
        String json3 = "{\"track\": " + track + "}";
        ordersClient.cancelOrder(json3);
    }

    @Test @DisplayName("Check creation the order with grey color")
    public void creationOrderGreyTest(){
        colors.add("GREY");
        OrdersClient ordersClient = new OrdersClient();
        Response orderCreateResponse = ordersClient.creationOrder(order);
        String track = orderCreateResponse.body().asString().replace("\"track\":", "").
                trim().replace("{", "").replace("}", "");
        String json3 = "{\"track\": " + track + "}";
        ordersClient.cancelOrder(json3);
    }

    @Test @DisplayName("Check creation the order without color")
    public void creationOrderWithoutColorTest(){
        OrdersClient ordersClient = new OrdersClient();
        Response orderCreateResponse = ordersClient.creationOrder(order);
        String track= orderCreateResponse.body().asString().replace("\"track\":", "").
                trim().replace("{", "").replace("}", "");
        String json3 = "{\"track\": " + track + "}";
        ordersClient.cancelOrder(json3);
    }

    @Test @DisplayName("Check containing the word track after creation the api.client.Order ")
    public void creationOrderContainsTrackTest() {
        OrdersClient ordersClient = new OrdersClient();
        Response orderCreateResponse = ordersClient.creationOrder(order);
        String track= orderCreateResponse.body().asString().replace("\"track\":", "").
                trim().replace("{", "").replace("}", "");
        Boolean expected = orderCreateResponse.body().asString().contains("track");
        assertEquals(expected, true);
        orderCreateResponse.then().assertThat().statusCode(201);
        String json3 = "{\"track\": " + track + "}";
        ordersClient.cancelOrder(json3);

    }

}

