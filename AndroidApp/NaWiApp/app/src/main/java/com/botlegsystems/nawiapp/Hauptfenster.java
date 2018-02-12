package com.botlegsystems.nawiapp;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.samples.vision.face.multitracker.*;
import com.radiusnetworks.proximity.KitConfig;
import com.radiusnetworks.proximity.ProximityKitBeacon;
import com.radiusnetworks.proximity.ProximityKitBeaconRegion;
import com.radiusnetworks.proximity.ProximityKitGeofenceRegion;
import com.radiusnetworks.proximity.ProximityKitManager;
import com.radiusnetworks.proximity.ProximityKitMonitorNotifier;
import com.radiusnetworks.proximity.ProximityKitRangeNotifier;
import com.radiusnetworks.proximity.ProximityKitSyncNotifier;
import com.radiusnetworks.proximity.model.KitBeacon;
import com.radiusnetworks.proximity.model.KitOverlay;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Hauptfenster extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        ProximityKitMonitorNotifier,
        ProximityKitRangeNotifier,
        ProximityKitSyncNotifier {

    String QR_CODE = "256";

    /**
     * Custom metadata key specific to the associated kit
     */
    @NonNull
    private static final String MAIN_OFFICE_LOCATION = "main-office";

    /**
     * General logging tag
     */
    private static final String TAG = "PKReferenceApplication";

    /**
     * Singleton storage for an instance of the manager
     */
    private static volatile ProximityKitManager pkManager;

    /**
     * Object to use as a thread-safe lock
     */
    @NonNull
    private static final Object SHARED_LOCK = new Object();

    /**
     * Proximity Kit Configuration
     */
    @NonNull
    private static final KitConfig KIT_CONFIG = new KitConfig(loadConfig());

    /**
     * Flag for tracking if the app was started in the background.
     */
    private boolean haveDetectedBeaconsSinceBoot = false;

    private boolean beaconDetectionActive = false;

    private static final int PERMISSION_REQUEST_COARSE_LOCATION = 1;

    private Date savedTime;
    private int savedKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_hauptfenster);

        savedTime = Calendar.getInstance().getTime();
        savedKey = -1;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // Android M Permission check
            if (checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("This app needs location access");
                builder.setMessage("Please grant location access so this app can detect beacons in the background.");
                builder.setPositiveButton(android.R.string.ok, null);
                builder.setOnDismissListener(
                        new DialogInterface.OnDismissListener() {
                            @TargetApi(23)
                            @Override
                            public void onDismiss(DialogInterface dialogInterface) {
                                requestPermissions(
                                        new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION},
                                        PERMISSION_REQUEST_COARSE_LOCATION
                                );
                            }
                        }
                );
                builder.show();
            }
        }

        /*
         * The app is responsible for handling the singleton instance of the Proximity Kit manager.
         * To ensure we have a single instance we synchronize our creation process.
         *
         * While this is not necessary inside an `Application` subclass it is necessary if the
         * single manager instance is created inside an `Activity` or other Android/Java component.
         * We're including the pattern here to show a method of ensuring a singleton instance.
         */
        if (null == pkManager) {
            synchronized (SHARED_LOCK) {
                if (null == pkManager) {
                    pkManager = ProximityKitManager.getInstance(this, KIT_CONFIG);
                }
            }
        }

        /* ----- begin code only for debugging ---- */

        pkManager.debugOn();

        /* ----- end code only for debugging ------ */

        /*
        /*
         * Set desired callbacks before calling `start()`.
         *
         * We can set these notifications after calling `start()`. However, this means we will miss
         * any notifications posted in the time between those actions.
         *
         * You are free to set only the notifiers you want callbacks for. We are setting all of them
         * to demonstrate how each set works.
         */
        pkManager.setProximityKitSyncNotifier(this);
        pkManager.setProximityKitMonitorNotifier(this);
        pkManager.setProximityKitRangeNotifier(this);

        /*
         * Now that we potentially have geofences setup and our notifiers are registered, we are
         * ready to start Proximity Kit.
         *
         * We could start it right now with:
         *
         *      pkManager.start();
         *
         * Instead we are letting the user decide when to start or stop in the UI.
         */

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        drawer.closeDrawer(Gravity.START);



        int navigationBarHeight = getNavigationBarHeight();

        /* adapt the image to the size of the display */
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        Bitmap bmp = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
                getResources(),R.drawable.map01),size.x,size.y - navigationBarHeight,true);

        /* fill the background ImageView with the resized image */
        ImageView iv_background = (ImageView) findViewById(R.id.background);
        iv_background.setImageBitmap(bmp);
        iv_background.setPadding(0, navigationBarHeight, 0, 0);

        Bitmap bmp2 = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
                getResources(), R.drawable.beacon_color), 96, 96, true);

        BitmapDrawable bitmapDrawable = new BitmapDrawable(getResources(), bmp2);
        ImageButton station1 = new ImageButton(this);
        station1.setBackground(bitmapDrawable);

        RelativeLayout.LayoutParams layoutParams;
        int button_width = size.y / 20;

        layoutParams = new RelativeLayout.LayoutParams(button_width, button_width);
        layoutParams.setMargins(10 * size.x / 28, size.y - size.y / 5, 0, 0);

        station1.setLayoutParams(layoutParams);

        station1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(Hauptfenster.this, Naturschutzzentrum.class);
                startActivity(i);
            }
        });

        TextView station1_text = new TextView(this);
        station1_text.setText("Naturschutzzentrum");

        station1_text.setTextColor(Color.parseColor("#FFFFFF"));
        station1_text.setTypeface(null, Typeface.BOLD);

        layoutParams = new RelativeLayout.LayoutParams(8*button_width, button_width);
        layoutParams.setMargins(7 * size.x / 28, size.y - size.y / 4, 0, 0);

        station1_text.setLayoutParams(layoutParams);

        // Station 2

        ImageButton station2 = new ImageButton(this);
        station2.setBackground(bitmapDrawable);

        layoutParams = new RelativeLayout.LayoutParams(button_width, button_width);
        layoutParams.setMargins(34 * size.x / 56, size.y - 2*size.y / 11, 0, 0);

        station2.setLayoutParams(layoutParams);

        station2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(Hauptfenster.this, Entstehung.class);
                startActivity(i);
            }
        });

        TextView station2_text = new TextView(this);
        station2_text.setText("Entstehung");

        station2_text.setTextColor(Color.parseColor("#FFFFFF"));
        station2_text.setTypeface(null, Typeface.BOLD);

        layoutParams = new RelativeLayout.LayoutParams(8*button_width, button_width);
        layoutParams.setMargins(16 * size.x / 28, size.y - 17*size.y / 72, 0, 0);

        station2_text.setLayoutParams(layoutParams);

        // Station 3

        ImageButton station3 = new ImageButton(this);
        station3.setBackground(bitmapDrawable);

        layoutParams = new RelativeLayout.LayoutParams(button_width, button_width);
        layoutParams.setMargins(31 * size.x / 56, size.y - 9*size.y / 22, 0, 0);

        station3.setLayoutParams(layoutParams);

        station3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(Hauptfenster.this, Torfstechen.class);
                startActivity(i);
            }
        });

        TextView station3_text = new TextView(this);
        station3_text.setText("Torfstechen");

        station3_text.setTextColor(Color.parseColor("#FFFFFF"));
        station3_text.setTypeface(null, Typeface.BOLD);

        layoutParams = new RelativeLayout.LayoutParams(8*button_width, button_width);
        layoutParams.setMargins(29 * size.x / 56, size.y - 10*size.y / 22, 0, 0);

        station3_text.setLayoutParams(layoutParams);

        // Station 4

        ImageButton station4 = new ImageButton(this);
        station4.setBackground(bitmapDrawable);

        layoutParams = new RelativeLayout.LayoutParams(button_width, button_width);
        layoutParams.setMargins(43 * size.x / 56, size.y - 15*size.y / 22, 0, 0);

        station4.setLayoutParams(layoutParams);

        station4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(Hauptfenster.this, Ueberwachsener_see.class);
                startActivity(i);
            }
        });

        TextView station4_text = new TextView(this);
        station4_text.setText("Überwachsener See");

        station4_text.setTextColor(Color.parseColor("#FFFFFF"));
        station4_text.setTypeface(null, Typeface.BOLD);

        layoutParams = new RelativeLayout.LayoutParams(8*button_width, button_width);
        layoutParams.setMargins(39 * size.x / 56, size.y - 14*size.y / 22, 0, 0);

        station4_text.setLayoutParams(layoutParams);

        // Station 5

        ImageButton station5 = new ImageButton(this);
        station5.setBackground(bitmapDrawable);

        layoutParams = new RelativeLayout.LayoutParams(button_width, button_width);
        layoutParams.setMargins(53 * size.x / 56, size.y - 33*size.y / 44, 0, 0);

        station5.setLayoutParams(layoutParams);

        station5.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(Hauptfenster.this, Grenzpfahl.class);
                startActivity(i);
            }
        });

        TextView station5_text = new TextView(this);
        station5_text.setText("Grenzpfahl");

        station5_text.setTextColor(Color.parseColor("#FFFFFF"));
        station5_text.setTypeface(null, Typeface.BOLD);

        layoutParams = new RelativeLayout.LayoutParams(8*button_width, button_width);
        layoutParams.setMargins(47 * size.x / 56, size.y - 33*size.y / 44, 0, 0);

        station5_text.setLayoutParams(layoutParams);

        // Station 6

        ImageButton station6 = new ImageButton(this);
        station6.setBackground(bitmapDrawable);

        layoutParams = new RelativeLayout.LayoutParams(button_width, button_width);
        layoutParams.setMargins(40 * size.x / 56, size.y - 39*size.y / 44, 0, 0);

        station6.setLayoutParams(layoutParams);

        station6.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(Hauptfenster.this, Hochmoor.class);
                startActivity(i);
            }
        });

        TextView station6_text = new TextView(this);
        station6_text.setText("Hochmoor\nEulenbruck");

        station6_text.setTextColor(Color.parseColor("#FFFFFF"));
        station6_text.setTypeface(null, Typeface.BOLD);

        layoutParams = new RelativeLayout.LayoutParams(8*button_width, 2*button_width);
        layoutParams.setMargins(38 * size.x / 56, size.y - 37*size.y / 44, 0, 0);

        station6_text.setLayoutParams(layoutParams);

        // Station 7

        ImageButton station7 = new ImageButton(this);
        station7.setBackground(bitmapDrawable);

        layoutParams = new RelativeLayout.LayoutParams(button_width, button_width);
        layoutParams.setMargins(20 * size.x / 56, size.y - 24*size.y / 44, 0, 0);

        station7.setLayoutParams(layoutParams);

        station7.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(Hauptfenster.this, Teichetuempel.class);
                startActivity(i);
            }
        });

        TextView station7_text = new TextView(this);
        station7_text.setText("Teiche &\nTümpel");

        station7_text.setTextColor(Color.parseColor("#FFFFFF"));
        station7_text.setTypeface(null, Typeface.BOLD);

        layoutParams = new RelativeLayout.LayoutParams(8*button_width, 2*button_width);
        layoutParams.setMargins(19 * size.x / 56, size.y - 28*size.y / 44, 0, 0);

        station7_text.setLayoutParams(layoutParams);

        RelativeLayout map_overlay = (RelativeLayout) findViewById(R.id.map_overlay);
        map_overlay.addView(station1);
        map_overlay.addView(station1_text);
        map_overlay.addView(station2);
        map_overlay.addView(station2_text);
        map_overlay.addView(station3);
        map_overlay.addView(station3_text);
        map_overlay.addView(station4);
        map_overlay.addView(station4_text);
        map_overlay.addView(station5);
        map_overlay.addView(station5_text);
        map_overlay.addView(station6);
        map_overlay.addView(station6_text);
        map_overlay.addView(station7);
        map_overlay.addView(station7_text);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nawi_pflanzen) {
            Intent i = new Intent(Hauptfenster.this, Pflanzen.class);
            startActivity(i);
        } else if (id == R.id.nawi_tiere) {
            Intent i = new Intent(Hauptfenster.this, Tiere.class);
            startActivity(i);
        } else if (id == R.id.nawi_umfrage) {
            Intent i = new Intent(Hauptfenster.this, Umfrage.class);
            startActivity(i);
        } else if (id == R.id.nawi_info) {
            Intent i = new Intent(Hauptfenster.this, Info.class);
            startActivity(i);
        } else if (id == R.id.qr_code) {
            // launch barcode activity.
            Intent intent = new Intent(this, MultiTrackerActivity.class);
            startActivityForResult(intent, 0);
        } else if(id == R.id.beacon) {
            if(beaconDetectionActive)
            {
                stopManager();
                beaconDetectionActive = false;
            }
            else
            {
                beaconDetectionActive = true;
                startManager();
            }

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(Gravity.START);
        return true;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {

                String contents = intent.getStringExtra("SCAN_RESULT");
                String format = intent.getStringExtra("SCAN_RESULT_FORMAT");
                if (format.equals(QR_CODE)) {
                    if(contents.contentEquals("Naturschutzzentrum")) {
                        Intent i = new Intent(Hauptfenster.this, Naturschutzzentrum.class);
                        startActivity(i);
                    } else if (contents.contentEquals("Entstehung")) {
                        Intent i = new Intent(Hauptfenster.this, Entstehung.class);
                        startActivity(i);
                    } else if (contents.contentEquals("Torfstechen")) {
                        Intent i = new Intent(Hauptfenster.this, Torfstechen.class);
                        startActivity(i);
                    } else if (contents.contentEquals("See")) {
                        Intent i = new Intent(Hauptfenster.this, Ueberwachsener_see.class);
                        startActivity(i);
                    } else if (contents.contentEquals("Grenzpfahl")) {
                        Intent i = new Intent(Hauptfenster.this, Grenzpfahl.class);
                        startActivity(i);
                    } else if (contents.contentEquals("Hochmoor")) {
                        Intent i = new Intent(Hauptfenster.this, Hochmoor.class);
                        startActivity(i);
                    } else if (contents.contentEquals("Teiche")) {
                        Intent i = new Intent(Hauptfenster.this, Teichetuempel.class);
                        startActivity(i);
                    } else {
                        Intent i = new Intent(Hauptfenster.this, Info.class);
                        startActivity(i);
                    }
                } else {
                    Toast.makeText(Hauptfenster.this, "Falsches Format!",
                            Toast.LENGTH_LONG).show();
                }

            } else if (resultCode == RESULT_CANCELED) {
                // To Handle cancel
                Log.i("App", "Konnte keinen QR Code scannen!");
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_COARSE_LOCATION: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG, "Lokalisierungszugriff verfügbar");
                } else {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Beacons nicht verwendbar");
                    builder.setMessage("Da kein Zugriff auf die Lokalisierungsdaten gewährleistet ist, können die Beacons der Stationen nicht erkannt werden.");
                    builder.setPositiveButton(android.R.string.ok, null);
                    builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            switchOnQRCode();
                        }
                    });
                    builder.show();
                }
                return;
            }
        }
    }

    private void switchOnQRCode()
    {

    }

    private void switchToStation(int key)
    {
        savedKey = key;
        savedTime = Calendar.getInstance().getTime();

        // the keys correlate to the id3 of the beacons, order in ascending order from the Naturschutzzentrum
        if(key == 4) {
            Intent i = new Intent(Hauptfenster.this, Naturschutzzentrum.class);
            startActivity(i);
        } else if (key == 5) {
            Intent i = new Intent(Hauptfenster.this, Entstehung.class);
            startActivity(i);
        } else if (key == 6) {
            Intent i = new Intent(Hauptfenster.this, Torfstechen.class);
            startActivity(i);
        } else if (key == 8) {
            Intent i = new Intent(Hauptfenster.this, Ueberwachsener_see.class);
            startActivity(i);
        } else if (key == 9) {
            Intent i = new Intent(Hauptfenster.this, Grenzpfahl.class);
            startActivity(i);
        } else if (key == 18) {
            Intent i = new Intent(Hauptfenster.this, Hochmoor.class);
            startActivity(i);
        } else if (key == 19) {
            Intent i = new Intent(Hauptfenster.this, Teichetuempel.class);
            startActivity(i);
        } else {
            Intent i = new Intent(Hauptfenster.this, Info.class);
            startActivity(i);
        }
    }

    private void handleBeacon(final ProximityKitBeacon beacon) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // You could instead call beacon.toString() which includes the identifiers
                int key = beacon.getId3().toInt();
                Date currentTime = Calendar.getInstance().getTime();
                if(currentTime.after(savedTime))
                {
                    // only recognize the same beacon after 3 minutes
                    if(currentTime.getTime() - savedTime.getTime() < 180*1000)
                    {
                        if(key != savedKey)
                        {
                            switchToStation(key);
                        }
                        else
                        {
                            savedTime = currentTime;
                        }
                    }
                    else
                    {
                        switchToStation(key);
                    }
                }
            }
        });
    }

    private int getNavigationBarHeight() {
        Resources resources = getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0) {
            return resources.getDimensionPixelSize(resourceId);
        }
        return 0;
    }

    /**
     * Verify that Google Play services is available.
     * <p/>
     * If the service is not available it could be due to several reasons. We take the easy way out
     * in this demo and simply log the error. We then use the utility class provided to pop a
     * notification to the end user with the message.
     * <p/>
     * Google Play services controls the text and content of this notification. We could roll our
     * own notification, display a dialog (which would require an Activity context), or do
     * something
     * else. This is why it is our (the app) responsibility to make this decision and not left up
     * to Proximity Kit.
     *
     * @return <code>true</code> if Google Play services is available, otherwise <code>false</code>
     * @see <a href="https://developer.android.com/google/play-services/setup.html">
     * Setup Google Play services
     * </a>
     */
    public boolean servicesConnected() {
        // Check that Google Play services is available
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);

        if (ConnectionResult.SUCCESS == resultCode) {
            Log.d(TAG, "Google Play services available");
            return true;
        }

        // Taking the easy way out: log it. Then let Google Play generate the appropriate action
        Log.w(TAG, GooglePlayServicesUtil.getErrorString(resultCode));
        PendingIntent nextAction = GooglePlayServicesUtil.getErrorPendingIntent(
                resultCode,
                this,
                0
        );

        // Make sure we have something to do
        if (nextAction == null) {
            Log.e(TAG, "Unable to determine action to handle Google Play Services error.");
        }

        // This isn't a crash worthy event
        try {
            nextAction.send(this, 0, new Intent());
        } catch (PendingIntent.CanceledException e) {
            Log.w(TAG, "Intent was canceled after we sent it.");
        } catch (NullPointerException npe) {
            // Likely on a mod without Google Play but log the exception to be safe
            Log.e(TAG, "Error occurred when trying to retrieve to Google Play Services.");
            npe.printStackTrace();
            displayFallbackGooglePlayDialog(resultCode);
        }
        return false;
    }

    /**
     * Displays an error dialog to the user.
     * <p/>
     * This manually attempts to display a dialog to the user. This is a fallback strategy if we
     * were unsuccessful using the pending intent. This may happen on rooted and modded phones
     * which cause exceptions when attempting to open the play store.
     * <p/>
     * If we are unable to display the activity now, because the main activity has not been created,
     * we delay and try again in one second. As soon as there is an activity we stop any more
     * attempt to display the error.
     *
     * @param resultCode    The code to provide to <code>GooglePlayServicesUtil</code> to tell it
     *                      which dialog message is needed.
     */
    private void displayFallbackGooglePlayDialog(final int resultCode) {
        final Handler handler = new Handler();
        final long oneSecond = 1000;

        Runnable runnable = new Runnable() {
            public void run() {
                displayGooglePlayErrorDialog(resultCode);
            }
        };
        handler.post(runnable);
    }

    /**
     * Display a dialog to the user explaining a Google Play service error.
     * <p/>
     * If there is no main activity for us to attach to we simply return. Otherwise, we try to get
     * the error dialog from <code>GooglePlayServiceUtil</code> so it can properly provide a
     * consistent experience for the user.
     * <p/>
     * If this fails, such as the device is a modded phone, we simply notify the user via a standard
     * dialog alert.
     *
     * @param resultCode    The code to provide to <code>GooglePlayServicesUtil</code> to tell it
     *                      which dialog message is needed.
     */
    private void displayGooglePlayErrorDialog(int resultCode){

        try {
            GooglePlayServicesUtil.getErrorDialog(resultCode, this, 0).show();
        } catch (Exception e) {
            //last resort
            new AlertDialog.Builder(this)
                    .setTitle("Missing Google Play Services")
                    .setMessage("Please visit the Google Play Store and install Google Play Services.")
                    .show();
        }
    }


    /***********************************************************************************************
     * START ProximityKitSyncNotifier
     **********************************************************************************************/

    @Override
    /**
     * Called when data has been sync'd with the Proximity Kit server.
     */
    public void didSync() {
        Log.i(TAG, "didSync(): Sycn'd with server");

        // Access every beacon configured in the kit, printing out the value of an attribute
        // named "myKey"
        for (KitBeacon beacon : pkManager.getKit().getBeacons()) {
            Log.d(
                    TAG,
                    "For beacon: " + beacon.getProximityUuid() + " " + beacon.getMajor() + " " +
                            beacon.getMinor() + ", the value of welcomeMessage is " +
                            beacon.getAttributes().get("welcomeMessage")
            );
        }

        // Access every geofence configured in the kit, printing out the value of an attribute
        // named "myKey"
        for (KitOverlay overlay : pkManager.getKit().getOverlays()) {
            Log.d(
                    TAG,
                    "For geofence: (" + overlay.getLatitude() + ", " + overlay.getLongitude() +
                            ") with radius " + overlay.getRadius() + ", the value of myKey is " +
                            overlay.getAttributes().get("myKey")
            );
        }
    }

    @Override
    /**
     * Called when syncing with the Proximity Kit server failed.
     *
     * @param e     The exception encountered while syncing
     */
    public void didFailSync(@NonNull Exception e) {
        Log.e(TAG, "didFailSync() called with exception: " + e);
    }

    /***********************************************************************************************
     * END ProximityKitSyncNotifier
     **********************************************************************************************/

    /***********************************************************************************************
     * START ProximityKitRangeNotifier
     * ********************************************************************************************/

    @Override
    /**
     * Called whenever the Proximity Kit manager sees registered beacons.
     *
     * @param beacons   a collection of <code>ProximityKitBeacon</code> instances seen in the most
     *                  recent ranging cycle.
     * @param region    The <code>ProximityKitBeaconRegion</code> instance that was used to start
     *                  ranging for these beacons.
     */
    public void didRangeBeaconsInRegion(@NonNull Collection<ProximityKitBeacon> beacons,
                                        @NonNull ProximityKitBeaconRegion region) {
        Log.d(
                TAG,
                "didRangeBeaconsInRegion: beacons=" + beacons + " region=" + region
        );
        if (beacons.size() == 0) {
            return;
        }

        Log.d(TAG, "didRangeBeaconsInRegion: size=" + beacons.size() + " region=" + region);

        for (ProximityKitBeacon beacon : beacons) {
            Log.d(
                    TAG,
                    "I have a beacon with data: " + beacon + " attributes=" +
                            beacon.getAttributes()
            );

            // We've wrapped up further behavior in some internal helper methods
            // Check their docs for details on additional things which you can do we beacon data
            displayBeacon(beacon);
        }
    }

    /***********************************************************************************************
     * END ProximityKitRangeNotifier
     **********************************************************************************************/

    /***********************************************************************************************
     * START ProximityKitMonitorNotifier
     **********************************************************************************************/

    @Override
    /**
     * Called when at least one beacon in a <code>ProximityKitBeaconRegion</code> is visible.
     *
     * @param region    an <code>ProximityKitBeaconRegion</code> which defines the criteria of
     *                  beacons being monitored
     */
    public void didEnterRegion(@NonNull ProximityKitBeaconRegion region) {
        // In this example, this class sends a notification to the user whenever an beacon
        // matching a Region (defined above) are first seen.
        Log.d(
                TAG,
                "[didEnterRegion] ENTER beacon region: " + region + " " +
                        region.getAttributes().get("welcomeMessage")
        );

        // Attempt to open the app now that we've entered a region if we started in the background
        tryAutoLaunch();

        // Notify the user that we've seen a beacon
        sendNotification(region);
    }

    @Override
    /**
     * Called when no more beacons in a <code>ProximityKitBeaconRegion</code> are visible.
     *
     * @param region    an <code>ProximityKitBeaconRegion</code> that defines the criteria of
     *                  beacons being monitored
     */
    public void didExitRegion(@NonNull ProximityKitBeaconRegion region) {
        Log.d(TAG, "[didExitRegion] EXIT beacon region: " + region);
    }

    @Override
    /**
     * Called when a the state of a <code>Region</code> changes.
     *
     * @param state     set to <code>ProximityKitMonitorNotifier.INSIDE</code> when at least one
     *                  beacon in a <code>ProximityKitBeaconRegion</code> is now visible; set to
     *                  <code>ProximityKitMonitorNotifier.OUTSIDE</code> when no more beacons in the
     *                  <code>ProximityKitBeaconRegion</code> are visible
     * @param region    an <code>ProximityKitBeaconRegion</code> that defines the criteria of
     *                  beacons being monitored
     */
    public void didDetermineStateForRegion(int state, @NonNull ProximityKitBeaconRegion region) {
        Log.d(TAG, "didDeterineStateForRegion called with state: " + state + "\tregion: " + region);

        switch (state) {
            case ProximityKitMonitorNotifier.INSIDE:
                String welcomeMessage = region.getAttributes().get("welcomeMessage");
                if (welcomeMessage != null) {
                    Log.d(TAG, "Beacon " + region + " says: " + welcomeMessage);
                }
                break;
            case ProximityKitMonitorNotifier.OUTSIDE:
                String goodbyeMessage = region.getAttributes().get("goodbyeMessage");
                if (goodbyeMessage != null) {
                    Log.d(TAG, "Beacon " + region + " says: " + goodbyeMessage);
                }
                break;
            default:
                Log.d(TAG, "Received unknown state: " + state);
                break;
        }
    }

    /**
     * App helper method to notify an activity when we see a beacon.
     *
     * @param beacon
     *         <code>org.altbeacon.beacon.Beacon</code> instance of the
     *         beacon seen
     */
    private void displayBeacon(ProximityKitBeacon beacon) {
        if (beacon == null) {
            return;
        }
        // We've elected to notify our only view of the beacon and a message to display
        handleBeacon(beacon);
    }

    /**
     * Turn the Proximity Kit manager on and update the UI accordingly.
     */
    private void startManager() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        MenuItem btn = (MenuItem) navigationView.getMenu().findItem(R.id.beacon);

        pkManager.start();
        btn.setTitle(R.string.manager_toggle_stop);
    }

    /**
     * Turn the Proximity Kit manager off and update the UI accordingly.
     */
    private void stopManager() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        MenuItem btn = (MenuItem) navigationView.getMenu().findItem(R.id.beacon);

        pkManager.stop();
        btn.setTitle(R.string.manager_toggle_start);
    }

    /**
     * App helper method to force Proximity Kit to sync.
     * <p/>
     * The Proximity Kit manager should automatically sync every hour, however, we can force an
     * ad-hoc sync anytime we want. This demonstrates how to do that.
     */
    private void forceSync() {
        Log.d(TAG, "Forcing a sync with the Proximity Kit server");
        pkManager.sync();
    }

    /**
     * Generate the app's Proximity Kit configuration.
     */
    private static Map<String, String> loadConfig() {
        Map<String, String> settings = new HashMap<>();
        settings.put(
                KitConfig.CONFIG_API_URL,
                "https://proximitykit.radiusnetworks.com/api/kits/8447"
        );
        settings.put(
                KitConfig.CONFIG_API_TOKEN,
                //"798b43980f334a164b228bb2f028337c2df04aee0ab8798a"
                "823a2b190a5cd6c566c67a0b54602d3ad50cc9a70b0f9ebf3074ef99380e821f"
        );
        settings.put(KitConfig.CONFIG_CELLULAR_DATA, "true");
        return settings;
    }

    /**
     * Send a notification stating a beacon is nearby.
     *
     * @param region
     *         The beacon region that was seen.
     */
    private void sendNotification(ProximityKitBeaconRegion region) {
        Log.d(TAG, "Sending notification.");
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this)
                        .setContentTitle("Proximity Kit Reference Application")
                        .setContentText("An beacon is nearby.")
                        .setSmallIcon(R.drawable.ic_launcher);

        NotificationManager notificationManager =
                (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, builder.build());
    }

    /**
     * Send a notification stating a geofence was entered.
     *
     * @param region
     *         Geofence which was entered.
     */
    private void sendNotification(ProximityKitGeofenceRegion region) {
        Log.d(TAG, "Sending notification.");
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this)
                        .setContentTitle("Proximity Kit Reference Application")
                        .setContentText("A geofence is nearby.")
                        .setSmallIcon(R.drawable.ic_launcher);

        NotificationManager notificationManager =
                (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, builder.build());
    }

    /**
     * Attempt to launch the main activity if we were started in the background.
     */
    private void tryAutoLaunch() {
        if (haveDetectedBeaconsSinceBoot) {
            return;
        }

        // If we were started in the background for some reason
        Log.d(TAG, "auto launching MainActivity");

        // The very first time since boot that we detect an beacon, we launch the
        // MainActivity
        Intent intent = new Intent(this, Hauptfenster.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // **IMPORTANT**: Make sure to add android:launchMode="singleInstance" in the manifest
        // to keep multiple copies of this activity from getting created if the user has
        // already manually launched the app.
        startActivity(intent);
        haveDetectedBeaconsSinceBoot = true;
    }

    private int getRealScreenSize(boolean returnWidth) {

        final DisplayMetrics metrics = new DisplayMetrics();
        Display display = getWindowManager().getDefaultDisplay();
        Method mGetRawH = null, mGetRawW = null;

        //Not real dimensions
        display.getMetrics(metrics);
        int width = metrics.heightPixels;
        int height = metrics.widthPixels;

        try {
            // For JellyBeans and onward
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR1) {
                display.getRealMetrics(metrics);

                //Real dimensions
                width = metrics.heightPixels;
                height = metrics.widthPixels;
            } else {
                mGetRawH = Display.class.getMethod("getRawHeight");
                mGetRawW = Display.class.getMethod("getRawWidth");

                try {
                    width = (Integer) mGetRawW.invoke(display);
                    height = (Integer) mGetRawH.invoke(display);
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        } catch (NoSuchMethodException e3) {
            e3.printStackTrace();
        }

        if (returnWidth) {
            return width;
        } else {
            return height;
        }
    }
}
