package dp.cmd.api.payLoad;

public class payLoad {

    public static String getPostData() {

        String b = "{" +
                "\"location\": {" +
                "\"lat\": -33.8669710," +
                "\"lng\": 151.1958750" +
                "}," +
                "\"accuracy\": 50," +
                "\"name\": \"Google Shoes!\"," +
                "\"phone_number\": \"(02) 9374 4000\"," +
                "\"address\": \"48 Pirrama Road, Pyrmont, NSW 2009, Australia\"," +
                "\"types\": [\"shoe_store\"]," +
                "\"website\": \"http://www.google.com.au/\"," +
                "\"language\": \"en-AU\"" +
                "}";


        return b;
    }


    public static String getImportJobPostData() {

        String data = "{\n" +
                "  \"recipe\": \"hello\",\n" +
                "  \"state\": \"New\",\n" +
                "  \"files\": [\n" +
                "    {\n" +
                "      \"alias_name\": \"CPI COICOP v4\",\n" +
                "      \"url\": \"https://s3-eu-west-1.amazonaws.com/dp-frontend-florence-file-uploads/8192-teams-report-02-08-2017xls\"\n" +
                "    }\n" +
                "  ]\n" +
                "}";

        return data;
    }

    public static String getInvalidJsonImportJobData(){
        String data = "{\n" +
                "  \"state\": \"New\",\n" +
                "  \"number_of_instances\": 2,\n" +
                "  \"files\": [\n" +
                "    {\n" +
                "      \"alias_name\": \"v4\",\n" +
                "      \"url\": \"https://s3-eu-west-1.amazonaws.com/dp-publish-content-test/OCIGrowth.csv\"\n" +
                "    }\n" +
                "  ]\n" +
                "}";

        return data;
    }

    public static String putJobsState() {

        String data = "{\n" +
                "  \"recipe\": \"hello\",\n" +
                "  \"state\": \"new\",\n" +
                "  \"number_of_instances\": 2,\n" +
                "  \"files\": [\n" +
                "    {\n" +
                "      \"alias_name\": \"v4\",\n" +
                "      \"url\": \"https://s3-eu-west-1.amazonaws.com/dp-publish-content-test/OCIGrowth.csv\"\n" +
                "    }\n" +
                "  ]\n" +
                "}";

        return data;

    }

    public static String getInvalidJsonJobsState(){
        String data = "{\n" +
                "  \"recipe\": \"hello\",\n" + "," +
                "  \"state\": \"new\",\n" +
                "  \"files\": [\n" +
                "    {\n" +
                "      \"alias_name\": \"v4\",\n" +
                "      \"url\": \"https://s3-eu-west-1.amazonaws.com/dp-publish-content-test/OCIGrowth.csv\"\n" +
                "    }\n" +
                "  ]\n" +
                "}";

        return data;
    }

    public static String putAddAFileIntoAJob(){

        String data = "{\n" +
                "  \"alias_name\": \"v4\",\n" +
                "  \"url\": \"https://s3-eu-west-1.amazonaws.com/dp-publish-content-test/OCIGrowth.csv\"\n" +
                "}";

        return data;
    }

    public static String getInvalidJsonAddFileToJob(){
        String data = "{\n" +
                "  \"url\": \"https://s3-eu-west-1.amazonaws.com/dp-publish-content-test/OCIGrowth.csv\"\n" +
                "}";

        return data;
    }
}