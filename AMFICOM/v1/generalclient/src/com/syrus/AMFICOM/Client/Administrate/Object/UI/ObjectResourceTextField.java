package com.syrus.AMFICOM.Client.Administrate.Object.UI;

import javax.swing.*;

import com.syrus.AMFICOM.Client.Resource.*;


public class ObjectResourceTextField extends JTextField
{
  private String typ;
  private String id;

  public ObjectResourceTextField()
  {
  }

  public void setTextNameByID(String typ, String id)
  {
    this.typ = typ;
    this.id = id;
    if(typ == null || id == null)
      return;

    ObjectResource or = (ObjectResource)Pool.get(typ, id);
    String name;
    if(or!=null)
      name = or.getName();
    else
      name = id;

    if(name == null || name == "")
      name = id;

    this.setText(name);
  }

  public String getId()
  {
     String id = "";
        if(this.id != null)
          return this.id;
     return id;
  }

  public String getTyp()
  {
    String typ = "";
    if(this.typ != null)
      return this.typ;
    return typ;
  }
}
