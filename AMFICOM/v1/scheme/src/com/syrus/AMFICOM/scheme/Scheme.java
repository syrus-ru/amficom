/*-
 * $Id: Scheme.java,v 1.63 2005/07/31 17:08:10 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme;

import static com.syrus.AMFICOM.general.ErrorMessages.NON_EMPTY_EXPECTED;
import static com.syrus.AMFICOM.general.ErrorMessages.NON_NULL_EXPECTED;
import static com.syrus.AMFICOM.general.ErrorMessages.NON_VOID_EXPECTED;
import static com.syrus.AMFICOM.general.ErrorMessages.OBJECT_NOT_INITIALIZED;
import static com.syrus.AMFICOM.general.ErrorMessages.REMOVAL_OF_AN_ABSENT_PROHIBITED;
import static com.syrus.AMFICOM.general.Identifier.VOID_IDENTIFIER;
import static com.syrus.AMFICOM.general.ObjectEntities.IMAGERESOURCE_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.MAP_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SCHEMECABLELINK_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SCHEMEELEMENT_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SCHEMELINK_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SCHEMEMONITORINGSOLUTION_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SCHEMEOPTIMIZEINFO_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SCHEME_CODE;
import static com.syrus.AMFICOM.scheme.corba.IdlSchemePackage.IdlKind.CABLE_SUBNETWORK;
import static java.util.logging.Level.SEVERE;

import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.administration.AbstractCloneableDomainMember;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CompoundCondition;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.DatabaseContext;
import com.syrus.AMFICOM.general.Describable;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.general.TypicalCondition;
import com.syrus.AMFICOM.general.corba.IdlStorableObject;
import com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlCompoundConditionPackage.CompoundConditionSort;
import com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlTypicalConditionPackage.OperationSort;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.resource.BitmapImageResource;
import com.syrus.AMFICOM.resource.SchemeImageResource;
import com.syrus.AMFICOM.scheme.corba.IdlScheme;
import com.syrus.AMFICOM.scheme.corba.IdlSchemeHelper;
import com.syrus.AMFICOM.scheme.corba.IdlSchemePackage.IdlKind;
import com.syrus.util.Log;

/**
 * #03 in hierarchy.
 *
 * @author $Author: bass $
 * @version $Revision: 1.63 $, $Date: 2005/07/31 17:08:10 $
 * @module scheme
 * @todo Possibly join (add|remove)Scheme(Element|Link|CableLink).
 */
public final class Scheme extends AbstractCloneableDomainMember implements Describable, SchemeCellContainer {
	private static final long serialVersionUID = 3257289136389173298L;

	private static final int DEFAULT_WIDTH = 840;

	private static final int DEFAULT_HEIGHT = 1190;

	private String name;

	private String description;

	private String label;

	private int width;

	private int height;

	private IdlKind kind;

	private Identifier mapId;

	private Identifier symbolId;

	private Identifier ugoCellId;

	private Identifier schemeCellId;

	Identifier parentSchemeElementId;

	/*-********************************************************************
	 * Conditions used to implement #getCurrentSchemeMonitoringSolution() *
	 **********************************************************************/

	private transient TypicalCondition currentSolutionTypicalCondition;

	private transient LinkedIdsCondition currentSolutionLinkedIdsCondition;

	private transient CompoundCondition currentSolutionCompoundCondition0; 

	private transient CompoundCondition currentSolutionCompoundCondition1; 

	/**
	 * @param id
	 * @throws RetrieveObjectException
	 * @throws ObjectNotFoundException
	 */
	Scheme(final Identifier id) throws RetrieveObjectException, ObjectNotFoundException {
		super(id);

		try {
			DatabaseContext.getDatabase(SCHEME_CODE).retrieve(this);
		} catch (final IllegalDataException ide) {
			throw new RetrieveObjectException(ide.getMessage(), ide);
		}
	}

	/**
	 * @param id
	 * @param created
	 * @param modified
	 * @param creatorId
	 * @param modifierId
	 * @param version
	 * @param domainId
	 * @param name
	 * @param description
	 * @param label
	 * @param width
	 * @param height
	 * @param kind
	 * @param map
	 * @param symbol
	 * @param ugoCell
	 * @param schemeCell
	 * @param parentSchemeElement
	 */
	Scheme(final Identifier id,
			final Date created,
			final Date modified,
			final Identifier creatorId,
			final Identifier modifierId,
			final StorableObjectVersion version,
			final Identifier domainId,
			final String name,
			final String description,
			final String label,
			final int width,
			final int height,
			final IdlKind kind,
			final Map map,
			final BitmapImageResource symbol,
			final SchemeImageResource ugoCell,
			final SchemeImageResource schemeCell,
			final SchemeElement parentSchemeElement) {
		super(id, created, modified, creatorId, modifierId, version, domainId);
		this.name = name;
		this.description = description;
		this.label = label;
		this.width = width;
		this.height = height;
		this.kind = kind;
		this.mapId = Identifier.possiblyVoid(map);
		this.symbolId = Identifier.possiblyVoid(symbol);
		this.ugoCellId = Identifier.possiblyVoid(ugoCell);
		this.schemeCellId = Identifier.possiblyVoid(schemeCell);
		this.parentSchemeElementId = Identifier.possiblyVoid(parentSchemeElement);
	}

