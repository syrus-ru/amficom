/*
 * $Id: SchemeProtoGroupImpl.java,v 1.1 2004/11/24 10:03:58 bass Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme.corba;

import com.syrus.AMFICOM.CORBA.Resource.ImageResource_Transferable;
import com.syrus.AMFICOM.general.corba.Identifier;
import com.syrus.util.logging.ErrorHandler;

/**
 * @author $Author: bass $
 * @version $Revision: 1.1 $, $Date: 2004/11/24 10:03:58 $
 * @module schemecommon_v1
 */
final class SchemeProtoGroupImpl extends SchemeProtoGroup implements Cloneable {
	private static final ErrorHandler ERROR_HANDLER = ErrorHandler.getInstance();

	SchemeProtoGroupImpl() {
	}

	SchemeProtoGroupImpl(final Identifier id) {
		this.thisId = id;
	}

	public SchemeProtoGroup cloneInstance() {
		try {
			return (SchemeProtoGroup) this.clone();
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

	public void description(String newDescription) {
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

	public void name(String newName) {
		throw new UnsupportedOperationException();
	}

	public SchemeProtoGroup parent() {
		throw new UnsupportedOperationException();
	}

	public String schemeProtoElementClass() {
		throw new UnsupportedOperationException();
	}

	public SchemeProtoElement[] schemeProtoElements() {
		throw new UnsupportedOperationException();
	}

	public SchemeProtoGroup[] schemeProtoGroups() {
		throw new UnsupportedOperationException();
	}

	public ImageResource_Transferable symbol() {
		throw new UnsupportedOperationException();
	}

	public void symbol(ImageResource_Transferable newSymbol) {
		throw new UnsupportedOperationException();
	}

	public long version() {
		throw new UnsupportedOperationException();
	}

	protected Object clone() throws CloneNotSupportedException {
		throw new UnsupportedOperationException();
	}
}
