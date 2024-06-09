package it.unisa.control;

import java.io.IOException;
import java.util.regex.Pattern;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import it.unisa.model.*;

@WebServlet("/Checkout")
public class CheckoutServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        ProdottoDao daoProd = new ProdottoDao();
        OrdineDao daoOrd = new OrdineDao();
        ComposizioneDao daoComp = new ComposizioneDao();
        IndirizzoSpedizioneDao daoSped = new IndirizzoSpedizioneDao();
        MetodoPagamentoDao daoPag = new MetodoPagamentoDao();

        UserBean user = (UserBean) request.getSession().getAttribute("currentSessionUser");
        OrdineBean ordine = new OrdineBean();
        ComposizioneBean comp = new ComposizioneBean();
        IndirizzoSpedizioneBean sped = new IndirizzoSpedizioneBean();
        MetodoPagamentoBean pag = new MetodoPagamentoBean();

        Carrello cart = (Carrello) request.getSession().getAttribute("cart");
        Double prezzoTot = cart.calcolaCosto();

        Date now = new Date();
        String pattern = "yyyy-MM-dd";
        SimpleDateFormat formatter = new SimpleDateFormat(pattern);
        String mysqlDateString = formatter.format(now);

        String nome = request.getParameter("nome");
        if (!Pattern.matches("^[A-Za-z]+$", nome)) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Il nome non è valido");
            return;
        }

        String cognome = request.getParameter("cognome");
        if (!Pattern.matches("^[A-Za-z]+$", cognome)) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Il cognome non è valido");
            return;
        }

        String telefono = request.getParameter("tel");
        if (!Pattern.matches("^[0-9]{10}$", telefono)) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Il telefono non è valido");
            return;
        }

        String città = request.getParameter("città");
        if (!Pattern.matches("^[A-Za-z]+$", città)) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "La città non è valida");
            return;
        }

        String ind = request.getParameter("ind");
        if (!Pattern.matches("^[a-zA-Z0-9\\s,.'-]+$", ind)) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "L'indirizzo non è valido");
            return;
        }

        String cap = request.getParameter("cap");
        if (!Pattern.matches("^[0-9]{5}$", cap)) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Il CAP non è valido");
            return;
        }

        String prov = request.getParameter("prov");
        if (!Pattern.matches("^[A-Za-z]{2}$", prov)) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "La provincia non è valida");
            return;
        }

        String tit = request.getParameter("tit");
        if (!Pattern.matches("^[A-Za-z\\s]+$", tit)) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Il nome del titolare non è valido");
            return;
        }

        String numC = request.getParameter("numC");
        if (!Pattern.matches("^[0-9]{16}$", numC)) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Il numero di carta di credito non è valido");
            return;
        }

        String scad = request.getParameter("scad");
        if (!Pattern.matches("^(0[1-9]|1[0-2])\\/[0-9]{2}$", scad)) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "La scadenza non è valida");
            return;
        }

        try {

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

            if (daoPag.doRetrieveByKey(numC) == null) {
                pag.setTitolare(tit);
                pag.setNumero(numC);
                pag.setScadenza(scad);
                daoPag.doSave(pag);
            }

            ordine.setEmail(user.getEmail());
            ordine.setIndirizzo(ind);
            ordine.setCap(cap);
            ordine.setCartaCredito(numC);
            ordine.setData(mysqlDateString);
            ordine.setStato("confermato");
            ordine.setImportoTotale(prezzoTot);
            daoOrd.doSave(ordine);

            ArrayList<OrdineBean> ordini = daoOrd.doRetrieveByEmail(user.getEmail());
            int newId = ordini.get(ordini.size() - 1).getIdOrdine();


            for (int i = 0; i < cart.size(); i++) {
                int qnt = cart.get(i).getQuantitàCarrello();
                ProdottoBean prod = cart.get(i).getProdotto();
                int newQnt = prod.getQuantità() - qnt;

                daoProd.doUpdateQnt(cart.get(i).getId(), newQnt);

                comp.setIdOrdine(newId);
                comp.setIdProdotto(cart.get(i).getId());
                comp.setPrezzoTotale(cart.get(i).getTotalPrice());
                comp.setIva(cart.get(i).getProdotto().getIva());
                comp.setQuantità(cart.get(i).getQuantitàCarrello());
                daoComp.doSave(comp);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }
        request.getSession().removeAttribute("cart");
        response.sendRedirect(request.getContextPath() + "/Home.jsp");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
