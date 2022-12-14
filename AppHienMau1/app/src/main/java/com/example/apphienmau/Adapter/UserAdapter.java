package com.example.apphienmau.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AlertDialogLayout;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.apphienmau.Email.JavaMailApi;
import com.example.apphienmau.Interface.IclickUser;
import com.example.apphienmau.MainActivity;
import com.example.apphienmau.Model.User;
import com.example.apphienmau.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder>{

    private Context context;
    private List<User> userList;
    private IclickUser iclickUser;

    public UserAdapter(Context context, List<User> userList, IclickUser iclickUser) {
        this.context = context;
        this.userList = userList;
        this.iclickUser = iclickUser;
    }

    public UserAdapter(Context context, List<User> userList) {
        this.context = context;
        this.userList = userList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_displayed_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final User user = userList.get(position);
        holder.type.setText(user.getType());
        //x??t n???u l?? ng?????i cho th?? ???n n??t g???i mail
        if (user.getType().equals("Ng?????i Cho")){
            holder.emailNow.setVisibility(View.VISIBLE);
            holder.callNow.setVisibility(View.VISIBLE);
        }
        holder.userEmail.setText(user.getEmail());
        holder.phoneNumber.setText(user.getPhonenumber());
        holder.userName.setText(user.getName());
        holder.bloodGroup.setText(user.getBloodgroup());

        Glide.with(context).load(user.getProfilepictureurl()).into(holder.userProfileImage);

        final String nameOfTheReceiver = user.getName();
        final String idOfTheReceiver = user.getId();
        final String phoneReceiver = user.getPhonenumber();

        //g???i mail
        holder.emailNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(context)
                        .setTitle("G???i mail")
                        .setMessage("B???n c?? mu???n g???i mail cho "+ user.getName()+ "?")
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                                        .child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                                reference.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        String nameOfSender = snapshot.child("name").getValue().toString();
                                        String email = snapshot.child("email").getValue().toString();
                                        String phone = snapshot.child("phonenumber").getValue().toString();
                                        String blood = snapshot.child("bloodgroup").getValue().toString();

                                        String mEmail = user.getEmail();
                                        String msubject = "Save Lifes App";
                                        String mMessage = "Xin ch??o "+nameOfTheReceiver+", "+nameOfSender+
                                                " Mu???n nh???n m??u t??? b???n. Th??ng tin c???a anh/c?? ???y:\n"
                                                +"T??n: "+nameOfSender+ "\n"+
                                                "S??? ??i???n tho???i: "+phone+ "\n"+
                                                "Email: "+email+ "\n"+
                                                "Nh??m M??u: "+blood+ "\n"+
                                                "Vui l??ng li??n h??? v???i anh/c?? ???y. C???m ??n!\n"
                                                +"Save Lifes App - Gi???t M??u, Kho B??u T??nh Ng?????i";

                                        JavaMailApi javaMailApi = new JavaMailApi(context, mEmail, msubject, mMessage);
                                        javaMailApi.execute();

                                        DatabaseReference Senderref = FirebaseDatabase.getInstance().getReference("emails")
                                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                                        Senderref.child(idOfTheReceiver).setValue(true).addOnCompleteListener(new OnCompleteListener<Void>() {
                                             @Override
                                             public void onComplete(@NonNull Task<Void> task) {
                                                 if (task.isSuccessful()){
                                                     DatabaseReference receiverRef = FirebaseDatabase.getInstance().getReference("emails")
                                                             .child(idOfTheReceiver);
                                                     receiverRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(true);

                                                     addNotification(idOfTheReceiver, FirebaseAuth.getInstance().getCurrentUser().getUid());

                                                 }
                                             }
                                         });
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }
                        })
                        .setNegativeButton("Kh??ng", null)
                        .show();
            }
        });
        holder.callNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               iclickUser.onclickitem(user);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (userList != null){
            return userList.size();
        }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public CircleImageView userProfileImage;
        public TextView type, userName, userEmail, phoneNumber, bloodGroup;
        public AppCompatButton emailNow, callNow;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            userProfileImage = itemView.findViewById(R.id.userProfileImage);
            type = itemView.findViewById(R.id.type);
            userName = itemView.findViewById(R.id.userName);
            userEmail = itemView.findViewById(R.id.userEmail);
            phoneNumber = itemView.findViewById(R.id.userphoneNumber);
            bloodGroup = itemView.findViewById(R.id.bloodGroup);
            emailNow = itemView.findViewById(R.id.emailNow);
            callNow = itemView.findViewById(R.id.callNow);
        }
    }
    private void addNotification(String receiverId, String senderId){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child("notifications").child(receiverId);
        String date = DateFormat.getDateInstance().format(new Date());
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("receiverId", receiverId);
        hashMap.put("senderId", senderId);
        hashMap.put("text", " ???? g???i cho b???n m???t Email, vui l??ng ki???m tra!");
        hashMap.put("date", date);

        reference.push().setValue(hashMap);
    }
}
