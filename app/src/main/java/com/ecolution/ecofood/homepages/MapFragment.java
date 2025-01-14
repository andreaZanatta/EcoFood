package com.ecolution.ecofood.homepages;

import android.content.Intent;
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
import androidx.fragment.app.Fragment;

import com.ecolution.ecofood.R;
import com.ecolution.ecofood.model.SellerModel;
import com.ecolution.ecofood.model.UserModel;
import com.ecolution.ecofood.shopdetail.ShopDetailsActivity;
import com.ecolution.ecofood.shoplist.ShopListActivity;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
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

        populateShopList(() -> {
            List<LatLng> locations = new ArrayList<>();
            Geocoder geocoder = new Geocoder(getContext());

            for (String address : addresses) {
                try {
                    List<Address> addressList = geocoder.getFromLocationName(address, 1);
                    if (addressList != null && !addressList.isEmpty()) {
                        Address location = addressList.get(0);
                        locations.add(new LatLng(location.getLatitude(), location.getLongitude()));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            // DA RIMUOVERE
            for(String eey : addresses) Log.d("Debug", "TRY DEBUG LOG" + eey);

            // Crea i marker utilizzando il ciclo for
            for (LatLng location : locations) {
                googleMap.addMarker(new MarkerOptions().position(location).title("Sezione"));
            }

            // Imposta un listener per i click sui marker
            googleMap.setOnMarkerClickListener(marker -> {
                Intent intent = new Intent(getActivity(), ShopDetailsActivity.class);
                startActivity(intent);
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
