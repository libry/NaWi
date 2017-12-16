package com.botlegsystems.nawiapp;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ScaleDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.internal.NavigationMenuView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Hauptfenster extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_hauptfenster);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

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
                Context context = getApplicationContext();
                CharSequence text = "Naturschutzzentrum";
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
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
                Context context = getApplicationContext();
                CharSequence text = "Naturschutzzentrum";
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
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
                Context context = getApplicationContext();
                CharSequence text = "Torfstechen";
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
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
                Context context = getApplicationContext();
                CharSequence text = "Überwachsener See";
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
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
                Context context = getApplicationContext();
                CharSequence text = "Grenzpfahl";
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
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
                Context context = getApplicationContext();
                CharSequence text = "Hochmoor\nEulenbruck";
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
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
                Context context = getApplicationContext();
                CharSequence text = "Teiche &\nTümpel";
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
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

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
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

    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.hauptfenster, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        return super.onOptionsItemSelected(item);
    }
    */

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nawi_pflanzen) {
            Intent i = new Intent(Hauptfenster.this, Pflanzen.class);
            startActivity(i);
        } else if (id == R.id.nawi_tiere) {
            Log.d("tiere", "Tiere gedrück!");
        } else if (id == R.id.nawi_umfrage) {

        } else if (id == R.id.nawi_info) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void showPlants(View view){
        Log.d("plants", "Show Pflanzen gedrück!");
    }

    public void showAnimals(View view){
        Log.d("tiere", "Show Tiere gedrück!");
    }

    private int getNavigationBarHeight() {
        Resources resources = getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0) {
            return resources.getDimensionPixelSize(resourceId);
        }
        return 0;
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
