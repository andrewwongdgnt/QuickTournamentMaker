package com.dgnt.quickTournamentMaker.task;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.dgnt.quickTournamentMaker.R;
import com.dgnt.quickTournamentMaker.activity.HistoryActivity;
import com.dgnt.quickTournamentMaker.model.history.HistoricalTournament;
import com.dgnt.quickTournamentMaker.model.tournament.Tournament;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

/**
 * Created by Owner on 4/17/2016.
 */
public class StartTournamentFromFileTask extends AsyncTask<String, Void, HistoricalTournament> {

    private Context context;
    private ProgressDialog dialog;


    private String filePath;
    private Exception exception;


    public StartTournamentFromFileTask(final Context context, final String filePath) {
        this.context = context;
        dialog = new ProgressDialog(context);
        this.filePath = filePath;
    }


    protected void onPreExecute() {
        this.dialog.setMessage(context.getString(R.string.readFileProgressMsg));
        this.dialog.show();

    }

    protected HistoricalTournament doInBackground(final String... args) {


        final File file = new File(filePath);

        final StringBuilder stringBuilder = new StringBuilder();

        try {
            final BufferedReader br = new BufferedReader(new FileReader(file));
            String line;

            while ((line = br.readLine()) != null) {
                stringBuilder.append(line);
            }
            br.close();

            final String content = stringBuilder.toString();

            return Tournament.JsonHelper.fromJson(content);


        } catch (Exception e) {


            exception = e;
            return null;
        }
    }

    @Override
    protected void onPostExecute(final HistoricalTournament historicalTournament) {

        if (dialog.isShowing()) {
            dialog.dismiss();
        }

        if (historicalTournament == null) {
            if (exception != null) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage(exception instanceof JSONException ? context.getString(R.string.corruptFileMsg) : context.getString(R.string.startTournamentFromFileFail, exception.getMessage()));
                builder.setPositiveButton(context.getString(android.R.string.ok), null);
                builder.create().show();
            }
        } else if (context instanceof HistoryActivity) {
            ((HistoryActivity) context).startTournament(((HistoryActivity) context), historicalTournament);
        }

    }


}