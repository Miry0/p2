package it.unisa.control;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import it.unisa.model.UserBean;
import it.unisa.model.UserDao;

@WebServlet("/CheckEmail")
public class CheckEmailServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        UserDao dao = new UserDao();
        String email = request.getParameter("em");

        // Validazione dell'indirizzo email utilizzando un'espressione regolare
        if (!isValidEmail(email)) {
            response.getWriter().write("0");
            return;
        }

        ArrayList<UserBean> users;
        try {
            users = dao.doRetrieveAll(null);
            for (UserBean user : users) {
                if (user.getEmail().equals(email)) {
                    response.getWriter().write("0");
                    return;
                }
            }
            response.getWriter().write("1");
            return;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Funzione per validare l'indirizzo email utilizzando un'espressione regolare
    private boolean isValidEmail(String email) {
        // Espressione regolare per la validazione dell'indirizzo email
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        
        return matcher.matches();
    }
}
