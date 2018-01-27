package com.samosasnearme;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import rx.Observer;
import rx.subscriptions.CompositeSubscription;

import java.util.ArrayList;
import java.util.List;

public class FindSamosasActivity extends AppCompatActivity {

  private static final String[] PERMISSIONS = {
    Manifest.permission.ACCESS_FINE_LOCATION,
    Manifest.permission.ACCESS_COARSE_LOCATION
  };

  public static final String DEFAULT_LATITUDE = "37.7568622";
  public static final String DEFAULT_LONGITUDE = "-122.413669";
  private static final String TAG = "FindSamosasActivity";

  private FoodListProvider mProvider;
  private FoodViewModule mViewModule;
  private SamosaService mSamosaService;
  private LocationManager mLocationManager;
  private String mLatitude = DEFAULT_LATITUDE;
  private String mLongitude = DEFAULT_LONGITUDE;
  private final CompositeSubscription mSubscriptions = new CompositeSubscription();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.find_samosas);

    final View addLocation = findViewById(R.id.add_location);
    ActivityCompat.requestPermissions(this, PERMISSIONS, 1089);

    addLocation.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        // TODO : Add logic to add custom location here
      }
    });
    final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

    mProvider = new FoodListProvider();
    mViewModule = new FoodViewModule(this, recyclerView);

    mViewModule.setAdapter(new LocationListAdapter(this, mProvider));
    mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
    mSamosaService = new SamosaService();

    final List<String> list = new ArrayList<>();
    list.add("samosas");
    list.add("sushi");
    list.add("ice cream");
    list.add("burrito");
    list.add("Pho");
    list.add("brunch");
    setUpFoodPills(list);
  }

  private void setUpFoodPills(final List<String> list) {
    final ViewGroup parent = (RelativeLayout) findViewById(R.id.parent_pills);

    // Add logic to set click listener on all the pills
    final int pillsCount = list.size() < parent.getChildCount() ? list.size() : parent.getChildCount();
    for (int i = 0; i < pillsCount; i++) {
      final TextView view = (TextView) parent.getChildAt(i);
      final String foodString = list.get(i);
      view.setVisibility(View.VISIBLE);
      view.setText(foodString);

      view.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          mProvider.clear();
          mViewModule.refreshList();
          getMeSuggestions(foodString);
        }
      });
    }
  }

  private void getMeSuggestions(String name) {
    mSubscriptions.add(mSamosaService.getRestaurants(name, mLatitude, mLongitude).subscribe(new Observer<SamosaResponse>() {
      @Override
      public void onCompleted() {
        // np-op
      }

      @Override
      public void onError(Throwable throwable) {
        Log.e(TAG, "APi call resulted in a failure - " + throwable.getMessage());
      }

      @Override
      public void onNext(SamosaResponse samosaResponse) {
        final List<SamosaResponse.Options> list = samosaResponse.list;
        setResponseOnUI(list);
      }
    }));
  }

  private void setResponseOnUI(List<SamosaResponse.Options> list) {
    mProvider.setItems(list);
    mViewModule.refreshList();
  }

  @SuppressLint("MissingPermission")
  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    if (requestCode != 1089) {
      return;
    }

    if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
      Log.v(TAG, "Permissions were granted");
      // Get location from SDK
      final Location location = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
      mLongitude = Double.toString(location.getLongitude());
      mLatitude = Double.toString(location.getLatitude());
    } else {
      Log.e(TAG, "Permissions were not granted");
    }
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    mSubscriptions.clear();
  }
}
