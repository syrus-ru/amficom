/*-
 * $Id: PathElement.java,v 1.102 2006/03/14 10:47:55 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme;

import static com.syrus.AMFICOM.general.ErrorMessages.EXACTLY_ONE_PARENT_REQUIRED;
import static com.syrus.AMFICOM.general.ErrorMessages.NON_NULL_EXPECTED;
import static com.syrus.AMFICOM.general.ErrorMessages.NON_VOID_EXPECTED;
import static com.syrus.AMFICOM.general.ErrorMessages.NO_COMMON_PARENT;
import static com.syrus.AMFICOM.general.ErrorMessages.OBJECT_BADLY_INITIALIZED;
import static com.syrus.AMFICOM.general.ErrorMessages.OBJECT_NOT_INITIALIZED;
import static com.syrus.AMFICOM.general.ErrorMessages.OBJECT_STATE_ILLEGAL;
import static com.syrus.AMFICOM.general.ErrorMessages.OBJECT_WILL_DELETE_ITSELF_FROM_POOL;
import static com.syrus.AMFICOM.general.ErrorMessages.OPERATION_IS_OPTIONAL;
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
import static java.util.logging.Level.WARNING;

import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.SortedSet;

import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Describable;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.ReverseDependencyContainer;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.general.corba.IdlStorableObject;
import com.syrus.AMFICOM.scheme.corba.IdlPathElement;
import com.syrus.AMFICOM.scheme.corba.IdlPathElementHelper;
import com.syrus.AMFICOM.scheme.corba.IdlPathElementPackage.IdlData;
import com.syrus.AMFICOM.scheme.corba.IdlPathElementPackage.IdlDataPackage.IdlKind;
import com.syrus.AMFICOM.scheme.corba.IdlPathElementPackage.IdlDataPackage.IdlSchemeElementData;
import com.syrus.AMFICOM.scheme.xml.XmlPathElement;
import com.syrus.util.Log;
import com.syrus.util.transport.idl.IdlConversionException;
import com.syrus.util.transport.xml.XmlConversionException;
import com.syrus.util.transport.xml.XmlTransferableObject;

/**
 * #17 in hierarchy.
 *
 * PathElement has no associated <code>name</code> field:
 * its {@link PathElement#getName() getName()} method actually returns
 * {@link PathElement#getAbstractSchemeElement() getAbstractSchemeElement()}<code>.</code>{@link AbstractSchemeElement#getName() getName()}.
 *
 * @author $Author: bass $
 * @version $Revision: 1.102 $, $Date: 2006/03/14 10:47:55 $
 * @module scheme
 * @todo If Scheme(Cable|)Port ever happens to belong to more than one
 *       SchemeElement
 */
