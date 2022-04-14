package api.client;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import static io.restassured.RestAssured.given;

public class CourierClient {

    @Step("Create courier")
    public Response creationCourier(String body) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(body)
                .when()
                .post("/api/v1/courier");
    }

    @Step ("Courier sign in")
    public Response loginCourier(String body){
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(body)
                .when()
                .post("/api/v1/courier/login");
    }

    @Step ("Courier delete")
    public Response deleteCourier(String body, String id){
       return  given()
                .header("Content-type", "application/json")
                .and()
                .body(body)
                .when()
                .delete("/api/v1/courier/" + id);
    }
}
