package com.ecolution.ecofood;

import com.ecolution.ecofood.R;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.ecolution.ecofood.model.UserModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import com.ecolution.ecofood.model.CustomerModel;
import com.ecolution.ecofood.model.SellerModel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class RegisterActivity extends AppCompatActivity {

    private static final int PICK_CUSTOMER_IMAGE = 1;
    private static final int PICK_SELLER_IMAGE = 2;
    private String customerImagePath = null;
    private String sellerLogoPath = null;

    private EditText etNome, etCognome, etEmail, etPassword, etNomeNegozio, etIndirizzo;
    private LinearLayout venditoreFields;
    private Button btnRegistrati;
    private boolean isVenditore = false;
    private RelativeLayout etProfileImageUrl, etStoreLogoUrl;
    private TextView tvLogin;

    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registrazione);

        //Variabili locali
        RadioGroup rgRole = findViewById(R.id.rg_role);

        // Inizializza Firebase
        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();

        // Inizializza i campi
        etNome = findViewById(R.id.et_nome);
        etCognome = findViewById(R.id.et_cognome);
        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);
        etNomeNegozio = findViewById(R.id.et_nome_negozio);
        etIndirizzo = findViewById(R.id.et_indirizzo);
        etProfileImageUrl = findViewById(R.id.upload_photo_customer);
        etStoreLogoUrl = findViewById(R.id.upload_photo_seller);

        venditoreFields = findViewById(R.id.venditore_fields);
        btnRegistrati = findViewById(R.id.btn_registrati);
        tvLogin = findViewById(R.id.tvLogin);

        // Gestione visibilità campi venditore
        rgRole.setOnCheckedChangeListener((group, checkedId) -> {
            isVenditore = checkedId == R.id.rb_venditore;
            venditoreFields.setVisibility(isVenditore ? View.VISIBLE : View.GONE);
        });

        tvLogin.setOnClickListener(v -> {
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
        });

        etProfileImageUrl.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, PICK_CUSTOMER_IMAGE);
        });

        etStoreLogoUrl.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, PICK_SELLER_IMAGE);
        });

        // Pulsante Registrati
        btnRegistrati.setOnClickListener(v -> {
            String nome = etNome.getText().toString().trim();
            String cognome = etCognome.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            /*TODO: Gestire galleria del dispositivo, inserire un'immagine e salvarla in una cartella specifica */
            //String profileImageUrl = etProfileImageUrl.getText().toString().trim();

            if (nome.isEmpty() || cognome.isEmpty() || email.isEmpty() || password.isEmpty() || customerImagePath == null) {
                Toast.makeText(this, "Compila tutti i campi obbligatori!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (isVenditore) {
                String nomeNegozio = etNomeNegozio.getText().toString().trim();
                String indirizzo = etIndirizzo.getText().toString().trim();
                //String storeLogoUrl = etStoreLogoUrl.getText().toString().trim();

                if (nomeNegozio.isEmpty() || indirizzo.isEmpty() || sellerLogoPath == null) {
                    Toast.makeText(this, "Compila tutti i campi per il venditore!", Toast.LENGTH_SHORT).show();
                    return;
                }

                /* TODO: SISTEMARE Store Logo Url paramether passing */
                registraUtente(email, password, nome, cognome, isVenditore, nomeNegozio, indirizzo, customerImagePath, sellerLogoPath);
            } else {
                /* TODO: SISTEMARE Profile Image paramether passing */
                registraUtente(email, password, nome, cognome, isVenditore, null, null, customerImagePath, null);
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if ( (requestCode == PICK_SELLER_IMAGE || requestCode == PICK_CUSTOMER_IMAGE) && resultCode == RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();
            if (selectedImageUri != null) {
                try {
                    // Copy the image to a local directory
                    File newFile = copyImageToLocalDirectory(selectedImageUri);

                    String imagePath = newFile.getAbsolutePath(); // Get the file path

                    // Pass the path to the appropriate variable based on the request code
                    if (requestCode == PICK_CUSTOMER_IMAGE)     customerImagePath = imagePath; // Save the customer image path
                    else    sellerLogoPath = imagePath; // Save the seller logo path

                    Toast.makeText(this, "Image saved to " + newFile.getAbsolutePath(), Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Error copying image", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private File copyImageToLocalDirectory(Uri selectedImageUri) throws IOException {
        // Get the image's input stream
        InputStream inputStream = getContentResolver().openInputStream(selectedImageUri);

        // Create a file in your app's local storage directory
        File outputDir = new File(getFilesDir(), "src");
        if (!outputDir.exists()) {
            outputDir.mkdir();
        }
        File outputFile = new File(outputDir, "image_" + System.currentTimeMillis() + ".jpg");

        // Copy the image content to the new file
        try (OutputStream outputStream = new FileOutputStream(outputFile)) {
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }
        } finally {
            inputStream.close();
        }

        return outputFile;
    }


    private void registraUtente(String email, String password, String nome, String cognome, boolean isVenditore, String nomeNegozio, String indirizzo, String profileImageUrl, String storeLogoUrl) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        String userId = mAuth.getCurrentUser().getUid();

                        UserModel user;
                        // Crea un oggetto utente con i dati
                        if(!isVenditore)
                            user = new CustomerModel(userId, nome, cognome, email, password, false, profileImageUrl);
                        else
                            user = new SellerModel(userId, nome, cognome, email, password, true, profileImageUrl, nomeNegozio, indirizzo, storeLogoUrl);


                        // Salva i dati su Firestore
                        mFirestore.collection("users").document(userId).set(user)
                                .addOnCompleteListener(dbTask -> {
                                    if (dbTask.isSuccessful()) {
                                        Toast.makeText(this, "Registrazione completata!", Toast.LENGTH_SHORT).show();

                                        // **Reindirizza alla schermata di login**
                                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                        startActivity(intent);
                                        finish(); // Chiude la RegisterActivity
                                    } else {
                                        Toast.makeText(this, "Errore nel salvataggio dati: " + dbTask.getException().getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                });
                    } else {
                        Toast.makeText(this, "Errore: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    // Classe per i dati utente
    /*public static class User {
        public String nome, cognome, email, nomeNegozio, indirizzo, profileImageUrl, storeLogoUrl;
        public boolean isVenditore;

        public User(String nome, String cognome, String email, boolean isVenditore, String nomeNegozio, String indirizzo, String profileImageUrl, String storeLogoUrl) {
            this.nome = nome;
            this.cognome = cognome;
            this.email = email;
            this.isVenditore = isVenditore;
            this.nomeNegozio = nomeNegozio;
            this.indirizzo = indirizzo;
            this.profileImageUrl = profileImageUrl;
            this.storeLogoUrl = storeLogoUrl;
        }
    } */
}

