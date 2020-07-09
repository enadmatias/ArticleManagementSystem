package com.example.articlemanagementsystem.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.articlemanagementsystem.Constant;
import com.example.articlemanagementsystem.R;
import com.example.articlemanagementsystem.Session;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btn_login;
    EditText editTxt_Username, editText_Password;
    TextView txtView_SignUp;
    Session session;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btn_login = findViewById(R.id.btn_Signin);
        editTxt_Username = findViewById(R.id.editTxt_username);
        editText_Password = findViewById(R.id.editTxt_password);
        txtView_SignUp = findViewById(R.id.textView_SignUp);


        btn_login.setOnClickListener(this);
        txtView_SignUp.setOnClickListener(this);
        session = new Session(getApplicationContext());

    }

    @Override
    public void onClick(View v) {
      int id = v.getId();
      switch (id){
          case R.id.btn_Signin:
              if(isNotEmptyFields()) {
                  String uname = editTxt_Username.getText().toString();
                  String upassword = editText_Password.getText().toString();
                  Login(uname, upassword);
              }
               break;
          case R.id.textView_SignUp:
               startActivity(new Intent(this, RegisterActivity.class));
               break;
      }

    }

    private void Login(final String uname, final String upass) {
        StringRequest request = new StringRequest(Request.Method.POST, Constant.API+"login.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String message = jsonObject.getString("message");
                    if(message.equals("success")){
                        String id = jsonObject.getString("userId");
                        session.createSession(id);
                        startActivity(new Intent(getApplicationContext(), ArticleDashboard.class));
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        finish();
                        Toast.makeText(getApplicationContext(), "Successfully Login", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "Email or password Mismatch", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {

                    Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
                error.printStackTrace();
            }
        }){
            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("uname", uname);
                params.put("upass", upass);
                return params;
            }


        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(request);
    }

    private boolean isNotEmptyFields() {
        String username = editTxt_Username.getText().toString().trim();
        String password = editText_Password.getText().toString().trim();

        if (username.isEmpty()) {
            Toast.makeText(this, "Fields can not be empty!", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (password.isEmpty()) {
            Toast.makeText(this, "Fields can not be empty!", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;

    }


    @Override
    protected void onStart() {
        super.onStart();
        if(session.isLoggin())
        {
            startActivity(new Intent(this, ArticleDashboard.class));
        }

        Log.v("", "onstart");
    }
    @Override
    protected void onResume() {
        super.onResume();

        Log.d("","onResume invoked");
    }

    @Override
    protected void onStop() {
        super.onStop();


        Log.d("","onStop invoked");
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(session.isLoggin())
        { startActivity(new Intent(this, ArticleDashboard.class));
        }
        Log.d("","onPause invoked");
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        Log.d("","onRestart invoked");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);


    }


}
