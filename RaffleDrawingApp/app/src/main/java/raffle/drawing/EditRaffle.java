package raffle.drawing;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class EditRaffle extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_raffle);

        //open DB to create a new raffle
        Database databaseConnection = new Database(this);
        final SQLiteDatabase db = databaseConnection.open();

        //Get data from mainactivity
        Intent editIntent = getIntent();
        final Raffle editRaffle = (Raffle)editIntent.getSerializableExtra(MainActivity.EDIT_RAFFLE_STRING);

        //Try to get the values from the inputs
        final EditText editraffleTitle = findViewById(R.id.editaffleTitle);
        final EditText editraffleDescription = findViewById(R.id.editaffleDescription);
        final EditText ticketnumber = findViewById(R.id.editaffleTicketNumber);
        final RadioGroup raffleType = findViewById(R.id.editaffleType);

        //UI to show previous content
        editraffleTitle.setText(editRaffle.getTitle());
        editraffleDescription.setText(editRaffle.getDescription());
        int number = editRaffle.getTicketNumber();
        ticketnumber.setText(String.valueOf(number));

        if(editRaffle.getType() == RaffleType.MARGINE)
        {
            raffleType.check(R.id.editaffleType1);
        }
        else
        {
            raffleType.check(R.id.editaffleType2);
        }


        Button saveDraft = findViewById(R.id.editaffleSave);
        Button publishOnline = findViewById(R.id.editaffleConfirm);

        //Publish online
        publishOnline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                editRaffle.setTitle(editraffleTitle.getText().toString());
                editRaffle.setDescription(editraffleDescription.getText().toString());
                editRaffle.setTicketNumber(Integer.parseInt(ticketnumber.getText().toString()));

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
                editRaffle.setType(type);

                editRaffle.setStatus(RaffleStatus.ONLINE);

                //Date
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
                String date = simpleDateFormat.format(new Date());
                editRaffle.setDate(date);

                RaffleTable.update(db, editRaffle);

                Toast.makeText(EditRaffle.this, "The raffle " + editRaffle.getTitle() + " has been updated successfully", Toast.LENGTH_SHORT).show();

                Intent i = new Intent(EditRaffle.this, MainActivity.class);
                startActivity(i);

            }
        });


        //Save as a draft
        saveDraft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                editRaffle.setTitle(editraffleTitle.getText().toString());
                editRaffle.setDescription(editraffleDescription.getText().toString());
                editRaffle.setTicketNumber(Integer.parseInt(ticketnumber.getText().toString()));

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
                editRaffle.setType(type);

                editRaffle.setStatus(RaffleStatus.DRAFT);

                //Date
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
                String date = simpleDateFormat.format(new Date());
                editRaffle.setDate(date);

                RaffleTable.update(db, editRaffle);

                Toast.makeText(EditRaffle.this, "The raffle " + editRaffle.getTitle() + " has been updated successfully", Toast.LENGTH_SHORT).show();

                Intent i = new Intent(EditRaffle.this, MainActivity.class);
                startActivity(i);
            }
        });
    }
}
