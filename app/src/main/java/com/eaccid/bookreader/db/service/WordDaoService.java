package com.eaccid.bookreader.db.service;

import android.database.Cursor;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;

import com.eaccid.bookreader.db.entity.Word;
import com.j256.ormlite.android.AndroidDatabaseResults;
import com.j256.ormlite.dao.CloseableIterator;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class WordDaoService implements Crud {

    private Dao<Word, String> dao;

    WordDaoService(DatabaseHelper dbHelper) throws SQLException {
        dao = DaoManager.createDao(dbHelper.getConnectionSource(), Word.class);
    }

    @Override
    public boolean createOrUpdate(Object word) {
        boolean created = false;
        try {
            Word wordToWright = (Word) word;
            Word existedWord = getWord((Word) word);
            if (existedWord != null) {
                wordToWright.setId(existedWord.getId());
            }
            Dao.CreateOrUpdateStatus status = dao.createOrUpdate(wordToWright);
            created = status.isCreated() || status.isUpdated();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return created;
    }

    @Override
    public boolean delete(Object word) {
        try {

            Word existedWord = getWord((Word) word);

            if (existedWord != null) {
                return dao.delete(existedWord) == 1;
            }

            return false;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public Object getById(String id) {
        try {
            return dao.queryForId(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Word> getAll() {

        List<Word> words = new ArrayList<>();
        try {
            words = dao.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return words;
    }

    //Work with DataProvider todo refactor queries

    public List<Word> getAllByWordNameCollection(Iterable<String> word) {
        try {

            QueryBuilder<Word, String> qb = dao.queryBuilder();
            qb.where().in("word", word);
            PreparedQuery<Word> preparedQuery = qb.prepare();

            return dao.query(preparedQuery);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public List<Word> getAllByWordNameCollectionAndBookId(Iterable<String> word, boolean excludeWords, String bookid) {
        try {

            QueryBuilder<Word, String> qb = dao.queryBuilder();
            Where<Word, String> where = qb.where();

            where.eq("book_id", bookid);
            where.and();
            if (excludeWords) {
                where.notIn("word", word);
            } else {
                where.in("word", word);
            }
            PreparedQuery<Word> preparedQuery = qb.prepare();
            return dao.query(preparedQuery);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public List<Word> getAllByBookId(String bookid) {
        try {
            QueryBuilder<Word, String> qb = dao.queryBuilder();
            Where<Word, String> where = qb.where();

            where.eq("book_id", bookid);

            PreparedQuery<Word> preparedQuery = qb.prepare();
            return dao.query(preparedQuery);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    //delete st

    public Cursor getWordCursor() {

        try {

            QueryBuilder<Word, String> qb = dao.queryBuilder();
            qb.selectColumns("word", "translation");

            CloseableIterator<Word> iterator = dao.iterator(qb.prepare());
            AndroidDatabaseResults results =
                    (AndroidDatabaseResults) iterator.getRawResults();

            iterator.closeQuietly();

            return results.getRawCursor();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public PreparedQuery getWordPreparedQuery() {

        try {

            QueryBuilder<Word, String> qb = dao.queryBuilder();
            qb.selectColumns("word", "translation");
            return qb.prepare();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Dao<Word, String> getWordDao() {
        return dao;
    }

    //delete en

    public List<Word> getAllByBookIdAndPage(String bookid, int pageNumber) {
        try {

            QueryBuilder<Word, String> qb = dao.queryBuilder();
            Where<Word, String> where = qb.where();

            where.eq("book_id", bookid);
            where.and();
            where.eq("page", pageNumber);

            PreparedQuery<Word> preparedQuery = qb.prepare();

            return dao.query(preparedQuery);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    @Nullable
    public Word getWordByBookIdAndPage(String wordBaseName, String bookid, int pageNumber) {
        try {
            QueryBuilder<Word, String> qb = dao.queryBuilder();
            Where<Word, String> where = qb.where();

            where.eq("book_id", bookid);
            where.and();
            where.eq("word", wordBaseName);
            where.and();
            where.eq("page", pageNumber);

            PreparedQuery<Word> preparedQuery = qb.prepare();
            return dao.queryForFirst(preparedQuery);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PROTECTED)
    public List<Word> getByWordName(String word) {
        List<String> words = new ArrayList<>();
        words.add(word);
        return getAllByWordNameCollection(words);
    }

    @Nullable
    private Word getWord(Word word) {
        return getWordByBookIdAndPage(word.getName(), word.getBook().getPath(), word.getPage());
    }


}
