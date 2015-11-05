package jotace.org.speedtouch;

import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ContactsAdapter adapter;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Seting Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Hidding Toolbar default text
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        // Obtaining the font
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/Animated.ttf");

        // Setting custom font to main title
        TextView txtTitle = (TextView)findViewById(R.id.main_title);
        txtTitle.setTypeface(font);

        // Setting FAB
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        // ****** TEST CODE *****
        listView = (ListView) findViewById(R.id.contacts_list);

        Log.i("DATABASE", "Starting database operations...");
        DatabaseHandler db = new DatabaseHandler(this);

        db.deleteAllContacts();

        if (db.getContactsCount() == 0) {

            ArrayList<Contact> data = new ArrayList<Contact>();
            Contact c = new Contact("Erasmus", "911", "A");
            data.add(c);
            c = new Contact("Batman", "911 11 111", "A");
            data.add(c);
            c = new Contact("Real Madrid", "767678787", "A");
            data.add(c);
            c = new Contact("Mama", "43535535", "A");
            data.add(c);
            c = new Contact("Julio", "4524524", "A");
            data.add(c);
            c = new Contact("Darlis", "4524524", "A");
            data.add(c);
            c = new Contact("Osito", "4524524", "A");
            data.add(c);
            c = new Contact("Bob Esponja", "4524524", "A");
            data.add(c);

            c = new Contact("Nirvana", "4524524", "A");
            data.add(c);

            for (Contact item: data) {
                Log.i("DATABASE", "Inserting item...");
                db.addContact(item);
            }
        }

        Log.i("DATABASE", "Getting all contacts...");
        ArrayList<Contact> contacts = db.getAllContacts();

        // Setting up ContactsAdapter
        Resources res = getResources();
        adapter = new ContactsAdapter(this, contacts, res);

        listView.setAdapter(adapter);
        // ***** END TEST CODE *****

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i("TEST", "*******************");

                Contact item = (Contact)listView.getAdapter().getItem(position);
                Log.i("ID", "ID: "+ item.getId());

                Snackbar.make(view, "ID: "+ item.getId(), Snackbar.LENGTH_SHORT).setAction("Action", null).show();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
