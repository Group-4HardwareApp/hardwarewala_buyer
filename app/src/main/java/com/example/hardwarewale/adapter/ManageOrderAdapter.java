package com.example.hardwarewale.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hardwarewale.OrderDetailsActivity;
import com.example.hardwarewale.OrderHistoryDetailsActivity;
import com.example.hardwarewale.R;
import com.example.hardwarewale.api.OrderService;
import com.example.hardwarewale.bean.Cart;
import com.example.hardwarewale.bean.Order;
import com.example.hardwarewale.bean.OrderItems;
import com.example.hardwarewale.databinding.ManageOrderItemListBinding;
import com.example.hardwarewale.utility.InternetConnectivity;

import java.io.Serializable;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ManageOrderAdapter extends RecyclerView.Adapter<ManageOrderAdapter.ManageOrderViewHolder> {
    Context context;
    ArrayList<Order> orderList;
    ProgressDialog pd;
    OnRecyclerViewClick listener;
    InternetConnectivity connectivity = new InternetConnectivity();

    public ManageOrderAdapter(Context context, ArrayList<Order> orderList) {
        this.context = context;
        this.orderList = orderList;
    }

    @NonNull
    @Override
    public ManageOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ManageOrderItemListBinding binding = ManageOrderItemListBinding.inflate(LayoutInflater.from(context), parent, false);
        return new ManageOrderViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ManageOrderViewHolder holder, final int position) {
        final Order order = orderList.get(position);
        final String id = order.getOrderId();
        holder.binding.tvOrderAddress.setText("" + order.getDeliveryAddress());
        holder.binding.tvOrderAmount.setText("₹ " + order.getTotalAmount());
        holder.binding.tvOrderDate.setText("" + order.getDate());
        holder.binding.tvOrderName.setText("" + order.getName());
        holder.binding.tvOrderId.setText("" + id);
        holder.binding.tvOrderStatus.setText("" + order.getShippingStatus());

        final SpannableString content = new SpannableString("View more");
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        holder.binding.btnViewMore.setText(content);

        Drawable unwrappedDrawable = AppCompatResources.getDrawable(context, R.drawable.close_icon);
        Drawable wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable);
        DrawableCompat.setTint(wrappedDrawable, Color.WHITE);

        holder.binding.btnCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (connectivity.isConnectedToInternet(context)) {
                    final AlertDialog ab = new AlertDialog.Builder(context).create();
                    LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View view = inflater.inflate(R.layout.alert_dialog, null);
                    ab.setView(view);
                    TextView tvtitleMsg = view.findViewById(R.id.tvTilteMsg);
                    tvtitleMsg.setText("You want to cancel this order");

                    CardView btnCancel = view.findViewById(R.id.btnCancel);
                    CardView btnOkay = view.findViewById(R.id.btnOkay);

                    btnOkay.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            pd = new ProgressDialog(context);
                            pd.setTitle("Removing");
                            pd.setMessage("Please wait...");
                            pd.show();
                            OrderService.OrderApi api = OrderService.getOrderApiInstance();
                            Call<Order> call = api.cancelOrder(id);
                            call.enqueue(new Callback<Order>() {
                                @Override
                                public void onResponse(Call<Order> call, Response<Order> response) {
                                    if (response.code() == 200) {
                                        Order o = response.body();
                                        orderList.remove(position);
                                        pd.dismiss();
                                        notifyDataSetChanged();
                                        ab.dismiss();
                                    } else
                                        Log.e("code", "==>" + response.code());
                                }

                                @Override
                                public void onFailure(Call<Order> call, Throwable t) {
                                    Toast.makeText(context, "" + t, Toast.LENGTH_SHORT).show();
                                    Log.e("Error : ", "==> " + t);
                                }
                            });
                        }
                    });
                    btnCancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ab.dismiss();
                        }
                    });
                    ab.show();
                }
            }
        });

        final ArrayList<OrderItems> items = (ArrayList<OrderItems>) order.getOrderItems();
        holder.binding.btnViewMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(context, OrderDetailsActivity.class);
                in.putExtra("item", (Serializable) items);
                context.startActivity(in);
            }
        });
        holder.binding.btnTrackStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Track status clicked", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public class ManageOrderViewHolder extends RecyclerView.ViewHolder {
        ManageOrderItemListBinding binding;

        public ManageOrderViewHolder(ManageOrderItemListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    Order o = orderList.get(position);
                    if (position != RecyclerView.NO_POSITION && listener != null)
                        listener.onItemClick(o, position);
                }
            });
        }
    }

    public interface OnRecyclerViewClick {
        void onItemClick(Order order, int posotion);
    }

    public void setOnItemClickListener(OnRecyclerViewClick listener) {
        this.listener = listener;
    }
}
