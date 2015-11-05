package jotace.org.speedtouch;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DatabaseHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "speedTouchContacts";

    // Contacts table name
    private static final String CONTACTS_TABLE = "contacts";

    // Contacts Table Columns names
    private static final String ID = "id";
    private static final String NAME = "name";
    private static final String NUMBER = "number";
    private static final String IMAGE = "image";


    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + CONTACTS_TABLE + "("
                + ID + " INTEGER PRIMARY KEY NOT NULL,"
                + NAME + " TEXT NOT NULL,"
                + NUMBER + " TEXT NOT NULL,"
                + IMAGE + " TEXT NOT NULL" + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + CONTACTS_TABLE);

        // Create tables again
        onCreate(db);
    }

    // Adding new contact
    public void addContact(Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(NAME, contact.getName());
        values.put(NUMBER, contact.getNumber());
        values.put(IMAGE, contact.getImage());


        db.insert(CONTACTS_TABLE, null, values);
        db.close();
    }

    // Getting single contact
    public Contact getContact(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(CONTACTS_TABLE, new String[] { ID, NAME, NUMBER, IMAGE }, ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Contact contact = new Contact(Integer.parseInt(cursor.getString(0)), cursor.getString(1),
                cursor.getString(2), cursor.getString(3));

        db.close();
        cursor.close();
        return contact;
    }

    // Getting All Contacts
    public ArrayList<Contact> getAllContacts() {
        ArrayList<Contact> contactList = new ArrayList<Contact>();

        String selectQuery = "SELECT  * FROM " + CONTACTS_TABLE;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Contact contact = new Contact();
                contact.setId(Integer.parseInt(cursor.getString(0)));
                contact.setName(cursor.getString(1));
                contact.setNumber(cursor.getString(2));
                contact.setImage(cursor.getString(3));

                contactList.add(contact);
            } while (cursor.moveToNext());
        }

        db.close();
        cursor.close();
        return contactList;
    }

    // Getting contacts Count
    public int getContactsCount() {
        String countQuery = "SELECT  * FROM " + CONTACTS_TABLE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int res = cursor.getCount();
        db.close();
        cursor.close();
        return res;
    }
    // Updating single contact
    public int updateContact(Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(NAME, contact.getName());
        values.put(NUMBER, contact.getNumber());
        values.put(IMAGE, contact.getImage());

        // updating row
        int res = db.update(CONTACTS_TABLE, values, ID + " = ?",
                new String[]{String.valueOf(contact.getId())});

        db.close();
        return res;
    }

    // Deleting single contact
    public void deleteContact(Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(CONTACTS_TABLE, ID + " = ?",
                new String[] { String.valueOf(contact.getId()) });
        db.close();
    }

    // Deleting all contacts
    public void deleteAllContacts() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(CONTACTS_TABLE, null, null);
        db.close();
    }

} // END
