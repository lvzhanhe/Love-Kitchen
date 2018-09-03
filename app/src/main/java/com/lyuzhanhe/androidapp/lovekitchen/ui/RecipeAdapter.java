package com.lyuzhanhe.androidapp.lovekitchen.ui;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;

import com.lyuzhanhe.androidapp.lovekitchen.Recipe;
import com.lyuzhanhe.androidapp.lovekitchen.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeAdapterViewHolder>{

    private ArrayList<Recipe> data;
    private final RecipeAdapter.RecipeAdapterOnClickHandler mClickHandler;

    public interface RecipeAdapterOnClickHandler {
        void onClick(Recipe data, ImageView iv);
    }

    public RecipeAdapter(RecipeAdapter.RecipeAdapterOnClickHandler clickHandler) {
        mClickHandler = clickHandler;
    }

    public class RecipeAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public Recipe mRecipe;
        final public ImageView recipe_image_view;
        final public TextView name_view;
        final public TextView description_view;

        public RecipeAdapterViewHolder(View itemView) {
            super(itemView);
            recipe_image_view = (ImageView) itemView.findViewById(R.id.recipe_image_view);
            name_view = (TextView) itemView.findViewById(R.id.name_view);
            description_view = (TextView) itemView.findViewById(R.id.description_view);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mClickHandler.onClick(mRecipe, recipe_image_view);
        }
    }

    @NonNull
    @Override
    public RecipeAdapter.RecipeAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.recipe_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        return new RecipeAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeAdapter.RecipeAdapterViewHolder holder, int position) {
        holder.mRecipe = data.get(position);
        Picasso.get()
                .load(holder.mRecipe.getImage())
                .into(holder.recipe_image_view);
        holder.name_view.setText(holder.mRecipe.getTitle());
        holder.description_view.setText("Ready in minutes: " + holder.mRecipe.getReadyInMinutes());
    }

    @Override
    public int getItemCount() {
        if (data == null) return 0;
        else return data.size();
    }

    public void setData(ArrayList<Recipe> data) {
        this.data = data;
        notifyDataSetChanged();
    }
}
