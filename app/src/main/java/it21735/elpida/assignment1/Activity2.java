package it21735.elpida.assignment1;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class Activity2 extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2);

        final Button back_button = findViewById(R.id.back_button);
        final DB_Control controller = new DB_Control(Activity2.this);
        final TableLayout table = findViewById(R.id.table);

        final Intent intent = getIntent();
        //βάζω στη μεταβλητή message, τη λέξη που θέλει να ψάξει ο χρήστης
        String message=intent.getExtras().getString("searchWord");

        //Eμφάνιση των εγγραφών σε πίνακα
        ShowData(controller,table,message);

        back_button.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               setResult(RESULT_OK,intent);
               finish();
            }
       });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Toast.makeText(getApplicationContext(),"Thank you for using my app!",Toast.LENGTH_SHORT).show();
        finish();
    }

    //Βασίστηκα σε μια μέθοδο που βρήκα εδώ: https://tranngocnhat.blogspot.com/2016/06/android-loading-sqlite-data-into.html
    //και την προσάρμοσα στο πρόγραμμά μου
    private void ShowData(DB_Control controller,TableLayout table,String message) {

        controller.DB_open();

        Cursor c = controller.SelectQuery(message);

        int rows = c.getCount();
        int columns = c.getColumnCount();

        c.moveToFirst();

        for (int i = 0; i < rows; i++) {

            TableRow row = new TableRow(this);
            row.setGravity(Gravity.CENTER_HORIZONTAL);

            for (int j = 0; j < columns; j++) {

                    TextView tv = new TextView(this);

                    tv.setGravity(Gravity.CENTER);

                    tv.setText(c.getString(j));

                    row.addView(tv);
            }
            c.moveToNext();

            table.addView(row);

        }
        c.close();
        controller.DB_close();

    }


}
