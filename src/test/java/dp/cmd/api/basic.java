package dp.cmd.api;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.Matchers.equalTo;

public class basic {

    Properties prop=new Properties();
    @BeforeTest
    public void getData() throws IOException
    {

        FileInputStream fis=new FileInputStream("//Users//manojpathi//IdeaProjects//CMD_API_Automation//src//main//resources//env.properties");
        prop.load(fis);

        //prop.get("HOST");
    }

//@Test
//    public void AddandDeletePlace()
//    {
//
//        //Task 1- Grab the response
//        RestAssured.baseURI= prop.getProperty("HOST");
//        Response res=given().
//
//                queryParam("key",prop.getProperty("KEY")).
//                body(payLoad.getPostData()).
//                when().
//                post(resources.placePostData()).
//                then().assertThat().statusCode(200).and().contentType(ContentType.JSON).and().
//                body("status",equalTo("OK")).
//                extract().response();
//
//        // Task 2- Grab the Place ID from response
//
//        String responseString=res.asString();
//        System.out.println(responseString);
//        JsonPath js= new JsonPath(responseString);
//        String placeid=js.get("place_id");
//        System.out.println(placeid);
//
//
//        //Task 3 place this place id in the Delete request
//        given().
//                queryParam("key",prop.getProperty("KEY")).
//
//                body("{"+
//                        "\"place_id\": \""+placeid+"\""+
//                        "}").
//                when().
//                post(resources.placePostDeleteData()).
//                then().assertThat().statusCode(200).and().contentType(ContentType.JSON).and().
//                body("status",equalTo("OK"));
//
//
//    }

    @Test
    public void CreateAnImportJobAndGetInfoAboutAnInstance(){

        //Task 1- Create an Import Job
        RestAssured.baseURI= prop.getProperty("HOST");
        Response res=given().
                body(payLoad.getImportJobPostData()).
                when().
                post(resources.importJobsPostData()).
                then().assertThat().statusCode(200).and().contentType(ContentType.JSON).and().
                extract().response();

        //Task 2 - Grab the instance id from the response

        String responseString=res.asString();
        System.out.println(responseString);
        JsonPath js= new JsonPath(responseString);
        ArrayList<String> instanceid=js.get("instanceIds");

        System.out.println(instanceid);

        // Task 3 - Get Information about an Instance
        when().get("/instances/"+instanceid.get(0)).
                then().assertThat().statusCode(200).and().contentType(ContentType.JSON).and().
                body("instanceId", equalTo(instanceid.get(0))).and().
                body("state", equalTo("Created"));

    }
}