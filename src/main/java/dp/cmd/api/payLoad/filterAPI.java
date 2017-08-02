package dp.cmd.api.payLoad;

public class filterAPI {

    public static String getFilterJobPostData() {

        String data = "{\n" +
                "  \"dataset\": \"CensusEthnicity\",\n" +
                "  \"edition\": \"1\",\n" +
                "  \"version\": \"1\",\n" +
                "  \"state\": \"created\",\n" +
                "  \"dimensions\": [\n" +
                "    {\n" +
                "      \"name\": \"time\",\n" +
                "      \"values\": [\n" +
                "        \"1997\",\n" +
                "        \"1998\"\n" +
                "      ]\n" +
                "    }\n" +
                "  ]\n" +
                " }";

        return data;
    }
}
