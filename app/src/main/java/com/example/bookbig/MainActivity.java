package com.example.bookbig;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bookbig.bookcover.Bookcover;
import com.example.bookbig.bookcover.BookcoverAdapter;
import com.example.bookbig.hifive.HiFiveActivity;
import com.example.bookbig.setting.UserInfoActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "Main Activity :";
    private List<Bookcover> bookcoverUserList;
    private BookcoverAdapter bookcoverAdapter;
    private TextView textView;
    private FirestoreOperation firestoreOperation;
    private List<Bookcover> bookcoverList;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private List<Profile> userInArea;
    private float distance;
    private Button mDiscoverySetting;
    private TextView mDiscoveryText;
    private Dialog dialog;
    private TextView distanceLabel;
    private LinearLayout mDiscoveryLayout;
    private Double latitude;
    private Double longtitude;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firestoreOperation = new FirestoreOperation();
        bookcoverList = new ArrayList<>();
        bookcoverUserList = new ArrayList<>();
        userInArea = new ArrayList<>();

        textView = findViewById(R.id.text);
        mDiscoverySetting = findViewById(R.id.discoveryButton);
        mDiscoveryText = findViewById(R.id.discoveryText);
        mDiscoveryLayout = findViewById(R.id.discoveryLayout);
        dialog = new Dialog(this);
        //Get user Max Distance
        firestoreOperation.getCurrentUserAccountRef()
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                                distance = (float) document.getLong("maxDistance");
                                Log.d(TAG, "Max D:  " + distance);
                            } else {
                                Log.d(TAG, "No such document");
                            }
                        } else {
                            Log.d(TAG, "get failed with ", task.getException());
                        }
                    }
                });

        textView.append("Latitude: " + latitude + "Longtitude: " + longtitude);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                latitude = location.getLatitude();
                longtitude = location.getLongitude();
                Log.d("Location", "Latitude: " + latitude);
                Log.d("Location", "Longtitude: " + longtitude);
                textView.append("Latitude: " + latitude + "Longtitude: " + longtitude);

                firestoreOperation.updateUserLocation(String.valueOf(latitude), String.valueOf(longtitude));
                getUserInYourArea(location, distance);

            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {
//                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//                startActivity(intent);
            }
        };
        getLocation();


