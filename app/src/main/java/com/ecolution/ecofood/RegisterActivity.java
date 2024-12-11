package com.ecolution.ecofood;

import com.ecolution.ecofood.R;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class RegisterActivity extends AppCompatActivity {

    private EditText etNome, etCognome, etEmail, etPassword, etNomeNegozio, etIndirizzo, etProfileImageUrl, etStoreLogoUrl;
    private RadioGroup rgRole;
    private LinearLayout venditoreFields;
    private Button btnRegistrati;
    private boolean isVenditore = false;

    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registrazione);

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
        etProfileImageUrl = findViewById(R.id.et_profile_image_url);
        etStoreLogoUrl = findViewById(R.id.et_store_logo_url);
        rgRole = findViewById(R.id.rg_role);
        venditoreFields = findViewById(R.id.venditore_fields);
        btnRegistrati = findViewById(R.id.btn_registrati);

        // Gestione visibilità campi venditore
        rgRole.setOnCheckedChangeListener((group, checkedId) -> {
            isVenditore = checkedId == R.id.rb_venditore;
            venditoreFields.setVisibility(isVenditore ? View.VISIBLE : View.GONE);
        });

        // Pulsante Registrati
        btnRegistrati.setOnClickListener(v -> {
            String nome = etNome.getText().toString().trim();
            String cognome = etCognome.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            String profileImageUrl = etProfileImageUrl.getText().toString().trim();

            if (nome.isEmpty() || cognome.isEmpty() || email.isEmpty() || password.isEmpty() || profileImageUrl.isEmpty()) {
                Toast.makeText(this, "Compila tutti i campi obbligatori!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (isVenditore) {
                String nomeNegozio = etNomeNegozio.getText().toString().trim();
                String indirizzo = etIndirizzo.getText().toString().trim();
                String storeLogoUrl = etStoreLogoUrl.getText().toString().trim();

                if (nomeNegozio.isEmpty() || indirizzo.isEmpty() || storeLogoUrl.isEmpty()) {
                    Toast.makeText(this, "Compila tutti i campi per il venditore!", Toast.LENGTH_SHORT).show();
                    return;
                }

                registraUtente(email, password, nome, cognome, isVenditore, nomeNegozio, indirizzo, profileImageUrl, storeLogoUrl);
            } else {
                registraUtente(email, password, nome, cognome, isVenditore, "", "", profileImageUrl, "");
            }
        });
    }

    private void registraUtente(String email, String password, String nome, String cognome, boolean isVenditore, String nomeNegozio, String indirizzo, String profileImageUrl, String storeLogoUrl) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        String userId = mAuth.getCurrentUser().getUid();

                        // Crea un oggetto utente con i dati
                        User user = new User(nome, cognome, email, isVenditore, nomeNegozio, indirizzo, profileImageUrl, storeLogoUrl);

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
    public static class User {
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
    }
}

