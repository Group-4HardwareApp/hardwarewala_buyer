package com.example.hardwarewale;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.hardwarewale.bean.Product;
import com.example.hardwarewale.databinding.ActivityBuyProductBinding;
import com.example.hardwarewale.databinding.BuyProductScreenBinding;
import com.example.hardwarewale.databinding.BuyScreenBinding;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.Calendar;

public class BuyProductActivity extends AppCompatActivity {
    //BuyProductScreenBinding binding;
    ActivityBuyProductBinding productBinding;
    String brand, name, productId, imageUrl, shopkeeperId, categoryId, description;
    Product product;
    Double price, tot, discount;
    int qty = 1, qtyInStock;
    int q;
    long timestamp;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //binding = BuyProductScreenBinding.inflate(LayoutInflater.from(this));
        //setContentView(binding.getRoot());

        productBinding = ActivityBuyProductBinding.inflate(LayoutInflater.from(this));
        setContentView(productBinding.getRoot());

        Intent in = getIntent();
        product = (Product) in.getSerializableExtra("product");

        productBinding.backPress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        name = product.getName();
        imageUrl = product.getImageUrl();
        description = product.getDescription();
        shopkeeperId = product.getShopkeeperId();
        productId = product.getProductId();
        categoryId = product.getCategoryId();
        brand = product.getBrand();
        discount = product.getDiscount();
        price = product.getPrice();
        qtyInStock = product.getQtyInStock();
        product.setQty(qty);
        timestamp = Calendar.getInstance().getTimeInMillis();

        double discount = product.getDiscount();
        int off = (int) discount;
        double amt = product.getPrice();
        double dis = amt * (discount / 100);
        final double offerPrice = amt - dis;

        //binding.tvDiscountedPrice.setText("₹ " + offerPrice);
        //productBinding.tvProductDiscount.setText("" + off + "% Off");

        productBinding.tvQtyy.setText(""+qty);
        productBinding.ivImage.setVisibility(View.VISIBLE);
        productBinding.ivSlider.setVisibility(View.GONE);
        Picasso.get().load(imageUrl).placeholder(R.drawable.default_photo_icon).into(productBinding.ivImage);
        productBinding.tvProductName.setText("" + name);
        productBinding.tvAmt.setText(" " + offerPrice);
        //productBinding.tvAmt.setText(new DecimalFormat("##.##").format(offerPrice));
        productBinding.tvQuantity.setText("" + qtyInStock);
        productBinding.tvQtyy.getText().toString();
        productBinding.tvBrand.setText(""+brand);
        productBinding.tvProductDescription.setText(""+description);
        product.setQty(qty);
        productBinding.ivAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                productBinding.tvQuantity.getText().toString();
                int q = Integer.parseInt(productBinding.tvQtyy.getText().toString());
                if (q < qtyInStock) {
                    q++;
                    productBinding.tvQtyy.setText("" + q);
                    product.setQty(q);
                    double total = Double.parseDouble(productBinding.tvAmt.getText().toString());
                    total = total + (offerPrice);
                    productBinding.tvAmt.setText("" + total);
                    product.setTotalAmt(offerPrice*q);
                }
            }
        });

        productBinding.ivSubrtact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                productBinding.tvQuantity.getText().toString();
                int q = Integer.parseInt(productBinding.tvQtyy.getText().toString());
                if (q > 1) {
                    q--;
                    productBinding.tvQtyy.setText("" + q);
                    product.setQty(q);
                    double total = Double.parseDouble(productBinding.tvAmt.getText().toString());
                    total = total - (offerPrice);
                    productBinding.tvAmt.setText("" + total);
                    product.setTotalAmt(offerPrice*q);
                }
            }
        });

        productBinding.ivContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BuyProductActivity.this, PlaceProductActivity.class);
                intent.putExtra("p", product);
                startActivity(intent);
            }
        });
    }
}
