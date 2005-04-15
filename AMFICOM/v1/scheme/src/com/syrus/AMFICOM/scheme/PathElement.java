/*-
 * $Id: PathElement.java,v 1.15 2005/04/15 17:47:38 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme;

import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.omg.CORBA.portable.IDLEntity;

import com.syrus.AMFICOM.configuration.corba.PortTypeSort;
import com.syrus.AMFICOM.general.AbstractCloneableStorableObject;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Describable;
import com.syrus.AMFICOM.general.ErrorMessages;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.scheme.corba.PathElement_Transferable;
import com.syrus.AMFICOM.scheme.corba.PathElement_TransferablePackage.Data;
import com.syrus.AMFICOM.scheme.corba.PathElement_TransferablePackage.DataPackage.Kind;
import com.syrus.AMFICOM.scheme.corba.PathElement_TransferablePackage.DataPackage.SchemeElementData;
import com.syrus.util.Log;

/**
 * #15 in hierarchy.
 * 
 * PathElement has no associated <code>name</code> field:
 * its {@link PathElement#getName() getName()} method actually returns
 * {@link PathElement#getAbstractSchemeElement() getAbstractSchemeElement()}<code>.</code>{@link AbstractSchemeElement#getName() getName()}.
 * 
 * @author $Author: bass $
 * @version $Revision: 1.15 $, $Date: 2005/04/15 17:47:38 $
 * @module scheme_v1
 * @todo <code>setAttributes()</code> should contain, among others,
 *       kind and sequentialNumber paremeters.
 */
public final class PathElement extends AbstractCloneableStorableObject implements Describable, Comparable {
	private static final long serialVersionUID = 3905799768986038576L;

	private Identifier parentSchemePathId;

	private int sequentialNumber;

	private Kind kind;

	/**
	 * May reference either {@link SchemePort} or {@link SchemeCablePort}.
	 * Empty if type is other than {@link Kind#SCHEME_ELEMENT}.
	 */
	private Identifier startAbstractSchemePortId;

	/**
	 * May reference either {@link SchemePort} or {@link SchemeCablePort}.
	 * Empty if type is other than {@link Kind#SCHEME_ELEMENT}.
	 */
	private Identifier endAbstractSchemePortId;

	/**
	 * Empty if type is other than
	 * {@link Kind#SCHEME_CABLE_LINK}.
	 */
	private Identifier schemeCableThreadId;

	/**
	 * Empty if type is other than {@link Kind#SCHEME_LINK}.
	 */
	private Identifier schemeLinkId;

	private PathElementDatabase pathElementDatabase;

	/**
	 * @param id
	 * @throws RetrieveObjectException
	 * @throws ObjectNotFoundException
	 */
	PathElement(final Identifier id) throws RetrieveObjectException, ObjectNotFoundException {
		super(id);
		
		this.pathElementDatabase = SchemeDatabaseContext.getPathElementDatabase();
		try {
			this.pathElementDatabase.retrieve(this);
		} catch (final IllegalDataException ide) {
			throw new RetrieveObjectException(ide.getMessage(), ide);
		}
	}

	/**
	 * Common constructor to be indirectly invoked by both clients and
	 * databases.
	 *
	 * @param id
	 * @param created
	 * @param modified
	 * @param creatorId
	 * @param modifierId
	 * @param version
	 * @param parentSchemePath
	 */
	private PathElement(final Identifier id, final Date created,
			final Date modified, final Identifier creatorId,
			final Identifier modifierId, final long version,
			final SchemePath parentSchemePath) {
		super(id, created, modified, creatorId, modifierId, version);
		this.parentSchemePathId = Identifier.possiblyVoid(parentSchemePath);
	}

