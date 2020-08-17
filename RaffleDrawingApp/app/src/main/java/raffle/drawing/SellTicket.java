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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class SellTicket extends AppCompatActivity {

    final static String TICKET_DETAIL_STRING ="Ticket Details";
    final static String RAFFLE_TITLE_STRING ="Raffle Title";
    final static String TICKET_NUMBERS_STRING ="Sold Ticket Numbers";
    final static String SOLD_TICKETS ="Sold Tickets Amount";
    final static String TICKET_TOTAL_PRICE_STRING =" Total Cost";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell_ticket);

        //open DB
        Database databaseConnection = new Database(this);
        final SQLiteDatabase db = databaseConnection.open();

        //Get raffle id from the mainactivity
        Intent sellTicket = getIntent();
        final Raffle raffle = (Raffle)sellTicket.getSerializableExtra(MainActivity.RAFFLE_ID_STRING);

        final EditText ticketNumber = findViewById(R.id.sellTicketNumber);
        final EditText ticketPrice = findViewById(R.id.sellticektprice);
        final EditText ticketName = findViewById(R.id.sellticektName);
        final EditText ticketEmail = findViewById(R.id.sellticektEmail);
        final EditText ticketMobile = findViewById(R.id.sellticektTelephone);

        Button confirmButton = findViewById(R.id.sellticketConfirm);
        Button cancelButton = findViewById(R.id.sellticketCancel);

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(ticketNumber.getText().toString()=="" || ticketPrice.getText().toString()=="" || ticketName.getText().toString()=="")
                {
                    Toast toast = Toast.makeText(SellTicket.this, "Ticket Number, tickt price and customer name cannot be empty", Toast.LENGTH_SHORT);
                    View view = toast.getView();
                    //To change the Background of Toast
                    view.setBackgroundColor(Color.TRANSPARENT);
                    TextView text = (TextView) view.findViewById(android.R.id.message);
                    //Shadow of the Of the Text Color
                    text.setShadowLayer(0, 0, 0, Color.TRANSPARENT);
                    text.setTextColor(Color.RED);
                    toast.show();
                }
                else
                {
                    if(Integer.parseInt(ticketNumber.getText().toString()) >3 || Integer.parseInt(ticketNumber.getText().toString())<0)
                    {
                        Toast toast = Toast.makeText(SellTicket.this, "One customer can only buy maximum three tickets", Toast.LENGTH_SHORT);
                        View view = toast.getView();
                        //To change the Background of Toast
                        view.setBackgroundColor(Color.TRANSPARENT);
                        TextView text = (TextView) view.findViewById(android.R.id.message);
                        //Shadow of the Of the Text Color
                        text.setShadowLayer(0, 0, 0, Color.TRANSPARENT);
                        text.setTextColor(Color.RED);
                        toast.show();
                    }
                    else
                    {
                        int left_ticket = raffle.getTicketNumber() - raffle.getSoldTickets() ;

                        //Check whether there are enough tickets
                        if(left_ticket >=0)
                        {
                            int ticket_amount = Integer.parseInt(ticketNumber.getText().toString());
                            String ticket_numbers ="";
                            Ticket ticket = new Ticket();
                            int sold_tickets = raffle.getSoldTickets();
                            for(int i=0; i<ticket_amount; i++)
                            {
                                int id = new Random().nextInt((100000 - 10000) + 1) + 10000;
                                ticket.setId(id);

                                ticket.setRaffleid(raffle.getId());

                                ticket.setCustomerName(ticketName.getText().toString());
                                ticket.setCustomerEmail(ticketEmail.getText().toString());
                                ticket.setCustomerMobile(ticketMobile.getText().toString());

                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
                                String date = simpleDateFormat.format(new Date());
                                ticket.setDate(date);

                                //Price for a single ticket
                                double single_price = Double.parseDouble(ticketPrice.getText().toString()) /ticket_amount;
                                ticket.setPrice(single_price);

                                //Generate number for a single ticket based on its type
                                if(raffle.getType() == RaffleType.NORMAL)
                                {
                                    int number = sold_tickets + 1;
                                    ticket.setNumber(number);
                                    ticket_numbers += " ," + number;
                                    sold_tickets ++;
                                }
                                else
                                {
                                    int number = new Random().nextInt((raffle.getTicketNumber()) + 1);
                                    ticket.setNumber(number);
                                    ticket_numbers  += " ," + number;
                                }

                                //Insert data
                                TicketTable.insert(db,ticket);
                            }


                            //Update raffle sold number;
                            int previous_Sold_number = raffle.getSoldTickets();
                            previous_Sold_number += ticket_amount;
                            raffle.setSoldTickets(previous_Sold_number );
                            RaffleTable.update(db, raffle);

                            Intent detailIntent = new Intent(SellTicket.this, TicketDetail.class);
                            detailIntent.putExtra(TICKET_DETAIL_STRING, ticket);
                            detailIntent.putExtra(RAFFLE_TITLE_STRING, raffle.getTitle());
                            detailIntent.putExtra(SOLD_TICKETS, ticketNumber.getText().toString());
                            detailIntent.putExtra(TICKET_NUMBERS_STRING,ticket_numbers);
                            detailIntent.putExtra(TICKET_TOTAL_PRICE_STRING,ticketPrice.getText().toString());
                            startActivity(detailIntent);

                        }
                        else
                        {

                            Toast toast = Toast.makeText(SellTicket.this, "Not enough Tickets", Toast.LENGTH_SHORT);
                            View view = toast.getView();
                            //To change the Background of Toast
                            view.setBackgroundColor(Color.TRANSPARENT);
                            TextView text = (TextView) view.findViewById(android.R.id.message);
                            //Shadow of the Of the Text Color
                            text.setShadowLayer(0, 0, 0, Color.TRANSPARENT);
                            text.setTextColor(Color.RED);
                            toast.show();
                        }

                    }

                }
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SellTicket.this);
                builder.setTitle("Cancel Sell Ticket");

                //set text on the alert dialog
                final EditText input = new EditText(SellTicket.this);
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                input.setText("Are you sure you want to cancel sell ticket?");
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
                        Toast.makeText(SellTicket.this, "Welcome to home page", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(SellTicket.this, MainActivity.class);
                        startActivity(i);
                    }
                });

                builder.create().show();
            }
        });


    }
}
