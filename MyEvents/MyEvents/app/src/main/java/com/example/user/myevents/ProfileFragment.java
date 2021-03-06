package com.example.user.myevents;


import android.content.Intent;
import android.os.Bundle;


import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class ProfileFragment extends Fragment implements View.OnClickListener {
    FirebaseAuth mAuth = FirebaseAuth.getInstance();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {

        /************** Stocker photo, videos,...************/
        /*FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        // File or Blob
        Uri file  = Uri.fromFile(new File("path/to/mountains.jpg"));

// Create the file metadata
       StorageMetadata metadata = new StorageMetadata.Builder()
                .setContentType("image/jpeg")
                .build();

// Upload file and metadata to the path 'images/mountains.jpg'
       UploadTask uploadTask = storageRef.child("images/"+file.getLastPathSegment()).putFile(file, metadata);

// Listen for state changes, errors, and completion of the upload.
        uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                System.out.println("Upload is " + progress + "% done");
            }
        }).addOnPausedListener(new OnPausedListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onPaused(UploadTask.TaskSnapshot taskSnapshot) {
                System.out.println("Upload is paused");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // Handle successful uploads on complete
                Uri downloadUrl = taskSnapshot.getMetadata().getDownloadUrl();
            }
        });
*/
            /************ Fin Stockage  ********************/

        View view = inflater.inflate(R.layout.profile_view, parent, false);

        Button btn = view.findViewById(R.id.Log_out_button);
        btn.setOnClickListener(this);
        Button btn2 = view.findViewById(R.id.Delete_button);
        btn2.setOnClickListener(this);
        ImageButton btn3 = view.findViewById(R.id.add_friend);
        btn3.setOnClickListener(this);

        final TextView nom = (TextView) view.findViewById(R.id.user_profile_name);
        nom.setText(mAuth.getCurrentUser().getDisplayName());
        final TextView mail = (TextView) view.findViewById(R.id.user_profile_short_bio);
        mail.setText(mAuth.getCurrentUser().getEmail());
        if (!mAuth.getCurrentUser().getProviders().get(0).equals("password")){
            ImageButton profilePicture = (ImageButton) view.findViewById(R.id.user_profile_photo);
            String photoUrl = mAuth.getCurrentUser().getPhotoUrl().toString();
            Picasso.with(getContext()).load(photoUrl).fit().centerCrop().into(profilePicture);
        }
        return view;
    }

    @Override
    public void onClick(View v) {
            switch (v.getId()) {

                case R.id.Log_out_button:
                    mAuth.signOut();
                    Toast.makeText(getActivity(), "Log Out !",
                            Toast.LENGTH_LONG).show();
                    startActivity(new Intent(getContext(),AuthActivity.class));
                    break;

                case R.id.Delete_button:
                    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
                    mDatabase.child("users").child(mAuth.getCurrentUser().getUid()).removeValue();
                    mAuth.getCurrentUser().delete();
                    mAuth.signOut();
                    Toast.makeText(getActivity(), "Account Deleted !",
                            Toast.LENGTH_LONG).show();
                    getActivity().finish();
                    break;

                case R.id.add_friend:
                    Toast.makeText(getActivity(), "Friends !",
                            Toast.LENGTH_LONG).show();
                    startActivity(new Intent(getContext(), SearchFriendsActivity.class));
                    break;

                default:
                    break;
            }
    }
}

