/*
 * $Id: DomainForAdmin.java,v 1.3 2004/09/27 15:49:37 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ
 */

package com.syrus.AMFICOM.Client.Resource.Object;

import com.syrus.AMFICOM.Client.Administrate.Object.UI.DomainPaneAdmin;
import com.syrus.AMFICOM.Client.General.UI.ObjectResourcePropertiesPane;

/**
 * @author $Author: bass $
 * @version $Revision: 1.3 $, $Date: 2004/09/27 15:49:37 $
 * @module generalclient_v1
 */
public class DomainForAdmin extends Domain
{
	public static ObjectResourcePropertiesPane getPropertyPane()
	{
		return new DomainPaneAdmin();
	}
}
