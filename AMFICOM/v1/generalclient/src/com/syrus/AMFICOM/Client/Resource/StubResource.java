/*
 * $Id: StubResource.java,v 1.7 2004/09/27 16:04:21 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ
 */

package com.syrus.AMFICOM.Client.Resource;

import com.syrus.AMFICOM.Client.General.Filter.ObjectResourceFilter;
import com.syrus.AMFICOM.Client.General.UI.*;
import com.syrus.AMFICOM.Client.Resource.Object.ObjectPermissionAttributes;

/**
 * @author $Author: bass $
 * @version $Revision: 1.7 $, $Date: 2004/09/27 16:04:21 $
 * @module generalclient_v1
 */
public class StubResource implements ObjectResource
{
	protected boolean changed = false;

	public boolean isChanged()
	{
		return this.changed;
	}

	public void setChanged(boolean changed)
	{
		this.changed = changed;
	}

	public static ObjectResourceDisplayModel getDefaultDisplayModel()
	{
		return new StubDisplayModel(new String[] {"name"}, new String[] {"Название"});
	}

	public static ObjectResourceDisplayModel getReportDisplayModel()
	{
		return new StubDisplayModel(new String[] {"name"}, new String[] {"Название"});
	}

	public static ObjectResourceFilter getFilter()
	{
		return null;
	}

	public static ObjectResourceSorter getSorter()
	{
		return getDefaultSorter();
	}

	public static ObjectResourceSorter getDefaultSorter()
	{
		return new ObjectResourceNameSorter();
	}

	public ObjectResourceModel getModel()
	{
		return new ObjectResourceModel();
	}

	public long getModified()
	{
		return 0;
	}

	public ObjectPermissionAttributes getPermissionAttributes()
	{
		return null;
	}

	public StubResource()
	{
	}
	public String getTyp()
	{
		throw new UnsupportedOperationException();
	}

	public Object getTransferable()
	{
		return null;
	}

	public String getName()
	{
		return "";
	}

	public String getId()
	{
		return "";
	}

	public String getDomainId()
	{
		return "sysdomain";
	}

	public void setLocalFromTransferable()
	{
	}

	public void setTransferableFromLocal()
	{
	}

	public void updateLocalFromTransferable()
	{
	}

	public String getColumnName(String col_id)
	{
		return "";
	}

	public String getColumnValue(String col_id)
	{
		return "";
	}

	public String getPropertyValue(String col_id)
	{
		return "";
	}

	public String getPropertyName(String col_id)
	{
		return "";
	}

}
