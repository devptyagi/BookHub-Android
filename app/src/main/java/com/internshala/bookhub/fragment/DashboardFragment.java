package com.internshala.bookhub.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.internshala.bookhub.R;
import com.internshala.bookhub.adapter.DashboardRecyclerAdapter;
import com.internshala.bookhub.model.Book;
import com.internshala.bookhub.util.ConnectionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class DashboardFragment extends Fragment {

    public DashboardFragment() {
        // Required empty public constructor
    }

    RecyclerView recyclerDashboard;
    RecyclerView.LayoutManager layoutManager;
    ConnectionManager connectionManager;
    ArrayList<Book> bookInfoList = new ArrayList<>();
    RelativeLayout progressLayout;
    ProgressBar progressBar;
    DashboardRecyclerAdapter recyclerAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        setHasOptionsMenu(true);

        progressLayout =  (RelativeLayout)view.findViewById(R.id.progressLayout);

        progressBar = (ProgressBar)view.findViewById(R.id.progressBar);

        progressLayout.setVisibility(View.VISIBLE);

        connectionManager = new ConnectionManager();

        recyclerDashboard = (RecyclerView)view.findViewById(R.id.recyclerDashboard);

        layoutManager = new LinearLayoutManager(getActivity());

        RequestQueue queue = Volley.newRequestQueue((Context)getActivity());

        String url = "http://13.235.250.119/v1/book/fetch_books/";

        if(connectionManager.checkConnectivity((Context)getActivity())){

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                progressLayout.setVisibility(View.INVISIBLE);
                                boolean success = response.getBoolean("success");
                                JSONArray data;
                                if(success) {
                                    data = response.getJSONArray("data");
                                    for(int i=0;i<data.length();i++) {
                                        JSONObject bookJsonObject = data.getJSONObject(i);
                                        Book bookObject = new Book(
                                                bookJsonObject.getString("book_id"),
                                                bookJsonObject.getString("name"),
                                                bookJsonObject.getString("author"),
                                                bookJsonObject.getString("rating"),
                                                bookJsonObject.getString("price"),
                                                bookJsonObject.getString("image")
                                        );

                                        bookInfoList.add(bookObject);

                                        recyclerAdapter = new DashboardRecyclerAdapter((Context)getActivity(),bookInfoList);

                                        recyclerDashboard.setAdapter(recyclerAdapter);

                                        recyclerDashboard.setLayoutManager(layoutManager);

                                    }
                                } else {
                                    Toast.makeText((Context)getActivity(), "Some Error Occurred", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                Toast.makeText((Context)getActivity(),"Some Unexpected error occurred !", Toast.LENGTH_SHORT).show();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            if(getActivity() != null) {
                                Toast.makeText((Context) getActivity(), "Volley error occurred !", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }) {

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<>();
                    headers.put("Content-type", "application/json");
                    headers.put("token", "5316cf769e236b");
                    return headers;
                }

            };

            queue.add(jsonObjectRequest);

        } else {
            AlertDialog.Builder dialog = new AlertDialog.Builder((Context)getActivity());
            dialog.setTitle("Error");
            dialog.setMessage("Internet Connection not Found");
            dialog.setPositiveButton("Open Settings", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent settingsIntent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
                    startActivity(settingsIntent);
                    getActivity().finish();
                }
            });
            dialog.setNegativeButton("EXIT", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ActivityCompat.finishAffinity((Activity)getActivity());
                }
            });
            dialog.create();
            dialog.show();
        }



        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_dashboard, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();
        if(id == R.id.action_sort) {
            Collections.sort(bookInfoList, new Comparator<Book>() {
                @Override
                public int compare(Book o1, Book o2) {
                    if(o1.bookRating.compareTo(o2.bookRating) == 0) {
                        return o1.bookName.compareTo(o2.bookName);
                    }
                    return o1.bookRating.compareTo(o2.bookRating);
                }
            });

            Collections.reverse(bookInfoList);
        }
        recyclerAdapter.notifyDataSetChanged();

        return super.onOptionsItemSelected(item);
    }
}
