package com.internshala.bookhub.fragment;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.internshala.bookhub.R;
import com.internshala.bookhub.adapter.FavoriteRecyclerAdapter;
import com.internshala.bookhub.database.BookDatabase;
import com.internshala.bookhub.database.BookEntity;

import java.util.List;
import java.util.concurrent.ExecutionException;


/**
 * A simple {@link Fragment} subclass.
 */
public class FavoritesFragment extends Fragment {

    public FavoritesFragment() {
        // Required empty public constructor
    }

    RecyclerView recyclerFavorite;
    RelativeLayout progressLayout;
    ProgressBar progressBar;
    RecyclerView.LayoutManager layoutManager;
    FavoriteRecyclerAdapter recyclerAdapter;
    BookDatabase db;
    List<BookEntity> dbBookList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorites, container, false);

        recyclerFavorite = (RecyclerView) view.findViewById(R.id.recyclerFavorite);
        progressLayout = (RelativeLayout) view.findViewById(R.id.progressLayout);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);

        layoutManager = new GridLayoutManager((Context)getActivity(), 2);
        db = Room.databaseBuilder((Context)getActivity(), BookDatabase.class, "books-db").build();

        try {
            dbBookList = new RetrieveFavorites().execute().get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        if(dbBookList != null && getActivity() != null) {
            progressLayout.setVisibility(View.GONE);
            recyclerAdapter = new FavoriteRecyclerAdapter((Context)getActivity(), dbBookList);
            recyclerFavorite.setAdapter(recyclerAdapter);
            recyclerFavorite.setLayoutManager(layoutManager);
        }

        return view;
    }

    class RetrieveFavorites extends AsyncTask<Void, Void, List<BookEntity>> {

        @Override
        protected List<BookEntity> doInBackground(Void... voids) {
            return db.bookDao().getAllBooks();
        }
    }

}
