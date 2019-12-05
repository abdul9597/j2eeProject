/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package esiee.grit;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author abdul9597
 */
public class UserActivity extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet UserActivity</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet UserActivity at " + request.getContextPath() + "</h1>");
            
            out.println("<br>");
            out.println("<br>");
           
        String nomDriver="org.apache.derby.jdbc.ClientDriver";
        String urlBD="jdbc:derby://localhost:1527/libraryBDD";

        
        try{  
            Class.forName(nomDriver);
        }catch (ClassNotFoundException ex) {
            ex.printStackTrace();
            System.exit(-1);
        }
        
        Connection cnx = null;
        
        try {
            cnx = DriverManager.getConnection(urlBD);
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.exit(-1);
        }
        
        Statement stmt = null;
        
        try {
            stmt = cnx.createStatement() ;
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.exit(-1);
        }
        
        String sql =  "SELECT * FROM BIBLIOTHEQUE";
        ResultSet res = null;
        
        try {
             res = stmt.executeQuery(sql) ;
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.exit(-1);
        }
        
        
        String pseudo = request.getParameter("pseudo");
        
        
        try {
            out.println("<h1>Liste des livres</h1>");
            out.println("<table border=6 cellspacing=12><tr> <td>Nom du livre</td> <td>Quantité disponible</td></tr>");
            while (res.next()) {
                out.print("<tr><td>"+res.getString("NOM")+"</td><td>"+res.getString("QUANTITE")+"</td>");
                 
               out.println("<td><form action=/library/Dispatch method=Post><input name=\"nom\" value=\""+res.getString("NOM")+"\" type =\"hidden\"/><input name=\"pseudo\" value=\""+pseudo+"\" type =\"hidden\"/> "
                       +"<input name=\"option\" value=\"emprunt\" type =\"hidden\"/>"
                       + "<input type = submit value = empreinter />"
                       + " </form></td></tr>");
                
            }
            
            out.println("</table>");
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.exit(-1);
        }
        
        String sql2 =  "SELECT * FROM EMPRUNTS WHERE PSEUDO = '"+pseudo+"'";
        ResultSet res2 = null;
        
        try {
             res2 = stmt.executeQuery(sql2) ;
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.exit(-1);
        }
        
        try {
            out.println("<h1>Mes emprunts</h1>");
            out.println("<table border=6 cellspacing=12><tr> <td>Pseudo</td><td>Nom du livre</td> <td>Quantité disponible</td></tr>");
            while (res2.next()) {
                out.print("<tr><td>"+res2.getString("PSEUDO")+"</td><td>"+res2.getString("LIVRE")+"</td><td>"+res2.getString("QUANTITE")+"</td>");
//                out.println("<td><input type=\"button\" name="+res2.getString("PSEUDO")+" value=\"rendre\"></td></tr>");
            
            out.println("<td><form action=/library/Dispatch method=Post><input name=\"nom\" value=\""+res2.getString("LIVRE")+"\" type =\"hidden\"/> <input name=\"pseudo\" value=\""+res2.getString("PSEUDO")+"\" type =\"hidden\"/> "
                       +"<input name=\"option\" value=\"rendre\" type =\"hidden\"/>"
                       + "<input type = submit value = rendre />"
                       + " </form></td></tr>");
            
            }
            
            out.println("</table>");
            
          
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.exit(-1);
        }
        
        
        if(pseudo.equalsIgnoreCase("admin")){
                   
         sql2 =  "SELECT * FROM EMPRUNTS WHERE PSEUDO = '"+pseudo+"'";
         res2 = null;
        
        try {
             res2 = stmt.executeQuery(sql2) ;
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.exit(-1);
        }
        
        try {
            out.println("<h1>Les livres empruntés de la librairie</h1>");
            out.println("<table border=6 cellspacing=12><tr> <td>Pseudo</td><td>Nom du livre</td> <td>Quantité disponible</td></tr>");
            while (res2.next()) {
                out.print("<tr><td>"+res2.getString("PSEUDO")+"</td><td>"+res2.getString("LIVRE")+"</td><td>"+res2.getString("QUANTITE")+"</td></tr>");
//                out.println("<td><input type=\"button\" name="+res2.getString("PSEUDO")+" value=\"rendre\"></td></tr>");

            }
            
            out.println("</table>");
            
          
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.exit(-1);
        }
          
         }
             
        try {
            cnx.close();
            stmt.close();
            res.close();
            res2.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        } 
            
             
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
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

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
