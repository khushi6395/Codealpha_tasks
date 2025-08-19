package hotelreservation;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.annotation.WebInitParam;
@WebServlet(
	    urlPatterns = "/book",
	    initParams = {
	        @WebInitParam(name = "driver", value = "com.mysql.cj.jdbc.Driver"),
	        @WebInitParam(name = "url", value = "jdbc:mysql://localhost:3306/hotel_db"),
	        @WebInitParam(name = "user", value = "root"),
	        @WebInitParam(name = "password", value = "khushi123")
	    }
	)
public class Book extends HttpServlet  {

  
      
        protected void doGet(HttpServletRequest req, HttpServletResponse res)
                throws ServletException, IOException {

            res.setContentType("text/html");
            PrintWriter out = res.getWriter();
            out.println("<html><head><title>Room Booking</title>");
            out.println("<style>");
            out.println("body{font-family:Arial,sans-serif;background:#fff8e1;padding:20px;}");
            out.println("form{background:#ffe082;padding:20px;border-radius:8px;max-width:400px;margin:auto;");
            out.println("box-shadow:0 0 15px rgba(255,152,0,0.3);}");
            out.println("input,textarea,select{width:100%;padding:8px;margin:6px 0;border:1px solid #ffb300;");
            out.println("border-radius:4px;background:#fffde7;}");
            out.println("input[type=submit]{background:#ff9800;color:#fff;border:none;cursor:pointer;font-weight:bold;}");
            out.println("input[type=submit]:hover{background:#f57c00;}");
            out.println("h2{text-align:center;color:#e65100;}");
            out.println("</style></head><body>");

            out.println("<h2>Enter Booking Details</h2>");
            out.println("<form action='book' method='post'>");

            out.println("Name: <input type='text' name='name' required>");
            out.println("Email: <input type='email' name='email' required>");
            out.println("Address: <textarea name='address' required></textarea>");
            out.println("Contact: <input type='tel' name='contact' required>");

            out.println("Room Type: <select name='roomType' required>");
            out.println("<option value='Standard'>Standard</option>");
            out.println("<option value='Deluxe'>Deluxe</option>");
            out.println("<option value='Suite'>Suite</option>");
            out.println("</select>");

            out.println("Room Quantity: <input type='number' name='roomQuantity' min='1' required>");

            out.println("Check-in Date: <input type='date' name='checkinDate' required>");
            out.println("Check-out Date: <input type='date' name='checkoutDate' required>");

            out.println("<input type='submit' value='Book'>");
            out.println("</form></body></html>");
        }

