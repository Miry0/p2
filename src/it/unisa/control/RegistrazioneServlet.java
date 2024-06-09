package it.unisa.control;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import it.unisa.model.UserBean;
import it.unisa.model.UserDao;

@WebServlet("/Registrazione")
public class RegistrazioneServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        UserDao dao = new UserDao();
        String nome = request.getParameter("nome");
        String cognome = request.getParameter("cognome");
        String email = request.getParameter("email");
        String dataNascita = request.getParameter("nascita");
        String username = request.getParameter("us");
        String pwd = request.getParameter("pw");

        // Verifica che il nome contenga solo lettere
        if (!Pattern.matches("[A-Za-z]+", nome)) {
            request.setAttribute("errore", "Il nome non è valido, reinserire correttamente.");
            request.getRequestDispatcher("/registrazione.jsp").forward(request, response);
            return;
        }

        // Verifica che il cognome contenga solo lettere
        if (!Pattern.matches("[A-Za-z]+", cognome)) {
            request.setAttribute("errore", "Il cognome non è valido, reinserire correttamente.");
            request.getRequestDispatcher("/registrazione.jsp").forward(request, response);
            return;
        }

        // Verifica che l'email abbia il formato corretto
        if (!Pattern.matches("[\\w\\.-]+@[\\w\\.-]+\\.\\w+", email)) {
            request.setAttribute("errore", "L'email non è valida, reinserire correttamente.");
            request.getRequestDispatcher("/registrazione.jsp").forward(request, response);
            return;
        }

        // Verifica che la data di nascita abbia il formato corretto
        if (!Pattern.matches("\\d{4}-\\d{2}-\\d{2}", dataNascita)) {
            request.setAttribute("errore", "La data di nascita non è valida, reinserire correttamente.");
            request.getRequestDispatcher("/registrazione.jsp").forward(request, response);
            return;
        }

        // Verifica che lo username abbia il formato corretto
        if (!Pattern.matches("[\\w.-]+", username)) {
            request.setAttribute("errore", "Lo username non è valido, reinserire correttamente.");
            request.getRequestDispatcher("/registrazione.jsp").forward(request, response);
            return;
        }

        // Verifica che la password abbia il formato corretto
        if (!Pattern.matches("[\\w!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]+", pwd)) {
            request.setAttribute("errore", "La password non è valida, reinserire correttamente.");
            request.getRequestDispatcher("/registrazione.jsp").forward(request, response);
            return;
        }

        try {

            UserBean user = new UserBean();
            user.setNome(nome);
            user.setCognome(cognome);
            user.setEmail(email);
            user.setDataDiNascita(Date.valueOf(dataNascita));
            user.setUsername(username);
            user.setPassword(pwd);
            user.setAmministratore(false);
            user.setCap(null);
            user.setIndirizzo(null);
            user.setCartaDiCredito(null);
            dao.doSave(user);

        } catch (SQLException e) {
            System.out.println("Error:" + e.getMessage());
        }

        response.sendRedirect(request.getContextPath() + "/Home.jsp");

    }
}
