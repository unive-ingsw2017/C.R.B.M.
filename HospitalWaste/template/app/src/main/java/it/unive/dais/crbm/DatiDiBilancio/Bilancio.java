package it.unive.dais.crbm.DatiDiBilancio;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by francescobenvenuto on 04/01/2018.
 */

public class Bilancio implements Parcelable {
    private String codiceSiope;
    private String codiceEnte;

    private int anno;
    private double importo;

    private String descrizioneCodice;

    public Bilancio(String codiceSiope, String codiceEnte, int anno, String descrizioneCodice, double importo) {
        this.codiceSiope = codiceSiope;
        this.codiceEnte = codiceEnte;
        this.anno = anno;
        this.descrizioneCodice = descrizioneCodice;
        this.importo = importo;
    }

    protected Bilancio(Parcel in) {
        codiceSiope = in.readString();
        codiceEnte = in.readString();
        anno = in.readInt();
        importo = in.readDouble();
        descrizioneCodice = in.readString();
    }

    public static final Creator<Bilancio> CREATOR = new Creator<Bilancio>() {
        @Override
        public Bilancio createFromParcel(Parcel in) {
            return new Bilancio(in);
        }

        @Override
        public Bilancio[] newArray(int size) {
            return new Bilancio[size];
        }
    };

    //usa un oggetto bilancio gia esistente e ne crea un altro a partire da questo e modifica solo anno e importo
    public Bilancio generateNewBilancio(int anno, double importo) {
        return new Bilancio(
                this.codiceSiope,
                this.codiceEnte,
                anno,
                this.descrizioneCodice,
                importo
        );
    }


    public String getCodiceSiope() {
        return codiceSiope;
    }

    public String getCodiceEnte() {
        return codiceEnte;
    }

    public int getAnno() {
        return anno;
    }

    public String getDescrizioneCodice() {
        return descrizioneCodice;
    }

    public double getImporto() {
        return importo;
    }

    public boolean isOfYear(int year) {
        return getAnno() == year;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        Object myContainer[] = new Object[]{
                codiceSiope,
                codiceEnte,
                anno,
                descrizioneCodice,
                importo,
        };
        parcel.writeArray(myContainer);
    }
}
