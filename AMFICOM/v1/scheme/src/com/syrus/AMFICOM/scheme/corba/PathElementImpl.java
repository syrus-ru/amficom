/*
 * $Id: PathElementImpl.java,v 1.1 2004/11/23 13:11:13 bass Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.                                              
 * Dept. of Science & Technology.                                               
 * Project: AMFICOM.                                                            
 */

package com.syrus.AMFICOM.scheme.corba;

import com.syrus.AMFICOM.general.corba.Identifier;
import com.syrus.AMFICOM.scheme.corba.PathElementPackage.Type;
import com.syrus.AMFICOM.scheme.SchemeStorableObjectPool;

/**
 * @author $Author: bass $
 * @version $Revision: 1.1 $, $Date: 2004/11/23 13:11:13 $
 * @module schemecommon_v1
 */
final class PathElementImpl extends PathElement {
	private AbstractSchemeElement abstractSchemeElement;

	PathElementImpl() {
	}

	PathElementImpl(Identifier id) {
		this.thisId = id;
	}

	public int sequentialNumber() {
		throw new UnsupportedOperationException();
	}

	public Type type() {
		throw new UnsupportedOperationException();
	}

	public SchemeCableThread schemeCableThread() {
		throw new UnsupportedOperationException();
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

	public AbstractSchemePort startAbstractSchemePort() {
		throw new UnsupportedOperationException();
	}

	public AbstractSchemePort endAbstractSchemePort() {
		throw new UnsupportedOperationException();
	}

	public Scheme scheme() {
		throw new UnsupportedOperationException();
	}

	public PathElement cloneInstance() {
		throw new UnsupportedOperationException();
	}

	public long created() {
		throw new UnsupportedOperationException();
	}

	public long modified() {
		throw new UnsupportedOperationException();
	}

	public long version() {
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

	public void name(String newName) {
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
}
