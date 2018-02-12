package com.botlegsystems.nawiapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Libry on 30.12.2017.
 */

public class Inhaltsuebersicht extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    final String [] beerenStrings = new String[] {
            "Heidelbeere",
            "Moosbeere",
            "Preiselbeere",
            "Rauschbeere"
    };

    final String [] farneStrings = new String[] {
            "Breitblättriger Dornfarn",
            "Gewöhnlicher Dornfarn",
            "Eichenfarn",
            "Frauenfarn",
            "Kammfarn",
            "Sumpffarn",
            "Tüpfelfarn"
    };

    final String [] mooseStrings = new String[] {
            "Brunnenlebermoos",
            "Federmoos",
            "Frauenhaarmoos",
            "Mittlere Torfmoos",
            "Sparriges Torfmoos",
            "Streifensternmoos"
    };

    final String [] pilzeStrings = new String[] {
            "Birkensporling",
            "Fliegenpilz",
            "Gesäumter Häubling",
            "Hallimasche",
            "Herkuleskeule",
            "Kahle Krempling",
            "Laubwald Rotkappe",
            "Rotbrauner Streifling",
            "Speitäubling",
            "Spitz-Morchel",
            "Stinkmorchel"
    };

    final String [] sumpfStrings = new String[] {
            "Ästiger Igelkolben",
            "Echte Zaunwinde",
            "Fieberklee",
            "Gewöhnliche Gilbweiderich",
            "Krebsschere",
            "Rundblättriges Wintergrün",
            "Schilfrohr",
            "Schwanenblume",
            "Sumpfdotterblume",
            "Wechselblättriges Milzkraut",
            "Zottiges Weideröschen",
            "Zungen-Hahnenfuß"
    };

    final String [] wasserStrings = new String[] {
            "Breitblättriger Rohrkolben",
            "Froschbiss",
            "Gewöhnlicher Wasserschlauch",
            "Kleine Wasserlinse",
            "Nickender Zweizahn",
            "Sumpf-Schwertlilie",
            "Weisse Seerose"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inhaltsuebersicht);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent thisIntent = getIntent();
        final String thisElement = thisIntent.getStringExtra("thisElement");
        final String[] elements = thisIntent.getStringArrayExtra("elements");
        final String category = thisIntent.getStringExtra("category");

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        setTitle(thisElement);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        drawer.closeDrawer(Gravity.START);

        GridView gridview = (GridView) findViewById(R.id.gridview);
        ImageAdapter imageAdapter = new ImageAdapter(this);
        imageAdapter.setImages(elements);

        gridview.setAdapter(imageAdapter);

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
            Intent i = new Intent(Inhaltsuebersicht.this, Inhaltselement.class);
            i.putExtra("thisElement", elements[position]);
            i.putExtra("elements", elements);
            i.putExtra("position", position);
            i.putExtra("parentName", thisElement);
            i.putExtra("category", category);
            startActivity(i);
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.pflanzen_beeren) {
            Intent i = new Intent(Inhaltsuebersicht.this, Inhaltsuebersicht.class);
            i.putExtra("thisElement", "Beeren");
            i.putExtra("elements", beerenStrings);
            i.putExtra("category", "Pflanzen");
            startActivity(i);
            finish();
        } else if (id == R.id.pflanzen_farne) {
            Intent i = new Intent(Inhaltsuebersicht.this, Inhaltsuebersicht.class);
            i.putExtra("thisElement", "Farne");
            i.putExtra("elements", farneStrings);
            i.putExtra("category", "Pflanzen");
            startActivity(i);
            finish();
        } else if (id == R.id.pflanzen_moose) {
            Intent i = new Intent(Inhaltsuebersicht.this, Inhaltsuebersicht.class);
            i.putExtra("thisElement", "Moose");
            i.putExtra("elements", mooseStrings);
            i.putExtra("category", "Pflanzen");
            startActivity(i);
            finish();
        } else if (id == R.id.pflanzen_pilze) {
            Intent i = new Intent(Inhaltsuebersicht.this, Inhaltsuebersicht.class);
            i.putExtra("thisElement", "Pilze");
            i.putExtra("elements", pilzeStrings);
            i.putExtra("category", "Pflanzen");
            startActivity(i);
            finish();
        } else if (id == R.id.pflanzen_sumpf) {
            Intent i = new Intent(Inhaltsuebersicht.this, Inhaltsuebersicht.class);
            i.putExtra("thisElement", "Sumpfpflanzen");
            i.putExtra("elements", sumpfStrings);
            i.putExtra("category", "Pflanzen");
            startActivity(i);
            finish();
        } else if (id == R.id.pflanzen_wasser) {
            Intent i = new Intent(Inhaltsuebersicht.this, Inhaltsuebersicht.class);
            i.putExtra("thisElement", "Wasserpflanzen");
            i.putExtra("elements", wasserStrings);
            i.putExtra("category", "Pflanzen");
            startActivity(i);
            finish();
        } else if (id == R.id.map) {
            Intent i = new Intent(Inhaltsuebersicht.this, Hauptfenster.class);
            startActivity(i);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(Gravity.START);
        return true;
    }

    class ImageAdapter extends BaseAdapter {
        private Context mContext;
        private final LayoutInflater mInflater;

        public ImageAdapter(Context c) {
            mInflater = LayoutInflater.from(c);
            mContext = c;
        }

        public int getCount() {
            return mImageIds.size();
        }

        public Object getItem(int position) {
            return null;
        }

        public long getItemId(int position) {
            return 0;
        }

        // create a new ImageView for each item referenced by the Adapter
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;
            ImageView picture;
            TextView name;

            if (v == null) {
                // if it's not recycled, initialize some attributes
                v = mInflater.inflate(R.layout.content_griditem, parent, false);
                v.setTag(R.id.picture_griditem, v.findViewById(R.id.picture_griditem));
                v.setTag(R.id.text_griditem, v.findViewById(R.id.text_griditem));
            }

            picture = (ImageView) v.getTag(R.id.picture_griditem);
            name = (TextView) v.getTag(R.id.text_griditem);

            Item item = mImageIds.get(position);

            picture.setImageResource(item.drawableId);
            name.setText(item.name);
            return v;
        }

        public void setImages(String [] elements)
        {
            mImageIds = new ArrayList<Item>();
            for(String element: elements)
            {
                String filename = element.replaceAll(" ", "_").toLowerCase().replace("ü", "ue")
                        .replace("ö", "oe")
                        .replace("ä", "ae")
                        .replace("ß", "ss")
                        .replace("-", "_");
                mImageIds.add(new Item(element, getResources().getIdentifier(filename, "drawable", getPackageName())));
            }
        }

        private class Item {
            public final String name;
            public final Integer drawableId;

            Item(String name, Integer drawableId) {
                this.name = name;
                this.drawableId = drawableId;
            }
        }

        ArrayList<Item> mImageIds;
    }
}
