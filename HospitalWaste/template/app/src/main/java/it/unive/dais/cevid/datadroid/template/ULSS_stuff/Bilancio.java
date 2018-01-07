package it.unive.dais.cevid.datadroid.template.ULSS_stuff;

/**
 * Created by francescobenvenuto on 04/01/2018.
 */

public class Bilancio {
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

    //usa un oggetto bilancio gia esistente e ne crea un altro a partire da questo e modifica solo anno e importo
    public Bilancio generateNewBilancio(int anno, double importo){
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



}
