package com.eaccid.hocreader.presentation.pager;

import com.eaccid.hocreader.presentation.BaseView;
import com.eaccid.hocreader.temp.provider.fromtext.WordFromText;

public interface PagerView extends BaseView {
    void showTranslationDialog(WordFromText wordFromText);
}
