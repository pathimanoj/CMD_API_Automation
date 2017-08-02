package dp.cmd.api.Tests;
import dp.cmd.api.payLoad.filterAPI;
import dp.cmd.api.resources.filterApiResources;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.hamcrest.Matchers;
import org.testng.ITestContext;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.Matchers.equalTo;

/** FILTER - Used to filter published datasets */

public class FilterAPI {

    Properties prop=new Properties();

    @BeforeTest
    public void getData() throws IOException
    {
        FileInputStream fis=new FileInputStream("//Users//manojpathi//IdeaProjects//CMD_API_Automation//src//main//resources//env.properties");
        prop.load(fis);


        RestAssured.baseURI= prop.getProperty("HOST");
    }


    /** POST Request - "/filters" - Create a filter job for a dataset
     *  Create a job so that dimensions can be added to filter a dataset

     *  201 - Job was created */
    @Test(groups = {"createFilterJob"})
    public void CreateAFilterJob(ITestContext context){

        Map<String,String> filterJob = new HashMap<String, String>();
        filterJob.put("dataset", "CensusEthnicity");
        filterJob.put("edition", "1");
        filterJob.put("version", "1");
        filterJob.put("state", "created");

        Map<String, String>dimensions = new HashMap<String, String>();
        dimensions.put("name", "time");
        dimensions.put("values", "1997");
        filterJob.put("dimensions", dimensions.toString());


        Response res= given().
                body(filterJob).
                when().
                post(filterApiResources.filterJobPostData()).
                then().assertThat().statusCode(201).and().contentType(ContentType.JSON).and().
                extract().response();

        /** Task 1 - Grab the filter id and other info from response */

        String responseString=res.asString();
        JsonPath js= new JsonPath(responseString);
        String filter_id = js.get("filter_id");
        context.setAttribute("filter_id", filter_id);

    }

    /** 400 - Invalid json message was sent to create a filter job API endpoint */
    @Test(groups = {"createFilterJob"})
    public void InvalidRequestBodyForFilterJob(ITestContext context){

        Map<String,String> filterJob = new HashMap<String, String>();

        filterJob.put("edition", "1");
        filterJob.put("version", "1");
        filterJob.put("state", "created");

        Map<String, String>dimensions = new HashMap<String, String>();
        dimensions.put("name", "time");
        dimensions.put("values", "1997");
        filterJob.put("dimensions", dimensions.toString());

       given().
                body(filterJob).
                when().
                post(filterApiResources.filterJobPostData()).
                then().assertThat().statusCode(400).and().contentType(ContentType.JSON);
    }

    /** GET Request - "/filters/{filter_id}" - Get a description of a filter job
     *  Get the state of the filter job

     *  200 - The job was found and a JobState is returned */
    @Test(groups = {"filterJobState"}, dependsOnGroups = {"createFilterJob"})
    public void GetTheStateOfFilterJob(ITestContext context){

        when().get("/filters/"+context.getAttribute("filter_id")).
                then().assertThat().statusCode(200).contentType(ContentType.JSON).and().
                body("filter_id", equalTo(context.getAttribute("filter_id"))).and().
                body("state", equalTo("created"));

    }

}
