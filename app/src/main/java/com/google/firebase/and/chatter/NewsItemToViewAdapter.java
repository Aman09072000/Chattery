package com.google.firebase.and.chatter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class NewsItemToViewAdapter extends ArrayAdapter<Article> {

    //Constructor
    public NewsItemToViewAdapter(Activity context, ArrayList<Article> news) {
        super(context, 0, news);
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        View listItems = view;
        if (listItems == null) {
            listItems = LayoutInflater.from(getContext()).inflate(R.layout.list_item,
                    parent, false);
        }
        Article currentArticle = getItem(position);
        TextView section = (TextView) listItems.findViewById(R.id.section);
        String sectionOfArticle = currentArticle.getSection();
        section.setText(sectionOfArticle);

        TextView title = (TextView) listItems.findViewById(R.id.title);
        String titleOfArticle = currentArticle.getTitle();
        title.setText(titleOfArticle);

        TextView date = (TextView) listItems.findViewById(R.id.date);
        String dateOfArticle = currentArticle.getDate();
        date.setText(dateOfArticle);

        TextView author = (TextView) listItems.findViewById(R.id.author);
        String authorOfArticle = currentArticle.getAuthor();
        author.setText(authorOfArticle);

        return listItems;
    }
}