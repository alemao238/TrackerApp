package co.edu.uninorte.trackerapp.SellerApp;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.location.LocationListener;
import com.google.firebase.database.DatabaseReference;

import co.edu.uninorte.trackerapp.Model.Position;
import co.edu.uninorte.trackerapp.R;

/**
 * Created by fdjvf on 5/15/2017.
 */

public class TrackingService extends Service implements LocationListener {


    DatabaseReference Vendedores;
    String ID = "";
    private NotificationManager mNM;

    @Nullable
    @Override

    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mNM = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        App.getGoogleApiHelper().addLocationLisetener(this);
        Log.d("a", "Mostrar notificacion");
        showNotification();
        Vendedores = App.getSellers();
    }


    private void showNotification() {

        // The PendingIntent to launch our activity if the user selects this notification
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, BeginTrackingActivity.class), 0);

// Set the info for the views that show in the notification panel.
        Notification notification = new Notification.Builder(this)
                .setSmallIcon(R.drawable.tw__login_btn_default) // the status icon
                .setTicker("Corriendo") // the status text
                .setWhen(System.currentTimeMillis())// the time stamp
                .setContentTitle("TrackingMelanioso")// the label of the entry
                .setContentText("Tracking Iniciado") // the contents of the entry
                .setContentIntent(contentIntent) // The intent to send when the entry is clicked
                .build();
        mNM.notify(R.string.notification, notification);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        ID = intent.getStringExtra("ID");
        return START_STICKY;
    }

    @Override
    public void onLocationChanged(Location location) {

        Vendedores.child(ID).child("Route").push().
                setValue(new Position(Double.toString(location.getLatitude()), Double.toString(location.getLongitude())));
        //OnLocationChangeddd

    }
}
