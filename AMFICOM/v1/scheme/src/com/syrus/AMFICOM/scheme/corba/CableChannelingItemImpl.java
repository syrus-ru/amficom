/*
 * $Id: CableChannelingItemImpl.java,v 1.1 2004/11/23 15:42:37 bass Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme.corba;

import com.syrus.AMFICOM.CORBA.Map.*;
import com.syrus.AMFICOM.general.corba.Identifier;
import com.syrus.util.logging.ErrorHandler;

/**
 * @author $Author: bass $
 * @version $Revision: 1.1 $, $Date: 2004/11/23 15:42:37 $
 * @module schemecommon_v1
 */
final class CableChannelingItemImpl extends CableChannelingItem implements Cloneable {
	private static final ErrorHandler ERROR_HANDLER = ErrorHandler.getInstance();

	/**
	 * @todo Check whether constructor is invoked during deserialization.
	 */
	CableChannelingItemImpl() {
	}

	CableChannelingItemImpl(Identifier id) {
		this.thisId = id;
	}

	public CableChannelingItem cloneInstance() {
		try {
			return (CableChannelingItem) this.clone();
		} catch (CloneNotSupportedException cnse) {
			ERROR_HANDLER.error(cnse);
			return null;
		}
	}

	public long created() {
		throw new UnsupportedOperationException();
	}

	public String description() {
		throw new UnsupportedOperationException();
	}

	public void description(String description) {
		throw new UnsupportedOperationException();
	}

	public MapSiteElement_Transferable endSite() {
		throw new UnsupportedOperationException();
	}

	public double endSpare() {
		throw new UnsupportedOperationException();
	}

	public Identifier id() {
		throw new UnsupportedOperationException();
	}

	public long modified() {
		throw new UnsupportedOperationException();
	}

	public String name() {
		throw new UnsupportedOperationException();
	}

	public void name(String name) {
		throw new UnsupportedOperationException();
	}

	public MapPhysicalLinkElement_Transferable physicalLink() {
		throw new UnsupportedOperationException();
	}

	public int placeY() {
		throw new UnsupportedOperationException();
	}

	public int rowX() {
		throw new UnsupportedOperationException();
	}

	public int sequentialNumber() {
		throw new UnsupportedOperationException();
	}

	public MapSiteElement_Transferable startSite() {
		throw new UnsupportedOperationException();
	}

	public double startSpare() {
		throw new UnsupportedOperationException();
	}

	public long version() {
		throw new UnsupportedOperationException();
	}

	protected Object clone() throws CloneNotSupportedException {
		throw new UnsupportedOperationException();
	}
}
