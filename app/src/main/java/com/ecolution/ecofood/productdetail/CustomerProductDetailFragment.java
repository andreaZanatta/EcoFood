package com.ecolution.ecofood.productdetail;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ecolution.ecofood.R;

import java.util.ArrayList;
import java.util.List;

//TODO
/**
 *  This file ->  vedi Product List Activity (in Miro)
 *
 *  -> questa è una ACTIVITY
 *      onCreate -> renderiza pagina e analizzare il Context di esecuzione. Suddividere le query in due diversi context (cliente, venditore)
 *      in questa pagina avviene query.
 * */


public class CustomerProductDetailFragment extends Fragment {

    RecyclerView recyclerView;
    //List<NavCategoryModel> categoryModelList;
    List<NavItemAdapter> navItemAdapter;

    public View onCreateView(@NonNull LayoutInflater infl, ViewGroup container, Bundle savedInstanceState) {
        View root = infl.inflate(R.layout.activity_productlist, container, false);

        recyclerView = root.findViewById(R.id.product_list_view);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));
        //categoryModelList = new ArrayList<>();
        //navItemAdapter = new navItemAdapter(getActivity(), categoryModelList);
        //recyclerView.setAdapter(navItemAdapter);
        return root;
    }
}