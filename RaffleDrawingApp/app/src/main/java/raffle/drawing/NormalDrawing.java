package raffle.drawing;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

public class NormalDrawing extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_normal_drawing);

        //Get raffle ID
        Intent drawingInent = getIntent();
        final Raffle selectedRaffle  = (Raffle)drawingInent.getSerializableExtra(MainActivity.DRAWING_RAFFLE_STRING);
        int rafflid = selectedRaffle.getId();

        //open connection
        Database databaseConnection = new Database(this);
        final SQLiteDatabase db = databaseConnection.open();

        //Get the tickets for the raffle
        ArrayList<Ticket> tickets  = TicketTable.getTicketByRaffleID(db, rafflid);

        //Drawing logic here For normal raffle which always has a winner
        int sold_tickets = selectedRaffle.getSoldTickets();

        int winner_number = new Random().nextInt(sold_tickets ) + 1;

        int win_index = 0;
        for(int i=0; i<tickets.size(); i++)
        {
            if(tickets.get(i).getNumber() == winner_number)
            {
               win_index = i;
               break;
            }
        }

        Ticket winTicket = tickets.get(win_index);
        TextView ticketnumber= findViewById(R.id.drawingticketNumber);
        ticketnumber.setText(String.valueOf(winTicket.getNumber()));

        TextView winerName = findViewById(R.id.drawingticketName);
        winerName.setText(winTicket.getCustomerName());

        TextView winerEmail = findViewById(R.id.drawingticketEmail);
        winerEmail.setText(winTicket.getCustomerEmail());

        TextView winerMobile = findViewById(R.id.drawingticketTelephone);
        winerMobile.setText(winTicket.getCustomerMobile());

        Button okBt = findViewById(R.id.drawingticketConfirm);
        okBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(NormalDrawing.this, "Welcome to home page", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(NormalDrawing.this, MainActivity.class);
                startActivity(i);
            }
        });


    }
}
