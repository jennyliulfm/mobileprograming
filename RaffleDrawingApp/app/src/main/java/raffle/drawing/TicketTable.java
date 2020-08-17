package raffle.drawing;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class TicketTable {

    //Each column in the ticket table.
    public static final String TABLE_NAME = "ticket";
    public static final String KEY_TICKET_ID = "ticketid";
    public static final String KEY_RAFFLE_ID = "raffleid";
    public static final String KEY_TICKET_NUMBER = "number";
    public static final String KEY_TICKET_PRICE = "price";
    public static final String KEY_TICKET_DATE = "date";
    public static final String KEY_TICKET_CUSTOMER_NAME = "customername";
    public static final String KEY_TICKET_CUSTOMER_EMAIL = "customeremail";
    public static final String KEY_TICKET_CUSTOMER_MOBILE = "customermobile";

    public static final String INSERTTAG ="Insert Ticket";
    public static final String CREATE_STATEMENT = "CREATE TABLE "
            + TABLE_NAME
            + " (" + KEY_TICKET_ID + " integer primary key , "
            + KEY_RAFFLE_ID  + " int not null, "
            + KEY_TICKET_NUMBER + " int not null, "
            + KEY_TICKET_PRICE + " double not null, "
            + KEY_TICKET_DATE + " string not null, "
            + KEY_TICKET_CUSTOMER_NAME + " string not null, "
            + KEY_TICKET_CUSTOMER_EMAIL + " string not null, "
            + KEY_TICKET_CUSTOMER_MOBILE + " string "
            + ");";

    public static void insert(SQLiteDatabase db, Ticket ticket) {
        ContentValues values = new ContentValues();
        values.put(KEY_TICKET_ID, ticket.getId());
        values.put( KEY_RAFFLE_ID, ticket.getRaffleid());
        values.put(KEY_TICKET_NUMBER, ticket.getNumber());
        values.put(KEY_TICKET_PRICE, ticket.getPrice());

        values.put(KEY_TICKET_CUSTOMER_NAME, ticket.getCustomerName());
        values.put(KEY_TICKET_CUSTOMER_EMAIL, ticket.getCustomerEmail());
        values.put(KEY_TICKET_CUSTOMER_MOBILE,  ticket.getCustomerMobile());
        values.put(KEY_TICKET_DATE, ticket.getDate());


        Log.d(INSERTTAG, String.valueOf(ticket.getId()));
        db.insert(TABLE_NAME, null, values);
    }

    public static void delete(SQLiteDatabase db, Ticket ticket) {
        ContentValues values = new ContentValues();
        String condition = KEY_TICKET_ID + " = ?";

        //delete the raffle according to its id
        db.delete(TABLE_NAME, condition,new String[]{"" + ticket.getId()});
    }

    public static void update(SQLiteDatabase db, Ticket ticket)
    {
        ContentValues values = new ContentValues();

        values.put(KEY_TICKET_ID,  ticket.getId());
        values.put( KEY_RAFFLE_ID,  ticket.getRaffleid());
        values.put(KEY_TICKET_NUMBER, ticket.getNumber());
        values.put(KEY_TICKET_PRICE, ticket.getPrice());

        values.put(KEY_TICKET_CUSTOMER_NAME, ticket.getCustomerName());
        values.put(KEY_TICKET_CUSTOMER_EMAIL, ticket.getCustomerEmail());
        values.put(KEY_TICKET_CUSTOMER_MOBILE,  ticket.getCustomerMobile());
        values.put(KEY_TICKET_DATE, ticket.getDate());


        db.update(TABLE_NAME, values, KEY_TICKET_ID + " = ?",
                new String[]{ ""+ticket.getId()});

    }

    public static Ticket createFromCursor(Cursor c)
    {
        if (c == null || c.isAfterLast() || c.isBeforeFirst())
        {
            return null;
        }
        else
        {
            Ticket ticket = new Ticket();

            ticket.setId(c.getInt(c.getColumnIndex(KEY_TICKET_ID)));
            ticket.setRaffleid(c.getInt(c.getColumnIndex(KEY_RAFFLE_ID)));
            ticket.setNumber(c.getInt(c.getColumnIndex(KEY_TICKET_NUMBER)));
            ticket.setPrice(c.getInt(c.getColumnIndex(KEY_TICKET_PRICE)));
            ticket.setDate(c.getString(c.getColumnIndex(KEY_TICKET_DATE)));
            ticket.setCustomerName(c.getString(c.getColumnIndex(KEY_TICKET_CUSTOMER_NAME)));
            ticket.setCustomerEmail(c.getString(c.getColumnIndex(KEY_TICKET_CUSTOMER_EMAIL)));
            ticket.setCustomerMobile(c.getString(c.getColumnIndex(KEY_TICKET_CUSTOMER_MOBILE)));
            ticket.setDate(c.getString(c.getColumnIndex(KEY_TICKET_DATE)));
            return ticket;
        }
    }

    public static ArrayList<Ticket> selectAll(SQLiteDatabase db)
    {
        ArrayList<Ticket> results = new ArrayList<Ticket>();

        Cursor c = db.query(TABLE_NAME, null, null, null, null, null, null);
        if (c != null)
        {
            //make sure the cursor is at the start of the list
            c.moveToFirst();
            //loop through until we are at the end of the list
            while (!c.isAfterLast())
            {
                Ticket t = createFromCursor(c);
                results.add(t);
                //increment the cursor
                c.moveToNext();
            }
        }
        return results;
    }

    public static ArrayList<Ticket> getTicketByRaffleID(SQLiteDatabase db, int raffleID)
    {
        //According to the raffleID to get its sold_tickets.
        ArrayList<Ticket> results = new ArrayList<Ticket>();
        String condition = KEY_RAFFLE_ID + " = ?";
        Cursor c = db.query(TABLE_NAME, null, condition, new String[] { ""+ raffleID }, null, null, KEY_TICKET_NUMBER +" ASC");
        if (c != null)
        {
            //make sure the cursor is at the start of the list
            c.moveToFirst();
            //loop through until we are at the end of the list
            while (!c.isAfterLast())
            {
                Ticket ticket = createFromCursor(c);
                results.add(ticket);
                //increment the cursor
                c.moveToNext();
            }
        }
        return results;
    }



}
