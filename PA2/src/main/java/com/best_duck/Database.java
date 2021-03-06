package com.best_duck;

import java.io.PrintWriter;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class Database {

    // JDBC driver name and database URL
    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://127.0.0.1:3306/best_duck";//?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
    //  Database credentials
    static final String USER = "root";
    static final String PASS = "LeoYoung981028";//"testdb124";

    /**
     * openConnection
     * @return
     */
    private static Connection openConnection() {
        Connection conn = null;
        try {
            Class.forName(JDBC_DRIVER); //Register JDBC driver

            System.out.println("Connecting to database..."); //Open a connection
            conn = DriverManager.getConnection(DB_URL,USER,PASS);

        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return conn;
    }


    /**
     * getProductsTest
     * @param out
     */
    public static void getProductsTest(PrintWriter out) {

        Connection conn = openConnection();

        Statement stmt = null;
        System.out.println("Creating statement...");
        try {
            stmt = conn.createStatement();
            String sql;
            sql = "SELECT * FROM best_duck.product;";
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next()){
                //Retrieve by column name
                String productName = rs.getString("ProductName");
                String productID = rs.getString("ProductID");

                System.out.print("product name: " + productName);
                System.out.println(", product id: " + productID);

                //Display in browser
                out.print("product name: " + productName);
                out.println(", product id: " + productID);
            }

            rs.close();
            stmt.close();
            conn.close();

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


    }

    /**
     * getAllProductsByCategory
     * @param category
     * @return
     */
    public static ArrayList<Map<String, Object>> getAllProductsByCategory(String category) {
        ArrayList<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        /*
        `product`  (
          `sku` int NULL DEFAULT NULL,
          `producer` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
          `catagory` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
          `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
          `image` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
          `price` decimal(10, 2) NULL DEFAULT NULL,
          `stock` int NULL DEFAULT NULL,
          `description` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL
        ) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

        */
        Connection conn = openConnection();

        //Statement stmt = null;
        System.out.println("Creating statement...");
        try {

            String sql;
            sql = "SELECT * FROM best_duck.product WHERE category=?";

            //prepare statement
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, category); // fill the category

            //execute statement
            ResultSet rs = ps.executeQuery();

            //used to printing the results of ResultSet
            //and store ResultSet into an arraylist of hashmaps
            ResultSetMetaData rsmd = rs.getMetaData();
            System.out.println("querying " + sql + "(?= " + category + ")");
            int columnsNumber = rsmd.getColumnCount();

            while(rs.next()){
                //Retrieve by column name

                Map<String, Object> row = new HashMap<String, Object>();

                row.put("sku",rs.getInt("sku"));
                row.put("producer",rs.getString("producer"));
                row.put("category",rs.getString("category"));
                row.put("name",rs.getString("name"));
                row.put("image",rs.getString("image"));
                row.put("price",rs.getFloat("price"));
                row.put("stock",rs.getInt("stock"));
                row.put("description",rs.getString("description"));
                list.add(row);
            }

            rs.close();
            //stmt.close();
            conn.close();



        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return list;

    }

    /**
     * getProduct
     * @param sku
     * @return
     */
    public static Map<String, Object> getProduct(String sku) {
        Map<String, Object> row = new HashMap<String, Object>();
        Connection conn = openConnection();

        //Statement stmt = null;
        System.out.println("Creating statement...");
        try {

            String sql;
            sql = "SELECT * FROM best_duck.product WHERE sku=?";

            //prepare statement
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, sku);

            //execute statement
            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                row.put("sku",rs.getInt("sku"));
                row.put("producer",rs.getString("producer"));
                row.put("category",rs.getString("category"));
                row.put("name",rs.getString("name"));
                row.put("image",rs.getString("image"));
                row.put("price",rs.getFloat("price"));
                row.put("stock",rs.getInt("stock"));
                row.put("description",rs.getString("description"));
            }

            rs.close();
            //stmt.close();
            conn.close();


        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return row;
    }


    /**
     * getOrderProducts
     * @param orderID
     * @return
     */
    public static Map<String, Object> getOrderInfo(String orderID) {
        Map<String, Object> row = new HashMap<String, Object>();
        Connection conn = openConnection();

        System.out.println("Creating statement...");
        try {

            String sql;

            sql = "SELECT DISTINCT " +
                    "order_num, first_name, last_name, address, city, " +
                    "state, zip, shipping_method, credit_num, exp_mon, " +
                    "exp_year, cvv, phone_num, email, userid, datetime " +
                    "FROM best_duck.order " +
                    "WHERE order_num=?";
                    //"GROUP BY order_num";

            //prepare statement
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, orderID);

            System.out.println(ps.toString());

            //execute statement
            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                row.put("order_num", rs.getString("order_num"));
                row.put("first_name", rs.getString("first_name"));
                row.put("last_name", rs.getString("last_name"));
                row.put("address", rs.getString("address"));
                row.put("city", rs.getString("city"));
                row.put("state", rs.getString("state"));
                row.put("zip", rs.getString("zip"));
                row.put("shipping_method", rs.getString("shipping_method"));
                row.put("credit_num", rs.getString("credit_num"));
                row.put("exp_mon", rs.getInt("exp_mon"));
                row.put("exp_year", rs.getInt("exp_year"));
                row.put("cvv", rs.getInt("cvv"));
                row.put("phone_num", rs.getString("phone_num"));
                row.put("email", rs.getString("email"));
            }

            rs.close();
            //stmt.close();
            conn.close();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return row;
    }

    /**
     * getOrderProducts
     * @param orderID
     * @return
     */
    public static ArrayList<Map<String, Object>> getOrderProducts(String orderID) {
        ArrayList<Map<String, Object>> orderproducts = new ArrayList<Map<String, Object>>();
        Connection conn = openConnection();

        /*
        CREATE TABLE `order`  (
          `order_num` bigint NOT NULL,
          `sku` bigint NULL DEFAULT NULL,
          `first_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
          `last_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
          `address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
          `city` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
          `state` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
          `zip` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
          `shipping_method` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
          `quantity` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
          `credit_num` bigint NULL DEFAULT NULL,
          `exp_mon` int NULL DEFAULT NULL,
          `exp_year` int NULL DEFAULT NULL,
          `cvv` int NULL DEFAULT NULL,
          `phone_num` bigint NULL DEFAULT NULL,
          `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
                PRIMARY KEY (`order_num`) USING BTREE
        ) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;
        */

        //Statement stmt = null;
        System.out.println("Creating statement...");
        try {

            String sql;
            sql = "SELECT * FROM best_duck.order WHERE order_num=?";

            //prepare statement
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, orderID);

            //execute statement
            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                Map<String, Object> row = new HashMap<String, Object>();

                row.put("order_num", rs.getString("order_num"));
                row.put("sku", rs.getInt("sku"));
                row.put("first_name", rs.getString("first_name"));
                row.put("last_name", rs.getString("last_name"));
                row.put("address", rs.getString("address"));
                row.put("city", rs.getString("city"));
                row.put("state", rs.getString("state"));
                row.put("zip", rs.getString("zip"));
                row.put("shipping_method", rs.getString("shipping_method"));
                row.put("quantity", rs.getInt("quantity"));
                row.put("credit_num", rs.getString("credit_num"));
                row.put("exp_mon", rs.getInt("exp_mon"));
                row.put("exp_year", rs.getInt("exp_year"));
                row.put("cvv", rs.getInt("cvv"));
                row.put("phone_num", rs.getString("phone_num"));
                row.put("email", rs.getString("email"));

                orderproducts.add(row);
            }

            rs.close();
            //stmt.close();
            conn.close();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return orderproducts;
    }




    public static void setOrder(
            String order_id,
            int sku,
            String first_name,
            String last_name,
            String address,
            String city,
            String state,
            String zip,
            String shipping_method,
            int quantity,
            String credit_num,
            int exp_mon,
            int exp_year,
            int cvv,
            String phone_num,
            String email,
            String user_id,
            java.util.Date todaysDate
    ) {

        // return value
        //int recordID = 0;
        int numOfRowsUpdated = 0;

        // convert java date to mysql date
        java.sql.Date mysqlTodaysDate = new java.sql.Date( todaysDate.getTime() );

        Connection conn = openConnection();

        //Statement stmt = null;
        System.out.println("Creating set-order statement set order...");
        try {
            String sql;
            sql = "INSERT INTO best_duck.order " +
                    "(order_num, sku, first_name, last_name, address, " +
                    "city, state, zip, shipping_method, quantity, credit_num, " +
                    "exp_mon, exp_year, cvv, phone_num, email, userid, datetime)" +
                    " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            //prepare statement
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            //ps.setInt(1, order_num);
            ps.setString(1, order_id);
            ps.setInt(2, sku);
            ps.setString(3, first_name);
            ps.setString(4, last_name);
            ps.setString(5, address);
            ps.setString(6, city);
            ps.setString(7, state);
            ps.setString(8, zip);
            ps.setString(9, shipping_method);
            ps.setInt(10, quantity);
            ps.setString(11, credit_num);
            ps.setInt(12, exp_mon);
            ps.setInt(13, exp_year);
            ps.setInt(14, cvv);
            ps.setString(15, phone_num);
            ps.setString(16, email);
            ps.setString(17, user_id);
            ps.setDate(18, mysqlTodaysDate);
            System.out.println(ps.toString());

            //execute statement
            numOfRowsUpdated = ps.executeUpdate();
            System.out.println(numOfRowsUpdated + " row(s) inserted in order table. ");

            // return record id of newly inserted record
            if(numOfRowsUpdated == 0) {
                throw new SQLException("Creating order record failed, no rows affected.");
            }


        } catch (SQLException e) {
            // TODO Auto-generated catch block
            System.out.println("Fail to access the Datebase.");
            e.printStackTrace();
        }
        //return recordID;
    }


    /**
     * getAllOrdersByUserid
     * @param userid
     * @return
     */
    public static ArrayList<Map<String, Object>> getAllOrdersByUserid(String userid) {
        ArrayList<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Connection conn = openConnection();

        //Statement stmt = null;
        System.out.println("Creating statement...");
        try {

            String sql;
            sql = "SELECT * FROM best_duck.order WHERE userid=?";

            //prepare statement
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1,userid); // fill the category

            //execute statement
            ResultSet rs = ps.executeQuery();

            //used to printing the results of ResultSet
            //and store ResultSet into an arraylist of hashmaps
            ResultSetMetaData rsmd = rs.getMetaData();
            System.out.println("querying " + sql + "(?= " + userid + ")");
            System.out.println(ps.toString());
            int columnsNumber = rsmd.getColumnCount();

            while(rs.next()){
                Map<String, Object> row = new HashMap<String, Object>();
                System.out.println("Get info");
                row.put("order_num", rs.getString("order_num"));
                row.put("sku", rs.getInt("sku"));
                row.put("first_name", rs.getString("first_name"));
                row.put("last_name", rs.getString("last_name"));
                row.put("address", rs.getString("address"));
                row.put("city", rs.getString("city"));
                row.put("state", rs.getString("state"));
                row.put("zip", rs.getString("zip"));
                row.put("shipping_method", rs.getString("shipping_method"));
                row.put("quantity", rs.getInt("quantity"));
                row.put("credit_num", rs.getString("credit_num"));
                row.put("exp_mon", rs.getInt("exp_mon"));
                row.put("exp_year", rs.getInt("exp_year"));
                row.put("cvv", rs.getInt("cvv"));
                row.put("phone_num", rs.getString("phone_num"));
                row.put("email", rs.getString("email"));
                list.add(row);
            }


            System.out.println("List Created");

            rs.close();
            conn.close();



        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return list;

    }
}
