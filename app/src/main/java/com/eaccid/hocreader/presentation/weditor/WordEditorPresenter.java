package com.eaccid.hocreader.presentation.weditor;

import android.util.Log;
import android.util.SparseBooleanArray;

import com.eaccid.hocreader.injection.App;
import com.eaccid.hocreader.provider.db.words.WordItemImpl;
import com.eaccid.hocreader.provider.db.words.WordListInteractor;
import com.eaccid.hocreader.presentation.BasePresenter;

import javax.inject.Inject;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class WordEditorPresenter implements BasePresenter<WordsEditorFragment> {
    private final String LOG_TAG = "PagerPresenter";
    private WordsEditorFragment mView;

    @Inject
    WordListInteractor wordListInteractor;

    @Override
    public void attachView(WordsEditorFragment wordsEditorFragment) {
        mView = wordsEditorFragment;
        App.getWordListComponent().inject(this);
        Log.i(LOG_TAG, "WordsEditorFragment has been attached.");
    }

    @Override
    public void detachView() {
        mView = null;
        Log.i(LOG_TAG, "WordsEditorFragment has been detached.");
    }

    public void onRefreshRecyclerView() {
        updateDataList();
    }

    public void onViewCreated() {
        updateDataList();
    }

    private void updateDataList() {
        final boolean isFilteredByBook = mView.getArguments().getBoolean("filter_by_book", false);
        wordListInteractor.updateSessionDataList(isFilteredByBook);
        mView.notifyDataSetChanged();
    }

    public void onItemRemoved(int position) {
        mView.showRemovedSnackBar(position);
    }

    public void onUndoClick() {
        int position = wordListInteractor.undoLastRemoval();
        if (position >= 0) {
            mView.notifyItemInserted(position);
        }
    }

    public void onItemClicked(int position) {
        WordItemImpl data = (WordItemImpl) wordListInteractor.getItem(position);
        if (data.isPinned()) {
            data.setPinned(false);
            mView.notifyItemChanged(position);
        }
    }

    public void removeItems(SparseBooleanArray selected) {
        wordListInteractor
                .removeItems(selected)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(succeed -> {
                    if (succeed)
                        mView.notifyDataSetChanged();
                });
    }

    public void onDeleteAllWords() {
        final boolean isFilteredByBook = mView.getArguments().getBoolean("filter_by_book", false);
        wordListInteractor
                .removeAllItems(isFilteredByBook)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(succeed -> {
                    if (succeed)
                        mView.notifyDataSetChanged();
                });
    }

    public void copyItems(SparseBooleanArray selectedIds) {
        mView.showToast("Under development: COPY_ITEMS");
    }

    public void setToLearnItems(SparseBooleanArray toLearnSelectedItems) {
        mView.showToast("Under development: SET_TO_LEARN");
    }

    public void onStartActionMode() {
        updateDataList();
    }
}

