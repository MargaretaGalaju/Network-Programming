package HttpRequestor;

import Models.HomeResponse;
import Models.RegisterResponse;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import static io.restassured.RestAssured.given;

public class HttpRequestor {
    private static String BASE_URI = "http://localhost:5000";

    public RegisterResponse parseRegisterResponse(){
        return getRequest("/register").getBody().as(RegisterResponse.class);
    }

    public HomeResponse parseHomeResponse(){
        return getRequest(parseRegisterResponse().getLink(), parseRegisterResponse().getAccess_token()).getBody().as(HomeResponse.class);
    }

    public Response getRequest(String uriPath) {
        RequestSpecification specification = new RequestSpecBuilder().setBaseUri(BASE_URI + uriPath).build();
        return given().spec(specification).get();
    }

    public Response getRequest(String uriPath, String header) {
        RequestSpecification specification = new RequestSpecBuilder().setBaseUri(BASE_URI + uriPath).addHeader("X-Access-Token", header).build();
        return given().spec(specification).get();
    }
}
