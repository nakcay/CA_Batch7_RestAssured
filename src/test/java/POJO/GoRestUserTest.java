package POJO;

import io.restassured.http.ContentType;
import org.apache.commons.lang3.RandomStringUtils;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.*;

public class GoRestUserTest {
    public String createRandomName(){
        return RandomStringUtils.randomAlphabetic(8);
    }

    public String createRandomEmail(){
        return RandomStringUtils.randomAlphabetic(8).toLowerCase()+"@techno.com";
    }

    @Test
    public void createAUser(){
        given()
                .header("Authorization","Bearer c5af8451a22262d7997e81e71a14453c1bb42db3b94196edaa3bcba053170bf6")
                .contentType(ContentType.JSON)
                .body("{\"id\": 12309019, \"name\":+cre,\"email\": \"devika_mmsinha@lebsack.test\",\"gender\": \"female\",\"status\": \"active\"}")
                .log().body()
                .when()
                .post("https://gorest.co.in/public/v2/users")
                .then()
                .log().body()
                .statusCode(201)
                .contentType(ContentType.JSON);

    }

}
