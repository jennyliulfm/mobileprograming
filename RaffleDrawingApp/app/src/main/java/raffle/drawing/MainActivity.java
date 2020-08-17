package raffle.drawing;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;


public class MainActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {

    private Raffle selectedRaffle;
    private SQLiteDatabase db;
    private RaffleAdaptor raffleListAdaptor;
    private ArrayList<Raffle> raffles;

    final static String EDIT_RAFFLE_STRING ="Edit Raffle";
    final static String RAFFLE_ID_STRING = "RAFFLE ID";
    final static String DELETE_TICKET_STRING ="Delete Ticket";
    final static String DRAWING_RAFFLE_STRING ="Drawing Raffle";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //open DB
        Database databaseConnection = new Database(this);
        db = databaseConnection.open();

        //Create a new raffle for the first time
        Raffle raffle = new Raffle();
        raffle.setId(1);
        raffle.setTitle("Fundarising for healthcare workers");
        raffle.setDescription("This raffle activity is used to fundarise for nurses and doctors who work in the front line");
        raffle.setStatus(RaffleStatus.DRAFT);
        raffle.setType(RaffleType.NORMAL);
        raffle.setTicketNumber(100);
        raffle.setWinNumber(0);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String date = simpleDateFormat.format(new Date());
        raffle.setDate(date);

        //Insert data into the DB
        RaffleTable.insert(db, raffle);

        //Get all raffles then set on the list
        raffles = RaffleTable.selectAll(db);

        //Set Adaptor
        final ListView raffleList = findViewById(R.id.raffleList);

        raffleListAdaptor =
                new RaffleAdaptor(getApplicationContext(),
                        R.layout.raffle_list_item, raffles);
        raffleList.setAdapter(raffleListAdaptor);

        //When you click on the items in the list for a long time, the pop up menu will show up instead of very short click
        raffleList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                selectedRaffle = raffleListAdaptor.getItem(position);

                PopupMenu rafflaction = new PopupMenu(MainActivity.this, view);
                rafflaction.inflate(R.menu.raffle_actions);
                rafflaction.show();

                rafflaction.setOnMenuItemClickListener(MainActivity.this);

