package raffle.drawing;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MarignDrawing extends AppCompatActivity {

    final static String MAGINE_RAFFLE_WIN_NUMBER =" Magine Win Number";
    final static String DRAWING_RAFFLE_STRING ="Drawing Raffle";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marign_drawing);

        //Get the raffle
        Intent drawingInent = getIntent();
        final Raffle selectedRaffle  = (Raffle)drawingInent.getSerializableExtra(MainActivity.DRAWING_RAFFLE_STRING);
        int rafflid = selectedRaffle.getId();

        final EditText maringEditText = findViewById(R.id.marginticketNumber);

        Button confirmBT = findViewById(R.id.marginticketConfirm);
        confirmBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(maringEditText.getText().toString()=="")
                {
                    Toast.makeText(MarignDrawing.this, "Please input margin number", Toast.LENGTH_SHORT).show();

                }
                else {

                    int magin_win = Integer.parseInt(maringEditText.getText().toString());
                    Intent drawingDetail = new Intent(MarignDrawing.this, MarginDrawingDetail.class);

                    drawingDetail.putExtra(MAGINE_RAFFLE_WIN_NUMBER, magin_win);
                    drawingDetail.putExtra(DRAWING_RAFFLE_STRING, selectedRaffle);
                    startActivity(drawingDetail);
                }


            }
        });
    }
}
