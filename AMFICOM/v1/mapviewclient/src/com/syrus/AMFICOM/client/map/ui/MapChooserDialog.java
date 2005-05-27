package com.syrus.AMFICOM.Client.Map.UI;

import com.syrus.AMFICOM.client.UI.dialogs.WrapperedTableChooserDialog;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.util.Wrapper;

/**
 * 
 * @author $Author: krupenn $
 * @version $Revision: 1.2 $, $Date: 2005/05/27 15:14:59 $
 * @module mapviewclient_v1
 */
public class MapChooserDialog extends WrapperedTableChooserDialog 
{
	public MapChooserDialog(String title, Wrapper controller, String[] keys)
	{
		super(title, controller, keys);
	}

	protected boolean delete(Object obj)
	{
		try 
		{
			Identifier id = ((StorableObject )obj).getId();
			StorableObjectPool.delete(id);
			StorableObjectPool.flush(id, true);
		} 
		catch (Exception ex) 
		{
			ex.printStackTrace();
			return false;
		} 
		return true;
	}
}