package com.syrus.AMFICOM.client.map.ui;

import com.syrus.AMFICOM.client.UI.dialogs.WrapperedTableChooserDialog;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.util.Wrapper;

/**
 * 
 * @author $Author: krupenn $
 * @version $Revision: 1.3 $, $Date: 2005/06/06 12:20:35 $
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