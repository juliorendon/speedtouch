package jotace.org.speedtouch;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
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

    private ContactsAdapter adapter;
    private  ListView listView;
    static final int ADD_NEW_CONTACT_RC = 1;

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
                startActivity(new Intent(MainActivity.this, AddActivity.class));
            }
        });

        // ****** TEST CODE *****
        listView = (ListView) findViewById(R.id.contacts_list);

        Log.i("DATABASE", "Starting database operations...");
        DatabaseHandler db = new DatabaseHandler(this);

/*
        db.deleteAllContacts();

        Bitmap imageBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.recio);
        byte[] imageByteArray = ImageHelper.bitmapToByteArray(imageBitmap);


        if (db.getContactsCount() == 0) {

            ArrayList<Contact> data = new ArrayList<Contact>();
            Contact c = new Contact("Erasmus", "911", imageByteArray);
            data.add(c);
            c = new Contact("Batman", "911 11 111", imageByteArray);
            data.add(c);
            c = new Contact("Real Madrid", "767678787", imageByteArray);
            data.add(c);
            c = new Contact("Mama", "43535535", imageByteArray);
            data.add(c);
            c = new Contact("Julio Cesar Rendon Ramirez", "4524524", imageByteArray);
            data.add(c);
            c = new Contact("Darlis Bracho Tudares", "4524524", imageByteArray);
            data.add(c);
            c = new Contact("Osito", "4524524", imageByteArray);
            data.add(c);
            c = new Contact("Bob Esponja", "4524524", imageByteArray);
            data.add(c);

            c = new Contact("Nirvana y sus Amigos", "4524524", imageByteArray);
            data.add(c);

            for (Contact item: data) {
                Log.i("DATABASE", "Inserting item...");
                db.addContact(item);
            }
        }
        */

        Log.i("DATABASE", "Getting all contacts...");
        ArrayList<Contact> contacts = db.getAllContacts();

        // Setting up ContactsAdapter
        Resources res = getResources();
        if (!contacts.isEmpty()) {
            adapter = new ContactsAdapter(this, contacts, res);
        }


        listView.setEmptyView(findViewById(R.id.empty_list));
        listView.setAdapter(adapter);
        // ***** END TEST CODE *****

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (Build.VERSION.SDK_INT >= 23 && (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED)) {
                    return;
                }

                Contact item = (Contact)listView.getAdapter().getItem(position);

                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + item.getNumber()));
                startActivity(callIntent);
                
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
