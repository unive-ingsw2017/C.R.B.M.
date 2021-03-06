/**
 * Questo package contiene le componenti Android riusabili.
 * Si tratta di classi che contengono già funzionalità base e possono essere riusate apportandovi modifiche
 */
package it.unive.dais.crbm;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import it.unive.dais.crbm.ConfrontoMultiplo.ConfrontoMultiploActivity;
import it.unive.dais.crbm.DatabaseUtils.BilancioHelper;
import it.unive.dais.crbm.DatabaseUtils.DBHelper;
import it.unive.dais.crbm.DatiFornitori.FornitoriActivity;
import it.unive.dais.crbm.DatiULSS.RicercaInDettaglioActivity;
import it.unive.dais.crbm.DatiULSS.ULSS;


/**
 * Questa classe è la componente principale del toolkit: fornisce servizi primari per un'app basata su Google Maps, tra cui localizzazione, pulsanti
 * di navigazione, preferenze ed altro. Essa rappresenta un template che è una buona pratica riusabile per la scrittura di app, fungendo da base
 * solida, robusta e mantenibile.
 * Vengono rispettate le convenzioni e gli standard di qualità di Google per la scrittura di app Android; ogni pulsante, componente,
 * menu ecc. è definito in modo che sia facile riprodurne degli altri con caratteristiche diverse.
 * All'interno del codice ci sono dei commenti che aiutano il programmatore ad estendere questa app in maniera corretta, rispettando le convenzioni
 * e gli standard qualitativi.
 * Per scrivere una propria app è necessario modificare questa classe, aggiungendo campi, metodi e codice che svolge le funzionalità richieste.
 *
 * @author Alvise Spanò, Università Ca' Foscari
 */
