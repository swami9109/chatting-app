package com.example.mychats;

import static com.example.mychats.chatWin.receiverImage;
import static com.example.mychats.chatWin.senderImg;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class messagesAdapter extends RecyclerView.Adapter {
    Context context;
    ArrayList<msgModelClass> messageAdapterArrayList;
    int ITEM_SEND =1;
    int ITEM_RECEIVE = 2;
    FirebaseUser user;

    public messagesAdapter(Context context, ArrayList<msgModelClass> messageAdapterArrayList) {
        this.context = context;
        this.messageAdapterArrayList = messageAdapterArrayList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == ITEM_SEND){
            View view = LayoutInflater.from(context).inflate(R.layout.sender_layout, parent, false);
            return new senderViewHolder(view);
        }else {
            View view = LayoutInflater.from(context).inflate(R.layout.reciver_layout, parent, false);
            return new reciverViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        msgModelClass messages = messageAdapterArrayList.get(position);
        if (holder.getClass() == senderViewHolder.class){
            senderViewHolder viewHolder = (senderViewHolder) holder;
            viewHolder.msgTxt.setText(messages.getMessage());
            Picasso.get().load(senderImg).into(viewHolder.circularImageView);
        }else {
            reciverViewHolder viewHolder = (reciverViewHolder) holder;
            viewHolder.msgTxt.setText(messages.getMessage());
            Picasso.get().load(receiverImage).into(viewHolder.circularImageView);
        }
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        msgModelClass messages = messageAdapterArrayList.get(position);
        if (FirebaseAuth.getInstance().getCurrentUser().getUid().equals(messages.getSenderId())){
            return ITEM_SEND;
        }else {
            return ITEM_RECEIVE;
        }
    }

    class senderViewHolder extends RecyclerView.ViewHolder {
        CircularImageView circularImageView;
        TextView msgTxt;
        public senderViewHolder(@NonNull View itemView) {
            super(itemView);
            circularImageView = itemView.findViewById(R.id.profileImagee);
            msgTxt = itemView.findViewById(R.id.msgSenderTyp);
        }
    }
    class reciverViewHolder extends RecyclerView.ViewHolder{
        CircularImageView circularImageView;
        TextView msgTxt;
        public reciverViewHolder(@NonNull View itemView) {
            super(itemView);
            circularImageView = itemView.findViewById(R.id.pro);
            msgTxt = itemView.findViewById(R.id.reciverTextSet);
        }
    }
}
