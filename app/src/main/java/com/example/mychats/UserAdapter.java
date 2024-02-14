package com.example.mychats;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.viewholder> {
    MainActivity mainActivity;
    ArrayList<Users> usersArrayList;
    public UserAdapter(MainActivity mainActivity, ArrayList<Users> usersArrayList) {
        this.mainActivity = mainActivity;
        this.usersArrayList = usersArrayList;
    }

    @NonNull
    @Override
    public UserAdapter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mainActivity).inflate(R.layout.user_item,parent,false);
        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserAdapter.viewholder holder, int position) {
        Users users = usersArrayList.get(position);

        holder.userNameTv.setText(users.userName);
        holder.statusTv.setText(users.status);
        Picasso.get().load(users.profileImage).into(holder.profileImage);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mainActivity, chatWin.class);
                intent.putExtra("namee", users.getUserName());
                intent.putExtra("reciverImg", users.getProfileImage());
                intent.putExtra("uid", users.getUserId());
                mainActivity.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class viewholder extends RecyclerView.ViewHolder {
        CircularImageView profileImage;
        TextView userNameTv, statusTv;
        public viewholder(@NonNull View itemView) {
            super(itemView);
            profileImage = itemView.findViewById(R.id.profileImage);
            userNameTv = itemView.findViewById(R.id.userNameTv);
            statusTv = itemView.findViewById(R.id.statusTv);
        }
    }
}
