package us.ttyl.sprinklercontroller;

import android.util.Log;

import java.io.IOException;

import retrofit.Call;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by test on 12/15/2016.
 */
public class SprinklerDao {

    public static final String TAG = "SprinklerDao";

    private static SprinklerDao mSprinklerDao;

    String ip = "192.168.10.118";
    String port = "8081";

    private SprinklerDao() {
    }

    public static synchronized SprinklerDao getInstance() {
        if (mSprinklerDao == null) {
            mSprinklerDao = new SprinklerDao();
        }
        return mSprinklerDao;
    }

    public SprinklerData getSprinklerData() throws IOException{
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://"+ip+":" + port)
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        SprinklerService service = retrofit.create(SprinklerService.class);

        Call<SprinklerData> call = service.getSprinklerTimes();
        Response<SprinklerData> data = call.execute();
        return data.body();
    }

    public SprinklerData updateSprinkerItem(long id, String day, String start, String end, String zone) throws IOException{
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://"+ip+":" + port)
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        SprinklerService service = retrofit.create(SprinklerService.class);

        Call<SprinklerData> call = service.updateSprinkerItem(id, day, start, end, zone);
        Response<SprinklerData> data = call.execute();
        return data.body();
    }

    public SprinklerData addSprinklerItem(String day, String start, String end, String zone) throws IOException{
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://"+ip+":" + port)
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        SprinklerService service = retrofit.create(SprinklerService.class);
        Log.i(TAG, day + "|" + start + "|" + end + "|" + zone);
        Call<SprinklerData> call = service.addSprinklerItem(day, start, end, zone);
        Response<SprinklerData> data = call.execute();
        return data.body();
    }

    public SprinklerData removeItem(long id) throws IOException {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://"+ip+":" + port)
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        SprinklerService service = retrofit.create(SprinklerService.class);

        Call<SprinklerData> call = service.addSprinklerItem(id);
        Response<SprinklerData> data = call.execute();
        return data.body();
    }

    public CurrentTime getCurrentTime() throws IOException {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://"+ip+":" + port)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        SprinklerService service = retrofit.create(SprinklerService.class);

        Call<CurrentTime> call = service.getCurrentTime();
        Response<CurrentTime> data = call.execute();
        return data.body();
    }
}
