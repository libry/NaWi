package com.botlegsystems.nawiapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;

public class Pflanzen extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

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
        setContentView(R.layout.activity_pflanzen);

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
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.pflanzen_beeren) {
            Intent i = new Intent(Pflanzen.this, Inhaltsuebersicht.class);
            i.putExtra("thisElement", "Beeren");
            i.putExtra("elements", beerenStrings);
            i.putExtra("category", "Pflanzen");
            startActivity(i);
        } else if (id == R.id.pflanzen_farne) {
            Intent i = new Intent(Pflanzen.this, Inhaltsuebersicht.class);
            i.putExtra("thisElement", "Farne");
            i.putExtra("elements", farneStrings);
            i.putExtra("category", "Pflanzen");
            startActivity(i);
        } else if (id == R.id.pflanzen_moose) {
            Intent i = new Intent(Pflanzen.this, Inhaltsuebersicht.class);
            i.putExtra("thisElement", "Moose");
            i.putExtra("elements", mooseStrings);
            i.putExtra("category", "Pflanzen");
            startActivity(i);
        } else if (id == R.id.pflanzen_pilze) {
            Intent i = new Intent(Pflanzen.this, Inhaltsuebersicht.class);
            i.putExtra("thisElement", "Pilze");
            i.putExtra("elements", pilzeStrings);
            i.putExtra("category", "Pflanzen");
            startActivity(i);
        } else if (id == R.id.pflanzen_sumpf) {
            Intent i = new Intent(Pflanzen.this, Inhaltsuebersicht.class);
            i.putExtra("thisElement", "Sumpfpflanzen");
            i.putExtra("elements", sumpfStrings);
            i.putExtra("category", "Pflanzen");
            startActivity(i);
        } else if (id == R.id.pflanzen_wasser) {
            Intent i = new Intent(Pflanzen.this, Inhaltsuebersicht.class);
            i.putExtra("thisElement", "Wasserpflanzen");
            i.putExtra("elements", wasserStrings);
            i.putExtra("category", "Pflanzen");
            startActivity(i);
        } else if (id == R.id.map) {
            Intent i = new Intent(Pflanzen.this, Hauptfenster.class);
            startActivity(i);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(Gravity.START);
        return true;
    }
}
