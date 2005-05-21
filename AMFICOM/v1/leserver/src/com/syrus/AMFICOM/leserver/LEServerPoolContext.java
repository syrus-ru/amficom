/*-
 * $Id: LEServerPoolContext.java,v 1.3 2005/05/21 19:56:13 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.leserver;

import com.syrus.AMFICOM.administration.AdministrationStorableObjectPool;
import com.syrus.AMFICOM.administration.DatabaseAdministrationObjectLoader;
import com.syrus.AMFICOM.event.DatabaseEventObjectLoader;
import com.syrus.AMFICOM.event.EventStorableObjectPool;
import com.syrus.AMFICOM.general.DatabaseGeneralObjectLoader;
import com.syrus.AMFICOM.general.GeneralStorableObjectPool;
import com.syrus.AMFICOM.general.PoolContext;
import com.syrus.AMFICOM.general.StorableObjectResizableLRUMap;

/**
 * @version $Revision: 1.3 $, $Date: 2005/05/21 19:56:13 $
 * @author $Author: arseniy $
 * @module leserver_v1
 */
final class LEServerPoolContext extends PoolContext {
	public void init() {
		GeneralStorableObjectPool.init(new DatabaseGeneralObjectLoader(), StorableObjectResizableLRUMap.class);
		AdministrationStorableObjectPool.init(new DatabaseAdministrationObjectLoader(), StorableObjectResizableLRUMap.class);
		EventStorableObjectPool.init(new DatabaseEventObjectLoader(), StorableObjectResizableLRUMap.class);
	}

}