//        filterAlreadyLikeBookcover();
        bookcoverAdapter = new BookcoverAdapter(MainActivity.this, R.layout.item, bookcoverList);
        final SwipeFlingAdapterView flingContainer = findViewById(R.id.frame);
        flingContainer.setAdapter(bookcoverAdapter);
        flingContainer.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {
                // this is the simplest way to delete an object from the Adapter (/AdapterView)
                Log.d("LIST", "removed object!");
                bookcoverList.remove(0);
                bookcoverAdapter.notifyDataSetChanged();
            }

            @Override
            public void onLeftCardExit(Object dataObject) {
                //Do something on the left!
                //You also have access to the original object.
                //If you want to use it just cast it (String) dataObject
                Bookcover bookcover = (Bookcover) dataObject;
                firestoreOperation.setNope(bookcover);

                Toast.makeText(MainActivity.this, "Left", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRightCardExit(Object dataObject) {
                final Bookcover bookcover = (Bookcover) dataObject;
                firestoreOperation.setLike(bookcover);
                firestoreOperation.isHiFive(bookcover.getUserId());
                Toast.makeText(MainActivity.this, "Right", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdapterAboutToEmpty(int itemsInAdapter) {
                // Ask for more data here
//                        bookcoverList.add(new Bookcover("test","test","test","test","test"));
//                        bookcoverAdapter.notifyDataSetChanged();
                Log.d("LIST", "notified");

            }

            @Override
            public void onScroll(float scrollProgressPercent) {

            }
        });
        // Optionally add an OnItemClickListener
        flingContainer.setOnItemClickListener(new SwipeFlingAdapterView.OnItemClickListener() {
            @Override
            public void onItemClicked(int itemPosition, Object dataObject) {
                Toast.makeText(MainActivity.this, "Click", Toast.LENGTH_SHORT).show();
            }
        });



        mDiscoverySetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Button mCancel;
                Button mUpdate;


                dialog.setContentView(R.layout.activity_distance_setting);
                mUpdate = dialog.findViewById(R.id.update);
                mCancel = dialog.findViewById(R.id.cancel);
                distanceLabel = dialog.findViewById(R.id.distance);
                distanceLabel.setText("Maximum Distance: " + distance );
                final SeekBar seekBar = dialog.findViewById(R.id.seekBar);
                seekBar.setProgress((int) distance);
                seekBar.setOnSeekBarChangeListener(seekBarChangeListener);

                mCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                mUpdate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int maxDistance = seekBar.getProgress();
                        firestoreOperation.updateUserMaxDistance(maxDistance);
                        distance = maxDistance;
                        dialog.dismiss();
                    }
                });
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
        if(bookcoverList.isEmpty()){
            Log.d(TAG,"EMPTY");
            mDiscoverySetting.setVisibility(View.VISIBLE);
            mDiscoveryText.setVisibility(View.VISIBLE);
            mDiscoveryLayout.setVisibility(View.VISIBLE);
        } else{
            mDiscoverySetting.setVisibility(View.INVISIBLE);
            mDiscoveryText.setVisibility(View.INVISIBLE);
            mDiscoveryLayout.setVisibility(View.INVISIBLE);
        }
    }

    public void goToUserInfo(View view) {
        Intent intent = new Intent(MainActivity.this, UserInfoActivity.class);
        startActivity(intent);
    }

    public void goToHiFive(View view) {
        Intent intent = new Intent(MainActivity.this, HiFiveActivity.class);
        startActivity(intent);
    }


    // Filter
    public void filterBookCover(final List<String> bookcoverIdList) {
        firestoreOperation.getBookcoverRef()
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w(TAG, "Listen failed.", e);
                            return;
                        }
                        for (QueryDocumentSnapshot document : value) {
                            if (document.exists()) {
                                String bookcoverId = document.getData().get("bookcoverId").toString();
                                if (!bookcoverIdList.contains(bookcoverId)) {
                                    Log.d(TAG, document.getId() + " => " + document.getData());
                                    String name = document.getData().get("name").toString();
                                    String description = document.getData().get("description").toString();
                                    String userId = document.getData().get("userId").toString();
                                    String photoUrl = document.getData().get("photoUrl").toString();
                                    Bookcover bookcover = new Bookcover(name, description, photoUrl, userId, bookcoverId);
                                    if (!bookcover.getUserId().equals(firestoreOperation.getCurrentUserId())) {
                                        bookcoverList.add(bookcover);
                                    }
                                }
                            }
                        }
                        bookcoverAdapter.notifyDataSetChanged();
                    }
                });
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 10:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getLocation();
                }

        }
    }


    private void getLocation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    Activity#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for Activity#requestPermissions for more details.
                requestPermissions(new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.INTERNET
                }, 10);
                return;
            }
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1000, locationListener);
    }


    public void getUserInYourArea(final Location currentLocation, final Float distance) {
        firestoreOperation.getUserAccountRef()
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w(TAG, "Listen failed.", e);
                            return;
                        }
                        Log.d(TAG, "HERERERERE");
                        for (QueryDocumentSnapshot document : value) {
                            if (document.exists()) {
                                Log.d(TAG, "WTF " + document.getData().toString());
                                String userId = document.getData().get("userId").toString();
                                String longtitude = document.getData().get("longtitude").toString();
                                String latitude = document.getData().get("latitude").toString();
                                Location location = new Location("");
                                location.setLatitude(Double.valueOf(latitude));
                                location.setLongitude(Double.valueOf(longtitude));
                                Log.d(TAG, "UserProfile :" + document.getData().toString());
                                if (!userId.equals(firestoreOperation.getCurrentUserId())) {
                                    Float calculatedDistance = currentLocation.distanceTo(location);
                                    int distanceInKM = Math.round(calculatedDistance / 1000);
                                    Log.d(TAG, "Distance :" + distanceInKM);
                                    if (distanceInKM <= distance) {
                                        Profile profile = new Profile(userId, latitude, longtitude);
                                        Log.d("Location", "UserId :" + userId);
                                        userInArea.add(profile);

                                    }
                                }
                            }
                        }

                        Log.d("TAG", "Correct User: " + userInArea.toString());
                        filterAlreadyHifiveUser();
                    }
                });
    }


    // Get userId that current user already hifive with
    private void filterAlreadyHifiveUser() {
        firestoreOperation.getHifiveRef()
                .whereArrayContains("userId", firestoreOperation.getCurrentUserId())
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w(TAG, "Listen failed.", e);
                            return;
                        }
                        for (QueryDocumentSnapshot document : value) {
                            if (document.exists()) {
                                Log.d(TAG, "In here" + document.getData().toString());
                                ArrayList<String> id = (ArrayList<String>) document.get("userId");
                                // Filter current user id out of array
                                for (int i = 0; i < id.size(); i++) {
                                    if (id.get(i).equals(firestoreOperation.getCurrentUserId())) {
                                        id.remove(i);
                                        i--;
                                    } else {
                                        Profile profile = new Profile(id.get(i));
                                        if (userInArea.contains(profile)) {
                                            Log.d(TAG, "Remove :" + profile.toString());
                                            userInArea.remove(profile);
                                        }
                                    }
                                }
                            }
                        }
                        Log.d(TAG, "Valid user: " + userInArea.toString());
                        filterAlreadyLikeBookcover();
                    }
                });
    }

    //Filter book cover that user already like out
    public void filterAlreadyLikeBookcover() {
        firestoreOperation.getLikeRef()
                .whereEqualTo("userLikeId", firestoreOperation.getCurrentUserId())
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w(TAG, "Listen failed.", e);
                            return;
                        }
                        final List<String> bookcoverIdList = new ArrayList<>();
                        for (QueryDocumentSnapshot document : value) {
                            Log.d("TAG", "Really: " + document.getData().toString());
                            String bookcoverId = document.getData().get("bookcoverId").toString();
                            bookcoverIdList.add(bookcoverId);
                        }
                        List<String> validUserId = new ArrayList<>();
                        for (Profile p : userInArea) {
                            validUserId.add(p.getUserId());
                        }
                        filterValidBookCover(bookcoverIdList, validUserId);
                    }
                });
    }

    // Filter
    public void filterValidBookCover(final List<String> bookcoverIdList, final List<String> validUserId) {
        firestoreOperation.getBookcoverRef()
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w(TAG, "Listen failed.", e);
                            return;
                        }
                        for (QueryDocumentSnapshot document : value) {
                            if (document.exists()) {
                                String bookcoverId = document.getData().get("bookcoverId").toString();
                                if (!bookcoverIdList.contains(bookcoverId)) {
                                    Log.d(TAG, document.getId() + " => " + document.getData());
                                    String name = document.getData().get("name").toString();
                                    String description = document.getData().get("description").toString();
                                    String userId = document.getData().get("userId").toString();
                                    String photoUrl = document.getData().get("photoUrl").toString();
                                    Bookcover bookcover = new Bookcover(name, description, photoUrl, userId, bookcoverId);
//                                    if (!bookcover.getUserId().equals(firestoreOperation.getCurrentUserId())) {
//                                        bookcoverList.add(bookcover);
//                                    }
                                    if (validUserId.contains(bookcover.getUserId())) {
                                        bookcoverList.add(bookcover);
                                    }
                                }
                            }
                        }
                        if(bookcoverList.isEmpty()){
                            Log.d(TAG,"EMPTY");
                            mDiscoverySetting.setVisibility(View.VISIBLE);
                            mDiscoveryText.setVisibility(View.VISIBLE);
                            mDiscoveryLayout.setVisibility(View.VISIBLE);
                        } else{
                            mDiscoverySetting.setVisibility(View.INVISIBLE);
                            mDiscoveryText.setVisibility(View.INVISIBLE);
                            mDiscoveryLayout.setVisibility(View.INVISIBLE);
                        }
                        Log.d(TAG, "Valid Bookcover: " + bookcoverList.toString());
                        bookcoverAdapter.notifyDataSetChanged();
                    }
                });
    }



    SeekBar.OnSeekBarChangeListener seekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            // updated continuously as the user slides the thumb
            distanceLabel.setText("Maximum Distance: " + progress);
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            // called when the user first touches the SeekBar
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            // called after the user finishes moving the SeekBar
        }
    };


}


