package com.syrus.AMFICOM.Client.Resource.Object;

import java.io.*;
import java.util.*;

import com.syrus.AMFICOM.CORBA.Admin.*;
import com.syrus.AMFICOM.Client.Administrate.Object.UI.*;
import com.syrus.AMFICOM.Client.General.UI.*;
import com.syrus.AMFICOM.Client.Resource.*;

public class OperatorCategory extends AdminObjectResource implements Serializable
{
	private static final long serialVersionUID = 01L;

  public static final String admin    = "admin";
  public static final String sysadmin = "sysadmin";
  public static final String designer = "designer";
  public static final String analyst  = "analyst";
  public static final String operator = "operator";
  public static final String spec     = "spec";
  public static final String subscriber     = "subscriber";


  public OperatorCategory_Transferable transferable =
      new OperatorCategory_Transferable();

  public String id="";
  public String name="";
  public String codename="";
  public String description="";
  public long modified = 0;

  public Vector user_ids = new Vector();
  public Hashtable users = new Hashtable();

  static final public String typ = "operatorcategory";

  public OperatorCategory()
  {
//    super("operatorcategory");
  }

  public OperatorCategory(OperatorCategory_Transferable transferable)
  {
//    super("operatorcategory");
    this.transferable = transferable;
    setLocalFromTransferable();
  }

  public OperatorCategory(
      String id,
      String name,
      String codename,
      String description,
      Vector user_ids)
  {
//    super("operatorcategory");
    this.id = id;
    this.name = name;
    this.codename = codename;
    this.description = description;
    this.user_ids = user_ids;
	this.modified = System.currentTimeMillis();

    transferable = new OperatorCategory_Transferable();
  }

  public void setLocalFromTransferable()
  {
    int l;
    int i;
    int count;

    id = transferable.id;
    name = transferable.name;
    codename = transferable.codename;
    description = transferable.description;
	modified = transferable.modified;

    count = transferable.user_ids.length;
    user_ids = new Vector(count);
    for(i = 0; i < count; i++)
      user_ids.add(transferable.user_ids[i]);
  }

  public void setTransferableFromLocal()
  {
    int l;
    int i;
    int count;

    transferable.id = id;
    transferable.name = name;
    transferable.codename = codename;
    transferable.description = description;
    transferable.modified = modified;

    count = user_ids.size();
    transferable.user_ids = new String[count];
    for(i = 0; i < count; i++)
      transferable.user_ids[i] = (String)user_ids.get(i);
  }

  public String getTyp()
  {
    return typ;
  }

  public String getName()
  {
    return name;
  }

  public String getId()
  {
    return id;
  }

  public void updateLocalFromTransferable()
  {
    int l;
    int i;

/*		int count = transferable.operator_ids.length;
  operator_ids = new Vector(count);
  for(i = 0; i < count; i++)
   operator_ids.add(transferable.operator_ids[i]);
*/
    l = user_ids.size();
    users = new Hashtable();
    for(i = 0; i < l; i++)
    {
      Object o = Pool.get(User.typ, (String)user_ids.get(i));
      if(o != null)
        users.put(user_ids.get(i), o);
    }
  }

  public Enumeration getChildren(String key)
  {
    if(key.equals(User.typ))
    {
      return users.elements();
    }
    return new Vector().elements();
  }

  public Enumeration getChildTypes()
  {
    Vector ret = new Vector();
    ret.add(User.typ);
    return ret.elements();
  }

  public Vector getChildIds(String key)
  {
    if(key.equals(User.typ))
      return user_ids;
    return new Vector();
  }

  public void addChildId(String key, String id)
  {
    if(key.equals(User.typ))
      user_ids.add(id);
  }

  public void removeChildId(String key, String id)
  {
    if(key.equals(User.typ))
      user_ids.remove(id);
  }


  public static Vector getChildTypes_()
  {
    Vector ret = new Vector();
    ret.add(User.typ);
    return ret;
  }

  public static PropertiesPanel getPropertyPane()
  {
    return new OperatorCategoryPanel();
  }

  public Class getChildClass(String type)
  {
    if(type.equals(User.typ))
      return User.class;
    return ObjectResource.class;
  }


  public ObjectResourceModel getModel()
  {
    return new OperatorCategoryModel(this);
  }

  public static ObjectResourceDisplayModel getDefaultDisplayModel()
  {
    return new OperatorCategoryDisplayModel();
  }


  static public String idByCodeName(String code)
  {
    for(Enumeration enum =
        Pool.getHash(OperatorCategory.typ).elements();
        enum.hasMoreElements();)
    {
      OperatorCategory cat = (OperatorCategory)enum.nextElement();
      if(cat.codename.equals(code))
        return cat.id;
    }
    return "";
  }

  public Object getTransferable()
  {
    return transferable;
  }

  public String getDomainId()
  {
    return null;
  }

  public long getModified()
  {
    return modified;
  }

	private void writeObject(java.io.ObjectOutputStream out) throws IOException
	{
		out.writeObject(id);
		out.writeObject(name);
		out.writeObject(codename);
		out.writeObject(description);
		out.writeLong(modified);
		out.writeObject(user_ids);
	}

	private void readObject(java.io.ObjectInputStream in)
			throws IOException, ClassNotFoundException
	{
		id = (String )in.readObject();
		name = (String )in.readObject();
		codename = (String )in.readObject();
		description = (String )in.readObject();
		modified = in.readLong();
		user_ids = (Vector )in.readObject();

		transferable = new OperatorCategory_Transferable();
		updateLocalFromTransferable();
	}

  public String getOwnerId()
  {
    return "";
  }

  public void setOwnerId(String ownerID)
  {
  }

  public  void setModificationTime(long time)
  {
    modified = time;
  }

}