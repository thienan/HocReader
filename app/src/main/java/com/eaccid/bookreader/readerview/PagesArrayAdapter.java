package com.eaccid.bookreader.readerview;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import com.eaccid.bookreader.wordview.Word;
import com.eaccid.bookreader.R;
import com.eaccid.bookreader.wordview.WordTranslator;
import com.eaccid.bookreader.wordview.WordOnTexvViewFinder;

import java.util.List;


public class PagesArrayAdapter extends ArrayAdapter{

    private Context mContext;
    private int mTextOnPage;
    private List<String> mPagesList;

    public PagesArrayAdapter(Context context, int textViewResourceId, List<String> pagesList) {
        super(context, textViewResourceId, pagesList);
        this.mContext = context;
        this.mTextOnPage = textViewResourceId;
        this.mPagesList = pagesList;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolderItem viewHolderItem;

        if (convertView == null) {

            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            convertView = inflater.inflate(mTextOnPage, parent, false);


            viewHolderItem = new ViewHolderItem();
            viewHolderItem.textViewItem = (TextView) convertView.findViewById(R.id.text_on_page);

            viewHolderItem.textViewItem.setOnTouchListener(new View.OnTouchListener() {

                @Override
                public boolean onTouch(View v, MotionEvent event) {

                    Word word = WordOnTexvViewFinder.getWordByMotionEvent((TextView) v, event);

                    WordTranslator wordTranslator = new WordTranslator(getContext());
                    wordTranslator.showTranslationView(word);

                    return false;
                }
            });

            convertView.setTag(viewHolderItem);

        } else {
            viewHolderItem = (ViewHolderItem) convertView.getTag();
        }

        String textOnPage = mPagesList.get(position);
        viewHolderItem.textViewItem.setText(textOnPage);

        return convertView;

    }

    private static class ViewHolderItem {
        TextView textViewItem;
    }



}





