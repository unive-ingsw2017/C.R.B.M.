package it.unive.dais.crbm;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.coreutils.BuildConfig;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.io.DataInputStream;
import java.io.IOException;

import it.unive.dais.crbm.DatabaseUtils.DBHelper;


/**
 * Activity per la schermata di crediti e about.
 *
 * @author Alvise Spanò, Università Ca' Foscari
 */
public class InfoActivity extends AppCompatActivity {

    /**
     * Produce la stringa completa coi crediti.
     * @param ctx oggetto Context, tipicamente {@code this} se chiamato da un'altra Activity.
     * @return ritorna la stringa completa.
     */
    public String credits(Context ctx) throws IOException {
        ApplicationInfo ai = ctx.getApplicationInfo();
        StringBuffer buf = new StringBuffer();
        StringBuffer developers = new StringBuffer();
        DBHelper helper = DBHelper.getSingleton();

        DataInputStream dataInputStream = new DataInputStream(getApplicationContext().openFileInput("db_creation_time.txt"));

        long lastTime = dataInputStream.readLong();

        String lastUpdateString = helper.getCreationDateString();

        int version = helper.getDatabseVersion();


        buf.append("\tVERSION.RELEASE {").append(Build.VERSION.RELEASE).append("}");
        buf.append("\n\tVERSION.INCREMENTAL {").append(Build.VERSION.INCREMENTAL).append("}");
        /*
        buf.append("\n\tVERSION.SDK {").append(Build.VERSION.SDK_INT).append("}");
        buf.append("\n\tBOARD {").append(Build.BOARD).append("}");
        buf.append("\n\tBRAND {").append(Build.BRAND).append("}");
        buf.append("\n\tDEVICE {").append(Build.DEVICE).append("}");
        buf.append("\n\tFINGERPRINT {").append(Build.FINGERPRINT).append("}");
        buf.append("\n\tHOST {").append(Build.HOST).append("}");
        buf.append("\n\tID {").append(Build.ID).append("}");
        */

        developers.append("\nCallegher Gianmarco");
        developers.append("\n\tRagazzo Alessio");
        developers.append("\n\tBenvenuto Francesco");
        developers.append("\n\tMakaj Aurelio");

        return String.format(
                "--- APP ---\n" +
                        "%s v%s [%s]\nDatabase last update: %s\nDatabase version: %s\n" +
                        "(c) %s %s @ %s - %s \n\n" +
                        "--- ANDROID ---\n%s\n" +
                        "--- DEVELOPERS ---\n%s",
                ctx.getString(ai.labelRes),
                BuildConfig.VERSION_NAME,
                BuildConfig.BUILD_TYPE,
                lastUpdateString,
                Integer.toString(version),
                R.string.credits_year, R.string.credits_project, R.string.credits_company, R.string.credits_authors,
                buf,
                developers);
    }

    /**
     * Metodo di creazione dell'activity che imposta il layout e la text view con la stringa con i crediti.
     * @param saveInstanceState
     */
    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_info);
        TextView tv_1 = (TextView) findViewById(R.id.textView_1);
        try {
            tv_1.setText(credits(this));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
