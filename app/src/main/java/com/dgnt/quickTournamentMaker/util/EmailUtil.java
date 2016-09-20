package com.dgnt.quickTournamentMaker.util;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.dgnt.quickTournamentMaker.R;
import com.dgnt.quickTournamentMaker.adapter.AppsAdapter;

import java.io.File;
import java.util.List;

/**
 * Created by Owner on 8/9/2016.
 */
public class EmailUtil {

    public static void sendEmail(final Context context, final String[] email_arr, final String subject, final String attachmentPath, final String attachmentType) {
        //For some reason, we can't get a list of email clients when also attaching a file when calling Intent.createChooser()

        //Solution is somewhat like this http://stackoverflow.com/questions/5841417/send-an-email-in-android-selecting-only-email-apps-and-specifying-attachment-mim
        //except we'll just assume using ACTION_SENDTO gets us all the email client apps we need

        //1. Create intent with attachment
        final Intent emailWithAttachmentIntent = new Intent(Intent.ACTION_SEND);
        emailWithAttachmentIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        emailWithAttachmentIntent.putExtra(Intent.EXTRA_TEXT, "");
        if (email_arr != null && email_arr.length > 0)
            emailWithAttachmentIntent.putExtra(Intent.EXTRA_EMAIL, email_arr);
        if (attachmentPath != null && attachmentType != null) {
            Uri uri = Uri.fromFile(new File(attachmentPath));
            emailWithAttachmentIntent.putExtra(Intent.EXTRA_STREAM, uri);
            emailWithAttachmentIntent.setType(attachmentType);
        }
        emailWithAttachmentIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        //2. Get list of apps when using the intent with ACTION_SENDTO and mailto(should be just email clients)
        final Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        Uri data = Uri.parse("mailto:");
        emailIntent.setData(data);
        final List<ResolveInfo> apps_arr = context.getApplicationContext().getPackageManager().queryIntentActivities(emailIntent, PackageManager.GET_RESOLVED_FILTER);

        //3. Build the list of email clients to pick from
        ListView listView = new ListView(context);
        listView.setAdapter(new AppsAdapter(context, apps_arr));

        final AlertDialog.Builder builder2 = new AlertDialog.Builder(context);
        builder2.setView(listView);
        builder2.setTitle(context.getString(R.string.emailPickerTitle));

        builder2.setNegativeButton(context.getString(android.R.string.cancel), new DialogInterface.OnClickListener() {
            public void onClick(final DialogInterface dialogInterface, final int n) {
                dialogInterface.cancel();
            }
        });
        final AlertDialog alert = builder2.create();
        alert.show();

        //4. Modify the emailWithAttachmentIntent to contain the correct componentName selected from the list
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                ActivityInfo activity = apps_arr.get(position).activityInfo;
                ComponentName name = new ComponentName(activity.applicationInfo.packageName,
                        activity.name);

                emailWithAttachmentIntent.setComponent(name);
                context.startActivity(emailWithAttachmentIntent);
                alert.cancel();
            }
        });
    }
}
