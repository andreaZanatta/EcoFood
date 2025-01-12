package com.ecolution.ecofood.homepages;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.ecolution.ecofood.R;
import com.ecolution.ecofood.shopdetail.ShopDetailsActivity;

public class ListaFragment extends Fragment {

    // Simula una lista di negozi
    private String[][] shops = {
            {"Negozio 1", "Descrizione del negozio 1"},
            {"Negozio 2", "Descrizione del negozio 2"},
            {"Negozio 3", "Descrizione del negozio 3"},
            {"Negozio 4", "Descrizione del negozio 4"}
    };

    private LinearLayout shopListContainer;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_shop_list, container, false);

        // Trova il contenitore della lista
        shopListContainer = view.findViewById(R.id.shop_list_container);

        // Visualizza tutti i negozi all'avvio
        displayShops(shops);

        return view;
    }

    /**
     * Metodo per filtrare i negozi in base al nome
     */
    public void filterShops(String query) {
        if (query == null || query.isEmpty()) {
            displayShops(shops); // Mostra tutti i negozi se la ricerca è vuota
            return;
        }

        String[][] filteredShops = filter(query);
        displayShops(filteredShops);
    }

    /**
     * Filtra la lista dei negozi in base alla query
     */
    private String[][] filter(String query) {
        int count = 0;

        // Conta quanti negozi corrispondono alla ricerca
        for (String[] shop : shops) {
            if (shop[0].toLowerCase().contains(query.toLowerCase())) {
                count++;
            }
        }

        // Crea una nuova lista con i risultati filtrati
        String[][] result = new String[count][2];
        int index = 0;

        for (String[] shop : shops) {
            if (shop[0].toLowerCase().contains(query.toLowerCase())) {
                result[index++] = shop;
            }
        }

        return result;
    }

    /**
     * Mostra la lista dei negozi nella UI
     */
    private void displayShops(String[][] shopsToDisplay) {
        shopListContainer.removeAllViews();

        if (shopsToDisplay.length == 0) {
            TextView noResult = new TextView(getContext());
            noResult.setText("Nessun negozio trovato.");
            noResult.setTextSize(18);
            shopListContainer.addView(noResult);
            return;
        }

        LayoutInflater inflater = getLayoutInflater();

        for (String[] shop : shopsToDisplay) {
            View shopItem = inflater.inflate(R.layout.item_shop, shopListContainer, false);

            TextView shopName = shopItem.findViewById(R.id.shop_name);
            TextView shopDescription = shopItem.findViewById(R.id.shop_description);

            shopName.setText(shop[0]);
            shopDescription.setText(shop[1]);

            // Imposta il click per ogni negozio
            shopItem.setOnClickListener(v -> {
                Intent intent = new Intent(getActivity(), ShopDetailsActivity.class);
                intent.putExtra("SHOP_NAME", shop[0]);
                startActivity(intent);
            });

            shopListContainer.addView(shopItem);
        }
    }
}

