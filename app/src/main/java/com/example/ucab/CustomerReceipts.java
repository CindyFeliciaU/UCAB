package com.example.ucab;

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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;


// FirebaseRecyclerAdapter is a class provided by
// FirebaseUI. it provides functions to bind, adapt and show
// database contents in a Recycler View
public class CustomerReceipts extends AppCompatActivity {
    private RecyclerView mFirestorelist;
    private FirebaseFirestore firebaseFirestore;
    private FirestoreRecyclerAdapter adapter;
    private AdapterView.OnItemClickListener listener;
    Intent intent;
    String tripId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_receipts);

        intent = getIntent();
        firebaseFirestore=FirebaseFirestore.getInstance();
        mFirestorelist = findViewById(R.id.firestore_list);

        //Query
        Query query = FirebaseFirestore.getInstance().collection("trip_payment").whereEqualTo("customerId", FirebaseAuth.getInstance().getCurrentUser().getUid());


        //RecyclerOptions
        FirestoreRecyclerOptions<ReceiptModel> response = new FirestoreRecyclerOptions.Builder<ReceiptModel>()
                .setQuery(query, ReceiptModel.class).setLifecycleOwner(this)
                .build();
        adapter = new FirestoreRecyclerAdapter<ReceiptModel, ReceiptHolder>(response) {

            @NonNull
            @Override
            public ReceiptHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_single_receipt, parent,false);
                return new ReceiptHolder(view);
            }

            @Override
            protected void onBindViewHolder(final ReceiptHolder receiptHolder, final int i, @NonNull final ReceiptModel receiptModel) {

                receiptHolder.list_destination.setText(receiptModel.getDestination());
                receiptHolder.list_time.setText(receiptModel.getTime());
                receiptHolder.list_departure.setText(receiptModel.getDeparture());
                if(!receiptModel.getCustomerName().isEmpty())
                receiptHolder.list_customerName.setText(receiptModel.getCustomerName());
                receiptHolder.list_date.setText(receiptModel.getDate());
                receiptHolder.list_price.setText(receiptModel.getPrice());
                if(!receiptModel.getDriverName().isEmpty())
                receiptHolder.list_driverName.setText(receiptModel.getDriverName());
                tripId = receiptModel.getTripId();
                receiptHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //get the position
                        final DocumentSnapshot snapshot = getSnapshots().getSnapshot(receiptHolder.getAdapterPosition());

                        final String idOffer = snapshot.getString("TripId");

                        Intent intent = new Intent(getApplicationContext(), Offer_details.class);
                        intent.putExtra("TripId",""+idOffer);

                        startActivity(intent);

                    }
                });

            }
        };

        mFirestorelist.setHasFixedSize(true);
        mFirestorelist.setLayoutManager(new LinearLayoutManager(this));
        mFirestorelist.setAdapter(adapter);
    }





    private class ReceiptHolder extends RecyclerView.ViewHolder {

        private  TextView list_destination;
        private  TextView list_departure;
        private  TextView list_date;
        private  TextView list_customerName;
        private  TextView list_time;
        private  TextView list_price;
        private  TextView list_driverName;

        public ReceiptHolder(@NonNull View itemView) {
            super(itemView);
            list_departure=itemView.findViewById(R.id.list_departure);
            list_destination=itemView.findViewById(R.id.list_destination);
            list_customerName= itemView.findViewById(R.id.list_customerId);
            list_driverName=itemView.findViewById(R.id.list_driverId);
            list_date= itemView.findViewById(R.id.list_date);
            list_time=itemView.findViewById(R.id.list_hour);
            list_price=itemView.findViewById(R.id.list_price);

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

