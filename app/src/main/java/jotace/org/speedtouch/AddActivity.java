package jotace.org.speedtouch;


import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.soundcloud.android.crop.Crop;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class AddActivity extends AppCompatActivity {

    private ImageView contactImage;
    private EditText nameEditText;
    private EditText numberEditText;
    private static final int REQUEST_CODE_PICK_CONTACTS = 7;
    private Uri uriContact;
    private String contactID;     // contacts unique ID

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_main);

        // Obtaining the font
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/Animated.ttf");

        // Setting custom font to main title
        TextView txtTitle = (TextView)findViewById(R.id.main_title);
        txtTitle.setTypeface(font);

        // Getting Pick from Contacts Button
        Button pickFromContactsBtn = (Button) findViewById(R.id.btn_pick_from_contacts);
        pickFromContactsBtn.setTypeface(font);

        pickFromContactsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickContactFromContacts();
            }
        });

        // Gettting ImageView
        contactImage = (ImageView) findViewById(R.id.contact_image);

        contactImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contactImage.setImageDrawable(null);
                Crop.pickImage(AddActivity.this);
            }
        });

        // Getting Image Selector Button
        Button selectImageBtn = (Button) findViewById(R.id.btn_select_image);

        selectImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contactImage.setImageDrawable(null);
                Crop.pickImage(AddActivity.this);
            }
        });

        // Getting Edit Texts
        nameEditText = (EditText) findViewById(R.id.contact_name);
        numberEditText = (EditText) findViewById(R.id.contact_phone);

        //Getting Save Button
        Button saveBtn = (Button) findViewById(R.id.btn_save);
        saveBtn.setTypeface(font);

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String contactName = nameEditText.getText().toString().trim();
                String contactNumber = numberEditText.getText().toString().trim();

                if(contactName.isEmpty() || contactNumber.isEmpty()) {
                    Snackbar.make(v, R.string.save_contact_validation, Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                } else {
                    v.setClickable(false);
                    Contact contact = new Contact();
                    contact.setName(contactName);
                    contact.setNumber(contactNumber);

                    // Setting image
                    contactImage.buildDrawingCache();
                    Bitmap imageBitmap = contactImage.getDrawingCache();
                    contact.setImage(ImageHelper.bitmapToByteArray(imageBitmap));

                    DatabaseHandler db = new DatabaseHandler(AddActivity.this);
                    db.addContact(contact);

                   Snackbar.make(v, R.string.save_contact_successfully, Snackbar.LENGTH_SHORT)
                            .setAction("Action", null)
                            .setCallback(new Snackbar.Callback() {
                                @Override
                                public void onDismissed(Snackbar snackbar, int event) {
                                    super.onDismissed(snackbar, event);

                                    Intent intent = new Intent(AddActivity.this, MainActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                }
                            }).show();
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent result) {
        if (requestCode == Crop.REQUEST_PICK && resultCode == RESULT_OK) {
            beginCrop(result.getData());
        } else if (requestCode == Crop.REQUEST_CROP) {
            handleCrop(resultCode, result);
        } else if (requestCode == REQUEST_CODE_PICK_CONTACTS && resultCode == RESULT_OK) {
            uriContact = result.getData();
            retrieveContactNumber();
            retrieveContactName();
            retrieveContactPhoto();
        }
    }

    private void beginCrop(Uri source) {
        Uri destination = Uri.fromFile(new File(getCacheDir(), "cropped"));
        Crop.of(source, destination).asSquare().start(this);
    }

    private void handleCrop(int resultCode, Intent result) {
        if (resultCode == RESULT_OK) {
            contactImage.setImageURI(Crop.getOutput(result));
        } else if (resultCode == Crop.RESULT_ERROR) {
            Toast.makeText(this, Crop.getError(result).getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void pickContactFromContacts(){
        Intent pickFromContactsIntent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(pickFromContactsIntent, REQUEST_CODE_PICK_CONTACTS);
    }


    private void retrieveContactName() {
        String contactName = null;

        // Querying contact data store
        Cursor cursor = getContentResolver().query(uriContact, null, null, null, null);

        if (cursor.moveToFirst()) {
            // DISPLAY_NAME = The display name for the contact.
            contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
        }

        // Setting the Name on the Edit Text
        nameEditText.setText(contactName);

        cursor.close();
    }

    private void retrieveContactNumber() {

        String contactNumber = null;

        // getting contacts ID
        Cursor cursorID = getContentResolver().query(uriContact, new String[]{ContactsContract.Contacts._ID}, null, null, null);

        if (cursorID.moveToFirst()) {

            contactID = cursorID.getString(cursorID.getColumnIndex(ContactsContract.Contacts._ID));
        }

        cursorID.close();

        // Using the contact ID now we will get contact phone number
        Cursor cursorPhone = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER},

                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ? AND " +
                        ContactsContract.CommonDataKinds.Phone.TYPE + " = " +
                        ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE,

                new String[]{contactID},
                null);

        if (cursorPhone.moveToFirst()) {
            contactNumber = cursorPhone.getString(cursorPhone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
        }

        // Setting the Phone Number on the Edit Text
        numberEditText.setText(contactNumber);

        cursorPhone.close();
    }

    private void retrieveContactPhoto() {

        Bitmap photo = null;

        try {
            InputStream inputStream = ContactsContract.Contacts.openContactPhotoInputStream(getContentResolver(),
                    ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, new Long(contactID)));

            if (inputStream != null) {
                photo = BitmapFactory.decodeStream(inputStream);
                 contactImage.setImageBitmap(photo);
            }

            assert inputStream != null;
            inputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }


} // END
