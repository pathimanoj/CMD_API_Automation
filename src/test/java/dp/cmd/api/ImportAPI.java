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

public class ImportAPI {

    Properties prop=new Properties();

    @BeforeTest
    public void getData() throws IOException
    {
        FileInputStream fis=new FileInputStream("//Users//manojpathi//IdeaProjects//CMD_API_Automation//src//main//resources//env.properties");
        prop.load(fis);
    }

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


