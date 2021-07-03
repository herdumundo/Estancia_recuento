package com.example.estancia;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class color_lista extends BaseAdapter{
    Context ctx;
    LayoutInflater lInflater;
    List<String> data;

    color_lista(Context context, List<String> data) {
        ctx = context;
        this.data = data;
        lInflater = (LayoutInflater) ctx
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = lInflater.inflate(R.layout.informe_recuento, parent, false);
        }

        if (position % 2 == 0) {
            view.setBackgroundResource(R.drawable.artists_list_backgroundcolor);
        } else {
            view.setBackgroundResource(R.drawable.artists_list_background_alternate);
        }



        return view;
    }
}