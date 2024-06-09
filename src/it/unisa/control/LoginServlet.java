package it.unisa.control;

import java.io.IOException;
import java.sql.SQLException;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import it.unisa.model.UserBean;
import it.unisa.model.UserDao;

@WebServlet("/Login")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        UserDao usDao = new UserDao();

        try {

            String username = request.getParameter("un");
            String password = request.getParameter("pw");

            // Controllo che username e password rispettino i criteri di sicurezza
            if (!isValidInput(username) || !isValidInput(password)) {
                response.sendRedirect(request.getContextPath() + "/Login.jsp?action=error");
                return;
            }

            UserBean user = usDao.doRetrieve(username, password);

            String checkout = request.getParameter("checkout");

            if (user.isValid()) {
                HttpSession session = request.getSession(true);
                session.setAttribute("currentSessionUser", user);
                if (checkout != null)
                    response.sendRedirect(request.getContextPath() + "/account?page=Checkout.jsp");
                else
                    response.sendRedirect(request.getContextPath() + "/Home.jsp");
            } else
                response.sendRedirect(request.getContextPath() + "/Login.jsp?action=error"); // error page
        } catch (SQLException e) {
            System.out.println("Error:" + e.getMessage());
        }
    }

    // Metodo per controllare se l'input è valido utilizzando Pattern.matches
    private boolean isValidInput(String input) {
        return Pattern.matches("^\\w+$", input);
    }
}
