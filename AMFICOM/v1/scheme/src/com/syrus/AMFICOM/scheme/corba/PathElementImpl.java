/*
 * $Id: PathElementImpl.java,v 1.8 2004/12/21 15:35:01 bass Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.                                              
 * Dept. of Science & Technology.                                               
 * Project: AMFICOM.                                                            
 */

package com.syrus.AMFICOM.scheme.corba;

import com.syrus.AMFICOM.general.corba.*;
import com.syrus.AMFICOM.scheme.SchemeStorableObjectPool;
import com.syrus.AMFICOM.scheme.corba.PathElementPackage.Type;
import com.syrus.util.logging.ErrorHandler;

/**
 * @author $Author: bass $
 * @version $Revision: 1.8 $, $Date: 2004/12/21 15:35:01 $
 * @module scheme_v1
 */
final class PathElementImpl extends PathElement implements Cloneable {
	private static final ErrorHandler ERROR_HANDLER = ErrorHandler.getInstance();

	private static final long serialVersionUID = 3833746603058934071L;

	private AbstractSchemeElement abstractSchemeElement;

	PathElementImpl() {
	}

	/**
	 * @see PathElement#abstractSchemeElement()
	 */
	public AbstractSchemeElement abstractSchemeElement() {
		try {
			if (this.abstractSchemeElement == null)
				this.abstractSchemeElement = (AbstractSchemeElement) SchemeStorableObjectPool.getStorableObject(this.abstractSchemeElementId, true);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return this.abstractSchemeElement;
	}

	/**
	 * @see PathElement#abstractSchemeElement(AbstractSchemeElement)
	 */
	public void abstractSchemeElement(AbstractSchemeElement abstractSchemeElement) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see StorableObject#changed()
	 */
	public boolean changed() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see PathElement#cloneInstance()
	 */
	public PathElement cloneInstance() {
		try {
			return (PathElement) this.clone();
		} catch (CloneNotSupportedException cnse) {
			ERROR_HANDLER.error(cnse);
			return null;
		}
	}

	/**
	 * @see StorableObject#created()
	 */
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

	/**
	 * @see Describable#description()
	 */
	public String description() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see Describable#description(java.lang.String)
	 */
	public void description(String description) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see PathElement#endAbstractSchemePort()
	 */
	public AbstractSchemePort endAbstractSchemePort() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see PathElement#endAbstractSchemePort(AbstractSchemePort)
	 */
	public void endAbstractSchemePort(AbstractSchemePort endAbstractSchemePort) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see StorableObject#headerTransferable()
	 */
	public StorableObject_Transferable headerTransferable() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see Identifiable#id()
	 */
	public Identifier id() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see StorableObject#modified()
	 */
	public long modified() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see StorableObject#modifierId()
	 */
	public Identifier modifierId() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see Namable#name()
	 * @todo Maybe, use own private property. However, there's a problem
	 *       with versioning.
	 */
	public String name() {
		return this.abstractSchemeElement().name();
	}

	/**
	 * @see Namable#name(java.lang.String)
	 */
	public void name(String name) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see PathElement#scheme()
	 */
	public Scheme scheme() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see PathElement#scheme(Scheme)
	 */
	public void scheme(Scheme scheme) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see PathElement#schemeCableThread()
	 */
	public SchemeCableThread schemeCableThread() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see PathElement#schemeCableThread(SchemeCableThread)
	 */
	public void schemeCableThread(SchemeCableThread schemeCableThread) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see PathElement#sequentialNumber()
	 */
	public int sequentialNumber() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see PathElement#sequentialNumber(int)
	 */
	public void sequentialNumber(int sequentialNumber) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see PathElement#startAbstractSchemePort()
	 */
	public AbstractSchemePort startAbstractSchemePort() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see PathElement#startAbstractSchemePort(AbstractSchemePort)
	 */
	public void startAbstractSchemePort(AbstractSchemePort startAbstractSchemePort) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see PathElement#type()
	 */
	public Type type() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see PathElement#type(Type)
	 */
	public void type(Type type) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see StorableObject#version()
	 */
	public long version() {
		throw new UnsupportedOperationException();
	}

	protected Object clone() throws CloneNotSupportedException {
		final PathElementImpl pathElement = (PathElementImpl) super.clone();
		/**
		 * @todo Update the newly created object.
		 */
		return pathElement;
	}
}
