package raffle.drawing;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class EditTicket extends AppCompatActivity {

    final static String TICKET_EDIT_DETAIL = "Ticket Details";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_ticket);

        //Get raffle ID
        Intent editTicketInent = getIntent();
        int rafflid = editTicketInent.getExtras().getInt(MainActivity.RAFFLE_ID_STRING);

        //open connection
        Database databaseConnection = new Database(this);
        final SQLiteDatabase db = databaseConnection.open();

        //Get the tickets for the raffle
        ArrayList<Ticket> tickets  = TicketTable.getTicketByRaffleID(db, rafflid);

        //Set Adaptor
        final ListView ticketList = findViewById(R.id.editticketList);

        final TicketAdaptor ticketListAdaptor =
                new TicketAdaptor(getApplicationContext(),
                        R.layout.ticket_list_item, tickets);
        ticketList.setAdapter(ticketListAdaptor);
        ticketList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Ticket selectedTicket = ticketListAdaptor.getItem(position);
                Intent editTicketDetail = new Intent(EditTicket.this, EditTicketDetail.class);
                editTicketDetail.putExtra(TICKET_EDIT_DETAIL, selectedTicket);
                startActivity(editTicketDetail);
            }
        });



        FloatingActionButton backButton = findViewById(R.id.editTicketFab);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(EditTicket.this, "Welcome to home page", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(EditTicket.this, MainActivity.class);
                startActivity(i);
            }
        });



    }
}
