package com.yh.db;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import com.yh.util.Resource;

public class DBManager {

   private Connection conn;
   private static DBManager manager;

   private DBManager(){
      try {
         Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
         String url = Resource.databasePath();
         conn =  DriverManager.getConnection(url);
      } catch (ClassNotFoundException e) {
         e.printStackTrace();
      } catch (SQLException e) {
         e.printStackTrace();
      }
   }

   public int update(String sql){
      Statement statement = null;
      try {
         statement = conn.createStatement();
         return statement.executeUpdate(sql);
      } catch (SQLException e) {
         e.printStackTrace();
      } finally{
         try {
            if(statement != null){
               statement.close();
            }
         } catch (SQLException e) {
            e.printStackTrace();
         }
      }
      return 0;
   }

   public int update(String sql, Object[]params){
      PreparedStatement statement = null;
      try {
         statement = conn.prepareStatement(sql);
         if(params.length > 0){
            for(int i=1; i<=params.length; i++){
               Object param = params[i - 1];
               statement.setObject(i, param);
            }
         }

         int affectRow =  statement.executeUpdate();
         return affectRow;
      } catch (SQLException e) {
         e.printStackTrace();
      } finally{
         try {
            if(statement != null){
               statement.close();
            }
         } catch (SQLException e) {
            e.printStackTrace();
         }
      }
      return 0;
   }

   public void query(String sql, RowCallbackHandler handler){
      Statement statement = null;
      ResultSet resultSet = null;
      try {
         statement = conn.createStatement();
         resultSet = statement.executeQuery(sql);
         if(resultSet == null){
            return;
         }
         while(resultSet.next()){
            handler.processRow(resultSet);
         }
      } catch (SQLException e) {
         e.printStackTrace();
      } finally{
         try {
            if(resultSet != null){
               resultSet.close();
            }
            if(statement != null){
               statement.close();
            }
         } catch (SQLException e) {
            e.printStackTrace();
         }
      }
   }


   public void query(String sql, Object [] params, RowCallbackHandler handler){
      PreparedStatement statement = null;
      ResultSet resultSet = null;
      try {
         statement = conn.prepareStatement(sql);
         if(params != null && params.length > 0){
            for(int i = 1; i <= params.length; i++){
               Object param = params[i - 1];
               statement.setObject(i, param);
            }
         }

         resultSet = statement.executeQuery();
         if(resultSet == null){
            return;
         }
         while(resultSet.next()){
            handler.processRow(resultSet);
         }


      } catch (SQLException e) {
         e.printStackTrace();
      } finally{
         try {
            if(resultSet != null){
               resultSet.close();
            }
            if(statement != null){
               statement.close();
            }
         } catch (SQLException e) {
            e.printStackTrace();
         }
      }
   }

   public List<Map<String, Object>> queryForList(String sql, Object [] params){
      PreparedStatement statement = null;
      ResultSet resultSet = null;
      try {
         statement = conn.prepareStatement(sql);
         if(params.length > 0){
            for(int i=1; i<=params.length; i++){
               Object param = params[i - 1];
               statement.setObject(i, param);
            }
         }
         List<Map<String, Object>> results = new ArrayList<Map<String, Object>>();
         resultSet = statement.executeQuery();

         if(resultSet != null){
            ResultSetMetaData md = resultSet.getMetaData();
            int columnCount = md.getColumnCount();

            while (resultSet.next()) {
               Map<String, Object> rowData = new HashMap<String, Object>(columnCount);

               for (int i = 1; i <= columnCount; i++) {
                  rowData.put(md.getColumnName(i), resultSet.getObject(i));
               }

               results.add(rowData);
            }
         }
         return results;

      } catch (SQLException e) {
         e.printStackTrace();
      } finally{
         try {
            if(resultSet != null){
               resultSet.close();
            }
            if(statement != null){
               statement.close();
            }
         } catch (SQLException e) {
            e.printStackTrace();
         }
      }
      return null;
   }

   public List<Map<String, Object>> queryForList(String sql){
      Statement statement = null;
      ResultSet resultSet = null;
      try {
         statement = conn.createStatement();
         resultSet = statement.executeQuery(sql);

         List<Map<String, Object>> results = new ArrayList<Map<String, Object>>();
         if(resultSet != null){
            ResultSetMetaData md = resultSet.getMetaData();
            int columnCount = md.getColumnCount();
            while (resultSet.next()) {
               Map<String, Object> rowData = new HashMap<String, Object>(columnCount);
               for (int i = 1; i <= columnCount; i++) {
                  rowData.put(md.getColumnName(i), resultSet.getObject(i));
               }
               results.add(rowData);
            }
         }
         return results;

      } catch (SQLException e) {
         e.printStackTrace();
      } finally{
         try {
            if(resultSet != null){
               resultSet.close();
            }
            if(statement != null){
               statement.close();
            }
         } catch (SQLException e) {
            e.printStackTrace();
         }
      }
      return null;
   }


   public void execute(String sql){
      System.out.println(sql);
      Statement statement = null;
      try {
         statement = conn.createStatement();
         statement.execute(sql);
      } catch (SQLException e) {
         e.printStackTrace();
      } finally {
         if(statement != null){
            try {
               statement.close();
            } catch (SQLException e) {
               e.printStackTrace();
            }
         }
      }
   }

   public Set<String> getAllTable(){
      try {
         DatabaseMetaData meta = conn.getMetaData();
         ResultSet res = meta.getTables(null, null, null, new String[]{"TABLE"});
         Set<String> set=new HashSet<String>();
         while (res.next()) {
            set.add(res.getString("TABLE_NAME").toLowerCase());
         }
         return set;
      } catch (SQLException e) {
         return null;
      }
   }

   public boolean isTableExist(String tableName){
      return getAllTable().contains(tableName.toLowerCase());
   }

   public static DBManager getInstance(){
      if(manager == null){
         manager = new DBManager();
      }
      return manager;
   }

   public Connection getConnection(){
      return conn;
   }
}