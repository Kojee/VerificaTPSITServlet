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
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Marco
 */
public class findcategory extends HttpServlet {

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

    protected void processRequest(HttpServletRequest request, HttpServletResponse response, ResultSet rs)
            throws ServletException, IOException, SQLException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Find Category</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("Elenco film per genere selezionato: " + request.getParameter("genere"));
            while(rs.next()){
                out.println("<li>");
                out.println("<a href='localhost/trama?titolo=" + rs.getString("titolo")+ "'>" + rs.getString("titolo") + "</a>");
                out.println("</li>");
            }
            out.println("<li>");
            out.println("</ul>");
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
            String genere = "", titolo = "";
            Short annopub = 0;
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = (Connection) DriverManager.getConnection(DB_URL, USER, PASS);
            Statement stmTitolo = (Statement) conn.createStatement();
            Statement stmAnno = (Statement) conn.createStatement();
            Statement stmGenere = (Statement) conn.createStatement();
            
            String sql = "select distinct f.titolo from "
                    +    "film f where f.genere like '%" + request.getParameter("genere") + "%'";
            ResultSet rs;
            rs = stmTitolo.executeQuery(sql);
            while(rs.next()){
                titolo = rs.getString("titolo");
            }
            sql = "select distinct f.annopub from "
                    +    "film f where f.genere like '%" + request.getParameter("genere") + "%'";
            
            rs = stmTitolo.executeQuery(sql);
            processRequest(request, response, rs);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(findcategory.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(findcategory.class.getName()).log(Level.SEVERE, null, ex);
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
