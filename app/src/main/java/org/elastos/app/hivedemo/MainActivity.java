package org.elastos.app.hivedemo;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    private String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE};

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

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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
                    Toast toast = Toast.makeText(this, "获取权限成功", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
            }
        }
    }
}
