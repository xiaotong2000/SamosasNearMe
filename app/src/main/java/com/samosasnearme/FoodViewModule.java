package com.samosasnearme;


import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

public class FoodViewModule {

  private final Context mContext;
  private final RecyclerView mRecyclerView;

  private LocationListAdapter mAdapter;

  public FoodViewModule(Context context, RecyclerView recyclerView) {
    this.mContext = context;
    this.mRecyclerView = recyclerView;

    recyclerView.setHasFixedSize(true);

    // use a linear layout manager
    recyclerView.setLayoutManager(new LinearLayoutManager(context));

  }

  public void setAdapter(LocationListAdapter adapter) {
    // specify an adapter (see also next example)
//    mAdapter = new LocationListAdapter(context);
    mAdapter = adapter;
    mRecyclerView.setAdapter(mAdapter);
  }

  public void refreshList() {
    if (mAdapter != null) {
      mAdapter.notifyDataSetChanged();
    }
  }
}
