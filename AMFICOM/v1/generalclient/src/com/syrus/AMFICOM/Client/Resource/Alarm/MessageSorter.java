package com.syrus.AMFICOM.Client.Resource.Alarm;

import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.general.StorableObject;

/**
 * @version $Revision: 1.3 $, $Date: 2005/05/13 19:05:47 $
 * @author $Author: bass $
 *
 * @deprecated
 */
final class MessageSorter extends ObjectResourceSorter {
	private MessageSorter() {
	}

	public long getLong(StorableObject or, String column) {
		throw new UnsupportedOperationException();
	}

	public String[][] getSortedColumns() {
		throw new UnsupportedOperationException();
	}

	public String getString(StorableObject or, String column) {
		throw new UnsupportedOperationException();
	}
}
