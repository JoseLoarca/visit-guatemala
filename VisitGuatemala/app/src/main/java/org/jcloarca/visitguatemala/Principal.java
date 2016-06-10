package org.jcloarca.visitguatemala;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Principal extends AppCompatActivity {

    private TextView txtUsuario;
    private Button btnMiCuenta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        txtUsuario =(TextView) findViewById(R.id.txtUsuario);
        Bundle bundle = this.getIntent().getExtras();
        txtUsuario.setText("Usuario logueado: "+bundle.getString("loggedUser").toString());

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
            switch(id){
                case R.id.action_about:
                    about();
                    break;
                case R.id.action_logout:
                    logout();
                    break;
                default:
                    return super.onOptionsItemSelected(item);
            }
        return true;
    }

    private void logout() {
        new AlertDialog.Builder(Principal.this).setTitle("Cerrar Sesión")
            .setMessage("Realmente desea cerrar sesión?")
            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(Principal.this, Login.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }
            }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        }).setIcon(android.R.drawable.ic_dialog_alert)
            .show();
    }

    private void about() {
        VisitGuatemalaApp app = (VisitGuatemalaApp) getApplication();
        String strUrl = app.getUrlAbout();

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(strUrl));
        startActivity(intent);
    }
}
