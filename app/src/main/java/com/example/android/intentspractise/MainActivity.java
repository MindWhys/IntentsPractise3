package com.example.android.intentspractise;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.AlarmClock;
import android.provider.CalendarContract;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.actions.NoteIntents;
import com.google.android.gms.actions.ReserveIntents;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;

import static android.provider.CalendarContract.EXTRA_EVENT_BEGIN_TIME;
import static android.provider.CalendarContract.EXTRA_EVENT_END_TIME;

public class MainActivity extends AppCompatActivity {
    /**
     * This method accesses the phones camera and gets a photo back
     */
    public class GenericFileProvider extends FileProvider {

    }
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_SELECT_PHONE_NUMBER = 2;
    static final int REQUEST_IMAGE_GET = 3;
    private static final int REQUEST_CALL = 1;
    Intent callIntent;
    Button mCallButton;
    private ImageView mImageView;
    private TextView contact_number;
    private ImageView photoFromStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mImageView = (ImageView) findViewById(R.id.mImageView);
        contact_number = (TextView) findViewById(R.id.contact_number);
        photoFromStorage = (ImageView) findViewById(R.id.photoFromStorage);
        init();
    }

    private void init() {
        mCallButton = (Button) findViewById(R.id.phone);
        mCallButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:01234567891"));
                if (ContextCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    android.support.v4.app.ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL);
                } else {
                    startActivity(callIntent);
                }
            }
        });
    }

    /**
     * A method that uses the "ACTION_SET_ALARM" intent to set an alarm on your phone
     */

    public void createAlarm(View view) {
        //This creates an ArrayList called alarmDays which is passed into the .EXTRA_DAYS method
        ArrayList<Integer> alarmDays = new ArrayList<>();
        //This sets Monday as one of the alarm days
        alarmDays.add(2);
        //This sets Tuesday as one of the alarm days
        alarmDays.add(3);
        //This sets Wednesday as one of the alarm days
        alarmDays.add(4);
        //How to set up a new Alarm intent
        Intent intent = new Intent(AlarmClock.ACTION_SET_ALARM)
                //This sets which days the alarm goes off on using the ArrayList above
                .putExtra(AlarmClock.EXTRA_DAYS, alarmDays)
                //This sets the name or message of the alarm
                .putExtra(AlarmClock.EXTRA_MESSAGE, "Wake up!")
                //This sets the hour of the alarm (24 hour) to 7am
                .putExtra(AlarmClock.EXTRA_HOUR, 7)
                // This sets the minutes of the alarm to 30
                .putExtra(AlarmClock.EXTRA_MINUTES, 30)
                // This sets the alarm to (false=not) vibrate
                .putExtra(AlarmClock.EXTRA_VIBRATE, false)
                // This is meant to make the alarm be silent but I don't think it works...
                .putExtra(AlarmClock.EXTRA_RINGTONE, "VALUE_RINGTONE_SILENT");
        // To show the Alarm app after adding an alarm, uncomment the line below:
        //intent.putExtra(AlarmClock.EXTRA_SKIP_UI, true);

        // This line first checks that your phone has an app that can handle the intent before
        // starting so the app doesn't crash if your phone doesn't an an appropriate app
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    /**
     * A method that uses the "ACTION_SENDTO" intent to set up sending an email using an email app on an Android phone
     */
    public void sendEmail(View view) {

        // How to set up a new email intent
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        //This line ensures only email apps are used as opposed to text or messenger apps being included.
        intent.setData(Uri.parse("mailto:")); // only email apps should handle this
        // Optional line of code to add text to the subject line of email
        intent.putExtra(Intent.EXTRA_SUBJECT, "Got Mail");
        // Optional line of code to add text to the body of the email
        intent.putExtra(Intent.EXTRA_TEXT, "This is the message.");
        // This set up a string array containing an email address is sent to
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"example@gmail.com"});
        // This string array contains two email addresses to add to the CC part of email
        intent.putExtra(Intent.EXTRA_CC, new String[]{"examplecc@gmail.com", "examplecc2@gmail.com"});
        // This string array has put each email on a different line
        // but functions the same to add to the BCC part of email
        intent.putExtra(Intent.EXTRA_BCC, new String[]{
                "examplebcc@gmail.com",
                "examplebcc2@gmail.com"});
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    /**
     * A method that uses the "ACTION_INSERT" intent to set up a calendar event on an Android phone
     */
    public void createCalendar(View view) {
        //If you uncomment the following line you intialise the boolean "allDayEvent" to true
        //boolean allDayEvent = true;

        //The next two lines establish the beginTime of the event by year, month, date, hourOfDay and minute
        Calendar beginTime = Calendar.getInstance();
        beginTime.set(2018, 0, 19, 18, 0);
        //The next two lines establish the endTime of the event by year, month, date, hourOfDay and minute
        Calendar endTime = Calendar.getInstance();
        endTime.set(2018, 0, 19, 20, 30);
        Intent intent = new Intent(Intent.ACTION_INSERT)
                .setData(CalendarContract.Events.CONTENT_URI)
                //If you uncomment the follwing line and the boolean line above the event is set to all day
                //.putExtra(EXTRA_EVENT_ALL_DAY, allDayEvent);

                //This line sets the start time of the event using data from beginTime above
                .putExtra(EXTRA_EVENT_BEGIN_TIME, beginTime.getTimeInMillis())
                //This line sets the finish time of the event using data from endTime above
                .putExtra(EXTRA_EVENT_END_TIME, endTime.getTimeInMillis())
                //This populates the name section of the calendar event
                .putExtra(CalendarContract.Events.TITLE, "Udacity Google Developers Meetup")
                //This populates the description section of the calendar event
                .putExtra(CalendarContract.Events.DESCRIPTION, "Coding Group")
                //This populates the location section of the calendar event
                .putExtra(CalendarContract.Events.EVENT_LOCATION, "Code Node")
                //This populates the availablility of the event
                .putExtra(CalendarContract.Events.AVAILABILITY, CalendarContract.Events.AVAILABILITY_BUSY)
                //This makes it so you send emails to attendees within this list
                .putExtra(Intent.EXTRA_EMAIL, "rowan@example.com,trevor@example.com");
        // This line first checks that your phone has an app that can handle the intent before
        // starting so the app doesn't crash if your phone doesn't an an appropriate app
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    public void takePhoto(View view) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    public void getContact(View view) {
        // Start an activity for the user to pick a phone number from contacts
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, REQUEST_SELECT_PHONE_NUMBER);
        }
    }

    public void fileStorage(View view) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, REQUEST_IMAGE_GET);
        }
    }

    public void getTaxi(View view) {
        Intent intent = new Intent(ReserveIntents.ACTION_RESERVE_TAXI_RESERVATION);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            Toast.makeText(this, "No App Available", Toast.LENGTH_SHORT).show();
        }
    }

    public void showMap(View view) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("geo:0,0?q=Big%20Ben%2C+UK"));
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    public void playMedia(View view) {
        File musicFile2Play = new File("/storage/external_SD/media/Silverchair/Frogstomp/02 Tomorrow.mp3");
        Intent intent = new Intent();
        intent.setAction(android.content.Intent.ACTION_VIEW);
        intent.setDataAndType(FileProvider.getUriForFile(MainActivity.this, BuildConfig.APPLICATION_ID + ".provider",(musicFile2Play)));
        startActivity(intent);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    public void newNote(View view) {
        Intent intent = new Intent(NoteIntents.ACTION_CREATE_NOTE)
                .putExtra(NoteIntents.EXTRA_NAME, "Title")
                .putExtra(NoteIntents.EXTRA_TEXT, "This is the main body of the note");
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            Toast.makeText(this, "No App Available", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CALL: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startActivity(callIntent);
                }
            }
        }
    }

    public void newSetting(View view) {
        //Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
        Intent intent = new Intent(Settings.ACTION_SETTINGS);
        //Intent intent = new Intent(Settings.ACTION_AIRPLANE_MODE_SETTINGS);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    public void sendMessage(View view) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("smsto:012345678910"));
        intent.putExtra("sms_body", "Hello World");
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    public void webBrowser(View view) {
        Uri webpage = Uri.parse("https://eu.udacity.com/");
        Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    /**
     * This method uses the image data sent from file storage and sends it to an ImageView
     *
     * @param requestCode checks that REQUEST_IMAGE_GET has been returned
     * @param resultCode  checks that the User did select some information
     *                    (here a photo image file) to be returned
     * @param data        is the photo image file itself
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // This line checks that the user did select a file to send to the app
        if (requestCode == REQUEST_IMAGE_GET && resultCode == RESULT_OK) {
            // This line gets the photo image file and stores it in a variable
            Uri fullPhotoUri = data.getData();
            // This line takes the file and puts it into an ImageView with an id photoFromStorage
            photoFromStorage.setImageURI(fullPhotoUri);
        }

        /**
         * This method uses the data sent from contacts and sends it to a TextView
         * @param requestCode checks that REQUEST_SELECT_PHONE_NUMBER has been returned
         * @param resultCode checks that the User did select some information
         *                  (here a phone number) to be returned
         * @param data is the phone number itself
         */
        if (requestCode == REQUEST_SELECT_PHONE_NUMBER && resultCode == RESULT_OK) {
            // Get the URI and query the content provider for the phone number
            Uri contactUri = data.getData();
            String[] projection = new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER};
            Cursor cursor = getContentResolver().query(contactUri, projection,
                    null, null, null);
            // If the cursor returned is valid, get the phone number
            if (cursor != null && cursor.moveToFirst()) {
                int numberIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                String number = cursor.getString(numberIndex);
                // Send the phone number to the TextView with id contact_number
                contact_number.setText(number);
            }
        }
        /**
         * This method uses the photo thumbnail sent from the camera and sends it to an ImageView
         * @param requestCode checks that REQUEST_IMAGE_CAPTURE has been returned
         * @param resultCode checks that the User did take and select a photo
         * @param data is the photo thumbnail (as a Bitmap) itself
         */
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            //This line gets the photo data
            Bundle extras = data.getExtras();
            //This line stores the photo data as a Bitmap variable
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            // This line sends the photo data to an ImageView so it can be seen
            mImageView.setImageBitmap(imageBitmap);
        }
    }
}