	/**
	 * @param transferable
	 */
	public Scheme(final IdlScheme transferable) {
		fromTransferable(transferable);
	}

	/**
	 * A shorthand for
	 * {@link #createInstance(Identifier, String, String, String, int, int, IdlKind, Identifier, Map, BitmapImageResource, SchemeImageResource, SchemeImageResource, SchemeElement)}.
	 *
	 * @param creatorId
	 * @param name
	 * @param kind
	 * @param domainId
	 * @throws CreateObjectException
	 */
	public static Scheme createInstance(final Identifier creatorId,
			final String name, final IdlKind kind,
			final Identifier domainId)
			throws CreateObjectException {
		return createInstance(creatorId, name, "", "", DEFAULT_WIDTH,
				DEFAULT_HEIGHT, kind, domainId, null, null,
				null, null, null);
	}

	/**
	 * @param creatorId
	 * @param name can be neither <code>null</code> nor empty.
	 * @param description cannot be <code>null</code>, but can be empty.
	 * @param label cannot be <code>null</code>, but can be empty.
	 * @param width
	 * @param height
	 * @param kind
	 * @param domainId
	 * @param map
	 * @param symbol
	 * @param ugoCell
	 * @param schemeCell
	 * @param parentSchemeElement
	 * @throws CreateObjectException
	 */
	public static Scheme createInstance(final Identifier creatorId,
			final String name,
			final String description,
			final String label,
			final int width,
			final int height,
			final IdlKind kind,
			final Identifier domainId,
			final Map map,
			final BitmapImageResource symbol,
			final SchemeImageResource ugoCell,
			final SchemeImageResource schemeCell,
			final SchemeElement parentSchemeElement) throws CreateObjectException {
		assert creatorId != null && !creatorId.isVoid(): NON_VOID_EXPECTED;
		assert name != null && name.length() != 0: NON_EMPTY_EXPECTED;
		assert description != null: NON_NULL_EXPECTED;
		assert label != null: NON_NULL_EXPECTED;
		assert domainId != null && !domainId.isVoid(): NON_VOID_EXPECTED;
		assert kind != null: NON_NULL_EXPECTED;

		try {
			final Date created = new Date();
			final Scheme scheme = new Scheme(IdentifierPool.getGeneratedIdentifier(SCHEME_CODE),
					created,
					created,
					creatorId,
					creatorId,
					StorableObjectVersion.createInitial(),
					domainId,
					name,
					description,
					label,
					width,
					height,
					kind,
					map,
					symbol,
					ugoCell,
					schemeCell,
					parentSchemeElement);
			scheme.markAsChanged();
			return scheme;
		} catch (final IdentifierGenerationException ige) {
			throw new CreateObjectException(
					"Scheme.createInstance | cannot generate identifier ", ige);
		}
	}

	/**
	 * @param schemeCableLink cannot be <code>null</code>.
	 */
	public void addSchemeCableLink(final SchemeCableLink schemeCableLink) {
		assert schemeCableLink != null: NON_NULL_EXPECTED;
		schemeCableLink.setParentScheme(this);
	}

	/**
	 * @param schemeElement cannot be <code>null</code>.
	 */
	public void addSchemeElement(final SchemeElement schemeElement) {
		assert schemeElement != null: NON_NULL_EXPECTED;
		schemeElement.setParentScheme(this);
	}

	/**
	 * @param schemeLink cannot be <code>null</code>.
	 */
	public void addSchemeLink(final SchemeLink schemeLink) {
		assert schemeLink != null: NON_NULL_EXPECTED;
		schemeLink.setParentScheme(this);
	}

	/**
	 * @param schemeOptimizeInfo cannot be <code>null</code>.
	 */
	public void addSchemeOptimizeInfo(final SchemeOptimizeInfo schemeOptimizeInfo) {
		assert schemeOptimizeInfo != null: NON_NULL_EXPECTED;
		schemeOptimizeInfo.setParentScheme(this);
	}

	public void addSchemeMonitoringSolution(final SchemeMonitoringSolution schemeMonitoringSolution) {
		assert schemeMonitoringSolution != null : NON_NULL_EXPECTED;
		schemeMonitoringSolution.setParentScheme(this);
	}

