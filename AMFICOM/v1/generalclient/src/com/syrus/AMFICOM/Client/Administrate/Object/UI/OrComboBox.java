package com.syrus.AMFICOM.Client.Administrate.Object.UI;

import java.util.*;


import com.syrus.AMFICOM.Client.General.UI.*;
import com.syrus.AMFICOM.Client.Resource.*;

public class OrComboBox extends ObjectResourceComboBox
{
  public OrComboBox()
  {
    super();
  }

/*
  public void setSelectedProfileByUser(String user_id)
  {
    if(user_id == null) return;
    User user = (User)Pool.get(User.typ, user_id);
    if(user == null) return;
    ObjectResource or = (ObjectResource)Pool.get(user.type, user.object_id);
    if(or == null) return;
    this.setSelected(or);
    return;
  }

  public void setSelectedProfileByUser(User user)
  {
    if(user == null) return;
    ObjectResource or = (ObjectResource)Pool.get(user.type, user.object_id);
    if(or == null) return;
    this.setSelected(or);
    return;
  }

  public String getSelectedUserIDfromProfile()
  {
    String s;
    String objID = (String)this.getSelected();
     if(objID != null && objID != "" )
       s = ((OperatorProfile)(Pool.get(OperatorProfile.typ, objID))).user_id;
     else
       s = null;
     return s;
  }
*/
  public void setTyp(String typ)
  {
    if(typ == null)
      return;
    Hashtable h = Pool.getHash(typ);
    if(h==null)
      return;
    this.setContents(h, true);
  }

  public void setSelectedTyp(String typ, String id)
  {
    if(id == null)
      id = "";
    Object o = Pool.get(typ, id);

//    if(o!=null)
      this.setSelected(o);
  }

/*
  public String getSelectedId()
  {
    String s = (String)this.getSelected();
    if(s!=null) return s;
    return "";
  }
*/




/*
  public void addTyp(String typ)
  {
    Hashtable h = Pool.getHash(typ);
    if(h==null) return;
  }
*/
}
