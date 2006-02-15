/**
 * $Id: DefaultStorableObjectRemoveWrapper.java,v 1.2 2006/02/15 11:27:49 stas Exp $
 *
 * Copyright 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.client.UI.dialogs;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.2 $, $Date: 2006/02/15 11:27:49 $
 * @author $Author: stas $
 * @author Andrei Kroupennikov
 * @module mapviewclient
 */
public class DefaultStorableObjectRemoveWrapper implements RemoveWrapper {

	private static DefaultStorableObjectRemoveWrapper instance = null;
	
	protected DefaultStorableObjectRemoveWrapper() {
		// empty
	}
	
	public static DefaultStorableObjectRemoveWrapper getInstance() {
		if(instance == null) {
			instance = new DefaultStorableObjectRemoveWrapper();
		}
		return instance;
	}

	public boolean remove(Object object) {
		Identifier id = ((StorableObject )object).getId();
		StorableObjectPool.delete(id);
		try {
			StorableObjectPool.flush(id, LoginManager.getUserId(), true);
			return true;
		} catch(ApplicationException e1) {
			Log.errorMessage(e1);
		}
		return false;
	}

}
