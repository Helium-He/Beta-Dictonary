package com.harpreet.mydictonary;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.support.v7.widget.SearchView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    Toolbar toolBar;
    TextView textView;
    SearchView searchView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolBar = findViewById(R.id.toolBar);
        setSupportActionBar(toolBar);
        textView = findViewById(R.id.textView);
        searchView = findViewById(R.id.search_view);
        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchView.onActionViewExpanded();
                Intent intent = new Intent(MainActivity.this,Word_meaningActivity.class);
                startActivity(intent);
            }
    });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id==R.id.item_setting)
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            LayoutInflater inflater = LayoutInflater.from(this);
            View view = inflater.inflate(R.layout.activity_setting,null);
            builder.setView(view);
            //builder.setTitle("Setting");
            AlertDialog myalert_dialogue = builder.create();
            myalert_dialogue.show();

        }
        if(id == R.id.item_exit)
        {
            System.exit(0);
        }
        return super.onOptionsItemSelected(item);
    }
}
