package hotelreservation;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebInitParam;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(
    urlPatterns = "/cancel",
    initParams = {
        @WebInitParam(name = "driver", value = "com.mysql.cj.jdbc.Driver"),
        @WebInitParam(name = "url", value = "jdbc:mysql://localhost:3306/hotel_db"),
        @WebInitParam(name = "user", value = "root"),
        @WebInitParam(name = "password", value = "khushi123")
    }
)
public class Cancel extends HttpServlet {

    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        res.setContentType("text/html");
        PrintWriter out = res.getWriter();

        out.println("<html><head><title>Cancel Reservation</title>");
        out.println("<style>");
        out.println("body{font-family:Arial;background:#fff8e1;padding:20px;}");
        out.println("form{background:#ffe082;padding:20px;border-radius:8px;max-width:400px;margin:auto;");
        out.println("box-shadow:0 0 15px rgba(255,152,0,0.3);}");
        out.println("input{width:100%;padding:8px;margin:6px 0;border:1px solid #ffb300;");
        out.println("border-radius:4px;background:#fffde7;}");
        out.println("input[type=submit]{background:#d32f2f;color:#fff;border:none;cursor:pointer;font-weight:bold;}");
        out.println("input[type=submit]:hover{background:#b71c1c;}");
        out.println("h2{text-align:center;color:#e65100;}");
        out.println("</style></head><body>");

        out.println("<h2>Cancel Reservation</h2>");
        out.println("<form action='cancel' method='post'>");
        out.println("Name <input type='name' name='name' required>");
        out.println("Email: <input type='email' name='email' required>");
        out.println("<input type='submit' value='Cancel Reservation'>");
        out.println("</form>");

        out.println("</body></html>");
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        res.setContentType("text/html");
        PrintWriter out = res.getWriter();

        String name = req.getParameter("name");
        String email = req.getParameter("email");

        try {
            String driver = getServletConfig().getInitParameter("driver");
            String url = getServletConfig().getInitParameter("url");
            String user = getServletConfig().getInitParameter("user");
            String password = getServletConfig().getInitParameter("password");

            Class.forName(driver);
            Connection conn = DriverManager.getConnection(url, user, password);

            String sql = "DELETE FROM bookings WHERE name = ? AND email = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1,name);
            ps.setString(2, email);

            int rows = ps.executeUpdate();
            out.println("<html><body style='font-family:Arial;text-align:center;padding:20px;'>");

            if (rows > 0) {
                out.println("<h2 style='color:green;'>Reservation cancelled successfully!</h2>");
            } else {
                out.println("<h2 style='color:red;'>No matching booking found. Please check your details.</h2>");
            }

            out.println("<a href='book'>Book Another Room</a>");
            out.println("</body></html>");

            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
            out.println("<h3 style='color:red;'>Error: " + e.getMessage() + "</h3>");
        }
    }
}
