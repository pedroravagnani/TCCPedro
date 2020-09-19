package com.pedrofonseca.tcc;

import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.util.Log;
import android.view.View;

import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.pedrofonseca.tcc.data.FirebaseAuthentication;
import com.pedrofonseca.tcc.ui.login.CadastroActivity;
import com.pedrofonseca.tcc.ui.login.LoginActivity;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Set;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private FirebaseAuth auth;
    private NavigationView navigationViewL;
    private final static int REQUEST_ENABLE_BT = 1;
    private final String ARQ_PREF ="ARQPREF";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        auth = new FirebaseAuthentication().getFirebaseAuthentication();
        boolean l;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkLogin();



        TextView selectDeviceText =  findViewById(R.id.selectDeviceText);
        Button selectDeviceButton = findViewById(R.id.buttonSelectDevice);

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
        BroadcastReceiver receiver = null;
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(receiver, filter);

        selectDeviceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedDeviceSearch();
            }
        });

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

        } else if (id == R.id.nav_logout) {
            logout();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void startButton(View view) {
        TextView button = findViewById(R.id.button);
        if (button.getText().equals("On")) {
            button.setText("Off");

        } else {
            if (checkLogin()) {
                button.setText("On");
                BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                if (bluetoothAdapter == null) {
                    // Device doesn't support Bluetooth
                    Toast.makeText(getApplicationContext(), "Este dispositivo não possui um adaptador bluetooth utilizavel", Toast.LENGTH_LONG).show();
                } else {
                    enableBluetooth();
                }
            }
        }
    }

    public boolean checkLogin() {
        auth = new FirebaseAuthentication().getFirebaseAuthentication();
        navigationViewL = findViewById(R.id.nav_view);
        //Log.i("Info", navigationViewL.toString());
        Menu navigationMenu = navigationViewL.getMenu();

        if (auth.getCurrentUser() != null) {

            navigationMenu.findItem(R.id.nav_cadastro).setVisible(false);
            navigationMenu.findItem(R.id.nav_login).setVisible(false);
            navigationMenu.findItem(R.id.nav_logout).setVisible(true);
            return true;

        } else {
            navigationMenu.findItem(R.id.nav_cadastro).setVisible(true);
            navigationMenu.findItem(R.id.nav_login).setVisible(true);
            navigationMenu.findItem(R.id.nav_logout).setVisible(false);
            return false;
        }
    }

    public void logout() {
        auth.signOut();
        checkLogin();
        Toast.makeText(getApplicationContext(), "Usuario deslogado", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onResume() {
        super.onResume();
        checkLogin();
    }

    public void enableBluetooth() {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (!bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            Toast.makeText(getApplicationContext(), "Ligando bluetooth...", Toast.LENGTH_LONG).show();

            Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();

            if (pairedDevices.size() > 0) {
                // There are paired devices. Get the name and address of each paired device.
                for (BluetoothDevice device : pairedDevices) {
                    String deviceName = device.getName();
                    String deviceHardwareAddress = device.getAddress(); // MAC address
                }

            }


        }
    }

    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Discovery has found a device. Get the BluetoothDevice
                // object and its info from the Intent.
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                String deviceName = device.getName();
                String deviceHardwareAddress = device.getAddress(); // MAC address
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Don't forget to unregister the ACTION_FOUND receiver.
        unregisterReceiver(receiver);
    }

    public void selectedDeviceSearch(){
        SharedPreferences pref = getSharedPreferences(ARQ_PREF, 0);
        SharedPreferences.Editor editor = pref.edit();
        Log.i("Info","Listener do select device button");
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
        String[] arrayDevices = new String[pairedDevices.size()];
        Log.i("Info/Array","Size :" + arrayDevices.length);

        ArrayList<BluetoothDevice> arrayListPairedBluetoothDevices = new ArrayList<>();
        ArrayList<BluetoothDevice> arrayListBluetoothDevices = null;
        ArrayList<String> arrayListpaired = new ArrayList<String>();

        Gson gson = new Gson();



        if (pairedDevices.size() > 0) {
            int i=0;
            // There are paired devices. Get the name and address of each paired device.
            for (BluetoothDevice device : pairedDevices) {
                String deviceName = device.getName();
                String deviceHardwareAddress = device.getAddress(); // MAC address
                arrayDevices[i]= device.getName();

                
                arrayListpaired.add(device.getName()+"\n"+device.getAddress());
                arrayListPairedBluetoothDevices.add(device);
                i++;
            }

        }
//        for (BluetoothDevice device : arrayListPairedBluetoothDevices){
//            Log.i("INFO/Bluetooth Device Name: ", device.getName());
//            Log.i("INFO/Bluetooth Device Mac: ", device.getAddress());
//        }

        for (String device : arrayDevices){
            Log.i("INFO/Bluetooth Device Name: ", device);
        }


        AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
        boolean checkedItems[] = new boolean[arrayListPairedBluetoothDevices.size()];

        ArrayList<BluetoothDevice> arrayUserDevices = new ArrayList<BluetoothDevice>();

        mBuilder.setTitle("Paired Devices");
       mBuilder.setMultiChoiceItems(arrayDevices, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                    if(isChecked){
                        if(! arrayUserDevices.contains(which)){
                            arrayUserDevices.add(arrayListPairedBluetoothDevices.get(which));
                        }else {
                            arrayUserDevices.remove(arrayListPairedBluetoothDevices.get(which));
                        }
                    }
                }
        });

       mBuilder.setCancelable(false);
       mBuilder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
           @Override

           public void onClick(DialogInterface dialog, int which) {

               String json ="{\n";
               for (int i = 0; i < arrayUserDevices.size(); i++) {
                   json += "\""+i+"\" : {";
                   Log.i("INFO/Bluetooth devices selected", arrayUserDevices.get(i).toString());
                   Log.i("INFO/Bluetooth devices selected", arrayUserDevices.get(i).getName());
                   json += "\"name\":\"" +arrayUserDevices.get(i).getName() +"\",";
                   json += "\"address\":\"" +arrayUserDevices.get(i).getAddress()+"\"";
                  if(i+1==arrayUserDevices.size()){ json += "\n}";}
                  else{json += "\n},";}
//                   json += "bluetoothClass\":" +arrayUserDevices.get(i).getBluetoothClass()+"\",\n";
//                   json += "bondState\":" +arrayUserDevices.get(i).getBondState()+"\",\n";
//                   json += "type\":" +arrayUserDevices.get(i).getType()+"\",\n";
//                   json += "uuids\":" +arrayUserDevices.get(i).getUuids()+"\",\n";
//                   json += "class \":" +arrayUserDevices.get(i).getClass()+"\"";
               }
               json += "\n}";

               Log.i("INFO/the GSON",json);
               editor.putString("arrayUserDevices", json);
               editor.commit();
           }});
       mBuilder.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
           @Override
           public void onClick(DialogInterface dialog, int which) {
               dialog.dismiss();
           }
       });
       mBuilder.setNeutralButton("Clear All", new DialogInterface.OnClickListener() {
           @Override
           public void onClick(DialogInterface dialog, int which) {
               for(int i=0; i<checkedItems.length;i++){
                   checkedItems[i] = false;
                   arrayUserDevices.clear();
               }
           }
       });
       mBuilder.create();
       mBuilder.show();

//        AlertDialog selection = new AlertDialog.Builder(this)
//                .setTitle("Select Device")
//                .setCancelable(false)
//                .setIcon(R.drawable.)
//                .setTitle(R.string.)
//                .setSingleChoiceItems(seq, pos,null)
//                .setPositiveButton( R.string.ok, new DialogInterface.OnClickListener() {
//                    public void onClick( DialogInterface dialog, int whichButton)
//                    {
//                        // dialog dismissed
//                    }
//                }).create();

    }

}

