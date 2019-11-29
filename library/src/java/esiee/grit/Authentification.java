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
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author abdul9597
 */
public class Authentification extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
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
        ResultSet res = null;
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            String option=request.getParameter("option");
            String pseudo=request.getParameter("login");
            String mdp=request.getParameter("mdp");
            int acces = 0;
            if (option.compareTo("login")==0){
                String sql =  "SELECT * FROM UTILISATEURS";
                try {
                     res = stmt.executeQuery(sql) ;
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    System.exit(-1);
                }
                
                try {
                    while (res.next()&&acces==0) {
                        if(res.getString("pseudo").equalsIgnoreCase(pseudo)&&res.getString("mdp").equals(mdp)){
                            acces = 1;
                        }
                    }
                    
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    System.exit(-1);
                }
            }else{
               String sql =  "SELECT * FROM UTILISATEURS"   ;
                try {
                     res = stmt.executeQuery(sql) ;
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    System.exit(-1);
                }
                
                try {
                    while (res.next()&&acces==0) {
                        if(res.getString("pseudo").equalsIgnoreCase(pseudo)){
                            acces = 2;
                        }
                    }
                    if(acces==0){
                        String resql =  "Insert Into UTILISATEURS VALUES"+"('"+pseudo+"','"+mdp+"',false)" ;
                        int rs = 999;
                        try {
                            rs = stmt.executeUpdate(resql);
                        } catch (SQLException ex) {
                            ex.printStackTrace();
                            System.exit(-1);
                        }
                        acces=3;
                        System.out.println("Resultat: "+rs);
                    }
                    
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    System.exit(-1);
                }
            }
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet Authentification</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet Authentification at " + request.getContextPath() + "</h1>");
            if (acces==1){
               out.println("<script type=\"text/javascript\">");
                 out.println("alert('Succes');");
                 out.println("location='UserActivity?pseudo="+pseudo+"';");
                 out.println("</script>");
            }else if (acces==2){
                 out.println("<script type=\"text/javascript\">");
                 out.println("alert('Pseudo non disponible');");
                 out.println("location='index.html';");
                 out.println("</script>");
                 
                
            }else if (acces==3){
                out.println("<script type=\"text/javascript\">");
                 out.println("alert('Utilisateur Cree');");
                 out.println("location='UserActivity?pseudo="+pseudo+"';");
                 out.println("</script>");
            }else{
                out.println("<script type=\"text/javascript\">");
                out.println("alert('Mot de passe ou user incorrect ');");
                out.println("location='index.html';");
                out.println("</script>");
            }
            
            out.println("</body>");
            out.println("</html>");
            
        }
        
        try {
            cnx.close();
            stmt.close();
            res.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
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
