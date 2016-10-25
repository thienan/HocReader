package com.eaccid.bookreader.recycleviewadapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.eaccid.bookreader.R;
import com.eaccid.bookreader.db.entity.Word;

public class DrawerRecyclerViewAdapter extends OrmliteCursorRecyclerViewAdapter<Word, DrawerRecyclerViewAdapter.ViewHolder> {

    public DrawerRecyclerViewAdapter(Context context) {
        super(context);
    }

    @Override
    public DrawerRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.word_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, Word word) {
        holder.drawerTextView.setText(word.getWord());
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView drawerTextView;

        public ViewHolder(View drawerView) {
            super(drawerView);
            drawerTextView = (TextView) drawerView.findViewById(R.id.word);
        }
    }
}
