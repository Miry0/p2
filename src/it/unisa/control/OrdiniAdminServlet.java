package it.unisa.control;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;
import java.util.regex.Pattern;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import it.unisa.model.OrdineDao;

@WebServlet("/ordiniAdmin")
public class OrdiniAdminServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        OrdineDao ord = new OrdineDao();
        String action = request.getParameter("action");

        try {
            if (action != null) {

                if (action.equalsIgnoreCase("data")) {
                    String data1 = request.getParameter("dal");
                    String data2 = request.getParameter("al");
                    if (isValidDate(data1) && isValidDate(data2)) {
                        request.getSession().setAttribute("adminOrdini", ord.doRetrieveByDate(data1, data2));
                    } else {
                        // Gestisci il caso in cui le date non siano nel formato corretto
                        // Ad esempio, reindirizza l'utente a una pagina di errore o visualizza un messaggio di errore
                    }

                } else if (action.equalsIgnoreCase("nominativo")) {
                    String nome = request.getParameter("nome");
                    String cognome = request.getParameter("cognome");
                    if (isValidName(nome) && isValidName(cognome)) {
                        request.getSession().setAttribute("adminOrdini", ord.doRetrieveByNominativo(nome, cognome));
                    } else {
                        // Gestisci il caso in cui nome o cognome non siano nel formato corretto
                        // Ad esempio, reindirizza l'utente a una pagina di errore o visualizza un messaggio di errore
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();

        }

        response.sendRedirect(request.getContextPath() + "/admin/ViewOrdini.jsp");
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    // Metodo per verificare se una stringa rappresenta una data nel formato desiderato (yyyy-MM-dd)
    private boolean isValidDate(String date) {
        return Pattern.matches("\\d{4}-\\d{2}-\\d{2}", date);
    }

    // Metodo per verificare se una stringa rappresenta un nome o cognome valido (solo lettere)
    private boolean isValidName(String name) {
        return Pattern.matches("[a-zA-Z]+", name);
    }
}

	
	