	@Override
	public Scheme clone() throws CloneNotSupportedException {
		final Scheme scheme = (Scheme) super.clone();
		/**
		 * @todo Update the newly created object.
		 */
		return scheme;
	}

	public SchemeMonitoringSolution getCurrentSchemeMonitoringSolution() {
		try {
			final Set<SchemeMonitoringSolution> schemeMonitoringSolutions =
				new HashSet<SchemeMonitoringSolution>();

			/*
			 * Common initialization.
			 */
			if (this.currentSolutionTypicalCondition == null) {
				this.currentSolutionTypicalCondition =
						new TypicalCondition(
								Boolean.TRUE,
								OperationSort.OPERATION_EQUALS,
								SCHEMEMONITORINGSOLUTION_CODE,
								SchemeMonitoringSolutionWrapper.COLUMN_ACTIVE);
			}
			/*
			 * Search type 1.
			 */
			if (this.currentSolutionCompoundCondition0 == null) {
				this.currentSolutionCompoundCondition0 = new CompoundCondition(
						this.currentSolutionTypicalCondition,
						CompoundConditionSort.AND,
						new LinkedIdsCondition(
								this.id,
								SCHEMEMONITORINGSOLUTION_CODE));
			}

			final Set<SchemeMonitoringSolution> schemeMonitoringSolutions0 =
					StorableObjectPool.getStorableObjectsByCondition(this.currentSolutionCompoundCondition0, true);
			schemeMonitoringSolutions.addAll(schemeMonitoringSolutions0);

			/*
			 * Search type 2.
			 */
			for (final SchemeOptimizeInfo schemeOptimizeInfo : this.getSchemeOptimizeInfos()) {
				final Identifier schemeOptimizeInfoId = schemeOptimizeInfo.getId();
				if (this.currentSolutionLinkedIdsCondition == null) {
					this.currentSolutionLinkedIdsCondition = new LinkedIdsCondition(schemeOptimizeInfoId, SCHEMEMONITORINGSOLUTION_CODE);
				} else {
					this.currentSolutionLinkedIdsCondition.setLinkedId(schemeOptimizeInfoId);
				}
				if (this.currentSolutionCompoundCondition1 == null) {
					this.currentSolutionCompoundCondition1 = new CompoundCondition(
							this.currentSolutionTypicalCondition,
							CompoundConditionSort.AND,
							this.currentSolutionLinkedIdsCondition);
				}
				final Set<SchemeMonitoringSolution> schemeMonitoringSolutions1 =
						StorableObjectPool.getStorableObjectsByCondition(this.currentSolutionCompoundCondition1, true);
				schemeMonitoringSolutions.addAll(schemeMonitoringSolutions1);
			}

			/*
			 * Sanity checks.
			 */
			assert this.getSchemeMonitoringSolutionsRecursively().isEmpty()
					? schemeMonitoringSolutions.isEmpty()
					: schemeMonitoringSolutions.size() == 1;
			/*
			 * Return the first entry or null if empty.
			 */
			for (final SchemeMonitoringSolution schemeMonitoringSolution : schemeMonitoringSolutions) {
				return schemeMonitoringSolution;
			}
			return null;
		} catch (final ApplicationException ae) {
			Log.debugException(ae, SEVERE);
			return null;
		}
	}

	/**
	 * @see com.syrus.AMFICOM.general.StorableObject#getDependencies()
	 */
	@Override
	public Set<Identifiable> getDependencies() {
		assert this.mapId != null
				&& this.symbolId != null
				&& this.ugoCellId != null
				&& this.schemeCellId != null
				&& this.parentSchemeElementId != null: OBJECT_NOT_INITIALIZED;
		final Set<Identifiable> dependencies = new HashSet<Identifiable>();
		dependencies.add(this.mapId);
		dependencies.add(this.symbolId);
		dependencies.add(this.ugoCellId);
		dependencies.add(this.schemeCellId);
		dependencies.add(this.parentSchemeElementId);
		dependencies.remove(null);
		dependencies.remove(VOID_IDENTIFIER);
		return Collections.unmodifiableSet(dependencies);
	}

	/**
	 * @see Describable#getDescription()
	 */
	public String getDescription() {
		assert this.description != null : OBJECT_NOT_INITIALIZED;
		return this.description;
	}

	public int getHeight() {
		return this.height;
	}

	/**
	 * @return this <code>Scheme</code>&apos;s label, or
	 *         empty string if none. Never returns <code>null</code>s.
	 */
	public String getLabel() {
		assert this.label != null: OBJECT_NOT_INITIALIZED;
		return this.label;
	}

