package dp.cmd.api;


public class resources {

    public static String placePostData()
    {

        String res="/maps/api/place/add/json";
        return res;
    }

    public static String placePostDeleteData(){
        String res="/maps/api/place/delete/json";
        return res;
    }

    public static String importJobsPostData(){
        String res = "/jobs";
        return res;
    }

}
