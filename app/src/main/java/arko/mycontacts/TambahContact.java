package arko.mycontacts;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class TambahContact extends AppCompatActivity {

    private DatabaseReference DBKoneksi;
    private StorageReference STKoneksi;
    private String contactId;

    ImageView FotoContact;
    EditText TextNama, TextTelp, TextEmail, TextAlamat;
    Button TombolSimpan, TombolBatal;
    ProgressBar progressBar;

    private Uri filePath;
    private String imageName, storageUrl;

    private static final int PICK_IMAGE_REQUEST = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_contact);


        FotoContact = findViewById(R.id.imageViewContact);
        TextNama = findViewById(R.id.editTextNama);
        TextTelp = findViewById(R.id.editTextTelp);
        TextAlamat = findViewById(R.id.editTextAlamat);
        TextEmail = findViewById(R.id.editTextEmail);

        TombolSimpan = findViewById(R.id.buttonSimpan);
        TombolBatal = findViewById(R.id.buttonBatal);

        progressBar = findViewById(R.id.progressBar);

        progressBar.setVisibility(View.INVISIBLE);


        DBKoneksi = FirebaseDatabase.getInstance().getReference("Contact");
        STKoneksi = FirebaseStorage.getInstance().getReference();
        contactId = DBKoneksi.push().getKey();

        TombolSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                imageName = "images/"+TextEmail.getText().toString();

                uploadImage();
            }
        });

        TombolBatal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        
        FotoContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showGallery();
            }
        });
    }

    private void uploadImage() {
        if(filePath != null){
            final StorageReference ref = STKoneksi.child(imageName);
            UploadTask uploadTask = ref.putFile(filePath);
            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()){
                            throw task.getException();
                        }
                    return ref.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if(task.isSuccessful()){
                        Uri downloadUri = task.getResult();
                        storageUrl = downloadUri.toString();

                        SimpanData(contactId,
                                TextNama.getText().toString(),
                                TextTelp.getText().toString(),
                                TextAlamat.getText().toString(),
                                TextEmail.getText().toString(),
                                storageUrl);

                        progressBar.setProgress(0);
                        progressBar.setVisibility(View.INVISIBLE);

                        Toast.makeText(TambahContact.this, "Data Telah Tersimpan", Toast.LENGTH_SHORT).show();

                        finish();
                    }
                }
            });

            uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    progressBar.setVisibility(View.VISIBLE);
                    double progress = (100.0*taskSnapshot.getBytesTransferred()/ taskSnapshot.getTotalByteCount());
                    progressBar.setProgress((int)progress);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(TambahContact.this, "Gagal Upload = "+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void showGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Pilih Foto"),PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST
                && resultCode == RESULT_OK &&
                data != null && data.getData() != null) {
                filePath = data.getData();
                Picasso.get().load(filePath).fit().into(FotoContact);
        }
    }

    private void SimpanData(String id, String nama, String telp, String alamat, String email, String foto) {
        Contact contact = new Contact(id, nama, telp, alamat, email, foto);
        DBKoneksi.child(contactId).setValue(contact);
    }

    @Override
    public void onBackPressed(){
        finish();
    }
}
