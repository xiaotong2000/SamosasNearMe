package com.samosasnearme;

import com.google.gson.annotations.SerializedName;

public class FoodRequest {

  @SerializedName("latitude")
  public String latitude;

  @SerializedName("longitude")
  public String longtitude;

  @SerializedName("query")
  public String query;

}
