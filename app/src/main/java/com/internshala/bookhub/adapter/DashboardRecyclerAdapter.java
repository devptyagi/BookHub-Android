package com.internshala.bookhub.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.internshala.bookhub.R;
import com.internshala.bookhub.activity.DescriptionActivity;
import com.internshala.bookhub.model.Book;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class DashboardRecyclerAdapter extends RecyclerView.Adapter<DashboardRecyclerAdapter.DashboardViewHolder>{

    private ArrayList<Book> itemList;
    private LayoutInflater mInflater;
    private Context context;

    public DashboardRecyclerAdapter(Context context, ArrayList<Book> itemList) {
        this.itemList = itemList;
        this.context = context;
    }

    @NonNull
    @Override
    public DashboardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_dashboard_single_row, parent, false);
        return new DashboardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final DashboardViewHolder holder, int position) {
        final Book b = itemList.get(position);
        holder.txtBookName.setText(b.bookName);
        holder.txtPrice.setText(b.bookPrice);
        holder.txtAuthorName.setText(b.bookAuthor);
        holder.txtRating.setText(b.bookRating);
        Picasso.get().load(b.bookImage).error(R.drawable.default_book_cover).into(holder.imgBook);

        holder.listLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DescriptionActivity.class);
                intent.putExtra("book_id", b.bookId);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    class DashboardViewHolder extends RecyclerView.ViewHolder{

        TextView txtBookName;
        TextView txtAuthorName;
        TextView txtPrice;
        TextView txtRating;
        ImageView imgBook;
        LinearLayout listLayout;

        public DashboardViewHolder(@NonNull View itemView) {
            super(itemView);
            txtBookName = (TextView)itemView.findViewById(R.id.txtBookName);
            txtAuthorName = (TextView)itemView.findViewById(R.id.txtBookAuthor);
            txtPrice = (TextView)itemView.findViewById(R.id.txtBookPrice);
            txtRating = (TextView)itemView.findViewById(R.id.txtBookRating);
            imgBook = (ImageView)itemView.findViewById(R.id.imgBookImage);
            listLayout = (LinearLayout)itemView.findViewById(R.id.listContent);
        }
    }

}
