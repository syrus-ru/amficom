/*-
 * $Id: PathElement.java,v 1.9 2005/03/30 10:19:51 bass Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme;

import com.syrus.AMFICOM.configuration.corba.PortTypeSort;
import com.syrus.AMFICOM.general.*;
import com.syrus.AMFICOM.scheme.corba.PathElementKind;
import com.syrus.util.Log;
import java.util.*;

/**
 * #15 in hierarchy.
 * 
 * PathElement has no associated <code>name</code> field:
 * its {@link PathElement#getName() getName()} method actually returns
 * {@link PathElement#getAbstractSchemeElement() getAbstractSchemeElement()}<code>.</code>{@link AbstractSchemeElement#getName() getName()}.
 * 
 * @author $Author: bass $
 * @version $Revision: 1.9 $, $Date: 2005/03/30 10:19:51 $
 * @module scheme_v1
 */
public final class PathElement extends AbstractCloneableStorableObject implements Describable, Comparable {
	private static final long serialVersionUID = 3905799768986038576L;

	/**
	 * Depending on {@link #pathElementKind}, may reference either
	 * {@link SchemePort} or {@link SchemeCablePort}. Empty if type is other
	 * than {@link PathElementKind#SCHEME_ELEMENT}.
	 */
	private Identifier endAbstractSchemePortId;

	private Identifier parentSchemePathId;

	private PathElementKind pathElementKind;

	/**
	 * Empty if type is other than
	 * {@link PathElementKind#SCHEME_CABLE_LINK}.
	 */
	private Identifier schemeCableThreadId;

	/**
	 * Empty if type is other than {@link PathElementKind#SCHEME_LINK}.
	 */
	private Identifier schemeLinkId;

	private int sequentialNumber;

