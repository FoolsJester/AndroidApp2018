package com.example.hp.androidproject;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.hp.androidproject.Objects.ReplyObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


/*
* Adapter for replies which is used to render replies in recycler view
*
*
* */
public class RepliesAdapter extends RecyclerView.Adapter<RepliesAdapter.ViewHolder>{

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView authorTextView;
        public TextView dateTextView;
        public TextView contentTextView;

        public ViewHolder(View itemView){
            super(itemView);

            authorTextView = (TextView) itemView.findViewById(R.id.author_reply);
            dateTextView =(TextView)itemView.findViewById(R.id.time_reply);
            contentTextView =(TextView)itemView.findViewById(R.id.content_reply);
        }
    }

    private List<ReplyObject> mReplies;//list of reply objects, later rendered into adapter

    public RepliesAdapter(List<ReplyObject> replies){
        //casting contents of list into adapter
        mReplies=replies;
    }

    @Override
    public RepliesAdapter.ViewHolder  onCreateViewHolder(ViewGroup parent, int viewType){
        //create viewholder for adapter
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View replyView = inflater.inflate(R.layout.item_reply,parent, false);

        ViewHolder viewHolder = new ViewHolder(replyView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RepliesAdapter.ViewHolder viewHolder, int position){
        ReplyObject replyObject = mReplies.get(position);

        TextView author = viewHolder.authorTextView;
        author.setText(replyObject.getAuthor());

        DateFormat dateFormat = new SimpleDateFormat("dd/MM HH:mm");
        Date date = new Date();
        TextView dateView = viewHolder.dateTextView;
        dateView.setText(dateFormat.format(date));
        TextView content = viewHolder.contentTextView;
        content.setText(replyObject.getReply());

    }

    @Override
    public int getItemCount() {
        return mReplies.size();
    }
}
