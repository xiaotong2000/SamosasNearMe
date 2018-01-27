package com.samosasnearme;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.gson.Gson;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

class SamosaService {

  private static final String BASE_URL = "https://emy91u4lqc.execute-api.us-west-1.amazonaws.com/";

  @Nullable
  private SamosaApi mSamosaApi;

  Observable<SamosaResponse> getRestaurants(String query, String lat, String lng) {
    final Observable<SamosaResponse> call = getService().getFoodInfo(lat, lng, query);
    return call.subscribeOn(Schedulers.io())
      .observeOn(AndroidSchedulers.mainThread());
  }

  interface SamosaApi {
    @GET("prod/samosas?")
    Observable<SamosaResponse> getFoodInfo(@Query("latitude") String lat, @Query("longitude") String lng, @Query("query") String query);
  }

  @NonNull
  private SamosaApi getService() {
    if (mSamosaApi == null) {
      HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
      logging.setLevel(HttpLoggingInterceptor.Level.BODY);
      OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

      httpClient.addInterceptor(logging);

      final Retrofit restAdapter = new Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(httpClient.build())
        .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create(new Gson()))
        .build();
      mSamosaApi = restAdapter.create(SamosaApi.class);
    }
    return mSamosaApi;
  }
}
