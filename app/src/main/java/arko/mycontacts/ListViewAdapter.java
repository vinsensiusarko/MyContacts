package arko.mycontacts;

import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ListViewAdapter extends BaseAdapter {

    private ArrayList<String> id;
    private ArrayList<String> nama;
    private ArrayList<String> telp;
    private ArrayList<String> foto;
    private AppCompatActivity activity;

    public ListViewAdapter(ArrayList<String> id, ArrayList<String> nama, ArrayList<String> telp, ArrayList<String> foto, AppCompatActivity activity) {
        this.id = id;
        this.nama = nama;
        this.telp = telp;
        this.foto = foto;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return id.size();
    }

    @Override
    public Object getItem(int position) {
        return id.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(activity.getApplicationContext()).inflate(R.layout.layout_listview, parent, false);

        (convertView.findViewById(R.id.textViewNama)).setTag(String.valueOf(id.get(position)));

        ((TextView)convertView.findViewById(R.id.textViewNama)).setText(String.valueOf(nama.get(position)));

        ((TextView)convertView.findViewById(R.id.textViewTelp)).setText(String.valueOf(telp.get(position)));

        String fotoUrl = foto.get(position);

        if (fotoUrl !=""){
            Picasso.get()
                    .load(foto.get(position))
                    .into((ImageView)convertView.findViewById(R.id.imageViewContact));
        }else{
            ((ImageView)convertView.findViewById(R.id.imageViewContact)).setImageResource(R.mipmap.ic_launcher_round);
        }

        return convertView;
    }
}
