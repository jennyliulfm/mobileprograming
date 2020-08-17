package raffle.drawing;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class NewRaffle extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_raffle);

        //open DB to create a new raffle
        Database databaseConnection = new Database(this);
        final SQLiteDatabase db = databaseConnection.open();

        //Try to get the values from the inputs
        final EditText newraffleTitle = findViewById(R.id.newraffleTitle);
        final EditText newraffleDescription = findViewById(R.id.newraffleDescription);
        final EditText ticketnumber = findViewById(R.id.newraffleTicketNumber);
        final RadioGroup raffleType = findViewById(R.id.newraffleType);
        Button saveDraft = findViewById(R.id.newraffleSave);
        Button publishOnline = findViewById(R.id.newraffleConfirm);

        //Publish online
        publishOnline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String title = newraffleTitle.getText().toString();
                int number = Integer.parseInt(ticketnumber.getText().toString());

                if(title=="" || number ==0)
                {
                    Toast toast = Toast.makeText(NewRaffle.this, "title and ticketnumber cannot be empty", Toast.LENGTH_SHORT);
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
                    Raffle raffle = new Raffle();
                    int raffleid = new Random().nextInt((100000 - 10000) + 1) + 10000;

                    raffle.setId(raffleid);
                    raffle.setTitle(title);
                    raffle.setDescription(newraffleDescription.getText().toString());
                    raffle.setTicketNumber(number);

                    RaffleType type;
                    int typeid= raffleType.getCheckedRadioButtonId();
                    if(typeid == R.id.newraffleType1 )
                    {
                        type = RaffleType.MARGINE;
                    }
                    else
                    {
                        type = RaffleType.NORMAL;
                    }
                    raffle.setType(type);

                    //Date
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
                    String date = simpleDateFormat.format(new Date());
                    raffle.setDate(date);


                    //RAffle status as online
                    raffle.setStatus(RaffleStatus.ONLINE);
                    //Insert the new Raffle
                    RaffleTable.insert(db, raffle);

                    Toast.makeText(NewRaffle.this, "The raffle " + raffle.getTitle() + " has been created successfully", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(NewRaffle.this, MainActivity.class);
                    startActivity(i);
                }

            }
        });


        //Save as a draft
        saveDraft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String title = newraffleTitle.getText().toString();
                int number = Integer.parseInt(ticketnumber.getText().toString());

                if(title=="" || number ==0)
                {
                    Toast toast = Toast.makeText(NewRaffle.this, "title and ticketnumber cannot be empty", Toast.LENGTH_SHORT);
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
                    Raffle raffle = new Raffle();
                    int raffleid = new Random().nextInt((10000 - 1000) + 1) + 1000;

                    raffle.setId(raffleid);
                    raffle.setTitle(title);
                    raffle.setDescription(newraffleDescription.getText().toString());
                    raffle.setTicketNumber(number);

                    RaffleType type;
                    int typeid= raffleType.getCheckedRadioButtonId();
                    if(typeid == R.id.newraffleType1 )
                    {
                        type = RaffleType.MARGINE;
                    }
                    else
                    {
                        type = RaffleType.NORMAL;
                    }
                    raffle.setType(type);

                    //Date
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
                    String date = simpleDateFormat.format(new Date());
                    raffle.setDate(date);

                    raffle.setSoldTickets(0);

                    //RAffle status as online
                    raffle.setStatus(RaffleStatus.DRAFT);
                    //Insert the new Raffle
                    RaffleTable.insert(db, raffle);

                    Toast.makeText(NewRaffle.this, "The raffle " + raffle.getTitle() + " has been saved successfully", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(NewRaffle.this, MainActivity.class);
                    startActivity(i);
                }

            }
        });



    }
}
