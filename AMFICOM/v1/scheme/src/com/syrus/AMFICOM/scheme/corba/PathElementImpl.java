/*
 * $Id: PathElementImpl.java,v 1.3 2004/11/24 10:03:58 bass Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.                                              
 * Dept. of Science & Technology.                                               
 * Project: AMFICOM.                                                            
 */

package com.syrus.AMFICOM.scheme.corba;

import com.syrus.AMFICOM.general.corba.Identifier;
import com.syrus.AMFICOM.scheme.corba.PathElementPackage.Type;
import com.syrus.AMFICOM.scheme.SchemeStorableObjectPool;
import com.syrus.util.logging.ErrorHandler;

/**
 * @author $Author: bass $
 * @version $Revision: 1.3 $, $Date: 2004/11/24 10:03:58 $
 * @module schemecommon_v1
 */
final class PathElementImpl extends PathElement implements Cloneable {
	private static final ErrorHandler ERROR_HANDLER = ErrorHandler.getInstance();

	private AbstractSchemeElement abstractSchemeElement;

	PathElementImpl() {
	}

	PathElementImpl(final Identifier id) {
		this.thisId = id;
	}

	public AbstractSchemeElement abstractSchemeElement() {
		try {
			if (this.abstractSchemeElement == null)
				this.abstractSchemeElement = (AbstractSchemeElement) SchemeStorableObjectPool.getStorableObject(this.abstractSchemeElementId, true);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return this.abstractSchemeElement;
	}

	public PathElement cloneInstance() {
		try {
			return (PathElement) this.clone();
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

	public AbstractSchemePort endAbstractSchemePort() {
		throw new UnsupportedOperationException();
	}

	public Identifier id() {
		throw new UnsupportedOperationException();
	}

	public long modified() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see com.syrus.AMFICOM.general.corba.Namable#name()
	 * @todo Maybe, use own private property. However, there's a problem
	 *       with versioning.
	 */
	public String name() {
		return this.abstractSchemeElement().name();
	}

	public void name(String name) {
		throw new UnsupportedOperationException();
	}

	public Scheme scheme() {
		throw new UnsupportedOperationException();
	}

	public SchemeCableThread schemeCableThread() {
		throw new UnsupportedOperationException();
	}

	public int sequentialNumber() {
		throw new UnsupportedOperationException();
	}

	public AbstractSchemePort startAbstractSchemePort() {
		throw new UnsupportedOperationException();
	}

	public Type type() {
		throw new UnsupportedOperationException();
	}

	public long version() {
		throw new UnsupportedOperationException();
	}

	protected Object clone() throws CloneNotSupportedException {
		throw new UnsupportedOperationException();
	}
}
