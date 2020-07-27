package com.internshala.bookhub.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.room.Room;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.internshala.bookhub.R;
import com.internshala.bookhub.database.BookDatabase;
import com.internshala.bookhub.database.BookEntity;
import com.internshala.bookhub.util.ConnectionManager;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class DescriptionActivity extends AppCompatActivity {

    TextView txtBookName;
    TextView txtBookAuthor;
    TextView txtBookPrice;
    TextView txtBookRating;
    ImageView imgBookImage;
    TextView txtBookDesc;
    Button btnAddToFav;
    RelativeLayout progressLayout;
    ProgressBar progressBar;
    Toolbar toolbar;
    ConnectionManager connectionManager;
    BookDatabase db;

    String bookId = "-1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description);

        txtBookName = (TextView)findViewById(R.id.txtBookName);
        txtBookAuthor = (TextView)findViewById(R.id.txtAuthorName);
        txtBookPrice = (TextView)findViewById(R.id.txtBookPrice);
        txtBookRating = (TextView)findViewById(R.id.txtBookRating);
        imgBookImage = (ImageView)findViewById(R.id.bookImage);
        txtBookDesc = (TextView)findViewById(R.id.txtBookDesc);
        btnAddToFav = (Button)findViewById(R.id.btnAddTofav);
        progressBar = (ProgressBar)findViewById(R.id.pBar);
        progressLayout = (RelativeLayout)findViewById(R.id.pLayout);
        progressLayout.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        connectionManager = new ConnectionManager();

        db = Room.databaseBuilder(getApplicationContext(), BookDatabase.class, "books-db").build();

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Book Details");

        if(getIntent() != null) {
            bookId = getIntent().getStringExtra("book_id");
            //System.out.println("BOOK ID IS: "+bookId);
        }else {
            finish();
            Toast.makeText(DescriptionActivity.this, "Some Error Occurred", Toast.LENGTH_SHORT).show();
        }

        if(bookId == "-1") {
            finish();
            Toast.makeText(DescriptionActivity.this, "Some Error Occurred", Toast.LENGTH_SHORT).show();
        }

        RequestQueue queue = Volley.newRequestQueue(DescriptionActivity.this);
        String url = "http://13.235.250.119/v1/book/get_book/";

        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("book_id",bookId);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if(connectionManager.checkConnectivity(DescriptionActivity.this)){
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonParams,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                boolean success = response.getBoolean("success");
                                if(success) {
                                    JSONObject bookJsonObject = response.getJSONObject("book_data");
                                    progressLayout.setVisibility(View.GONE);
                                    String bookImageUrl = bookJsonObject.getString("image");
                                    Picasso.get().load(bookJsonObject.getString("image")).error(R.drawable.default_book_cover).into(imgBookImage);
                                    txtBookName.setText(bookJsonObject.getString("name"));
                                    txtBookAuthor.setText(bookJsonObject.getString("author"));
                                    txtBookPrice.setText(bookJsonObject.getString("price"));
                                    txtBookRating.setText(bookJsonObject.getString("rating"));
                                    txtBookDesc.setText(bookJsonObject.getString("description"));

                                    final BookEntity bookEntity = new BookEntity(
                                            bookId,
                                            txtBookName.getText().toString(),
                                            txtBookAuthor.getText().toString(),
                                            txtBookPrice.getText().toString(),
                                            txtBookRating.getText().toString(),
                                            txtBookDesc.getText().toString(),
                                            bookImageUrl
                                    );

                                    DBASyncTask task1 = new DBASyncTask(bookEntity, 1);
                                    boolean checkFav = task1.execute().get();

                                    if(checkFav) {
                                        btnAddToFav.setText("Remove from Favorite");
                                    } else {
                                        btnAddToFav.setText("Add to Favorites");
                                    }

                                    btnAddToFav.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            try {
                                                DBASyncTask task2 = new DBASyncTask(bookEntity, 1);
                                                boolean check = false;
                                                check = task2.execute().get();
                                                if(!check) {
                                                    DBASyncTask async = new DBASyncTask(bookEntity, 2);
                                                    boolean added = async.execute().get();
                                                    if(added) {
                                                        Toast.makeText(DescriptionActivity.this, "Book Added to Favorites", Toast.LENGTH_SHORT).show();
                                                        btnAddToFav.setText("Remove from Favorites");
                                                    } else {
                                                        Toast.makeText(DescriptionActivity.this, "Some error occurred", Toast.LENGTH_SHORT).show();
                                                    }
                                                } else {
                                                    DBASyncTask async = new DBASyncTask(bookEntity, 3);
                                                    boolean removed = async.execute().get();
                                                    if(removed) {
                                                        Toast.makeText(DescriptionActivity.this, "Book Removed from Favorites", Toast.LENGTH_SHORT).show();
                                                        btnAddToFav.setText("Add to Favorites");
                                                    } else {
                                                        Toast.makeText(DescriptionActivity.this, "Some error occurred", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            } catch (ExecutionException e) {
                                                e.printStackTrace();
                                            } catch (InterruptedException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    });

                                }
                                else {
                                    Toast.makeText(DescriptionActivity.this, "Some Error Occurred", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                Toast.makeText(DescriptionActivity.this,"Some Unexpected error occurred !", Toast.LENGTH_SHORT).show();
                            } catch (InterruptedException e) {
                                Toast.makeText(DescriptionActivity.this,"InterriptedException occurred !", Toast.LENGTH_SHORT).show();
                            } catch (ExecutionException e) {
                                Toast.makeText(DescriptionActivity.this,"ExecutionException occurred !", Toast.LENGTH_SHORT).show();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(DescriptionActivity.this,"Volley error occurred !", Toast.LENGTH_SHORT).show();
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
            AlertDialog.Builder dialog = new AlertDialog.Builder(DescriptionActivity.this);
            dialog.setTitle("Error");
            dialog.setMessage("Internet Connection not Found");
            dialog.setPositiveButton("Open Settings", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent settingsIntent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
                    startActivity(settingsIntent);
                    finish();
                }
            });
            dialog.setNegativeButton("EXIT", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ActivityCompat.finishAffinity(DescriptionActivity.this);
                }
            });
            dialog.create();
            dialog.show();
        }
    }

    class DBASyncTask extends AsyncTask<Void, Void, Boolean> {

        BookEntity bookEntity;
        int mode;

        public  DBASyncTask(BookEntity bookEntity, int mode) {
            this.bookEntity = bookEntity;
            this.mode = mode;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            if(mode == 1) {
                BookEntity book = db.bookDao().getBookById(bookEntity.book_id);
                return book != null;
            }
            if(mode == 2) {
                db.bookDao().insertBook(bookEntity);
                return true;
            }
            if(mode == 3) {
                db.bookDao().deletBook(bookEntity);
                return true;
            }
            return false;
        }
    }




}
