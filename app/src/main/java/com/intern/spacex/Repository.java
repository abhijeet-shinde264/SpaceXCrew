package com.intern.spacex;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.intern.spacex.Constant.BASE_URL;


public class Repository {

    public final String TAG = Repository.class.getName();
    private final AppDao dao;
    private ResponseCallback mListener;

    public Repository(Context context) {
        dao = AppDatabase.getInstance(context).dao();
    }

    public void setCallback(ResponseCallback callback){
        mListener = callback;
    }

    public void fetchCrewMember() {
        CrewApi api  = RetrofitBuilder.getInstance(BASE_URL).create(CrewApi.class);

        Call<List<CrewMember>> call = api.getCrewMembers();

        call.enqueue(new Callback<List<CrewMember>>() {
            @Override
            public void onResponse(Call<List<CrewMember>> call, Response<List<CrewMember>> response) {
                if(!response.isSuccessful()) {
                    Log.d(TAG, "onResponse: something went wrong");
                    if(mListener != null) {
                        mListener.onFailed("No data found");
                    }
                    return ;
                }

                List<CrewMember> crewMembers = response.body();
                if (response.body() != null) {
                    Log.d(TAG, "onResponse: " + crewMembers.toString());
                    mListener.onSuccessful();
                    ArrayList<CrewMember> members = new ArrayList<>(crewMembers);
                    clearAllAndInsert(members);
                }
            }

            @Override
            public void onFailure(Call<List<CrewMember>> call, Throwable t) {
                Log.d(TAG, "onFailure: Bad network");
                Log.d(TAG, "onFailure:  error message = " + t.getMessage());
                mListener.onFailed("Bad network :(");
            }
        });

    }

    private void clearAllAndInsert(ArrayList<CrewMember> crewMembers) {
        Log.d(TAG, "clearAllAndInsert: "+ crewMembers.toString());
        AppDatabase.databaseWriteExecutor.execute(() -> {
            dao.deleteAll();
            dao.insertMembers(crewMembers);
        });
    }

}
