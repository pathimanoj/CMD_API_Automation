package dp.cmd.api.Tests;

import dp.cmd.api.Util;
import dp.cmd.api.payLoad.payLoad;
import dp.cmd.api.resources.resources;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.ITestContext;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

public class ImportAPI {

    Properties prop=new Properties();

    @BeforeTest
    public void getData() throws IOException
    {
        FileInputStream fis=new FileInputStream("//Users//manojpathi//IdeaProjects//CMD_API_Automation//src//main//resources//env.properties");
        prop.load(fis);

        RestAssured.baseURI= prop.getProperty("ImportAPI_Host");
    }


    /** POST Request - "/jobs" - Create an import job
     * To import a dataset a job must be created first. To do this a data baker recipe is needed
     * and the number of instances which the recipe creates.
     * Once a job is created files can be added to the job and the state of the job can be changed.

     * 201 - An import job was successfully created */

    @Test(groups = {"createImportJob"})
    public void CreateAnImportJob(ITestContext context) throws IOException{

        Response res=given().
                body(payLoad.getImportJobPostData()).
                when().
                post(resources.importJobsPostData()).
                then().assertThat().statusCode(201).and().contentType(ContentType.JSON).and().
                extract().response();

        /** Task 1 - Grab the job id and instance id from the import job response */

        String responseString=res.asString();
        JsonPath js= new JsonPath(responseString);
        String job_id = js.get("job_id");
        context.setAttribute("job_id", job_id);

        String instanceId = js.get("instances[0].id");
        context.setAttribute("instanceId", instanceId);
    }

    /** 400 - Invalid json message was sent to the import job API */

    @Test(groups = {"createImportJob"})
    public void CheckInvalidJsonResponseForImportJob(){

        given().
                body(payLoad.getInvalidJsonImportJobData()).
                when().
                post(resources.importJobsPostData()).
                then().assertThat().statusCode(400);
    }


    /** PUT Request - "/jobs/{job_id}" - Update the jobs state
     *  Update the state of the job. If this is set to submitted, this shall trigger the
     *  import process

     *  200 - The job is in a queue */

    @Test(groups = {"updatejobstatus"}, dependsOnGroups = {"createImportJob"})
    public void UpdateTheJobStatus(ITestContext context) throws IOException{

        given().
                body(payLoad.putJobsState()).
                when().
                put(resources.jobsStatePutData() + context.getAttribute("job_id")).
                then().assertThat().statusCode(200);
    }

    /** 400 - Invalid json message was sent to the update the job status API */
    @Test(groups = {"updatejobstatus"}, dependsOnGroups = {"createImportJob"} )
    public void CheckInvalidJsonResponseForJobStatus(ITestContext context){

        given().
                body(payLoad.getInvalidJsonJobsState()).
                when().
                put(resources.jobsStatePutData() + context.getAttribute("job_id")).
                then().assertThat().statusCode(400);
    }

    /** This test will fail as there is a bug in the code. Dev will fix soon.
     *  404 - JobId does not match any import jobs */

    @Test(groups = {"updatejobstatus"}, dependsOnGroups = {"createImportJob"} )
    public void CheckJobIDDontMatchAnyImportJobs(ITestContext context){

        given().
                body(payLoad.putJobsState()).
                when().
                put(resources.jobsStatePutData() + context.getAttribute("job_id") + Util.getRandomInt(3)).
                then().assertThat().statusCode(404);
    }

    /** PUT Request - "/jobs/{job_id}/files" - Add a file into a job
     *  Add a file into a job, for each file added an alias name needs to be given. This name needs to link to the recipe

     *  200 - The file was added to the import job */

    @Test(groups = {"addfiletojob"}, dependsOnGroups = {"createImportJob"} )
    public void AddAFileIntoJob(ITestContext context){

        given().
                body(payLoad.putAddAFileIntoAJob()).
                when().
                put(resources.jobsStatePutData() + context.getAttribute("job_id") + "/files").
                then().assertThat().statusCode(200);
    }

    /** 400 - Invalid json message was sent to the Add a file into a job API */
    @Test(groups = {"addfiletojob"}, dependsOnGroups = {"createImportJob"} )
    public void CheckInvalidJsonResponseForAddAFileIntoJob(ITestContext context){
        given().
                body(payLoad.getInvalidJsonAddFileToJob()).
                when().
                put(resources.jobsStatePutData() + context.getAttribute("job_id") + "/files").
                then().assertThat().statusCode(400);
    }

    /** 404 - JobId does not match any import jobs */
    @Test(groups = {"addfiletojob"}, dependsOnGroups = {"createImportJob"} )
    public void CheckJobIDDontMatchAnyImportJobsInAddFiles(ITestContext context){

        given().
                body(payLoad.putAddAFileIntoAJob()).
                when().
                put(resources.jobsStatePutData() + context.getAttribute("job_id")+ Util.getRandomInt(3) + "/files").
                then().assertThat().statusCode(404).body(containsString("Import job not found"));

    }

    /** GET Request - "/instances/{instance_id}" - Get Information about an instance
     *  Get the current state of an instance, this includes all events which have happened.

     *  200 - Return a single instance state */
    @Test(groups = {"InfoAboutAnInstance"}, dependsOnGroups = {"createImportJob"} )
    public void GetInfoAboutAnInstance(ITestContext context){

        when().get("/instances/"+context.getAttribute("instanceId")).
                then().assertThat().statusCode(200).and().contentType(ContentType.JSON).and().
                body("instance_id", equalTo(context.getAttribute("instanceId"))).and().
                body("state", equalTo("Created"));

    }

    /** 404 - InstanceId does not match any import jobs */
    @Test(groups = {"InfoAboutAnInstance"}, dependsOnGroups = {"createImportJob"} )
    public void CheckIntanceIDDontMatchAnyImportJobs(ITestContext context) {
        when().get("/instances/" + context.getAttribute("instanceId") + Util.getRandomInt(3)).
                then().assertThat().statusCode(404);

    }
}


