package arko.mycontacts;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class DetailContact extends AppCompatActivity {

    private DatabaseReference DBKoneksi;
    private StorageReference STKoneksi;
    private String contactId, fotoUrl;

    ImageView FotoContact;
    EditText TextNama, TextTelp, TextEmail, TextAlamat;
    Button TombolUpdate, TombolHapus;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_contact);

        FotoContact = findViewById(R.id.imageViewContact);
        TextNama = findViewById(R.id.editTextNama);
        TextTelp = findViewById(R.id.editTextTelp);
        TextAlamat = findViewById(R.id.editTextAlamat);
        TextEmail = findViewById(R.id.editTextEmail);

        TombolUpdate = findViewById(R.id.buttonSimpan);
        TombolHapus = findViewById(R.id.buttonHapus);

        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);

        contactId = getIntent().getExtras().getString("id");
        DBKoneksi = FirebaseDatabase.getInstance().getReference("Contact");

        readData();

        TombolUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateData();
            }
        });
        TombolHapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hapusData();
                hapusFoto();
            }
        });
        }

    private void hapusFoto() {
        STKoneksi = FirebaseStorage.getInstance().getReferenceFromUrl(fotoUrl);
        STKoneksi.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
            }
        });
    }

    private void hapusData() {
        Query hapus = DBKoneksi.orderByKey().equalTo(contactId);

        hapus.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                DBKoneksi.child(contactId).removeValue();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        finish();
    }

    private void updateData() {

        Query update = DBKoneksi.orderByKey().equalTo(contactId);

        update.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    child.getRef().child("nama").setValue(TextNama.getText().toString());
                    child.getRef().child("telp").setValue(TextTelp.getText().toString());
                    child.getRef().child("alamat").setValue(TextAlamat.getText().toString());
                    child.getRef().child("email").setValue(TextEmail.getText().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        finish();
    }

    private void readData() {
        progressBar.setVisibility(View.VISIBLE);

        Query findQuery = DBKoneksi.orderByKey().equalTo(contactId);

        findQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot getSnapshot : dataSnapshot.getChildren()) {

                    TextNama.setText(getSnapshot.child("nama").getValue().toString());
                    TextTelp.setText(getSnapshot.child("telp").getValue().toString());
                    TextAlamat.setText(getSnapshot.child("alamat").getValue().toString());
                    TextEmail.setText(getSnapshot.child("email").getValue().toString());

                    fotoUrl = getSnapshot.child("foto").getValue().toString();

                    Picasso.get().load(fotoUrl).fit().into(FotoContact);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressBar.setVisibility(View.INVISIBLE);
            }
        });
        progressBar.setVisibility(View.INVISIBLE);
    }
}
