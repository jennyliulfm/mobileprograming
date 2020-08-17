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

public class EditTicketDetail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_ticket_detail);

        Database databaseConnection = new Database(this);
        final SQLiteDatabase db = databaseConnection.open();

        //Get data from mainactivity
        Intent editIntent = getIntent();
        final Ticket editTicket = (Ticket)editIntent.getSerializableExtra(EditTicket.TICKET_EDIT_DETAIL);

        final TextView ticketName = findViewById(R.id.editticketdetailName);
        ticketName.setText(editTicket.getCustomerName());

        final TextView ticketEmail = findViewById(R.id.editticketdetailEmail);
        ticketEmail.setText(editTicket.getCustomerEmail());

        final TextView ticketTelephone = findViewById(R.id.editticketdetailTelephone);
        ticketTelephone.setText(editTicket.getCustomerMobile());


        Button confirmButton = findViewById(R.id.editticketdetailConfirm);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTicket.setCustomerName(ticketName.getText().toString());

                editTicket.setCustomerEmail(ticketEmail.getText().toString());
                editTicket.setCustomerMobile(ticketTelephone.getText().toString());

                TicketTable.update(db, editTicket);
                Toast.makeText(EditTicketDetail.this, "The ticket " + editTicket.getNumber() + " has been updated", Toast.LENGTH_SHORT).show();

                Intent i = new Intent(EditTicketDetail.this, MainActivity.class);
                startActivity(i);
            }
        });


        Button cancelButton = findViewById(R.id.editticketdetailCancel);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(EditTicketDetail.this);
                builder.setTitle("Edit Ticket");

                //set text on the alert dialog
                final EditText input = new EditText(EditTicketDetail.this);
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                input.setText("Are you sure you want to cancel editing the raffle?");
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
                        Intent i = new Intent(EditTicketDetail.this, MainActivity.class);
                        startActivity(i);
                    }
                });

                builder.create().show();
            }
        });

    }
}
