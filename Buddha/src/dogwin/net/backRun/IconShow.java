package dogwin.net.backRun;import android.annotation.SuppressLint;import android.annotation.TargetApi;import android.app.Activity;import android.app.Notification;import android.app.NotificationManager;import android.app.PendingIntent;import android.content.Context;import android.content.Intent;import android.os.Build;import android.support.v4.app.NotificationCompat;import dogwin.net.apps.Buddha;import dogwin.net.apps.R;@TargetApi(Build.VERSION_CODES.HONEYCOMB)@SuppressLint("NewApi")public class IconShow extends Activity{	private int FM_NOTIFICATION_ID = 819;	/*	public void createNotification(Activity activity){		// Prepare intent which is triggered if the	    // notification is selected	    Intent intent = new Intent(activity, Buddha.class);	    PendingIntent pIntent = PendingIntent.getActivity(activity, 0, intent, 0);	    // Build notification	    // Actions are just fake	    Notification noti = new Notification.Builder(this)	        .setContentTitle("Buddha")	        .setContentText("妙缘").setSmallIcon(R.drawable.ic_launcher)	        .setContentIntent(pIntent)	        .addAction(R.drawable.ic_launcher, "Call", pIntent)	        .addAction(R.drawable.ic_launcher, "More", pIntent)	        .addAction(R.drawable.ic_launcher, "And more", pIntent).build();	    NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);	    // Hide the notification after its selected	    noti.flags |= Notification.FLAG_AUTO_CANCEL;	    notificationManager.notify(0, noti);	}*/		public void createNotification(Activity activity){		Intent intent = new Intent(activity, Buddha.class);		PendingIntent pIntent = PendingIntent.getActivity(activity, 0, intent, 0);		Notification noti = new Notification.Builder(activity)        .setContentTitle("Buddha")        .setContentText("妙缘").setSmallIcon(R.drawable.ic_launcher)        .setContentIntent(pIntent).build();		 NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);		    // Hide the notification after its selected		 noti.flags |= Notification.FLAG_AUTO_CANCEL;		 notificationManager.notify(0, noti);	}	/*	public void addNotification() {	    NotificationCompat.Builder builder =  	            new NotificationCompat.Builder(this)  	            .setSmallIcon(R.drawable.ic_launcher)  	            .setContentTitle("Notifications Example")  	            .setContentText("This is a test notification");  	    Intent notificationIntent = new Intent(this, Buddha.class);  	    PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent,   	            PendingIntent.FLAG_UPDATE_CURRENT);  	    builder.setContentIntent(contentIntent);  	    // Add as notification  	    NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);  	    manager.notify(FM_NOTIFICATION_ID, builder.build());  	}  	// Remove notification  	public void removeNotification() {  	    NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);  	    manager.cancel(FM_NOTIFICATION_ID);  	}  */}