package com.syrus.AMFICOM.Client.Resource.Object;

import com.syrus.AMFICOM.Client.Resource.ObjectResource;
import com.syrus.AMFICOM.Client.Resource.StubResource;

import java.util.List;
import java.util.Collection;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public abstract class AdminObjectResource extends StubResource
{
/*
  public AdminObjectResource(String typ)
  {
    super(typ);
  }

  public AdminObjectResource()
  {
    super();
  }

  public AdminObjectResource(String typ, boolean changed)
  {
    super(typ, changed);
  }
*/

	public abstract Collection getChildTypes();
	public abstract Collection getChildren(String key);
	public abstract List getChildIds(String key);
	public abstract void addChildId(String key, String id);
	public abstract void removeChildId(String key, String id);
	public abstract String getOwnerId();
	public abstract void setOwnerId(String ownerID);
	public abstract void setModificationTime(long time);

}
