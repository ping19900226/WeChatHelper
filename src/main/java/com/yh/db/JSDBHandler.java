package com.yh.db;

import com.yh.util.JSONUtil;

import java.util.List;
import java.util.Map;

public class JSDBHandler {

   public void execute(String sql){
      DBManager.getInstance().execute(sql);
   }

   public void update(String sql){
      DBManager.getInstance().update(sql);
   }

   public String query(String sql){
      sql = sql.replaceAll("\"", "'");
      List<Map<String, Object>> results = DBManager.getInstance().queryForList(sql);
      return JSONUtil.toJSONString(results);
   }

}
