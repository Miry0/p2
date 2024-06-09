package it.unisa.control;

import java.io.IOException;
import java.sql.SQLException;
import java.util.regex.Pattern;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import it.unisa.model.ComposizioneDao;
import it.unisa.model.OrdineDao;
import it.unisa.model.UserBean;

@WebServlet("/Ordine")
public class OrdineServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        OrdineDao ordDao = new OrdineDao();
        ComposizioneDao compDao = new ComposizioneDao();
        UserBean user = (UserBean) request.getSession().getAttribute("currentSessionUser");

        String action = request.getParameter("action");

        try {
            if (action != null) {
                if (action.equalsIgnoreCase("mieiOrdini")) {
                    String email = user.getEmail();
                    request.getSession().removeAttribute("ordini");
                    request.getSession().setAttribute("ordini", ordDao.doRetrieveByEmail(email));
                    RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/MieiOrdini.jsp");
                    dispatcher.forward(request, response);

                } else if (action.equalsIgnoreCase("dettagliOrdine")) {
                    String idParam = request.getParameter("id");
                    if (idParam != null && Pattern.matches("\\d+", idParam)) {
                        int id = Integer.parseInt(idParam);
                        request.getSession().removeAttribute("composizione");
                        request.getSession().setAttribute("composizione", compDao.doRetrieveByOrdine(id));
                        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/ComposizioneOrdine.jsp");
                        dispatcher.forward(request, response);
                    } else {
                        // Gestisci il caso in cui id non sia un numero valido
                        // Ad esempio, reindirizza l'utente a una pagina di errore o visualizza un messaggio di errore
                        // Qui puoi gestire il caso in cui il parametro id non sia un numero valido
                    }
                }
            }

        } catch (SQLException e) {
            System.out.println("Error:" + e.getMessage());
        }
    }

}
