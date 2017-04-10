package com.mba.AjjkiaPakaen;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Muhammad Bilal on 27/03/2016.
 */
public class RecycleListAdapter extends RecyclerView.Adapter<RecycleListAdapter.viewHolder> implements Filterable {

    private List<DishRecipie> orig;
    List<DishRecipie> dishes = Collections.emptyList();
    private LayoutInflater inflater;
    private ClickListner clickListner;
    Firebase readFirebaseRef, readFirebaseRef1;
    double ratings = 0d;
    long count = 0;

    public RecycleListAdapter(Context context, List<DishRecipie> dishes) {
        inflater = LayoutInflater.from(context);
        this.dishes = dishes;

    }

    public void setClickListner(ClickListner cl) {
        this.clickListner = cl;
    }

    @Override
    public viewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.list_item, parent, false);
        viewHolder holder = new viewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final viewHolder holder, int position) {

        DishRecipie current = dishes.get(position);
        holder.dishName.setText(current.getDishName());
        Bitmap b1 = BitmapFactory.decodeByteArray(current.getImg(), 0, current.getImg().length);
        holder.dishImg.setImageBitmap(b1);
        holder.ratingBar.setRating(0.0f);

        readFirebaseRef1 = new Firebase("https://dishes-12c91.firebaseio.com/" + current.getDishId());
        readFirebaseRef1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                count = 0;
                if (snapshot.getValue() != null) {

                    count = (long) snapshot.child("count").getValue();
                    ratings = (double) snapshot.child("rating").getValue();

                }

                if (count > 0) {
                    holder.ratingBar.setRating(Float.parseFloat(String.valueOf(ratings / count)));
                } else
                    holder.ratingBar.setRating(0);


            }


            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });


       /* readFirebaseRef = new Firebase("https://dishes-12c91.firebaseio.com/" + current.getDishId() + "/rating");
        readFirebaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.getValue() != null) {
                    ratings = (double) snapshot.getValue();
                    if (count > 0) {
                        holder.ratingBar.setRating(Float.parseFloat(String.valueOf((Double) snapshot.getValue() / count)));
                    } else
                        holder.ratingBar.setRating(Float.parseFloat(String.valueOf((Double) snapshot.getValue())));
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });*/
    }


    @Override
    public int getItemCount() {
        return dishes.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                final FilterResults oReturn = new FilterResults();
                final List<DishRecipie> results = new ArrayList<DishRecipie>();
                if (orig == null)
                    orig = dishes;
                if (constraint != null) {
                    if (orig != null & orig.size() > 0) {
                        for (final DishRecipie g : orig) {
                            if (g.getDishName().contains(constraint.toString()))
                                results.add(g);
                        }
                    }
                    oReturn.values = results;
                }
                return oReturn;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                dishes = (ArrayList<DishRecipie>) results.values;
                notifyDataSetChanged();

            }
        };
    }

    public class viewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView dishName;
        ImageView dishImg;
        RatingBar ratingBar;

        public viewHolder(View itemView) {
            super(itemView);
            itemView.setClickable(true);
            itemView.setOnClickListener(this);
            dishImg = (ImageView) itemView.findViewById(R.id.dishImg);

            dishName = (TextView) itemView.findViewById(R.id.dishName);
            ratingBar = (RatingBar) itemView.findViewById(R.id.titleRb);
            LayerDrawable stars = (LayerDrawable) ratingBar.getProgressDrawable();

            stars.getDrawable(2).setColorFilter(Color.parseColor("#c9079f"), PorterDuff.Mode.SRC_ATOP);

        }

        @Override
        public void onClick(View view) {
            if (clickListner != null) {
                clickListner.itemClicked(view, getPosition());
            }
        }

    }

    public interface ClickListner {
        void itemClicked(View itemView, int position);
    }

}

