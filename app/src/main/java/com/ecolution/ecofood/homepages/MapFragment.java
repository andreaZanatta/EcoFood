package com.ecolution.ecofood.homepages;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.ecolution.ecofood.R;
import com.ecolution.ecofood.model.SellerModel;
import com.ecolution.ecofood.model.UserModel;
import com.ecolution.ecofood.shopdetail.ShopDetailsActivity;
import com.ecolution.ecofood.shoplist.ShopListActivity;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MapFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap googleMap;
    private List<SellerModel> shopList;
    private List<String> addresses;
    private FirebaseFirestore db;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_map, container, false);

        // db initialization
        db = FirebaseFirestore.getInstance();

        // initializer
        shopList = new ArrayList<>();
        addresses = new ArrayList<>();

        // Inizializza il SupportMapFragment
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);


        if (mapFragment != null) mapFragment.getMapAsync(this);
        else Log.e("Debug", "MapFragment is null");

        return view;
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        this.googleMap = googleMap;


        // Check if location permission is granted
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // Enable location layer on the map
            googleMap.setMyLocationEnabled(true);

            // Get user's current location and zoom to that
            FusedLocationProviderClient fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());
            fusedLocationProviderClient.getLastLocation()
                    .addOnSuccessListener(getActivity(), location -> {
                        if (location != null) {
                            LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());

                            // Move and zoom to the user's location
                            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15)); // Zoom level 15 for street view
                        } else {
                            Toast.makeText(getContext(), "Unable to get your location", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            // Request permissions if not granted
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }

        populateShopList(() -> {
            List<LatLng> locations = new ArrayList<>();
            Geocoder geocoder = new Geocoder(getContext());

            for (SellerModel shop : shopList) {
                try {
                    List<Address> addressList = geocoder.getFromLocationName(shop.getAddress(), 1);
                    if (addressList != null && !addressList.isEmpty()) {
                        Address location = addressList.get(0);
                        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                        locations.add(new LatLng(location.getLatitude(), location.getLongitude()));

                        Marker marker = googleMap.addMarker(
                                new MarkerOptions()
                                        .position(latLng)
                                        .title(shop.getShopName()) // Optional: Set title to shopName
                        );

                        if (marker != null) {
                            // Store the shopName and shopId in the marker's tag
                            marker.setTag(shop);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            // DA RIMUOVERE
            //for(String eey : addresses) Log.d("Debug", "TRY DEBUG LOG" + eey);

            // Crea i marker utilizzando il ciclo for
            for (LatLng location : locations) {
                googleMap.addMarker(new MarkerOptions().position(location).title("Sezione"));
            }

            // Imposta un listener per i click sui marker
            googleMap.setOnMarkerClickListener(marker -> {
                SellerModel shop = (SellerModel) marker.getTag();
                if(shop != null){
                    Intent intent = new Intent(getActivity(), ShopDetailsActivity.class);
                    intent.putExtra("shopName", shop.getShopName()); // Pass shopName
                    intent.putExtra("shopId", shop.getUser_id()); // Pass shopId
                    intent.putExtra("from", "map");
                    startActivity(intent);
                }
                return false;
            });
        });
    }

    private void populateShopList(@NonNull Runnable onComplete){
        db.collection("users").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            for(QueryDocumentSnapshot doc : task.getResult()) {
                                UserModel document = doc.toObject(UserModel.class);
                                if(document.isSeller()){
                                    SellerModel shop = doc.toObject(SellerModel.class);
                                    shopList.add(shop);
                                    if(shop.getAddress() != null) {
                                        addresses.add(shop.getAddress());
                                    }
                                }
                            }
                        } else Log.e("Error", "Error loading map");
                        onComplete.run();
                    }
                });
    }
}