                return false;
            }
        });

        //create a raffle by clicking on the float button
        FloatingActionButton newRaffleBT = findViewById(R.id.newRafffab);
        newRaffleBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(MainActivity.this, NewRaffle.class);
                startActivity(i);
                raffleListAdaptor.notifyDataSetChanged();
            }
        });

    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
       switch (item.getItemId())
       {
           //Release raffle online
           case  R.id.releaseraffle:

               if (selectedRaffle.getStatus() == RaffleStatus.DRAFT)
               {
                   selectedRaffle.setStatus(RaffleStatus.ONLINE);
                   RaffleTable.update(db,selectedRaffle );
                   //to notify the data has been changed.
                   raffleListAdaptor.notifyDataSetChanged();
                   Toast.makeText(this, "The raffle " + selectedRaffle.getTitle() +" is online now!", Toast.LENGTH_SHORT).show();
               }
               else
               {
                   Toast toast = Toast.makeText(this, "The raffle " + selectedRaffle.getTitle() + " has been online already!", Toast.LENGTH_SHORT);

                   View view = toast.getView();
                   //To change the Background of Toast
                   view.setBackgroundColor(Color.TRANSPARENT);
                   TextView text = (TextView) view.findViewById(android.R.id.message);
                   //Shadow of the Of the Text Color
                   text.setShadowLayer(0, 0, 0, Color.TRANSPARENT);
                   text.setTextColor(Color.RED);
                   toast.show();

               }

               return true;

               //Delete Raffle
           case R.id.deleteraffle:

               AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
               builder.setTitle("Delete Raffle");

               //set text on the alert dialog
               final EditText input = new EditText(MainActivity.this);
               input.setInputType(InputType.TYPE_CLASS_TEXT);
               input.setText("Are you sure you want to delete the raffle?");
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
                       RaffleTable.delete(db, selectedRaffle);

                       //also need to delete raffle from the list
                       for(int i=0; i< raffles.size(); i++)
                       {
                           if(raffles.get(i).getId() == selectedRaffle.getId())
                           {
                               raffles.remove(i);
                           }
                       }

                       raffleListAdaptor.notifyDataSetChanged();
                   }
               });

               builder.create().show();
               return true;

           case R.id.editRaffle:
             if(selectedRaffle.getStatus() == RaffleStatus.DRAFT)
             {
                   Intent editItent = new Intent(MainActivity.this, EditRaffle.class);
                   editItent.putExtra(this.EDIT_RAFFLE_STRING,selectedRaffle);
                   startActivity(editItent );
                 raffleListAdaptor.notifyDataSetChanged();

             }
             else if(selectedRaffle.getStatus() == RaffleStatus.ONLINE)
             {
                 Toast.makeText(this, "The Raffle " + selectedRaffle.getTitle() + " is online, so it cannot be edited", Toast.LENGTH_SHORT).show();
             }
             else
             {
                Toast toast = Toast.makeText(this, "The Raffle " + selectedRaffle.getTitle() + " is invalid, so it cannot be edited", Toast.LENGTH_SHORT);

                 View view = toast.getView();
                 //To change the Background of Toast
                 view.setBackgroundColor(Color.TRANSPARENT);
                 TextView text = (TextView) view.findViewById(android.R.id.message);
                 //Shadow of the Of the Text Color
                 text.setShadowLayer(0, 0, 0, Color.TRANSPARENT);
                 text.setTextColor(Color.RED);
                 toast.show();

             }
               return true;

           case R.id.sellTicket:
               if(selectedRaffle.getStatus() == RaffleStatus.ONLINE)
               {
                   Intent sellIntent = new Intent(MainActivity.this, SellTicket.class);
                   sellIntent.putExtra(RAFFLE_ID_STRING, selectedRaffle);
                   startActivity(sellIntent);
                   raffleListAdaptor.notifyDataSetChanged();
               }
               else
               {
                   Toast toast = Toast.makeText(this, "The Raffle " + selectedRaffle.getTitle() + " is draft, publish it first", Toast.LENGTH_SHORT);

                   View view = toast.getView();
                   //To change the Background of Toast
                   view.setBackgroundColor(Color.TRANSPARENT);
                   TextView text = (TextView) view.findViewById(android.R.id.message);
                   //Shadow of the Of the Text Color
                   text.setShadowLayer(0, 0, 0, Color.TRANSPARENT);
                   text.setTextColor(Color.RED);
                   toast.show();
               }
               return true;

           case R.id.deleteTicket :
               if(selectedRaffle.getStatus() == RaffleStatus.ONLINE)
               {
                   if(selectedRaffle.getSoldTickets() > 0)
                   {
                       Intent deleteTicketIntent = new Intent(MainActivity.this, DeleteTicket.class);
                       deleteTicketIntent.putExtra(RAFFLE_ID_STRING, selectedRaffle.getId());
                       deleteTicketIntent.putExtra(DELETE_TICKET_STRING,selectedRaffle);
                       startActivity(deleteTicketIntent);
                       raffleListAdaptor.notifyDataSetChanged();

                   }
                   else {
                       Toast.makeText(this, "No tickets have been sold", Toast.LENGTH_SHORT).show();
                   }

               }
               else
               {
                   Toast toast = Toast.makeText(this, "The Raffle " + selectedRaffle.getTitle() + " is draft, publish it first", Toast.LENGTH_SHORT);

                   View view = toast.getView();
                   //To change the Background of Toast
                   view.setBackgroundColor(Color.TRANSPARENT);
                   TextView text = (TextView) view.findViewById(android.R.id.message);
                   //Shadow of the Of the Text Color
                   text.setShadowLayer(0, 0, 0, Color.TRANSPARENT);
                   text.setTextColor(Color.RED);
                   toast.show();
               }

               return true;

           case R.id.editTicket:
               if(selectedRaffle.getStatus() == RaffleStatus.ONLINE)
               {
                   if(selectedRaffle.getSoldTickets() > 0)
                   {
                       Intent editTicketIntent = new Intent(MainActivity.this, EditTicket.class);
                       editTicketIntent.putExtra(RAFFLE_ID_STRING, selectedRaffle.getId());
                       startActivity(editTicketIntent);
                       raffleListAdaptor.notifyDataSetChanged();
                   }
                   else
                   {
                       Toast.makeText(this, "No tickets have been sold", Toast.LENGTH_SHORT).show();
                   }

               }
               else
               {
                   Toast toast = Toast.makeText(this, "The Raffle " + selectedRaffle.getTitle() + " is draft, publish it first", Toast.LENGTH_SHORT);

                   View view = toast.getView();
                   //To change the Background of Toast
                   view.setBackgroundColor(Color.TRANSPARENT);
                   TextView text = (TextView) view.findViewById(android.R.id.message);
                   //Shadow of the Of the Text Color
                   text.setShadowLayer(0, 0, 0, Color.TRANSPARENT);
                   text.setTextColor(Color.RED);
                   toast.show();
               }

               return true;

           case R.id.viewTicket:
               if(selectedRaffle.getStatus() == RaffleStatus.ONLINE)
               {
                   if(selectedRaffle.getSoldTickets() > 0)
                   {
                       Intent viewTicketIntent = new Intent(MainActivity.this, ViewTicket.class);
                       viewTicketIntent.putExtra(RAFFLE_ID_STRING, selectedRaffle.getId());
                       startActivity(viewTicketIntent);
                   }
                   else
                   {
                       Toast.makeText(this, "No tickets have been sold", Toast.LENGTH_SHORT).show();
                   }

               }
               else
               {
                   Toast toast = Toast.makeText(this, "The Raffle " + selectedRaffle.getTitle() + " is draft, publish it first", Toast.LENGTH_SHORT);

                   View view = toast.getView();
                   //To change the Background of Toast
                   view.setBackgroundColor(Color.TRANSPARENT);
                   TextView text = (TextView) view.findViewById(android.R.id.message);
                   //Shadow of the Of the Text Color
                   text.setShadowLayer(0, 0, 0, Color.TRANSPARENT);
                   text.setTextColor(Color.RED);
                   toast.show();
               }
               return true;
           case R.id.drawRaffle:
               if(selectedRaffle.getStatus() == RaffleStatus.ONLINE)
               {
                   if(selectedRaffle.getType() == RaffleType.NORMAL)
                   {
                       if(selectedRaffle.getSoldTickets() > 0)
                       {
                           Intent drawTicketIntent1 = new Intent(MainActivity.this, NormalDrawing.class);
                           drawTicketIntent1.putExtra(DRAWING_RAFFLE_STRING, selectedRaffle);
                           startActivity(drawTicketIntent1);
                           raffleListAdaptor.notifyDataSetChanged();
                       }
                       else {
                           Toast toast = Toast.makeText(this, "The Raffle " + selectedRaffle.getTitle() + " is draft, publish it first", Toast.LENGTH_SHORT);

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
                   else
                   {
                       if(selectedRaffle.getSoldTickets() > 0)
                       {
                           Intent drawTicketIntent2 = new Intent(MainActivity.this, MarignDrawing.class);
                           drawTicketIntent2.putExtra(DRAWING_RAFFLE_STRING, selectedRaffle);
                           startActivity(drawTicketIntent2);
                           raffleListAdaptor.notifyDataSetChanged();
                       }
                       else {
                           Toast toast = Toast.makeText(this, "The Raffle " + selectedRaffle.getTitle() + " is draft, publish it first", Toast.LENGTH_SHORT);

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
               else
               {
                   Toast toast = Toast.makeText(this, "The Raffle " + selectedRaffle.getTitle() + " is draft, publish it first", Toast.LENGTH_SHORT);

                   View view = toast.getView();
                   //To change the Background of Toast
                   view.setBackgroundColor(Color.TRANSPARENT);
                   TextView text = (TextView) view.findViewById(android.R.id.message);
                   //Shadow of the Of the Text Color
                   text.setShadowLayer(0, 0, 0, Color.TRANSPARENT);
                   text.setTextColor(Color.RED);
                   toast.show();

               }
               return true;

           default:
               return false;
       }

    }
}
