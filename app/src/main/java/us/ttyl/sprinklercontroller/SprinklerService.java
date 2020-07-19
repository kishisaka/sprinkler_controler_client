package us.ttyl.sprinklercontroller;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Path;

/**
 * Created by test on 12/15/2016.
 */

public interface SprinklerService {

    @GET("currenttime")
    Call<CurrentTime> getCurrentTime();

    @GET("listitems")
    Call<SprinklerData> getSprinklerTimes();

    /**
     * http://192.168.10.118:8081/updateItem/1/3/20:07/20:08/1
     * @return
     */
    @GET("updateitem/{id}/{day}/{start}/{end}/{zone}")
    Call<SprinklerData> updateSprinkerItem(@Path("id") long id, @Path("day") String day, @Path("start") String start, @Path("end") String end, @Path("zone") String zone);

    /**
     * http://192.168.10.118:8081/addItem/5/10:10/10:11/1
     * @return
     */
    @GET("additem/{day}/{start}/{end}/{zone}")
    Call<SprinklerData> addSprinklerItem(@Path("day") String day, @Path("start") String start, @Path("end") String end, @Path("zone") String zone);

    /**
     * http://192.168.10.118:8081/removeitem/5
     * @return
     */
    @GET("removeitem/{id}")
    Call<SprinklerData> addSprinklerItem(@Path("id") long id);

    @GET("status/")
    Call<SprinklerStatus> getSprinklerStatus();

}
