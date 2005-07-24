/*-
 * $Id: PathElement.java,v 1.52 2005/07/24 16:58:44 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme;

import static com.syrus.AMFICOM.configuration.corba.IdlPortTypePackage.PortTypeSort.PORTTYPESORT_OPTICAL;
import static com.syrus.AMFICOM.general.ErrorMessages.EXACTLY_ONE_PARENT_REQUIRED;
import static com.syrus.AMFICOM.general.ErrorMessages.NON_NULL_EXPECTED;
import static com.syrus.AMFICOM.general.ErrorMessages.NON_VOID_EXPECTED;
import static com.syrus.AMFICOM.general.ErrorMessages.NO_COMMON_PARENT;
import static com.syrus.AMFICOM.general.ErrorMessages.OBJECT_BADLY_INITIALIZED;
import static com.syrus.AMFICOM.general.ErrorMessages.OBJECT_NOT_INITIALIZED;
import static com.syrus.AMFICOM.general.ErrorMessages.OBJECT_STATE_ILLEGAL;
import static com.syrus.AMFICOM.general.Identifier.VOID_IDENTIFIER;
import static com.syrus.AMFICOM.general.ObjectEntities.PATHELEMENT_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SCHEMECABLEPORT_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SCHEMECABLETHREAD_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SCHEMELINK_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SCHEMEPATH_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SCHEMEPORT_CODE;
import static com.syrus.AMFICOM.scheme.corba.IdlPathElementPackage.IdlDataPackage.IdlKind.SCHEME_CABLE_LINK;
import static com.syrus.AMFICOM.scheme.corba.IdlPathElementPackage.IdlDataPackage.IdlKind.SCHEME_ELEMENT;
import static com.syrus.AMFICOM.scheme.corba.IdlPathElementPackage.IdlDataPackage.IdlKind.SCHEME_LINK;
import static com.syrus.AMFICOM.scheme.corba.IdlPathElementPackage.IdlDataPackage.IdlKind._SCHEME_CABLE_LINK;
import static com.syrus.AMFICOM.scheme.corba.IdlPathElementPackage.IdlDataPackage.IdlKind._SCHEME_ELEMENT;
import static com.syrus.AMFICOM.scheme.corba.IdlPathElementPackage.IdlDataPackage.IdlKind._SCHEME_LINK;
import static java.util.logging.Level.SEVERE;

import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.DatabaseContext;
import com.syrus.AMFICOM.general.Describable;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.corba.IdlStorableObject;
import com.syrus.AMFICOM.scheme.corba.IdlPathElement;
import com.syrus.AMFICOM.scheme.corba.IdlPathElementHelper;
import com.syrus.AMFICOM.scheme.corba.IdlPathElementPackage.IdlData;
import com.syrus.AMFICOM.scheme.corba.IdlPathElementPackage.IdlDataPackage.IdlKind;
import com.syrus.AMFICOM.scheme.corba.IdlPathElementPackage.IdlDataPackage.IdlSchemeElementData;
import com.syrus.util.Log;

/**
 * #15 in hierarchy.
 *
 * PathElement has no associated <code>name</code> field:
 * its {@link PathElement#getName() getName()} method actually returns
 * {@link PathElement#getAbstractSchemeElement() getAbstractSchemeElement()}<code>.</code>{@link AbstractSchemeElement#getName() getName()}.
 *
 * @author $Author: arseniy $
 * @version $Revision: 1.52 $, $Date: 2005/07/24 16:58:44 $
 * @module scheme_v1
 * @todo <code>setAttributes()</code> should contain, among others,
 *       kind and sequentialNumber paremeters.
 * @todo If Scheme(Cable|)Port ever happens to belong to more than one
 *       SchemeElement
 */
