package it21735.elpida.assignment1;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.TextView;

public class DB_Control {

    private Context context;
    private DataOpenHelper dbhelper;
    private SQLiteDatabase database;

    //Constructor με το context σαν όρισμα όπως και ο DataOpenHelper
    public DB_Control(Context c){
        context = c;
    }

    public DB_Control DB_open(){
        dbhelper = new DataOpenHelper(context);
        database = dbhelper.getWritableDatabase();
        return this;
    }

    public void DB_close() {
        dbhelper.close();
    }
    

    public void insertData(int id,TextView fname,TextView lname,TextView age) {

        ContentValues values = new ContentValues();
        values.put(DataOpenHelper.KEY_ID, id);
        values.put(DataOpenHelper.KEY_FNAME, fname.getText().toString());
        values.put(DataOpenHelper.KEY_LNAME, lname.getText().toString());
        values.put(DataOpenHelper.KEY_AGE, Integer.parseInt(age.getText().toString()));
        database.insert(DataOpenHelper.TABLE, null, values);

    }

    public Cursor SelectQuery(String message) {
        String[] columns = new String[] { DataOpenHelper.KEY_ID, DataOpenHelper.KEY_FNAME,DataOpenHelper.KEY_LNAME,DataOpenHelper.KEY_AGE };
        //Cursor c = database.query(DataOpenHelper.TABLE, columns,DataOpenHelper.KEY_FNAME+" =?",new String[]{message}, null, null, null);
        Cursor c = database.query(DataOpenHelper.TABLE, columns,DataOpenHelper.KEY_FNAME+" LIKE ?",new String[]{message+"%"}, null, null, DataOpenHelper.KEY_ID);
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }


    //μέθοδος για να βρίσκω το αμέσως προηγούμενο id και να το αυξάνω κατά 1 για να είναι μοναδικό(δηλαδή το μεγαλύτερο που υπάρχει στις εγγραφές)
    public int newID() {
        Cursor c = database.rawQuery("SELECT MAX("+DataOpenHelper.KEY_ID+") FROM "+DataOpenHelper.TABLE+" ;",null);
        if (c != null) {
            c.moveToFirst();
        }
        int max=c.getInt(0);
        max++;
        return max;
    }

    //μέθοδος που ελέγχει αν υπάρχει η εγγραφή του χρήστη στη βάση, ώστε αν ξαναπατήσει ο χρήστης το κουμπί της υποβολής
    //να μην φτιαχτεί δεύτερη ίδια εγγραφή με νέο id
    public boolean Exists(int Age,String Fname,String Lname) {
        DB_open();
        String query = "SELECT * FROM " + DataOpenHelper.TABLE
                + " WHERE " + DataOpenHelper.KEY_FNAME+" = '"+ Fname
                +"' AND "+DataOpenHelper.KEY_LNAME+" = '"+ Lname
                +"' AND "+DataOpenHelper.KEY_AGE+" = "+Age+" ;";
        Cursor cursor = database.rawQuery(query,null);
        if(cursor.getCount() <= 0){
            cursor.close();
            DB_close();
            return false;
        }
        cursor.close();
        DB_close();
        return true;
    }

    //μέθοδος που ελέγχει αν υπάρχει εγγραφή με το όνομα που έδωσε ο χρήστης στη βάση
    public boolean ExistsForSearch(String Fname){
        DB_open();
        String[] columns = new String[] { DataOpenHelper.KEY_ID, DataOpenHelper.KEY_FNAME,DataOpenHelper.KEY_LNAME,DataOpenHelper.KEY_AGE };
        Cursor c = database.query(DataOpenHelper.TABLE, columns,DataOpenHelper.KEY_FNAME+" LIKE ?",new String[]{Fname+"%"}, null, null, null);
        if(c.getCount() <=0){
            c.close();
            DB_close();
            return false;
        }
        c.close();
        DB_close();
        return true;
    }

}
