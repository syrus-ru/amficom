/*
 * $Id: OrComboBox.java,v 1.3 2004/09/27 13:00:19 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ
 */

package com.syrus.AMFICOM.Client.Administrate.Object.UI;

import com.syrus.AMFICOM.Client.General.UI.ObjectResourceComboBox;
import com.syrus.AMFICOM.Client.Resource.Pool;
import java.util.Map;

/**
 * @author $Author: bass $
 * @version $Revision: 1.3 $, $Date: 2004/09/27 13:00:19 $
 * @module generalclient_v1
 */
public class OrComboBox extends ObjectResourceComboBox
{
	public void setTyp(String typ)
	{
		if (typ != null)
		{
			Map map = Pool.getMap(typ);
			if (map != null)
				this.setContents(map, true);
		}
	}

	public void setSelectedTyp(String typ, String id)
	{
		if (id == null)
			this.setSelectedTyp(typ, "");
		else
			this.setSelected(Pool.get(typ, id));
	}
}
