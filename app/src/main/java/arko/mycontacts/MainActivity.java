package arko.mycontacts;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;

public class MainActivity extends AppCompatActivity {

    private DatabaseReference DBKoneksi;
    private ListView listViewContact;
    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, TambahContact.class));
            }
        });

        listViewContact = findViewById(R.id.listViewContact);
        progressBar = findViewById(R.id.progressBar2);
        progressBar.setVisibility(View.INVISIBLE);

        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        DBKoneksi = FirebaseDatabase.getInstance().getReference("Contact");

        readData();

        listViewContact.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String idContact = (view.findViewById(R.id.textViewNama)).getTag().toString();

                Intent detailcontact = new Intent(MainActivity.this, DetailContact.class);

                detailcontact.putExtra("id", idContact);

                startActivity(detailcontact);

            }
        });


    }

    private void readData() {
        final ArrayList<String> id = new ArrayList<>();
        final ArrayList<String> nama = new ArrayList<>();
        final ArrayList<String> telp = new ArrayList<>();
        final ArrayList<String> foto = new ArrayList<>();

        progressBar.setVisibility(View.INVISIBLE);

        DBKoneksi.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> snapshotIterable = dataSnapshot.getChildren();
                Iterator<DataSnapshot> iterator = snapshotIterable.iterator();

                while (iterator.hasNext()) {

                    Contact value = iterator.next().getValue(Contact.class);

                    assert value != null;
                    id.add(value.getId());
                    nama.add(value.getNama());
                    telp.add(value.getTelp());
                    foto.add(value.getFoto());

                    ((ListViewAdapter) listViewContact.getAdapter()).notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressBar.setVisibility(View.INVISIBLE);
            }
        });

        listViewContact.setAdapter(new ListViewAdapter(id, nama, telp, foto, this));
        progressBar.setVisibility(View.INVISIBLE);
            }
                @Override
            public boolean onCreateOptionsMenu (Menu menu) {
                getMenuInflater().inflate(R.menu.menu_main, menu);
                return true;
            }
                @Override
            public void onResume() {
                super.onResume();
                readData();
            }
                @Override
            public boolean onOptionsItemSelected(MenuItem item) {
                int id = item.getItemId();
                //noinspection SimplifiableIfStatement
                    if (id == R.id.action_settings) {

                        finish();
                        overridePendingTransition(0,0);
                        startActivity(getIntent());
                        overridePendingTransition(0,0);
                        return  true;
                    }
                    return  super.onOptionsItemSelected(item);
    }
}
