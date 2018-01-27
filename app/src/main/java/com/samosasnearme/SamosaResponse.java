package com.samosasnearme;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SamosaResponse {

  @SerializedName("businesses")
  public List<Options> list;

  class Options {

    @SerializedName("id")
    public String id;

    @SerializedName("name")
    public String name;

    @SerializedName("distance")
    public String distance;
  }
}
