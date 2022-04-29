package api.client;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class OrdersClient {

    @Step ("Create order")
    public Response creationOrder(Order order){
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(order)
                .when()
                .post("/api/v1/orders");
    }

    @Step ("Cancel of order")
    public Response cancelOrder(String body){
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(body)
                .when()
                .delete("/api/v1/orders/cancel");
    }

    @Step ("Accept of the Order")
    public Response acceptOfTheOrder(String body, String idOrder, String idCourier){
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(body)
                .when()
                .put("/api/v1/orders/accept/" + idOrder + "?courierId=" + idCourier);
    }

    @Step("Show List of the Orders")
    public Response listOrders(String idCourier){
        return given()
                .header("Content-type", "application/json")
                .and()
                .when()
                .get("/api/v1/orders?courierId=" + idCourier);
    }
}
