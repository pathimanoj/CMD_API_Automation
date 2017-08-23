package dp.cmd.api.Tests;
import dp.cmd.api.Util;
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
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;

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
        filterJob.put("dataset_filter_id", "CensusEthnicity");
        filterJob.put("state", "created");

       // Map<String, String>dimensions = new HashMap<String, String>();
       // dimensions.put("name", "age");
      //  dimensions.put("values", "26");
      //  filterJob.put("dimensions", dimensions.toString());


        Response res= given().
                body(filterJob).
                when().
                post(filterApiResources.filterJobPostData()).
                then().assertThat().statusCode(201).and().contentType(ContentType.JSON).and().
                body("dataset_filter_id", equalTo(filterJob.get("dataset_filter_id"))).and().
                body("state", equalTo(filterJob.get("state"))).and().
                body("dimensions.name", equalTo(filterJob.get("dimensions.name"))).and().
                body("dimensions.values", equalTo(filterJob.get("dimensions.values"))).and().
                body("downloads[2].xls", nullValue()).and().
                body("downloads[1].json", nullValue()).and().
                body("downloads[0].csv", nullValue()).and().
                extract().response();

        /** Task 1 - Grab the filter id and other info from response */

        String responseString=res.asString();
        JsonPath js= new JsonPath(responseString);
        String filter_job_id = js.get("filter_job_id");
        context.setAttribute("filter_job_id", filter_job_id);
        String dataset_filter_id = js.get("dataset_filter_id");
        context.setAttribute("dataset_filter_id", dataset_filter_id);
        String state = js.get("state");
        context.setAttribute("state", state);
        String dimensions_name = js.get("dimensions.name");
        context.setAttribute("dimensions_name", dimensions_name);
        String dimensions_values = js.get("dimensions.values");
        context.setAttribute("dimensions_values", dimensions_values);
        String downloads_xls_url = js.get("downloads.xls.url");
        context.setAttribute("downloads_xls_url", downloads_xls_url);
        String downloads_xls_size = js.get("downloads.xls.size");
        context.setAttribute("downloads_xls_size", downloads_xls_size);
        String downloads_json_url = js.get("downloads.json.url");
        context.setAttribute("downloads_json_url", downloads_json_url);
        String downloads_json_size = js.get("downloads.json.size");
        context.setAttribute("downloads_json_size", downloads_json_size);
        String downloads_csv_url = js.get("downloads_csv_url");
        context.setAttribute("downloads_csv_url", downloads_csv_url);
        String downloads_csv_size = js.get("downloads.csv.size");
        context.setAttribute("downloads_csv_size", downloads_csv_size);

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
                then().assertThat().statusCode(400).and().contentType(ContentType.TEXT);
    }

    /** GET Request - "/filters/{filter_id}" - Get a description of a filter job
     *  Get the state of the filter job

     *  200 - The job was found and a JobState is returned */
    @Test(groups = {"filterJobState"}, dependsOnGroups = {"createFilterJob"})
    public void GetTheStateOfFilterJob(ITestContext context){

        when().get("/filters/"+context.getAttribute("filter_job_id")).
                then().assertThat().statusCode(200).contentType(ContentType.JSON).and().
                body("filter_job_id", equalTo(context.getAttribute("filter_job_id"))).and().
                body("dataset_filter_id", equalTo(context.getAttribute("dataset_filter_id"))).and().
                body("state", equalTo(context.getAttribute("state"))).and().
                body("dimensions.name", equalTo(context.getAttribute("dimensions.name"))).and().
                body("dimensions.values", equalTo(context.getAttribute("dimensions.values"))).and().
                body("downloads.xls.url", equalTo(context.getAttribute("downloads_xls_url"))).and().
                body("downloads.xls.size", equalTo(context.getAttribute("downloads_xls_size"))).and().
                body("downloads.json.url", equalTo(context.getAttribute("downloads_json_url"))).and().
                body("downloads.json.size", equalTo(context.getAttribute("downloads_json_size"))).and().
                body("downloads.csv.url", equalTo(context.getAttribute("downloads_csv_url"))).and().
                body("downloads.csv.size", equalTo(context.getAttribute("downloads_csv_size")));

    }

    /**  404 - Filter Job not found */
    @Test(groups = {"filterJobState"}, dependsOnGroups = {"createFilterJob"})
    public void InvalidFilterJob(ITestContext context){

        when().get("/filters/"+context.getAttribute("filter_id") + Util.getRandomInt(3)).
                then().assertThat().statusCode(404).contentType(ContentType.TEXT);

    }

    /** PUT Request - "/filters/{filter_id}" - Update the state of the job
     *  Update the filter job by providing new properties

     *  200 - The state has been updated */
    @Test(groups = {"updateFilterJobState"}, dependsOnGroups = {"createFilterJob"})
    public void UpdateTheFilterJob(ITestContext context){

        Map<String,String> filterJob = new HashMap<String, String>();
        filterJob.put("dataset", "CensusEthnicity");
        filterJob.put("state", "submitted");

        Map<String, String>dimensions = new HashMap<String, String>();
        dimensions.put("name", "time");
        dimensions.put("options", "1997");
        dimensions.put("name", "time");
        dimensions.put("options", "2002");
        filterJob.put("dimensions", dimensions.toString());


        given().
                body(filterJob).
                when().
                put(filterApiResources.filterJobPostData() + context.getAttribute("filter_id")).
                then().assertThat().statusCode(200).and().contentType(ContentType.JSON);

    }

    /**  400 - The job has been locked as it has been submitted to be processed */
    @Test(groups = {"updateFilterJobState"}, dependsOnGroups = {"createFilterJob"})
    public void UpdateTheSubmittedFilterJob(ITestContext context){

        Map<String,String> filterJob = new HashMap<String, String>();
        filterJob.put("dataset", "CensusEthnicity");
        filterJob.put("edition", "1");
        filterJob.put("version", "1");
        filterJob.put("state", "submitted");

        Map<String, String>dimensions = new HashMap<String, String>();
        dimensions.put("name", "time");
        dimensions.put("values", "1997");
        dimensions.put("name", "time");
        dimensions.put("values", "2012");
        filterJob.put("dimensions", dimensions.toString());

        given().
                body(filterJob).
                when().
                put(filterApiResources.filterJobPostData() + context.getAttribute("filter_id")).
                then().assertThat().statusCode(400).and().contentType(ContentType.JSON);

    }

}
