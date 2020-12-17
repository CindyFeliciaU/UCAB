package com.example.ucab;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class HistoryOffers extends AppCompatActivity {
    private RecyclerView mFirestorelist;
    private FirebaseFirestore firebaseFirestore;
    private FirestoreRecyclerAdapter adapter;
    FirebaseAuth fAuth;
    String userId;
    private AdapterView.OnItemClickListener listener;
    Intent intent;
    FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_offers);

        intent = getIntent();
        firebaseFirestore=FirebaseFirestore.getInstance();
        mFirestorelist = findViewById(R.id.firestore_list);

        fAuth = FirebaseAuth.getInstance();
        userId=fAuth.getCurrentUser().getUid();

        //Query

        Query query = FirebaseFirestore.getInstance().collection("offer_trip").whereEqualTo("userId", userId);

        //RecyclerOptions
        FirestoreRecyclerOptions<OfferModel> response = new FirestoreRecyclerOptions.Builder<OfferModel>()
                .setQuery(query, OfferModel.class).setLifecycleOwner(this)
                .build();
        adapter = new FirestoreRecyclerAdapter<OfferModel, HistoryOffers.OfferHolder>(response) {

            @NonNull
            @Override
            public HistoryOffers.OfferHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_single, parent,false);
                return new HistoryOffers.OfferHolder(view);
            }

            @Override
            protected void onBindViewHolder(final HistoryOffers.OfferHolder offerHolder, final int i, @NonNull final OfferModel offerModel) {
                offerHolder.list_destination.setText(offerModel.getDestination());
                offerHolder.list_hour.setText(offerModel.getTime());
                offerHolder.list_departure.setText(offerModel.getDeparture());
                offerHolder.list_seats.setText(offerModel.getSeats());
                offerHolder.list_date.setText(offerModel.getDate());
                offerHolder.list_price.setText(offerModel.getPrice());

                offerHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //get the position
                        final DocumentSnapshot snapshot = getSnapshots().getSnapshot(offerHolder.getAdapterPosition());

                        final String ItemId = snapshot.getId();

                        Intent intent = new Intent(getApplicationContext(), MyOffer_details.class);
                        intent.putExtra("ItemId",""+ItemId);
                        startActivity(intent);

                    }
                });

            }
        };

        mFirestorelist.setHasFixedSize(true);
        mFirestorelist.setLayoutManager(new LinearLayoutManager(this));
        mFirestorelist.setAdapter(adapter);
    }





    private class OfferHolder extends RecyclerView.ViewHolder {
        private TextView list_seats;
        private  TextView list_hour;
        private  TextView list_date;
        private  TextView list_price;
        private  TextView list_destination;
        private  TextView list_departure;
        private  TextView driverInfo;

        public OfferHolder(@NonNull View itemView) {
            super(itemView);
            list_departure=itemView.findViewById(R.id.list_departure);
            list_destination=itemView.findViewById(R.id.list_destination);
            list_hour= itemView.findViewById(R.id.list_hour);
            list_seats=itemView.findViewById(R.id.list_seats);
            list_date= itemView.findViewById(R.id.list_date);
            list_price=itemView.findViewById(R.id.list_price);
            driverInfo=itemView.findViewById(R.id.driverInfo);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item1:
                Intent intent = new Intent(getApplicationContext(), ProfilActivity.class);
                startActivity(intent);
                finish();
                return true;

            case R.id.item2:
                Intent intent2 = new Intent(getApplicationContext(), OfferListAdapter.class);
                startActivity(intent2);
                finish();
                return true;

            case R.id.item3:
                Intent intent3 = new Intent(getApplicationContext(), TripOffer.class);
                startActivity(intent3);
                finish();
                return true;
            case R.id.item4:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                return true;

            case R.id.item5:
                Intent intent4 = new Intent(getApplicationContext(), HistoryOffers.class);
                startActivity(intent4);
                finish();
                return true;


            case R.id.item6:
                Intent intent6 = new Intent(getApplicationContext(), CustomerReceipts.class);
                startActivity(intent6);
                finish();
                return true;

            case R.id.item7:
                Intent intent7 = new Intent(getApplicationContext(), DriverReceipts.class);
                startActivity(intent7);
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
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