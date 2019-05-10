package org.elastos.app.hivedemo;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.File;

;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    private String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE};

    private String[] CAMERA_permissions = {Manifest.permission.CAMERA};


    private static SimpleCarrier sSimpleCarrier;

    private Toolbar toolbar;
    private ListView fileList;
    private TextView content_filepath, content_fileback;
    FileAdapter fileAdapter;
    File[] data;
    private String BaseFilePath;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int i = ContextCompat.checkSelfPermission(getApplicationContext(), permissions[0]);
            int l = ContextCompat.checkSelfPermission(getApplicationContext(), permissions[1]);
            if (i != PackageManager.PERMISSION_GRANTED || l != PackageManager.PERMISSION_GRANTED) {
                startRequestPermission();
            } else {
                initDate();
                initClick();
            }
        }
    }

    private void initView() {
        content_filepath = findViewById(R.id.content_filepath);
        content_fileback = findViewById(R.id.content_fileback);
        fileList = findViewById(R.id.content_filelist);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Local storage");
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        sSimpleCarrier = SimpleCarrier.getInstance();

    }

    private void initDate() {
        // 获得外部存储的路径
        String sDStateString = Environment.getExternalStorageState();

        if (sDStateString.equals(Environment.MEDIA_MOUNTED)) {
            File SDFile = Environment.getExternalStorageDirectory();
            data = SDFile.listFiles();
            fileAdapter = new FileAdapter(this, data);
            fileList.setAdapter(fileAdapter);
            BaseFilePath = SDFile.getPath();
            content_filepath.setText(SDFile.getPath());
            Log.d("MainActivity", BaseFilePath);
        }

    }

    private void initClick() {
        fileList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                File f = fileAdapter.getItem(position);
                if (f.isDirectory()) {
                    File[] files = f.listFiles();
                    fileAdapter = new FileAdapter(MainActivity.this, files);
                    fileList.setAdapter(fileAdapter);
                    content_filepath.setText(f.getPath());
                } else {
                    // TODO
                    Toast.makeText(MainActivity.this, "文件" + f.getName(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        content_fileback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (content_filepath.getText().toString().equals(BaseFilePath)) {
                    //没有上一级
                } else {
                    String filepath = content_filepath.getText().toString().substring(0, content_filepath.getText().toString().lastIndexOf("/"));
                    File file = new File(filepath);
                    File[] files = file.listFiles();
                    fileAdapter = new FileAdapter(MainActivity.this, files);
                    fileList.setAdapter(fileAdapter);
                    content_filepath.setText(file.getPath());
                }
            }
        });
    }

    private void startRequestPermission() {
        ActivityCompat.requestPermissions(this, permissions, 321);
    }

    private void startRequestPermission_CAMERA() {
        ActivityCompat.requestPermissions(this, CAMERA_permissions, 654);
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
        if (id == R.id.action_newfile) {
            //TODO New folder
            ShowEditDialog();
            return true;
        } else if (id == R.id.action_filepaste) {
            //TODO File Paste

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            //Local storage
            toolbar.setTitle("Local storage");
        } else if (id == R.id.nav_gallery) {
            //OneDrive
            toolbar.setTitle("OneDrive");

        } else if (id == R.id.nav_main) {
            Intent intent = new Intent(this, PersonalActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_myfriends) {
            Intent intent = new Intent(this, MyFriendsActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_addfriend) {
            AddFriend();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void AddFriend() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int i = ContextCompat.checkSelfPermission(getApplicationContext(), CAMERA_permissions[0]);
            if (i != PackageManager.PERMISSION_GRANTED) {
                startRequestPermission_CAMERA();
            } else {
                IntentIntegrator integrator = new IntentIntegrator(MainActivity.this);
                integrator.initiateScan();
            }
        }
    }


    /**
     * 用户权限 申请 的回调方法
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 321) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast toast = Toast.makeText(this, "Access failed, open manually!", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                } else {
                    initDate();
                    initClick();
                }
            }
        } else if (requestCode == 654) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast toast = Toast.makeText(this, "Access failed, open manually!", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                } else {
                    IntentIntegrator integrator = new IntentIntegrator(MainActivity.this);
                    integrator.initiateScan();
                }
            }
        }
    }


    /**
     * Activity回调方法
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (scanResult != null) {
            String result = scanResult.getContents();
            if (result != null && !result.isEmpty()) {
                //TODO Add friend.
                sSimpleCarrier.AddFriend(result);
                Toast.makeText(this, "Added successfully!", Toast.LENGTH_LONG).show();
            }
            return;
        }
    }

    private void ShowEditDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("New Folder");
        builder.setIcon(R.drawable.ic_folder);
        final EditText editText = new EditText(MainActivity.this);
        editText.setSingleLine(true);
        editText.setMaxLines(1);
        builder.setView(editText);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (!editText.getText().toString().equals("")) {
                    String path = content_filepath.getText().toString() + File.separator + editText.getText().toString();
                    File file = new File(path);
                    file.mkdirs();

                    File[] files = file.listFiles();
                    fileAdapter = new FileAdapter(MainActivity.this, files);
                    fileList.setAdapter(fileAdapter);
                    content_filepath.setText(path);
                }
            }
        });
        builder.setNegativeButton("取消", null);
        builder.show();
    }
}
