package com.yh.request.entity;

public class Bug {
   public static final String TYPE_BLOCK = "blo";
   public static final String TYPE_MAJ = "maj";
   public static final String TYPE_NORMAL = "nor";
   //critical

   public static final String STATUS_NEW = "NEW";
   public static final String STATUS_ASSIGNED = "ASSIGNED";
   public static final String STATUS_UNCONFIRMED = "UNCOMFIRMED";
   public static final String STATUS_REOPENED = "REOPENED";
   public static final String STATUS_RESOLVED = "RESOLVED";
   public static final String STATUS_VERIFIED = "VERIFIED";
   public static final String STATUS_CLOSED = "CLOSED";

   private int id;
   private String type;
   private String priority;
   private String reporter;
   private String status;
   private String summary;

   public int getId() {
      return id;
   }

   public void setId(int id) {
      this.id = id;
   }

   public String getType() {
      return type;
   }

   public void setType(String type) {
      this.type = type;
   }

   public String getPriority() {
      return priority;
   }

   public void setPriority(String priority) {
      this.priority = priority;
   }

   public String getReporter() {
      return reporter;
   }

   public void setReporter(String reporter) {
      this.reporter = reporter;
   }

   public String getStatus() {
      return status;
   }

   public void setStatus(String status) {
      this.status = status;
   }

   public String getSummary() {
      return summary;
   }

   public void setSummary(String summary) {
      this.summary = summary;
   }
}
