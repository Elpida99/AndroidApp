package it21735.elpida.assignment1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class MainActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EditText fnameText = findViewById(R.id.fname);
        final EditText lnameText = findViewById(R.id.lname);
        final EditText ageText = findViewById(R.id.age);

        final Button submitButton = findViewById(R.id.submit_button);
        final Button searchButton = findViewById(R.id.search_button);

        final EditText searchText = findViewById(R.id.search);

        final DB_Control controller=new DB_Control(MainActivity.this);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Έλεγχος για το αν είναι κάποιο edittext κενό
                String FirstName = fnameText.getText().toString();
                String LasttName = lnameText.getText().toString();
                String Age = ageText.getText().toString();

                //Έλεγχος για κενά edittext
                if(FirstName.equals("") || LasttName.equals("") || Age.equals("")) {
                    if(FirstName.equals(""))
                        fnameText.setError("First name is required!");
                    if(LasttName.equals(""))
                        lnameText.setError("Last name is required!");
                    if(Age.equals(""))
                        ageText.setError("Age is required!");
                    Toast.makeText(MainActivity.this,"Please fill in all the blanks",Toast.LENGTH_SHORT).show();

                //Έλεχγος για τα ονόματα, ώστε να μην είναι μόνο 1 γράμμα
                }else if(FirstName.length()<2 || LasttName.length()<2) {
                    Toast.makeText(MainActivity.this, "Please write at least 2 letters!", Toast.LENGTH_SHORT).show();
                }else{
                    //Έλεγχος για το αν υπάρχει ήδη εγγραφή με ίδιο όνομα και ηλικία, για να μην μπορεί να πατήσει
                    //κάποιος το κουμπί submit δύο φορές
                    if(controller.Exists(Integer.parseInt(ageText.getText().toString()),fnameText.getText().toString(),lnameText.getText().toString())){
                        Toast toast = Toast.makeText(MainActivity.this,"You have already submitted these data!",Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER,0,0);
                        toast.show();
                    }else {
                        //Εισαγωγή δεδομένων στη βάση
                        controller.DB_open();
                        int id = controller.newID();
                        controller.insertData(id, fnameText, lnameText, ageText);

                        //Έλεγχος ότι αποθηκεύτηκαν τα δεδομένα στη βάση και μήνυμα επιβεβαίωσης
                        if(controller.Exists(Integer.parseInt(ageText.getText().toString()),fnameText.getText().toString(),lnameText.getText().toString())) {

                            Toast toast = Toast.makeText(MainActivity.this, "Your data have been submitted!", Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                        } else {
                            Toast toast = Toast.makeText(MainActivity.this,"There was a problem while submitting your data!",Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER,0,0);
                            toast.show();
                        }
                        controller.DB_close();
                    }

                }

            }

        });

//Αναζήτηση με βάση το πρώτο όνομα
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //φτιάχνω ένα string με τη λέξη που ψάχνει ο χρήστης
                String usertext = searchText.getText().toString();

                //Έλεγχος ότι το edittext δεν είναι κενό
                if (usertext.equals("")) {
                    searchText.setError("No input detected!");

                //Έλεγχος ότι ο χρήστης έγραψε πάνω από 1 γράμμα
                } else if (usertext.length() < 2) {
                    searchText.setError("Write at least 2 letters!");

                    //Έλεγχος ότι υπάρχει στη βάση
                } else {
                    if (controller.ExistsForSearch(usertext)) {

                        Intent i = new Intent();
                        i.setClassName("it21735.elpida.assignment1",
                                "it21735.elpida.assignment1.Activity2");
                        i.setAction(Intent.ACTION_VIEW);

                        i.putExtra("searchWord", usertext);

                        startActivityForResult(i, 5);
                    } else {
                        //Μήνυμα ότι δεν υπάρχει το όνομα στη βάση
                        Toast.makeText(MainActivity.this, "We could not find this name! Try another one or check for typos!", Toast.LENGTH_SHORT).show();
                    }
                }
            }

        });

    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
            if(requestCode==5){
                Toast.makeText(MainActivity.this,"Thank you for using my app!",Toast.LENGTH_SHORT).show();
            }
        }
    }

}
