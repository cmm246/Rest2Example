package com.example.chung.rest2example;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;

// Using https://github.com/luizgrp/SectionedRecyclerViewAdapter
// to split a Recyclerview into sections with headers.
import io.github.luizgrp.sectionedrecyclerviewadapter.StatelessSection;

/**
 * Created by chung on 8/1/16.
 */
public class PlaylistSection extends StatelessSection {

    private Context mContext;
    private String title;
    private List<Track> list;

    public PlaylistSection(Context context, String title, List<Track> list) {
        // call constructor with layout resources for this Section header, footer and items
        super(R.layout.section_header, R.layout.track_list_row);

        this.mContext = context;
        this.title = title;
        this.list = list;
    }

    @Override
    public int getContentItemsTotal() {
        return (list == null) ? 0 : list.size(); // number of items of this section
    }

    @Override
    public RecyclerView.ViewHolder getItemViewHolder(View view) {
        // return a custom instance of ViewHolder for the items of this section
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position) {
        ItemViewHolder itemHolder = (ItemViewHolder) holder;
        Track track = list.get(position);

        // bind your view here
        itemHolder.titleTextView.setText(track.getTitle());
        if (track.getArtworkURL() == null) {
            Picasso.with(mContext).load(R.drawable.album_placeholder).into(itemHolder.imageView);
        }
        else {
            // Trigger the download of the URL asynchronously into the image view.
            Picasso.with(mContext).load(track.getArtworkURL()).placeholder(R.color.colorAccent).into(itemHolder.imageView);
        }
    }

    @Override
    public RecyclerView.ViewHolder getHeaderViewHolder(View view) {
        return new HeaderViewHolder(view);
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder) {
        HeaderViewHolder headerHolder = (HeaderViewHolder) holder;

        // bind your header view here
        headerHolder.sectionTitle.setText(title);
    }

    class HeaderViewHolder extends RecyclerView.ViewHolder {

        private final TextView sectionTitle;

        public HeaderViewHolder(View view) {
            super(view);

            sectionTitle = (TextView) view.findViewById(R.id.section_text);
        }
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        private final View rootView;
        private final ImageView imageView;
        private final TextView titleTextView;

        public ItemViewHolder(View view) {
            super(view);

            rootView = view;
            imageView = (ImageView) itemView.findViewById(R.id.track_image);
            titleTextView = (TextView) itemView.findViewById(R.id.track_title);
        }
    }
}
