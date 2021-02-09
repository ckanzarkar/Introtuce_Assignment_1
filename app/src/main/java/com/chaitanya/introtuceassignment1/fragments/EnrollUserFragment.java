package com.chaitanya.introtuceassignment1.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.chaitanya.introtuceassignment1.R;
import com.chaitanya.introtuceassignment1.models.Users;
import com.chaitanya.introtuceassignment1.utils.CheckNetwork;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.ServerTimestamp;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EnrollUserFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EnrollUserFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    //variables
    ImageView ivProfile;
    TextView tvSelectProfile;
    Button btnAddUser;

    EditText etName, etLastName, etDob, etGender, etCountry, etState, etTown, etPhNo, etTeleNo;

    String name, lastName, dob, gender, country, state, town, phNo, teleNO, uuid;

    Uri imageUri = null;

    FirebaseStorage firebaseStorage;
    StorageReference storageReference;
    ProgressDialog dialog;
    FirebaseFirestore db;
    private List<Users> usersList = new ArrayList<>();
    private boolean uniqueId = true;
    ScrollView scrollView;
    private Timestamp ServerTimestamp;

    public EnrollUserFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EnrollUserFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EnrollUserFragment newInstance(String param1, String param2) {
        EnrollUserFragment fragment = new EnrollUserFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_enroll_user, container, false);

        dialog = new ProgressDialog(getContext());


        //Set Id's To Varabels
        etName = view.findViewById(R.id.et_first_name);
        etLastName = view.findViewById(R.id.et_last_name);
        etDob = view.findViewById(R.id.et_dob);
        etGender = view.findViewById(R.id.et_gender);
        etCountry = view.findViewById(R.id.et_country);
        etState = view.findViewById(R.id.et_state);
        etTown = view.findViewById(R.id.et_home_town);
        etPhNo = view.findViewById(R.id.et_ph_no);
        etTeleNo = view.findViewById(R.id.et_telephone_num);

        btnAddUser = view.findViewById(R.id.btn_add_user);
        tvSelectProfile = view.findViewById(R.id.tv_select_image);
        ivProfile = view.findViewById(R.id.iv_profile_selected);
        scrollView = view.findViewById(R.id.sc_v_enroll);
        //till

        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
        db = FirebaseFirestore.getInstance();

        btnAddUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = etName.getText().toString();
                lastName = etLastName.getText().toString();
                dob = etDob.getText().toString();
                gender = etGender.getText().toString();
                country = etCountry.getText().toString();
                state = etState.getText().toString();
                town = etTown.getText().toString();
                phNo = etPhNo.getText().toString();
                teleNO = etTeleNo.getText().toString();

                if (imageUri == null) {
                    Toast.makeText(getContext(), "Select Profile First", Toast.LENGTH_SHORT).show();
                    scrollView.fullScroll(View.FOCUS_UP);
                } else {

                    if (name.isEmpty() || lastName.isEmpty() || dob.isEmpty()
                            || gender.isEmpty() || country.isEmpty() || state.isEmpty()
                            || town.isEmpty() || phNo.isEmpty() || teleNO.isEmpty() || phNo.length() != 10) {

                        if (phNo.length() != 10)
                            Toast.makeText(getContext(), "Invalid Phone Number", Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(getContext(), " Invalid Fields ", Toast.LENGTH_SHORT).show();

                    } else {
                        if (new CheckNetwork().checkNetworkConnection(getContext()) && dob.matches("\\d{2}/\\d{2}/\\d{4}")) {
                            for (Users users : usersList) {
                                if (users.getPhNo().equalsIgnoreCase(phNo)) {
                                    uniqueId = false;
                                    Toast.makeText(getContext(), " Phone Already Exists", Toast.LENGTH_SHORT).show();
                                    break;
                                } else {
                                    uniqueId = true;
                                }
                            }
                            if (uniqueId)
                                uploadImage();
                        } else {
                            if (dob.matches("\\d{2}/\\d{2}/\\d{4}"))
                                Toast.makeText(getContext(), "Check Internet Connection", Toast.LENGTH_SHORT).show();
                            else
                                Toast.makeText(getContext(), "Invalid Date Of Birth Format", Toast.LENGTH_SHORT).show();
                        }

                    }
                }

            }

        });

        tvSelectProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto, 1);
            }
        });

        ivProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto, 1);
            }
        });

        return view;
    }

    private void uploadImage() {
        dialog.setMessage("Adding New User....");
        dialog.show();
        uuid = UUID.randomUUID().toString();
        StorageReference ref = storageReference.child("profile_images/" + uuid);

        ref.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Image uploaded successfully
                        // Dismiss dialog
                        addUserData();
                        // progressDialog.dismiss();
//                                Toast
//                                        .makeText(getContext(),
//                                                "Image Uploaded!!",
//                                                Toast.LENGTH_SHORT)
//                                        .show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        // Error, Image not uploaded
                        dialog.dismiss();
                        Toast
                                .makeText(getContext(),
                                        "Failed " + e.getMessage(),
                                        Toast.LENGTH_SHORT)
                                .show();
                    }
                })
                .addOnProgressListener(
                        new OnProgressListener<UploadTask.TaskSnapshot>() {

                            // Progress Listener for loading
                            // percentage on the dialog box
                            @Override
                            public void onProgress(
                                    UploadTask.TaskSnapshot taskSnapshot) {
                                double progress
                                        = (100.0
                                        * taskSnapshot.getBytesTransferred()
                                        / taskSnapshot.getTotalByteCount());
                                dialog.setMessage(
                                        "Adding  "
                                                + (int) progress + "%...");
                            }
                        });
    }


    private void addUserData(){

        Map<String, Object> user = new HashMap<>();
        user.put("first_name", name);
        user.put("last_name", lastName);
        user.put("dob", dob);
        user.put("gender", gender);
        user.put("country", country);
        user.put("state", state);
        user.put("town", town);
        user.put("ph_no", phNo);
        user.put("tele_no", teleNO);
        user.put("profile", uuid);
        user.put("timestamp", Timestamp.now());

        db.collection("UserData")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(getContext(), "New User Added Successfully", Toast.LENGTH_SHORT).show();
                        dialog.setMessage("User Added Successfully");
                        resetFields();
                        Log.d("TAG", "DocumentSnapshot added with ID: " + documentReference.getId());
                        dialog.dismiss();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("TAG", "Error adding User", e);
                        Toast.makeText(getContext(), "Failed ..." + e.toString(), Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });

    }

    private void resetFields(){
        ivProfile.setImageDrawable(getResources().getDrawable(R.drawable.profile_pic));
        imageUri = null;
        etName.setText("");
        etLastName.setText("");
        etDob.setText("");
        etGender.setText("");
        etCountry.setText("");
        etTown.setText("");
        etState.setText("");
        etPhNo.setText("");
        etTeleNo.setText("");
    }

    private void getUsersList() {
        db.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if (task.isSuccessful()) {
                            // Toast.makeText(getContext(), "Successful", Toast.LENGTH_SHORT).show();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                addUserInList(document);
                                Log.d("DOCS>>", document.getId() + " => " + document.getData());
                            }


                        } else {
                            Log.w("TAG", "Error getting documents.", task.getException());
                        }
                    }
                });
    }

    private void addUserInList(QueryDocumentSnapshot document) {
        Users users = new Users();
        users.setPhNo(document.getData().get("ph_no").toString());
        usersList.add(users);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK){
            imageUri = data.getData();
            ivProfile.setImageURI(imageUri);
        }
    }
}