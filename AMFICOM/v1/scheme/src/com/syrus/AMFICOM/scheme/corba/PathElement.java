/*
 * $Id: PathElement.java,v 1.3 2005/03/11 17:26:58 bass Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme.corba;

import com.syrus.AMFICOM.general.*;
import com.syrus.AMFICOM.general.corba.*;
import com.syrus.AMFICOM.scheme.SchemeStorableObjectPool;
import com.syrus.AMFICOM.scheme.corba.PathElementPackage.Type;
import java.util.*;

/**
 * PathElement has no associated <code>name</code> (or <code>thisName</code>)
 * field: its {@link PathElement#name() name()}method actually returns
 * {@link PathElement#abstractSchemeElement() abstractSchemeElement()}
 * <code>.</code> {@link AbstractSchemeElement#name() name()}.
 * 
 * @author $Author: bass $
 * @version $Revision: 1.3 $, $Date: 2005/03/11 17:26:58 $
 * @module scheme_v1
 */
public final class PathElement extends CloneableStorableObject implements Namable,
		Describable {

	/**
	 * Depending on {@link #thisType}, may reference either
	 * {@link SchemeLink}or {@link SchemeCableLink}or
	 * {@link SchemeElement}.
	 */
	protected Identifier abstractSchemeElementId = null;

	/**
	 * Depending on {@link #thisType}, may reference either
	 * {@link SchemePort}or {@link SchemeCablePort}.
	 */
	protected Identifier endAbstractSchemePortId = null;

	/**
	 * Empty if type is other than
	 * {@link com.syrus.AMFICOM.scheme.corba.PathElementPackage.Type#SCHEME_CABLE_LINK}.
	 */
	protected Identifier schemeCableThreadId = null;

	protected Identifier schemeId = null;

	/**
	 * Depending on {@link #thisType}, may reference either
	 * {@link SchemePort}or {@link SchemeCablePort}.
	 */
	protected Identifier startAbstractSchemePortId = null;

	protected String thisDescription = null;

	protected int thisSequentialNumber = 0;

	protected Type thisType = null;

	private AbstractSchemeElement abstractSchemeElement;

	/**
	 * @param id
	 */
	protected PathElement(Identifier id) {
		super(id);
	}

	/**
	 * @param id
	 * @param created
	 * @param modified
	 * @param creatorId
	 * @param modifierId
	 * @param version
	 */
	protected PathElement(Identifier id, Date created, Date modified,
			Identifier creatorId, Identifier modifierId,
			long version) {
		super(id, created, modified, creatorId, modifierId, version);
	}

	/**
	 * @see PathElement#abstractSchemeElement()
	 */
	public AbstractSchemeElement abstractSchemeElement() {
		try {
			if (this.abstractSchemeElement == null)
				this.abstractSchemeElement = (AbstractSchemeElement) SchemeStorableObjectPool
						.getStorableObject(
								this.abstractSchemeElementId,
								true);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return this.abstractSchemeElement;
	}

	/**
	 * @see PathElement#abstractSchemeElement(AbstractSchemeElement)
	 */
	public void abstractSchemeElement(
			AbstractSchemeElement abstractSchemeElement) {
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
	public void endAbstractSchemePort(
			AbstractSchemePort endAbstractSchemePort) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see StorableObject#getCreated()
	 */
	public Date getCreated() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see StorableObject#getCreatorId()
	 */
	public Identifier getCreatorId() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see StorableObject#getDependencies()
	 */
	public List getDependencies() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see StorableObject#getHeaderTransferable()
	 */
	public StorableObject_Transferable getHeaderTransferable() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see Identifiable#getId()
	 */
	public Identifier getId() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see StorableObject#getModified()
	 */
	public Date getModified() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see StorableObject#getModifierId()
	 */
	public Identifier getModifierId() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see StorableObject#getVersion()
	 */
	public long getVersion() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see StorableObject#isChanged()
	 */
	public boolean isChanged() {
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
	public void startAbstractSchemePort(
			AbstractSchemePort startAbstractSchemePort) {
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

	public Object clone() {
		final PathElement pathElement = (PathElement) super.clone();
		/**
		 * @todo Update the newly created object.
		 */
		return pathElement;
	}

	/**
	 * @see com.syrus.AMFICOM.general.TransferableObject#getTransferable()
	 */
	public Object getTransferable() {
		throw new UnsupportedOperationException();
	}

	public static PathElement createInstance(final Identifier creatorId)
			throws CreateObjectException {
		assert creatorId != null;
		try {
			final Date created = new Date();
			final PathElement pathElement = new PathElement(
					IdentifierPool
							.getGeneratedIdentifier(ObjectEntities.PATH_ELEMENT_ENTITY_CODE),
					created, created, creatorId, creatorId,
					0L);
			pathElement.changed = true;
			return pathElement;
		} catch (final IllegalObjectEntityException ioee) {
			throw new CreateObjectException(
					"PathElement.createInstance | cannot generate identifier ", ioee); //$NON-NLS-1$
		}
	}

	/**
	 * @deprecated Use {@link #createInstance(Identifier)}instead.
	 */
	public static PathElement createInstance() {
		throw new UnsupportedOperationException();
	}
}
