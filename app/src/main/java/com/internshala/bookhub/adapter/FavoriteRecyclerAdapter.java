package com.internshala.bookhub.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.internshala.bookhub.R;
import com.internshala.bookhub.database.BookEntity;
import com.squareup.picasso.Picasso;

import java.util.List;

public class FavoriteRecyclerAdapter extends RecyclerView.Adapter<FavoriteRecyclerAdapter.FavoriteViewHolder>{

    List<BookEntity> bookList;
    Context context;

    public FavoriteRecyclerAdapter(Context context, List<BookEntity> bookList) {
        this.context = context;
        this.bookList = bookList;
    }

    @NonNull
    @Override
    public FavoriteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_favorite_single_row, parent, false);
        return new FavoriteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteViewHolder holder, int position) {

        BookEntity b = bookList.get(position);

        holder.txtBookName.setText(b.bookName);
        holder.txtBookAuthor.setText(b.bookAuthor);
        holder.txtBookPrice.setText(b.bookPrice);
        holder.txtBookRating.setText(b.bookRating);
        Picasso.get().load(b.bookImage).error(R.drawable.default_book_cover).into(holder.imgBookImage);
    }

    @Override
    public int getItemCount() {
        return bookList.size();
    }

    class FavoriteViewHolder extends RecyclerView.ViewHolder {

        TextView txtBookName;
        TextView txtBookAuthor;
        TextView txtBookPrice;
        TextView txtBookRating;
        ImageView imgBookImage;
        LinearLayout llContent;

        public FavoriteViewHolder(@NonNull View itemView) {
            super(itemView);

            txtBookName = (TextView)itemView.findViewById(R.id.txtFavBookTitle);
            txtBookAuthor = (TextView)itemView.findViewById(R.id.txtFavBookAuthor);
            txtBookPrice = (TextView)itemView.findViewById(R.id.txtFavBookPrice);
            txtBookRating = (TextView)itemView.findViewById(R.id.txtFavBookRating);
            imgBookImage = (ImageView) itemView.findViewById(R.id.imgFavBookImage);
            llContent = (LinearLayout)itemView.findViewById(R.id.llFavContent);
        }
    }
}
