/*
 * $Id: SchemeMonitoringSolutionImpl.java,v 1.7 2004/12/21 15:35:01 bass Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme.corba;

import com.syrus.AMFICOM.general.corba.*;
import com.syrus.util.logging.ErrorHandler;

/**
 * @author $Author: bass $
 * @version $Revision: 1.7 $, $Date: 2004/12/21 15:35:01 $
 * @module scheme_v1
 */
final class SchemeMonitoringSolutionImpl extends SchemeMonitoringSolution implements Cloneable {
	private static final ErrorHandler ERROR_HANDLER = ErrorHandler.getInstance();

	private static final long serialVersionUID = 3617859659659358515L;

	SchemeMonitoringSolutionImpl() {
	}

	/**
	 * @see StorableObject#changed()
	 */
	public boolean changed() {
		throw new UnsupportedOperationException();
	}

	public SchemeMonitoringSolution cloneInstance() {
		try {
			return (SchemeMonitoringSolution) this.clone();
		} catch (CloneNotSupportedException cnse) {
			ERROR_HANDLER.error(cnse);
			return null;
		}
	}

	public long created() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see StorableObject#creatorId()
	 */
	public Identifier creatorId() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see StorableObject#dependencies()
	 */
	public StorableObject[] dependencies() {
		throw new UnsupportedOperationException();
	}

	public String description() {
		throw new UnsupportedOperationException();
	}

	public void description(String description) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see StorableObject#headerTransferable()
	 */
	public StorableObject_Transferable headerTransferable() {
		throw new UnsupportedOperationException();
	}

	public Identifier id() {
		throw new UnsupportedOperationException();
	}

	public long modified() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see StorableObject#modifierId()
	 */
	public Identifier modifierId() {
		throw new UnsupportedOperationException();
	}

	public String name() {
		throw new UnsupportedOperationException();
	}

	public void name(String name) {
		throw new UnsupportedOperationException();
	}

	public double price() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param newPrice
	 * @see com.syrus.AMFICOM.scheme.corba.SchemeMonitoringSolution#price(double)
	 */
	public void price(double newPrice) {
		throw new UnsupportedOperationException();
	}

	public Scheme scheme() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param newScheme
	 * @see com.syrus.AMFICOM.scheme.corba.SchemeMonitoringSolution#scheme(com.syrus.AMFICOM.scheme.corba.Scheme)
	 */
	public void scheme(Scheme newScheme) {
		throw new UnsupportedOperationException();
	}

	public SchemePath[] schemePaths() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param newSchemePaths
	 * @see com.syrus.AMFICOM.scheme.corba.SchemeMonitoringSolution#schemePaths(com.syrus.AMFICOM.scheme.corba.SchemePath[])
	 */
	public void schemePaths(SchemePath[] newSchemePaths) {
		throw new UnsupportedOperationException();
	}

	public long version() {
		throw new UnsupportedOperationException();
	}

	protected Object clone() throws CloneNotSupportedException {
		final SchemeMonitoringSolutionImpl schemeMonitoringSolution = (SchemeMonitoringSolutionImpl) super.clone();
		/**
		 * @todo Update the newly created object.
		 */
		return schemeMonitoringSolution;
	}
}
