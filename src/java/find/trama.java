/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package find;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Marco
 */
public class trama extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    
    private String DB_URL = "jdbc:mysql://localhost/cinema";
    private String USER = "root";
    private String PASS = "";
    public static int numVisitatori;
    protected void processRequest(HttpServletRequest request, HttpServletResponse response, ResultSet registi, ResultSet cast, String trama, Short anno, String titolo)
            throws ServletException, IOException, SQLException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet trama</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Scheda film per: " + titolo + " (" + anno + ")</h1>");
            out.println("<h3>Regia:</h3><br><ul>");
            while(registi.next()){
                out.println("<li>" + registi.getString("nome") + " " + registi.getString("cognome") + "</li>");
            }
            out.println("</ul><br><h3>Cast: </h3><br><ul>");
            while(cast.next()){
                out.println("<li>" + cast.getString("nome") + " " + cast.getString("cognome") + "</li>");
            }
            out.println("</ul><br><h3>Trama: </h3><br>");
            
            
            out.println("<p>" + trama + "</p>");
            Cookie[] cookies = request.getCookies();
            String cookieName = "visite" + titolo.replaceAll("\\s+","");
            for(Cookie cookie : cookies){
                if(cookieName.equals(cookie.getName())){
                    String value = cookie.getValue();
                    out.println("<p>Tue visite: " + value + "</p>");
                    
                }
            }
            out.println("<p>Numero totale visitatori: " + numVisitatori + "</p>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            numVisitatori++;
            int valueInt = 1;
            Cookie[] cookies = request.getCookies();
            
            
            String trama = "", titolo = "";
            ResultSet registi, cast;
            Short data = 0;
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = (Connection) DriverManager.getConnection(DB_URL, USER, PASS);
            Statement stmTitolo = (Statement) conn.createStatement();
            Statement stmAnno = (Statement) conn.createStatement();
            Statement stmTrama = (Statement) conn.createStatement();
            Statement stmRegisti = (Statement) conn.createStatement();
            Statement stmCast = (Statement) conn.createStatement();
            String sql = "select distinct f.titolo from "
                    +    "film f where f.titolo like '%" + request.getParameter("titolo") + "%'";
            ResultSet rs;
            rs = stmTitolo.executeQuery(sql);
            
            while(rs.next()){
                titolo = rs.getString("titolo");
            }
            String cookieName = "visite" + titolo.replaceAll("\\s+","");
            for(Cookie cookie : cookies){
                if(cookieName.equals(cookie.getName())){
                    String value = cookie.getValue();
                    valueInt = Integer.parseInt(value) + 1;
                    
                }
            }
            Cookie visit = new Cookie(cookieName, Integer.toString(valueInt));
            visit.setMaxAge(60*60*24*30);
            response.addCookie(visit);
            sql = "select f.annopub from film f where f.titolo = '" + titolo + "'";
            rs = stmAnno.executeQuery(sql);
            
            while(rs.next()){
                data = rs.getShort("annopub");
            }
            sql = "select f.trama from film f where f.titolo = '" + titolo + "'";
            rs = stmTrama.executeQuery(sql);
            
            while(rs.next()){
                trama = rs.getString("trama");
            }
            sql = "select distinct a.nome, a.cognome from (attori a inner join cast c on a.idatt=c.idatt) "
                    + "inner join film f on c.idfilm=f.idfilm where f.titolo = '" + titolo + "'";
            cast = stmRegisti.executeQuery(sql);
            sql = "select distinct r.nome, r.cognome from (registi r inner join regia c on r.idreg=c.idreg) "
                    + "inner join film f on c.idfilm=f.idfilm where f.titolo = '" + titolo + "'";
            registi = stmCast.executeQuery(sql);
            
            
            processRequest(request, response, registi, cast, trama, data, titolo);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(findcategory.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException exception) {
            String str = exception.getMessage();
            Logger.getLogger(findcategory.class.getName()).log(Level.SEVERE, null, exception);
        }
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    /*@Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }*/

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
