package Parser;

import android.util.Log;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import it.unive.dais.crbm.DatiAppalti.Appalto;

/**
 * Created by francescobenvenuto on 31/01/2018.
 */

public class XMLAppaltiParser {
    private static XMLAppaltiParser instance = null;
    private DocumentBuilder dBuilder = null;

    private XMLAppaltiParser() {
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            dBuilder = dbFactory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
    }

    public static XMLAppaltiParser getInstance() {
        if (instance == null) {
            instance = new XMLAppaltiParser();
        }
        return instance;
    }

    public List<Appalto> parseAppalti(List<URL> urls, String codiceEnte) {
        List<Appalto> result = new LinkedList();

        for (URL url : urls) {//iterate to all the url for the codiceEnte
            try {
                URLConnection connection = url.openConnection();
                result.addAll(parsePage(connection.getInputStream(), codiceEnte));
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    private Collection<? extends Appalto> parsePage(InputStream response, String codiceEnte) {
        List<Appalto> appaltoList = new LinkedList();

        try {
            Document doc = dBuilder.parse(response);

            NodeList lottiList = doc.getElementsByTagName("lotto");
            for (int index = 0; index < lottiList.getLength(); index++) { // scorre tutti i lotti
                Element lotto = (Element) lottiList.item(index);

                Node codiceFiscaleNode = getFirstElement(lotto, "codiceFiscale");
                Node ragioneSocialeNode = getFirstElement(lotto, "ragioneSociale");

                if (codiceFiscaleNode == null || ragioneSocialeNode == null) {
                    continue; // skipe this turn some data is wrong
                }

                appaltoList.add(
                        new Appalto(
                                getFirstElement(lotto, "cig").getTextContent(),
                                getFirstElement(lotto, "oggetto").getTextContent(),
                                getFirstElement(lotto, "sceltaContraente").getTextContent(),
                                codiceFiscaleNode.getTextContent(),
                                ragioneSocialeNode.getTextContent(),
                                Double.parseDouble(
                                        getFirstElement(lotto,"importoAggiudicazione").getTextContent()
                                ),
                                codiceEnte
                        )
                );
            }

        } catch (SAXException | IOException e) {
            Log.d("parsePage", "errore nel parsing della risposta");
            e.printStackTrace();
        }
        return appaltoList;
    }

    private Node getFirstElement(Element lotto, String cig) {
        return lotto.getElementsByTagName(cig).item(0);
    }
}