import POJO.Location;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.List;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class ZippoTest {
    @Test
    public void test() {
        given()  //preparation  (token,request body,parameters)

                .when() //for URL and request method (get,post,put,delete)

                .then();//response body,assertions,extract data from the response
    }

    @Test
    public void statusCodeTest() {
        given()


                .when()
                .get("http://api.zippopotam.us/us/90210")
                .then()
                .log().body()
                .log().status()// prints the response body
                .statusCode(200);

    }

    @Test
    public void contentTypeTest() {
        given()
                .when()
                .get("http://api.zippopotam.us/us/90210")
                .then()
                .log().body()
                .statusCode(200)
                .contentType(ContentType.JSON);

    }

    @Test
    public void checkCountryFromResponseBody() {
        given()
                .when()
                .get("http://api.zippopotam.us/us/90210")
                .then()
                .log().body()
                .body("country", equalTo("United States"));

        // pm                                               // Rest Assured
        // pm.response.json().'post code'                   // body("'post code'", ...)
        // pm.response.json().places[0].'place name'        // body("places[0].'place name'", ...)
        // pm.response.json().places[0].'place name'        // body("places.'place name'", ...) gives all place names  in the list

    }

    @Test
    public void checkStateFromResponse() {
        given()
                .when()
                .get("http://api.zippopotam.us/us/90210")
                .then()
                .log().body()
                .statusCode(200)
                .body("places[0].state", equalTo("California")); //check if the states is california
    }

    @Test
    public void bodyHasItem() {
        given()
                .when()
                .get("http://api.zippopotam.us/tr/01000")
                .then()
                // .log().body()
                .statusCode(200)
                .body("places.'place name'", hasItem("Büyükdikili Köyü"));
    }

    @Test
    public void bodyArraySize() {
        given()
                .when()
                .get("http://api.zippopotam.us/us/90210")
                .then()
                .log().body()
                .statusCode(200)
                .body("places", hasSize(1));


    }

    @Test
    public void bodyArraySize2() {
        given()
                .when()
                .get("http://api.zippopotam.us/tr/01000")
                .then()
                .log().body()
                .statusCode(200)
                .body("places.'place name'", hasSize(71));
    }

    @Test
    public void multipleTests() {
        given()
                .when()
                .get("http://api.zippopotam.us/tr/01000")
                .then()
                .log().body()
                .statusCode(200)
                .body("places.'place name'", hasSize(71))
                .body("places.'place name'", hasItem("Büyükdikili Köyü"))
                .body("places[2].'place name'", equalTo("Dörtağaç Köyü"));
    }

    @Test
    public void pathPramsTest() {
        given()
                .pathParam("Country", "us")
                .pathParam("ZipCode", "90210")
                .log().uri() // prints the request url
                .when()
                .get("http://api.zippopotam.us/{Country}/{ZipCode}")
                .then()
                .log().body()
                .statusCode(200);
    }

    @Test
    public void paramsTest1() {
        for (int i = 90210; i <= 90213; i++) {


            given()
                    .pathParam("Country", "us")
                    .pathParams("ZipCode", i)
                    .log().uri() // prints the request url
                    .when()
                    .get("http://api.zippopotam.us/{Country}/{ZipCode}")
                    .then()
                    .log().body()
                    .statusCode(200)
                    .body("places", hasSize(1));
        }
    }

    @Test
    public void queryParams() {
        for (int i = 1; i <= 10; i++) {
            given()
                    .param("page", i)//https://gorest.co.in/public/v1/users?page=2
                    .when()
                    .get("https://gorest.co.in/public/v1/users")
                    .then()
                    .log().body()
                    .statusCode(200)
                    .body("meta.pagination.page", equalTo(i));


        }
    }

    RequestSpecification requestSpec;
    ResponseSpecification responseSpec;

    @BeforeClass
    public void setup() {
        baseURI = "https://gorest.co.in/public/v1";
        requestSpec = new RequestSpecBuilder()
                .log(LogDetail.URI)
                .setContentType(ContentType.JSON)
                .build();
        responseSpec = new ResponseSpecBuilder()
                .expectStatusCode(200)
                .expectContentType(ContentType.JSON)
                .log(LogDetail.BODY)
                .build();

    }

    @Test
    public void baseURI() {

        given()
                .param("page", 2)
                .when()
                .get("/users")
                .then()
                .log().body()
                .statusCode(200)
                .body("meta.pagination.page", equalTo(2));

    }

    @Test
    public void extractData() {
        String placeName = given()
                .pathParam("Country", "us")
                .pathParam("ZipCode", "90210")
                .log().uri() // prints the request url
                .when()
                .get("http://api.zippopotam.us/{Country}/{ZipCode}")
                .then()
                .log().body()
                .statusCode(200)
                .extract().path("places[0].'place name'");
        System.out.println(placeName);
    }

    @Test
    public void extractData1() {
        int limit = given()
                .param("page", 2)
                .when()
                .get("/users")
                .then()
                .log().body()
                .statusCode(200)
                .extract().path("meta.pagination.limit");
        System.out.println(limit);
        Assert.assertEquals(limit, 10, "Test is failed");
    }

    @Test
    public void extractData2() {

        List<Integer> ids = given()
                .when()
                .get("/users")
                .then()
                .log().body()
                .statusCode(200)
                .extract().path("data.id");
        System.out.println(ids.get(1));
        Assert.assertTrue(ids.contains(1061487));

    }

    @Test
    public void extractData3() {
// send get request to https://gorest.co.in/public/v1/users.
        // extract all names from data to a List
        List<String> names = given()
                .when()
                .get("/users")
                .then()
                .log().body()
                .statusCode(200)
                .extract().path("data.name");
        System.out.println(names);
        System.out.println(names.get(5));
        Assert.assertEquals(names.get(5), "Ranjit Devar");
    }

    @Test
    public void extractData4() {
        Response response = given()
                .when()
                .get("/users")
                .then()
                .log().body()
                .statusCode(200)
                .extract().response();
        List<Integer> listOfIds = response.path("data.id");
        List<String> listOfNames = response.path("data.name");
        int limit = response.path("meta.pagination.limit");
        String currentLink = response.path("meta.pagination.links.current");
        System.out.println("ListOFIDS= " + listOfIds);
        System.out.println("ListOfNames= " + listOfNames);
        System.out.println("Limit= " + limit);
        System.out.println("currentLinks= " + currentLink);
        Assert.assertTrue(listOfNames.contains("Shobhana Asan"));
        Assert.assertTrue(listOfIds.contains(1079983));
        Assert.assertEquals(limit, 10);
    }
    @Test
    public void extractJsonPOJO() {
        //Location;                                    //Place
        //String post code;                            String place name;
        //String country;                              String longitude;
       // String country abbreviation;                 String state;
        //List <Place> places;                         String state abbreviation;
                                    // String latitude

      Location location=  given()
                .when()
                .get("http://api.zippopotam.us/us/90210")
                .then()
                .log().body()
                .extract().as(Location.class);
        System.out.println("location.getCountry()= "+location.getCountry());
        System.out.println("location.getPstCode()= "+location.getPostCode());
        System.out.println("location.getPlaces().get(0).getPlaceName()= "+location.getPlaces().get(0).getPlaceName());
        System.out.println("location.getPlaces().get(0).getState()= "+location.getPlaces().get(0).getState());

    }







}