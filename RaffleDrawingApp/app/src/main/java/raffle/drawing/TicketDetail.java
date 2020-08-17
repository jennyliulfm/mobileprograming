package raffle.drawing;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class TicketDetail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_detail);

        Intent ticektDetail = getIntent();
        final Ticket soldTicket = (Ticket) ticektDetail.getSerializableExtra(SellTicket.TICKET_DETAIL_STRING);
        String title = ticektDetail.getExtras().getString(SellTicket.RAFFLE_TITLE_STRING);
        final String sold_numbers = ticektDetail.getExtras().getString(SellTicket.TICKET_NUMBERS_STRING);
        final String sold_amount = ticektDetail.getExtras().getString(SellTicket.SOLD_TICKETS);
        final String total_price = ticektDetail.getExtras().getString(SellTicket.TICKET_TOTAL_PRICE_STRING);

        EditText raffleTitle = findViewById(R.id.ticketDetailRaffleTitle);
        raffleTitle.setText(title);

        EditText ticektAmount = findViewById(R.id.ticketDetailAmount);
        ticektAmount.setText(String.valueOf(sold_amount ));

        EditText ticketPrice = findViewById(R.id.ticketDetailprice);
        ticketPrice.setText(total_price);

        EditText customerName = findViewById(R.id.ticketDetailName);
        customerName.setText(soldTicket.getCustomerName());

        EditText customerEmail = findViewById(R.id.ticketDetailEmail);
        customerEmail.setText(soldTicket.getCustomerEmail());

        EditText customerMobile = findViewById(R.id.ticketDetailTelephone);
        customerMobile.setText(soldTicket.getCustomerMobile());

        EditText ticketNumber = findViewById(R.id.ticketDetailNumbers);
        ticketNumber.setText(sold_numbers);

        Button backButton = findViewById(R.id.ticketDetailOK);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String content = String.format("%s, Tickets: %s, Cost: $%s, Purchased: %s", soldTicket.getCustomerName(),sold_numbers,total_price, soldTicket.getDate());

                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = content ;
                String shareSub = "Raffle Drawing App";

                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, shareSub);
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Share using"));

//                Toast.makeText(TicketDetail.this, "Welcome to home page", Toast.LENGTH_SHORT).show();
//                Intent i = new Intent(TicketDetail.this, MainActivity.class);
//                startActivity(i);
            }
        });

        Button backButton2 = findViewById(R.id.ticketDetailBack);
        backButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(TicketDetail.this, "Welcome to home page", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(TicketDetail.this, MainActivity.class);
                startActivity(i);

            }
        });

    }
}
