package com.syrus.AMFICOM.corba.portable.reflect.common;

import com.syrus.AMFICOM.corba.portable.common.Identifier;

/**
 * @version $Revision: 1.1 $, $Date: 2004/06/22 12:27:26 $
 * @author $Author: bass $
 */
public class ImmutableObjectResourceImpl extends ImmutableObjectResource {
	ImmutableObjectResourceImpl() {
	}

	public Identifier getId() {
		return id;
	}

	public String getCodename() {
		return codename;
	}

	public String getName() {
		return name;
	}
}
