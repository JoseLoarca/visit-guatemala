package org.jcloarca.visitguatemala;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.jcloarca.visitguatemala.bean.Usuario;
import org.jcloarca.visitguatemala.volley.WebService;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {

    private Button btnLogin, btnRegistro;
    private EditText txtEmail, txtPassword;
    private Usuario loggedUser = null;
    private ProgressBar progressBar;

    public final static String USERPREFERENCES = "UserPrefs";
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnLogin = (Button)findViewById(R.id.btnLogin);
        txtEmail = (EditText)findViewById(R.id.txtEmail);
        txtPassword = (EditText)findViewById(R.id.txtPassword);
        btnRegistro = (Button)findViewById(R.id.btnRegistro);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        sharedPreferences = getSharedPreferences(USERPREFERENCES, Context.MODE_PRIVATE);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                boolean failText = false;
                if(txtEmail.getText().toString().trim().length() == 0){
                    failText = true;
                    txtEmail.setError("Este campo es obligatorio!");
                }
                if(txtPassword.getText().toString().trim().length() == 0){
                    failText = true;
                    txtPassword.setError("Este campo es obligatorio!");
                }
                if(failText == false){
                    progressBar.setVisibility(View.VISIBLE);
                    loginDisabler();
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("correo", txtEmail.getText().toString());
                    params.put("password", txtPassword.getText().toString());
                    JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, WebService.autenticar, new JSONObject(params), new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                JSONArray listaUsuarios = response.getJSONArray("user");
                                if (listaUsuarios.length() > 0) {
                                    JSONObject user = listaUsuarios.getJSONObject(0);

                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putInt("idUsuario", user.getInt("idUsuario"));
                                    editor.putString( "NombreCompleto",user.getString("nombreCompleto"));
                                    editor.putString("Telefono", user.getString("telefono"));
                                    editor.putString("Email", user.getString("correo"));
                                    editor.putString("Direccion", user.getString("direccion"));
                                    editor.putString("Username", user.getString("username"));
                                    editor.putString("Password", null);
                                    editor.putString("Token", response.getString("token"));
                                    editor.putString("Exp", response.getString("exp"));


                                    loggedUser = new Usuario(
                                            user.getInt("idUsuario"),
                                            user.getString("nombreCompleto"),
                                            user.getString("telefono"),
                                            user.getString("correo"),
                                            user.getString("direccion"),
                                            user.getString("username"),
                                            "",
                                            response.getString("token"),
                                            response.getString("exp")
                                    );
                                    Intent intent = new Intent(Login.this, Principal.class);
                                    intent.putExtra("loggedUser", loggedUser.getNombreCompleto());
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                    finish();

                                } else {
                                    loginEnabler();
                                    clear();
                                    progressBar.setVisibility(View.GONE);
                                    Snackbar.make(v, "Usuario no encontrado.", Snackbar.LENGTH_LONG)
                                            .setAction("Crear cuenta?", new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    startActivity(new Intent(Login.this, Registro.class));
                                                }
                                            }).show();
                                }
                            } catch (Exception ex) {
                                Log.e("Error: ", ex.getMessage());
                            }
                        }
                    }, new Response.ErrorListener(){
                        @Override
                        public void onErrorResponse(VolleyError error){
                            Log.e("Error: Response", "" +error.getMessage());
                        }
                    });
                    WebService.getInstance(v.getContext()).addToRequestQueue(request);
            }
        }});

        btnRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, Registro.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });

    }

    private void loginEnabler() {
        txtEmail.setEnabled(true);
        txtPassword.setEnabled(true);
        btnLogin.setEnabled(true);
        btnRegistro.setEnabled(true);
    }

    private void loginDisabler(){
        txtEmail.setEnabled(false);
        txtPassword.setEnabled(false);
        btnLogin.setEnabled(false);
        btnRegistro.setEnabled(false);
    }

    private void clear() {
        txtPassword.setText("");
    }
}
