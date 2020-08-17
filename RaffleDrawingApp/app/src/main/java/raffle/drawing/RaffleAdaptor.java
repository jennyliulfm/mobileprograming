package raffle.drawing;

import android.app.Service;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class RaffleAdaptor extends ArrayAdapter<Raffle>
{

        private int mLayoutResourceID;
        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater =
                    (LayoutInflater)getContext().getSystemService(Service.LAYOUT_INFLATER_SERVICE);
            View row = layoutInflater.inflate(mLayoutResourceID, parent, false);

            final Raffle raffle = this.getItem(position);

            TextView raffleTitle = row.findViewById(R.id.raffleTitle);
            raffleTitle.setText( raffle.getTitle() );

            LinearLayout imageLayout = row.findViewById(R.id.imageLayout);

            TextView raffleStatus = row.findViewById(R.id.raffleStatus);
            raffleStatus.setText("" + raffle.getStatus().toString());


            if(raffle.getStatus() == RaffleStatus.DRAFT)
            {
                raffleStatus.setTextColor(Color.parseColor("#989A9C"));
            }
            else
            {
                raffleStatus.setTextColor(Color.parseColor("#0DD1A0"));
                imageLayout.setBackgroundResource(R.drawable.listview_background);
            }

            TextView raffleType = row.findViewById(R.id.raffleType);
            raffleType.setText(raffle.getType().toString());
            if(raffle.getType() == RaffleType.MARGINE)
            {
                raffleType.setTextColor(Color.parseColor("#0DD1A0"));
            }

            TextView raffleTickets = row.findViewById(R.id.raffleTicket);
            String ticketmsg = String.format("%d/%d",raffle.getSoldTickets(), raffle.getTicketNumber());
            raffleTickets.setText("" + ticketmsg);

            TextView raffleDate = row.findViewById(R.id.raffledate);
            raffleDate.setText( raffle.getDate() );
            return row;
        }

        public RaffleAdaptor(Context context, int resource, List<Raffle> objects)
        {
            super(context, resource, objects);
            this.mLayoutResourceID = resource;
        }

    @Nullable
    @Override
    public Raffle getItem(int position) {
        return super.getItem(position);
    }
}

