package raffle.drawing;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class ViewTicket extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_ticket);

        //Get raffle ID
        Intent editTicketInent = getIntent();
        int rafflid = editTicketInent.getExtras().getInt(MainActivity.RAFFLE_ID_STRING);

        //open connection
        Database databaseConnection = new Database(this);
        final SQLiteDatabase db = databaseConnection.open();

        //Get the tickets for the raffle
        ArrayList<Ticket> tickets  = TicketTable.getTicketByRaffleID(db, rafflid);

        //Set Adaptor
        final ListView ticketList = findViewById(R.id.viewticketList);

        TicketAdaptor ticketListAdaptor =
                new TicketAdaptor(getApplicationContext(),
                        R.layout.ticket_list_item, tickets);
        ticketList.setAdapter(ticketListAdaptor);

        FloatingActionButton gobackButton = findViewById(R.id.viewTicketFab);
        gobackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ViewTicket.this, "Welcome to home page", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(ViewTicket.this, MainActivity.class);
                startActivity(i);
            }
        });

    }
}
