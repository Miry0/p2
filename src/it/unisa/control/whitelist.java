package it.unisa.control;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class whitelist {
	// Whitelist delle pagine consentite
    private static final Set<String> ALLOWED_PAGES = new HashSet<>(Arrays.asList(
        "Home.jsp",
        "Login.jsp",
        "Account.jsp",
        "Profile.jsp",
        "Catalogo.jsp",
        "Carrello.jsp",
        "Dettagli.jps",
        "Registrazione.jsp",
        "MieiOrdini.jsp",
        "Checkout.jsp",
        "ComposizioneOrdine.jsp",
        "Ps4.jsp",
        "Ps5.jsp",
        "Switch.jsp",
        "XboxOne.jsp", 
        "XboxSeries.jsp"
        
        // Aggiungi altre pagine consentite qui
    ));

    public static boolean isPageAllowed(String page) {
        return ALLOWED_PAGES.contains(page);
    }
}
