package com.eaccid.hocreader.presentation.memorizing.cardremember;

import com.eaccid.hocreader.presentation.BaseView;
import com.eaccid.hocreader.provider.db.words.WordItem;

public interface CardWordView extends BaseView {
    void setDataToView(WordItem wordItem);
}
