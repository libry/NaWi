package com.botlegsystems.nawiapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;

public class Tiere extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    final String [] amphibienStrings = new String[]{
            "Bergmolch",
            "Erdkröte",
            "Grasfrosch",
            "Laubfrosch",
            "Moorfrosch",
            "Teichfrosch"
    };

    final String [] insektenStrings = new String[]{
            "Europäische Maulwurfsgrille",
            "Feldgrille",
            "Feldwespe",
            "Strauchschrecke",
            "Wasserskorpion"
    };

    final String [] libellenStrings = new String [] {
            "Glänzende Smaragdlibelle",
            "Großer Blaupfeil",
            "Große Königslibelle",
            "Vierfleck"
    };

    final String [] reptilienStrings = new String [] {
            "Blindschleiche",
            "Europäische Sumpfschildkröte",
            "Kreuzotter",
            "Waldeidechse",
            "Zauneidechse"
    };

    final String [] saeugetiereStrings = new String [] {
            "Bisamratte",
            "Haselmaus",
            "Rötelmaus",
            "Siebenschläfer",
            "Wasserfledermaus",
            "Wasserspitzmaus",
            "Zwergmaus"
    };

    final String [] spinnenStrings = new String [] {
            "Feenlämpchenspinne",
            "Jagdspinne",
            "Veränderliche Krabbenspinne",
            "Zebraspinne"
    };

    final String [] voegelStrings = new String [] {
            "Blässhuhn",
            "Eisvogel",
            "Fluss-Seeschwalbe",
            "Höckerschwan",
            "Lachmöwe",
            "Reiherente",
            "Stockente",
            "Tafelente",
            "Zwergtaucher"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tiere);

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

        if (id == R.id.tiere_amphibien) {
            Intent i = new Intent(Tiere.this, Inhaltsuebersicht_Tiere.class);
            i.putExtra("thisElement", "Amphibien");
            i.putExtra("elements", amphibienStrings);
            i.putExtra("category", "Tiere");
            startActivity(i);
        } else if (id == R.id.tiere_insekten) {
            Intent i = new Intent(Tiere.this, Inhaltsuebersicht_Tiere.class);
            i.putExtra("thisElement", "Insekten");
            i.putExtra("elements", insektenStrings);
            i.putExtra("category", "Tiere");
            startActivity(i);
        } else if (id == R.id.tiere_libellen) {
            Intent i = new Intent(Tiere.this, Inhaltsuebersicht_Tiere.class);
            i.putExtra("thisElement", "Libellen");
            i.putExtra("elements", libellenStrings);
            i.putExtra("category", "Tiere");
            startActivity(i);
        } else if (id == R.id.tiere_reptilien) {
            Intent i = new Intent(Tiere.this, Inhaltsuebersicht_Tiere.class);
            i.putExtra("thisElement", "Reptilien");
            i.putExtra("elements", reptilienStrings);
            i.putExtra("category", "Tiere");
            startActivity(i);
        } else if (id == R.id.tiere_saeugetiere) {
            Intent i = new Intent(Tiere.this, Inhaltsuebersicht_Tiere.class);
            i.putExtra("thisElement", "Säugetiere");
            i.putExtra("elements", saeugetiereStrings);
            i.putExtra("category", "Tiere");
            startActivity(i);
        } else if (id == R.id.tiere_spinnen) {
            Intent i = new Intent(Tiere.this, Inhaltsuebersicht_Tiere.class);
            i.putExtra("thisElement", "Spinnen");
            i.putExtra("elements", spinnenStrings);
            i.putExtra("category", "Tiere");
            startActivity(i);
        } else if (id == R.id.tiere_voegel) {
            Intent i = new Intent(Tiere.this, Inhaltsuebersicht_Tiere.class);
            i.putExtra("thisElement", "Vögel");
            i.putExtra("elements", voegelStrings);
            i.putExtra("category", "Tiere");
            startActivity(i);
        } else if (id == R.id.map) {
            Intent i = new Intent(Tiere.this, Hauptfenster.class);
            startActivity(i);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(Gravity.START);
        return true;
    }
}
