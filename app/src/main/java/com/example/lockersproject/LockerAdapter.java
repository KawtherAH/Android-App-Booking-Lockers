package com.example.lockersproject;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class LockerAdapter extends RecyclerView.Adapter<LockerAdapter.ViewHolder> implements Filterable {

    ArrayList<Lockers> lockerList;
    private ItemClickListener listener;

    public LockerAdapter(ArrayList<Lockers> lockerList, ItemClickListener listener){
        this.lockerList = lockerList;
        this.listener = listener;
    }
    public interface ItemClickListener {
        void onItemClick(Lockers locker);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.lockers_item,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Lockers currentLocker = lockerList.get(position);

        int No = currentLocker.getLockerNo();
        int isAvailable = currentLocker.getIsAvailable();

        holder.LockerNoTV.setText(String.valueOf(No));
        holder.LockerBoolTV.setText(isAvailable == 1? "Available":"UnAvailable");
        holder.LockerBoolTV.setTextColor(isAvailable==1? (Color.parseColor("#FFFFFFFF")):(Color.parseColor("#FF0000")));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClick(lockerList.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return lockerList.size();
    }
    @Override
    public Filter getFilter() {
        return FilterLocker;
    }

    private Filter FilterLocker = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            String searchtext = charSequence.toString().toLowerCase();
            List<Lockers>  templist = new ArrayList<>();

            if(searchtext.length()==0|| searchtext.isEmpty()){
                templist.addAll( lockerList );
            }
            else{
                for( Lockers l: lockerList){
                    if(String.valueOf( l.getLockerNo() ).contains( searchtext)){
                        templist.add( l );
                    }
                }
            }

            FilterResults filterResults = new FilterResults();
            filterResults.values = templist;

            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            lockerList.clear();
            lockerList.addAll( (Collection< ? extends Lockers >) filterResults.values );
            notifyDataSetChanged();
        }
    };

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView LockerNoTV;
        public TextView LockerBoolTV;
        public LinearLayout itemLayout;

        public ViewHolder(View v) {
            super(v);

            LockerNoTV = v.findViewById(R.id.Locker_No_view);
            LockerBoolTV = v.findViewById(R.id.Locker_isAvailable_view);
            itemLayout = v.findViewById(R.id.locker_item_layout);

            v.setOnClickListener(this);
        }
        @Override
        public void onClick(View view) {

        }

    }
}
