package com.syrus.AMFICOM.Client.Resource.Object;

import java.util.*;


import com.syrus.AMFICOM.CORBA.Admin.*;
import com.syrus.AMFICOM.Client.Administrate.Object.UI.*;
import com.syrus.AMFICOM.Client.General.UI.*;
import com.syrus.AMFICOM.Client.Resource.*;

public class User extends AdminObjectResource
{
  public User_Transferable transferable = new User_Transferable();

  public String id="";
  public String login="";
  public long logged;
  public String object_id="";
  public int sessions;
  public long last_login;
  public String type="";

  public Vector category_ids = new Vector();
  public Vector group_ids = new Vector();

  public Hashtable groups = new Hashtable();
  public Hashtable categories = new Hashtable();

  static final public String typ = "user";

  public User()
  {
//    super("user");
  }

  public User(User_Transferable transferable)
  {
//    super("user");
    this.transferable = transferable;
    setLocalFromTransferable();
  }

  public void setLocalFromTransferable()
  {
    int l;
    int i;
    id = transferable.id;
    login = transferable.login;
    logged = transferable.logged;
    object_id = transferable.object_id;
    sessions = transferable.sessions;
    last_login = transferable.last_login;
    type = transferable.type;

    l = transferable.category_ids.length;
    this.category_ids = new Vector();
    for(i=0; i<l; i++)
    {
      this.category_ids.add(transferable.category_ids[i]);
    }

    l = transferable.group_ids.length;
    this.group_ids = new Vector();
    for(i=0; i<l; i++)
    {
      this.group_ids.add(transferable.group_ids[i]);
    }
  }


  public void setTransferableFromLocal()
  {
    int l;
    int i;

    transferable.id = id;
    transferable.last_login = last_login;
    transferable.logged = logged;
    transferable.login = login;
    transferable.object_id = object_id;
    transferable.sessions = sessions;
    transferable.type = type;

    l = this.group_ids.size();
    transferable.group_ids = new String[l];
    for(i=0; i<l; i++)
    {
      transferable.group_ids[i] = (String)this.group_ids.get(i);
    }

    l = this.category_ids.size();
    transferable.category_ids = new String[l];
    for(i=0; i<l; i++)
    {
      transferable.category_ids[i] = (String)this.category_ids.get(i);
    }
  }

  public String getTyp()
  {
    return typ;
  }

  public String getName()
  {
    return login;
  }

  public String getId()
  {
    return id;
  }

  public void updateLocalFromTransferable()
  {
    int l;
    int i;

    l = category_ids.size();
    categories = new Hashtable();
    for(i = 0; i < l; i++)
    {
      Object o = Pool.get(OperatorCategory.typ, (String)category_ids.get(i));
      if(o!=null)
      categories.put(category_ids.get(i), o);
    }

    l = group_ids.size();
    groups = new Hashtable();
    for(i = 0; i < l; i++)
    {
      Object o = Pool.get(OperatorGroup.typ, (String)group_ids.get(i));
      if(o!=null)
       groups.put(group_ids.get(i), o);
    }

  }

  public static PropertiesPanel getPropertyPane()
  {
    return new UserPane();
  }

  public ObjectResourceModel getModel()
  {
    return new UserModel(this);
  }


  public static ObjectResourceDisplayModel getDefaultDisplayModel()
  {
    return new UserDisplayModel();
  }

  public Object getTransferable()
  {
    return transferable;
  }

  public String getDomainId()
  {
    return null;
  }

  public String getOwnerId()
  {
    return "";
  }

  public void setOwnerId(String ownerID)
  {
  }

  public Vector getChildIds(String key)
  {
    if(key.equals(OperatorGroup.typ))
      return group_ids;
    if(key.equals(OperatorCategory.typ))
      return category_ids;
    return new Vector();
  }

  public void addChildId(String key, String id)
  {
    if(key.equals(OperatorGroup.typ))
      group_ids.add(id);
    if(key.equals(OperatorCategory.typ))
      category_ids.add(id);
  }
  public void removeChildId(String key, String id)
  {
    if(key.equals(OperatorGroup.typ))
      group_ids.remove(id);
    if(key.equals(OperatorCategory.typ))
      category_ids.remove(id);
  }


  public Enumeration getChildren(String key)
  {
    if(key.equals(OperatorGroup.typ))
    {
      return groups.elements();
    }
    if(key.equals(OperatorCategory.typ))
    {
      return categories.elements();
    }
    return new Vector().elements();
  }

  public Enumeration getChildTypes()
  {
    Vector ret = new Vector();
    ret.add(OperatorCategory.typ);
    ret.add(OperatorGroup.typ);
    return ret.elements();
  }

  public static Vector getChildTypes_()
  {
    Vector ret = new Vector();
    ret.add(OperatorCategory.typ);
    ret.add(OperatorGroup.typ);
    return ret;
  }

  public  void setModificationTime(long time)
  {
  }



}
