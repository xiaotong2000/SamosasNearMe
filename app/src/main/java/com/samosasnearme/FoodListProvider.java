package com.samosasnearme;

import java.util.ArrayList;
import java.util.List;

public class FoodListProvider {

  final List<SamosaResponse.Options> items;

  public FoodListProvider() {
    items = new ArrayList<>();
  }

  public SamosaResponse.Options itemAt(int position) {
    return items.get(position);
  }

  public int count() {
    return items.size();
  }

  public void setItems(List<SamosaResponse.Options> newList) {
    items.clear();
    items.addAll(newList);
  }

  void clear(){
    items.clear();
  }
}
