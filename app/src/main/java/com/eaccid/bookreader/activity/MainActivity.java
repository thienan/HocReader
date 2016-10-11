package com.eaccid.bookreader.activity;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.provider.SearchRecentSuggestions;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.MenuInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ExpandableListView;

import com.eaccid.bookreader.file.searcher.FileFinder;
import com.eaccid.bookreader.file.searcher.ItemObjectChild;
import com.eaccid.bookreader.file.searcher.ItemObjectGroup;
import com.eaccid.bookreader.R;
import com.eaccid.bookreader.file.searcher.SearchAdapter;
import com.eaccid.bookreader.file.searcher.SearchSuggestionsProvider;

import java.io.File;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener, SearchView.OnCloseListener {


    private SearchAdapter searchAdapter;
    private ExpandableListView expandableListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        fab.setOnClickListener(view -> Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show());

        expandableListView = (ExpandableListView) findViewById(R.id.expandableListView_main);
        fillExpandableListView();

    }

    private void fillExpandableListView() {

        ArrayList<ItemObjectGroup> itemObjectGroupList = new ArrayList<>();

        //TODO create class to handle files into item objects CRUTCH
        ////////////////////////////////////////////////////////////////////////////////////////////
        FileFinder fileFinder = new FileFinder();
        ArrayList<File> foundFiles = fileFinder.findFiles();

        ArrayList<ItemObjectChild> childObjectItemTXT = new ArrayList<>();
        ArrayList<ItemObjectChild> childObjectItemPDF = new ArrayList<>();

        for (File file : foundFiles) {
            String ext = MimeTypeMap.getFileExtensionFromUrl(file.getName());
            if (ext.equalsIgnoreCase("txt")) {
                childObjectItemTXT.add(new ItemObjectChild(R.mipmap.generic_icon, file.getName(), file));
            } else {
                childObjectItemPDF.add(new ItemObjectChild(R.mipmap.generic_icon, file.getName(), file));
            }
        }
        ItemObjectGroup itemObjectGroupTXT = new ItemObjectGroup("TXT", childObjectItemTXT);
        itemObjectGroupList.add(itemObjectGroupTXT);
        ItemObjectGroup itemObjectGroupPDF = new ItemObjectGroup("PDF", childObjectItemPDF);
        itemObjectGroupList.add(itemObjectGroupPDF);
        ////////////////////////////////////////////////////////////////////////////////////////////


        //TODO something with this crashing!
        // The app will crash if display list is not called here???

        searchAdapter = new SearchAdapter(MainActivity.this, itemObjectGroupList);
        expandableListView.setAdapter(searchAdapter);

        expandListViewGroup();

    }

    private void expandListViewGroup() {
        int groupCount = searchAdapter.getGroupCount();
        for (int i = 0; i < groupCount; i++) {
            expandableListView.expandGroup(i);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        // Assumes current activity is the searchable activity
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        // Do not iconify the widget, expand it by default
        searchView.setIconifiedByDefault(false);

        searchView.setOnQueryTextListener(this);
        searchView.setOnCloseListener(this);

        //Call this to try to give focus to a specific view or to one of its descendants
        searchView.requestFocus();

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar will automatically handle clicks on the Home/Up button,
        // so long as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
            case R.id.action_search:
                return true;
            case R.id.action_clearSearchHistory:
                // TODO 1. provide a confirmation dialog to verify that the user wants to delete their search history
                SearchRecentSuggestions suggestions = new SearchRecentSuggestions(this, SearchSuggestionsProvider.AUTHORITY, SearchSuggestionsProvider.MODE);
                suggestions.clearHistory();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onClose() {
        searchAdapter.filterData("");
        expandListViewGroup();
        return false;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {

        SearchRecentSuggestions suggestions = new SearchRecentSuggestions(this,
                SearchSuggestionsProvider.AUTHORITY, SearchSuggestionsProvider.MODE);
        suggestions.saveRecentQuery(query, null);

        searchAdapter.filterData(query);
        expandListViewGroup();

        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        searchAdapter.filterData(newText);
        expandListViewGroup();
        return false;
    }


}