	Identifier getMapId() {
		assert this.mapId != null: OBJECT_NOT_INITIALIZED;
		assert this.mapId.isVoid() || this.mapId.getMajor() == MAP_CODE;
		return this.mapId;
	}

	/**
	 * A wrapper around {@link #getMapId()}.
	 */
	public Map getMap() {
		try {
			return (Map) StorableObjectPool.getStorableObject(this.getMapId(), true);
		} catch (final ApplicationException ae) {
			Log.debugException(ae, SEVERE);
			return null;
		}
	}

	/**
	 * @see com.syrus.AMFICOM.general.Namable#getName()
	 */
	public String getName() {
		assert this.name != null && this.name.length() != 0 : OBJECT_NOT_INITIALIZED;
		return this.name;
	}

	Identifier getParentSchemeElementId() {
		assert this.parentSchemeElementId != null : OBJECT_NOT_INITIALIZED;
		assert this.parentSchemeElementId.isVoid() || this.parentSchemeElementId.getMajor() == SCHEMEELEMENT_CODE;
		return this.parentSchemeElementId;
	}

	/**
	 * A wrapper around {@link #getParentSchemeElementId()}.
	 */
	public SchemeElement getParentSchemeElement() {
		try {
			return (SchemeElement) StorableObjectPool.getStorableObject(this.getParentSchemeElementId(), true);
		} catch (final ApplicationException ae) {
			Log.debugException(ae, SEVERE);
			return null;
		}
	}

	public Set<SchemeCableLink> getSchemeCableLinks() {
		try {
			return Collections.unmodifiableSet(this.getSchemeCableLinks0());
		} catch (final ApplicationException ae) {
			Log.debugException(ae, SEVERE);
			return Collections.emptySet();
		}
	}

	private Set<SchemeCableLink> getSchemeCableLinks0() throws ApplicationException {
			return StorableObjectPool.getStorableObjectsByCondition(new LinkedIdsCondition(this.id, SCHEMECABLELINK_CODE), true);
	}

	Identifier getSchemeCellId() {
		assert this.schemeCellId != null: OBJECT_NOT_INITIALIZED;
		assert this.schemeCellId.isVoid() || this.schemeCellId.getMajor() == IMAGERESOURCE_CODE;
		return this.schemeCellId;
	}

	/**
	 * A wrapper around {@link #getSchemeCellId()}.
	 *
	 * @see SchemeCellContainer#getSchemeCell()
	 */
	public SchemeImageResource getSchemeCell() {
		try {
			return (SchemeImageResource) StorableObjectPool.getStorableObject(this.getSchemeCellId(), true);
		} catch (final ApplicationException ae) {
			Log.debugException(ae, SEVERE);
			return null;
		}
	}

	public Set<SchemeElement> getSchemeElements() {
		try {
			return Collections.unmodifiableSet(this.getSchemeElements0());
		} catch (final ApplicationException ae) {
			Log.debugException(ae, SEVERE);
			return Collections.emptySet();
		}
	}

	private Set<SchemeElement> getSchemeElements0() throws ApplicationException {
		return StorableObjectPool.getStorableObjectsByCondition(new LinkedIdsCondition(this.id, SCHEMEELEMENT_CODE), true);
	}

	public IdlKind getKind() {
		assert this.kind != null: OBJECT_NOT_INITIALIZED;
		return this.kind;
	}

	public Set<SchemeLink> getSchemeLinks() {
		try {
			return Collections.unmodifiableSet(this.getSchemeLinks0());
		} catch (final ApplicationException ae) {
			Log.debugException(ae, SEVERE);
			return Collections.emptySet();
		}
	}

	private Set<SchemeLink> getSchemeLinks0() throws ApplicationException {
		return StorableObjectPool.getStorableObjectsByCondition(new LinkedIdsCondition(this.id, SCHEMELINK_CODE), true);
	}

	public Set<SchemeOptimizeInfo> getSchemeOptimizeInfos() {
		try {
			return Collections.unmodifiableSet(this.getSchemeOptimizeInfos0());
		} catch (final ApplicationException ae) {
			Log.debugException(ae, SEVERE);
			return Collections.emptySet();
		}
	}

	private Set<SchemeOptimizeInfo> getSchemeOptimizeInfos0() throws ApplicationException {
		return StorableObjectPool.getStorableObjectsByCondition(new LinkedIdsCondition(this.id, SCHEMEOPTIMIZEINFO_CODE), true);
	}