        protected void doPost(HttpServletRequest req, HttpServletResponse res)
                throws ServletException, IOException {

            res.setContentType("text/html");
            PrintWriter out = res.getWriter();

            String name = req.getParameter("name");
            String email = req.getParameter("email");
            String address = req.getParameter("address");
            String contact = req.getParameter("contact");
            String roomType = req.getParameter("roomType");
            int roomQuantity = Integer.parseInt(req.getParameter("roomQuantity"));
            String checkinDate = req.getParameter("checkinDate");
            String checkoutDate = req.getParameter("checkoutDate");

            // Step 1: Set room price per type
            double pricePerRoom = 0;
            switch (roomType) {
                case "Standard":
                    pricePerRoom = 2000; // ₹2000 per night
                    break;
                case "Deluxe":
                    pricePerRoom = 3500;
                    break;
                case "Suite":
                    pricePerRoom = 5000;
                    break;
            }

            // Step 2: Calculate total days
            long totalDays = 1; // Default 1 day
            try {
                java.time.LocalDate checkIn = java.time.LocalDate.parse(checkinDate);
                java.time.LocalDate checkOut = java.time.LocalDate.parse(checkoutDate);
                totalDays = java.time.temporal.ChronoUnit.DAYS.between(checkIn, checkOut);
                if (totalDays <= 0) totalDays = 1; // Prevent negative or zero days
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Step 3: Calculate total amount
            double totalAmount = pricePerRoom * roomQuantity * totalDays;

            try {
                String driver = getServletConfig().getInitParameter("driver");
                String url = getServletConfig().getInitParameter("url");
                String user = getServletConfig().getInitParameter("user");
                String password = getServletConfig().getInitParameter("password");
                Class.forName(driver);

                Connection conn = DriverManager.getConnection(url, user, password);
                String sql = "INSERT INTO bookings (name, email, address, contact, room_type, room_quantity, checkin_date, checkout_datea) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setString(1, name);
                ps.setString(2, email);
                ps.setString(3, address);
                ps.setString(4, contact);
                ps.setString(5, roomType);
                ps.setInt(6, roomQuantity);
                ps.setString(7, checkinDate);
                ps.setString(8, checkoutDate);


                int rows = ps.executeUpdate();
                if (rows > 0) {
                    out.println("<html><head><title>Booking Confirmation</title>");
                    out.println("<style>");
                    out.println("body { font-family: Arial, sans-serif; background-color: #fff8e1; text-align: center; padding: 30px; }");
                    out.println(".success-box { background: #d4edda; color: #155724; padding: 20px; border-radius: 8px; max-width: 500px; margin: auto; box-shadow: 0 0 15px rgba(0,0,0,0.1); }");
                    out.println(".success-box h3 { margin-top: 0; }");
                    out.println(".success-box p { font-size: 16px; margin: 5px 0; }");
                    out.println(".amount { font-weight: bold; font-size: 18px; color: #e65100; }");
                    out.println("</style></head><body>");

                    out.println("<div class='success-box'>");
                    out.println("<h3>Booking Successful!</h3>");
                    out.println("<p><b>Name:</b> " + name + "</p>");
                    out.println("<p><b>Email:</b> " + email + "</p>");
                    out.println("<p><b>Room Type:</b> " + roomType + "</p>");
                    out.println("<p><b>Room Quantity:</b> " + roomQuantity + "</p>");
                    out.println("<p><b>Total Days:</b> " + totalDays + "</p>");
                    out.println("<p class='amount'><b>Total Amount:</b> ₹" + totalAmount + "</p>");
                    out.println("</div>");

                    out.println("</body></html>");
                } else {
                    out.println("<html><head><title>Booking Failed</title>");
                    out.println("<style>");
                    out.println("body { font-family: Arial, sans-serif; background-color: #fff0f0; text-align: center; padding: 30px; }");
                    out.println(".error-box { background: #f8d7da; color: #721c24; padding: 20px; border-radius: 8px; max-width: 500px; margin: auto; box-shadow: 0 0 15px rgba(0,0,0,0.1); }");
                    out.println(".error-box h3 { margin-top: 0; }");
                    out.println("</style></head><body>");

                    out.println("<div class='error-box'>");
                    out.println("<h3>Booking Failed</h3>");
                    out.println("<p>Please try again.</p>");
                    out.println("</div>");

                    out.println("</body></html>");
                }}
                catch (Exception e) {
                    e.printStackTrace();

                    out.println("<html><head><title>Error</title>");
                    out.println("<style>");
                    out.println("body { font-family: Arial, sans-serif; background-color: #fff0f0; text-align: center; padding: 30px; }");
                    out.println(".error-box { background: #f8d7da; color: #721c24; padding: 20px; border-radius: 8px; max-width: 500px; margin: auto; box-shadow: 0 0 15px rgba(0,0,0,0.1); }");
                    out.println(".error-box h3 { margin-top: 0; }");
                    out.println(".back-btn { display: inline-block; margin-top: 15px; padding: 10px 20px; background: #721c24; color: white; text-decoration: none; border-radius: 5px; }");
                    out.println(".back-btn:hover { background: #501217; }");
                    out.println("</style></head><body>");

                    out.println("<div class='error-box'>");
                    out.println("<h3>Error: " + e.getMessage() + "</h3>");
                    out.println("<a href='book' class='back-btn'>Go Back</a>");
                    out.println("</div>");

                    out.println("</body></html>");
                }

            }
        }


        


   


    

