/*
 * $Id: AdminObjectResource.java,v 1.7 2004/09/27 14:32:46 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ
 */

package com.syrus.AMFICOM.Client.Resource.Object;

import com.syrus.AMFICOM.Client.Resource.StubResource;
import java.util.Collection;
import java.util.List;

/**
 * @author $Author: bass $
 * @version $Revision: 1.7 $, $Date: 2004/09/27 14:32:46 $
 * @module generalclient_v1
 */
public abstract class AdminObjectResource extends StubResource
{
	public abstract Collection getChildTypes();

	public abstract Collection getChildren(String key);

	public abstract List getChildIds(String key);

	public abstract void addChildId(String key, String id);

	public abstract void removeChildId(String key, String id);

	public abstract String getOwnerId();

	public abstract void setOwnerId(String ownerID);

	public abstract void setModificationTime(long time);
}
