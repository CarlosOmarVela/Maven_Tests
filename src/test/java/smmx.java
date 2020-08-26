import java.net.URLEncoder;
import org.junit.Test;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import static org.junit.Assert.*;
import static io.restassured.RestAssured.*;
import java.util.Base64;

public class smmx {
    @Test
    public void getTokenStatusFailTest(){
        given().queryParam("lang","es")
                .when().post("https://webapi.segundamano.mx/nga/api/v1.1/private/accounts")
                .then().statusCode(400);
    }    @Test
    public void Coronavirus(){
        RestAssured.baseURI = String.format("https://api.quarantine.country/api/v1/summary/latest");
        Response response = given()
                .log().all()
                .header("Accept","application/json")
                .get();

        String body = response.getBody().asString();//Obtener el body de la petición

        System.out.println("Body response= " + body);
        System.out.println("Response status = " + response.getStatusCode());
        System.out.println("Response Headers = " + response.getHeaders());
        System.out.println("Response ContentType = " + response.contentType());
        assertEquals(200,response.getStatusCode());
        assertTrue(body.contains("summary"));
    }

    @Test
    public void t01_get_token_fail(){
        //Request an account token without authorization header
        RestAssured.baseURI = String.format("https://webapi.segundamano.mx/nga/api/v1.1/private/accounts");
        Response response = given().log().all()
                .post();
        //validations
        System.out.println("Status expected: 400" );
        System.out.println("Result: " + response.getStatusCode());
        assertEquals(400,response.getStatusCode());
        String errorCode = response.jsonPath().getString("error.code");
        System.out.println("Error Code expected: VALIDATION FAILED \nResult: " + errorCode);
        assertEquals("VALIDATION_FAILED",errorCode);
    }

    @Test
    public void getToken(){
        String email = "apitest@mailinator.com";
        String pass = "12345";
        String ToEncode = email + ":" + pass;
        String Basic_encoded = Base64.getEncoder().encodeToString(ToEncode.getBytes());

        baseURI = String.format("https://webapi.segundamano.mx/nga/api/v1.1/private/accounts");
        Response response = given().queryParam("lang","es")
                .log().all().header("Authorization","Basic YXBpdGVzdEBtYWlsaW5hdG9yLmNvbToxMjM0NQ==")
                .post();
        String body = response.getBody().asString();
        System.out.println("Body response= " + body);
        assertEquals(200,response.getStatusCode());
        assertTrue(body.contains("access_token"));
    }
}
