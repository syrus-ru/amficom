package com.syrus.AMFICOM.Client.Map.UI;

import com.syrus.AMFICOM.client_.general.ui_.ObjectResourceChooserDialog;
import com.syrus.AMFICOM.client_.resource.ObjectResourceController;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.mapview.MapViewStorableObjectPool;

/**
 * 
 * @author $Author: krupenn $
 * @version $Revision: 1.1 $, $Date: 2005/03/04 14:31:35 $
 * @module mapviewclient_v1
 */
public class MapViewChooserDialog extends ObjectResourceChooserDialog 
{
	public MapViewChooserDialog(String title, ObjectResourceController controller)
	{
		super(title, controller);
	}

	protected boolean delete(Object obj)
	{
		try 
		{
			MapViewStorableObjectPool.delete(((StorableObject )obj).getId());
			MapViewStorableObjectPool.flush(true);
		} 
		catch (Exception ex) 
		{
			ex.printStackTrace();
			return false;
		} 
		return true;
	}
}