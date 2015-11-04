package jotace.org.speedtouch;

import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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

        ArrayList<Contact> data = new ArrayList<Contact>();
        Contact c = new Contact("A", "Erasmus", "911");
        data.add(c);
        c = new Contact("A", "Batman", "911 11 111");
        data.add(c);
        c = new Contact("A", "Real Madrid", "767678787");
        data.add(c);
        c = new Contact("A", "Mama", "43535535");
        data.add(c);
        c = new Contact("A", "Julio", "4524524");
        data.add(c);
        c = new Contact("A", "Darlis", "4524524");
        data.add(c);
        c = new Contact("A", "Osito", "4524524");
        data.add(c);
        c = new Contact("A", "Bob Esponja", "4524524");
        data.add(c);

        // Setting up ContactsAdapter
        Resources res = getResources();
        adapter = new ContactsAdapter(this, data, res);

        listView.setAdapter(adapter);
        // ***** END TEST CODE *****

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
