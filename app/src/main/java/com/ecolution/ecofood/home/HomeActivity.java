package com.ecolution.ecofood.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.ecolution.ecofood.R;
import com.ecolution.ecofood.productdetail.ProductListActivity;

public class HomeActivity extends AppCompatActivity {

    private Fragment currentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        boolean isVenditore = getIntent().getBooleanExtra("isVenditore", false);

        // Inizializza i bottoni
        Button buttonLista = findViewById(R.id.button_lista);
        Button buttonProfilo = findViewById(R.id.button_profilo);
        Button searchButton = findViewById(R.id.search_button);
        EditText searchBar = findViewById(R.id.search_bar);

        // Carica il frammento iniziale
        loadFragment(new ListaFragment());

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String query = searchBar.getText().toString().trim();
                if (currentFragment instanceof ListaFragment) {
                    ((ListaFragment) currentFragment).filterShops(query);
                }
            }
        });

        // Impostiamo l'azione del bottone "Negozi"
        buttonLista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ListaFragment listaFragment = new ListaFragment();
                loadFragment(listaFragment);
                currentFragment = listaFragment; // Aggiorna il frammento corrente
            }
        });

        // Impostiamo l'azione del bottone "Profilo"
        buttonProfilo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Assicurati di creare e caricare il ProfiloFragment
            }
        });

        // Aggiungi il bottone "Mio Negozio" per i venditori
        if (isVenditore) {
            Button buttonMioNegozio = new Button(this);
            buttonMioNegozio.setText("Mio Negozio");
            buttonMioNegozio.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
            buttonMioNegozio.setGravity(View.TEXT_ALIGNMENT_CENTER);
            buttonMioNegozio.setPadding(8, 8, 8, 8);

            // Assicurati che la navBar sia già presente nel layout
            LinearLayout navBar = findViewById(R.id.nav_bar);
            navBar.addView(buttonMioNegozio);

            // Gestisci il click sul bottone "Mio Negozio"
            buttonMioNegozio.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent SellerIntent = new Intent(HomeActivity.this, ProductListActivity.class);
                    SellerIntent.putExtra("userType", "seller");
                    startActivity(SellerIntent);
                }
            });
        }
    }

    // Metodo per caricare i Fragment
    private void loadFragment(Fragment fragment) {
        // Se il frammento corrente non è null, rimuovilo
        if (currentFragment != null) {
            getSupportFragmentManager().beginTransaction().remove(currentFragment).commit();
        }

        // Aggiungi il nuovo frammento
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frameLayout, fragment);  // Assicurati che R.id.frameLayout sia il tuo contenitore per i fragment
        transaction.commit();

        // Aggiorna il frammento corrente
        currentFragment = fragment;
    }
}
