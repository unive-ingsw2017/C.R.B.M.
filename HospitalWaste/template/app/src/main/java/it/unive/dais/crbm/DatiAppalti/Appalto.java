package it.unive.dais.crbm.DatiAppalti;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by francescobenvenuto on 04/01/2018.
 */

public class Appalto implements Parcelable {
    private String cig;
    private String oggetto;
    private String sceltaContraente;
    private String codice_fiscale_aggiudicatario;
    private String aggiudicatario;
    private double importo;
    private String codiceEnte;

    public Appalto(String cig, String oggetto, String sceltaContraente, String codice_fiscale_aggiudicatario, String aggiudicatario, double importo, String codiceEnte) {
        this.cig = cig;
        this.oggetto = oggetto;
        this.sceltaContraente = sceltaContraente;
        this.codice_fiscale_aggiudicatario = codice_fiscale_aggiudicatario;
        this.aggiudicatario = aggiudicatario;
        this.importo = importo;
        this.codiceEnte = codiceEnte;
    }

    protected Appalto(Parcel in) {
        cig = in.readString();
        oggetto = in.readString();
        sceltaContraente = in.readString();
        codice_fiscale_aggiudicatario = in.readString();
        aggiudicatario = in.readString();
        importo = in.readDouble();
        codiceEnte = in.readString();
    }

    public static final Creator<Appalto> CREATOR = new Creator<Appalto>() {
        @Override
        public Appalto createFromParcel(Parcel in) {
            return new Appalto(in);
        }

        @Override
        public Appalto[] newArray(int size) {
            return new Appalto[size];
        }
    };

    public String getCig() {

        return cig;
    }

    public String getOggetto() {
        return oggetto;
    }

    public String getCodice_fiscale_aggiudicatario() {
        return codice_fiscale_aggiudicatario;
    }

    public String getSceltaContraente() {
        return sceltaContraente;
    }

    public String getAggiudicatario() {
        return aggiudicatario;
    }

    public double getImporto() {
        return importo;
    }

    public String getCodiceEnte() {
        return codiceEnte;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        Object myContainer[] = new Object[]{
                cig,
                oggetto,
                sceltaContraente,
                codice_fiscale_aggiudicatario,
                aggiudicatario,
                importo,
                codiceEnte,
        };
        parcel.writeArray(myContainer);
    }
}
