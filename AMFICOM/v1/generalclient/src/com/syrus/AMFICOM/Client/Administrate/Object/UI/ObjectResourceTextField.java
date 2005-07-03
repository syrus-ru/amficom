package com.syrus.AMFICOM.Client.Administrate.Object.UI;

import javax.swing.*;

import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.general.StorableObject;


public class ObjectResourceTextField extends JTextField
{
  private String typ;
  private String id;

  public ObjectResourceTextField()
  {
  }

  public void setTextNameByID(String typ, String id)
  {
    this.getClass().getName() = typ;
    this.getId() = id;
    if(typ == null || id == null)
      return;

    StorableObject or = (StorableObject)Pool.get(typ, id);
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
        if(this.getId() != null)
          return this.getId();
     return id;
  }

  public String getTyp()
  {
    String typ = "";
    if(this.getClass().getName() != null)
      return this.getClass().getName();
    return typ;
  }
}
