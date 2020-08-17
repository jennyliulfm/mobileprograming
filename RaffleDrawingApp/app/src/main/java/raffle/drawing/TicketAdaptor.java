package raffle.drawing;

import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

public class TicketAdaptor extends ArrayAdapter<Ticket> {

    private int mLayoutResourceID;
    private Database databaseConnection;
    private SQLiteDatabase db;
    private Context mContext;

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater =
                (LayoutInflater)getContext().getSystemService(Service.LAYOUT_INFLATER_SERVICE);
        View row = layoutInflater.inflate(mLayoutResourceID, parent, false);

        final Ticket ticket = this.getItem(position);

        TextView ticketNumber = row.findViewById(R.id.ticketEditNumber);
        ticketNumber.setText(String.valueOf(ticket.getNumber()));

        TextView ticketName = row.findViewById(R.id.ticketEditName);
        ticketName.setText(ticket.getCustomerName());

        TextView ticketEmail = row.findViewById(R.id.ticketEditEmail);
        ticketEmail.setText(ticket.getCustomerEmail());

        TextView ticketMobile = row.findViewById(R.id.ticketEditMobile);
        ticketMobile.setText(ticket.getCustomerMobile());

        TextView ticketEditDate = row.findViewById(R.id.ticketEditDate);
        ticketEditDate.setText(ticket.getDate());

        return row;
    }

    public TicketAdaptor(Context context, int resource, List<Ticket> objects)
    {
        super(context, resource, objects);
        this.mLayoutResourceID = resource;
        this.mContext = context;

        databaseConnection = new Database(context);
        db = databaseConnection.open();
    }
    @Nullable
    @Override
    public Ticket getItem(int position) {
        return super.getItem(position);
    }

}
