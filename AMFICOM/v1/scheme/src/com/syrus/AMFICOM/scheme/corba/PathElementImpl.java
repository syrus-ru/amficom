/*
 * $Id: PathElementImpl.java,v 1.6 2004/12/15 15:08:31 bass Exp $
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
import java.util.*;

/**
 * @author $Author: bass $
 * @version $Revision: 1.6 $, $Date: 2004/12/15 15:08:31 $
 * @module schemecommon_v1
 */
final class PathElementImpl extends PathElement implements Cloneable {
	private static final ErrorHandler ERROR_HANDLER = ErrorHandler.getInstance();

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
	 * @see java.util.JavaUtilIStorableObject#createdImpl()
	 */
	public Date createdImpl() {
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
	 * @see JavaUtilIStorableObject#getCreated()
	 */
	public Date getCreated() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see JavaUtilIStorableObject#getDependencies()
	 */
	public List getDependencies() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see IStorableObject#getHeaderTransferable()
	 */
	public StorableObject_Transferable getHeaderTransferable() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see JavaUtilIStorableObject#getModified()
	 */
	public Date getModified() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see Identifiable#id()
	 */
	public Identifier id() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see IStorableObject#isChanged()
	 */
	public boolean isChanged() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see StorableObject#modified()
	 */
	public long modified() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see java.util.JavaUtilIStorableObject#modifiedImpl()
	 */
	public Date modifiedImpl() {
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
		throw new UnsupportedOperationException();
	}
}
