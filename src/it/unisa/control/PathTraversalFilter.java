package it.unisa.control;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.IOException;
import java.util.ArrayList;

@WebFilter("/RicercaProdotto")
public class PathTraversalFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Inizializzazione del filtro
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;

        // Sovrascriviamo la richiesta per tradurre i caratteri sensibili
        HttpServletRequest wrappedRequest = new PathTraversalWrapper(httpRequest);

        // Passiamo la richiesta modificata alla catena dei filtri successivi o alla servlet
        chain.doFilter(wrappedRequest, response);
    }

    @Override
    public void destroy() {
        // Operazioni di pulizia del filtro
    }

    // Implementazione di HttpServletRequestWrapper per tradurre i caratteri sensibili
    static class PathTraversalWrapper extends HttpServletRequestWrapper {

        public PathTraversalWrapper(HttpServletRequest request) {
            super(request);
        }

        @Override
        public String getParameter(String parameter) {
            String originalValue = super.getParameter(parameter);
            return translatePathTraversal(originalValue);
        }

        // Metodo per tradurre i caratteri sensibili
        private String translatePathTraversal(String value) {
            // Esegui qui la logica per tradurre i caratteri sensibili
            // Ad esempio, puoi sostituire "../" con una stringa vuota o con un altro valore
            // Assicurati di implementare questa logica in modo sicuro e accurato per evitare vulnerabilità di path traversal
            return value != null ? value.replaceAll("\\.\\./", "") : null;
        }
    }
}