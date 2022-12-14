package com.example.rpmp2.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MotionEvent;

import com.example.rpmp2.R;

import org.osmdroid.bonuspack.routing.OSRMRoadManager;
import org.osmdroid.bonuspack.routing.Road;
import org.osmdroid.bonuspack.routing.RoadManager;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.Projection;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Polyline;

import java.util.ArrayList;

public class GeoserviceActivity extends AppCompatActivity
{
    private RoadManager roadManager = new OSRMRoadManager(this, "");
    private MapView map;
    private Polyline roadOverlay;
    private Marker startMarker;
    private static final GeoPoint cyberDorm = new GeoPoint(50.388354835940326, 30.48975285699694);
    private static final GeoPoint cyberFaculty = new GeoPoint(50.383332895963285, 30.47120356614197);

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Context ctx = getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));
        setContentView(R.layout.activity_geoservice);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.WRITE_EXTERNAL_STORAGE }, 0);

        this.map = (MapView) findViewById(R.id.geoservice_map);
        this.map.setTileSource(TileSourceFactory.MAPNIK);

        new Thread(() ->
        {
            this.map.getController().animateTo(cyberFaculty);
            this.map.getController().setZoom(12);
            this.map.getOverlays().add(new DoubleTapListenerOverlay(this));
            setRoute(cyberDorm);
        }).start();
    }

    public void setRoute(GeoPoint start)
    {
        this.map.getOverlays().remove(this.roadOverlay);
        this.map.getOverlays().remove(this.startMarker);
        this.startMarker = new Marker(this.map);
        this.startMarker.setPosition(start.clone());
        this.startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        ArrayList<GeoPoint> waypoints = new ArrayList<GeoPoint>();
        waypoints.add(start.clone());
        waypoints.add(cyberFaculty.clone());
        Road road = this.roadManager.getRoad(waypoints);
        this.roadOverlay = RoadManager.buildRoadOverlay(road);
        this.map.getOverlays().add(this.roadOverlay);
        this.map.getOverlays().add(this.startMarker);
        this.map.invalidate();
    }

    public void onResume()
    {
        super.onResume();
        this.map.onResume();
    }

    public void onPause()
    {
        super.onPause();
        this.map.onPause();
    }

    class DoubleTapListenerOverlay extends org.osmdroid.views.overlay.Overlay
    {
        private GeoserviceActivity activity;

        public DoubleTapListenerOverlay()
        {
            super(); // TODO Auto-generated constructor stub
        }

        public DoubleTapListenerOverlay(GeoserviceActivity geoserviceActivity) {
            this();
            this.activity = geoserviceActivity;
        }

        @Override
        public boolean onDoubleTap(MotionEvent e, MapView mapView)
        {
            Projection proj = mapView.getProjection();
            GeoPoint location = (GeoPoint) proj.fromPixels((int)e.getX(), (int)e.getY());

            if (location != null && this.activity != null)
                new Thread(() -> this.activity.setRoute(location)).start();

            return true;
        }
    }
}