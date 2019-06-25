package com.harpreet.mydictonary;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.support.v7.widget.SearchView;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v4.widget.CursorAdapter;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    Toolbar toolBar;
    TextView textView;
    SearchView searchView;
    static  Databasehelper mdatabase;
    static boolean databaseopened = false;
    SimpleCursorAdapter suggestionadapter;

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

            }
    });
        mdatabase = new Databasehelper(this);
        if(mdatabase.checkDatabase())
        {
            openDatabase();
        }
        else
        {
            LoadDatabase ld = new LoadDatabase(this);
            ld.execute();
        }

        final String[] from = new String[] {"en_word"};
        final int[] to  = new int[]{R.id.textView};
        suggestionadapter = new SimpleCursorAdapter(MainActivity.this,R.layout.suggestion_row,null,from,to,0)
        {
            @Override
            public void changeCursor(Cursor cursor) {
                super.changeCursor(cursor);
            }
        };
        searchView.setSuggestionsAdapter(suggestionadapter);
        searchView.setOnSuggestionListener(new SearchView.OnSuggestionListener() {
            @Override
            public boolean onSuggestionSelect(int i) {
                return false;
            }

            @Override
            public boolean onSuggestionClick(int i) {
                CursorAdapter ca = searchView.getSuggestionsAdapter();
                Cursor cursor = ca.getCursor();
                cursor.moveToPosition(i);
                String clicked_word = cursor.getString(cursor.getColumnIndex("en_word"));
                searchView.setQuery(clicked_word,false);
                searchView.clearFocus();
                searchView.setFocusable(false);
                Intent intent = new Intent(MainActivity.this,Word_meaningActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("en_word",clicked_word);
                intent.putExtras(bundle);
                startActivity(intent);
                return true;
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                    String text = searchView.getQuery().toString();
                    Cursor c = mdatabase.getmeaning(text);
                if (c.getCount()==0)
                {
                    searchView.setQuery("",false);
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("Word Not Found");
                    builder.setMessage("Please search again");
                    String positivetext = getString(android.R.string.ok);
                    builder.setPositiveButton(positivetext, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    String negativetext = getString(android.R.string.cancel);
                    builder.setNegativeButton(negativetext, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            searchView.clearFocus();
                        }
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();

                }
                else{
                        searchView.clearFocus();
                        searchView.setFocusable(false);

                        Intent intent = new Intent(MainActivity.this,Word_meaningActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("en_word",text);
                        intent.putExtras(bundle);
                        startActivity(intent);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {

                searchView.setIconifiedByDefault(false);
                Cursor cursorSuggestion = mdatabase.getSuggestions(s);
                suggestionadapter.changeCursor(cursorSuggestion);

                return false;
            }
        });







    }
    protected static void openDatabase()
    {
        try{
            mdatabase.openDatabase();
            databaseopened = true;

        }catch (SQLException e){
            e.printStackTrace();
        }
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
