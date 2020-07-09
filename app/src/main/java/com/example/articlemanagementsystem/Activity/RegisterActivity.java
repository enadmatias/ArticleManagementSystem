package com.example.articlemanagementsystem.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    EditText editText_Fullname, editText_emailAdd, editText_Username, editText_password, editText_Cpassword;
    Button btn_SignUp;
    TextView txtView_Login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        editText_Fullname = findViewById(R.id.editText_Reg_Fullname);
        editText_emailAdd = findViewById(R.id.editText_Reg_EmailAdd);
        editText_Username = findViewById(R.id.editText_Reg_Username);
        editText_password = findViewById(R.id.editText_Reg_password);
        editText_Cpassword = findViewById(R.id.editText_Reg_CPassword);
        txtView_Login = findViewById(R.id.textView_Login);
        btn_SignUp = findViewById(R.id.button_SignUp);

        btn_SignUp.setOnClickListener(this);
        txtView_Login.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
          int id = v.getId();

          switch (id){
              case R.id.button_SignUp:
                  String fname = editText_Fullname.getText().toString();
                  String emailAdd = editText_emailAdd.getText().toString();
                  String username = editText_Username.getText().toString();
                  String password = editText_password.getText().toString();
                  String cpassword = editText_Cpassword.getText().toString();

                  Register(fname,emailAdd,username,password,cpassword);
                  break;
              case  R.id.textView_Login:
                  finish();
                  startActivity(new Intent(this, LoginActivity.class));
                  break;
          }

    }

    private void Register(final String Fname , final String EmailAdd, final String Username, final String Password, final String Cpassword) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.API + "register.php", new Response.Listener<String>() {
            @Override

            public void onResponse(String response) {
                Log.d("Error", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String message = jsonObject.getString("message");
                    if(message.equals("success")){

                        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                        finish();
                        Toast.makeText(getApplicationContext(), "Successfully Registered", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "Registration Failed", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Fname", Fname);
                params.put("EmailAdd", EmailAdd);
                params.put("Username", Username);
                params.put("Password", Password);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    public boolean isNotEmptyFields() {

        String fullname = editText_Fullname.getText().toString().trim();
        String email = editText_emailAdd.getText().toString().trim();
        String username = editText_Username.getText().toString().trim();
        String password = editText_password.getText().toString().trim();
        String cpass = editText_Cpassword.getText().toString().trim();

        if (fullname.isEmpty()) {
            Toast.makeText(this, "Fields can not be empty!", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (username.isEmpty()) {
            Toast.makeText(this, "Fields can not be empty!", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (cpass.isEmpty()) {
            Toast.makeText(this, "Fields can not be empty!", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (username.isEmpty()) {
            Toast.makeText(this, "Fields can not be empty!", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (email.isEmpty()) {
            Toast.makeText(this, "Fields can not be empty!", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (password.isEmpty()) {
            Toast.makeText(this, "Fields can not be empty!", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;

    }

}
