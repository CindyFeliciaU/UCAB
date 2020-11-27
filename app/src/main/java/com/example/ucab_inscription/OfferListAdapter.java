package com.example.ucab_inscription;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;


// FirebaseRecyclerAdapter is a class provided by
// FirebaseUI. it provides functions to bind, adapt and show
// database contents in a Recycler View
public class OfferListAdapter extends AppCompatActivity {
    private RecyclerView mFirestorelist;
    private FirebaseFirestore firebaseFirestore;
    private FirestoreRecyclerAdapter adapter;

    List<String> offersList;
    List<String> offersListAll;

    public OfferListAdapter(List<String> offersList){
        this.offersList=offersList;
        this.offersListAll=new ArrayList<>(offersList);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_offers);

        final ListView listView = (ListView) findViewById(R.id.listView);

        firebaseFirestore=FirebaseFirestore.getInstance();
        mFirestorelist = findViewById(R.id.firestore_list);

        //Query
        Query query = FirebaseFirestore.getInstance().collection("offer_trip").orderBy("date");
        //RecyclerOptions
        FirestoreRecyclerOptions<OfferModel> response = new FirestoreRecyclerOptions.Builder<OfferModel>()
                .setQuery(query, OfferModel.class).setLifecycleOwner(this)
                .build();
         adapter = new FirestoreRecyclerAdapter<OfferModel, OfferHolder>(response) {

            @NonNull
            @Override
            public OfferHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_single, parent,false);
                return new OfferHolder(view);
            }

            @Override
            protected void onBindViewHolder(OfferHolder offerHolder, int i, @NonNull OfferModel offerModel) {


                offerHolder.list_destination.setText(offerModel.getDestination());
                offerHolder.list_hour.setText(offerModel.getTime());
                offerHolder.list_departure.setText(offerModel.getDeparture());
                offerHolder.list_seats.setText(offerModel.getSeats());
                offerHolder.list_date.setText(offerModel.getDate());
                offerHolder.list_price.setText(offerModel.getPrice());

            }
        };

        mFirestorelist.setHasFixedSize(true);
        mFirestorelist.setLayoutManager(new LinearLayoutManager(this));
        mFirestorelist.setAdapter(adapter);
    }





    private class OfferHolder extends RecyclerView.ViewHolder {
        private  TextView list_seats;
        private  TextView list_hour;
        private  TextView list_date;
        private  TextView list_price;
        private  TextView list_destination;
        private  TextView list_departure;

        public OfferHolder(@NonNull View itemView) {
            super(itemView);
            list_departure=itemView.findViewById(R.id.list_departure);
            list_destination=itemView.findViewById(R.id.list_destination);
            list_hour= itemView.findViewById(R.id.list_hour);
            list_seats=itemView.findViewById(R.id.list_seats);
            list_date= itemView.findViewById(R.id.list_date);
            list_price=itemView.findViewById(R.id.list_price);
        }
    }


    @Override
    protected void onStart(){
        super.onStart();
        adapter.startListening();
    }
    @Override
    protected void onStop(){
        super.onStop();
        adapter.stopListening();
    }
}

