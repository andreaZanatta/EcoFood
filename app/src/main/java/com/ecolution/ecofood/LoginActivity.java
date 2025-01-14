package com.ecolution.ecofood;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.ecolution.ecofood.homepages.HomeActivity;
import com.ecolution.ecofood.model.CustomerModel;
import com.ecolution.ecofood.model.SellerModel;
import com.ecolution.ecofood.model.UserModel;
import com.ecolution.ecofood.productdetail.ProductListActivity;
import com.ecolution.ecofood.shoplist.ShopListActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class LoginActivity extends AppCompatActivity {
    private EditText etEmail, etPassword;
    private Button btnLogin;
    private TextView tvRegister;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        // Inizializza Firebase
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();


        // Inizializza le views
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);


        // Gestione login
        btnLogin.setOnClickListener(v -> {
            String email = etEmail.getText().toString();
            String password = etPassword.getText().toString();

            if (!email.isEmpty() || !password.isEmpty()) loginUser(email, password);

            else Toast.makeText(this, "Inserisci email e password", Toast.LENGTH_SHORT).show();
        });


        // Naviga alla schermata di registrazione
        tvRegister = findViewById(R.id.tvRegister);

        tvRegister.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }


    //login user function:
    private void loginUser(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                FirebaseUser user = mAuth.getCurrentUser();
                if (user != null) {
                    Log.d("Debug", "user fetched?" + user);
                    String uId = user.getUid();
                    manageSession(uId);
                }
            } else
                Toast.makeText(this, "Login failed: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
        });
    }

    private void manageSession(String uId){
        db.collection("users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()) {
                    for(QueryDocumentSnapshot us : task.getResult()){
                        UserModel user = us.toObject(UserModel.class);
                        if(user.getUser_id().equals(uId)) {

                            SharedPreferences sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("uId", user.getUser_id());
                            editor.putString("email", user.getEmail());
                            editor.putString("firstName", user.getFirstName());
                            editor.putBoolean("userType", user.isSeller());

                            Intent intent;
                            if(user.isSeller()){
                                SellerModel seller = us.toObject(SellerModel.class);
                                editor.putString("shopName", seller.getShopName());
                                editor.apply();

                                intent = new Intent(LoginActivity.this, ProductListActivity.class);
                            } else {
                                //CustomerModel customer = us.toObject(CustomerModel.class);
                                editor.apply();

                                intent = new Intent(LoginActivity.this, ShopListActivity.class);
                            }

                            startActivity(intent);
                            finish();
                        }
                    }
                }
            }
        });
    }
}
    /*
    private void checkUserType(String userId) {
        Log.d("Debug", "Am I checking type");
        db.child(userId).child("seller").get().addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                Boolean isSeller = task.getResult().getValue(Boolean.class);

                if(isSeller != null) {
                    manageSession(isSeller);
                } else Toast.makeText(this, "Error fetching user type.", Toast.LENGTH_SHORT).show();
            } else Toast.makeText(this, "Database error: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
        });
    }

    private void manageSession(boolean isSeller) {
        Intent intent;

        Log.d("Debug", "isSeller è: " + isSeller + "\n Sessione sta per essere gestita!");
        if (isSeller) {
            // Navigate to SellerActivity
            intent = new Intent(LoginActivity.this, HomeActivity.class);
        } else {
            // Navigate to CustomerActivity
            intent = new Intent(LoginActivity.this, MainActivity.class);
        }
        startActivity(intent);
        finish(); // Close LoginActivity
    }
}
*/
/*
    private void loginUser(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                        if(user != null){
                            String userId = user.getUid();
                            String mail = user.getEmail();

                            fetchIsSeller(userId, (isSeller) -> {
                                SharedPreferences sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("userId", userId);
                                editor.putString("email", mail);
                                editor.putBoolean("isSeller", isSeller);
                                debugLogs(userId, mail, "" + isSeller);
                                editor.apply();

                                Toast.makeText(this, "Accesso riuscito!", Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish(); // Chiude la LoginActivity
                            });
                        }
                    } else {
                        Toast.makeText(this, "Errore: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }


*/

    /*
    private void fetchIsSeller(String userId, OnIsSellerFetchedListener listener) {



        db.child("users").child(userId).child("seller").get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Boolean isSeller = task.getResult().getValue(Boolean.class);
                        if (isSeller != null) {
                            listener.onFetched(isSeller);
                        } else {
                            listener.onFetched(false);  // Default to false if not found
                        }
                    } else {
                        Log.e("FirebaseError", "Error fetching isSeller field", task.getException());
                        listener.onFetched(false);  // Default to false if error occurs
                    }
                });
    }

    interface OnIsSellerFetchedListener {
        void onFetched(boolean isSeller);
    }
*/
/*
    private boolean checkIfUserIsVend(String Uid){
        final boolean[] isVend = {false};


        db.child("users").child(Uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Check if the user exists
                if (dataSnapshot.exists()) {
                    // Retrieve the "isSeller" field
                    Boolean isSeller = dataSnapshot.child("isSeller").getValue(Boolean.class);

                    if (isSeller != null) {
                        isVend[0] = isSeller;
                        Log.d("UserInfo", "User is seller: " + isVend[0]);
                    }
                    else Log.d("UserInfo", "isSeller field is missing.");

                } else {
                    Log.d("UserInfo", "User not found.");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors
                Log.e("FirebaseError", "Error reading data: " + databaseError.getMessage());
            }
        });
        return isVend[0];
    }*/