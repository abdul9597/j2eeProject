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
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author apple
 */
public class Dispatch extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
         ResultSet res = null;
         ResultSet res2 = null;
        try {
            stmt = cnx.createStatement() ;
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.exit(-1);
        }
        
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
        
            String nom=request.getParameter("nom");
            String pseudo=request.getParameter("pseudo");
            String option=request.getParameter("option");

            
            
      
                if(option.equals("emprunt")){ 
                    
                    
                String sql =  "SELECT quantite FROM BIBLIOTHEQUE WHERE nom='"+nom+"'";
     
                try {
                     res = stmt.executeQuery(sql) ;
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    System.exit(-1);
                }
                
                int quantite = 0;
                try {
                    while (res.next()){
                        quantite = res.getInt("quantite");    
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(Dispatch.class.getName()).log(Level.SEVERE, null, ex);
                }
                String resql;
                String sql2;
                
                boolean disponible=false;
                
                if(quantite>0){
                     resql="UPDATE BIBLIOTHEQUE SET quantite = quantite - 1 WHERE nom='"+ nom+"'";
                     sql ="SELECT quantite FROM EMPRUNTS WHERE livre ='"+nom+"' AND pseudo ='"+pseudo+"'";
                        
                      disponible=true;
                      int rs = 999;
             try {
                 rs = stmt.executeUpdate(resql);
                 res = stmt.executeQuery(sql);
                 } catch (SQLException ex) {
              ex.printStackTrace();
                System.exit(-1);
                 }
             
             try{
                 boolean exist =false;
                 while (res.next()){
                        res.updateInt("quantite", res.getInt("quantite")+1); 
                        exist =true;
                    }
                   if (!exist){
                      sql = "INSERT INTO EMPRUNTS VALUES  ('"+pseudo+"','"+nom+"', 1)";
                       rs = stmt.executeUpdate(sql);
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(Dispatch.class.getName()).log(Level.SEVERE, null, ex);
                }
                }
                
             }      
           
            
            if(option.equals("rendre")){
                 String sql;
                 String sql2;
                
                sql ="SELECT quantite FROM EMPRUNTS WHERE livre ='"+nom+"' AND pseudo ='"+pseudo+"' AND quantite =1";
                sql2= "SELECT quantite FROM EMPRUNTS WHERE livre ='"+nom+"' AND pseudo ='"+pseudo+"' AND quantite > 1";
     
                try {
                    
                     res = stmt.executeQuery(sql) ;
                     res2 = stmt.executeQuery(sql2) ;
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    System.exit(-1);
                }
                
                int quantite = 0;
                try {
                    while (res.next()){
                        res.deleteRow();  
                    }
                    if((res2 != null))
                    {
                        while (res2.next()){
                        res2.updateInt("quantite", res2.getInt("quantite")+1);  
                        }
                    }             
                } catch (SQLException ex) {
                    Logger.getLogger(Dispatch.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                
            String resql =  "UPDATE BIBLIOTHEQUE SET quantite = quantite + 1 WHERE nom='"+nom+"'";

            int rs = 999;
             try {
                 rs = stmt.executeUpdate(resql);
                 } catch (SQLException ex) {
              ex.printStackTrace();
                System.exit(-1);
                 }

            
             }      
            
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet Dispatch</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet Dispatch at " + request.getContextPath() + "</h1>");
            
            out.println("votre option est "+option);
            if(option=="rendre"){
            out.println("<script type=\"text/javascript\">");
            out.println("alert('Votre livre est rendu');");
            out.println("location='UserActivity?pseudo="+pseudo+"';");
            out.println("</script>");
            }else if(option == "emprunt"){
            out.println("<script type=\"text/javascript\">");
            out.println("alert('Votre livre est emprunté');");
            out.println("location='UserActivity?pseudo="+pseudo+"';");
            out.println("</script>");
            }
            
          
            out.println("</body>");
            out.println("</html>");
        
        
          try {
            cnx.close();
            stmt.close();
            res.close();
            
        
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        } 
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
