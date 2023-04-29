package Campus;

import Campus.Models.Country;
import io.restassured.http.ContentType;
import io.restassured.http.Cookies;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.*;

public class CountryTest {
    Cookies cookies;
    Country country;

    @BeforeClass
    public void login() {
        Map<String, String> credentials = new HashMap<>();
        credentials.put("username", "turkeyts");
        credentials.put("password", "TechnoStudy123");
        credentials.put("rememberMe", "true");


        cookies = (Cookies) given()
                .body(credentials)
                .contentType(ContentType.JSON)
                .when()
                .post("https://test.mersys.io/auth/login")
                .then()
                .statusCode(200)
                .log().body();
    }

    public String randomCountryName() {
        return RandomStringUtils.randomAlphabetic(8);

    }

    public String randomCountryCode() {
        return RandomStringUtils.randomAlphabetic(3);
    }

    @Test
    public void createCountry() {
        Country country = new Country();
        country.setName(randomCountryName());
        country.setCode(randomCountryCode());

        given()
                .body(country)
                .contentType(ContentType.JSON)
                .cookies(cookies)

                .when()
                .post("https://test.mersys.io/school-service/api/countries")

                .then()
                .statusCode(201);

    }

    @Test
    public void createCountryNegative() {
         country = new Country();
        country.setName(randomCountryName());
        country.setCode(randomCountryCode());

        given()
                .body(country)
                .contentType(ContentType.JSON)
                .cookies(cookies)

                .when()
                .post("https://test.mersys.io/school-service/api/countries")

                .then()
                .statusCode(400);
    }
}