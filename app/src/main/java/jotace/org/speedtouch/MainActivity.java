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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ContactsAdapter adapter;
    private  ListView listView;
    private static final int ADD_NEW_CONTACT_RC = 1;
    private static final String CONTACT_DATA = "CONTACT";
    private DatabaseHandler db;
    ArrayList<Contact> contacts;

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


        // Getting Contacts Text filter
        EditText contactsFilter = (EditText)findViewById(R.id.contacts_filter);
        contactsFilter.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                System.out.println("Text ["+s+"]");

                adapter.getFilter().filter(s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });



        // ****** TEST CODE *****
        listView = (ListView) findViewById(R.id.contacts_list);

        Log.i("DATABASE", "Starting database operations...");
        db = new DatabaseHandler(this);

        Log.i("DATABASE", "Getting all contacts...");
        contacts = db.getAllContacts();

        // Setting up ContactsAdapter
        Resources res = getResources();
        if (!contacts.isEmpty()) {
            adapter = new ContactsAdapter(this, contacts, res);
        }

        listView.setEmptyView(findViewById(R.id.empty_list));
        listView.setAdapter(adapter);

        listView.setFocusableInTouchMode(true);
        listView.requestFocus();

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

        registerForContextMenu(listView);

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

        EditText searchFilter = (EditText) findViewById(R.id.contacts_filter);

        switch (id) {
            case R.id.enable_text_search:
                searchFilter.setVisibility(View.VISIBLE);
                break;

            case R.id.disable_text_search:
                searchFilter.setVisibility(View.GONE);
                break;

            default:
                searchFilter.setVisibility(View.GONE);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.contextual_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(final MenuItem item) {
        AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.edit_contact:
                Contact contactItem = (Contact) listView.getAdapter().getItem(((AdapterContextMenuInfo) item.getMenuInfo()).position);
                Intent intent = new Intent(MainActivity.this, EditActivity.class);
                intent.putExtra(CONTACT_DATA, (Serializable) contactItem);
                startActivity(intent);
                return true;

                default:
                return super.onContextItemSelected(item);
        }
    }


} // END
