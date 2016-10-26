package com.eaccid.bookreader.activity;

import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.View;

import com.eaccid.bookreader.adapter.PagerAdapter;
import com.eaccid.bookreader.advancedrecyclerview.ExampleDataProvider;
import com.eaccid.bookreader.advancedrecyclerview.ExampleDataProviderFragment;
import com.eaccid.bookreader.advancedrecyclerview.ItemPinnedMessageDialogFragment;
import com.eaccid.bookreader.fragment.WordsEditorFragment;
import com.eaccid.bookreader.dev.AppDatabaseManager;
import com.eaccid.bookreader.file.FileToPagesReader;
import com.eaccid.bookreader.R;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;

public class PagerActivity extends FragmentActivity  implements ItemPinnedMessageDialogFragment.EventListener {

    private static ArrayList<String> pagesList = new ArrayList<>();

    private static final String FRAGMENT_TAG_DATA_PROVIDER = "data provider";
    private static final String FRAGMENT_LIST_VIEW = "list view";
    private static final String FRAGMENT_TAG_ITEM_PINNED_DIALOG = "item pinned dialog";

    private void fillPagesListAndRefreshDatabase() {
        String filePath = getIntent().getStringExtra("filePath");
        String fileName = getIntent().getStringExtra("fileName");
        FileToPagesReader fileToPagesReader = new FileToPagesReader(this, filePath);
        pagesList = fileToPagesReader.getPages();

        AppDatabaseManager.createOrUpdateBook(filePath, fileName, pagesList.size());
        AppDatabaseManager.setCurrentBookForAddingWord(filePath);
    }

    public static ArrayList<String> getPagesList() {
        return pagesList;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pager_fragment_activity);

        fillPagesListAndRefreshDatabase();

        PagerAdapter pagerAdapter = new PagerAdapter(getSupportFragmentManager());
        ViewPager pager = (ViewPager) findViewById(R.id.pager);
        pager.setPageTransformer(true, new ZoomOutPageTransformer());
        pager.setAdapter(pagerAdapter);

        CirclePageIndicator circleIndicator = (CirclePageIndicator) findViewById(R.id.indicator);
        circleIndicator.setViewPager(pager);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(new ExampleDataProviderFragment(), FRAGMENT_TAG_DATA_PROVIDER)
                    .commit();
            getSupportFragmentManager().beginTransaction()
                    .add(new WordsEditorFragment(), FRAGMENT_LIST_VIEW)
                    .commit();
        }

    }

    public void onItemRemoved(int position) {
        Snackbar snackbar = Snackbar.make(
                findViewById(R.id.container),
                R.string.snack_bar_text_item_removed,
                Snackbar.LENGTH_LONG);

        snackbar.setAction(R.string.snack_bar_action_undo, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemUndoActionClicked();
            }
        });
        snackbar.setActionTextColor(ContextCompat.getColor(this, R.color.snackbar_action_color_done));
        snackbar.show();
    }

    public void onItemPinned(int position) {
        final DialogFragment dialog = ItemPinnedMessageDialogFragment.newInstance(position);

        getSupportFragmentManager()
                .beginTransaction()
                .add(dialog, FRAGMENT_TAG_ITEM_PINNED_DIALOG)
                .commit();
    }

    public void onItemClicked(int position) {
        final Fragment fragment = getSupportFragmentManager().findFragmentByTag(FRAGMENT_LIST_VIEW);
        ExampleDataProvider.ConcreteData data = getDataProvider().getItem(position);

        if (data.isPinned()) {
            // unpin if tapped the pinned item
            data.setPinned(false);
            ((WordsEditorFragment) fragment).notifyItemChanged(position);
        }
    }

    private void onItemUndoActionClicked() {
        int position = getDataProvider().undoLastRemoval();
        if (position >= 0) {
            final Fragment fragment = getSupportFragmentManager().findFragmentByTag(FRAGMENT_LIST_VIEW);
            ((WordsEditorFragment) fragment).notifyItemInserted(position);
        }
    }

    @Override
    public void onNotifyItemPinnedDialogDismissed(int itemPosition, boolean ok) {
        final Fragment fragment = getSupportFragmentManager().findFragmentByTag(FRAGMENT_LIST_VIEW);

        getDataProvider().getItem(itemPosition).setPinned(ok);
        ((WordsEditorFragment) fragment).notifyItemChanged(itemPosition);
    }

    public ExampleDataProvider getDataProvider() {
        final Fragment fragment = getSupportFragmentManager().findFragmentByTag(FRAGMENT_TAG_DATA_PROVIDER);
        return ((ExampleDataProviderFragment) fragment).getDataProvider();
    }

}







