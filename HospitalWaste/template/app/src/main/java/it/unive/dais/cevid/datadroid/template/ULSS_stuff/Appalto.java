package it.unive.dais.cevid.datadroid.template.ULSS_stuff;

/**
 * Created by francescobenvenuto on 04/01/2018.
 */

public class Appalto {
    private String cig;
    private String oggetto;
    private String aggiudicatario;
    private String codiceFiscale;
    private String ragioneSociale;
    private double importo;
    private String codiceEnte;

    public Appalto(String cig, String oggetto, String aggiudicatario, String codiceFiscale, String ragioneSociale, double importo, String codiceEnte) {
        this.cig = cig;
        this.oggetto = oggetto;
        this.aggiudicatario = aggiudicatario;
        this.codiceFiscale = codiceFiscale;
        this.ragioneSociale = ragioneSociale;
        this.importo = importo;
        this.codiceEnte = codiceEnte;
    }

    public String getCig() {

        return cig;
    }

    public String getOggetto() {
        return oggetto;
    }

    public String getAggiudicatario() {
        return aggiudicatario;
    }

    public String getRagioneSociale() {
        return ragioneSociale;
    }

    public double getImporto() {
        return importo;
    }

    public String getCodiceEnte() {
        return codiceEnte;
    }

    public String getCodiceFiscale() {
        return codiceFiscale;
    }
}