	/**
	 * Client-side constructor; creates an instance of type
	 * {@link Kind#SCHEME_ELEMENT}.
	 * 
	 * @param id
	 * @param created
	 * @param modified
	 * @param creatorId
	 * @param modifierId
	 * @param version
	 * @param parentSchemePath
	 * @param startAbstractSchemePort
	 * @param endSbstractSchemePort
	 * @todo Narrow visibility to private and remove
	 *       {@link Identifier#possiblyVoid(com.syrus.AMFICOM.general.StorableObject)}
	 *       invocations (in favor of direct checks) unless this constructor
	 *       is ever called by databases.
	 */
	PathElement(final Identifier id, final Date created,
			final Date modified, final Identifier creatorId,
			final Identifier modifierId, final long version,
			final SchemePath parentSchemePath,
			final AbstractSchemePort startAbstractSchemePort,
			final AbstractSchemePort endSbstractSchemePort) {
		this(id, created, modified, creatorId, modifierId, version, parentSchemePath);
		this.sequentialNumber = parentSchemePath == null
				? -1
				: parentSchemePath.getPathElements().size();
		this.kind = Kind.SCHEME_ELEMENT;
		this.startAbstractSchemePortId = Identifier.possiblyVoid(startAbstractSchemePort);
		this.endAbstractSchemePortId = Identifier.possiblyVoid(endSbstractSchemePort);
		this.schemeCableThreadId = Identifier.VOID_IDENTIFIER;
		this.schemeLinkId = Identifier.VOID_IDENTIFIER;
	}

	/**
	 * Client-side constructor; creates an instance of type
	 * {@link Kind#SCHEME_CABLE_LINK}.
	 * 
	 * @param id
	 * @param created
	 * @param modified
	 * @param creatorId
	 * @param modifierId
	 * @param version
	 * @param parentSchemePath
	 * @param schemeCableThread
	 * @todo Narrow visibility to private and remove
	 *       {@link Identifier#possiblyVoid(com.syrus.AMFICOM.general.StorableObject)}
	 *       invocations (in favor of direct checks) unless this constructor
	 *       is ever called by databases.
	 */
	PathElement(final Identifier id, final Date created,
			final Date modified, final Identifier creatorId,
			final Identifier modifierId, final long version,
			final SchemePath parentSchemePath,
			final SchemeCableThread schemeCableThread) {
		this(id, created, modified, creatorId, modifierId, version, parentSchemePath);
		this.sequentialNumber = parentSchemePath == null
				? -1
				: parentSchemePath.getPathElements().size();
		this.kind = Kind.SCHEME_CABLE_LINK;
		this.startAbstractSchemePortId = Identifier.VOID_IDENTIFIER;
		this.endAbstractSchemePortId = Identifier.VOID_IDENTIFIER;
		this.schemeCableThreadId = Identifier.possiblyVoid(schemeCableThread);
		this.schemeLinkId = Identifier.VOID_IDENTIFIER;
	}

	/**
	 * Client-side constructor; creates an instance of type
	 * {@link Kind#SCHEME_LINK}.
	 * 
	 * @param id
	 * @param created
	 * @param modified
	 * @param creatorId
	 * @param modifierId
	 * @param version
	 * @param parentSchemePath
	 * @param schemeLink
	 * @todo Narrow visibility to private and remove
	 *       {@link Identifier#possiblyVoid(com.syrus.AMFICOM.general.StorableObject)}
	 *       invocations (in favor of direct checks) unless this constructor
	 *       is ever called by databases.
	 */
	PathElement(final Identifier id, final Date created,
			final Date modified, final Identifier creatorId,
			final Identifier modifierId, final long version,
			final SchemePath parentSchemePath,
			final SchemeLink schemeLink) {
		this(id, created, modified, creatorId, modifierId, version, parentSchemePath);
		this.sequentialNumber = parentSchemePath == null
				? -1
				: parentSchemePath.getPathElements().size();
		this.kind = Kind.SCHEME_LINK;
		this.startAbstractSchemePortId = Identifier.VOID_IDENTIFIER;
		this.endAbstractSchemePortId = Identifier.VOID_IDENTIFIER;
		this.schemeCableThreadId = Identifier.VOID_IDENTIFIER;
		this.schemeLinkId = Identifier.possiblyVoid(schemeLink);
	}

