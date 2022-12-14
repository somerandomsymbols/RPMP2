package com.example.rpmp2.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rpmp2.R;
import com.example.rpmp2.database.model.Wheat;

import java.util.ArrayList;
import java.util.List;

public class WheatAdapter extends RecyclerView.Adapter<WheatAdapter.StudentViewHolder>
{
    private List<Wheat> wheats = new ArrayList<>();
    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener listener)
    {
        onItemClickListener = listener;
    }

    public void setWheats(List<Wheat> wheats)
    {
        this.wheats = wheats;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public StudentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_wheat, parent, false);
        return new StudentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentViewHolder holder, int position)
    {
        if (!wheats.isEmpty())
            holder.setInfo(wheats.get(position));
    }

    @Override
    public int getItemCount()
    {
        return wheats.size();
    }

    class StudentViewHolder extends RecyclerView.ViewHolder
    {
        TextView yearTextView, productionTextView, priceTextView;

        public StudentViewHolder(@NonNull View itemView)
        {
            super(itemView);
            yearTextView = itemView.findViewById(R.id.year_text_view);
            productionTextView = itemView.findViewById(R.id.production_text_view);
            priceTextView = itemView.findViewById(R.id.price_text_view);
        }

        public void setInfo(Wheat wheat)
        {
            if (wheat.getYear() > 0)
                yearTextView.setText(String.format(itemView.getContext().getString(R.string.year_text), wheat.getYear()));
            else
                yearTextView.setText(String.format(itemView.getContext().getString(R.string.year_text_negative), -wheat.getYear()));

            productionTextView.setVisibility(View.VISIBLE);
            productionTextView.setText(String.format(itemView.getContext().getString(R.string.production_text), wheat.getProduction()));

            if (wheat.getPrice() != -1)
            {
                priceTextView.setVisibility(View.VISIBLE);
                priceTextView.setText(String.format(itemView.getContext().getString(R.string.price_text), wheat.getPrice()));
            }
            else
                priceTextView.setVisibility(View.GONE);


            itemView.setOnClickListener(view -> onItemClickListener.onClick(wheat.getId()));
        }
    }

    public interface OnItemClickListener
    {
        void onClick(int studentId);
    }
}
