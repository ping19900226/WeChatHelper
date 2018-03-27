package com.yh.data;

import java.sql.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class DatabaseUtil {

   public static DatabaseUtil get() {
      if(util != null) {
         return util;
      }

      lock.lock();

      try {
         util = new DatabaseUtil();
         return util;
      }
      finally {
         lock.unlock();
      }

   }

   public void closeConn() {
      try {
         conn.close();
      }
      catch(SQLException e) {
         e.printStackTrace();
      }
   }

   public Statement getStatement() {
      try {
         Statement stat = conn.createStatement();
         return stat;
      }
      catch(SQLException e) {
         System.out.println("get statement failed:" + e.getMessage());
      }

      return null;
   }

   public Statement getStatement(String sql) {
      try {
         Statement stat = conn.prepareStatement(sql);
         return stat;
      }
      catch(SQLException e) {
         System.out.println("get statement failed:" + e.getMessage());
      }

      return null;
   }

   public int update(String sql, Object[] params) {
      PreparedStatement stat = null;
      try {
         stat = conn.prepareStatement(sql);

         for(int i = 1; i < params.length; i++) {
            stat.setObject(i, params[i - 1]);
         }

         return stat.executeUpdate();
      }
      catch(SQLException e) {
         System.out.println("get statement failed:" + e.getMessage());
         return 0;
      }
      finally {
         try {
            stat.close();
         }
         catch(SQLException e) {
            // do nothing
         }
      }
   }

   public boolean execute(String sql) {
      Statement stat = null;
      try {
         stat = conn.createStatement();
         return stat.execute(sql);
      }
      catch(SQLException e) {
         System.out.println("get statement failed:" + e.getMessage());
         return false;
      }
      finally {
         if(stat != null) {
            try {
               stat.close();
            }
            catch(SQLException e) {
               // do nothing.
            }
         }
      }
   }

   private DatabaseUtil() {
      try {
         Class.forName(DRIVER);
         conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
         System.out.println("database connected succeed.");
      }
      catch(ClassNotFoundException e) {
         System.out.println("database connect failed: " + e.getMessage());
      }
      catch(SQLException e) {
         System.out.println("database connected failed: " + e.getMessage());
      }
   }

   private static Lock lock = new ReentrantLock();
   private static DatabaseUtil util;
   private Connection conn;
   public static final String DRIVER = "oracle.jdbc.OracleDriver";
   public static final String URL = "jdbc:oracle:thin:@192.168.1.90:1521:orcl";
   public static final String USERNAME = "yhuser";
   public static final String PASSWORD = "yhuser";
}
