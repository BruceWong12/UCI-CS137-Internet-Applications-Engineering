package com.best_duck;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;
import java.util.UUID;

import static java.lang.String.valueOf;

@WebServlet(name = "OrderList", urlPatterns = "/orderlist",description = "orderlist")
public class OrderList extends HttpServlet {
    PrintWriter output;
    HttpServletResponse response;

    public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
        System.out.println("Inside include servlet");
        renderPage(req,res);
    }
    public void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
        System.out.println("Inside include servlet");
        renderPage(req,res);
    }

    private void renderPage(HttpServletRequest req, HttpServletResponse res) throws IOException {
        response = res;
        response.setContentType("text/html;charset=UTF-8");

        String category= req.getParameter("category");
        System.out.println("Inside include servlet");
        //p("");
        //p("<!doctype html>");
        //p("<html lang=\"en\">");
        //Head
        p("<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "    <link href=\"./css/style.css\" rel=\"stylesheet\">\n" +
                "    <link href=\"./css/products.css\" rel=\"stylesheet\">\n" +
                "    <link href=\"./css/product_category.css\" rel =\"stylesheet\">\n" +
                "\n" +
                "    <header>\n" +
                "        <div class=\"topNav\">\n" +
                "            <!-- Left-aligned links -->\n" +
                "            <a href=\"./index.html\">Home</a>\n" +
                "            <!-- https://www.w3schools.com/howto/howto_css_subnav.asp -->\n" +
                "            <a class=\"active\" href = \"../../products.html\">Products</a>\n" +
                "            <a href=\"./team.html\">Team</a>\n" +
                "            <a href=\"./about.html\">Contact</a>\n" +
                "\n" +
                "            <!-- Right-aligned links-->\n" +
                "            <div class=\"topNav-right\">\n" +
                "                <a><i class=\"fas fa-shopping-cart\"></i>Shopping Cart</a>\n" +
                "            </div>\n" +
                "        </div>\n" +
                "    </header>\n" +
                "</head>");
        //body
        p("<body>");
        //start of table
        p("<div class=\"main-container\">\n" +
                "    <div class=\"main\">\n" +
                "\n" +
                "        <!-- Product Table -->\n" +
                "        <div class=\"product-table\">\n" +
                "            <table>\n" +
                "                <caption><h1>Orders</h1></caption>\n" +
                "                <thead>\n" +
                "                <tr>\n" +
                "                    <!--<th>Table Header</th>-->\n" +
                "                </tr>\n" +
                "                </thead>\n" +
                "                <tfoot>\n" +
                "                <tr>\n" +
                "                    <!--<td colspan=\"3\">Copyright &copy; 2018 Example Organization</td>-->\n" +
                "                </tr>\n" +
                "                </tfoot>\n" +
                "                <tbody>\n");

        String userid="0";

        ArrayList<Map<String, Object>> orderList = com.best_duck.Database.getAllOrdersByUserid(userid);

        int i=0;

        for(Map<String, Object> order : orderList) {
            if( (i%3)==0 ) {
                p("<tr>");
            }

            int sku=(int)order.get("sku");
            Map<String, Object> product=Database.getProduct(valueOf(sku));
            String name= (String) product.get("name");
            String imagelink = (String) product.get("image");


            p("<td>\n"
                    // dynamically generate the cards
                    + "<div class=\"productcategory-card\">\n"
                    + "<a href=\"details?sku="+order.get("sku")+"\">\n"
                    + "<img src=\""+ imagelink+
                     "\" alt=\"" +
                    name +
                    "\" style=\"width:100%\">\n"
                    + "</a>\n"
                    + "<div class=\"productcategory-card-container\">\n"
                    + "<h4><b>"
                    + name
                    + "</b></h4>\n"
                    + "<p>First name:" +
                    order.get("first_name") +
                    "</p>\n"
                    + "<p>"
                    + "<p>OrderID:" +
                    order.get("order_num") +
                    "</p>\n"
                    + "<p>"
                    + "<p>Address:" +
                    order.get("address") +
                    "</p>\n"
                    + "<p>"
                    + "<p>City:" +
                    order.get("city") +
                    "</p>\n"
                    + "<p>State:" +
                    order.get("state") +
                    "</p>\n"
                    + "<p>Quantity: " +
                    ////"Currently In Stock: 10" +
                    order.get("quantity") +
                    "</p>\n"
                    + "</div>\n"
                    + "</div>\n"
                    + "</td>\n");
            if( (i%3)==2 ) {
                p("</tr>");
            }
            i++;
            if(i==6) {break;}
        }





        //end of table
        p("                </tr>\n" +
                "                </tbody>\n" +
                "            </table>\n" +
                "        </div> <!-- end of \"product-table\" div tag -->\n" +
                "    </div> <!-- end of \"main\" div tag -->\n" +
                "</div>");
        output = null;
        response = null;
    }

    /**
     * Helper Method
     * @param message
     * @throws IOException
     */
    private void p(String message) throws IOException{
        if(output!=null) {
            output.println(message);
        } else {
            output = response.getWriter();
        }
    }

}
