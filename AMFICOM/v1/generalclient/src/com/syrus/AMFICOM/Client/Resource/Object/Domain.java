package com.syrus.AMFICOM.Client.Resource.Object;

import com.syrus.AMFICOM.CORBA.Admin.Domain_Transferable;
import com.syrus.AMFICOM.CORBA.Admin.ObjectPermissionAttributes_Transferable;
import com.syrus.AMFICOM.Client.Administrate.Object.UI.DomainPaneConfig;
import com.syrus.AMFICOM.Client.General.UI.ObjectResourceDisplayModel;
import com.syrus.AMFICOM.Client.General.UI.PropertiesPanel;
import com.syrus.AMFICOM.Client.Resource.ObjectResource;
import com.syrus.AMFICOM.Client.Resource.ObjectResourceModel;
import com.syrus.AMFICOM.Client.Resource.Pool;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;

public class Domain extends AdminObjectResource
{
  public Domain_Transferable transferable = new Domain_Transferable();

  public ObjectPermissionAttributes opa = new ObjectPermissionAttributes();

  public String id="";
  public String name="";
  public String codename="";
  public String owner_id="";
  public String description="";
  public long created;
  public String created_by="";
  public long modified;
  public String modified_by="";


  public String domain_id="";

  public List domain_ids = new ArrayList();

  public HashMap domains = new HashMap();

  static final public String typ = "domain";

  public Domain()
  {
//    super("domain");
  }

  public Domain(Domain_Transferable transferable)
  {
//    super("domain");
    this.transferable = transferable;
    setLocalFromTransferable();
  }

  public Domain(
      String id,
      String name,
      String codename,
      String owner_id,
      String description,
      long created,
      String created_by,
      long modified,
      String modified_by,
      String domain_id,
      List domain_ids)
  {
//    super("domain");
    this.id = id;
    this.name = name;
    this.codename = codename;
    this.owner_id = owner_id;
    this.description = description;
    this.created = created;
    this.created_by = created_by;
    this.modified = modified;
    this.modified_by = modified_by;
    this.domain_id = domain_id;
    this.domain_ids = domain_ids;

    transferable = new Domain_Transferable();
  }

  public void setLocalFromTransferable()
  {
    int l;
    int i;
    int count;

    id = transferable.id;
    name = transferable.name;
    codename = transferable.codename;
    owner_id = transferable.owner_id;
    description = transferable.description;
    created = transferable.created;
    created_by = transferable.created_by;
    modified = transferable.modified;
    modified_by = transferable.modified_by;
    domain_id = transferable.domain_id;

    count = transferable.domain_ids.length;
    domain_ids = new ArrayList();
    for(i = 0; i < count; i++)
      domain_ids.add(transferable.domain_ids[i]);

    opa = new ObjectPermissionAttributes(this.transferable.permissions);
  }

  public void setTransferableFromLocal()
  {
    int l;
    int i;
    int count;

    transferable.id = id;
    transferable.name = name;
    transferable.codename = codename;
    transferable.owner_id = owner_id;
    transferable.description = description;
    transferable.created = created;
    transferable.created_by = created_by;
    transferable.modified = modified;
    transferable.modified_by = modified_by;
    transferable.domain_id = domain_id;

    count = domain_ids.size();
    transferable.domain_ids = new String[count];
    for(i=0; i<count; i++)
    {
      transferable.domain_ids[i] = (String)domain_ids.get(i);
    }

    opa.setTransferableFromLocal();
    transferable.permissions = (ObjectPermissionAttributes_Transferable )opa.getTransferable();
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
    domains = new HashMap();
    
    for(ListIterator lIt = domain_ids.listIterator(); lIt.hasNext();)
    {
      String str = (String)lIt.next();
      Object o = Pool.get(Domain.typ, str);
      if(o != null)
        domains.put(str, o);
    }

    opa.updateLocalFromTransferable();
  }

//AdminObjectResource
  public List getChildIds(String key)
  {
    if(key.equals(Domain.typ))
      return domain_ids;
    return new ArrayList();
  }

  public void addChildId(String key, String id)
  {
    if(key.equals(Domain.typ))
      domain_ids.add(id);
  }
  public void removeChildId(String key, String id)
  {
    if(key.equals(Domain.typ))
      domain_ids.remove(id);
  }

  public Collection getChildren(String key)
  {
    if(key.equals(Domain.typ))
    {
      return domains.values();
    }
    return new ArrayList();
  }

  public Collection getChildTypes()
  {
    List ret = new ArrayList();
    ret.add(Domain.typ);
    return ret;
  }

  public static Collection getChildTypes_()
  {
    List ret = new ArrayList();
    ret.add(Domain.typ);
    return ret;
  }

  public Class getChildClass(String type)
  {
    if(type.equals(Domain.typ))
      return Domain.class;
    return ObjectResource.class;
  }


  public static PropertiesPanel getPropertyPane()
  {
    return new DomainPaneConfig();
  }

  public ObjectResourceModel getModel()
  {
    return new DomainModel(this);
  }

  public static ObjectResourceDisplayModel getDefaultDisplayModel()
  {
    return new DomainDisplayModel();
  }


  public Object getTransferable()
  {
    return transferable;
  }

  public String getDomainId()
  {
    return null;
  }

  public ObjectPermissionAttributes getPermissionAttributes()
  {
    return opa;
  }

  public String getOwnerId()
  {
    return owner_id;
  }

  public void setOwnerId(String ownerID)
  {
    this.owner_id = ownerID;
  }


  public  void setModificationTime(long time)
  {
    modified = time;
  }

}
