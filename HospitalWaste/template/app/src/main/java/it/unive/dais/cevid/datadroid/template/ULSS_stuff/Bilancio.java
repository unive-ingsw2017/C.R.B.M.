package it.unive.dais.cevid.datadroid.template.ULSS_stuff;

/**
 * Created by francescobenvenuto on 04/01/2018.
 */

public class Bilancio {
    private String codiceSiope;
    private String codiceEnte;
    private int anno;

    public Bilancio(String codiceSiope, String codiceEnte, int anno, String descrizioneStringa, double importo) {
        this.codiceSiope = codiceSiope;
        this.codiceEnte = codiceEnte;
        this.anno = anno;
        this.descrizioneStringa = descrizioneStringa;
        this.importo = importo;
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

    public String getDescrizioneStringa() {
        return descrizioneStringa;
    }

    public double getImporto() {
        return importo;
    }

    private String descrizioneStringa;
    private double importo;

}
