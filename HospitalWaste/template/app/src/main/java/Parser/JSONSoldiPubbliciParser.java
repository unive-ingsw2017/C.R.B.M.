package Parser;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;

import java.util.LinkedList;
import java.util.List;

import it.unive.dais.cevid.datadroid.template.DatiDiBilancio.Bilancio;

/**
 * Created by gianmarcocallegher on 31/01/18.
 */

public class JSONSoldiPubbliciParser {
    JSONObject jsonObject;

    public JSONSoldiPubbliciParser (JSONObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    public List<Bilancio> parse () throws JSONException {
        JSONArray jsonArray = jsonObject.getJSONArray("data");
        List<Bilancio> vociBilancio = new LinkedList<>();

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jo = jsonArray.getJSONObject(i);

            String codiceEnte = jo.getString("cod_ente");
            String codiceSiope = jo.getString("codice_siope");
            String descrizioneCodice = jo.getString("descrizione_codice");

            double importo_2015 = Double.parseDouble(convertToValue(jo.getString("importo_2015")));
            double importo_2016 = Double.parseDouble(convertToValue(jo.getString("importo_2016")));
            double importo_2017 = Double.parseDouble(convertToValue(jo.getString("importo_2017")));
            Log.e("IMPORTO_DOUBLE", String.valueOf(importo_2016));
            Log.e("IMPORTO", jo.getString("importo_2016"));
            vociBilancio.add(new Bilancio(codiceSiope, codiceEnte, 2015, descrizioneCodice, importo_2015));
            vociBilancio.add(new Bilancio(codiceSiope, codiceEnte, 2016, descrizioneCodice, importo_2016));
            vociBilancio.add(new Bilancio(codiceSiope, codiceEnte, 2017, descrizioneCodice, importo_2017));
        }

        return vociBilancio;

    }

    private String convertToValue(String s) { return (s != null && !s.equals("null"))? s : "0.0"; }

}