	/**
	 * Returns a {@code Set} of {@code SchemeMonitoringSolution}s
	 * <em>directly</em> referencing this {@code Scheme}, i. e. having
	 * <em>no</em> parent {@code SchemeOptimizeInfo}. To get the
	 * <em>entire</em> {@code Set} of {@code SchemeMonitoringSolution}s
	 * that belong to this {@code Scheme}, use
	 * {@link #getSchemeMonitoringSolutionsRecursively()} instead.
	 * 
	 * @see #getSchemeMonitoringSolutionsRecursively()
	 */
	public Set<SchemeMonitoringSolution> getSchemeMonitoringSolutions() {
		try {
			return Collections.unmodifiableSet(this.getSchemeMonitoringSolutions0());
		} catch (final ApplicationException ae) {
			Log.debugException(ae, SEVERE);
			return Collections.emptySet();
		}
	}

	private Set<SchemeMonitoringSolution> getSchemeMonitoringSolutions0() throws ApplicationException {
		return StorableObjectPool.getStorableObjectsByCondition(new LinkedIdsCondition(super.id, SCHEMEMONITORINGSOLUTION_CODE), true);
	}

	Identifier getSymbolId() {
		assert this.symbolId != null: OBJECT_NOT_INITIALIZED;
		assert this.symbolId.isVoid() || this.symbolId.getMajor() == IMAGERESOURCE_CODE;
		return this.symbolId;
	}