public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        GoogleMap.OnMapClickListener, GoogleMap.OnMapLongClickListener,
        GoogleMap.OnCameraMoveStartedListener, GoogleMap.OnMarkerClickListener,
        GoogleMap.OnInfoWindowClickListener, NavigationView.OnNavigationItemSelectedListener {

    protected static final int REQUEST_CHECK_SETTINGS = 500;
    protected static final int PERMISSIONS_REQUEST_ACCESS_BOTH_LOCATION = 501;
    // alcune costanti
    private static final String TAG = "MapsActivity";
    /**
     * Questo oggetto è la mappa di Google Maps. Viene inizializzato asincronamente dal metodo {@code onMapsReady}.
     */
    protected GoogleMap gMap;

    private LatLng POSIZIONE_VENETO_CENTRALE = new LatLng(45.6670603, 12.0536513);

    private SearchView searchView;

    /**
     * Pulsanti in sovraimpressione gestiti da questa app. Da non confondere con i pulsanti che GoogleMaps mette in sovraimpressione e che non
     * fanno parte degli oggetti gestiti manualmente dal codice.
     */

    /**
     * API per i servizi di localizzazione.
     */
    protected FusedLocationProviderClient fusedLocationClient;
    /**
     * Posizione corrente. Potrebbe essere null prima di essere calcolata la prima volta.
     */
    @Nullable
    protected LatLng currentPosition = null;
    /**
     * Il marker che viene creato premendo il pulsante button_here (cioè quello dell'app, non quello di Google Maps).
     * E' utile avere un campo d'istanza che tiene il puntatore a questo marker perché così è possibile rimuoverlo se necessario.
     * E' null quando non è stato creato il marker, cioè prima che venga premuto il pulsante HERE la prima volta.
     */
    @Nullable
    protected Marker hereMarker = null;

    private Map<String, String> mapDenominazioneCodice = Collections.EMPTY_MAP;

    @Nullable
    private Collection<Marker> markers;

    private BilancioHelper bilancioHelper;

    private List<String> descrizioneCodice;


    /**
     * Questo metodo viene invocato quando viene inizializzata questa activity.
     * Si tratta di una sorta di "main" dell'intera activity.
     * Inizializza i campi d'istanza, imposta alcuni listener e svolge gran parte delle operazioni "globali" dell'activity.
     *
     * @param savedInstanceState bundle con lo stato dell'activity.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        bilancioHelper = new BilancioHelper();
        descrizioneCodice = bilancioHelper.getDescrizioneCodici();

        setContentView(R.layout.activity_maps);
        setDrawerMenu();

        mapDenominazioneCodice = new HashMap(); // mapping between name and codice ente for the ULSS

        confrontaUlssList = new HashSet();
        confrontoMultiploButton = (Button) MapsActivity.this.findViewById(R.id.confronto_button);

        // inizializza le preferenze
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);

        // API per i servizi di localizzazione
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // inizializza la mappa asincronamente
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;

    private void setDrawerMenu() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    // ciclo di vita della app
    //

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    /**
     * Applica le impostazioni (preferenze) della mappa ad ogni chiamata.
     */
    @Override
    protected void onResume() {
        super.onResume();
        applyMapSettings();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    /**
     * Pulisce la mappa quando l'app viene distrutta.
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        gMap.clear();
    }

    /**
     * Quando arriva un Intent viene eseguito questo metodo.
     * Può essere esteso e modificato secondo le necessità.
     *
     * @see Activity#onActivityResult(int, int, Intent)
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        final LocationSettingsStates states = LocationSettingsStates.fromIntent(intent);
        switch (requestCode) {
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        // inserire codice qui
                        break;
                    case Activity.RESULT_CANCELED:
                        // o qui
                        break;
                    default:
                        break;
                }
                break;
        }
    }

    /**
     * Questo metodo viene chiamato quando viene richiesto un permesso.
     * Si tratta di un pattern asincrono che va gestito come qui impostato: per gestire nuovi permessi, qualora dovesse essere necessario,
     * è possibile riprodurre un comportamento simile a quello già implementato.
     *
     * @param requestCode  codice di richiesta.
     * @param permissions  array con i permessi richiesti.
     * @param grantResults array con l'azione da intraprende per ciascun dei permessi richiesti.
     * @see Activity#onRequestPermissionsResult(int, String[], int[])
     */
    @SuppressLint("MissingPermission") // we already check for the permission
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_BOTH_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    gMap.setMyLocationEnabled(true);
                    Log.d(TAG, "permissions granted: ACCESS_FINE_LOCATION + ACCESS_COARSE_LOCATION");
                } else {
                    Log.e(TAG, "permissions not granted: ACCESS_FINE_LOCATION + ACCESS_COARSE_LOCATION");
                    Snackbar.make(this.findViewById(R.id.map), R.string.msg_permissions_not_granted, Snackbar.LENGTH_LONG);
                }
            }
        }
    }

    /**
     * Invocato quando viene creato il menu delle impostazioni.
     *
     * @param menu l'oggetto menu.
     * @return ritornare true per visualizzare il menu.
     * @see Activity#onCreateOptionsMenu(Menu)
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.maps_with_options, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        MenuItem myMenuItem = menu.findItem(R.id.search);
        searchView = (SearchView) myMenuItem.getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                List<String> filteredList = filter(newText.toLowerCase());
                List<String> codiciEnti = bilancioHelper.filteredULSS(filteredList);

                for (Marker marker : markers) {
                    if (codiciEnti.contains(mapDenominazioneCodice.get(marker.getTitle())))
                        marker.setVisible(true);
                    else
                        marker.setVisible(false);
                }
                if (visibleMarkers() > 1 && newText.length() > 2) {
                    confrontoMultiploButton.setVisibility(View.VISIBLE);
                    queryConfrontoSearch = newText;
                } else {
                    confrontoMultiploButton.setVisibility(View.INVISIBLE);
                    queryConfrontoSearch = "";
                }
                return false;
            }
        });
        MenuItemCompat.setOnActionExpandListener(myMenuItem, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                //the searchview has been closed
                for (Marker marker : markers) {
                    marker.setVisible(true);
                }
                return true;  // Return true to collapse action view
            }

            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return true;  // Return true to expand action view
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    private List<String> filter(String newText) {
        List<String> l = new LinkedList<>();
        for (String s : descrizioneCodice) {
            if (s.toLowerCase().contains(newText))
                l.add(s);
        }
        return l;

    }

    /**
     * Invocato quando viene cliccata una voce nel menu delle impostazioni.
     *
     * @param item la voce di menu cliccata.
     * @return ritorna true per continuare a chiamare altre callback; false altrimenti.
     * @see Activity#onOptionsItemSelected(MenuItem)
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // onConnection callbacks
    //
    //

    /**
     * Viene chiamata quando i servizi di localizzazione sono attivi.
     * Aggiungere qui eventuale codice da eseguire in tal caso.
     *
     * @param bundle il bundle passato da Android.
     * @see com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks#onConnected(Bundle)
     */
    @Override
    public void onConnected(Bundle bundle) {
        Log.i(TAG, "location service connected");
    }

    /**
     * Viene chiamata quando i servizi di localizzazione sono sospesi.
     * Aggiungere qui eventuale codice da eseguire in tal caso.
     *
     * @param i un intero che rappresenta il codice della causa della sospenzione.
     * @see com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks#onConnectionSuspended(int)
     */
    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "location service connection suspended");
        Toast.makeText(this, R.string.conn_suspended, Toast.LENGTH_LONG).show();
    }

    /**
     * Viene chiamata quando la connessione ai servizi di localizzazione viene persa.
     * Aggiungere qui eventuale codice da eseguire in tal caso.
     *
     * @param connectionResult oggetto che rappresenta il risultato della connessione, con le cause della disconnessione ed altre informazioni.
     * @see com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener#onConnectionFailed(ConnectionResult)
     */
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.e(TAG, "location service connection lost");
        Toast.makeText(this, R.string.conn_failed, Toast.LENGTH_LONG).show();
    }

    // maps callbacks
    //
    //

    /**
     * Chiamare questo metodo per aggiornare la posizione corrente del GPS.
     * Si tratta di un metodo proprietario, che non ridefinisce alcun metodo della superclasse né implementa alcuna interfaccia: un metodo
     * di utilità per aggiornare asincronamente in modo robusto e sicuro la posizione corrente del dispositivo mobile.
     */
    public void updateCurrentPosition() {
        if (ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "requiring permission");
            ActivityCompat.requestPermissions(MapsActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSIONS_REQUEST_ACCESS_BOTH_LOCATION);
        } else {
            Log.d(TAG, "permission granted");
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(MapsActivity.this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location loc) {
                            if (loc != null) {
                                currentPosition = new LatLng(loc.getLatitude(), loc.getLongitude());
                                Log.i(TAG, "current position updated");
                            }
                        }
                    });
        }
    }

    /**
     * Viene chiamato quando si clicca sulla mappa.
     * Aggiungere qui codice che si vuole eseguire quando l'utente clicca sulla mappa.
     *
     * @param latLng la posizione del click.
     */
    @Override
    public void onMapClick(LatLng latLng) {
        // nascondi il pulsante della navigazione (non quello di google maps, ma il nostro pulsante custom)
        //button_car.setVisibility(View.INVISIBLE);
    }

    /**
     * Viene chiamato quando si clicca a lungo sulla mappa (long click).
     * Aggiungere qui codice che si vuole eseguire quando l'utente clicca a lungo sulla mappa.
     *
     * @param latLng la posizione del click.
     */
    @Override
    public void onMapLongClick(LatLng latLng) {
    }

    /**
     * Viene chiamato quando si muove la camera.
     * Attenzione: solamente al momento in cui la camera comincia a muoversi, non durante tutta la durata del movimento.
     *
     * @param reason
     */
    @Override
    public void onCameraMoveStarted(int reason) {
    }


    /**
     * Questo metodo è molto importante: esso viene invocato dal sistema quando la mappa è pronta.
     * Il parametro è l'oggetto di tipo GoogleMap pronto all'uso, che viene immediatamente assegnato ad un campo interno della
     * classe.
     * La natura asincrona di questo metodo, e quindi dell'inizializzazione del campo gMap, implica che tutte le
     * operazioni che coinvolgono la mappa e che vanno eseguite appena essa diventa disponibile, vanno messe in questo metodo.
     * Ciò non significa che tutte le operazioni che coinvolgono la mappa vanno eseguite qui: è naturale aver bisogno di accedere al campo
     * gMap in altri metodi, per eseguire operazioni sulla mappa in vari momenti, ma è necessario tenere a mente che tale campo potrebbe
     * essere ancora non inizializzato e va pertanto verificata la nullness.
     *
     * @param googleMap oggetto di tipo GoogleMap.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        gMap = googleMap;

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MapsActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSIONS_REQUEST_ACCESS_BOTH_LOCATION);
        } else {
            gMap.setMyLocationEnabled(true);
        }

        gMap.setOnMapClickListener(this);
        gMap.setOnMapLongClickListener(this);
        gMap.setOnCameraMoveStartedListener(this);
        gMap.setOnMarkerClickListener(this);
        gMap.setOnInfoWindowClickListener(this);

        UiSettings uis = gMap.getUiSettings();
        uis.setZoomGesturesEnabled(true);
        uis.setMyLocationButtonEnabled(true);

        uis.setCompassEnabled(true);
        uis.setZoomControlsEnabled(true);
        uis.setMapToolbarEnabled(true);

        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                POSIZIONE_VENETO_CENTRALE, 8.0f)
        );

        applyMapSettings();

        putMarkers();
    }


    /**
     * Metodo proprietario che forza l'applicazione le impostazioni (o preferenze) che riguardano la mappa.
     */

    protected void applyMapSettings() {
        if (gMap != null) {
            Log.d(TAG, "applying map settings");
            gMap.setMapType(SettingsActivity.getMapStyle(this));
        }
    }

    // marker stuff
    //
    //

    private boolean confrontaEnable = false;

    /**
     * Callback che viene invocata quando viene cliccato un marker.
     * Questo metodo viene invocato al click di QUALUNQUE marker nella mappa; pertanto, se è necessario
     * eseguire comportamenti specifici per un certo marker o gruppo di marker, va modificato questo metodo
     * con codice che si occupa di discernere tra un marker e l'altro in qualche modo.
     *
     * @param marker il marker che è stato cliccato.
     * @return ritorna true per continuare a chiamare altre callback nella catena di callback per i marker; false altrimenti.
     */
    @Override
    public boolean onMarkerClick(final Marker marker) {
        marker.setSnippet(manageMarkerDescription(marker));
        return false; // continue to call back the default staff (zoom, and show info)
    }

    // change the description of the marker based on the situation
    private String manageMarkerDescription(Marker marker) {
        if (confrontaEnable && !confrontaUlssList.contains(marker)) {
            // we are in the confronto modality but the ulss is not in the confronto list
            return getString(R.string.add_to_confronto);
        } else if (confrontaUlssList.contains(marker)) {// ulss is in confronto list
            return getString(R.string.remove_from_confronto);
        }
        return getString(R.string.marker_description); // normal mode
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        marker.hideInfoWindow(); // hide the infoWindow in all the cases

        if (confrontaEnable) { // manage the confronto modality
            confrontaMarkerClick(marker);
        } else { // manage the normal modality -> ricerca in dettaglio
            Intent ric_in_dett = new Intent(this, RicercaInDettaglioActivity.class);

            ric_in_dett.putExtra("ULSS name", marker.getTitle());
            ric_in_dett.putExtra("codiceEnte", mapDenominazioneCodice.get(marker.getTitle()));
            startActivity(ric_in_dett);
        }

    }

    // putMarkers code
    private void putMarkers() {
        Collection<Marker> markers = new LinkedList();

        for (ULSS ulss : DBHelper.getSingleton().getULSS()) {
            MarkerOptions opts = new MarkerOptions()
                    .title(ulss.getDescrizione())
                    .position(ulss.getCoordinate())
                    .snippet(getString(R.string.marker_description));
            markers.add(gMap.addMarker(opts));

            mapDenominazioneCodice.put(ulss.getDescrizione(), ulss.getCodiceEnte());
        }
        this.markers = markers;
    }

    // confronto multiplo stuff from here
    private Button confrontoMultiploButton;
    private Set<Marker> confrontaUlssList; // will contains the long choosed marker
    private String queryConfrontoSearch = "";

    private void removeUlssConfronto(Marker marker) {
        confrontaUlssList.remove(marker); // remove from the list of pressed marker
        marker.setIcon(BitmapDescriptorFactory.defaultMarker()); // back to normal color

        if (confrontaUlssList.size() <= 1) {
            confrontoMultiploButton.setVisibility(View.INVISIBLE);
        }
    }

    private void addUlssConfronto(Marker marker) {
        confrontaUlssList.add(marker);
        marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));

        if (confrontaUlssList.size() >= 2) {
            confrontoMultiploButton.setVisibility(View.VISIBLE);
        }
    }

    private void confrontaMarkerClick(Marker marker) {
        if (confrontaUlssList.contains(marker)) {
            removeUlssConfronto(marker);
        } else {
            addUlssConfronto(marker);
        }
    }

    /**
     * here there
     *
     * @param view
     */
    public void confrontoMultiplo(View view) {
        Intent confrontoMultiploIntent = new Intent(this, ConfrontoMultiploActivity.class);

        HashMap ulssNameCodiceEnte = new HashMap();
        if (confrontaEnable) {

            for (Marker marker : confrontaUlssList) {//for avoid ConcurrentModificationException
                ulssNameCodiceEnte.put(mapDenominazioneCodice.get(marker.getTitle()), marker.getTitle());
            }
            exitConfrontaModality(); // manage the exit of the compare modality
        } else {
            for (Marker marker : markers) {
                if (marker.isVisible()) {
                    ulssNameCodiceEnte.put(mapDenominazioneCodice.get(marker.getTitle()), marker.getTitle());
                }
            }
        }

        confrontoMultiploIntent.putExtra("query", queryConfrontoSearch);
        confrontoMultiploIntent.putExtra("map", ulssNameCodiceEnte);
        startActivity(confrontoMultiploIntent);
    }

    private Snackbar mSnackBar;

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.drawer_confronta) {
            enterConfrontaModality();
        } else if (id == R.id.drawer_fornitori) {
            startActivity(new Intent(this, FornitoriActivity.class));
        } else if (id == R.id.drawer_impostazioni) {
            startActivity(new Intent(this, SettingsActivity.class));
        } else if (id == R.id.drawer_info) {
            startActivity(new Intent(this, InfoActivity.class));
        } else if (id == R.id.drawer_help) {
            startActivity(new Intent(this, HelpActivity.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //stuff to manage the modality of compare


    private void enterConfrontaModality() {
        mSnackBar = Snackbar.make(findViewById(R.id.nav_view), getString(R.string.confronta_string), Snackbar.LENGTH_INDEFINITE);
        mSnackBar.setAction(R.string.esci_confronta,
                v -> {// snackbar removed by default
                    exitConfrontaModality();
                }
        );

        mSnackBar.getView().post(
                () -> gMap.setPadding(0, 0, 0, mSnackBar.getView().getHeight())
        );
        mSnackBar.show();

        confrontaEnable = true;
    }

    private void exitConfrontaModality() {
        gMap.setPadding(0, 0, 0, 0);

        for (Marker marker : new HashSet<>(confrontaUlssList)) {//for avoid ConcurrentModificationException
            removeUlssConfronto(marker);
        }
        mSnackBar.dismiss();
        confrontaEnable = false;
    }

    /**
     * Created by gianmarcocallegher on 26/01/18.
     */

    private int visibleMarkers() {
        int c = 0;
        for (Marker marker : markers) {
            if (marker.isVisible())
                c++;
        }
        return c;
    }
}
