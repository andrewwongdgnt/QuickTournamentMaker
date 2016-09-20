package com.dgnt.quickTournamentMaker.task;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.dgnt.quickTournamentMaker.R;
import com.dgnt.quickTournamentMaker.util.EmailUtil;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

/**
 * Created by Owner on 4/17/2016.
 */
public class ExportTournamentTask extends AsyncTask<String, Void, String> {

    private Context context;
    private View tournamentView_root;
    private ProgressDialog dialog;


    private String imageFileName;
    private String dirPath;
    private String fullPath;

    private Bitmap bitmap;

    private Boolean resultSuccessful;


    public ExportTournamentTask(final Context context, final View tournamentView_root, final String imageFileName, final String dirPath) {
        this.context = context;
        this.tournamentView_root = tournamentView_root;
        this.imageFileName = fileEncoder(imageFileName);
        dialog = new ProgressDialog(context);
        this.dirPath = dirPath;
        fullPath = this.dirPath + "/" + this.imageFileName;
    }


    public static String fileEncoder(String sourceString) {
        return sourceString.replaceAll("[^A-Za-z0-9\\-_\\. ]", "-") + ".jpg";
    }


    protected void onPreExecute() {
        this.dialog.setMessage(context.getString(R.string.exportAsImageProgressMsg));
        this.dialog.show();

        final int width = tournamentView_root.getWidth();
        final int height = tournamentView_root.getHeight();

        bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(bitmap);

        final Drawable bgDrawable = tournamentView_root.getBackground();
        if (bgDrawable != null)
            //has background drawable, then draw it on the canvas
            bgDrawable.draw(canvas);
        else
            //does not have background drawable, then draw white background on the canvas
            canvas.drawColor(Color.WHITE);

        tournamentView_root.layout(tournamentView_root.getLeft(), tournamentView_root.getTop(), tournamentView_root.getRight(), tournamentView_root.getBottom());
        tournamentView_root.draw(canvas);

    }

    protected String doInBackground(final String... args) {


        resultSuccessful = false;

        String state = Environment.getExternalStorageState();
        if (!Environment.MEDIA_MOUNTED.equals(state)) {
            return context.getString(R.string.sdNotMounted);
        }


        //Create a directory for your PDF
        final File dir = new File(dirPath);

        if (!dir.exists()) {
            dir.mkdir();
        }


        if (tournamentView_root != null) {
            try {
                if (bitmap != null) {
                    OutputStream fos = null;
                    File file = new File(dirPath, imageFileName);
                    fos = new FileOutputStream(file);
                    BufferedOutputStream bos = new BufferedOutputStream(fos);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 50, bos);
                    bos.flush();
                    bos.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
                return context.getString(R.string.exportAsImageFail, e.getMessage());
            }
        }

        resultSuccessful = true;

        return context.getString(R.string.exportAsImageSuccess, fullPath);


    }

    @Override
    protected void onPostExecute(final String resultMsg) {

        if (dialog.isShowing()) {
            dialog.dismiss();
        }
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(resultMsg);
        if (resultSuccessful) {


            builder.setPositiveButton(context.getString(R.string.share), new DialogInterface.OnClickListener() {
                public void onClick(final DialogInterface dialogInterface, final int n) {

                    EmailUtil.sendEmail(context,null,"",fullPath,"application/image");
                }
            });

            builder.setNeutralButton(context.getString(R.string.view), null);
        } else {
            builder.setNeutralButton(context.getString(R.string.copyToClipboard), null);
        }
        builder.setNegativeButton(context.getString(android.R.string.ok), new DialogInterface.OnClickListener() {
            public void onClick(final DialogInterface dialogInterface, final int n) {
                dialogInterface.cancel();
            }
        });

        final AlertDialog alertDialog = builder.create();
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(final DialogInterface dialog) {

                Button b = alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL);
                b.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        if (resultSuccessful) {
                            viewImage();
                        } else {
                            ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                            ClipData clip = ClipData.newPlainText("errorMsg", resultMsg);
                            clipboard.setPrimaryClip(clip);

                            Toast.makeText(context.getApplicationContext(), context.getString(R.string.copyToClipboardMsg), Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
        alertDialog.show();
    }

    private void viewImage() {
        try {
            final Intent intent = new Intent(Intent.ACTION_VIEW);
            final Uri uri = Uri.fromFile(new File(fullPath));
            intent.setDataAndType(uri, "image/*");
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setMessage(context.getString(R.string.noImageAppInstalledMsg));
            builder.setPositiveButton(context.getString(android.R.string.ok), new DialogInterface.OnClickListener() {
                public void onClick(final DialogInterface dialogInterface, final int n) {
                    dialogInterface.cancel();
                }
            });
            builder.create().show();
        }
    }


}