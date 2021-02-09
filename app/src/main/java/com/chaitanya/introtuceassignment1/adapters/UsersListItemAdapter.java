package com.chaitanya.introtuceassignment1.adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.bumptech.glide.Glide;
import com.chaitanya.introtuceassignment1.R;
import com.chaitanya.introtuceassignment1.models.Users;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class UsersListItemAdapter extends RecyclerView.Adapter<UsersListItemAdapter.UsersListItemAdapterViewHolder> {

    List<Users> usersList;
    Context context;

    public UsersListItemAdapter(List<Users> usersList, Context context) {
        this.usersList = usersList;
        this.context = context;
    }

    @NonNull
    @Override
    public UsersListItemAdapter.UsersListItemAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.userslist_item, parent, false);
        return new UsersListItemAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UsersListItemAdapter.UsersListItemAdapterViewHolder holder, int position) {

        CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(context);
        circularProgressDrawable.start();

        StorageReference storageReference = FirebaseStorage.getInstance().getReference()
                .child("profile_images/"
                        + usersList.get(position).getProfileImage());

        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(context).load(uri).placeholder(circularProgressDrawable).into(holder.ivProfile);
            }
        });

        holder.name.setText(usersList.get(position).getName() + " " + usersList.get(position).getLastName());

        String details = usersList.get(position).getGender() + " | ";
        int age = 2020 -
                Integer.parseInt(usersList.get(position).getDob().split("/")[2]);
        details = details + String.valueOf(age) + " | " +
                usersList.get(position).getTown();
        holder.userdata.setText(details);

        holder.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notifyItemRemoved(position);
                deleteUser(usersList.get(position).getId());
                usersList.remove(position);
                notifyDataSetChanged();

            }
        });


    }

    @Override
    public int getItemCount() {
        return usersList.size();
    }

    private void deleteUser(String id) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("UserData").document(id).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(context, "User Deleted", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public class UsersListItemAdapterViewHolder extends RecyclerView.ViewHolder {
        TextView name, userdata;
        ImageView ivDelete, ivProfile;

        public UsersListItemAdapterViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.tv_user_name);
            userdata = itemView.findViewById(R.id.tv_user_details);
            ivDelete = itemView.findViewById(R.id.iv_delete_user);
            ivProfile = itemView.findViewById(R.id.iv_profile);
        }
    }
}
