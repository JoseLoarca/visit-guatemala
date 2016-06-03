package org.jcloarca.visitguatemala;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.jcloarca.visitguatemala.volley.WebService;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Registro extends AppCompatActivity {

    private Button btnRegistrar, btnLogin;
    private EditText txtNombre, txtTelefono, txtCorreo, txtDireccion, txtUsername, txtContraseña;
    SharedPreferences usuarioLogueado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        btnRegistrar = (Button) findViewById(R.id.btnRegistrar);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        txtNombre = (EditText) findViewById(R.id.txtNombre);
        txtTelefono = (EditText) findViewById(R.id.txtTelefono);
        txtCorreo = (EditText) findViewById(R.id.txtCorreo);
        txtDireccion = (EditText) findViewById(R.id.txtDireccion);
        txtUsername = (EditText) findViewById(R.id.txtUsername);
        txtContraseña = (EditText) findViewById(R.id.txtContraseña);

        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean failText = false;
                if(txtNombre.getText().toString().trim().length() == 0){
                    failText = true;
                    txtNombre.setError("Este campo es obligatorio!");
                }
                if(txtTelefono.getText().toString().trim().length() == 0){
                    failText = true;
                    txtTelefono.setError("Este campo es obligatorio!");
                }
                if(txtCorreo.getText().toString().trim().length() == 0){
                    failText = true;
                    txtCorreo.setError("Este campo es obligatorio!");
                }
                if(txtDireccion.getText().toString().trim().length() == 0){
                    failText = true;
                    txtDireccion.setError("Este campo es obligatorio!");
                }
                if(txtUsername.getText().toString().trim().length() == 0){
                    failText = true;
                    txtUsername.setError("Este campo es obligatorio!");
                }
                if(txtContraseña.getText().toString().trim().length() == 0){
                    failText = true;
                    txtContraseña.setError("Este campo es obligatorio!");
                }
                if(failText == false){
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("nombreCompleto", txtNombre.getText().toString());
                    params.put("telefono", txtTelefono.getText().toString());
                    params.put("correo", txtCorreo.getText().toString());
                    params.put("direccion", txtDireccion.getText().toString());
                    params.put("username", txtUsername.getText().toString());
                    params.put("password", txtContraseña.getText().toString());
                    JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, WebService.registro, new JSONObject(params), new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                String mensajeRespuesta = response.getString("mensaje");
                                if (mensajeRespuesta.length() > 0) {
                                    new AlertDialog.Builder(Registro.this).setTitle("Éxito!")
                                            .setMessage("El usuario, "+txtNombre.getText().toString()+ "ha sido registrado con éxito, por favor inicie sesión.")
                                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    Intent intent = new Intent (Registro.this, Login.class);
                                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                    startActivity(intent);
                                                    finish();
                                                }
                                            }).setIcon(android.R.drawable.sym_def_app_icon)
                                            .show();
                                } else {
                                    Toast.makeText(getApplicationContext(), "Operación fallida!", Toast.LENGTH_LONG).show();
                                }
                            } catch (Exception ex) {
                                Log.e("Error: ", ex.getMessage());
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("Error: Response", " " + error.getMessage());
                        }
                    });
                    WebService.getInstance(v.getContext()).addToRequestQueue(request);
            }
        }});

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Registro.this, Login.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });
    }
}
