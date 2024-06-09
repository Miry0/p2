package it.unisa.control;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import it.unisa.model.Carrello;
import it.unisa.model.ProdottoDao;

@WebServlet("/dettagli")
public class DettagliServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    ProdottoDao prodDao = new ProdottoDao();

	    try {
	        String idParam = request.getParameter("id");
	        if (idParam != null && idParam.matches("\\d+")) { // Verifica che id sia un intero positivo
	            int id = Integer.parseInt(idParam);
	            request.getSession().removeAttribute("product");
	            request.getSession().setAttribute("product", prodDao.doRetrieveByKey(id));
	        } else {
	            // Id non è un intero valido, gestire di conseguenza
	            // Ad esempio, reindirizzare l'utente a una pagina di errore o visualizzare un messaggio di errore
	            // Qui puoi gestire il caso in cui il parametro id non sia un numero valido
	        }
	    } catch (SQLException e) {
	        System.out.println("Error:" + e.getMessage());
	    }

	    /*RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/Dettagli.jsp");
	    dispatcher.forward(request, response);*/
	    response.sendRedirect(request.getContextPath() + "/Dettagli.jsp");
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		doGet(request, response);
	}

}