public final class PathElement extends StorableObject
		implements Describable, Comparable<PathElement>,
		PathMember<SchemePath, PathElement>, ReverseDependencyContainer,
		XmlTransferableObject<XmlPathElement>{
	private static final long serialVersionUID = 3905799768986038576L;

	Identifier parentSchemePathId;

	int sequentialNumber;

	private int kind;

	/**
	 * May reference either {@link SchemePort} or {@link SchemeCablePort}.
	 * Empty if type is other than {@link IdlKind#SCHEME_ELEMENT}.
	 */
	private Identifier startAbstractSchemePortId;

	/**
	 * May reference either {@link SchemePort} or {@link SchemeCablePort}.
	 * Empty if type is other than {@link IdlKind#SCHEME_ELEMENT}.
	 */
	private Identifier endAbstractSchemePortId;

	/**
	 * Empty if type is other than
	 * {@link IdlKind#SCHEME_CABLE_LINK}.
	 */
	private Identifier schemeCableThreadId;

	/**
	 * Empty if type is other than {@link IdlKind#SCHEME_LINK}.
	 */
	Identifier schemeLinkId;

	private transient String name;

	private transient String description;

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
	private PathElement(final Identifier id,
			final Date created,
			final Date modified,
			final Identifier creatorId,
			final Identifier modifierId,
			final StorableObjectVersion version,
			final SchemePath parentSchemePath) {
		super(id, created, modified, creatorId, modifierId, version);
		this.parentSchemePathId = Identifier.possiblyVoid(parentSchemePath);
	}

	/**
	 * Client-side constructor; creates an instance of type
	 * {@link IdlKind#SCHEME_ELEMENT}.
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
	 * @throws ApplicationException
	 * @todo Narrow visibility to private and remove
	 *       {@link Identifier#possiblyVoid(com.syrus.AMFICOM.general.Identifiable)}
	 *       invocations (in favor of direct checks) unless this constructor
	 *       is ever called by databases.
	 */
	PathElement(final Identifier id,
			final Date created,
			final Date modified,
			final Identifier creatorId,
			final Identifier modifierId,
			final StorableObjectVersion version,
			final SchemePath parentSchemePath,
			final AbstractSchemePort startAbstractSchemePort,
			final AbstractSchemePort endSbstractSchemePort)
	throws ApplicationException {
		this(id, created, modified, creatorId, modifierId, version, parentSchemePath);
		/*
		 * Here and below: this will work since current object is not
		 * yet put to the pool.
		 */
		this.sequentialNumber = (parentSchemePath == null)
				? -1
				: parentSchemePath.getPathMembers().size();
		this.kind = _SCHEME_ELEMENT;
		this.startAbstractSchemePortId = Identifier.possiblyVoid(startAbstractSchemePort);
		this.endAbstractSchemePortId = Identifier.possiblyVoid(endSbstractSchemePort);
		this.schemeCableThreadId = VOID_IDENTIFIER;
		this.schemeLinkId = VOID_IDENTIFIER;
	}

	/**
	 * Client-side constructor; creates an instance of type
	 * {@link IdlKind#SCHEME_CABLE_LINK}.
	 *
	 * @param id
	 * @param created
	 * @param modified
	 * @param creatorId
	 * @param modifierId
	 * @param version
	 * @param parentSchemePath
	 * @param schemeCableThread
	 * @throws ApplicationException
	 * @todo Narrow visibility to private and remove
	 *       {@link Identifier#possiblyVoid(com.syrus.AMFICOM.general.Identifiable)}
	 *       invocations (in favor of direct checks) unless this constructor
	 *       is ever called by databases.
	 */
	PathElement(final Identifier id,
			final Date created,
			final Date modified,
			final Identifier creatorId,
			final Identifier modifierId,
			final StorableObjectVersion version,
			final SchemePath parentSchemePath,
			final SchemeCableThread schemeCableThread)
	throws ApplicationException {
		this(id, created, modified, creatorId, modifierId, version, parentSchemePath);
		this.sequentialNumber = (parentSchemePath == null)
				? -1
				: parentSchemePath.getPathMembers().size();
		this.kind = _SCHEME_CABLE_LINK;
		this.startAbstractSchemePortId = VOID_IDENTIFIER;
		this.endAbstractSchemePortId = VOID_IDENTIFIER;
		this.schemeCableThreadId = Identifier.possiblyVoid(schemeCableThread);
		this.schemeLinkId = VOID_IDENTIFIER;
	}

	/**
	 * Client-side constructor; creates an instance of type
	 * {@link IdlKind#SCHEME_LINK}.
	 *
	 * @param id
	 * @param created
	 * @param modified
	 * @param creatorId
	 * @param modifierId
	 * @param version
	 * @param parentSchemePath
	 * @param schemeLink
	 * @throws ApplicationException
	 * @todo Narrow visibility to private and remove
	 *       {@link Identifier#possiblyVoid(com.syrus.AMFICOM.general.Identifiable)}
	 *       invocations (in favor of direct checks) unless this constructor
	 *       is ever called by databases.
	 */
	PathElement(final Identifier id,
			final Date created,
			final Date modified,
			final Identifier creatorId,
			final Identifier modifierId,
			final StorableObjectVersion version,
			final SchemePath parentSchemePath,
			final SchemeLink schemeLink)
	throws ApplicationException {
		this(id, created, modified, creatorId, modifierId, version, parentSchemePath);
		this.sequentialNumber = (parentSchemePath == null)
				? -1
				: parentSchemePath.getPathMembers().size();
		this.kind = _SCHEME_LINK;
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
	PathElement(final Identifier id,
			final Date created,
			final Date modified,
			final Identifier creatorId,
			final Identifier modifierId,
			final StorableObjectVersion version,
			final SchemePath parentSchemePath,
			final int sequentialNumber,
			final IdlKind kind,
			final AbstractSchemePort startAbstractSchemePort,
			final AbstractSchemePort endAbstractSchemePort,
			final SchemeCableThread schemeCableThread,
			final SchemeLink schemeLink) {
		this(id, created, modified, creatorId, modifierId, version, parentSchemePath);
		this.sequentialNumber = sequentialNumber;
		this.kind = (kind == null) ? 0 : kind.value();
		this.startAbstractSchemePortId = Identifier.possiblyVoid(startAbstractSchemePort);
		this.endAbstractSchemePortId = Identifier.possiblyVoid(endAbstractSchemePort);
		this.schemeCableThreadId = Identifier.possiblyVoid(schemeCableThread);
		this.schemeLinkId = Identifier.possiblyVoid(schemeLink);
	}

	/**
	 * @param transferable
	 * @throws CreateObjectException
	 */
	public PathElement(final IdlPathElement transferable) throws CreateObjectException {
		try {
			fromIdlTransferable(transferable);
		} catch (final IdlConversionException ice) {
			throw new CreateObjectException(ice);
		}
	}

	/**
	 * @param creatorId
	 * @param parentSchemePath
	 * @param startAbstractSchemePort
	 * @param endAbstractSchemePort
	 * @return a newly-created instance of type
	 *         {@link IdlKind#SCHEME_ELEMENT}.
	 * @throws CreateObjectException
	 */
	@ParameterizationPending(value = {"final boolean usePool"})
	public static PathElement createInstance(final Identifier creatorId,
			final SchemePath parentSchemePath,
			final AbstractSchemePort startAbstractSchemePort,
			final AbstractSchemePort endAbstractSchemePort)
	throws CreateObjectException {
		final boolean usePool = false;

		assert creatorId != null && !creatorId.isVoid(): NON_VOID_EXPECTED;
		
		/*
		 * Starting AbstractSchemePort may be null IF AND ONLY IF parent
		 * SchemePath contains no other PathElements, i. e. this one is
		 * also the first one.
		 *
		 * Ending AbstractSchemePort may be null, as noone cares.
		 */
		try {
			assert parentSchemePath != null
					&& (parentSchemePath.getPathMembers().isEmpty()
							== (startAbstractSchemePort == null)): NON_NULL_EXPECTED;
		} catch (final ApplicationException ae) {
			Log.debugMessage(ae, SEVERE);
		}

		try {
			final Date created = new Date();
			final PathElement pathElement = new PathElement(IdentifierPool.getGeneratedIdentifier(PATHELEMENT_CODE),
					created,
					created,
					creatorId,
					creatorId,
					StorableObjectVersion.INITIAL_VERSION,
					parentSchemePath,
					startAbstractSchemePort,
					endAbstractSchemePort);
			parentSchemePath.getPathElementContainerWrappee().addToCache(pathElement, usePool);

			pathElement.markAsChanged();
			return pathElement;
		} catch (final CreateObjectException coe) {
			throw coe;
		} catch (final IdentifierGenerationException ige) {
			throw new CreateObjectException("PathElement.createInstance() | cannot generate identifier ", ige);
		} catch (final ApplicationException ae) {
			throw new CreateObjectException(ae);
		}
	}

	/**
	 * @param creatorId
	 * @param parentSchemePath
	 * @param schemeCableThread
	 * @return a newly-created instance of type
	 *         {@link IdlKind#SCHEME_CABLE_LINK}.
	 * @throws CreateObjectException
	 */
	@ParameterizationPending(value = {"final boolean usePool"})
	public static PathElement createInstance(final Identifier creatorId,
			final SchemePath parentSchemePath,
			final SchemeCableThread schemeCableThread)
	throws CreateObjectException {
		final boolean usePool = false;

		assert creatorId != null && !creatorId.isVoid(): NON_VOID_EXPECTED;
		assert parentSchemePath != null && schemeCableThread != null: NON_NULL_EXPECTED;
		try {
			final Date created = new Date();
			final PathElement pathElement = new PathElement(IdentifierPool.getGeneratedIdentifier(PATHELEMENT_CODE),
					created,
					created,
					creatorId,
					creatorId,
					StorableObjectVersion.INITIAL_VERSION,
					parentSchemePath,
					schemeCableThread);
			parentSchemePath.getPathElementContainerWrappee().addToCache(pathElement, usePool);

			pathElement.markAsChanged();
			return pathElement;
		} catch (final CreateObjectException coe) {
			throw coe;
		} catch (final IdentifierGenerationException ige) {
			throw new CreateObjectException("PathElement.createInstance() | cannot generate identifier ", ige);
		} catch (final ApplicationException ae) {
			throw new CreateObjectException(ae);
		}
	}

	/**
	 * @param creatorId
	 * @param parentSchemePath
	 * @param schemeLink
	 * @return a newly-created instance of type
	 *         {@link IdlKind#SCHEME_LINK}.
	 * @throws CreateObjectException
	 */
	@ParameterizationPending(value = {"final boolean usePool"})
	public static PathElement createInstance(final Identifier creatorId,
			final SchemePath parentSchemePath,
			final SchemeLink schemeLink)
	throws CreateObjectException {
		final boolean usePool = false;

		assert creatorId != null && !creatorId.isVoid(): NON_VOID_EXPECTED;
		assert parentSchemePath != null && schemeLink != null: NON_NULL_EXPECTED;
		try {
			final Date created = new Date();
			final PathElement pathElement = new PathElement(IdentifierPool.getGeneratedIdentifier(PATHELEMENT_CODE),
					created,
					created,
					creatorId,
					creatorId,
					StorableObjectVersion.INITIAL_VERSION,
					parentSchemePath,
					schemeLink);
			parentSchemePath.getPathElementContainerWrappee().addToCache(pathElement, usePool);

			pathElement.markAsChanged();
			return pathElement;
		} catch (final CreateObjectException coe) {
			throw coe;
		} catch (final IdentifierGenerationException ige) {
			throw new CreateObjectException("PathElement.createInstance() | cannot generate identifier ", ige);
		} catch (final ApplicationException ae) {
			throw new CreateObjectException(ae);
		}
	}

	/**
	 * @param that
	 */
	public int compareTo(final PathElement that) {
		assert this.parentSchemePathId.equals(that.parentSchemePathId): NO_COMMON_PARENT;
		return this.sequentialNumber <= that.sequentialNumber ? this.sequentialNumber < that.sequentialNumber ? -1 : 0 : 1;
	}

	public Identifier getAbstractSchemeElementId() {
		final Identifier abstractSchemeElementId;

		switch (this.getKind().value()) {
		case _SCHEME_CABLE_LINK:
			abstractSchemeElementId = this.getSchemeCableThread().getParentSchemeCableLinkId();
			break;
		case _SCHEME_ELEMENT:
			abstractSchemeElementId = this.getSchemeElementId();
			break;
		case _SCHEME_LINK:
			abstractSchemeElementId = this.getSchemeLinkId();
			break;
		default:
			throw new UnsupportedOperationException(OBJECT_STATE_ILLEGAL);
		}

		assert abstractSchemeElementId != null : NON_NULL_EXPECTED + "; " + this + "; id = " + this.getId().getIdentifierCode();
		assert !abstractSchemeElementId.isVoid() : NON_VOID_EXPECTED + "; " + this + "; id = " + this.getId().getIdentifierCode();

		return abstractSchemeElementId;
	}

	public AbstractSchemeElement getAbstractSchemeElement() {
		final AbstractSchemeElement abstractSchemeElement;
		
		switch (this.getKind().value()) {
		case _SCHEME_CABLE_LINK:
			abstractSchemeElement = this.getSchemeCableLink();
			break;
		case _SCHEME_ELEMENT:
			abstractSchemeElement = this.getSchemeElement();
			break;
		case _SCHEME_LINK:
			abstractSchemeElement = this.getSchemeLink();
			break;
		default:
			throw new UnsupportedOperationException(OBJECT_STATE_ILLEGAL);
		}

		assert abstractSchemeElement != null : NON_NULL_EXPECTED + "; " + this + "; id = " + this.getId().getIdentifierCode();

		return abstractSchemeElement;
	}

	/**
	 * @see com.syrus.AMFICOM.general.StorableObject#getDependenciesTmpl()
	 */
	@Override
	protected Set<Identifiable> getDependenciesTmpl() {
		assert this.parentSchemePathId != null && !this.parentSchemePathId.isVoid()
				&& this.sequentialNumber != -1
				&& this.startAbstractSchemePortId != null
				&& this.endAbstractSchemePortId != null
				&& this.schemeCableThreadId != null
				&& this.schemeLinkId != null : OBJECT_NOT_INITIALIZED;
		switch (this.getKind().value()) {
			case _SCHEME_ELEMENT:
				/*-
				 * Check for
				 * (isLast() || !this.endAbstractSchemePortId.isVoid())
				 * is disabled due to
				 * a ConcurrentModificationException in LRUMap. 
				 */
				assert (isFirst() || !this.startAbstractSchemePortId.isVoid())
//						&& (isLast() || !this.endAbstractSchemePortId.isVoid())
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
		return dependencies;
	}

	/**
	 * @see com.syrus.AMFICOM.general.ReverseDependencyContainer#getReverseDependencies(boolean)
	 */
	public Set<Identifiable> getReverseDependencies(final boolean usePool) {
		return Collections.<Identifiable>singleton(super.id);
	}

	/**
	 * @see Describable#getDescription()
	 */
	public String getDescription() {
		if (this.description == null) {
			this.description = this.getAbstractSchemeElement().getDescription();
		}
		return this.description;
	}

	Identifier getEndAbstractSchemePortId() {
		assert this.endAbstractSchemePortId != null: OBJECT_NOT_INITIALIZED;

		final boolean endAbstractSchemePortIdVoid = this.endAbstractSchemePortId.isVoid();
		final short endAbstractSchemePortIdMajor = this.endAbstractSchemePortId.getMajor();

		assert endAbstractSchemePortIdVoid
				|| endAbstractSchemePortIdMajor == SCHEMEPORT_CODE
				|| endAbstractSchemePortIdMajor == SCHEMECABLEPORT_CODE;
		if (this.getKind() == SCHEME_ELEMENT) {
			/*
			 * The assertion is turned off since #isLast() behaves
			 * incorrectly when working server side and not all
			 * siblings are saved.
			 */
			assert true || isLast() || !endAbstractSchemePortIdVoid : OBJECT_BADLY_INITIALIZED;
		} else {
			assert endAbstractSchemePortIdVoid;
		}
		return this.endAbstractSchemePortId;
	}

	/**
	 * A wrapper around {@link #getEndAbstractSchemePortId()}.
	 */
	public AbstractSchemePort getEndAbstractSchemePort() {
		if (this.getKind() != SCHEME_ELEMENT) {
			throw new UnsupportedOperationException(OBJECT_STATE_ILLEGAL);
		}

		try {
			return StorableObjectPool.getStorableObject(this.getEndAbstractSchemePortId(), true);
		} catch (final ApplicationException ae) {
			Log.debugMessage(ae, SEVERE);
			return null;
		}
	}

	/**
	 * @see com.syrus.AMFICOM.general.Namable#getName()
	 */
	public String getName() {
		if (this.name == null) {
			this.name = this.getAbstractSchemeElement().getName();
		}
		return this.name;
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
			return StorableObjectPool.getStorableObject(this.getParentSchemePathId(), true);
		} catch (final ApplicationException ae) {
			Log.debugMessage(ae, SEVERE);
			return null;
		}
	}

	public IdlKind getKind() {
		return IdlKind.from_int(this.kind);
	}

	public SchemeCableLink getSchemeCableLink() {
		return getSchemeCableThread().getParentSchemeCableLink();
	}

	Identifier getSchemeCableThreadId() {
		assert this.schemeCableThreadId != null: OBJECT_NOT_INITIALIZED;
		assert this.schemeCableThreadId.isVoid() ^ this.getKind() == SCHEME_CABLE_LINK;
		assert this.schemeCableThreadId.isVoid() ^ this.schemeCableThreadId.getMajor() == SCHEMECABLETHREAD_CODE;
		return this.schemeCableThreadId;
	}

	/**
	 * A wrapper around {@link #getSchemeCableThreadId()}.
	 */
	public SchemeCableThread getSchemeCableThread() {
		if (this.getKind() != SCHEME_CABLE_LINK) {
			throw new UnsupportedOperationException(OBJECT_STATE_ILLEGAL);
		}

		try {
			return StorableObjectPool.getStorableObject(this.getSchemeCableThreadId(), true);
		} catch (final ApplicationException ae) {
			Log.debugMessage(ae, SEVERE);
			return null;
		}
	}

	private Identifier getSchemeElementId() {
		final SchemeDevice parentSchemeDevice;
		if (this.startAbstractSchemePortId.isVoid()) {
			if (this.endAbstractSchemePortId.isVoid()) {
				Log.debugMessage("Both (abstract) scheme ports of this path element are null. Seems strange, unless it's the only element of its parent path. Returning null as well.",
						SEVERE);
				return VOID_IDENTIFIER;
			}
			parentSchemeDevice = this.getEndAbstractSchemePort().getParentSchemeDevice();
		} else {
			parentSchemeDevice = this.getStartAbstractSchemePort().getParentSchemeDevice();
			assert this.endAbstractSchemePortId.isVoid() || this.getEndAbstractSchemePort().getParentSchemeDeviceId().equals(parentSchemeDevice) : NO_COMMON_PARENT;
		}
		assert parentSchemeDevice != null;
		return parentSchemeDevice.getParentSchemeElementId();
	}

	public SchemeElement getSchemeElement() {
		final SchemeDevice parentSchemeDevice;
		if (this.startAbstractSchemePortId.isVoid()) {
			if (this.endAbstractSchemePortId.isVoid()) {
				Log.debugMessage("Both (abstract) scheme ports of this path element are null. Seems strange, unless it's the only element of its parent path. Returning null as well.",
						SEVERE);
				return null;
			}
			parentSchemeDevice = this.getEndAbstractSchemePort().getParentSchemeDevice();
		} else {
			parentSchemeDevice = this.getStartAbstractSchemePort().getParentSchemeDevice();
			assert this.endAbstractSchemePortId.isVoid() || this.getEndAbstractSchemePort().getParentSchemeDeviceId().equals(parentSchemeDevice) : NO_COMMON_PARENT;
		}
		assert parentSchemeDevice != null;
		return parentSchemeDevice.getParentSchemeElement();
	}

	Identifier getSchemeLinkId() {
		assert this.schemeLinkId != null : OBJECT_NOT_INITIALIZED;
		assert this.schemeLinkId.isVoid() ^ this.getKind() == SCHEME_LINK;
		assert this.schemeLinkId.isVoid() ^ this.schemeLinkId.getMajor() == SCHEMELINK_CODE;
		return this.schemeLinkId;
	}

	/**
	 * A wrapper around {@link #getSchemeLinkId()}.
	 */
	public SchemeLink getSchemeLink() {
		if (this.getKind() != SCHEME_LINK) {
			throw new UnsupportedOperationException(OBJECT_STATE_ILLEGAL);
		}

		try {
			return StorableObjectPool.getStorableObject(this.getSchemeLinkId(), true);
		} catch (final ApplicationException ae) {
			Log.debugMessage(ae, SEVERE);
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

	/**
	 * @param sequentialNumber
	 */
	private void setSequentialNumber(final int sequentialNumber) {
		if (this.sequentialNumber == sequentialNumber) {
			return;
		}
		this.sequentialNumber = sequentialNumber;
		super.markAsChanged();
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
		if (this.getKind() == SCHEME_ELEMENT) {
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
		if (this.getKind() != SCHEME_ELEMENT) {
			throw new UnsupportedOperationException(OBJECT_STATE_ILLEGAL);
		}

		try {
			return StorableObjectPool.getStorableObject(this.getStartAbstractSchemePortId(), true);
		} catch (final ApplicationException ae) {
			Log.debugMessage(ae, SEVERE);
			return null;
		}
	}

	/**
	 * @param orb
	 * @see com.syrus.util.transport.idl.IdlTransferableObject#getIdlTransferable(org.omg.CORBA.ORB)
	 */
	@Override
	public IdlPathElement getIdlTransferable(final ORB orb) {
		final IdlData data = new IdlData();
		final IdlKind idlKind = this.getKind();
		switch (this.getKind().value()) {
			case _SCHEME_ELEMENT:
				data.schemeElementData(idlKind, new IdlSchemeElementData(
						this.startAbstractSchemePortId.getIdlTransferable(),
						this.endAbstractSchemePortId.getIdlTransferable()));
				break;
			case _SCHEME_CABLE_LINK:
				data.schemeCableThreadId(idlKind, this.schemeCableThreadId.getIdlTransferable());
				break;
			case _SCHEME_LINK:
				data.schemeLinkId(idlKind, this.schemeLinkId.getIdlTransferable());
				break;
			default:
				assert false;
		}
		return IdlPathElementHelper.init(orb,
				this.id.getIdlTransferable(),
				this.created.getTime(),
				this.modified.getTime(),
				this.creatorId.getIdlTransferable(),
				this.modifierId.getIdlTransferable(),
				this.version.longValue(),
				this.parentSchemePathId.getIdlTransferable(),
				this.sequentialNumber, data);
	}

	/**
	 * @param pathElement
	 * @param importType
	 * @param usePool
	 * @throws XmlConversionException
	 * @see com.syrus.util.transport.xml.XmlTransferableObject#getXmlTransferable(org.apache.xmlbeans.XmlObject, String, boolean)
	 */
	public void getXmlTransferable(
			final XmlPathElement pathElement,
			final String importType,
			final boolean usePool)
	throws XmlConversionException {
		throw new UnsupportedOperationException();
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
	void setAttributes(final Date created,
			final Date modified,
			final Identifier creatorId,
			final Identifier modifierId,
			final StorableObjectVersion version,
			final Identifier parentSchemePathId,
			final int sequentialNumber,
			final IdlKind kind,
			final Identifier startAbstractSchemePortId,
			final Identifier endAbstractSchemePortId,
			final Identifier schemeCableThreadId,
			final Identifier schemeLinkId) {
		synchronized (this) {
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
			this.kind = kind.value();
			this.startAbstractSchemePortId = startAbstractSchemePortId;
			this.endAbstractSchemePortId = endAbstractSchemePortId;
			this.schemeCableThreadId = schemeCableThreadId;
			this.schemeLinkId = schemeLinkId;
		}
	}

	/**
	 * This is an optional operation, not implemented here.
	 *
	 * @throws UnsupportedOperationException
	 * @see Describable#setDescription(String)
	 */
	public void setDescription(final String description) {
		throw new UnsupportedOperationException(OPERATION_IS_OPTIONAL);
	}

	/**
	 * @param endAbstractSchemePortId
	 */
	void setEndAbstractSchemePortId(final Identifier endAbstractSchemePortId) {
		if (this.getKind() != SCHEME_ELEMENT) {
			throw new UnsupportedOperationException(OBJECT_STATE_ILLEGAL);
		}
		assert endAbstractSchemePortId != null : NON_NULL_EXPECTED;
		assert !endAbstractSchemePortId.isVoid() : NON_VOID_EXPECTED;
		assert endAbstractSchemePortId.getMajor() == SCHEMEPORT_CODE || endAbstractSchemePortId.getMajor() == SCHEMECABLEPORT_CODE;

		if (this.endAbstractSchemePortId.equals(endAbstractSchemePortId)) {
			return;
		}
		this.endAbstractSchemePortId = endAbstractSchemePortId;
		super.markAsChanged();
	}

	/**
	 * A wrapper around {@link #setEndAbstractSchemePortId(Identifier)}.
	 *
	 * @param endAbstractSchemePort
	 */
	public void setEndAbstractSchemePort(final AbstractSchemePort endAbstractSchemePort) {
		/*
		 * The two following checks are doubled in the modifier method
		 * for id.
		 */
		if (this.getKind() != SCHEME_ELEMENT) {
			throw new UnsupportedOperationException(OBJECT_STATE_ILLEGAL);
		}
		assert endAbstractSchemePort != null : NON_NULL_EXPECTED;

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
		this.setEndAbstractSchemePortId(Identifier.possiblyVoid(endAbstractSchemePort));
	}

	/**
 	 * This is an optional operation, not implemented here.
 	 * 
	 * @throws UnsupportedOperationException
	 * @see com.syrus.AMFICOM.general.Namable#setName(String)
	 */
	public void setName(final String name) {
		throw new UnsupportedOperationException(OPERATION_IS_OPTIONAL);
	}

	/**
	 * @param parentScheme
	 * @param usePool
	 * @throws ApplicationException
	 */
	public void setParentScheme(final Scheme parentScheme,
			final boolean usePool)
	throws ApplicationException {
		getAbstractSchemeElement().setParentScheme(parentScheme, usePool);
	}

	/**
	 * @param parentSchemePathId
	 * @throws ApplicationException
	 */
	@ParameterizationPending(value = {"final boolean usePool"})
	void setParentSchemePathId(final Identifier parentSchemePathId)
	throws ApplicationException {
		final boolean usePool = false;

		assert parentSchemePathId != null : NON_NULL_EXPECTED;
		assert parentSchemePathId.isVoid() || parentSchemePathId.getMajor() == SCHEMEPATH_CODE;

		if (this.parentSchemePathId.equals(parentSchemePathId)) {
			return;
		}

		this.getParentPathOwner().getPathElementContainerWrappee().removeFromCache(this, usePool);

		if (parentSchemePathId.isVoid()) {
			Log.debugMessage(OBJECT_WILL_DELETE_ITSELF_FROM_POOL, WARNING);
			StorableObjectPool.delete(this.getReverseDependencies(usePool));
		} else {
			StorableObjectPool.<SchemePath>getStorableObject(parentSchemePathId, true).getPathElementContainerWrappee().addToCache(this, usePool);
		}

		this.parentSchemePathId = parentSchemePathId;
		this.markAsChanged();
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
	 * @throws ApplicationException
	 */
	public void setParentPathOwner(final SchemePath parentSchemePath,
			final boolean processSubsequentSiblings)
	throws ApplicationException {
		assert this.parentSchemePathId != null : OBJECT_NOT_INITIALIZED;
		assert !this.parentSchemePathId.isVoid() : EXACTLY_ONE_PARENT_REQUIRED;
		final Identifier newParentSchemePathId = Identifier.possiblyVoid(parentSchemePath);
		
		if (this.parentSchemePathId.equals(newParentSchemePathId)) {
			return;
		}

		int newSequentialNumber = (parentSchemePath == null)
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

	/**
	 * A wrapper around {@link #setParentSchemePathId(Identifier)}.
	 *
	 * @param newParentSchemePathId
	 * @param newSequentialNumber
	 * @throws ApplicationException
	 */
	private void setParentPathOwner(final Identifier newParentSchemePathId,
			final int newSequentialNumber)
	throws ApplicationException {
		this.setSequentialNumber(newParentSchemePathId.isVoid()
				? -1
				: newSequentialNumber);
		this.setParentSchemePathId(newParentSchemePathId);
	}

	private void shiftLeft() {
		this.sequentialNumber--;
		super.markAsChanged();
	}

	private void shiftRight() {
		this.sequentialNumber++;
		super.markAsChanged();
	}

	/**
	 * @param schemeCableThreadId
	 */
	void setSchemeCableThreadId(final Identifier schemeCableThreadId) {
		if (this.getKind() != SCHEME_CABLE_LINK) {
			throw new UnsupportedOperationException(OBJECT_STATE_ILLEGAL);
		}
		assert schemeCableThreadId != null : NON_NULL_EXPECTED;
		assert !schemeCableThreadId.isVoid() : NON_VOID_EXPECTED;
		assert schemeCableThreadId.getMajor() == SCHEMECABLETHREAD_CODE;

		if (this.schemeCableThreadId.equals(schemeCableThreadId)) {
			return;
		}
		this.schemeCableThreadId = schemeCableThreadId;
		super.markAsChanged();
	}

	/**
	 * A wrapper around {@link #setSchemeCableThreadId(Identifier)}.
	 *
	 * @param schemeCableThread
	 */
	public void setSchemeCableThread(final SchemeCableThread schemeCableThread) {
		this.setSchemeCableThreadId(Identifier.possiblyVoid(schemeCableThread));
	}

	/**
	 * @param schemeLinkId
	 */
	void setSchemeLinkId(final Identifier schemeLinkId) {
		if (this.getKind() != SCHEME_LINK) {
			throw new UnsupportedOperationException(OBJECT_STATE_ILLEGAL);
		}
		assert schemeLinkId != null : NON_NULL_EXPECTED;
		assert !schemeLinkId.isVoid() : NON_VOID_EXPECTED;
		assert schemeLinkId.getMajor() == SCHEMELINK_CODE;

		if (this.schemeLinkId.equals(schemeLinkId)) {
			return;
		}
		this.schemeLinkId = schemeLinkId;
		super.markAsChanged();
	}

	/**
	 * A wrapper around {@link #setSchemeLinkId(Identifier)}.
	 *
	 * @param schemeLink
	 */
	public void setSchemeLink(final SchemeLink schemeLink) {
		this.setSchemeLinkId(Identifier.possiblyVoid(schemeLink));
	}

	/**
	 * @param startAbstractSchemePortId
	 */
	void setStartAbstractSchemePortId(final Identifier startAbstractSchemePortId) {
		if (this.getKind() != SCHEME_ELEMENT) {
			throw new UnsupportedOperationException(OBJECT_STATE_ILLEGAL);
		}
		assert startAbstractSchemePortId != null : NON_NULL_EXPECTED;
		assert !startAbstractSchemePortId.isVoid() : NON_VOID_EXPECTED;
		assert startAbstractSchemePortId.getMajor() == SCHEMEPORT_CODE || startAbstractSchemePortId.getMajor() == SCHEMECABLEPORT_CODE;

		if (this.startAbstractSchemePortId.equals(startAbstractSchemePortId)) {
			return;
		}
		this.startAbstractSchemePortId = startAbstractSchemePortId;
		super.markAsChanged();
	}

	/**
	 * A wrapper around {@link #setStartAbstractSchemePortId(Identifier)}.
	 *
	 * @param startAbstractSchemePort
	 */
	public void setStartAbstractSchemePort(final AbstractSchemePort startAbstractSchemePort) {
		/*
		 * The two following checks are doubled in the modifier method
		 * for id.
		 */
		if (this.getKind() != SCHEME_ELEMENT) {
			throw new UnsupportedOperationException(OBJECT_STATE_ILLEGAL);
		}
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
		this.setStartAbstractSchemePortId(Identifier.possiblyVoid(startAbstractSchemePort));
	}

	/**
	 * @param transferable
	 * @throws IdlConversionException
	 * @see com.syrus.AMFICOM.general.StorableObject#fromIdlTransferable(IdlStorableObject)
	 */
	@Override
	protected void fromIdlTransferable(final IdlStorableObject transferable)
	throws IdlConversionException {
		synchronized (this) {
			final IdlPathElement pathElement = (IdlPathElement) transferable;
			super.fromIdlTransferable(pathElement);
			this.parentSchemePathId = new Identifier(pathElement.parentSchemePathId);
			this.sequentialNumber = pathElement.sequentialNumber;
			final IdlData data = pathElement.data;
			this.kind = data.discriminator().value();
			switch (this.getKind().value()) {
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
	}

	/**
	 * @param xmlPathElement
	 * @param importType
	 * @throws XmlConversionException
	 * @see XmlTransferableObject#fromXmlTransferable(org.apache.xmlbeans.XmlObject, String)
	 */
	public void fromXmlTransferable(final XmlPathElement xmlPathElement,
			final String importType)
	throws XmlConversionException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param that
	 * @throws ApplicationException
	 */
	public void insertSelfBefore(final PathElement that) throws ApplicationException {
		assert that != null : NON_NULL_EXPECTED;

		if (this == that || this.equals(that)) {
			return;
		}

		final SchemePath parentSchemePath = this.getParentPathOwner();
		assert parentSchemePath.equals(that.getParentSchemePathId());

		final int thatSequentialNumber = that.getSequentialNumber();
		assert this.sequentialNumber != thatSequentialNumber;

		if (thatSequentialNumber - this.sequentialNumber == 1) {
			/*-
			 * This one is already situated immediately before that.
			 */
			return;
		}

		final SortedSet<PathElement> pathElements = parentSchemePath.getPathMembers0();
		if (this.sequentialNumber < thatSequentialNumber) {
			final SortedSet<PathElement> toShiftLeft = pathElements.subSet(this, that);
			toShiftLeft.remove(this);
			for (final PathElement pathElement : toShiftLeft) {
				pathElement.shiftLeft();
			}
			this.sequentialNumber = thatSequentialNumber - 1;
		} else {
			final SortedSet<PathElement> toShiftRight = pathElements.subSet(that, this);
			for (final PathElement pathElement : toShiftRight) {
				pathElement.shiftRight();
			}
			this.sequentialNumber = thatSequentialNumber;
		}
		super.markAsChanged();
	}

	/**
	 * @param that
	 * @throws ApplicationException
	 */
	public void insertSelfAfter(final PathElement that) throws ApplicationException {
		assert that != null : NON_NULL_EXPECTED;

		if (this == that || this.equals(that)) {
			return;
		}

		final SchemePath parentSchemePath = this.getParentPathOwner();
		assert parentSchemePath.equals(that.getParentSchemePathId());

		final int thatSequentialNumber = that.getSequentialNumber();
		assert this.sequentialNumber != thatSequentialNumber;

		if (this.sequentialNumber - thatSequentialNumber == 1) {
			/*-
			 * This one is already situated immediately after that.
			 */
			return;
		}

		final SortedSet<PathElement> pathElements = parentSchemePath.getPathMembers0();
		if (this.sequentialNumber > thatSequentialNumber) {
			final SortedSet<PathElement> toShiftRight = pathElements.subSet(that, this);
			toShiftRight.remove(that);
			for (final PathElement pathElement : toShiftRight) {
				pathElement.shiftRight();
			}
			this.sequentialNumber = thatSequentialNumber + 1;
		} else {
			final SortedSet<PathElement> toShiftLeft = pathElements.subSet(this, that);
			toShiftLeft.remove(this);
			for (final PathElement pathElement : toShiftLeft) {
				pathElement.shiftLeft();
			}
			that.shiftLeft();
			this.sequentialNumber = thatSequentialNumber;
		}
		super.markAsChanged();
	}

	/**
	 * @see com.syrus.AMFICOM.general.StorableObject#getWrapper()
	 */
	@Override
	protected PathElementWrapper getWrapper() {
		return PathElementWrapper.getInstance();
	}

	/*-********************************************************************
	 * Non-model members.                                                 *
	 **********************************************************************/

	private boolean isFirst() {
		return this.sequentialNumber == 0;
	}

	private boolean isLast() {
		try {
			return this.sequentialNumber + 1 == this.getParentPathOwner().getPathMembers().size();
		} catch (final ApplicationException ae) {
			Log.debugMessage(ae, SEVERE);
			return true;
		}
	}

	public boolean isSpacious() {
		switch (this.getKind().value()) {
		case _SCHEME_CABLE_LINK:
		case _SCHEME_LINK:
			return true;
		case _SCHEME_ELEMENT:
		default:
			return false;
		}
	}

	public double getOpticalLength() {
		switch (this.getKind().value()) {
		case _SCHEME_CABLE_LINK:
		case _SCHEME_LINK:
			return ((AbstractSchemeLink) this.getAbstractSchemeElement()).getOpticalLength();
		case _SCHEME_ELEMENT:
		default:
			return 0;
		}
	}

	public void setOpticalLength(final double opticalLength) {
		switch (this.getKind().value()) {
		case _SCHEME_CABLE_LINK:
		case _SCHEME_LINK:
			((AbstractSchemeLink) this.getAbstractSchemeElement()).setOpticalLength(opticalLength);
			break;
		case _SCHEME_ELEMENT:
		default:
			break;
		}
	}

	public double getPhysicalLength() {
		switch (this.getKind().value()) {
		case _SCHEME_CABLE_LINK:
		case _SCHEME_LINK:
			return ((AbstractSchemeLink) this.getAbstractSchemeElement()).getPhysicalLength();
		case _SCHEME_ELEMENT:
		default:
			return 0;
		}
	}

	public void setPhysicalLength(final double physicalLength) {
		switch (this.getKind().value()) {
		case _SCHEME_CABLE_LINK:
		case _SCHEME_LINK:
			((AbstractSchemeLink) this.getAbstractSchemeElement()).setPhysicalLength(physicalLength);
			break;
		case _SCHEME_ELEMENT:
		default:
			break;
		}
	}
}
