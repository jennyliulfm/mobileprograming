package raffle.drawing;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class DeleteTicket extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_ticket);

        //Get raffle ID
        Intent editTicketInent = getIntent();
        final int rafflid = editTicketInent.getExtras().getInt(MainActivity.RAFFLE_ID_STRING);
        final Raffle raffle= (Raffle) editTicketInent.getSerializableExtra(MainActivity.DELETE_TICKET_STRING);

        //open connection
        Database databaseConnection = new Database(this);
        final SQLiteDatabase db = databaseConnection.open();

        //Get the tickets for the raffle
       final ArrayList<Ticket> tickets  = TicketTable.getTicketByRaffleID(db, rafflid);

        //Set Adaptor
        final ListView ticketList = findViewById(R.id.deleteticketList);

        final TicketAdaptor ticketListAdaptor =
                new TicketAdaptor(getApplicationContext(),
                        R.layout.ticket_list_item, tickets);
        ticketList.setAdapter(ticketListAdaptor);

        ticketList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final Ticket selectedTicket = ticketListAdaptor.getItem(position);

                AlertDialog.Builder builder = new AlertDialog.Builder(DeleteTicket.this);
                builder.setTitle("Delete Raffle");

                //set text on the alert dialog
                final EditText input = new EditText(DeleteTicket.this);
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                input.setText("Are you sure you want to delete the ticket?");
                input.setTextColor(Color.parseColor("#FC1100"));
                builder.setView(input);

                //add one cancel button there
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        TicketTable.delete(db, selectedTicket);
                        //also need to delete raffle from the list
                        //Delete ticket sfrom the list
                        for(int i=0; i< tickets.size(); i++)
                        {
                            if(tickets.get(i).getId() == selectedTicket.getId())
                            {
                                tickets.remove(i);
                            }
                        }
                        ticketListAdaptor.notifyDataSetChanged();

                        //Also  update raffle sold tickets numbers
                        int sold_tickets = raffle.getSoldTickets();
                        sold_tickets --;
                        raffle.setSoldTickets(sold_tickets);
                        RaffleTable.update(db, raffle);

                    }
                });

                builder.create().show();

            }
        });

        FloatingActionButton gobackButton = findViewById(R.id.deleteTicketFab);
        gobackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(DeleteTicket.this, "Welcome to home page", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(DeleteTicket.this, MainActivity.class);
                startActivity(i);
            }
        });

    }
}
