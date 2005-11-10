/*-
 * $Id: ObjectEntitiesTest.java,v 1.1 2005/11/10 15:47:05 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.general;

import static java.util.logging.Level.ALL;
import static java.util.logging.Level.FINEST;

import com.syrus.util.Application;
import com.syrus.util.Log;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.1 $, $Date: 2005/11/10 15:47:05 $
 * @module general
 */
final class ObjectEntitiesTest {
	private ObjectEntitiesTest() {
		assert false;
	}

	public static void main(final String args[]) {
		Application.init("common");
		Log.setLevel(ALL);
		for (final short groupCode : ObjectGroupEntities.getCodes()) {
			Log.debugMessage(groupCode + "\t" + ObjectGroupEntities.codeToString(groupCode), FINEST);

			for (final short entityCode : ObjectGroupEntities.getEntityCodes(groupCode).toArray()) {
				Log.debugMessage("\t" + entityCode + "\t" + ObjectEntities.codeToString(entityCode), FINEST);
			}
		}
	}
}
