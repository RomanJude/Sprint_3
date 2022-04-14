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
import static org.junit.Assert.assertEquals;

public class ListOfOrdersTest {

    private final String login = "vasya7";
    private final String password = "1237";
    private final String firstName = "max";
    private final String json = "{\"login\": \"" + login + "\", \"password\": \"" + password +
            "\", \"firstName\": \"" + firstName + "\"}";
    private final String json2 = "{\"login\": \"" + login + "\", \"password\": \"" + password + "\"}";
    List<String> colors = new ArrayList<String>();
    Order order = new Order("Naruto", "Uchiha", "Konoha, 142 apt.", 4, "+7 800 355 35 35", 5,
            "2022-06-06", "Saske, come back to Konoha", colors);

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
    }

    @Test @DisplayName("Getting the list of orders")
    public void listOfOrdersTest() {
        CourierClient courierClient = new CourierClient();
        Response createCourierResponse = courierClient.creationCourier(json);

        colors.add(" ");
        OrdersClient ordersClient = new OrdersClient();
        Response orderCreateResponse = ordersClient.creationOrder(order);

        Response loginCourierResponse = courierClient.loginCourier(json2);
        String idCourier = loginCourierResponse.body().asString().replace("\"id\":", "").
                trim().replace("{", "").replace("}", "");
        String idOrder = orderCreateResponse.body().asString().replace("\"track\":", "").
                trim().replace("{", "").replace("}", "");

        String json4 = "{idOrder + idCourier}";
        Response acceptOrderResponse = ordersClient.acceptOfTheOrder(json4, idOrder, idCourier);

        Response listOrdersResponse = ordersClient.listOrders(idCourier);

        Boolean expected = listOrdersResponse.body().asString().contains("orders");
        assertEquals(expected, true);
        listOrdersResponse.then().assertThat().statusCode(200);
    }

    @After
    public void cleanUp() {
        CourierClient courierClient = new CourierClient();
        Response loginCourier = courierClient.loginCourier(json);
        String id = loginCourier.body().asString().replace("\"id\":", "").
                trim().replace("{", "").replace("}", "");
        String json3 = "{\"id\": " + id + "}";
        Response deleteCourier =courierClient.deleteCourier(json3, id);
    }
}