	/**
	 * A wrapper around {@link #getSymbolId()}.
	 *
	 * @see SchemeSymbolContainer#getSymbol()
	 */
	public BitmapImageResource getSymbol() {
		try {
			return (BitmapImageResource) StorableObjectPool.getStorableObject(this.getSymbolId(), true);
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
	public IdlScheme getTransferable(final ORB orb) {
		/*
		 * domainId is assumed to be non-null.
		 */
		return IdlSchemeHelper.init(orb,
				this.id.getTransferable(),
				this.created.getTime(),
				this.modified.getTime(),
				this.creatorId.getTransferable(),
				this.modifierId.getTransferable(),
				this.version.longValue(),
				this.name, this.description, this.label,
				this.width, this.height, this.kind,
				super.getDomainId().getTransferable(),
				this.mapId.getTransferable(),
				this.symbolId.getTransferable(),
				this.ugoCellId.getTransferable(),
				this.schemeCellId.getTransferable(),
				this.parentSchemeElementId.getTransferable());
	}

	Identifier getUgoCellId() {
		assert this.ugoCellId != null: OBJECT_NOT_INITIALIZED;
		assert this.ugoCellId.isVoid() || this.ugoCellId.getMajor() == IMAGERESOURCE_CODE;
		return this.ugoCellId;
	}

	/**
	 * A wrapper around {@link #getUgoCellId()}.
	 *
	 * @see SchemeCellContainer#getUgoCell()
	 */
	public SchemeImageResource getUgoCell() {
		try {
			return (SchemeImageResource) StorableObjectPool.getStorableObject(this.getUgoCellId(), true);
		} catch (final ApplicationException ae) {
			Log.debugException(ae, SEVERE);
			return null;
		}
	}

	public int getWidth() {
		return this.width;
	}

	/**
	 * The <code>SchemeCableLink</code> must belong to this
	 * <code>Scheme</code>, or crap will meet the fan.
	 *
	 * @param schemeCableLink
	 */
	public void removeSchemeCableLink(final SchemeCableLink schemeCableLink) {
		assert schemeCableLink != null: NON_NULL_EXPECTED;
		assert schemeCableLink.getParentSchemeId().equals(super.id) : REMOVAL_OF_AN_ABSENT_PROHIBITED;
		schemeCableLink.setParentScheme(null);
	}

	/**
	 * The <code>SchemeElement</code> must belong to this
	 * <code>Scheme</code>, or crap will meet the fan.
	 *
	 * @param schemeElement
	 */
	public void removeSchemeElement(final SchemeElement schemeElement) {
		assert schemeElement != null: NON_NULL_EXPECTED;
		assert schemeElement.getParentSchemeId().equals(super.id) : REMOVAL_OF_AN_ABSENT_PROHIBITED;
		schemeElement.setParentScheme(null);
	}

	/**
	 * The <code>SchemeLink</code> must belong to this <code>Scheme</code>,
	 * or crap will meet the fan.
	 *
	 * @param schemeLink
	 */
	public void removeSchemeLink(final SchemeLink schemeLink) {
		assert schemeLink != null: NON_NULL_EXPECTED;
		assert schemeLink.getParentSchemeId().equals(super.id) : REMOVAL_OF_AN_ABSENT_PROHIBITED;
		schemeLink.setParentScheme(null);
	}

	/**
	 * The {@code SchemeOptimizeInfo} must belong to this {@code Scheme}, or
	 * crap will meet the fan.
	 *
	 * @param schemeOptimizeInfo
	 */
	public void removeSchemeOptimizeInfo(final SchemeOptimizeInfo schemeOptimizeInfo) {
		assert schemeOptimizeInfo != null: NON_NULL_EXPECTED;
		assert schemeOptimizeInfo.getParentSchemeId().equals(super.id) : REMOVAL_OF_AN_ABSENT_PROHIBITED;
		schemeOptimizeInfo.setParentScheme(null);
	}

	/**
	 * The {@code SchemeMonitoringSolution} must belong to this
	 * {@code Scheme}, or crap will meet the fan.
	 *
	 * @param schemeMonitoringSolution
	 */
	public void removeSchemeMonitoringSolution(final SchemeMonitoringSolution schemeMonitoringSolution) {
		assert schemeMonitoringSolution != null : NON_NULL_EXPECTED;
		assert schemeMonitoringSolution.getParentSchemeId().equals(super.id) : REMOVAL_OF_AN_ABSENT_PROHIBITED;
		schemeMonitoringSolution.setParentScheme(null);
	}

	/**
	 * @param created
	 * @param modified
	 * @param creatorId
	 * @param modifierId
	 * @param version
	 * @param name
	 * @param description
	 * @param label
	 * @param width
	 * @param height
	 * @param kind
	 * @param domainId
	 * @param mapId
	 * @param symbolId
	 * @param ugoCellId
	 * @param schemeCellId
	 * @param parentSchemeElementId
	 */
	synchronized void setAttributes(final Date created,
			final Date modified,
			final Identifier creatorId,
			final Identifier modifierId,
			final StorableObjectVersion version,
			final String name,
			final String description,
			final String label,
			final int width,
			final int height,
			final IdlKind kind,
			final Identifier domainId,
			final Identifier mapId,
			final Identifier symbolId,
			final Identifier ugoCellId,
			final Identifier schemeCellId,
			final Identifier parentSchemeElementId) {
		super.setAttributes(created, modified, creatorId, modifierId, version, domainId);

		assert name != null && name.length() != 0: NON_EMPTY_EXPECTED;
		assert description != null: NON_NULL_EXPECTED;
		assert label != null: NON_NULL_EXPECTED;
		assert kind != null: NON_NULL_EXPECTED;
		assert mapId != null: NON_NULL_EXPECTED;
		assert symbolId != null: NON_NULL_EXPECTED;
		assert ugoCellId != null: NON_NULL_EXPECTED;
		assert schemeCellId != null: NON_NULL_EXPECTED;
		assert parentSchemeElementId != null: NON_NULL_EXPECTED;

		this.name = name;
		this.description = description;
		this.label = label;
		this.width = width;
		this.height = height;
		this.kind = kind;
		this.mapId = mapId;
		this.symbolId = symbolId;
		this.ugoCellId = ugoCellId;
		this.schemeCellId = schemeCellId;
		this.parentSchemeElementId = parentSchemeElementId;
	}

	public void setCurrentSchemeMonitoringSolution(final SchemeMonitoringSolution currentSchemeMonitoringSolution) {
		assert currentSchemeMonitoringSolution != null : NON_NULL_EXPECTED;
		/*
		 * Not ...getParentSchemeId(), since we'll miss solutions with
		 * non-null parent optimizeInfo. Here getParentSchemeId() may
		 * eventually return a void identifier, while getParentScheme()
		 * will never return null, unless this object is deleted, which
		 * is unlikely.
		 */
		assert currentSchemeMonitoringSolution.getParentScheme().getId().equals(super.id);
		currentSchemeMonitoringSolution.setActive(true);
	}

	/**
	 * @see Describable#setDescription(String)
	 */
	public void setDescription(final String description) {
		assert this.description != null : OBJECT_NOT_INITIALIZED;
		assert description != null : NON_NULL_EXPECTED;
		if (this.description.equals(description))
			return;
		this.description = description;
		super.markAsChanged();
	}

	public void setHeight(final int height) {
		if (this.height == height)
			return;
		this.height = height;
		super.markAsChanged();
	}

	/**
	 * @param label cannot be <code>null</code>. For this purpose, supply
	 *        an empty string as an argument.
	 */
	public void setLabel(final String label) {
		assert this.label != null: OBJECT_NOT_INITIALIZED;
		assert label != null: NON_NULL_EXPECTED;
		if (this.label.equals(label))
			return;
		this.label = label;
		super.markAsChanged();
	}

	public void setMap(final Map map) {
		final Identifier newMapId = Identifier.possiblyVoid(map);
		if (this.mapId.equals(newMapId))
			return;
		this.mapId = newMapId;
		super.markAsChanged();
	}

	/**
	 * @see com.syrus.AMFICOM.general.Namable#setName(String)
	 */
	public void setName(final String name) {
		assert this.name != null && this.name.length() != 0 : OBJECT_NOT_INITIALIZED;
		assert name != null && name.length() != 0 : NON_EMPTY_EXPECTED;
		if (this.name.equals(name))
			return;
		this.name = name;
		super.markAsChanged();
	}

	public void setParentSchemeElement(final SchemeElement parentSchemeElement) {
		final Identifier newParentSchemeElementId = Identifier.possiblyVoid(parentSchemeElement);
		if (this.parentSchemeElementId.equals(newParentSchemeElementId))
			return;
		this.parentSchemeElementId = newParentSchemeElementId;
		super.markAsChanged();
	}

	public void setSchemeCableLinks(final Set<SchemeCableLink> schemeCableLinks) throws ApplicationException {
		assert schemeCableLinks != null: NON_NULL_EXPECTED;
		final Set<SchemeCableLink> oldSchemeCableLinks = this.getSchemeCableLinks0();
		/*
		 * Check is made to prevent SchemeCableLinks from
		 * permanently losing their parents.
		 */
		oldSchemeCableLinks.removeAll(schemeCableLinks);
		for (final SchemeCableLink oldSchemeCableLink : oldSchemeCableLinks) {
			this.removeSchemeCableLink(oldSchemeCableLink);
		}
		for (final SchemeCableLink schemeCableLink : schemeCableLinks) {
			this.addSchemeCableLink(schemeCableLink);
		}
	}

	/**
	 * @param schemeCell
	 * @see SchemeCellContainer#setSchemeCell(SchemeImageResource)
	 */
	public void setSchemeCell(final SchemeImageResource schemeCell) {
		final Identifier newSchemeCellId = Identifier.possiblyVoid(schemeCell);
		if (this.schemeCellId.equals(newSchemeCellId))
			return;
		this.schemeCellId = newSchemeCellId;
		super.markAsChanged();
	}

	public void setSchemeElements(final Set<SchemeElement> schemeElements) throws ApplicationException {
		assert schemeElements != null: NON_NULL_EXPECTED;
		final Set<SchemeElement> oldSchemeElements = this.getSchemeElements0();
		/*
		 * Check is made to prevent SchemeElements from
		 * permanently losing their parents.
		 */
		oldSchemeElements.removeAll(schemeElements);
		for (final SchemeElement oldSchemeElement : oldSchemeElements) {
			this.removeSchemeElement(oldSchemeElement);
		}
		for (final SchemeElement schemeElement : schemeElements) {
			this.addSchemeElement(schemeElement);
		}
	}

	/**
	 * @param kind
	 */
	public void setKind(final IdlKind kind) {
		assert this.kind != null: OBJECT_NOT_INITIALIZED;
		assert kind != null: NON_NULL_EXPECTED;
		if (this.kind.value() == kind.value())
			return;
		this.kind = kind;
		super.markAsChanged();
	}

	public void setSchemeLinks(final Set<SchemeLink> schemeLinks) throws ApplicationException {
		assert schemeLinks != null: NON_NULL_EXPECTED;
		final Set<SchemeLink> oldSchemeLinks = this.getSchemeLinks0();
		/*
		 * Check is made to prevent SchemeLinks from
		 * permanently losing their parents.
		 */
		oldSchemeLinks.removeAll(schemeLinks);
		for (final SchemeLink oldSchemeLink : oldSchemeLinks) {
			this.removeSchemeLink(oldSchemeLink);
		}
		for (final SchemeLink schemeLink : schemeLinks) {
			this.addSchemeLink(schemeLink);
		}
	}

	public void setSchemeOptimizeInfos(final Set<SchemeOptimizeInfo> schemeOptimizeInfos) throws ApplicationException {
		assert schemeOptimizeInfos != null: NON_NULL_EXPECTED;
		final Set<SchemeOptimizeInfo> oldSchemeOptimizeInfos = this.getSchemeOptimizeInfos0();
		/*
		 * Check is made to prevent SchemeOptimizeInfos from
		 * permanently losing their parents.
		 */
		oldSchemeOptimizeInfos.removeAll(schemeOptimizeInfos);
		for (final SchemeOptimizeInfo oldSchemeOptimizeInfo : oldSchemeOptimizeInfos) {
			this.removeSchemeOptimizeInfo(oldSchemeOptimizeInfo);
		}
		for (final SchemeOptimizeInfo schemeOptimizeInfo : schemeOptimizeInfos) {
			this.addSchemeOptimizeInfo(schemeOptimizeInfo);
		}
	}

	public void setSchemeMonitoringSolutions(final Set<SchemeMonitoringSolution> schemeMonitoringSolutions) throws ApplicationException {
		assert schemeMonitoringSolutions != null : NON_NULL_EXPECTED;
		final Set<SchemeMonitoringSolution> oldSchemeMonitoringSolutions = this.getSchemeMonitoringSolutions0();
		/*
		 * Check is made to prevent SchemeMonitoringSolutions from
		 * permanently losing their parents.
		 */
		oldSchemeMonitoringSolutions.removeAll(schemeMonitoringSolutions);
		for (final SchemeMonitoringSolution oldSchemeMonitoringSolution : oldSchemeMonitoringSolutions) {
			this.removeSchemeMonitoringSolution(oldSchemeMonitoringSolution);
		}
		for (final SchemeMonitoringSolution schemeMonitoringSolution : schemeMonitoringSolutions) {
			this.addSchemeMonitoringSolution(schemeMonitoringSolution);
		}
	}

	/**
	 * @param symbol
	 * @see SchemeSymbolContainer#setSymbol(BitmapImageResource)
	 */
	public void setSymbol(final BitmapImageResource symbol) {
		final Identifier newSymbolId = Identifier.possiblyVoid(symbol);
		if (this.symbolId.equals(newSymbolId))
			return;
		this.symbolId = newSymbolId;
		super.markAsChanged();
	}

	/**
	 * @param ugoCell
	 * @see SchemeCellContainer#setUgoCell(SchemeImageResource)
	 */
	public void setUgoCell(final SchemeImageResource ugoCell) {
		final Identifier newUgoCellId = Identifier.possiblyVoid(ugoCell);
		if (this.ugoCellId.equals(newUgoCellId))
			return;
		this.ugoCellId = newUgoCellId;
		super.markAsChanged();
	}

	public void setWidth(final int width) {
		if (this.width == width)
			return;
		this.width = width;
		super.markAsChanged();
	}

	/**
	 * @param transferable
	 * @see com.syrus.AMFICOM.general.StorableObject#fromTransferable(IdlStorableObject)
	 */
	@Override
	protected void fromTransferable(final IdlStorableObject transferable) {
		final IdlScheme scheme = (IdlScheme) transferable;
		super.fromTransferable(scheme, new Identifier(scheme.domainId));
		this.name = scheme.name;
		this.description = scheme.description;
		this.label = scheme.label;
		this.width = scheme.width;
		this.height = scheme.height;
		this.kind = scheme.kind;
		this.mapId = new Identifier(scheme.mapId);
		this.symbolId = new Identifier(scheme.symbolId);
		this.ugoCellId = new Identifier(scheme.ugoCellId);
		this.schemeCellId = new Identifier(scheme.schemeCellId);
		this.parentSchemeElementId = new Identifier(scheme.parentSchemeElementId);
	}

	/*-********************************************************************
	 * Non-model members.                                                 *
	 **********************************************************************/

	public Set<SchemeMonitoringSolution> getSchemeMonitoringSolutionsRecursively() {
		return Collections.unmodifiableSet(this.getSchemeMonitoringSolutionsRecursively0());
	}

	Set<SchemeMonitoringSolution> getSchemeMonitoringSolutionsRecursively0() {
		final Set<SchemeMonitoringSolution> schemeMonitoringSolutions =
				new HashSet<SchemeMonitoringSolution>();
		schemeMonitoringSolutions.addAll(this.getSchemeMonitoringSolutions());
		for (final SchemeOptimizeInfo schemeOptimizeInfo : this.getSchemeOptimizeInfos()) {
			schemeMonitoringSolutions.addAll(schemeOptimizeInfo.getSchemeMonitoringSolutions());
		}
		return schemeMonitoringSolutions;
	}

	/**
	 * To get the {@code Set} of {@code SchemePath}s for the current
	 * {@code SchemeMonitoringSolution} only, use
	 * {@link #getCurrentSchemeMonitoringSolution()}.{@link SchemeMonitoringSolution#getSchemePaths() getSchemePaths()}
	 * instead.
	 */
	public Set<SchemePath> getSchemePathsRecursively() {
		final Set<SchemePath> schemePaths = new HashSet<SchemePath>();
		for (final SchemeMonitoringSolution schemeMonitoringSolution : this.getSchemeMonitoringSolutionsRecursively()) {
			schemePaths.addAll(schemeMonitoringSolution.getSchemePaths());
		}
		return Collections.unmodifiableSet(schemePaths);
	}

	public Set<SchemePath> getTopologicalPaths() {
		final Set<SchemePath> schemePaths = new HashSet<SchemePath>(this.getCurrentSchemeMonitoringSolution().getSchemePaths());
		for (final SchemeElement schemeElement : this.getSchemeElements()) {
			for (final Scheme scheme : schemeElement.getSchemes()) {
				if (scheme.getKind() == CABLE_SUBNETWORK) {
					for (final SchemePath schemePath : scheme.getTopologicalPaths()) {
						schemePaths.add(schemePath);
					}
				}
			}
		}
		return Collections.unmodifiableSet(schemePaths);
	}
}
