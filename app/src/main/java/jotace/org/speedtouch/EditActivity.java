package jotace.org.speedtouch;


import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.soundcloud.android.crop.Crop;

import java.io.File;


public class EditActivity extends AppCompatActivity {

    private ImageView contactImage;
    private static final String CONTACT_DATA = "CONTACT";
    private DatabaseHandler db = new DatabaseHandler(EditActivity.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_main);

        // Getting intent and data
        final Contact contact = (Contact) getIntent().getSerializableExtra(CONTACT_DATA);

        // Obtaining the font
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/Animated.ttf");

        // Setting custom font to main title
        TextView txtTitle = (TextView)findViewById(R.id.main_title);
        txtTitle.setTypeface(font);

        // Gettting ImageView
        contactImage = (ImageView) findViewById(R.id.contact_image);
        contactImage.setImageBitmap(ImageHelper.byteArrayToBitmap(contact.getImage()));

        contactImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contactImage.setImageDrawable(null);
                Crop.pickImage(EditActivity.this);
            }
        });

        // Getting Image Selector Button
        Button selectImageBtn = (Button) findViewById(R.id.btn_select_image);

        selectImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contactImage.setImageDrawable(null);
                Crop.pickImage(EditActivity.this);
            }
        });

        // Getting Edit Texts
        final EditText nameEditText = (EditText) findViewById(R.id.contact_name);
        nameEditText.setText((CharSequence) contact.getName());

        final EditText numberEditText = (EditText) findViewById(R.id.contact_phone);
        numberEditText.setText((CharSequence) contact.getNumber());

        //Getting Save Button
        Button saveBtn = (Button) findViewById(R.id.btn_save);
        saveBtn.setTypeface(font);

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String contactName = nameEditText.getText().toString().trim();
                String contactNumber = numberEditText.getText().toString().trim();

                if (contactName.isEmpty() || contactNumber.isEmpty()) {
                    Snackbar.make(v, R.string.save_contact_validation, Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                } else {
                    v.setClickable(false);
                    contact.setName(contactName);
                    contact.setNumber(contactNumber);

                    contactImage.buildDrawingCache();
                    Bitmap imageBitmap = contactImage.getDrawingCache();
                    contact.setImage(ImageHelper.bitmapToByteArray(imageBitmap));

                    db.updateContact(contact);

                    Snackbar.make(v, R.string.update_contact_successfully, Snackbar.LENGTH_SHORT)
                            .setAction("Action", null)
                            .setCallback(new Snackbar.Callback() {
                                @Override
                                public void onDismissed(Snackbar snackbar, int event) {
                                    super.onDismissed(snackbar, event);
                                    startActivity(new Intent(EditActivity.this, MainActivity.class));
                                }
                            }).show();
                }
            }
        });

        //Getting Delete Button
        Button deleteBtn = (Button) findViewById(R.id.btn_delete);
        deleteBtn.setTypeface(font);
        deleteBtn.setVisibility(View.VISIBLE);

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                // Confirmation Dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(EditActivity.this);
                // Add the buttons
                builder.setPositiveButton(R.string.dialog_yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        db.deleteContact(contact);

                        Snackbar.make(v, R.string.delete_contact_successfully, Snackbar.LENGTH_SHORT)
                                .setAction("Action", null)
                                .setCallback(new Snackbar.Callback() {
                                    @Override
                                    public void onDismissed(Snackbar snackbar, int event) {
                                        super.onDismissed(snackbar, event);
                                        startActivity(new Intent(EditActivity.this, MainActivity.class));
                                    }
                                }).show();
                    }
                })
                .setNegativeButton(R.string.dialog_no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                })
                        .setTitle(R.string.delete_contact)
                        .setMessage(R.string.delete_contact_confirmation)
                        .create().show();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent result) {
        if (requestCode == Crop.REQUEST_PICK && resultCode == RESULT_OK) {
            beginCrop(result.getData());
        } else if (requestCode == Crop.REQUEST_CROP) {
            handleCrop(resultCode, result);
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

}
