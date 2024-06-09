package it.unisa.control;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import it.unisa.model.IndirizzoSpedizioneBean;
import it.unisa.model.IndirizzoSpedizioneDao;
import it.unisa.model.MetodoPagamentoBean;
import it.unisa.model.MetodoPagamentoDao;
import it.unisa.model.UserBean;
import it.unisa.model.UserDao;

@WebServlet("/account")
public class AccountServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String redirectedPage = request.getParameter("page");

        // Validazione e sanitizzazione del parametro di input
        if (!whitelist.isPageAllowed(redirectedPage)) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid page parameter");
            return;
        }

        UserDao daoUser = new UserDao();
        UserBean user = (UserBean) request.getSession().getAttribute("currentSessionUser");
        IndirizzoSpedizioneBean sped = new IndirizzoSpedizioneBean();
        IndirizzoSpedizioneDao daoSped = new IndirizzoSpedizioneDao();
        MetodoPagamentoBean pag = new MetodoPagamentoBean();
        MetodoPagamentoDao daoPag = new MetodoPagamentoDao();
        String action = request.getParameter("action");

        String nome = request.getParameter("nome");
        String cognome = request.getParameter("cognome");
        String telefono = request.getParameter("tel");
        String città = request.getParameter("città");
        String ind = request.getParameter("ind");
        String cap = request.getParameter("cap");
        String prov = request.getParameter("prov");

        String tit = request.getParameter("tit");
        String numC = request.getParameter("numC");
        String scad = request.getParameter("scad");

        try {
            if (action != null) {
                if (action.equalsIgnoreCase("addS")) {
                    if (daoSped.doRetrieveByKey(ind, cap) == null) {
                        sped.setNome(nome);
                        sped.setCognome(cognome);
                        sped.setIndirizzo(ind);
                        sped.setTelefono(telefono);
                        sped.setCap(cap);
                        sped.setProvincia(prov);
                        sped.setCittà(città);
                        daoSped.doSave(sped);
                    }
                    daoUser.doUpdateSpedizione(user.getEmail(), ind, cap);
                } else if (action.equalsIgnoreCase("removeS")) {
                    daoUser.doUpdateSpedizione(user.getEmail(), null, null);
                    request.getSession().removeAttribute("spedizione");
                } else if (action.equalsIgnoreCase("addP")) {
                    if (daoPag.doRetrieveByKey(numC) == null) {
                        pag.setTitolare(tit);
                        pag.setNumero(numC);
                        pag.setScadenza(scad);
                        daoPag.doSave(pag);
                    }
                    daoUser.doUpdatePagamento(user.getEmail(), numC);
                } else if (action.equalsIgnoreCase("removeP")) {
                    daoUser.doUpdatePagamento(user.getEmail(), null);
                    request.getSession().removeAttribute("pagamento");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (user.getIndirizzo() != null && user.getCap() != null) {
            try {
                request.getSession().removeAttribute("spedizione");
                request.getSession().setAttribute("spedizione", daoSped.doRetrieveByKey(user.getIndirizzo(), user.getCap()));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        if (user.getCartaDiCredito() != null) {
            try {
                request.getSession().setAttribute("pagamento", daoPag.doRetrieveByKey(user.getCartaDiCredito()));
            } catch (NumberFormatException | SQLException e) {
                e.printStackTrace();
            }
        }

        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/" + redirectedPage);
        dispatcher.forward(request, response);
    }
}
