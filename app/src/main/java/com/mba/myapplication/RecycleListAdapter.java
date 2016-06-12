package com.mba.myapplication;

import android.content.Context;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


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
    public void onBindViewHolder(viewHolder holder, int position) {
        DishRecipie current = dishes.get(position);
        holder.dishName.setText(current.getDishName());
        holder.ingredients.setText(current.getDishIng());
        holder.dishImg.setImageResource(current.getImgId());

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

        TextView dishName, ingredients;
        ImageView dishImg;

        public viewHolder(View itemView) {
            super(itemView);
            itemView.setClickable(true);
            itemView.setOnClickListener(this);
            dishImg = (ImageView) itemView.findViewById(R.id.dishImg);
            ingredients = (TextView) itemView.findViewById(R.id.dishIng);
            dishName = (TextView) itemView.findViewById(R.id.dishName);
        }

        @Override
        public void onClick(View view) {
            if (clickListner != null) {
                clickListner.itemClicked(view, getPosition());
            }
        }

    }

    public interface ClickListner {
        public void itemClicked(View itemView, int position);
    }

}