public final class PathElement extends StorableObject
		implements Describable, Cloneable, Comparable<PathElement>, PathMember<SchemePath, PathElement> {
	private static final long serialVersionUID = 3905799768986038576L;

	Identifier parentSchemePathId;

	int sequentialNumber;

	private IdlKind kind;

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

	/**
	 * @param id
	 * @throws RetrieveObjectException
	 * @throws ObjectNotFoundException
	 */
	PathElement(final Identifier id) throws RetrieveObjectException, ObjectNotFoundException {
		super(id);
		
		try {
			DatabaseContext.getDatabase(PATHELEMENT_CODE).retrieve(this);
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
		/*
		 * Here and below: this will work since current object is not
		 * yet put to the pool.
		 */
		this.sequentialNumber = parentSchemePath == null
				? -1
				: parentSchemePath.getPathMembers().size();
		this.kind = SCHEME_ELEMENT;
		this.startAbstractSchemePortId = Identifier.possiblyVoid(startAbstractSchemePort);
		this.endAbstractSchemePortId = Identifier.possiblyVoid(endSbstractSchemePort);
		this.schemeCableThreadId = VOID_IDENTIFIER;
		this.schemeLinkId = VOID_IDENTIFIER;
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
				: parentSchemePath.getPathMembers().size();
		this.kind = SCHEME_CABLE_LINK;
		this.startAbstractSchemePortId = VOID_IDENTIFIER;
		this.endAbstractSchemePortId = VOID_IDENTIFIER;
		this.schemeCableThreadId = Identifier.possiblyVoid(schemeCableThread);
		this.schemeLinkId = VOID_IDENTIFIER;
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
				: parentSchemePath.getPathMembers().size();
		this.kind = SCHEME_LINK;
		this.startAbstractSchemePortId = VOID_IDENTIFIER;
		this.endAbstractSchemePortId = VOID_IDENTIFIER;
		this.schemeCableThreadId = VOID_IDENTIFIER;
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
			final IdlKind kind,
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
	public PathElement(final IdlPathElement transferable) {
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
		assert creatorId != null && !creatorId.isVoid(): NON_VOID_EXPECTED;
		
		/*
		 * Starting AbstractSchemePort may be null IF AND ONLY IF parent
		 * SchemePath contains no other PathElements, i. e. this one is
		 * also the first one.
		 *
		 * Ending AbstractSchemePort may be null, as noone cares.
		 */
		assert parentSchemePath != null
				&& (parentSchemePath.getPathMembers().isEmpty()
						== (startAbstractSchemePort == null)): NON_NULL_EXPECTED;
		try {
			final Date created = new Date();
			final PathElement pathElement = new PathElement(
					IdentifierPool.getGeneratedIdentifier(PATHELEMENT_CODE),
					created, created, creatorId, creatorId,
					0L, parentSchemePath,
					startAbstractSchemePort,
					endAbstractSchemePort);
			pathElement.markAsChanged();
			return pathElement;
		} catch (final IdentifierGenerationException ige) {
			throw new CreateObjectException(
					"PathElement.createInstance() | cannot generate identifier ", ige);
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
		assert creatorId != null && !creatorId.isVoid(): NON_VOID_EXPECTED;
		assert parentSchemePath != null && schemeCableThread != null: NON_NULL_EXPECTED;
		try {
			final Date created = new Date();
			final PathElement pathElement = new PathElement(
					IdentifierPool.getGeneratedIdentifier(PATHELEMENT_CODE),
					created, created, creatorId, creatorId,
					0L, parentSchemePath, schemeCableThread);
			pathElement.markAsChanged();
			return pathElement;
		} catch (final IdentifierGenerationException ige) {
			throw new CreateObjectException(
					"PathElement.createInstance() | cannot generate identifier ", ige);
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
		assert creatorId != null && !creatorId.isVoid(): NON_VOID_EXPECTED;
		assert parentSchemePath != null && schemeLink != null: NON_NULL_EXPECTED;
		try {
			final Date created = new Date();
			final PathElement pathElement = new PathElement(
					IdentifierPool.getGeneratedIdentifier(PATHELEMENT_CODE),
					created, created, creatorId, creatorId,
					0L, parentSchemePath, schemeLink);
			pathElement.markAsChanged();
			return pathElement;
		} catch (final IdentifierGenerationException ige) {
			throw new CreateObjectException(
					"PathElement.createInstance() | cannot generate identifier ", ige);
		}
	}

	@Override
	public PathElement clone() throws CloneNotSupportedException {
		final PathElement pathElement = (PathElement) super.clone();
		/**
		 * @todo Update the newly created object.
		 */
		return pathElement;
	}

	/**
	 * @param that
	 */
	public int compareTo(final PathElement that) {
		assert this.parentSchemePathId.equals(that.parentSchemePathId): NO_COMMON_PARENT;
		return this.sequentialNumber <= that.sequentialNumber ? this.sequentialNumber < that.sequentialNumber ? -1 : 0 : 1;
	}

	public AbstractSchemeElement getAbstractSchemeElement() {
		switch (this.kind.value()) {
			case _SCHEME_CABLE_LINK:
				return this.getSchemeCableLink();
			case _SCHEME_ELEMENT:
				return this.getSchemeElement();
			case _SCHEME_LINK:
				return this.getSchemeLink();
			default:
				throw new UnsupportedOperationException(OBJECT_STATE_ILLEGAL);
		}
	}

	/**
	 * @see com.syrus.AMFICOM.general.StorableObject#getDependencies()
	 */
	@Override
	public Set<Identifiable> getDependencies() {
		assert this.parentSchemePathId != null && !this.parentSchemePathId.isVoid()
				&& this.sequentialNumber != -1
				&& this.kind != null
				&& this.startAbstractSchemePortId != null
				&& this.endAbstractSchemePortId != null
				&& this.schemeCableThreadId != null
				&& this.schemeLinkId != null : OBJECT_NOT_INITIALIZED;
		switch (this.kind.value()) {
			case _SCHEME_ELEMENT:
				assert (isFirst() || !this.startAbstractSchemePortId.isVoid())
						&& (isLast() || !this.endAbstractSchemePortId.isVoid())
						&& this.schemeCableThreadId.isVoid()
						&& this.schemeLinkId.isVoid(): OBJECT_BADLY_INITIALIZED;
				break;
			case _SCHEME_CABLE_LINK:
				assert this.startAbstractSchemePortId.isVoid()
						&& this.endAbstractSchemePortId.isVoid()
						&& !this.schemeCableThreadId.isVoid()
						&& this.schemeLinkId.isVoid(): OBJECT_BADLY_INITIALIZED;
				break;
			case _SCHEME_LINK:
				assert this.startAbstractSchemePortId.isVoid()
						&& this.endAbstractSchemePortId.isVoid()
						&& this.schemeCableThreadId.isVoid()
						&& !this.schemeLinkId.isVoid(): OBJECT_BADLY_INITIALIZED;
				break;
			default:
				assert false;
		}
		final Set<Identifiable> dependencies = new HashSet<Identifiable>();
		dependencies.add(this.parentSchemePathId);
		dependencies.add(this.startAbstractSchemePortId);
		dependencies.add(this.endAbstractSchemePortId);
		dependencies.add(this.schemeCableThreadId);
		dependencies.add(this.schemeLinkId);
		dependencies.remove(null);
		dependencies.remove(VOID_IDENTIFIER);
		return Collections.unmodifiableSet(dependencies);
	}

	/**
	 * @see Describable#getDescription()
	 */
	public String getDescription() {
		return getAbstractSchemeElement().getDescription();
	}

	Identifier getEndAbstractSchemePortId() {
		assert this.endAbstractSchemePortId != null: OBJECT_NOT_INITIALIZED;

		final boolean endAbstractSchemePortIdVoid = this.endAbstractSchemePortId.isVoid();
		final short endAbstractSchemePortIdMajor = this.endAbstractSchemePortId.getMajor();

		assert endAbstractSchemePortIdVoid
				|| endAbstractSchemePortIdMajor == SCHEMEPORT_CODE
				|| endAbstractSchemePortIdMajor == SCHEMECABLEPORT_CODE;
		if (this.kind == SCHEME_ELEMENT) {
			assert isLast() || !endAbstractSchemePortIdVoid : OBJECT_BADLY_INITIALIZED;
		} else {
			assert endAbstractSchemePortIdVoid;
		}
		return this.endAbstractSchemePortId;
	}

	/**
	 * A wrapper around {@link #getEndAbstractSchemePortId()}.
	 */
	public AbstractSchemePort getEndAbstractSchemePort() {
		if (this.kind != SCHEME_ELEMENT)
			throw new UnsupportedOperationException(OBJECT_STATE_ILLEGAL);

		try {
			return (AbstractSchemePort) StorableObjectPool.getStorableObject(this.getEndAbstractSchemePortId(), true);
		} catch (final ApplicationException ae) {
			Log.debugException(ae, SEVERE);
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

	Identifier getParentSchemePathId() {
		assert this.parentSchemePathId != null && !this.parentSchemePathId.isVoid(): OBJECT_NOT_INITIALIZED;
		assert this.parentSchemePathId.getMajor() == SCHEMEPATH_CODE;
		return this.parentSchemePathId;
	}

	/**
	 * A wrapper around {@link #getParentSchemePathId()}.
	 *
	 * @see #getParentSchemePathId()
	 * @see PathMember#getParentPathOwner()
	 */
	public SchemePath getParentPathOwner() {
		try {
			return (SchemePath) StorableObjectPool.getStorableObject(this.getParentSchemePathId(), true);
		} catch (final ApplicationException ae) {
			Log.debugException(ae, SEVERE);
			return null;
		}
	}

	public IdlKind getKind() {
		assert this.kind != null: OBJECT_NOT_INITIALIZED;
		return this.kind;
	}

	public SchemeCableLink getSchemeCableLink() {
		return getSchemeCableThread().getParentSchemeCableLink();
	}

	Identifier getSchemeCableThreadId() {
		assert this.schemeCableThreadId != null: OBJECT_NOT_INITIALIZED;
		assert this.schemeCableThreadId.isVoid() ^ this.kind == SCHEME_CABLE_LINK;
		assert this.schemeCableThreadId.isVoid() ^ this.schemeCableThreadId.getMajor() == SCHEMECABLETHREAD_CODE;
		return this.schemeCableThreadId;
	}

	/**
	 * A wrapper around {@link #getSchemeCableThreadId()}.
	 */
	public SchemeCableThread getSchemeCableThread() {
		if (this.kind != SCHEME_CABLE_LINK)
			throw new UnsupportedOperationException(OBJECT_STATE_ILLEGAL);

		try {
			return (SchemeCableThread) StorableObjectPool.getStorableObject(this.getSchemeCableThreadId(), true);
		} catch (final ApplicationException ae) {
			Log.debugException(ae, SEVERE);
			return null;
		}
	}

	public SchemeElement getSchemeElement() {
		final AbstractSchemePort startAbstractSchemePort = this.getStartAbstractSchemePort();
		final AbstractSchemePort endAbstractSchemePort = this.getEndAbstractSchemePort();
		SchemeDevice parentSchemeDevice;
		if (startAbstractSchemePort != null) {
			parentSchemeDevice = startAbstractSchemePort.getParentSchemeDevice();
			assert endAbstractSchemePort == null || endAbstractSchemePort.getParentSchemeDevice().getId().equals(parentSchemeDevice.getId()) : NO_COMMON_PARENT;
		} else if (endAbstractSchemePort != null) {
			parentSchemeDevice = endAbstractSchemePort.getParentSchemeDevice();
		} else {
			Log.debugMessage("PathElement.getSchemeElement() | Both (abstract) scheme ports of this path element are null. Seems strange, unless it's the only element of its parent path. Returning null as well.",
					SEVERE);
			parentSchemeDevice = null;
		}
		return parentSchemeDevice == null ? null : parentSchemeDevice.getParentSchemeElement();
	}

	Identifier getSchemeLinkId() {
		assert this.schemeLinkId != null : OBJECT_NOT_INITIALIZED;
		assert this.schemeLinkId.isVoid() ^ this.kind == SCHEME_LINK;
		assert this.schemeLinkId.isVoid() ^ this.schemeLinkId.getMajor() == SCHEMELINK_CODE;
		return this.schemeLinkId;
	}

	/**
	 * A wrapper around {@link #getSchemeLinkId()}.
	 */
	public SchemeLink getSchemeLink() {
		if (this.kind != SCHEME_LINK)
			throw new UnsupportedOperationException(OBJECT_STATE_ILLEGAL);

		try {
			return (SchemeLink) StorableObjectPool.getStorableObject(this.getSchemeLinkId(), true);
		} catch (final ApplicationException ae) {
			Log.debugException(ae, SEVERE);
			return null;
		}
	}

	/**
	 * @return this <code>PathElement</code>&apos;s sequential number
	 *         (starting with 0).
	 * @see PathMember#getSequentialNumber()
	 */
	public int getSequentialNumber() {
		assert this.getParentPathOwner().assertContains(this);
		return this.sequentialNumber;
	}

	Identifier getStartAbstractSchemePortId() {
		assert this.startAbstractSchemePortId != null : OBJECT_NOT_INITIALIZED;
		
		final boolean startAbstractSchemePortIdVoid = this.startAbstractSchemePortId.isVoid();
		final short startAbstractSchemePortIdMajor = this.startAbstractSchemePortId.getMajor();

		assert startAbstractSchemePortIdVoid
				|| startAbstractSchemePortIdMajor == SCHEMEPORT_CODE
				|| startAbstractSchemePortIdMajor == SCHEMECABLEPORT_CODE;
		/*
		 * If this is a "SCHEME_ELEMENT" PathElement, then do a full
		 * check (starting AbstractSchemePort is permitted to (but not
		 * necessarilly should) be null only if this PathElement is a
		 * first one in the SchemePath that contains it.
		 * 
		 * Otherwise, require the unused field to be null.
		 */
		if (this.kind == SCHEME_ELEMENT) {
			assert isFirst() || !startAbstractSchemePortIdVoid : OBJECT_BADLY_INITIALIZED;
		} else {
			assert startAbstractSchemePortIdVoid;
		}
		return this.startAbstractSchemePortId;
	}

	/**
	 * A wrapper around {@link #getStartAbstractSchemePortId()}.
	 */
	public AbstractSchemePort getStartAbstractSchemePort() {
		if (this.kind != SCHEME_ELEMENT)
			throw new UnsupportedOperationException(OBJECT_STATE_ILLEGAL);

		try {
			return (AbstractSchemePort) StorableObjectPool.getStorableObject(this.getStartAbstractSchemePortId(), true);
		} catch (final ApplicationException ae) {
			Log.debugException(ae, SEVERE);
			return null;
		}
	}

	/**
	 * @param orb
	 * @see com.syrus.AMFICOM.general.TransferableObject#getTransferable(org.omg.CORBA.ORB)
	 */
	@Override
	public IdlPathElement getTransferable(final ORB orb) {
		final IdlData data = new IdlData();
		switch (this.kind.value()) {
			case _SCHEME_ELEMENT:
				data.schemeElementData(this.kind, new IdlSchemeElementData(
						this.startAbstractSchemePortId.getTransferable(),
						this.endAbstractSchemePortId.getTransferable()));
				break;
			case _SCHEME_CABLE_LINK:
				data.schemeCableThreadId(this.kind, this.schemeCableThreadId.getTransferable());
				break;
			case _SCHEME_LINK:
				data.schemeLinkId(this.kind, this.schemeLinkId.getTransferable());
				break;
			default:
				assert false;
		}
		return IdlPathElementHelper.init(orb,
				this.id.getTransferable(),
				this.created.getTime(),
				this.modified.getTime(),
				this.creatorId.getTransferable(),
				this.modifierId.getTransferable(),
				this.version,
				this.parentSchemePathId.getTransferable(),
				this.sequentialNumber, data);
	}

	public void setAbstractSchemeElement(final AbstractSchemeElement abstractSchemeElement) {
		switch (this.kind.value()) {
			case _SCHEME_CABLE_LINK:
				setSchemeCableLink((SchemeCableLink) abstractSchemeElement);
				break;
			case _SCHEME_ELEMENT:
				setSchemeElement((SchemeElement) abstractSchemeElement);
				break;
			case _SCHEME_LINK:
				setSchemeLink((SchemeLink) abstractSchemeElement);
				break;
			default:
				throw new UnsupportedOperationException(OBJECT_STATE_ILLEGAL);
		}
	}

	/**
	 * @param created
	 * @param modified
	 * @param creatorId
	 * @param modifierId
	 * @param version
	 * @param parentSchemePathId
	 * @param sequentialNumber
	 * @param kind
	 * @param startAbstractSchemePortId
	 * @param endAbstractSchemePortId
	 * @param schemeCableThreadId
	 * @param schemeLinkId
	 */
	synchronized void setAttributes(final Date created, final Date modified,
			final Identifier creatorId,
			final Identifier modifierId, final long version,
			final Identifier parentSchemePathId,
			final int sequentialNumber, final IdlKind kind,
			final Identifier startAbstractSchemePortId,
			final Identifier endAbstractSchemePortId,
			final Identifier schemeCableThreadId,
			final Identifier schemeLinkId) {
		super.setAttributes(created, modified, creatorId, modifierId, version);

		assert parentSchemePathId != null && !parentSchemePathId.isVoid(): NON_VOID_EXPECTED;
		assert sequentialNumber != -1;
		assert kind != null && startAbstractSchemePortId != null
				&& endAbstractSchemePortId != null
				&& schemeCableThreadId != null
				&& schemeLinkId != null : NON_NULL_EXPECTED;
		switch (kind.value()) {
			case _SCHEME_ELEMENT:
				assert (sequentialNumber == 0 || !startAbstractSchemePortId.isVoid())
						&& schemeCableThreadId.isVoid()
						&& schemeLinkId.isVoid();
				break;
			case _SCHEME_CABLE_LINK:
				assert startAbstractSchemePortId.isVoid()
						&& endAbstractSchemePortId.isVoid()
						&& !schemeCableThreadId.isVoid()
						&& schemeLinkId.isVoid();
				break;
			case _SCHEME_LINK:
				assert startAbstractSchemePortId.isVoid()
						&& endAbstractSchemePortId.isVoid()
						&& schemeCableThreadId.isVoid()
						&& !schemeLinkId.isVoid();
				break;
			default:
				assert false;
		}

		this.parentSchemePathId = parentSchemePathId;
		this.sequentialNumber = sequentialNumber;
		this.kind = kind;
		this.startAbstractSchemePortId = startAbstractSchemePortId;
		this.endAbstractSchemePortId = endAbstractSchemePortId;
		this.schemeCableThreadId = schemeCableThreadId;
		this.schemeLinkId = schemeLinkId;
	}

	/**
	 * @see Describable#setDescription(String)
	 */
	public void setDescription(final String description) {
		getAbstractSchemeElement().setDescription(description);
	}

	public void setEndAbstractSchemePort(final AbstractSchemePort endAbstractSchemePort) {
		if (this.kind != SCHEME_ELEMENT)
			throw new UnsupportedOperationException(OBJECT_STATE_ILLEGAL);
		assert endAbstractSchemePort != null: NON_NULL_EXPECTED;
		/*
		 * Either the object is not yet initialized (which is very
		 * unlikely), or ensure that starting and ending ports belong to
		 * the same device (do compare references, see bug #86). Also
		 * note that this code WILL NOT work when bug #88 is fixed
		 * (scheme ports and scheme cable ports will have NO parent
		 * device).
		 */
		assert (isFirst() && this.startAbstractSchemePortId.isVoid())
				|| getStartAbstractSchemePort().getParentSchemeDevice()
				== endAbstractSchemePort.getParentSchemeDevice(): NO_COMMON_PARENT;
		final Identifier newEndAbstractSchemePortId = endAbstractSchemePort.getId();
		if (this.endAbstractSchemePortId.equals(newEndAbstractSchemePortId))
			return;
		this.endAbstractSchemePortId = newEndAbstractSchemePortId;
		super.markAsChanged();
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
	 * <p><em>Removes</em> itself from the old {@code SchemePath} and
	 * <em>adds</em> to the end of the new {@code SchemePath} if it&apos;s
	 * non-{@code null} (accordingly adjusting own
	 * {@link #sequentialNumber sequential number}), otherwise
	 * <em>deletes</em> itself from the pool.</p>
	 *
	 * <p>If {@code processSubsequentSiblings} is {@code true}, the same
	 * operation is undertaken with respect to {@code PathElement}s
	 * following this one within the old {@code SchemePath}, in order;
	 * otherwise subsequent {@code PathElement}s are only shifted by
	 * {@code -1}, their parent {@code SchemePath} remaining unchanged.</p>
	 *
	 * @param parentSchemePath
	 * @param processSubsequentSiblings
	 */
	public void setParentPathOwner(final SchemePath parentSchemePath,
			final boolean processSubsequentSiblings) {
		assert this.parentSchemePathId != null : OBJECT_NOT_INITIALIZED;
		assert !this.parentSchemePathId.isVoid() : EXACTLY_ONE_PARENT_REQUIRED;
		final Identifier newParentSchemePathId = Identifier.possiblyVoid(parentSchemePath);
		
		if (this.parentSchemePathId.equals(newParentSchemePathId)) {
			return;
		}

		int newSequentialNumber = parentSchemePath == null
				? -1
				: parentSchemePath.getPathMembers().size();
		final Iterator<PathElement> pathElementIterator =
				this.getParentPathOwner().getPathMembers().tailSet(this).iterator();
		if (processSubsequentSiblings) {
			while (pathElementIterator.hasNext()) {
				pathElementIterator.next().setParentPathOwner(newParentSchemePathId,
						newSequentialNumber++);
			}
		} else {
			assert pathElementIterator.hasNext();
			final PathElement pathElement = pathElementIterator.next();
			assert pathElement == this;

			pathElement.setParentPathOwner(newParentSchemePathId,
					newSequentialNumber++);

			while (pathElementIterator.hasNext()) {
				pathElementIterator.next().shiftLeft();
			}
		}
	}

	private void setParentPathOwner(final Identifier newParentSchemePathId,
			final int newSequentialNumber) {
		this.parentSchemePathId = newParentSchemePathId;
		super.markAsChanged();
		if (newParentSchemePathId.isVoid()) {
			this.sequentialNumber = -1;
			StorableObjectPool.delete(super.id);
		} else {
			this.sequentialNumber = newSequentialNumber;
		}
	}

	private void shiftLeft() {
		this.sequentialNumber--;
		super.markAsChanged();
	}

	/**
	 * @todo Remove {@code SuppressWarnings} annotation.
	 */
	@SuppressWarnings("unused")
	private void shiftRight() {
		this.sequentialNumber++;
		super.markAsChanged();
	}

	public void setSchemeCableLink(final SchemeCableLink schemeCableLink) {
		getSchemeCableThread().setParentSchemeCableLink(schemeCableLink);
	}

	public void setSchemeCableThread(final SchemeCableThread schemeCableThread) {
		if (this.kind != SCHEME_CABLE_LINK)
			throw new UnsupportedOperationException(OBJECT_STATE_ILLEGAL);
		assert schemeCableThread != null: NON_NULL_EXPECTED;
		final Identifier newSchemeCableThreadId = schemeCableThread.getId();
		if (this.schemeCableThreadId.equals(newSchemeCableThreadId))
			return;
		this.schemeCableThreadId = newSchemeCableThreadId;
		super.markAsChanged();
	}

	public void setSchemeElement(final SchemeElement schemeElement) {
		final SchemeDevice parentSchemeDevice = getStartAbstractSchemePort().getParentSchemeDevice();
		assert (parentSchemeDevice == getEndAbstractSchemePort().getParentSchemeDevice()): NO_COMMON_PARENT;
		parentSchemeDevice.setParentSchemeElement(schemeElement);
	}

	public void setSchemeLink(final SchemeLink schemeLink) {
		if (this.kind != SCHEME_LINK)
			throw new UnsupportedOperationException(OBJECT_STATE_ILLEGAL);
		assert schemeLink != null: NON_NULL_EXPECTED;
		final Identifier newSchemeLinkId = schemeLink.getId();
		if (this.schemeLinkId.equals(newSchemeLinkId))
			return;
		this.schemeLinkId = newSchemeLinkId;
		super.markAsChanged();
	}

	public void setStartAbstractSchemePort(final AbstractSchemePort startAbstractSchemePort) {
		if (this.kind != SCHEME_ELEMENT)
			throw new UnsupportedOperationException(OBJECT_STATE_ILLEGAL);
		assert startAbstractSchemePort != null: NON_NULL_EXPECTED;
		/*
		 * Either the object is not yet initialized (which is very
		 * unlikely), or ensure that starting and ending ports belong to
		 * the same device (do compare references, see bug #86). Also
		 * note that this code WILL NOT work when bug #88 is fixed
		 * (scheme ports and scheme cable ports will have NO parent
		 * device).
		 */
		assert (isLast() && this.endAbstractSchemePortId.isVoid())
				|| getEndAbstractSchemePort().getParentSchemeDevice()
				== startAbstractSchemePort.getParentSchemeDevice(): NO_COMMON_PARENT;
		final Identifier newStartAbstractSchemePortId = startAbstractSchemePort.getId();
		if (this.startAbstractSchemePortId.equals(newStartAbstractSchemePortId))
			return;
		this.startAbstractSchemePortId = newStartAbstractSchemePortId;
		super.markAsChanged();
	}

	/**
	 * @param transferable
	 * @see com.syrus.AMFICOM.general.StorableObject#fromTransferable(IdlStorableObject)
	 */
	@Override
	protected void fromTransferable(final IdlStorableObject transferable) {
		final IdlPathElement pathElement = (IdlPathElement) transferable;
		try {
			super.fromTransferable(pathElement);
		} catch (final ApplicationException ae) {
			/*
			 * Never. Arseniy, don't add any error-handling code,
			 * please.
			 */
			assert false;
		}
		this.parentSchemePathId = new Identifier(pathElement.parentSchemePathId);
		this.sequentialNumber = pathElement.sequentialNumber;
		final IdlData data = pathElement.data;
		this.kind = data.discriminator();
		switch (this.kind.value()) {
			case _SCHEME_ELEMENT:
				final IdlSchemeElementData schemeElementData = data.schemeElementData();
				this.startAbstractSchemePortId = new Identifier(schemeElementData.startAbstractSchemePortId);
				this.endAbstractSchemePortId = new Identifier(schemeElementData.endAbstractSchemePortId);
				this.schemeCableThreadId = VOID_IDENTIFIER;
				this.schemeLinkId = VOID_IDENTIFIER;
				break;
			case _SCHEME_CABLE_LINK:
				this.startAbstractSchemePortId = VOID_IDENTIFIER;
				this.endAbstractSchemePortId = VOID_IDENTIFIER;
				this.schemeCableThreadId = new Identifier(data.schemeCableThreadId());
				this.schemeLinkId = VOID_IDENTIFIER;
				break;
			case _SCHEME_LINK:
				this.startAbstractSchemePortId = VOID_IDENTIFIER;
				this.endAbstractSchemePortId = VOID_IDENTIFIER;
				this.schemeCableThreadId = VOID_IDENTIFIER;
				this.schemeLinkId = new Identifier(data.schemeLinkId());
				break;
			default:
				assert false;
		}
	}

	/**
	 * @todo Remove {@code SuppressWarnings} annotation.
	 */
	public void insertSelfBefore(@SuppressWarnings("unused") final PathElement sibling) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @todo Remove {@code SuppressWarnings} annotation.
	 */
	public void insertSelfAfter(@SuppressWarnings("unused") final PathElement sibling) {
		throw new UnsupportedOperationException();
	}

	/*-********************************************************************
	 * Non-model members.                                                 *
	 **********************************************************************/

	private boolean isFirst() {
		return this.sequentialNumber == 0;
	}

	private boolean isLast() {
		return this.sequentialNumber + 1 == this.getParentPathOwner().getPathMembers().size();
	}

	boolean hasOpticalPort() {
		final AbstractSchemePort startAbstractSchemePort = getStartAbstractSchemePort();
		if (startAbstractSchemePort instanceof SchemePort
				&& startAbstractSchemePort.getPortType().getSort() == PORTTYPESORT_OPTICAL)
			return true;
		
		final AbstractSchemePort endAbstractSchemePort = getEndAbstractSchemePort();
		if (endAbstractSchemePort instanceof SchemePort
				&& endAbstractSchemePort.getPortType().getSort() == PORTTYPESORT_OPTICAL)
			return true;
		return false;
	}
}
