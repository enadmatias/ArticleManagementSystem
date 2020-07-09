package com.example.articlemanagementsystem.Activity;



import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;


import android.os.Handler;
import android.provider.MediaStore;
import android.text.Html;


import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.articlemanagementsystem.Adapter.ArticleListAdapter;
import com.example.articlemanagementsystem.Constant;
import com.example.articlemanagementsystem.Model.ArticleData;
import com.example.articlemanagementsystem.R;
import com.example.articlemanagementsystem.Session;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ArticleDashboard extends AppCompatActivity implements View.OnClickListener, ArticleListAdapter.ArticleItemListener {
    ActionBar actionBar;
    private List<ArticleData> dataList = new ArrayList<>();
    private RecyclerView recyclerView;
    private ArticleListAdapter articleListAdapter;
    private static final String TAG = "Article Dashboard";




    Uri tempUri;
    Bitmap bitmap;
    private static final int PICK_IMAGE = 1;

    ImageView imgView_Thumbnail, imageView_attachFile;
    EditText editText_ArticleTitle, editText_ArticleContent, editText_NameOfImage;
    Button btn_PostArticle;
    Session session;
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_dashboard);


        session = new Session(getApplicationContext());

        actionBar = getSupportActionBar();
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#5BB0BA"));
        actionBar.setTitle(Html.fromHtml("<font color='#EBF5F7'>Article Management System </font>"));
        actionBar.setBackgroundDrawable(colorDrawable);

        imageView_attachFile = findViewById(R.id.imageView_attach);
        imgView_Thumbnail = findViewById(R.id.imageView_Thumbnail);
        editText_ArticleTitle = findViewById(R.id.editText_articleTitle);
        editText_ArticleContent = findViewById(R.id.editText_articleArea);
        editText_NameOfImage = findViewById(R.id.editTxt_nameOfpic);
        btn_PostArticle = findViewById(R.id.btn_Post);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);

        recyclerView = findViewById(R.id.recyclerView);
        articleListAdapter = new ArticleListAdapter(this, dataList, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(articleListAdapter);

        imageView_attachFile.setOnClickListener(this);
        btn_PostArticle.setOnClickListener(this);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to make your refresh action
                // CallYourRefreshingMethod();
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(swipeRefreshLayout.isRefreshing()) {
                            swipeRefreshLayout.setRefreshing(false);
                        }
                    }
                }, 1000);
            }
        });

        dataList.clear();

//        dataList.add(new ArticleData("Homemade Pandesal", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Tellus molestie nunc non blandit massa enim. Amet commodo nulla facilisi nullam vehicula ipsum a arcu. Tincidunt eget nullam non nisi est sit amet facilisis magna. Vivamus arcu felis bibendum ut tristique et. Pellentesque id nibh tortor id aliquet lectus proin nibh. Scelerisque in dictum non consectetur a erat. ", "02-05-26"));
//        dataList.add(new ArticleData("Tinongs Bakery", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Tellus molestie nunc non blandit massa enim. Amet commodo nulla facilisi nullam vehicula ipsum a arcu. Tincidunt eget nullam non nisi est sit amet facilisis magna. Vivamus arcu felis bibendum ut tristique et. Pellentesque id nibh tortor id aliquet lectus proin nibh. Scelerisque in dictum non consectetur a erat. ", "02-05-26"));
        populateData();
    }


    private void populateData() {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, Constant.API+"articleList.php"  , null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("Tag", "Request body: " + new String(response.toString()));
                        try {
                            JSONArray jsonArray = response.getJSONArray("articlesData");
                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject articles = jsonArray.getJSONObject(i);
                                String user_id = articles.getString("userId");
                                if(user_id.equals(session.toString())) {
                                    String id = articles.getString("articleId");
                                    String title = articles.getString("articleTitle");
                                    String content = articles.getString("articleContent");
                                    String date = articles.getString("articleDate");
                                    String thumbnail = articles.getString("thumbnail");
                                    dataList.add(new ArticleData(id, thumbnail, title, content, date));
                                    articleListAdapter.notifyDataSetChanged();
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(request);

    }






    @Override
    public void onClick(View v) {
         int id = v.getId();
         switch (id){
             case R.id.imageView_attach:
                 openGalleryIntent();
                 break;
             case R.id.btn_Post:
                 saveData(editText_ArticleTitle.getText().toString(), editText_ArticleContent.getText().toString(), editText_NameOfImage.getText().toString());
                 break;
         }
    }

    private void saveData(final String articleTitle, final String articleContent, final String thumbnailName) {
        StringRequest request = new StringRequest(Request.Method.POST, Constant.API + "postArticle.php", new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("error", response);
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response);
                    String message = jsonObject.getString("message");
                    if(message.equals("success")){
                        finish();
                        startActivity(new Intent(getApplicationContext(), ArticleDashboard.class));
                        Toast.makeText(getApplicationContext(), "Successfully Posted", Toast.LENGTH_SHORT).show();

                    }
                    else{
                        Toast.makeText(getApplicationContext(), "Failed to Post", Toast.LENGTH_SHORT).show();
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
                params.put("articleTitle", articleTitle);
                params.put("articleContent", articleContent);
                params.put("thumbnailName", thumbnailName);
                params.put("userId", session.toString());
                params.put("Thumbnail", imagetoString(bitmap));
                return params;
            }


        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(request);

    }



    private void openGalleryIntent() {
        Intent intent = new Intent(); //blind intent
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE) {
            tempUri = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), tempUri);
                imgView_Thumbnail.setImageURI(tempUri);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }
    private String imagetoString(Bitmap bitmap){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
        byte [] imageBytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imageBytes,Base64.DEFAULT);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.options_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.profile:
                break;
            case  R.id.logout:
                 logout();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void logout(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ArticleDashboard.this);
        alertDialogBuilder.setMessage("Would you like to logout?");
        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                finish();
                session.logout();
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alertDialogBuilder.create();
        alertDialogBuilder.show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(!session.isLoggin())
        {
            startActivity(new Intent(this, LoginActivity.class));
        }

        Log.v(TAG, "onstart");
    }
    @Override
    protected void onResume() {
        super.onResume();

        Log.d(TAG,"onResume invoked");
    }

    @Override
    protected void onStop() {
        super.onStop();


        Log.d(TAG,"onStop invoked");
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(!session.isLoggin())
        { startActivity(new Intent(this, LoginActivity.class));
        }
        Log.d(TAG,"onPause invoked");
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        Log.d(TAG,"onRestart invoked");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);


    }


    @Override
    public void articleItemClick(int position) {
     dataList.remove(position);
     articleListAdapter.notifyDataSetChanged();

    }
}
