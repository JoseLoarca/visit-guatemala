package org.jcloarca.visitguatemala;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class Principal extends AppCompatActivity {

    private TextView txtUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        txtUsuario =(TextView) findViewById(R.id.txtUsuario);

        Bundle bundle = this.getIntent().getExtras();

        txtUsuario.setText(bundle.getString("loggedUser").toString());
    }
}