	/**
	 * Depending on {@link #pathElementKind}, may reference either
	 * {@link SchemePort} or {@link SchemeCablePort}. Empty if type is other
	 * than {@link PathElementKind#SCHEME_ELEMENT}.
	 */
	private Identifier startAbstractSchemePortId;

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
	 * @deprecated Use {@link #createInstance(Identifier, PathElementKind)} instead.
	 */
	public static PathElement createInstance() {
		throw new UnsupportedOperationException();
	}

	public static PathElement createInstance(final Identifier creatorId, final PathElementKind pathElementKind)
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

	public Object clone() {
		final PathElement pathElement = (PathElement) super.clone();
		/**
		 * @todo Update the newly created object.
		 */
		return pathElement;
	}

	/**
	 * @param o
	 * @see Comparable#compareTo(Object)
	 */
	public int compareTo(final Object o) {
		return compareTo((PathElement) o);
	}

	/**
	 * @param that
	 */
	public int compareTo(final PathElement that) {
		assert this.parentSchemePathId.equals(that.parentSchemePathId): ErrorMessages.NO_COMMON_PARENT; 
		return this.sequentialNumber <= that.sequentialNumber ? this.sequentialNumber < that.sequentialNumber ? -1 : 0 : 1; 
	}

	public AbstractSchemeElement getAbstractSchemeElement() {
		switch (this.pathElementKind.value()) {
			case PathElementKind._SCHEME_CABLE_LINK:
				return getSchemeCableLink();
			case PathElementKind._SCHEME_ELEMENT:
				return getSchemeElement();
			case PathElementKind._SCHEME_LINK:
				return getSchemeCableLink();
			default:
				throw new UnsupportedOperationException(ErrorMessages.OBJECT_STATE_ILLEGAL);
		}
	}

	/**
	 * @see StorableObject#getDependencies()
	 */
	public List getDependencies() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see Describable#getDescription()
	 */
	public String getDescription() {
		return getAbstractSchemeElement().getDescription();
	}

	public AbstractSchemePort getEndAbstractSchemePort() {
		if (this.pathElementKind.value() != PathElementKind._SCHEME_ELEMENT)
			throw new UnsupportedOperationException(ErrorMessages.OBJECT_STATE_ILLEGAL);
		assert this.endAbstractSchemePortId != null: ErrorMessages.OBJECT_NOT_INITIALIZED;
		assert !this.endAbstractSchemePortId.equals(Identifier.VOID_IDENTIFIER): ErrorMessages.OBJECT_BADLY_INITIALIZED;
		try {
			return (AbstractSchemePort) SchemeStorableObjectPool.getStorableObject(this.endAbstractSchemePortId, true);
		} catch (final ApplicationException ae) {
			/*
			 * Never.
			 */
			assert false: ae.getMessage();
			Log.debugException(ae, Log.SEVERE);
			return null;
		}
	}

	/**
	 * @see Namable#getName()
	 */
	public String getName() {
		return getAbstractSchemeElement().getName();
	}

	public Scheme getParentScheme() {
		return getAbstractSchemeElement().getParentScheme();
	}

	public SchemePath getParentSchemePath() {
		throw new UnsupportedOperationException();
	}

	public PathElementKind getPathElementKind() {
		return this.pathElementKind;
	}

	public SchemeCableLink getSchemeCableLink() {
		return getSchemeCableThread().getParentSchemeCableLink();
	}

	public SchemeCableThread getSchemeCableThread() {
		if (this.pathElementKind.value() != PathElementKind._SCHEME_CABLE_LINK)
			throw new UnsupportedOperationException(ErrorMessages.OBJECT_STATE_ILLEGAL);
		assert this.schemeCableThreadId != null: ErrorMessages.OBJECT_NOT_INITIALIZED;
		assert !this.schemeCableThreadId.equals(Identifier.VOID_IDENTIFIER): ErrorMessages.OBJECT_BADLY_INITIALIZED;
		try {
			return (SchemeCableThread) SchemeStorableObjectPool.getStorableObject(this.schemeCableThreadId, true);
		} catch (final ApplicationException ae) {
			/*
			 * Never.
			 */
			assert false: ae.getMessage();
			Log.debugException(ae, Log.SEVERE);
			return null;
		}
	}

	public SchemeElement getSchemeElement() {
		final SchemeDevice parentSchemeDevice = getStartAbstractSchemePort().getParentSchemeDevice();
		assert (parentSchemeDevice == getEndAbstractSchemePort().getParentSchemeDevice()): ErrorMessages.NO_COMMON_PARENT;
		return parentSchemeDevice.getParentSchemeElement();
	}

	public SchemeLink getSchemeLink() {
		if (this.pathElementKind.value() != PathElementKind._SCHEME_LINK)
			throw new UnsupportedOperationException(ErrorMessages.OBJECT_STATE_ILLEGAL);
		assert this.schemeLinkId != null: ErrorMessages.OBJECT_NOT_INITIALIZED;
		assert !this.schemeLinkId.equals(Identifier.VOID_IDENTIFIER): ErrorMessages.OBJECT_BADLY_INITIALIZED;
		try {
			return (SchemeLink) SchemeStorableObjectPool.getStorableObject(this.schemeLinkId, true);
		} catch (final ApplicationException ae) {
			/*
			 * Never.
			 */
			assert false: ae.getMessage();
			Log.debugException(ae, Log.SEVERE);
			return null;
		}
	}

	public int getSequentialNumber() {
		return this.sequentialNumber;
	}

	public AbstractSchemePort getStartAbstractSchemePort() {
		if (this.pathElementKind.value() != PathElementKind._SCHEME_ELEMENT)
			throw new UnsupportedOperationException(ErrorMessages.OBJECT_STATE_ILLEGAL);
		assert this.startAbstractSchemePortId != null: ErrorMessages.OBJECT_NOT_INITIALIZED;
		assert !this.startAbstractSchemePortId.equals(Identifier.VOID_IDENTIFIER): ErrorMessages.OBJECT_BADLY_INITIALIZED;
		try {
			return (AbstractSchemePort) SchemeStorableObjectPool.getStorableObject(this.startAbstractSchemePortId, true);
		} catch (final ApplicationException ae) {
			/*
			 * Never.
			 */
			assert false: ae.getMessage();
			Log.debugException(ae, Log.SEVERE);
			return null;
		}
	}

	/**
	 * @see TransferableObject#getTransferable()
	 */
	public Object getTransferable() {
		throw new UnsupportedOperationException();
	}

	public void setAbstractSchemeElement(final AbstractSchemeElement abstractSchemeElement) {
		switch (this.pathElementKind.value()) {
			case PathElementKind._SCHEME_CABLE_LINK:
				setSchemeCableLink((SchemeCableLink) abstractSchemeElement);
			case PathElementKind._SCHEME_ELEMENT:
				setSchemeElement((SchemeElement) abstractSchemeElement);
			case PathElementKind._SCHEME_LINK:
				setSchemeLink((SchemeLink) abstractSchemeElement);
			default:
				throw new UnsupportedOperationException(ErrorMessages.OBJECT_STATE_ILLEGAL);
		}
	}

	/**
	 * @see Describable#setDescription(String)
	 */
	public void setDescription(final String description) {
		getAbstractSchemeElement().setDescription(description);
	}

	public void setEndAbstractSchemePort(final AbstractSchemePort endAbstractSchemePort) {
		if (this.pathElementKind.value() != PathElementKind._SCHEME_ELEMENT)
			throw new UnsupportedOperationException(ErrorMessages.OBJECT_STATE_ILLEGAL);
		assert endAbstractSchemePort != null: ErrorMessages.NON_NULL_EXPECTED;
		/*
		 * Either the object is not yet initialized (which is very
		 * unlikely), or ensure that starting and ending ports belong to
		 * the same device (do compare references, see bug #86). Also
		 * note that this code WILL NOT work when bug #88 is fixed
		 * (scheme ports and scheme cable ports will have NO parent
		 * device).
		 */
		assert (this.startAbstractSchemePortId.equals(Identifier.VOID_IDENTIFIER)) 
				|| getStartAbstractSchemePort().getParentSchemeDevice()
				== endAbstractSchemePort.getParentSchemeDevice(): ErrorMessages.NO_COMMON_PARENT;
		final Identifier newEndAbstractSchemePortId = endAbstractSchemePort.getId();
		if (this.endAbstractSchemePortId.equals(newEndAbstractSchemePortId))
			return;
		this.endAbstractSchemePortId = newEndAbstractSchemePortId;
		this.changed = true;
	}

	/**
	 * @see Namable#setName(String)
	 */
	public void setName(final String name) {
		getAbstractSchemeElement().setName(name);
	}

	public void setParentScheme(final Scheme parentScheme) {
		getAbstractSchemeElement().setParentScheme(parentScheme);
	}

	public void setParentSchemePath(final SchemePath parentSchemePath) {
		throw new UnsupportedOperationException();
	}

	public void setSchemeCableLink(final SchemeCableLink schemeCableLink) {
		getSchemeCableThread().setParentSchemeCableLink(schemeCableLink);
	}

	public void setSchemeCableThread(final SchemeCableThread schemeCableThread) {
		if (this.pathElementKind.value() != PathElementKind._SCHEME_CABLE_LINK)
			throw new UnsupportedOperationException(ErrorMessages.OBJECT_STATE_ILLEGAL);
		assert schemeCableThread != null: ErrorMessages.NON_NULL_EXPECTED;
		final Identifier newSchemeCableThreadId = schemeCableThread.getId();
		if (this.schemeCableThreadId.equals(newSchemeCableThreadId))
			return;
		this.schemeCableThreadId = newSchemeCableThreadId;
		this.changed = true;
	}

	public void setSchemeElement(final SchemeElement schemeElement) {
		final SchemeDevice parentSchemeDevice = getStartAbstractSchemePort().getParentSchemeDevice();
		assert (parentSchemeDevice == getEndAbstractSchemePort().getParentSchemeDevice()): ErrorMessages.NO_COMMON_PARENT;
		parentSchemeDevice.setParentSchemeElement(schemeElement);
	}

	public void setSchemeLink(final SchemeLink schemeLink) {
		if (this.pathElementKind.value() != PathElementKind._SCHEME_LINK)
			throw new UnsupportedOperationException(ErrorMessages.OBJECT_STATE_ILLEGAL);
		assert schemeLink != null: ErrorMessages.NON_NULL_EXPECTED;
		final Identifier newSchemeLinkId = schemeLink.getId();
		if (this.schemeLinkId.equals(newSchemeLinkId))
			return;
		this.schemeLinkId = newSchemeLinkId;
		this.changed = true;
	}

	public void setSequentialNumber(final int sequentialNumber) {
		if (this.sequentialNumber == sequentialNumber)
			return;
		this.sequentialNumber = sequentialNumber;
		this.changed = true;
	}

	public void setStartAbstractSchemePort(final AbstractSchemePort startAbstractSchemePort) {
		if (this.pathElementKind.value() != PathElementKind._SCHEME_ELEMENT)
			throw new UnsupportedOperationException(ErrorMessages.OBJECT_STATE_ILLEGAL);
		assert startAbstractSchemePort != null: ErrorMessages.NON_NULL_EXPECTED;
		/*
		 * Either the object is not yet initialized (which is very
		 * unlikely), or ensure that starting and ending ports belong to
		 * the same device (do compare references, see bug #86). Also
		 * note that this code WILL NOT work when bug #88 is fixed
		 * (scheme ports and scheme cable ports will have NO parent
		 * device).
		 */
		assert (this.endAbstractSchemePortId.equals(Identifier.VOID_IDENTIFIER)) 
				|| getEndAbstractSchemePort().getParentSchemeDevice()
				== startAbstractSchemePort.getParentSchemeDevice(): ErrorMessages.NO_COMMON_PARENT;
		final Identifier newStartAbstractSchemePortId = startAbstractSchemePort.getId();
		if (this.startAbstractSchemePortId.equals(newStartAbstractSchemePortId))
			return;
		this.startAbstractSchemePortId = newStartAbstractSchemePortId;
		this.changed = true;
	}

	/**********************************************************************
	 * Non-model methods.                                                 *
	 **********************************************************************/

	boolean hasOpticalPort() {
		final AbstractSchemePort startAbstractSchemePort = getStartAbstractSchemePort();
		if (startAbstractSchemePort instanceof SchemePort
				&& startAbstractSchemePort.getPortType().getSort().value() == PortTypeSort._PORTTYPESORT_OPTICAL)
			return true;
		
		final AbstractSchemePort endAbstractSchemePort = getEndAbstractSchemePort();
		if (endAbstractSchemePort instanceof SchemePort
				&& endAbstractSchemePort.getPortType().getSort().value() == PortTypeSort._PORTTYPESORT_OPTICAL)
			return true;
		return false;
	}
}
