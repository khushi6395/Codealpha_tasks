package studentgrader;

import java.io.*;
import java.sql.*;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
public class Newuser extends HttpServlet {
    public void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
      
    	PrintWriter out = res.getWriter();
    	res.setContentType("text/html");

        // Get DB config from web.xml
        ServletContext d_obj = getServletContext();
        String dname = d_obj.getInitParameter("driver");
        String co = d_obj.getInitParameter("connection");
        String uname = d_obj.getInitParameter("dusername");
        String pas = d_obj.getInitParameter("dpassword");
        String Name=req.getParameter("name");
        String id=req.getParameter("email");
        String pass=req.getParameter("password");
       

        try {
            // Load the JDBC driver
            Class.forName(dname);
         

            // Establish connection
            Connection con = DriverManager.getConnection(co, uname, pas);
          
        
           PreparedStatement ps=con.prepareStatement("INSERT INTO login (email,name,pass)VALUES(?,?,?)");
           ps.setString(1,id);
           ps.setString(2,Name);
           ps.setString(3,pass);
          
           int rows = ps.executeUpdate();
           out.println("data inserted");
          
           if (rows > 0) {
        	            out.println("<!DOCTYPE html>");
        		        out.println("<html>");
        		        out.println("<head>");
        		        out.println("<title>useradded</title>");
        		        out.println("<style>");
        		        out.println(".popup-box {");
        		        out.println("  margin: 100px auto;");
        		        out.println("  padding: 20px;");
        		        out.println("  width: 300px;");
        		        out.println("  background-color: #f8f8f8;");
        		        out.println("  border: 2px solid #444;");
        		        out.println("  border-radius: 10px;");
        		        out.println("  text-align: center;");
        		        out.println("  font-family: Arial, sans-serif;");
        		        out.println("  box-shadow: 0 0 10px rgba(0,0,0,0.3);");
        		        out.println("}");
        		        out.println("</style>");
        		        out.println("</head>");
        		        out.println("<body>");

        		        out.println("<div class='popup-box'>");
        		        out.println("<h3>Message</h3>");
        		        out.println("<p>Registered Successfully</p>");
        		        out.println("<a href='index.html'>OK</a>");
        		        out.println("</div>");

        		        out.println("</body>");
        		        out.println("</html>");
           }
		

            // Close resources
          
            con.close();

        } catch (Exception e) {
            out.print("<h3>Error: " + e.getMessage() + "</h3>");
        }
   
}
}
