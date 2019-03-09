package com.example.livestreamingvideoproject;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class Recycler_view_for_stream_list extends RecyclerView.Adapter<Recycler_view_for_stream_list.ViewHolder> {

    Context context;

    List<User_subjects> news;

    public Recycler_view_for_stream_list(List<User_subjects> getNews, Context context){

        super();

        this.news = getNews;

        this.context = context;
    }

    @Override
    public Recycler_view_for_stream_list.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.stream_list_items, parent, false);

        Recycler_view_for_stream_list.ViewHolder viewHolder = new Recycler_view_for_stream_list.ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(Recycler_view_for_stream_list.ViewHolder holder, int position) {

        User_subjects streams =  news.get(position);

    //    holder.id.setText(streams.get_id());
        holder.name.setText(streams.getName());
  //      holder.stream_link.setText(streams.getRtmp());
    }

    @Override
    public int getItemCount() {

        return news.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        public TextView name,id,stream_link;


        public ViewHolder(View itemView) {

            super(itemView);

//            id = (TextView)itemView.findViewById(R.id.txt_all_article_auther);
            name = (TextView)itemView.findViewById(R.id.txt_username);
//            stream_link = (TextView) itemView.findViewById(R.id.txt_all_popular_artical_title) ;
            //      content_news = (TextView)itemView.findViewById(R.id.txt_all_artical_contents);
        }
    }
}

