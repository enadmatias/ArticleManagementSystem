package com.example.articlemanagementsystem.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Html;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.articlemanagementsystem.Constant;
import com.example.articlemanagementsystem.Model.ArticleData;
import com.example.articlemanagementsystem.R;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class UpdatePostActivity extends AppCompatActivity implements View.OnClickListener {

    CircularImageView circularImageView_thumbnail;
    EditText article_title, article_content;
    Button btn_Update;
    String id;
    ActionBar actionBar;


    Uri tempUri;
    Bitmap bitmap;
    private static final int PICK_IMAGE = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_post);

        circularImageView_thumbnail = findViewById(R.id.image_thumbnail_1);
        article_title = findViewById(R.id.editText_title);
        article_content = findViewById(R.id.editText_articleContents);
        btn_Update = findViewById(R.id.btn_Update);

        actionBar = getSupportActionBar();
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#5BB0BA"));
        actionBar.setTitle(Html.fromHtml("<font color='#EBF5F7'>Article Management System </font>"));
        actionBar.setBackgroundDrawable(colorDrawable);


        Intent i = getIntent();
        id = i.getStringExtra("ArticleID");
        Log.d("TAG", "ArticleID" + id);

        displayData(id);

        btn_Update.setOnClickListener(this);


    }

    private void displayData(final String articleid) {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, Constant.API+"articleList.php"  , null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("Tag", "Request body: " + new String(response.toString()));
                        try {
                            JSONArray jsonArray = response.getJSONArray("articlesData");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject articles = jsonArray.getJSONObject(i);
                                String article_id = articles.getString("articleId");
                                Log.d("TAG", "Article " + article_id);
                                if(article_id.equals(articleid)) {
                                    String title = articles.getString("articleTitle");
                                    String content = articles.getString("articleContent");
                                    String thumbnail = articles.getString("thumbnail");
                                    Picasso.get().load(thumbnail).resize(100, 100).into(circularImageView_thumbnail);
                                    article_title.setText(title);
                                    article_content.setText(content);
                                }

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },   new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(request);
    }

    @Override
    public void onClick(View v) {
        final String title = article_title.getText().toString();
        final String content = article_content.getText().toString();


        StringRequest request = new StringRequest(Request.Method.POST, Constant.API + "updateArticle.php", new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("TAG", "Error" + response);
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response);
                    String message = jsonObject.getString("message");

                    if(message.equals("success")){
                        startActivity(new Intent(getApplicationContext(), ArticleDashboard.class));
                        finish();
                        Toast.makeText(getApplicationContext(), "Successfully Updated", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "Failed to Update", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id", id);
                params.put("title", title);
                params.put("content", content);
                return params;
            }


        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(request);



    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.home:
                Intent intent = NavUtils.getParentActivityIntent(this);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                        | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                NavUtils.navigateUpTo(this, intent);
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
       Intent i = new Intent(this,ArticleDashboard.class);
       startActivity(i);
       overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);


    }


}
