package com.eaccid.bookreader.db.entity;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

@DatabaseTable(tableName = "words")
public class Word implements Serializable {

    @DatabaseField(columnName = "id", canBeNull = false, id = true)
    private String word;

    @DatabaseField
    private String translation;

    @DatabaseField
    private String context;

    @DatabaseField
    private int page;

    @DatabaseField(foreign = true, foreignColumnName = "id")
    private  Book book;

    @DatabaseField
    private boolean enabledOnline;

    public Word() {

    }

    public Word(String word, String translation, String context, int page, Book book, boolean enabledOnline) {
        this.word = word;
        this.translation = translation;
        this.context = context;
        this.page = page;
        this.book = book;
        this.enabledOnline = enabledOnline;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getTranslation() {
        return translation;
    }

    public void setTranslation(String translation) {
        this.translation = translation;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public boolean isEnabledOnline() {
        return enabledOnline;
    }

    public void setEnabledOnline(boolean enabledOnline) {
        this.enabledOnline = enabledOnline;
    }

    @Override
    public String toString() {
        return "Word{" +
                ", word='" + word + '\'' +
                ", translation='" + translation + '\'' +
                ", context='" + context + '\'' +
                ", page=" + page +
                ", book=" + book.getName() +
                ", enabledOnline=" + enabledOnline +
                '}';
    }

}
