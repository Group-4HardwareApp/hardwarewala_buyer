package com.example.hardwarewale;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hardwarewale.adapter.ViewBillAdapter;
import com.example.hardwarewale.bean.BuyCart;
import com.example.hardwarewale.bean.Cart;
import com.example.hardwarewale.databinding.BillBinding;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;

public class ViewBill extends BottomSheetDialogFragment {
    BillBinding binding;
    String name;
    double price, qty;
    ViewBillAdapter billAdapter;
    ArrayList<Cart> cart;

    public ViewBill() {
    }

    public ViewBill(ArrayList<Cart> cart) {
        this.cart = cart;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = BillBinding.inflate(LayoutInflater.from(getActivity()));
        View v = inflater.inflate(R.layout.bill,container,false);
        RecyclerView rvBill = v.findViewById(R.id.rvBill);
        billAdapter = new ViewBillAdapter(getContext(),cart);
        rvBill.setAdapter(billAdapter);
        rvBill.setLayoutManager(new LinearLayoutManager(getContext()));
        return v;
    }
}
