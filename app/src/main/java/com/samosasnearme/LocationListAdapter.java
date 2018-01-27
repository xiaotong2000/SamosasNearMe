package com.samosasnearme;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class LocationListAdapter extends RecyclerView.Adapter<LocationListAdapter.LocationListViewHolder> {

  private final Context mContext;
  private final FoodListProvider mFoodLocationProvider;

  public LocationListAdapter(Context context, FoodListProvider provider) {
    mContext = context;
    mFoodLocationProvider = provider;
  }

  @Override
  public LocationListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    final View view = LayoutInflater.from(mContext).inflate(R.layout.food_item, parent, false);
    return new LocationListViewHolder(view);
  }

  @Override
  public void onBindViewHolder(LocationListViewHolder holder, int position) {
    holder.bind(mFoodLocationProvider.itemAt(position), position);
  }

  @Override
  public int getItemCount() {
    return mFoodLocationProvider.count();
  }

  public class LocationListViewHolder extends RecyclerView.ViewHolder {

    final TextView mLocationNAme;
    final TextView mPosition;

    public LocationListViewHolder(View itemView) {
      super(itemView);
      mPosition = (TextView) itemView.findViewById(R.id.food_location_name_1);
      mLocationNAme = (TextView) itemView.findViewById(R.id.food_location_name);
    }

    public void bind(SamosaResponse.Options data, int position) {
      final String posStr = Integer.toString(position + 1);
      mPosition.setText(posStr);
      mLocationNAme.setText(data.name);
    }
  }
}
