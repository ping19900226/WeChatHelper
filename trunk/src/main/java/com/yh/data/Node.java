package com.yh.data;

/**
 * Node
 */
public class Node {
   private NodeType type;
   private String name;
   private Pos pos;
   private String attrName;

   public NodeType getType() {
      return type;
   }

   public void setType(NodeType type) {
      this.type = type;
   }

   public String getName() {
      return name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public Pos getPos() {
      return pos;
   }

   public void setPos(Pos pos) {
      this.pos = pos;
   }

   public String getAttrName() {
      return attrName;
   }

   public void setAttrName(String attrName) {
      this.attrName = attrName;
   }
}
