package wilmol.com.github.sleepeasy.tools;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v7.app.AlertDialog;

/**
 * Opens an alarm app on an android phone.
 *
 * Thanks to http://stackoverflow.com/a/4281243 for the main implementation.
 *
 * @author will 2016-09-06
 */
public class AlarmAppOpener {

    // Various android alarm clock apps and their packages
    private static final String clockImpls[][] = {
            {"HTC Alarm Clock", "com.htc.android.worldclock", "com.htc.android.worldclock.WorldClockTabControl"},
            {"Standar Alarm Clock", "com.android.deskclock", "com.android.deskclock.AlarmClock"},
            {"Froyo Nexus Alarm Clock", "com.google.android.deskclock", "com.android.deskclock.DeskClock"},
            {"Moto Blur Alarm Clock", "com.motorola.blur.alarmclock", "com.motorola.blur.alarmclock.AlarmClock"},
            {"Samsung Galaxy Clock", "com.sec.android.app.clockpackage", "com.sec.android.app.clockpackage.ClockPackage"},
            {"Sony Ericsson Xperia Z", "com.sonyericsson.organizer", "com.sonyericsson.organizer.Organizer_WorldClock"},
            {"ASUS Tablets", "com.asus.deskclock", "com.asus.deskclock.DeskClock"}
    };
    private Context context;
    private Activity activity;

    public AlarmAppOpener(Context context, Activity activity) {
        this.context = context;
        this.activity = activity;
    }

    public void openAlarmAppIfOneExistsOtherwiseOpenStore() {

        PackageManager packageManager = context.getPackageManager();
        Intent alarmClockIntent = new Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_LAUNCHER);

        boolean foundClockImpl = false;
        for (int i = 0; i < clockImpls.length; i++) {
            String vendor = clockImpls[i][0];
            String packageName = clockImpls[i][1];
            String className = clockImpls[i][2];
            try {
                ComponentName cn = new ComponentName(packageName, className);
                ActivityInfo aInfo = packageManager.getActivityInfo(cn, PackageManager.GET_META_DATA);
                alarmClockIntent.setComponent(cn);
                foundClockImpl = true;
            } catch (PackageManager.NameNotFoundException e) {
            }
        }

        if (foundClockImpl) {
            openAlarmClockApp(alarmClockIntent);
        } else {
            promptUserAndOpenStore();
        }
    }

    private void openAlarmClockApp(Intent alarmClockIntent) {
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, alarmClockIntent, 0);
        activity.startActivity(alarmClockIntent);
    }

    private void promptUserAndOpenStore() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setMessage("No alarm found, open store?");
        alertDialogBuilder.setCancelable(true);

        alertDialogBuilder.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        try {
                            openStore();
                        } catch (ActivityNotFoundException e) { // error if google play store isn;t installed, (mostly for emulator)
                            createStoreNotFoundDialogue();
                        }
                    }
                });

        alertDialogBuilder.setNegativeButton(
                "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        createAndShowAlertDialogFromBuilder(alertDialogBuilder);
    }

    private void openStore() throws ActivityNotFoundException {
        // Bring user to the market to choose an app
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setData(Uri.parse("market://details?id=" + "com.package.name"));
        activity.startActivity(intent);
    }

    private void createStoreNotFoundDialogue() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setMessage("Market store not found!");
        alertDialogBuilder.setCancelable(true);
        alertDialogBuilder.setPositiveButton(
                "Okay",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        createAndShowAlertDialogFromBuilder(alertDialogBuilder);
    }

    private void createAndShowAlertDialogFromBuilder(AlertDialog.Builder alertDialogBuilder) {
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

}
