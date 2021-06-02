package com.intern.spacex;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface CrewApi {
    @GET("/v4/crew")
    Call<List<CrewMember>> getCrewMembers();

}
