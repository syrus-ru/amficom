package com.syrus.AMFICOM.corba.portable.reflect.common;

import com.syrus.AMFICOM.corba.portable.common.Identifier;

/**
 * @version $Revision: 1.1 $, $Date: 2004/06/22 12:27:24 $
 * @author $Author: bass $
 */
public class AbstractEventSourceImpl extends AbstractEventSource {
	protected AbstractEventSourceImpl() {
	}

	public Identifier getId() {
		return id;
	}
}
