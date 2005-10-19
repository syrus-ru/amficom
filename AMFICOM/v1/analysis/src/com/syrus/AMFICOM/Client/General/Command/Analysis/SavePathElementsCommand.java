/*-
 * $Id: SavePathElementsCommand.java,v 1.3 2005/10/19 08:03:55 saa Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.Client.General.Command.Analysis;

import com.syrus.AMFICOM.client.model.AbstractCommand;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.util.Log;

//FIXME: unused
public class SavePathElementsCommand  extends AbstractCommand {

	public SavePathElementsCommand(ApplicationContext aContext) {
		// empty
	}
	
	@Override
	public void execute()
	{
		try {
			StorableObjectPool.flush(ObjectEntities.SCHEMECABLELINK_CODE, LoginManager.getUserId(), false);
			StorableObjectPool.flush(ObjectEntities.SCHEMELINK_CODE, LoginManager.getUserId(), false);
		} catch (ApplicationException e) {
			Log.errorException(e);
		}
	}
}
