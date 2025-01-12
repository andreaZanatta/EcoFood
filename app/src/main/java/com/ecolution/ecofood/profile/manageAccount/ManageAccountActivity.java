package com.ecolution.ecofood.profile.manageAccount;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.ecolution.ecofood.R;
import com.ecolution.ecofood.model.SellerModel;
import com.ecolution.ecofood.model.UserModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class ManageAccountActivity extends AppCompatActivity {

    EditText nameEditText, surnameEditText, emailEditText, passwordEditText, shopNameEditText, addressEditText;
    RadioGroup roleRadioGroup;
    RadioButton customerRadioButton, sellerRadioButton;
    ImageView profileImageView;
    Button confirmButton;
    LinearLayout sellerFieldLinearLayout;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    UserModel userModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prepareActivity();

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        getUserModel();
        updateInterface();
    }

    private void updateInterface() {
        nameEditText.setText(userModel.getFirstName());
        surnameEditText.setText(userModel.getLastName());
        emailEditText.setText(userModel.getEmail());
        passwordEditText.setText("");
        customerRadioButton.setEnabled(false);
        sellerRadioButton.setEnabled(false);

        if (userModel instanceof SellerModel) {
            SellerModel sellerModel = (SellerModel) userModel;
            customerRadioButton.setActivated(false);
            sellerRadioButton.setActivated(true);

            shopNameEditText.setText(sellerModel.getShopName());
            addressEditText.setText(sellerModel.getAddress());
            sellerFieldLinearLayout.setVisibility(View.VISIBLE);
        } else {
            customerRadioButton.setActivated(true);
            sellerRadioButton.setActivated(false);
            sellerFieldLinearLayout.setVisibility(View.GONE);
        }

        Glide.with(this)
                .load(userModel.getImage())
                .into(profileImageView);
    }

    private void prepareActivity() {
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_manage_account);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        nameEditText = findViewById(R.id.firstNameManageProfileEditText);
        surnameEditText = findViewById(R.id.lastNameManageProfileEditText);
        emailEditText = findViewById(R.id.emailManageProfileEditText);
        passwordEditText = findViewById(R.id.passwordManageProfileEditText);
        roleRadioGroup = findViewById(R.id.roleManageProfileRadio);
        customerRadioButton = findViewById(R.id.customerManageProfileRadio);
        sellerRadioButton = findViewById(R.id.sellerManageProfileRadio);
        profileImageView = findViewById(R.id.imageView5);
        confirmButton = findViewById(R.id.confirmManageProfileButton);
        shopNameEditText = findViewById(R.id.shopNameManageAccountEditText);
        addressEditText = findViewById(R.id.addressManageAccountEditText);
        sellerFieldLinearLayout = findViewById(R.id.sellerFieldsManageAccount);

        confirmButton.setOnClickListener(button -> confirmButtonPressed());

        Toolbar toolbar = findViewById(R.id.manageAccountToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Manage Account");
    }

    private void getUserModel() {
        userModel = (UserModel) getIntent().getSerializableExtra("user");
    }

    private void backToProfileActivity() {
        getOnBackPressedDispatcher().onBackPressed();
    }

    private void updatePasswordIfNecessary() {
        String password = passwordEditText.getText().toString();
        if (password != "") {
            var currentUser = mAuth.getCurrentUser();
            currentUser.updatePassword(password)
                    .addOnCompleteListener(result -> {

                    });
        }
    }

    private Map<String, Object> getValuesToUpdate() {
        Map<String, Object> updates = new HashMap<>();
        String firstName = nameEditText.getText().toString();
        if (!firstName.isEmpty() && userModel.getFirstName() != firstName) {
            updates.put("firstName", firstName);
        }
        String lastName = surnameEditText.getText().toString();
        if (!lastName.isEmpty() && userModel.getLastName() != lastName) {
            updates.put("lastName", lastName);
        }
        String email = emailEditText.getText().toString();
        if (!lastName.isEmpty() && userModel.getEmail() != email) {
            updates.put("email", email);
        }
        if (userModel instanceof SellerModel) {
            SellerModel sellerModel = (SellerModel) userModel;
            String shopName = shopNameEditText.getText().toString();
            if (!lastName.isEmpty() && sellerModel.getAddress() != shopName) {
                updates.put("shopName", shopName);
            }
            String address = addressEditText.getText().toString();
            if (!lastName.isEmpty() && sellerModel.getAddress() != address) {
                updates.put("address", address);
            }
        }

        return updates;
    }

    private void confirmButtonPressed() {
        Map<String, Object> updates = getValuesToUpdate();

        updatePasswordIfNecessary();

        if (!updates.isEmpty()) {
            db.collection("users")
                    .document(userModel.getUser_id())
                    .update(updates)
                    .addOnCompleteListener(result -> {
                        String firstName = nameEditText.getText().toString();
                        if (!firstName.isEmpty() && userModel.getFirstName() != firstName) {
                            userModel.setFirstName(firstName);
                        }
                        String lastName = surnameEditText.getText().toString();
                        if (!lastName.isEmpty() && userModel.getLastName() != lastName) {
                            userModel.setLastName(lastName);
                        }
                        String email = emailEditText.getText().toString();
                        if (!lastName.isEmpty() && userModel.getEmail() != email) {
                            userModel.setEmail(email);
                        }
                        if (userModel instanceof SellerModel) {
                            SellerModel sellerModel = (SellerModel) userModel;
                            String shopName = shopNameEditText.getText().toString();
                            if (!lastName.isEmpty() && sellerModel.getAddress() != shopName) {
                                sellerModel.setShopName(shopName);
                            }
                            String address = addressEditText.getText().toString();
                            if (!lastName.isEmpty() && sellerModel.getAddress() != address) {
                                sellerModel.setAddress(address);
                            }
                        }
                        backToProfileActivity();
                    });
        }
    }
}