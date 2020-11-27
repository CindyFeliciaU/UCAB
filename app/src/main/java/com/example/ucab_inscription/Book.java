package com.example.ucab_inscription;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.android.volley.VolleyError;
import com.checkout.android_sdk.PaymentForm;
import com.checkout.android_sdk.Response.CardTokenisationFail;
import com.checkout.android_sdk.Response.CardTokenisationResponse;

public class Book extends AppCompatActivity {
    private PaymentForm mPaymentForm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);
        PaymentForm.PaymentFormCallback mFormListener = new PaymentForm.PaymentFormCallback() {
            @Override
            public void onFormSubmit() {
                // form submit initiated; you can potentially display a loader
            }
            @Override
            public void onTokenGenerated(CardTokenisationResponse response) {
                // your token is here: response.token
                mPaymentForm.clearForm(); // this clears the Payment Form
            }
            @Override
            public void onError(CardTokenisationFail response) {
                // token request error
            }
            @Override
            public void onNetworkError(VolleyError error) {
                // network error
            }
            @Override
            public void onBackPressed() {
                // the user decided to leave the payment page
                mPaymentForm.clearForm(); // this clears the Payment Form
            }
        };
    }
}