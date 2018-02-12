package com.botlegsystems.nawiapp;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.botlegsystems.nawiapp.Pflanzen;
import com.botlegsystems.nawiapp.Tiere;

import org.w3c.dom.Text;

/**
 * Created by Libry on 30.12.2017.
 */

public class Inhaltselement extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inhaltselement);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent thisIntent = getIntent();
        this.thisElement = thisIntent.getStringExtra("thisElement");
        this.elements = thisIntent.getStringArrayExtra("elements");
        this.position = thisIntent.getIntExtra("position", -1);
        this.parentName = thisIntent.getStringExtra("parentName");
        this.category = thisIntent.getStringExtra("category");

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        drawer.closeDrawer(Gravity.START);

        setTitle(thisElement);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        LinearLayout navbarInhaltselement = (LinearLayout) navigationView.getHeaderView(0);
        ImageView navigationMenuPicture = (ImageView) navbarInhaltselement.findViewById(R.id.navbar_inhaltselement_picture);
        String ressourceName = this.parentName.toLowerCase();
        int id = getResources().getIdentifier(ressourceName, "drawable", getPackageName());
        navigationMenuPicture.setImageResource(id);

        TextView navigationMenuText = (TextView) navbarInhaltselement.findViewById(R.id.navbar_inhaltselement_text);
        navigationMenuText.setText(this.parentName);

        MenuItem parentMenuItem = navigationView.getMenu().findItem(R.id.parent);
        parentMenuItem.setTitle(this.parentName);

        TextView elementText = (TextView) findViewById(R.id.text_view_inhaltselement);
        String thisElementReplaced = this.thisElement.replaceAll(" ", "_").toLowerCase().replace("ü", "ue")
                .replace("ö", "oe")
                .replace("ä", "ae")
                .replace("ß", "ss")
                .replace("-", "_");
        ressourceName = "content_" + thisElementReplaced;
        id = getResources().getIdentifier(ressourceName, "string", getPackageName());
        String text = thisElement + "\n\n";
        text += getResources().getString(id);
        elementText.setText(text);

        ImageView elementImage = (ImageView) findViewById(R.id.picture_inhaltselement);
        id = getResources().getIdentifier(thisElementReplaced, "drawable", getPackageName());
        elementImage.setImageResource(id);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.parent) {
            Intent i = new Intent(Inhaltselement.this, Inhaltsuebersicht.class);
            i.putExtra("thisElement", this.parentName);
            i.putExtra("elements", this.elements);
            i.putExtra("category", this.category);
            startActivity(i);
        } else if (id == R.id.prev) {
            Intent i = new Intent(Inhaltselement.this, Inhaltselement.class);
            if(this.position > 0) {
                i.putExtra("thisElement", this.elements[this.position - 1]);
                i.putExtra("position", this.position - 1);
            }
            else
            {
                i.putExtra("thisElement", this.elements[this.elements.length - 1]);
                i.putExtra("position", this.elements.length - 1);
            }
            i.putExtra("elements", this.elements);
            i.putExtra("parentName", this.parentName);
            i.putExtra("category", this.category);
            startActivity(i);
        } else if (id == R.id.next) {
            Intent i = new Intent(Inhaltselement.this, Inhaltselement.class);
            if(this.position < this.elements.length - 1) {
                i.putExtra("thisElement", this.elements[this.position + 1]);
                i.putExtra("position", this.position + 1);
            }
            else
            {
                i.putExtra("thisElement", this.elements[0]);
                i.putExtra("position", 0);
            }
            i.putExtra("elements", this.elements);
            i.putExtra("parentName", this.parentName);
            i.putExtra("category", this.category);
            startActivity(i);
        } else if (id == R.id.map) {
            Intent i = new Intent(Inhaltselement.this, Hauptfenster.class);
            startActivity(i);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(Gravity.START);
        return true;
    }

    private String thisElement;
    private String[] elements;
    private int position;
    private String parentName;
    private String category;
}
