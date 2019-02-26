/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author ksiundu
 */
@WebServlet(name = "payment", urlPatterns = {"/payment/mpesa"})
public class payment extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected String processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
        String result = "";
        try {
            StringBuilder sb = new StringBuilder();
            BufferedReader br = request.getReader();
            String str = "";
            while ((str = br.readLine()) != null) {
                sb.append(str);
                System.out.println(str);
            }
            
            String url = "https://testgateway.ekenya.co.ke:8443/ServiceLayer/onlinecheckout/request";
            JSONObject json = new JSONObject(sb.toString());
            String phone = json.getString("number");
            String amount = json.getString("amount");
            
            JSONObject finalJSON = new JSONObject();
            finalJSON.put("username", "wafula.siundu@ekenya.co.ke");
            finalJSON.put("password", "4f26551309eff4b08fd5dee9d8a103c78292d6fff99f3893735204effe9157184d072c0071c43541f77cf1c28a806c6eca9aab0803177a257c3b3d6a22d6e08f");
            finalJSON.put("amount", amount);
            finalJSON.put("clientid", "5062");
            finalJSON.put("accountno", phone);
            finalJSON.put("narration", "(Test)");
            finalJSON.put("serviceid", "5067");
            finalJSON.put("msisdn", phone);
            finalJSON.put("transactionid", "XA37701786653095");
            finalJSON.put("accountreference", "FTMM");
            
            String jsonString = finalJSON.toString();
            String finalResponse = POST(url, jsonString);
            out.print(finalResponse);
            out.flush();
            return finalResponse;
        } catch (JSONException ex) {
            return ex.toString();
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

    public static String POST(String query_url, String payload) {
        try {
             URL url = new URL(query_url);
           HttpURLConnection conn = (HttpURLConnection) url.openConnection();
           conn.setConnectTimeout(5000);
           conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
           conn.setDoOutput(true);
           conn.setDoInput(true);
           conn.setRequestMethod("POST");
           OutputStream os = conn.getOutputStream();
           os.write(payload.getBytes("UTF-8"));
           os.close(); 
            // read the response
            InputStream in = new BufferedInputStream(conn.getInputStream());
            String result = IOUtils.toString(in, "UTF-8");
            in.close();
            conn.disconnect();
            return result;
        } catch (IOException ex) {
            Logger.getLogger(payment.class.getName()).log(Level.SEVERE, null, ex);
            return ex.toString();
        }
    }
    
}
