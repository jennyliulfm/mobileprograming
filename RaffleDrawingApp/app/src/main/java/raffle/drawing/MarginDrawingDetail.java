package raffle.drawing;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

public class MarginDrawingDetail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_margin_drawing_detail);

        //Get raffle ID
        Intent drawingInent = getIntent();
        final Raffle selectedRaffle  = (Raffle)drawingInent.getSerializableExtra(MarignDrawing.DRAWING_RAFFLE_STRING);
        int rafflid = selectedRaffle.getId();

        int winner_number = drawingInent.getExtras().getInt(MarignDrawing.MAGINE_RAFFLE_WIN_NUMBER);

                //open connection
        Database databaseConnection = new Database(MarginDrawingDetail.this);
        final SQLiteDatabase db = databaseConnection.open();

        //Get the tickets for the raffle
        ArrayList<Ticket> tickets  = TicketTable.getTicketByRaffleID(db, rafflid);

        //Drawing logic here For normal raffle which always has a winner
        int ticket_number  = selectedRaffle.getTicketNumber();

        int win_index = -1;
        for(int i=0; i<tickets.size(); i++)
        {
            if(tickets.get(i).getNumber() == winner_number)
            {
                win_index = i;
                break;
            }
        }
        LinearLayout uiLayout = findViewById(R.id.drawingticketMaginLayout);
        if(win_index != -1)
        {
            uiLayout.setVisibility(View.VISIBLE);

            Ticket winTicket = tickets.get(win_index);
            TextView ticketnumber= findViewById(R.id.drawingticketMaginNumber);
            ticketnumber.setText(String.valueOf(winTicket.getNumber()));

            TextView winerName = findViewById(R.id.drawingticketMaginName);
            winerName.setText(winTicket.getCustomerName());

            TextView winerEmail = findViewById(R.id.drawingticketMaginEmail);
            winerEmail.setText(winTicket.getCustomerEmail());

            TextView winerMobile = findViewById(R.id.drawingticketMaginTelephone);
            winerMobile.setText(winTicket.getCustomerMobile());

        }
        else
        {
            uiLayout.setVisibility(View.VISIBLE);
            TextView msgTV = findViewById(R.id.drawingticketMaginMsg);
            msgTV.setText("There is no winner for the raffle ");
            msgTV.setTextColor(Color.parseColor("#ED0915"));

        }

        Button okBt = findViewById(R.id.drawingticketMaginConfirm);
        okBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MarginDrawingDetail.this, "Welcome to home page", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(MarginDrawingDetail.this, MainActivity.class);
                startActivity(i);
            }
        });


    }
}
