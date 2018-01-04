package it.unive.dais.cevid.datadroid.template.ULSS_stuff;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

/**
 * Created by francescobenvenuto on 04/01/2018.
 */

public class ULSS{
    private String descrizione;
    private String codiceEnte;
    private String ospedaliAssociati; // unica Stringa con tutti gli ospedali

    private LatLng coordinate;

    private int numeroLetti;

    public ULSS(String descrizione, String codiceEnte, String ospedaliAssociati, LatLng coordinate, int numeroLetti) {
        this.descrizione = descrizione;
        this.codiceEnte = codiceEnte;
        this.ospedaliAssociati = ospedaliAssociati;
        this.coordinate = coordinate;
        this.numeroLetti = numeroLetti;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public String getCodiceEnte() {
        return codiceEnte;
    }

    public LatLng getCoordinate() {
        return coordinate;
    }

    public int getNumeroLetti() {
        return numeroLetti;
    }

    public String getOspedaliAssociati() {
        return ospedaliAssociati;
    }
}
