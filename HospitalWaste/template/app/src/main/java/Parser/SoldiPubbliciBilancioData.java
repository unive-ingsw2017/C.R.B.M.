package Parser;

import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import it.unive.dais.cevid.datadroid.template.DatiDiBilancio.Bilancio;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by gianmarcocallegher on 31/01/18.
 */

public class SoldiPubbliciBilancioData {
    private String codiceComparto;
    private String codiceEnte;

    public SoldiPubbliciBilancioData (String codiceComparto, String codiceEnte) {
        this.codiceComparto = codiceComparto;
        this.codiceEnte = codiceEnte;
    }

    public List<Bilancio> getBilancio() throws IOException {

        RequestBody fromRequest = new FormBody.Builder()
                .add("codicecomparto", codiceComparto)
                .add("codiceente", codiceEnte)
                .build();

        Request request = new Request.Builder()
                .url("http://soldipubblici.gov.it/it/ricerca")
                .addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
                .addHeader("Accept", "Application/json")
                .addHeader("X-Requested-With", "XMLHttpRequest")
                .post(fromRequest)
                .build();

        try {
            return parse(new OkHttpClient().newCall(request).execute().body().string());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    private List<Bilancio> parse (String data) throws JSONException {
        JSONSoldiPubbliciParser jsonSoldipubbliciParser = new JSONSoldiPubbliciParser(new JSONObject(data));
        return jsonSoldipubbliciParser.parse();
    }
}
