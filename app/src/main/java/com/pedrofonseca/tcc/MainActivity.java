package com.pedrofonseca.tcc;

import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.View;

import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.pedrofonseca.tcc.data.FirebaseAuthentication;
import com.pedrofonseca.tcc.ui.login.CadastroActivity;
import com.pedrofonseca.tcc.ui.login.LoginActivity;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
        private FirebaseAuth auth;
        private NavigationView navigationViewL;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        auth = new FirebaseAuthentication().getFirebaseAuthentication();
        boolean l;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkLogin();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        FloatingActionButton fab = findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_login) {

            Intent login = new Intent(this, LoginActivity.class);
            startActivity(login);
        } else if (id == R.id.nav_cadastro) {
            Intent cadastro = new Intent(this, CadastroActivity.class);
            startActivity(cadastro);
        } else if (id == R.id.nav_send) {

        }else if  (id == R.id.nav_logout){
            logout();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void startButton(View view){
        TextView button = findViewById(R.id.button);
        if(button.getText().equals("On")){
            button.setText("Off");
        }else {
            button.setText("On");
        }
    }

    public boolean checkLogin(){
        auth = new FirebaseAuthentication().getFirebaseAuthentication();
        navigationViewL = findViewById(R.id.nav_view);
        Log.i("Info", navigationViewL.toString());
         Menu navigationMenu = navigationViewL.getMenu();

        if( auth.getCurrentUser() != null){

            navigationMenu.findItem(R.id.nav_cadastro).setVisible(false);
            navigationMenu.findItem(R.id.nav_login).setVisible(false);
            navigationMenu.findItem(R.id.nav_logout).setVisible(true);
            return true;

        }else{
            navigationMenu.findItem(R.id.nav_cadastro).setVisible(true);
            navigationMenu.findItem(R.id.nav_login).setVisible(true);
            navigationMenu.findItem(R.id.nav_logout).setVisible(false);
            return false;
        }
    }

    public void logout(){
        auth.signOut();
        checkLogin();
        Toast.makeText(getApplicationContext(),"Usuario deslogado",Toast.LENGTH_LONG).show();
    }
}