	/**
	 * Constructor to be invoked by databases only (if applicable).
	 *
	 * @param id
	 * @param created
	 * @param modified
	 * @param creatorId
	 * @param modifierId
	 * @param version
	 * @param parentSchemePath
	 * @param sequentialNumber
	 * @param kind
	 * @param startAbstractSchemePort
	 * @param endAbstractSchemePort
	 * @param schemeCableThread
	 * @param schemeLink
	 */
	PathElement(final Identifier id, final Date created,
			final Date modified, final Identifier creatorId,
			final Identifier modifierId, final long version,
			final SchemePath parentSchemePath,
			final int sequentialNumber,
			final Kind kind,
			final AbstractSchemePort startAbstractSchemePort,
			final AbstractSchemePort endAbstractSchemePort,
			final SchemeCableThread schemeCableThread,
			final SchemeLink schemeLink) {
		this(id, created, modified, creatorId, modifierId, version, parentSchemePath);
		this.sequentialNumber = sequentialNumber;
		this.kind = kind;
		this.startAbstractSchemePortId = Identifier.possiblyVoid(startAbstractSchemePort);
		this.endAbstractSchemePortId = Identifier.possiblyVoid(endAbstractSchemePort);
		this.schemeCableThreadId = Identifier.possiblyVoid(schemeCableThread);
		this.schemeLinkId = Identifier.possiblyVoid(schemeLink);
	}

	/**
	 * @param transferable
	 */
	PathElement(final PathElement_Transferable transferable) {
		this.pathElementDatabase = SchemeDatabaseContext.getPathElementDatabase();
		fromTransferable(transferable);
	}

	/**
	 * @param creatorId
	 * @param parentSchemePath
	 * @param startAbstractSchemePort
	 * @param endAbstractSchemePort
	 * @return a newly-created instance of type
	 *         {@link Kind#SCHEME_ELEMENT}.
	 * @throws CreateObjectException
	 */
	public static PathElement createInstance(final Identifier creatorId,
			final SchemePath parentSchemePath,
			final AbstractSchemePort startAbstractSchemePort,
			final AbstractSchemePort endAbstractSchemePort)
			throws CreateObjectException {
		assert creatorId != null && !creatorId.isVoid(): ErrorMessages.NON_VOID_EXPECTED;
		assert parentSchemePath != null
				&& startAbstractSchemePort != null
				&& endAbstractSchemePort != null: ErrorMessages.NON_NULL_EXPECTED;
		try {
			final Date created = new Date();
			final PathElement pathElement = new PathElement(
					IdentifierPool
							.getGeneratedIdentifier(ObjectEntities.PATH_ELEMENT_ENTITY_CODE),
					created, created, creatorId, creatorId,
					0L, parentSchemePath,
					startAbstractSchemePort,
					endAbstractSchemePort);
			pathElement.changed = true;
			return pathElement;
		} catch (final IllegalObjectEntityException ioee) {
			throw new CreateObjectException(
					"PathElement.createInstance() | cannot generate identifier ", ioee); //$NON-NLS-1$
		}
	}

	/**
	 * @param creatorId
	 * @param parentSchemePath
	 * @param schemeCableThread
	 * @return a newly-created instance of type
	 *         {@link Kind#SCHEME_CABLE_LINK}.
	 * @throws CreateObjectException
	 */
	public static PathElement createInstance(final Identifier creatorId,
			final SchemePath parentSchemePath,
			final SchemeCableThread schemeCableThread)
			throws CreateObjectException {
		assert creatorId != null && !creatorId.isVoid(): ErrorMessages.NON_VOID_EXPECTED;
		assert parentSchemePath != null && schemeCableThread != null: ErrorMessages.NON_NULL_EXPECTED;
		try {
			final Date created = new Date();
			final PathElement pathElement = new PathElement(
					IdentifierPool
							.getGeneratedIdentifier(ObjectEntities.PATH_ELEMENT_ENTITY_CODE),
					created, created, creatorId, creatorId,
					0L, parentSchemePath, schemeCableThread);
			pathElement.changed = true;
			return pathElement;
		} catch (final IllegalObjectEntityException ioee) {
			throw new CreateObjectException(
					"PathElement.createInstance() | cannot generate identifier ", ioee); //$NON-NLS-1$
		}
	}

