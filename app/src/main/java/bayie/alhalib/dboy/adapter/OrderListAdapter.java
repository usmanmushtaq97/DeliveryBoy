package bayie.alhalib.dboy.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import bayie.alhalib.dboy.R;
import bayie.alhalib.dboy.activity.OrderDetailActivity;
import bayie.alhalib.dboy.helper.AppController;
import bayie.alhalib.dboy.helper.Constant;
import bayie.alhalib.dboy.model.OrderList;

public class OrderListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    // for load more
    public final int VIEW_TYPE_ITEM = 0;
    public final int VIEW_TYPE_LOADING = 1;
    public boolean isLoading;
    Activity activity;
    ArrayList<OrderList> orderLists;
    String id = "0";

    public OrderListAdapter(Activity activity, ArrayList<OrderList> orderLists) {
        this.activity = activity;
        this.orderLists = orderLists;
    }

    public void add(int position, OrderList item) {
        orderLists.add(position, item);
        notifyItemInserted(position);
    }

    public void setLoaded() {
        isLoading = false;
    }


    // Create new views (invoked by the layout manager)
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, final int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(activity).inflate(R.layout.lyt_order_list, parent, false);
            return new OrderHolderItems(view);
        } else if (viewType == VIEW_TYPE_LOADING) {
            View view = LayoutInflater.from(activity).inflate(R.layout.item_progressbar, parent, false);
            return new ViewHolderLoading(view);
        }

        return null;
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holderparent, final int position) {

        if (holderparent instanceof OrderHolderItems) {
            OrderHolderItems holder = (OrderHolderItems) holderparent;
            final OrderList orderList = orderLists.get(position);
            id = orderList.getId();

            holder.tvCustomerOrderNo.setText(activity.getString(R.string.order_number) + orderList.getId());
            holder.tvCustomerOrderDate.setText(activity.getString(R.string.order_on) + orderList.getDate_added());

            holder.tvCustomerName.setText(orderList.getName());
            holder.tvCustomerMobile.setText(orderList.getMobile());

            if (orderList.getPayment_method().equals("cod")) {
                holder.tvCustomerPaymentMethod.setText(activity.getString(R.string.via) + "C.O.D.");
            } else {
                holder.tvCustomerPaymentMethod.setText(activity.getString(R.string.via) + orderList.getPayment_method());
            }


            if (orderList.getActive_status().equalsIgnoreCase(Constant.RECEIVED)) {
                holder.card_view_status.setCardBackgroundColor(ContextCompat.getColor(activity, R.color.received_status_bg));
            } else if (orderList.getActive_status().equalsIgnoreCase(Constant.PROCESSED)) {
                holder.card_view_status.setCardBackgroundColor(ContextCompat.getColor(activity, R.color.processed_status_bg));
            } else if (orderList.getActive_status().equalsIgnoreCase(Constant.SHIPPED)) {
                holder.card_view_status.setCardBackgroundColor(ContextCompat.getColor(activity, R.color.shipped_status_bg));
            } else if (orderList.getActive_status().equalsIgnoreCase(Constant.DELIVERED)) {
                holder.card_view_status.setCardBackgroundColor(ContextCompat.getColor(activity, R.color.delivered_status_bg));
            } else if (orderList.getActive_status().equalsIgnoreCase(Constant.CANCELLED) || orderList.getActive_status().equalsIgnoreCase(Constant.RETURNED)) {
                holder.card_view_status.setCardBackgroundColor(ContextCompat.getColor(activity, R.color.returned_and_cancel_status_bg));
            }

            holder.tvStatus.setText(AppController.toTitleCase(orderList.getActive_status()));

            holder.lytOrderList.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Constant.Position_Value = position;
                    activity.startActivity(new Intent(activity, OrderDetailActivity.class).putExtra(Constant.ORDER_ID, orderList.getId()));
                }
            });
        } else if (holderparent instanceof ViewHolderLoading) {
            ViewHolderLoading loadingViewHolder = (ViewHolderLoading) holderparent;
            loadingViewHolder.progressBar.setIndeterminate(true);
        }


    }

    @Override
    public int getItemCount() {
        return orderLists.size();
    }

    @Override
    public int getItemViewType(int position) {
        return orderLists.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    @Override
    public long getItemId(int position) {
        OrderList product = orderLists.get(position);
        if (product != null)
            return Integer.parseInt(product.getId());
        else
            return position;
    }

    class ViewHolderLoading extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public ViewHolderLoading(View view) {
            super(view);
            progressBar = view.findViewById(R.id.itemProgressbar);
        }
    }

    public class OrderHolderItems extends RecyclerView.ViewHolder {

        TextView tvCustomerName, tvCustomerMobile, tvCustomerOrderNo, tvCustomerPaymentMethod, tvCustomerOrderDate, tvStatus;
        CardView card_view_status;
        RelativeLayout lytOrderList;

        public OrderHolderItems(@NonNull View itemView) {
            super(itemView);

            tvCustomerOrderNo = itemView.findViewById(R.id.tvCustomerOrderNo);
            tvCustomerOrderDate = itemView.findViewById(R.id.tvCustomerOrderDate);

            tvStatus = itemView.findViewById(R.id.tvStatus);
            card_view_status = itemView.findViewById(R.id.card_view_status);
            tvCustomerName = itemView.findViewById(R.id.tvCustomerName);

            tvCustomerMobile = itemView.findViewById(R.id.tvCustomerMobile);
            tvCustomerPaymentMethod = itemView.findViewById(R.id.tvCustomerPaymentMethod);

            lytOrderList = itemView.findViewById(R.id.lytOrderList);


        }
    }
}