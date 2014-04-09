package com.example;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by yoman on 03.11.2013.
 */

public class DrawerAdapterClass  extends ArrayAdapter<String> {
    Context context;
    private ArrayList<String> TextValue = new ArrayList<String>();
    private ArrayList<Integer> IconsDrawableValues = new ArrayList<Integer>();


    public DrawerAdapterClass(Context context, ArrayList<String> TextValue, ArrayList<Integer> IconsDrawableValues) {
        super(context, R.layout.row_of_drawer, TextValue);
        this.context = context;
        this.TextValue= TextValue;
        this.IconsDrawableValues = IconsDrawableValues;
    }


    @Override
    public View getView(int position, View coverView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.row_of_drawer,
                parent, false);

        ImageView imageView = (ImageView)rowView.findViewById(R.id.imageView);
        imageView.setImageDrawable(getContext().getResources().getDrawable(IconsDrawableValues.get(position)));

        TextView text1 = (TextView)rowView.findViewById(R.id.text_drawer);
        text1.setText(TextValue.get(position));

        return rowView;

    }

}