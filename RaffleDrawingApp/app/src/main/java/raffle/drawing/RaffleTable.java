package raffle.drawing;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import java.util.ArrayList;

public class RaffleTable {

    public static final String TABLE_NAME = "raffle";
    public static final String KEY_RAFFLE_ID = "id";
    public static final String KEY_RAFFLE_TITLE = "title";
    public static final String KEY_RAFFLE_DESCRIPTION = "description";
    public static final String KEY_RAFFLE_STATUS = "status";
    public static final String KEY_RAFFLE_TYPE = "type";
    public static final String KEY_RAFFLE_TICKET_NUMBER  = "ticket_number";
    public static final String KEY_RAFFLE_WIN_NUMBER ="win_number";
    public static final String KEY_RAFFLE_SOLD_TICKET = "sold_tickets";
    public static final String KEY_RAFFLE_DATE = "date";

    public static final String INSERTTAG ="Insert Raffle";

    public static final String CREATE_STATEMENT = "CREATE TABLE "
            + TABLE_NAME
            + " (" + KEY_RAFFLE_ID + " integer primary key ,"
            + KEY_RAFFLE_TITLE + " string not null, "
            + KEY_RAFFLE_DESCRIPTION  + " string , "
            + KEY_RAFFLE_STATUS + " string not null, "
            + KEY_RAFFLE_TYPE + " string not null,"
            + KEY_RAFFLE_TICKET_NUMBER  + " int not null,"
            + KEY_RAFFLE_SOLD_TICKET + " int not null, "
            + KEY_RAFFLE_WIN_NUMBER  + " int , "
            + KEY_RAFFLE_DATE + " string not null "
            + ");";

    public static void insert(SQLiteDatabase db, Raffle raffle) {
        ContentValues values = new ContentValues();
        values.put(KEY_RAFFLE_ID, raffle.getId());
        values.put(KEY_RAFFLE_TITLE, raffle.getTitle());
        values.put(KEY_RAFFLE_DESCRIPTION,  raffle.getDescription());
        values.put(KEY_RAFFLE_STATUS, raffle.getStatus().toString());
        values.put(KEY_RAFFLE_TYPE,  raffle.getType().toString());
        values.put(KEY_RAFFLE_TICKET_NUMBER,  raffle.getTicketNumber());
        values.put(KEY_RAFFLE_WIN_NUMBER, raffle.getWinNumber());
        values.put(KEY_RAFFLE_SOLD_TICKET, raffle.getSoldTickets());
        values.put(KEY_RAFFLE_DATE, raffle.getDate());

        Log.d(INSERTTAG, raffle.getTitle());
        db.insert(TABLE_NAME, null, values);

    }

    public static void delete(SQLiteDatabase db, Raffle raffle) {
        ContentValues values = new ContentValues();
        String condition = KEY_RAFFLE_ID + " = ?";

        //delete the raffle according to its id
        db.delete(TABLE_NAME, condition, new String[]{"" + raffle.getId()});
    }

    public static void update(SQLiteDatabase db, Raffle raffle)
    {
        ContentValues values = new ContentValues();

        values.put(KEY_RAFFLE_ID,  raffle.getId());
        values.put(KEY_RAFFLE_TITLE, raffle.getTitle());
        values.put(KEY_RAFFLE_DESCRIPTION , raffle.getDescription());

        values.put(KEY_RAFFLE_STATUS, raffle.getStatus().toString());
        values.put(KEY_RAFFLE_TYPE, raffle.getType().toString());
        values.put(KEY_RAFFLE_TICKET_NUMBER, raffle.getTicketNumber());
        values.put(KEY_RAFFLE_SOLD_TICKET, raffle.getSoldTickets());
        values.put(KEY_RAFFLE_DATE, raffle.getDate());

        String condition = KEY_RAFFLE_ID + " = ?";
        db.update(TABLE_NAME, values, condition,
                new String[]{ ""+ raffle.getId()});
    }

    public static  Raffle createFromCursor(Cursor c)
    {
        if (c == null || c.isAfterLast() || c.isBeforeFirst())
        {
            return null;
        }
        else
        {
            Raffle raffle = new Raffle();
            raffle.setId(c.getInt(c.getColumnIndex(KEY_RAFFLE_ID)));
            raffle.setTitle(c.getString(c.getColumnIndex(KEY_RAFFLE_TITLE)));
            raffle.setDescription(c.getString(c.getColumnIndex(KEY_RAFFLE_DESCRIPTION)));

            //conversion
            raffle.setStatus(RaffleStatus.valueOf(c.getString(c.getColumnIndex(KEY_RAFFLE_STATUS))));
            raffle.setType(RaffleType.valueOf(c.getString(c.getColumnIndex(KEY_RAFFLE_TYPE))));

            raffle.setTicketNumber(c.getInt(c.getColumnIndex(KEY_RAFFLE_TICKET_NUMBER)));
            raffle.setSoldTickets(c.getInt(c.getColumnIndex(KEY_RAFFLE_SOLD_TICKET)));
            raffle.setDate(c.getString(c.getColumnIndex(KEY_RAFFLE_DATE)));

            return raffle;
        }
    }

    public static ArrayList<Raffle> selectAll(SQLiteDatabase db)
    {
        ArrayList<Raffle> results = new ArrayList<Raffle>();

        try
        {
            Cursor c = db.query(TABLE_NAME, null, null, null, null, null,KEY_RAFFLE_TITLE +" DESC" );

            if (c != null)
            {
                //make sure the cursor is at the start of the list
                c.moveToFirst();
                //loop through until we are at the end of the list
                while (!c.isAfterLast())
                {
                    Raffle raffle = createFromCursor(c);
                    results.add(raffle);
                    //increment the cursor
                    c.moveToNext();
                }
            }

        }catch (Exception e)
        {
            Log.d("error", e.toString());
        }

        return results;
    }


}