	/**
	 * @param creatorId
	 * @param parentSchemePath
	 * @param schemeLink
	 * @return a newly-created instance of type
	 *         {@link Kind#SCHEME_LINK}.
	 * @throws CreateObjectException
	 */
	public static PathElement createInstance(final Identifier creatorId,
			final SchemePath parentSchemePath,
			final SchemeLink schemeLink)
			throws CreateObjectException {
		assert creatorId != null && !creatorId.isVoid(): ErrorMessages.NON_VOID_EXPECTED;
		assert parentSchemePath != null && schemeLink != null: ErrorMessages.NON_NULL_EXPECTED;
		try {
			final Date created = new Date();
			final PathElement pathElement = new PathElement(
					IdentifierPool
							.getGeneratedIdentifier(ObjectEntities.PATH_ELEMENT_ENTITY_CODE),
					created, created, creatorId, creatorId,
					0L, parentSchemePath, schemeLink);
			pathElement.changed = true;
			return pathElement;
		} catch (final IllegalObjectEntityException ioee) {
			throw new CreateObjectException(
					"PathElement.createInstance() | cannot generate identifier ", ioee); //$NON-NLS-1$
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
		switch (this.kind.value()) {
			case Kind._SCHEME_CABLE_LINK:
				return getSchemeCableLink();
			case Kind._SCHEME_ELEMENT:
				return getSchemeElement();
			case Kind._SCHEME_LINK:
				return getSchemeCableLink();
			default:
				throw new UnsupportedOperationException(ErrorMessages.OBJECT_STATE_ILLEGAL);
		}
	}

	/**
	 * @see com.syrus.AMFICOM.general.StorableObject#getDependencies()
	 */
	public Set getDependencies() {
		assert this.parentSchemePathId != null && !this.parentSchemePathId.isVoid()
				&& this.sequentialNumber != -1
				&& this.kind != null
				&& this.startAbstractSchemePortId != null
				&& this.endAbstractSchemePortId != null
				&& this.schemeCableThreadId != null
				&& this.schemeLinkId != null : ErrorMessages.OBJECT_NOT_INITIALIZED;
		switch (this.kind.value()) {
			case Kind._SCHEME_ELEMENT:
				assert !this.startAbstractSchemePortId.isVoid()
						&& !this.endAbstractSchemePortId.isVoid()
						&& this.schemeCableThreadId.isVoid()
						&& this.schemeLinkId.isVoid() : ErrorMessages.OBJECT_BADLY_INITIALIZED; 
				break;
			case Kind._SCHEME_CABLE_LINK:
				assert this.startAbstractSchemePortId.isVoid()
						&& this.endAbstractSchemePortId.isVoid()
						&& !this.schemeCableThreadId.isVoid()
						&& this.schemeLinkId.isVoid(): ErrorMessages.OBJECT_BADLY_INITIALIZED; 
				break;
			case Kind._SCHEME_LINK:
				assert this.startAbstractSchemePortId.isVoid()
						&& this.endAbstractSchemePortId.isVoid()
						&& this.schemeCableThreadId.isVoid()
						&& !this.schemeLinkId.isVoid(): ErrorMessages.OBJECT_BADLY_INITIALIZED; 
				break;
			default:
				assert false;
		}
		final Set dependencies = new HashSet();
		dependencies.add(this.parentSchemePathId);
		dependencies.add(this.startAbstractSchemePortId);
		dependencies.add(this.endAbstractSchemePortId);
		dependencies.add(this.schemeCableThreadId);
		dependencies.add(this.schemeLinkId);
		dependencies.remove(null);
		dependencies.remove(Identifier.VOID_IDENTIFIER);
		return Collections.unmodifiableSet(dependencies);
	}

	/**
	 * @see Describable#getDescription()
	 */
	public String getDescription() {
		return getAbstractSchemeElement().getDescription();
	}

	public AbstractSchemePort getEndAbstractSchemePort() {
		if (this.kind.value() != Kind._SCHEME_ELEMENT)
			throw new UnsupportedOperationException(ErrorMessages.OBJECT_STATE_ILLEGAL);
		assert this.endAbstractSchemePortId != null: ErrorMessages.OBJECT_NOT_INITIALIZED;
		assert !this.endAbstractSchemePortId.isVoid(): ErrorMessages.OBJECT_BADLY_INITIALIZED;
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
	 * @see com.syrus.AMFICOM.general.Namable#getName()
	 */
	public String getName() {
		return getAbstractSchemeElement().getName();
	}

	public Scheme getParentScheme() {
		return getAbstractSchemeElement().getParentScheme();
	}

	public SchemePath getParentSchemePath() {
		assert this.parentSchemePathId != null && !this.parentSchemePathId.isVoid(): ErrorMessages.OBJECT_NOT_INITIALIZED;
		try {
			return (SchemePath) SchemeStorableObjectPool.getStorableObject(this.parentSchemePathId, true);
		} catch (final ApplicationException ae) {
			Log.debugException(ae, Log.SEVERE);
			return null;
		}
	}

	public Kind getKind() {
		return this.kind;
	}

	public SchemeCableLink getSchemeCableLink() {
		return getSchemeCableThread().getParentSchemeCableLink();
	}

	public SchemeCableThread getSchemeCableThread() {
		if (this.kind.value() != Kind._SCHEME_CABLE_LINK)
			throw new UnsupportedOperationException(ErrorMessages.OBJECT_STATE_ILLEGAL);
		assert this.schemeCableThreadId != null: ErrorMessages.OBJECT_NOT_INITIALIZED;
		assert !this.schemeCableThreadId.isVoid(): ErrorMessages.OBJECT_BADLY_INITIALIZED;
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
		if (this.kind.value() != Kind._SCHEME_LINK)
			throw new UnsupportedOperationException(ErrorMessages.OBJECT_STATE_ILLEGAL);
		assert this.schemeLinkId != null: ErrorMessages.OBJECT_NOT_INITIALIZED;
		assert !this.schemeLinkId.isVoid(): ErrorMessages.OBJECT_BADLY_INITIALIZED;
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

	/**
	 * @return this <code>PathElement</code>&apos;s sequential number
	 *         (starting with 0), or -1 if this <code>PathElement</code>
	 *         is not yet bound to any <code>SchemePath</code> (which
	 *         seems impossible).
	 */
	public int getSequentialNumber() {
		assert getParentSchemePath().assertContains(this)
				|| (this.parentSchemePathId.isVoid() && this.sequentialNumber == -1);
		return this.sequentialNumber;
	}

	public AbstractSchemePort getStartAbstractSchemePort() {
		if (this.kind.value() != Kind._SCHEME_ELEMENT)
			throw new UnsupportedOperationException(ErrorMessages.OBJECT_STATE_ILLEGAL);
		assert this.startAbstractSchemePortId != null: ErrorMessages.OBJECT_NOT_INITIALIZED;
		assert !this.startAbstractSchemePortId.isVoid(): ErrorMessages.OBJECT_BADLY_INITIALIZED;
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
	 * @see com.syrus.AMFICOM.general.TransferableObject#getTransferable()
	 */
	public IDLEntity getTransferable() {
		final Data data = new Data();
		switch (this.kind.value()) {
			case Kind._SCHEME_ELEMENT:
				data.schemeElementData(this.kind, new SchemeElementData(
						(Identifier_Transferable) this.startAbstractSchemePortId.getTransferable(),
						(Identifier_Transferable) this.endAbstractSchemePortId.getTransferable()));
				break;
			case Kind._SCHEME_CABLE_LINK:
				data.schemeCableThreadId(this.kind, (Identifier_Transferable) this.schemeCableThreadId.getTransferable());
				break;
			case Kind._SCHEME_LINK:
				data.schemeLinkId(this.kind, (Identifier_Transferable) this.schemeLinkId.getTransferable());
				break;
			default:
				assert false;
		}
		return new PathElement_Transferable(getHeaderTransferable(),
				(Identifier_Transferable) this.parentSchemePathId.getTransferable(),
				this.sequentialNumber, data);
	}

	public void setAbstractSchemeElement(final AbstractSchemeElement abstractSchemeElement) {
		switch (this.kind.value()) {
			case Kind._SCHEME_CABLE_LINK:
				setSchemeCableLink((SchemeCableLink) abstractSchemeElement);
			case Kind._SCHEME_ELEMENT:
				setSchemeElement((SchemeElement) abstractSchemeElement);
			case Kind._SCHEME_LINK:
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
		if (this.kind.value() != Kind._SCHEME_ELEMENT)
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
		assert (this.startAbstractSchemePortId.isVoid()) 
				|| getStartAbstractSchemePort().getParentSchemeDevice()
				== endAbstractSchemePort.getParentSchemeDevice(): ErrorMessages.NO_COMMON_PARENT;
		final Identifier newEndAbstractSchemePortId = endAbstractSchemePort.getId();
		if (this.endAbstractSchemePortId.equals(newEndAbstractSchemePortId))
			return;
		this.endAbstractSchemePortId = newEndAbstractSchemePortId;
		this.changed = true;
	}

	/**
	 * @see com.syrus.AMFICOM.general.Namable#setName(String)
	 */
	public void setName(final String name) {
		getAbstractSchemeElement().setName(name);
	}

	public void setParentScheme(final Scheme parentScheme) {
		getAbstractSchemeElement().setParentScheme(parentScheme);
	}

	/**
	 * If the old <code>SchemePath</code> is non-null; removes itself and
	 * all subsequent <code>PathElement</code>s from the old
	 * <code>SchemePath</code>. If the new <code>SchemePath</code> is
	 * non-null, adds itself to the end of the new <code>SchemePath</code>,
	 * accordingly adjusting own sequential number, otherwise deletes itself
	 * from the pool.
	 *
	 * @param parentSchemePath
	 */
	public void setParentSchemePath(final SchemePath parentSchemePath) {
		throw new UnsupportedOperationException();
	}

	public void setSchemeCableLink(final SchemeCableLink schemeCableLink) {
		getSchemeCableThread().setParentSchemeCableLink(schemeCableLink);
	}

	public void setSchemeCableThread(final SchemeCableThread schemeCableThread) {
		if (this.kind.value() != Kind._SCHEME_CABLE_LINK)
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
		if (this.kind.value() != Kind._SCHEME_LINK)
			throw new UnsupportedOperationException(ErrorMessages.OBJECT_STATE_ILLEGAL);
		assert schemeLink != null: ErrorMessages.NON_NULL_EXPECTED;
		final Identifier newSchemeLinkId = schemeLink.getId();
		if (this.schemeLinkId.equals(newSchemeLinkId))
			return;
		this.schemeLinkId = newSchemeLinkId;
		this.changed = true;
	}

	public void setStartAbstractSchemePort(final AbstractSchemePort startAbstractSchemePort) {
		if (this.kind.value() != Kind._SCHEME_ELEMENT)
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
		assert (this.endAbstractSchemePortId.isVoid()) 
				|| getEndAbstractSchemePort().getParentSchemeDevice()
				== startAbstractSchemePort.getParentSchemeDevice(): ErrorMessages.NO_COMMON_PARENT;
		final Identifier newStartAbstractSchemePortId = startAbstractSchemePort.getId();
		if (this.startAbstractSchemePortId.equals(newStartAbstractSchemePortId))
			return;
		this.startAbstractSchemePortId = newStartAbstractSchemePortId;
		this.changed = true;
	}

	/**
	 * @param transferable
	 * @see com.syrus.AMFICOM.general.StorableObject#fromTransferable(IDLEntity)
	 */
	protected void fromTransferable(final IDLEntity transferable) {
		final PathElement_Transferable pathElement = (PathElement_Transferable) transferable;
		try {
			super.fromTransferable(pathElement.header);
		} catch (final ApplicationException ae) {
			/*
			 * Never. Arseniy, don't add any error-handling code,
			 * please.
			 */
		}
		this.parentSchemePathId = new Identifier(pathElement.parentSchemePathId);
		this.sequentialNumber = pathElement.sequentialNumber;
		final Data data = pathElement.data;
		this.kind = data.discriminator();
		switch (this.kind.value()) {
			case Kind._SCHEME_ELEMENT:
				final SchemeElementData schemeElementData = data.schemeElementData();
				this.startAbstractSchemePortId = new Identifier(schemeElementData.startAbstractSchemePortId);
				this.endAbstractSchemePortId = new Identifier(schemeElementData.endAbstractSchemePortId);
				this.schemeCableThreadId = Identifier.VOID_IDENTIFIER;
				this.schemeLinkId = Identifier.VOID_IDENTIFIER;
				break;
			case Kind._SCHEME_CABLE_LINK:
				this.startAbstractSchemePortId = Identifier.VOID_IDENTIFIER;
				this.endAbstractSchemePortId = Identifier.VOID_IDENTIFIER;
				this.schemeCableThreadId = new Identifier(data.schemeCableThreadId());
				this.schemeLinkId = Identifier.VOID_IDENTIFIER;
				break;
			case Kind._SCHEME_LINK:
				this.startAbstractSchemePortId = Identifier.VOID_IDENTIFIER;
				this.endAbstractSchemePortId = Identifier.VOID_IDENTIFIER;
				this.schemeCableThreadId = Identifier.VOID_IDENTIFIER;
				this.schemeLinkId = new Identifier(data.schemeLinkId());
				break;
			default:
				assert false;
		}
	}

	/*-********************************************************************
	 * Non-model members.                                                 *
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
