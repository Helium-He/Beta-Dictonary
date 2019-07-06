package com.harpreet.mydictonary;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.support.v7.widget.SearchView;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v4.widget.CursorAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.net.PasswordAuthentication;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {
    Toolbar toolBar;
    TextView textView;
    SearchView searchView;
    static  Databasehelper mdatabase;
    static boolean databaseopened = false;
    SimpleCursorAdapter suggestionadapter;
    ArrayList<history> historyList;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter historyAdapter;
    RelativeLayout emptyHistory;
    Cursor cursorHistory;

    boolean doublebackexit = false;
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
        final int[] to  = new int[]{R.id.suggestion_text};
        suggestionadapter = new SimpleCursorAdapter(MainActivity.this,
                R.layout.suggestion_row,null,from,to,0)
        {
            @Override
            public void changeCursor(Cursor cursor) {
                super.swapCursor(cursor);
                }
        };
        searchView.setSuggestionsAdapter(suggestionadapter);
        searchView.setOnSuggestionListener(new SearchView.OnSuggestionListener(){
            @Override
            public boolean onSuggestionSelect(int i) {
                return true;
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
                    String text = searchView.getQuery().toString().replaceAll("\\s+","");
                    Pattern P = Pattern.compile("[A-Za-z \\-.]{1,25}");
                    Matcher m = P.matcher(text);
                if(m.matches()) {
                    Cursor c = mdatabase.getmeaning(text);
                    if (c.getCount() == 0) {

                        showDialog();
                    } else {
                        searchView.clearFocus();
                        searchView.setFocusable(false);

                        Intent intent = new Intent(MainActivity.this, Word_meaningActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("en_word", text);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                }

                else{
                    showDialog();
                }

                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                s.replaceAll("\\s+","");
                searchView.setIconifiedByDefault(false);
                Pattern P = Pattern.compile("[A-Za-z \\-.]{1,25}");
                Matcher m = P.matcher(s);
                if (m.matches())
                {
                    Cursor cursorSuggestion = mdatabase.getSuggestions(s);
                    suggestionadapter.changeCursor(cursorSuggestion);
                }


                return false;
            }
        });
        emptyHistory = findViewById(R.id.empty_history);
        recyclerView = findViewById(R.id.recycler_view_history);
        layoutManager = new LinearLayoutManager(MainActivity.this);

        recyclerView.setLayoutManager(layoutManager);
        fetch_history();
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
    private void fetch_history()
    {
        historyList = new ArrayList<>();
        historyAdapter = new RecyclerViewAdapterHistory(this,historyList);
        recyclerView.setAdapter(historyAdapter);
        history h;
        if(databaseopened)
        {
            cursorHistory = mdatabase.getHistory();
            if(cursorHistory.moveToFirst()){
                do{


                    h= new history(cursorHistory.getString(cursorHistory.getColumnIndex("word")),cursorHistory.getString(cursorHistory.getColumnIndex("en_definition")));

                    historyList.add(h);
                }while(cursorHistory.moveToNext());
            }
            historyAdapter.notifyDataSetChanged();
            if (historyAdapter.getItemCount()==0)
            {
                emptyHistory.setVisibility(View.VISIBLE);
            }
            else
            {
                emptyHistory.setVisibility(View.GONE);
            }
        }
    }
    private void showDialog()
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
            final AlertDialog myalert_dialogue = builder.create();
            CardView c = view.findViewById(R.id.clear);
            myalert_dialogue.show();
            c.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showAlertDialog();
                    myalert_dialogue.dismiss();
                }
            });
        }

        if(id == R.id.item_exit)
        {
            System.exit(0);
        }
        return super.onOptionsItemSelected(item);
    }
    private void showAlertDialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Are you sure?");
        builder.setMessage("All the history will be deleted");

        String positiveText = "Yes";
        builder.setPositiveButton(positiveText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mdatabase.deletehistory();
                       recreate();
                    }
                });

        String negativeText = "No";
        builder.setNegativeButton(negativeText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        AlertDialog dialog = builder.create();
        // display dialog
        dialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        fetch_history();
    }

    @Override
    public void onBackPressed() {
        if (doublebackexit) {
            super.onBackPressed();
        }

        this.doublebackexit = true;
        Toast.makeText(this, "Press BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doublebackexit=false;
            }
        }, 2000);

    }
}
