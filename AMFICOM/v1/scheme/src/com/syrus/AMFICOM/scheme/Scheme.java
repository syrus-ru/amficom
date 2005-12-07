/*-
 * $Id: Scheme.java,v 1.124 2005/12/07 17:17:20 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme;

import static com.syrus.AMFICOM.general.ErrorMessages.NON_EMPTY_EXPECTED;
import static com.syrus.AMFICOM.general.ErrorMessages.NON_NULL_EXPECTED;
import static com.syrus.AMFICOM.general.ErrorMessages.NON_VOID_EXPECTED;
import static com.syrus.AMFICOM.general.ErrorMessages.OBJECT_BADLY_INITIALIZED;
import static com.syrus.AMFICOM.general.ErrorMessages.OBJECT_NOT_INITIALIZED;
import static com.syrus.AMFICOM.general.ErrorMessages.REMOVAL_OF_AN_ABSENT_PROHIBITED;
import static com.syrus.AMFICOM.general.ErrorMessages.XML_BEAN_NOT_COMPLETE;
import static com.syrus.AMFICOM.general.Identifier.VOID_IDENTIFIER;
import static com.syrus.AMFICOM.general.Identifier.XmlConversionMode.MODE_RETURN_VOID_IF_ABSENT;
import static com.syrus.AMFICOM.general.Identifier.XmlConversionMode.MODE_THROW_IF_ABSENT;
import static com.syrus.AMFICOM.general.ObjectEntities.IMAGERESOURCE_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.MAP_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SCHEMECABLELINK_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SCHEMEELEMENT_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SCHEMELINK_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SCHEMEMONITORINGSOLUTION_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SCHEMEOPTIMIZEINFO_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SCHEME_CODE;
import static com.syrus.AMFICOM.general.XmlComplementor.ComplementationMode.EXPORT;
import static com.syrus.AMFICOM.general.XmlComplementor.ComplementationMode.POST_IMPORT;
import static com.syrus.AMFICOM.general.XmlComplementor.ComplementationMode.PRE_IMPORT;
import static com.syrus.AMFICOM.scheme.corba.IdlSchemeElementPackage.IdlSchemeElementKind.SCHEME_ELEMENT_CONTAINER;
import static com.syrus.AMFICOM.scheme.corba.IdlSchemePackage.IdlKind.CABLE_SUBNETWORK;
import static java.util.logging.Level.SEVERE;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.administration.AbstractCloneableDomainMember;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CompoundCondition;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Describable;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.LocalXmlIdentifierPool;
import com.syrus.AMFICOM.general.ReverseDependencyContainer;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.general.TypicalCondition;
import com.syrus.AMFICOM.general.XmlComplementorRegistry;
import com.syrus.AMFICOM.general.corba.IdlStorableObject;
import com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlCompoundConditionPackage.CompoundConditionSort;
import com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlTypicalConditionPackage.OperationSort;
import com.syrus.AMFICOM.general.xml.XmlIdentifier;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.resource.BitmapImageResource;
import com.syrus.AMFICOM.resource.SchemeImageResource;
import com.syrus.AMFICOM.scheme.corba.IdlScheme;
import com.syrus.AMFICOM.scheme.corba.IdlSchemeHelper;
import com.syrus.AMFICOM.scheme.corba.IdlSchemePackage.IdlKind;
import com.syrus.AMFICOM.scheme.xml.XmlScheme;
import com.syrus.AMFICOM.scheme.xml.XmlSchemeCableLink;
import com.syrus.AMFICOM.scheme.xml.XmlSchemeCableLinkSeq;
import com.syrus.AMFICOM.scheme.xml.XmlSchemeElement;
import com.syrus.AMFICOM.scheme.xml.XmlSchemeElementSeq;
import com.syrus.AMFICOM.scheme.xml.XmlSchemeLink;
import com.syrus.AMFICOM.scheme.xml.XmlSchemeLinkSeq;
import com.syrus.AMFICOM.scheme.xml.XmlSchemeMonitoringSolution;
import com.syrus.AMFICOM.scheme.xml.XmlSchemeMonitoringSolutionSeq;
import com.syrus.AMFICOM.scheme.xml.XmlSchemeOptimizeInfo;
import com.syrus.AMFICOM.scheme.xml.XmlSchemeOptimizeInfoSeq;
import com.syrus.util.Log;
import com.syrus.util.Shitlet;
import com.syrus.util.transport.xml.XmlConversionException;
import com.syrus.util.transport.xml.XmlTransferableObject;

/**
 * #03 in hierarchy.
 *
 * @author $Author: bass $
 * @version $Revision: 1.124 $, $Date: 2005/12/07 17:17:20 $
 * @module scheme
 * @todo Possibly join (add|remove)Scheme(Element|Link|CableLink).
 */
public final class Scheme extends AbstractCloneableDomainMember<Scheme>
		implements Describable, SchemeCellContainer,
		ReverseDependencyContainer, XmlTransferableObject<XmlScheme> {
	private static final long serialVersionUID = 3257289136389173298L;

	private static final int DEFAULT_WIDTH = 840;

	private static final int DEFAULT_HEIGHT = 1190;

	private String name;

	private String description;

	private String label;

	private int width;

	private int height;

	private int kind;

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
		this.kind = (kind == null) ? 0 : kind.value();
		this.mapId = Identifier.possiblyVoid(map);
		this.symbolId = Identifier.possiblyVoid(symbol);
		this.ugoCellId = Identifier.possiblyVoid(ugoCell);
		this.schemeCellId = Identifier.possiblyVoid(schemeCell);
		this.parentSchemeElementId = Identifier.possiblyVoid(parentSchemeElement);
	}

	/**
	 * Minimalistic constructor used when importing from XML.
	 *
	 * @param id
	 * @param importType
	 * @param created
	 * @param creatorId
	 * @throws IdentifierGenerationException
	 */
	private Scheme(final XmlIdentifier id,
			final String importType,
			final Date created,
			final Identifier creatorId)
	throws IdentifierGenerationException {
		super(id, importType, SCHEME_CODE, created, creatorId);
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
	 * @param parentSchemeElement may be {@code null}.
	 * @throws CreateObjectException
	 */
	@ParameterizationPending(value = {"final boolean usePool"})
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
			final SchemeElement parentSchemeElement)
	throws CreateObjectException {
		final boolean usePool = false;

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
					StorableObjectVersion.INITIAL_VERSION,
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
			if (parentSchemeElement != null) {
				parentSchemeElement.getSchemeContainerWrappee().addToCache(scheme, usePool);
			}

			scheme.markAsChanged();
			return scheme;
		} catch (final CreateObjectException coe) {
			throw coe;
		} catch (final IdentifierGenerationException ige) {
			throw new CreateObjectException("Scheme.createInstance | cannot generate identifier ", ige);
		} catch (final ApplicationException ae) {
			throw new CreateObjectException(ae);
		}
	}

	/**
	 * @param creatorId
	 * @param xmlScheme
	 * @throws CreateObjectException
	 */
	public static Scheme createInstance(final Identifier creatorId,
			final XmlScheme xmlScheme)
	throws CreateObjectException {
		assert creatorId != null && !creatorId.isVoid() : NON_VOID_EXPECTED;

		try {
			final String importType = xmlScheme.getImportType();
			final XmlIdentifier xmlId = xmlScheme.getId();
			final Date created = new Date();
			final Identifier id = Identifier.fromXmlTransferable(xmlId, importType, MODE_RETURN_VOID_IF_ABSENT);
			Scheme scheme;
			if (id.isVoid()) {
				scheme = new Scheme(xmlId,
						importType,
						created,
						creatorId);
			} else {
				scheme = StorableObjectPool.getStorableObject(id, true);
				if (scheme == null) {
					LocalXmlIdentifierPool.remove(xmlId, importType);
					scheme = new Scheme(xmlId,
							importType,
							created,
							creatorId);
				}
			}
			scheme.fromXmlTransferable(xmlScheme, importType);
			assert scheme.isValid() : OBJECT_BADLY_INITIALIZED;
			scheme.markAsChanged();
			return scheme;
		} catch (final CreateObjectException coe) {
			throw coe;
		} catch (final ApplicationException ae) {
			throw new CreateObjectException(ae);
		} catch (final XmlConversionException xce) {
			throw new CreateObjectException(xce);
		}
	}

	/**
	 * @throws CloneNotSupportedException
	 * @see Object#clone()
	 */
	@Override
	public Scheme clone() throws CloneNotSupportedException {
		final boolean usePool = false;

		try {
			final Scheme clone = super.clone();

			if (clone.clonedIdMap == null) {
				clone.clonedIdMap = new HashMap<Identifier, Identifier>();
			}

			clone.clonedIdMap.put(this.id, clone.id);

			final SchemeImageResource ugoCell = this.getUgoCell0();
			if (ugoCell == null) {
				clone.setUgoCell(null);
			} else {
				final SchemeImageResource ugoCellClone = ugoCell.clone();
				clone.clonedIdMap.putAll(ugoCellClone.getClonedIdMap());
				clone.setUgoCell(ugoCellClone);
			}
			final SchemeImageResource schemeCell = this.getSchemeCell0();
			if (schemeCell == null) {
				clone.setSchemeCell(null);
			} else {
				final SchemeImageResource schemeCellClone = schemeCell.clone();
				clone.clonedIdMap.putAll(schemeCellClone.getClonedIdMap());
				clone.setSchemeCell(schemeCellClone);
			}
			clone.schemeCableLinkContainerWrappee = null;
			for (final SchemeCableLink schemeCableLink : this.getSchemeCableLinks0(usePool)) {
				final SchemeCableLink schemeCableLinkClone = schemeCableLink.clone();
				clone.clonedIdMap.putAll(schemeCableLinkClone.getClonedIdMap());
				clone.addSchemeCableLink(schemeCableLinkClone, usePool);
			}
			clone.schemeElementContainerWrappee = null;
			for (final SchemeElement schemeElement : this.getSchemeElements0(usePool)) {
				final SchemeElement schemeElementClone = schemeElement.clone();
				clone.clonedIdMap.putAll(schemeElementClone.getClonedIdMap());
				clone.addSchemeElement(schemeElementClone, usePool);
			}
			clone.schemeLinkContainerWrappee = null;
			for (final SchemeLink schemeLink : this.getSchemeLinks0(usePool)) {
				final SchemeLink schemeLinkClone = schemeLink.clone();
				clone.clonedIdMap.putAll(schemeLinkClone.getClonedIdMap());
				clone.addSchemeLink(schemeLinkClone, usePool);
			}

			/*-
			 * Port references remapping.
			 */
			for (final SchemeLink schemeLink : clone.getSchemeLinks0(usePool)) {
				final Identifier sourceSchemePortId = clone.clonedIdMap.get(schemeLink.sourceAbstractSchemePortId);
				final Identifier targetSchemePortId = clone.clonedIdMap.get(schemeLink.targetAbstractSchemePortId);
				schemeLink.setSourceAbstractSchemePortId((sourceSchemePortId == null) ? VOID_IDENTIFIER : sourceSchemePortId);
				schemeLink.setTargetAbstractSchemePortId((targetSchemePortId == null) ? VOID_IDENTIFIER : targetSchemePortId);
			}
			for (final SchemeCableLink schemeCableLink : clone.getSchemeCableLinks0(usePool)) {
				final Identifier sourceSchemeCablePortId = clone.clonedIdMap.get(schemeCableLink.sourceAbstractSchemePortId);
				final Identifier targetSchemeCablePortId = clone.clonedIdMap.get(schemeCableLink.targetAbstractSchemePortId);
				schemeCableLink.setSourceAbstractSchemePortId((sourceSchemeCablePortId == null) ? VOID_IDENTIFIER : sourceSchemeCablePortId);
				schemeCableLink.setTargetAbstractSchemePortId((targetSchemeCablePortId == null) ? VOID_IDENTIFIER : targetSchemeCablePortId);

				for (final SchemeCableThread schemeCableThread : schemeCableLink.getSchemeCableThreads0(usePool)) {
					final Identifier sourceSchemePortId = clone.clonedIdMap.get(schemeCableThread.sourceSchemePortId);
					final Identifier targetSchemePortId = clone.clonedIdMap.get(schemeCableThread.targetSchemePortId);
					schemeCableThread.setSourceSchemePortId((sourceSchemePortId == null) ? VOID_IDENTIFIER : sourceSchemePortId);
					schemeCableThread.setTargetSchemePortId((targetSchemePortId == null) ? VOID_IDENTIFIER : targetSchemePortId);
				}
			}

			return clone;
		} catch (final ApplicationException ae) {
			final CloneNotSupportedException cnse = new CloneNotSupportedException();
			cnse.initCause(ae);
			throw cnse;
		}
	}

	/**
	 * @param usePool
	 * @throws ApplicationException
	 */
	public SchemeMonitoringSolution getCurrentSchemeMonitoringSolution(
			final boolean usePool)
	throws ApplicationException {
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
							SchemeMonitoringSolutionWrapper.COLUMN_ACTIVE) {
				private static final long serialVersionUID = -2174802384926550856L;

				@Override
				public boolean isNeedMore(final Set<? extends Identifiable> identifiables) {
					return identifiables.isEmpty();
				}
			};
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
		for (final SchemeOptimizeInfo schemeOptimizeInfo : this.getSchemeOptimizeInfos(usePool)) {
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
		assert this.getSchemeMonitoringSolutionsRecursively(usePool).isEmpty()
				? schemeMonitoringSolutions.isEmpty()
				: schemeMonitoringSolutions.size() == 1;
		/*
		 * Return the first entry or null if empty.
		 */
		for (final SchemeMonitoringSolution schemeMonitoringSolution : schemeMonitoringSolutions) {
			return schemeMonitoringSolution;
		}
		return null;
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
	 * @see com.syrus.AMFICOM.general.ReverseDependencyContainer#getReverseDependencies(boolean)
	 */
	public Set<Identifiable> getReverseDependencies(final boolean usePool) throws ApplicationException {
		final Set<Identifiable> reverseDependencies = new HashSet<Identifiable>();
		reverseDependencies.add(super.id);
		for (final ReverseDependencyContainer reverseDependencyContainer : this.getSchemeCableLinks0(usePool)) {
			reverseDependencies.addAll(reverseDependencyContainer.getReverseDependencies(usePool));
		}
		for (final ReverseDependencyContainer reverseDependencyContainer : this.getSchemeElements0(usePool)) {
			reverseDependencies.addAll(reverseDependencyContainer.getReverseDependencies(usePool));
		}
		for (final ReverseDependencyContainer reverseDependencyContainer : this.getSchemeLinks0(usePool)) {
			reverseDependencies.addAll(reverseDependencyContainer.getReverseDependencies(usePool));
		}
		for (final ReverseDependencyContainer reverseDependencyContainer : this.getSchemeOptimizeInfos0(usePool)) {
			reverseDependencies.addAll(reverseDependencyContainer.getReverseDependencies(usePool));
		}
		for (final ReverseDependencyContainer reverseDependencyContainer : this.getSchemeMonitoringSolutions0(usePool)) {
			reverseDependencies.addAll(reverseDependencyContainer.getReverseDependencies(usePool));
		}
		reverseDependencies.remove(null);
		reverseDependencies.remove(VOID_IDENTIFIER);
		return Collections.unmodifiableSet(reverseDependencies);
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
			return StorableObjectPool.getStorableObject(this.getMapId(), true);
		} catch (final ApplicationException ae) {
			Log.debugMessage(ae, SEVERE);
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
	public SchemeElement getParentSchemeElement() throws ApplicationException {
		return StorableObjectPool.getStorableObject(this.getParentSchemeElementId(), true);
	}

	Identifier getSchemeCellId() {
		assert this.schemeCellId != null: OBJECT_NOT_INITIALIZED;
		assert this.schemeCellId.isVoid() || this.schemeCellId.getMajor() == IMAGERESOURCE_CODE;
		return this.schemeCellId;
	}

	/**
	 * A wrapper around {@link #getSchemeCell0()}.
	 *
	 * @see SchemeCellContainer#getSchemeCell()
	 */
	public SchemeImageResource getSchemeCell() {
		try {
			return this.getSchemeCell0();
		} catch (final ApplicationException ae) {
			Log.debugMessage(ae, SEVERE);
			return null;
		}
	}

	/**
	 * A wrapper around {@link #getSchemeCellId()}.
	 *
	 * @throws ApplicationException
	 */
	SchemeImageResource getSchemeCell0() throws ApplicationException {
		return StorableObjectPool.getStorableObject(this.getSchemeCellId(), true);
	}

	public IdlKind getKind() {
		return IdlKind.from_int(this.kind);
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
			return StorableObjectPool.getStorableObject(this.getSymbolId(), true);
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
	public IdlScheme getIdlTransferable(final ORB orb) {
		/*
		 * domainId is assumed to be non-null.
		 */
		return IdlSchemeHelper.init(orb,
				this.id.getIdlTransferable(),
				this.created.getTime(),
				this.modified.getTime(),
				this.creatorId.getIdlTransferable(),
				this.modifierId.getIdlTransferable(),
				this.version.longValue(),
				this.name, this.description, this.label,
				this.width, this.height, this.getKind(),
				super.getDomainId().getIdlTransferable(),
				this.mapId.getIdlTransferable(),
				this.symbolId.getIdlTransferable(),
				this.ugoCellId.getIdlTransferable(),
				this.schemeCellId.getIdlTransferable(),
				this.parentSchemeElementId.getIdlTransferable());
	}

	/**
	 * @param scheme
	 * @param importType
	 * @param usePool
	 * @throws XmlConversionException
	 * @see com.syrus.util.transport.xml.XmlTransferableObject#getXmlTransferable(org.apache.xmlbeans.XmlObject, String, boolean)
	 */
	public void getXmlTransferable(final XmlScheme scheme,
			final String importType,
			final boolean usePool)
	throws XmlConversionException {
		try {
			super.id.getXmlTransferable(scheme.addNewId(), importType);
			scheme.setName(this.name);
			if (scheme.isSetDescription()) {
				scheme.unsetDescription();
			}
			if (this.description.length() != 0) {
				scheme.setDescription(this.description);
			}
			if (scheme.isSetLabel()) {
				scheme.unsetLabel();
			}
			if (this.label.length() != 0) {
				scheme.setLabel(this.label);
			}
			scheme.setWidth(this.width);
			scheme.setHeight(this.height);
			scheme.setKind(XmlScheme.Kind.Enum.forInt(this.getKind().value() + 1));
			if (scheme.isSetDomainId()) {
				scheme.unsetDomainId();
			}
			super.getDomainId().getXmlTransferable(scheme.addNewDomainId(), importType);
			if (scheme.isSetMapId()) {
				scheme.unsetMapId();
			}
			if (!this.mapId.isVoid()) {
				this.mapId.getXmlTransferable(scheme.addNewMapId(), importType);
			}
			if (scheme.isSetSymbolId()) {
				scheme.unsetSymbolId();
			}
			if (!this.symbolId.isVoid()) {
				this.symbolId.getXmlTransferable(scheme.addNewSymbolId(), importType);
			}
			if (scheme.isSetUgoCellId()) {
				scheme.unsetUgoCellId();
			}
			if (!this.ugoCellId.isVoid()) {
				this.ugoCellId.getXmlTransferable(scheme.addNewUgoCellId(), importType);
			}
			if (scheme.isSetSchemeCellId()) {
				scheme.unsetSchemeCellId();
			}
			if (!this.schemeCellId.isVoid()) {
				this.schemeCellId.getXmlTransferable(scheme.addNewSchemeCellId(), importType);
			}
			if (scheme.isSetParentSchemeElementId()) {
				scheme.unsetParentSchemeElementId();
			}
			if (!this.parentSchemeElementId.isVoid()) {
				this.parentSchemeElementId.getXmlTransferable(scheme.addNewParentSchemeElementId(), importType);
			}
			if (scheme.isSetSchemeElements()) {
				scheme.unsetSchemeElements();
			}
			final Set<SchemeElement> schemeElements = this.getSchemeElements0(usePool);
			if (!schemeElements.isEmpty()) {
				final XmlSchemeElementSeq schemeElementSeq = scheme.addNewSchemeElements();
				for (final SchemeElement schemeElement : schemeElements) {
					schemeElement.getXmlTransferable(schemeElementSeq.addNewSchemeElement(), importType, usePool);
				}
			}
			if (scheme.isSetSchemeLinks()) {
				scheme.unsetSchemeLinks();
			}
			final Set<SchemeLink> schemeLinks = this.getSchemeLinks0(usePool);
			if (!schemeLinks.isEmpty()) {
				final XmlSchemeLinkSeq schemeLinkSeq = scheme.addNewSchemeLinks(); 
				for (final SchemeLink schemeLink : schemeLinks) {
					schemeLink.getXmlTransferable(schemeLinkSeq.addNewSchemeLink(), importType, usePool);
				}
			}
			if (scheme.isSetSchemeCableLinks()) {
				scheme.unsetSchemeCableLinks();
			}
			final Set<SchemeCableLink> schemeCableLinks = this.getSchemeCableLinks0(usePool);
			if (!schemeCableLinks.isEmpty()) {
				final XmlSchemeCableLinkSeq schemeCableLinkSeq = scheme.addNewSchemeCableLinks();
				for (final SchemeCableLink schemeCableLink : schemeCableLinks) {
					schemeCableLink.getXmlTransferable(schemeCableLinkSeq.addNewSchemeCableLink(), importType, usePool);
				}
			}
			if (scheme.isSetSchemeOptimizeInfos()) {
				scheme.unsetSchemeOptimizeInfos();
			}
			final Set<SchemeOptimizeInfo> schemeOptimizeInfos = this.getSchemeOptimizeInfos0(usePool);
			if (!schemeOptimizeInfos.isEmpty()) {
				final XmlSchemeOptimizeInfoSeq schemeOptimizeInfoSeq = scheme.addNewSchemeOptimizeInfos();
				for (final SchemeOptimizeInfo schemeOptimizeInfo : schemeOptimizeInfos) {
					schemeOptimizeInfo.getXmlTransferable(schemeOptimizeInfoSeq.addNewSchemeOptimizeInfo(), importType, usePool);
				}
			}
			if (scheme.isSetSchemeMonitoringSolutions()) {
				scheme.unsetSchemeMonitoringSolutions();
			}
			final Set<SchemeMonitoringSolution> schemeMonitoringSolutions = this.getSchemeMonitoringSolutions0(usePool);
			if (!schemeMonitoringSolutions.isEmpty()) {
				final XmlSchemeMonitoringSolutionSeq schemeMonitoringSolutionSeq = scheme.addNewSchemeMonitoringSolutions();
				for (final SchemeMonitoringSolution schemeMonitoringSolution : schemeMonitoringSolutions) {
					schemeMonitoringSolution.getXmlTransferable(schemeMonitoringSolutionSeq.addNewSchemeMonitoringSolution(), importType, usePool);
				}
			}
			scheme.setImportType(importType);
			XmlComplementorRegistry.complementStorableObject(scheme, SCHEME_CODE, importType, EXPORT);
		} catch (final ApplicationException ae) {
			throw new XmlConversionException(ae);
		}
	}

	Identifier getUgoCellId() {
		assert this.ugoCellId != null: OBJECT_NOT_INITIALIZED;
		assert this.ugoCellId.isVoid() || this.ugoCellId.getMajor() == IMAGERESOURCE_CODE;
		return this.ugoCellId;
	}

	/**
	 * A wrapper around {@link #getUgoCell0()}.
	 *
	 * @see SchemeCellContainer#getUgoCell()
	 */
	public SchemeImageResource getUgoCell() {
		try {
			return this.getUgoCell0();
		} catch (final ApplicationException ae) {
			Log.debugMessage(ae, SEVERE);
			return null;
		}
	}

	/**
	 * A wrapper around {@link #getUgoCellId()}.
	 *
	 * @throws ApplicationException
	 */
	SchemeImageResource getUgoCell0() throws ApplicationException {
		return StorableObjectPool.getStorableObject(this.getUgoCellId(), true);
	}

	public int getWidth() {
		return this.width;
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
	void setAttributes(final Date created,
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
		synchronized (this) {
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
			this.kind = kind.value();
			this.mapId = mapId;
			this.symbolId = symbolId;
			this.ugoCellId = ugoCellId;
			this.schemeCellId = schemeCellId;
			this.parentSchemeElementId = parentSchemeElementId;
		}
	}

	/**
	 * @param currentSchemeMonitoringSolution
	 * @param usePool
	 * @throws ApplicationException
	 */
	public void setCurrentSchemeMonitoringSolution(
			final SchemeMonitoringSolution currentSchemeMonitoringSolution,
			final boolean usePool)
	throws ApplicationException {
		assert currentSchemeMonitoringSolution != null : NON_NULL_EXPECTED;
		/*
		 * Not ...getParentSchemeId(), since we'll miss solutions with
		 * non-null parent optimizeInfo. Here getParentSchemeId() may
		 * eventually return a void identifier, while getParentScheme()
		 * will never return null, unless this object is deleted, which
		 * is unlikely.
		 */
		assert currentSchemeMonitoringSolution.getParentSchemeId().equals(this);
		currentSchemeMonitoringSolution.setActive(true, usePool);
	}

	/**
	 * @see Describable#setDescription(String)
	 */
	public void setDescription(final String description) {
		assert this.description != null : OBJECT_NOT_INITIALIZED;
		assert description != null : NON_NULL_EXPECTED;
		if (this.description.equals(description)) {
			return;
		}
		this.description = description;
		super.markAsChanged();
	}

	public void setHeight(final int height) {
		if (this.height == height) {
			return;
		}
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
		if (this.label.equals(label)) {
			return;
		}
		this.label = label;
		super.markAsChanged();
	}

	/**
	 * @param mapId
	 */
	void setMapId(final Identifier mapId) {
		assert mapId.isVoid() || mapId.getMajor() == MAP_CODE;
		if (this.mapId.equals(mapId)) {
			return;
		}
		this.mapId = mapId;
		super.markAsChanged();
	}

	/**
	 * A wrapper around {@link #setMapId(Identifier)}.
	 *
	 * @param map
	 */
	public void setMap(final Map map) {
		this.setMapId(Identifier.possiblyVoid(map));
	}

	/**
	 * @see com.syrus.AMFICOM.general.Namable#setName(String)
	 */
	public void setName(final String name) {
		assert this.name != null && this.name.length() != 0 : OBJECT_NOT_INITIALIZED;
		assert name != null && name.length() != 0 : NON_EMPTY_EXPECTED;
		if (this.name.equals(name)) {
			return;
		}
		this.name = name;
		super.markAsChanged();
	}

	/**
	 * A wrapper around {@link #setParentSchemeElement(SchemeElement, boolean)}.
	 *
	 * @param parentSchemeElementId
	 * @param usePool
	 * @throws ApplicationException
	 */
	void setParentSchemeElementId(final Identifier parentSchemeElementId,
			final boolean usePool)
	throws ApplicationException {
		assert parentSchemeElementId != null : NON_NULL_EXPECTED;
		assert parentSchemeElementId.isVoid() || parentSchemeElementId.getMajor() == SCHEMEELEMENT_CODE;

		if (this.parentSchemeElementId.equals(parentSchemeElementId)) {
			return;
		}

		this.setParentSchemeElement(
				StorableObjectPool.<SchemeElement>getStorableObject(parentSchemeElementId, true),
				usePool);
	}

	/**
	 * @param parentSchemeElement
	 * @param usePool
	 * @throws ApplicationException
	 */
	public void setParentSchemeElement(
			final SchemeElement parentSchemeElement,
			final boolean usePool)
	throws ApplicationException {
		if (parentSchemeElement != null
				&& parentSchemeElement.getKind() == SCHEME_ELEMENT_CONTAINER) {
			throw new ClassCastException();
		}

		assert this.parentSchemeElementId != null : OBJECT_BADLY_INITIALIZED;

		final Identifier newParentSchemeElementId = Identifier.possiblyVoid(parentSchemeElement);
		if (this.parentSchemeElementId.equals(newParentSchemeElementId)) {
			return;
		}

		final SchemeElement oldParentSchemeElement = this.getParentSchemeElement();
		if (oldParentSchemeElement != null) {
			oldParentSchemeElement.getSchemeContainerWrappee().removeFromCache(this, usePool);
		}
		if (parentSchemeElement != null) {
			parentSchemeElement.getSchemeContainerWrappee().addToCache(this, usePool);
		}

		this.parentSchemeElementId = newParentSchemeElementId;
		super.markAsChanged();
	}

	/**
	 * @param schemeCellId
	 */
	void setSchemeCellId(final Identifier schemeCellId) {
		assert schemeCellId.isVoid() || schemeCellId.getMajor() == IMAGERESOURCE_CODE;
		if (this.schemeCellId.equals(schemeCellId)) {
			return;
		}
		this.schemeCellId = schemeCellId;
		super.markAsChanged();
	}

	/**
	 * A wrapper around {@link #setSchemeCellId(Identifier)}.
	 *
	 * @param schemeCell
	 * @see SchemeCellContainer#setSchemeCell(SchemeImageResource)
	 */
	public void setSchemeCell(final SchemeImageResource schemeCell) {
		this.setSchemeCellId(Identifier.possiblyVoid(schemeCell));
	}

	/**
	 * @param kind
	 */
	public void setKind(final IdlKind kind) {
		assert kind != null: NON_NULL_EXPECTED;
		if (this.getKind() == kind) {
			return;
		}
		this.kind = kind.value();
		super.markAsChanged();
	}

	/**
	 * @param symbolId
	 */
	void setSymbolId(final Identifier symbolId) {
		assert symbolId.isVoid() || symbolId.getMajor() == IMAGERESOURCE_CODE;
		if (this.symbolId.equals(symbolId)) {
			return;
		}
		this.symbolId = symbolId;
		super.markAsChanged();
	}

	/**
	 * A wrapper around {@link #setSymbolId(Identifier)}.
	 * @param symbol
	 * @see SchemeSymbolContainer#setSymbol(BitmapImageResource)
	 */
	public void setSymbol(final BitmapImageResource symbol) {
		this.setSymbolId(Identifier.possiblyVoid(symbol));
	}

	/**
	 * @param ugoCellId
	 */
	void setUgoCellId(final Identifier ugoCellId) {
		assert ugoCellId.isVoid() || ugoCellId.getMajor() == IMAGERESOURCE_CODE;
		if (this.ugoCellId.equals(ugoCellId)) {
			return;
		}
		this.ugoCellId = ugoCellId;
		super.markAsChanged();
	}

	/**
	 * A wrapper around {@link #setUgoCellId(Identifier)}.
	 *
	 * @param ugoCell
	 * @see SchemeCellContainer#setUgoCell(SchemeImageResource)
	 */
	public void setUgoCell(final SchemeImageResource ugoCell) {
		this.setUgoCellId(Identifier.possiblyVoid(ugoCell));
	}

	public void setWidth(final int width) {
		if (this.width == width) {
			return;
		}
		this.width = width;
		super.markAsChanged();
	}

	/**
	 * @param transferable
	 * @see com.syrus.AMFICOM.general.StorableObject#fromTransferable(IdlStorableObject)
	 */
	@Override
	protected void fromTransferable(final IdlStorableObject transferable) {
		synchronized (this) {
			final IdlScheme scheme = (IdlScheme) transferable;
			super.fromTransferable(scheme, new Identifier(scheme.domainId));
			this.name = scheme.name;
			this.description = scheme.description;
			this.label = scheme.label;
			this.width = scheme.width;
			this.height = scheme.height;
			this.kind = scheme.kind.value();
			this.mapId = new Identifier(scheme.mapId);
			this.symbolId = new Identifier(scheme.symbolId);
			this.ugoCellId = new Identifier(scheme.ugoCellId);
			this.schemeCellId = new Identifier(scheme.schemeCellId);
			this.parentSchemeElementId = new Identifier(scheme.parentSchemeElementId);
		}
	}

	/**
	 * @param scheme
	 * @param importType
	 * @throws XmlConversionException
	 * @see XmlTransferableObject#fromXmlTransferable(org.apache.xmlbeans.XmlObject, String)
	 */
	public void fromXmlTransferable(final XmlScheme scheme,
			final String importType)
	throws XmlConversionException {
		try {
			XmlComplementorRegistry.complementStorableObject(scheme, SCHEME_CODE, importType, PRE_IMPORT);
	
			this.name = scheme.getName();
			this.description = scheme.isSetDescription()
					? scheme.getDescription()
					: "";
			this.label = scheme.isSetLabel()
					? scheme.getLabel()
					: "";
			this.width = scheme.getWidth();
			this.height = scheme.getHeight();
			this.kind = scheme.getKind().intValue() - 1;
			if (scheme.isSetDomainId()) {
				super.setDomainId0(Identifier.fromXmlTransferable(scheme.getDomainId(), importType, MODE_THROW_IF_ABSENT));
			} else {
				throw new XmlConversionException("Scheme.fromXmlTransferable() | "
						+ XML_BEAN_NOT_COMPLETE);
			}
			this.mapId = scheme.isSetMapId()
					? Identifier.fromXmlTransferable(scheme.getMapId(), importType, MODE_THROW_IF_ABSENT)
					: VOID_IDENTIFIER;
			this.symbolId = scheme.isSetSymbolId()
					? Identifier.fromXmlTransferable(scheme.getSymbolId(), importType, MODE_THROW_IF_ABSENT)
					: VOID_IDENTIFIER;
			this.ugoCellId = scheme.isSetUgoCellId()
					? Identifier.fromXmlTransferable(scheme.getUgoCellId(), importType, MODE_THROW_IF_ABSENT)
					: VOID_IDENTIFIER;
			this.schemeCellId = scheme.isSetSchemeCellId()
					? Identifier.fromXmlTransferable(scheme.getSchemeCellId(), importType, MODE_THROW_IF_ABSENT)
					: VOID_IDENTIFIER;
			this.parentSchemeElementId = scheme.isSetParentSchemeElementId()
					? Identifier.fromXmlTransferable(scheme.getParentSchemeElementId(), importType, MODE_THROW_IF_ABSENT)
					: VOID_IDENTIFIER;
			if (scheme.isSetSchemeElements()) {
				for (final XmlSchemeElement schemeElement : scheme.getSchemeElements().getSchemeElementArray()) {
					SchemeElement.createInstance(super.creatorId, schemeElement, importType);
				}
			}
			if (scheme.isSetSchemeLinks()) {
				for (final XmlSchemeLink schemeLink : scheme.getSchemeLinks().getSchemeLinkArray()) {
					SchemeLink.createInstance(super.creatorId, schemeLink, importType);
				}
			}
			if (scheme.isSetSchemeCableLinks()) {
				for (final XmlSchemeCableLink schemeCableLink : scheme.getSchemeCableLinks().getSchemeCableLinkArray()) {
					SchemeCableLink.createInstance(super.creatorId, schemeCableLink, importType);
				}
			}
			if (scheme.isSetSchemeOptimizeInfos()) {
				for (final XmlSchemeOptimizeInfo schemeOptimizeInfo : scheme.getSchemeOptimizeInfos().getSchemeOptimizeInfoArray()) {
					// empty so far
				}
			}
			if (scheme.isSetSchemeMonitoringSolutions()) {
				for (final XmlSchemeMonitoringSolution schemeMonitoringSolution : scheme.getSchemeMonitoringSolutions().getSchemeMonitoringSolutionArray()) {
					// empty so far
				}
			}
	
			XmlComplementorRegistry.complementStorableObject(scheme, SCHEME_CODE, importType, POST_IMPORT);
		} catch (final ApplicationException ae) {
			throw new XmlConversionException(ae);
		}
	}

	/**
	 * @see com.syrus.AMFICOM.general.StorableObject#getWrapper()
	 */
	@Override
	protected SchemeWrapper getWrapper() {
		return SchemeWrapper.getInstance();
	}

	/*-********************************************************************
	 * Children manipulation: scheme elements                             *
	 **********************************************************************/

	private transient StorableObjectContainerWrappee<SchemeElement> schemeElementContainerWrappee;

	StorableObjectContainerWrappee<SchemeElement> getSchemeElementContainerWrappee() {
		return (this.schemeElementContainerWrappee == null)
				? this.schemeElementContainerWrappee = new StorableObjectContainerWrappee<SchemeElement>(this, SCHEMEELEMENT_CODE)
				: this.schemeElementContainerWrappee;
	}

	/**
	 * @param schemeElement cannot be <code>null</code>.
	 * @param usePool
	 * @throws ApplicationException
	 */
	public void addSchemeElement(final SchemeElement schemeElement,
			final boolean usePool)
	throws ApplicationException {
		assert schemeElement != null: NON_NULL_EXPECTED;
		schemeElement.setParentScheme(this, usePool);
	}

	/**
	 * The <code>SchemeElement</code> must belong to this
	 * <code>Scheme</code>, or crap will meet the fan.
	 *
	 * @param schemeElement
	 * @param usePool
	 * @throws ApplicationException
	 */
	public void removeSchemeElement(final SchemeElement schemeElement,
			final boolean usePool)
	throws ApplicationException {
		assert schemeElement != null: NON_NULL_EXPECTED;
		assert schemeElement.getParentSchemeId().equals(this) : REMOVAL_OF_AN_ABSENT_PROHIBITED;
		schemeElement.setParentScheme(null, usePool);
	}

	/**
	 * @param usePool
	 * @throws ApplicationException
	 */
	public Set<SchemeElement> getSchemeElements(final boolean usePool)
	throws ApplicationException {
		return Collections.unmodifiableSet(this.getSchemeElements0(usePool));
	}

	/**
	 * @param usePool
	 * @throws ApplicationException
	 */
	Set<SchemeElement> getSchemeElements0(final boolean usePool)
	throws ApplicationException {
		return this.getSchemeElementContainerWrappee().getContainees(usePool);
	}

	/**
	 * @param schemeElements
	 * @param usePool
	 * @throws ApplicationException
	 */
	public void setSchemeElements(final Set<SchemeElement> schemeElements,
			final boolean usePool)
	throws ApplicationException {
		assert schemeElements != null: NON_NULL_EXPECTED;

		final Set<SchemeElement> oldSchemeElements = this.getSchemeElements0(usePool);

		final Set<SchemeElement> toRemove = new HashSet<SchemeElement>(oldSchemeElements);
		toRemove.removeAll(schemeElements);
		for (final SchemeElement schemeElement : toRemove) {
			this.removeSchemeElement(schemeElement, usePool);
		}

		final Set<SchemeElement> toAdd = new HashSet<SchemeElement>(schemeElements);
		toAdd.removeAll(oldSchemeElements);
		for (final SchemeElement schemeElement : toAdd) {
			this.addSchemeElement(schemeElement, usePool);
		}
	}

	/*-********************************************************************
	 * Children manipulation: scheme links                                *
	 **********************************************************************/

	private transient StorableObjectContainerWrappee<SchemeLink> schemeLinkContainerWrappee;

	StorableObjectContainerWrappee<SchemeLink> getSchemeLinkContainerWrappee() {
		return (this.schemeLinkContainerWrappee == null)
				? this.schemeLinkContainerWrappee = new StorableObjectContainerWrappee<SchemeLink>(this, SCHEMELINK_CODE)
				: this.schemeLinkContainerWrappee;
	}

	/**
	 * @param schemeLink cannot be <code>null</code>.
	 * @param usePool
	 * @throws ApplicationException
	 */
	public void addSchemeLink(final SchemeLink schemeLink,
			final boolean usePool)
	throws ApplicationException {
		assert schemeLink != null: NON_NULL_EXPECTED;
		schemeLink.setParentScheme(this, usePool);
	}

	/**
	 * The <code>SchemeLink</code> must belong to this <code>Scheme</code>,
	 * or crap will meet the fan.
	 *
	 * @param schemeLink
	 * @param usePool
	 * @throws ApplicationException
	 */
	public void removeSchemeLink(final SchemeLink schemeLink,
			final boolean usePool)
	throws ApplicationException {
		assert schemeLink != null: NON_NULL_EXPECTED;
		assert schemeLink.getParentSchemeId().equals(this) : REMOVAL_OF_AN_ABSENT_PROHIBITED;
		schemeLink.setParentScheme(null, usePool);
	}

	/**
	 * @param usePool
	 * @throws ApplicationException
	 */
	public Set<SchemeLink> getSchemeLinks(final boolean usePool)
	throws ApplicationException {
		return Collections.unmodifiableSet(this.getSchemeLinks0(usePool));
	}

	/**
	 * @param usePool
	 * @throws ApplicationException
	 */
	Set<SchemeLink> getSchemeLinks0(final boolean usePool)
	throws ApplicationException {
		return this.getSchemeLinkContainerWrappee().getContainees(usePool);
	}

	/**
	 * @param schemeLinks
	 * @param usePool
	 * @throws ApplicationException
	 */
	public void setSchemeLinks(final Set<SchemeLink> schemeLinks,
			final boolean usePool)
	throws ApplicationException {
		assert schemeLinks != null: NON_NULL_EXPECTED;

		final Set<SchemeLink> oldSchemeLinks = this.getSchemeLinks0(usePool);

		final Set<SchemeLink> toRemove = new HashSet<SchemeLink>(oldSchemeLinks);
		toRemove.removeAll(schemeLinks);
		for (final SchemeLink schemeLink : toRemove) {
			this.removeSchemeLink(schemeLink, usePool);
		}

		final Set<SchemeLink> toAdd = new HashSet<SchemeLink>(schemeLinks);
		toAdd.removeAll(oldSchemeLinks);
		for (final SchemeLink schemeLink : toAdd) {
			this.addSchemeLink(schemeLink, usePool);
		}
	}

	/*-********************************************************************
	 * Children manipulation: scheme cable links                          *
	 **********************************************************************/

	transient StorableObjectContainerWrappee<SchemeCableLink> schemeCableLinkContainerWrappee;

	StorableObjectContainerWrappee<SchemeCableLink> getSchemeCableLinkContainerWrappee() {
		return (this.schemeCableLinkContainerWrappee == null)
				? this.schemeCableLinkContainerWrappee = new StorableObjectContainerWrappee<SchemeCableLink>(this, SCHEMECABLELINK_CODE)
				: this.schemeCableLinkContainerWrappee;
	}

	/**
	 * @param schemeCableLink cannot be <code>null</code>.
	 * @param usePool
	 * @throws ApplicationException
	 */
	public void addSchemeCableLink(final SchemeCableLink schemeCableLink,
			final boolean usePool)
	throws ApplicationException {
		assert schemeCableLink != null: NON_NULL_EXPECTED;
		schemeCableLink.setParentScheme(this, usePool);
	}

	/**
	 * The <code>SchemeCableLink</code> must belong to this
	 * <code>Scheme</code>, or crap will meet the fan.
	 *
	 * @param schemeCableLink
	 * @param usePool
	 * @throws ApplicationException
	 */
	public void removeSchemeCableLink(final SchemeCableLink schemeCableLink,
			final boolean usePool)
	throws ApplicationException {
		assert schemeCableLink != null: NON_NULL_EXPECTED;
		assert schemeCableLink.getParentSchemeId().equals(this) : REMOVAL_OF_AN_ABSENT_PROHIBITED;
		schemeCableLink.setParentScheme(null, usePool);
	}

	/**
	 * @param usePool
	 * @throws ApplicationException
	 */
	public Set<SchemeCableLink> getSchemeCableLinks(final boolean usePool)
	throws ApplicationException {
		return Collections.unmodifiableSet(this.getSchemeCableLinks0(usePool));
	}

	/**
	 * @param usePool
	 * @throws ApplicationException
	 */
	Set<SchemeCableLink> getSchemeCableLinks0(final boolean usePool)
	throws ApplicationException {
		return this.getSchemeCableLinkContainerWrappee().getContainees(usePool);
	}

	/**
	 * @param schemeCableLinks
	 * @param usePool
	 * @throws ApplicationException
	 */
	public void setSchemeCableLinks(
			final Set<SchemeCableLink> schemeCableLinks,
			final boolean usePool)
	throws ApplicationException {
		assert schemeCableLinks != null: NON_NULL_EXPECTED;

		final Set<SchemeCableLink> oldSchemeCableLinks = this.getSchemeCableLinks0(usePool);

		final Set<SchemeCableLink> toRemove = new HashSet<SchemeCableLink>(oldSchemeCableLinks);
		toRemove.removeAll(schemeCableLinks);
		for (final SchemeCableLink schemeCableLink : toRemove) {
			this.removeSchemeCableLink(schemeCableLink, usePool);
		}

		final Set<SchemeCableLink> toAdd = new HashSet<SchemeCableLink>(schemeCableLinks);
		toAdd.removeAll(oldSchemeCableLinks);
		for (final SchemeCableLink schemeCableLink : toAdd) {
			this.addSchemeCableLink(schemeCableLink, usePool);
		}
	}

	/*-********************************************************************
	 * Children manipulation: scheme optimizeInfos                        *
	 **********************************************************************/

	transient StorableObjectContainerWrappee<SchemeOptimizeInfo> schemeOptimizeInfoContainerWrappee;

	StorableObjectContainerWrappee<SchemeOptimizeInfo> getSchemeOptimizeInfoContainerWrappee() {
		return (this.schemeOptimizeInfoContainerWrappee == null)
				? this.schemeOptimizeInfoContainerWrappee = new StorableObjectContainerWrappee<SchemeOptimizeInfo>(this, SCHEMEOPTIMIZEINFO_CODE)
				: this.schemeOptimizeInfoContainerWrappee;
	}

	/**
	 * @param schemeOptimizeInfo cannot be <code>null</code>.
	 * @param usePool
	 * @throws ApplicationException
	 */
	public void addSchemeOptimizeInfo(
			final SchemeOptimizeInfo schemeOptimizeInfo,
			final boolean usePool)
	throws ApplicationException {
		assert schemeOptimizeInfo != null: NON_NULL_EXPECTED;
		schemeOptimizeInfo.setParentScheme(this, usePool);
	}

	/**
	 * The {@code SchemeOptimizeInfo} must belong to this {@code Scheme}, or
	 * crap will meet the fan.
	 *
	 * @param schemeOptimizeInfo
	 * @param usePool
	 * @throws ApplicationException
	 */
	public void removeSchemeOptimizeInfo(
			final SchemeOptimizeInfo schemeOptimizeInfo,
			final boolean usePool)
	throws ApplicationException {
		assert schemeOptimizeInfo != null: NON_NULL_EXPECTED;
		assert schemeOptimizeInfo.getParentSchemeId().equals(this) : REMOVAL_OF_AN_ABSENT_PROHIBITED;
		schemeOptimizeInfo.setParentScheme(null, usePool);
	}

	/**
	 * @param usePool
	 * @throws ApplicationException
	 */
	public Set<SchemeOptimizeInfo> getSchemeOptimizeInfos(
			final boolean usePool)
	throws ApplicationException {
		return Collections.unmodifiableSet(this.getSchemeOptimizeInfos0(usePool));
	}

	/**
	 * @param usePool
	 * @throws ApplicationException
	 */
	private Set<SchemeOptimizeInfo> getSchemeOptimizeInfos0(
			final boolean usePool)
	throws ApplicationException {
		return this.getSchemeOptimizeInfoContainerWrappee().getContainees(usePool);
	}

	/**
	 * @param schemeOptimizeInfos
	 * @param usePool
	 * @throws ApplicationException
	 */
	public void setSchemeOptimizeInfos(
			final Set<SchemeOptimizeInfo> schemeOptimizeInfos,
			final boolean usePool)
	throws ApplicationException {
		assert schemeOptimizeInfos != null: NON_NULL_EXPECTED;

		final Set<SchemeOptimizeInfo> oldSchemeOptimizeInfos = this.getSchemeOptimizeInfos0(usePool);

		final Set<SchemeOptimizeInfo> toRemove = new HashSet<SchemeOptimizeInfo>(oldSchemeOptimizeInfos);
		toRemove.removeAll(schemeOptimizeInfos);
		for (final SchemeOptimizeInfo schemeOptimizeInfo : toRemove) {
			this.removeSchemeOptimizeInfo(schemeOptimizeInfo, usePool);
		}

		final Set<SchemeOptimizeInfo> toAdd = new HashSet<SchemeOptimizeInfo>(schemeOptimizeInfos);
		toAdd.removeAll(oldSchemeOptimizeInfos);
		for (final SchemeOptimizeInfo schemeOptimizeInfo : toAdd) {
			this.addSchemeOptimizeInfo(schemeOptimizeInfo, usePool);
		}
	}

	/*-********************************************************************
	 * Children manipulation: scheme monitoringSolutions                  *
	 **********************************************************************/

	transient StorableObjectContainerWrappee<SchemeMonitoringSolution> schemeMonitoringSolutionContainerWrappee;

	StorableObjectContainerWrappee<SchemeMonitoringSolution> getSchemeMonitoringSolutionContainerWrappee() {
		return (this.schemeMonitoringSolutionContainerWrappee == null)
				? this.schemeMonitoringSolutionContainerWrappee = new StorableObjectContainerWrappee<SchemeMonitoringSolution>(this, SCHEMEMONITORINGSOLUTION_CODE)
				: this.schemeMonitoringSolutionContainerWrappee;
	}

	/**
	 * @param schemeMonitoringSolution
	 * @param usePool
	 * @throws ApplicationException
	 */
	public void addSchemeMonitoringSolution(
			final SchemeMonitoringSolution schemeMonitoringSolution,
			final boolean usePool)
	throws ApplicationException {
		assert schemeMonitoringSolution != null : NON_NULL_EXPECTED;
		schemeMonitoringSolution.setParentScheme(this, usePool);
	}

	/**
	 * The {@code SchemeMonitoringSolution} must belong to this
	 * {@code Scheme}, or crap will meet the fan.
	 *
	 * @param schemeMonitoringSolution
	 * @param usePool
	 * @throws ApplicationException
	 */
	public void removeSchemeMonitoringSolution(
			final SchemeMonitoringSolution schemeMonitoringSolution,
			final boolean usePool)
	throws ApplicationException {
		assert schemeMonitoringSolution != null : NON_NULL_EXPECTED;
		assert schemeMonitoringSolution.getParentSchemeId().equals(this) : REMOVAL_OF_AN_ABSENT_PROHIBITED;
		schemeMonitoringSolution.setParentScheme(null, usePool);
	}

	/**
	 * Returns a {@code Set} of {@code SchemeMonitoringSolution}s
	 * <em>directly</em> referencing this {@code Scheme}, i. e. having
	 * <em>no</em> parent {@code SchemeOptimizeInfo}. To get the
	 * <em>entire</em> {@code Set} of {@code SchemeMonitoringSolution}s
	 * that belong to this {@code Scheme}, use
	 * {@link #getSchemeMonitoringSolutionsRecursively(boolean)} instead.
	 * 
	 * @param usePool
	 * @throws ApplicationException
	 * @see #getSchemeMonitoringSolutionsRecursively(boolean)
	 */
	public Set<SchemeMonitoringSolution> getSchemeMonitoringSolutions(
			final boolean usePool)
	throws ApplicationException {
		return Collections.unmodifiableSet(this.getSchemeMonitoringSolutions0(usePool));
	}

	/**
	 * @param usePool
	 * @throws ApplicationException
	 */
	private Set<SchemeMonitoringSolution> getSchemeMonitoringSolutions0(
			final boolean usePool)
	throws ApplicationException {
		return this.getSchemeMonitoringSolutionContainerWrappee().getContainees(usePool);
	}

	/**
	 * @param schemeMonitoringSolutions
	 * @param usePool
	 * @throws ApplicationException
	 */
	public void setSchemeMonitoringSolutions(
			final Set<SchemeMonitoringSolution> schemeMonitoringSolutions,
			final boolean usePool)
	throws ApplicationException {
		assert schemeMonitoringSolutions != null : NON_NULL_EXPECTED;

		final Set<SchemeMonitoringSolution> oldSchemeMonitoringSolutions = this.getSchemeMonitoringSolutions0(usePool);

		final Set<SchemeMonitoringSolution> toRemove = new HashSet<SchemeMonitoringSolution>(oldSchemeMonitoringSolutions);
		toRemove.removeAll(schemeMonitoringSolutions);
		for (final SchemeMonitoringSolution schemeMonitoringSolution : toRemove) {
			this.removeSchemeMonitoringSolution(schemeMonitoringSolution, usePool);
		}

		final Set<SchemeMonitoringSolution> toAdd = new HashSet<SchemeMonitoringSolution>(schemeMonitoringSolutions);
		toAdd.removeAll(oldSchemeMonitoringSolutions);
		for (final SchemeMonitoringSolution schemeMonitoringSolution : toAdd) {
			this.addSchemeMonitoringSolution(schemeMonitoringSolution, usePool);
		}
	}

	/*-********************************************************************
	 * Non-model members.                                                 *
	 **********************************************************************/

	public Set<SchemeMonitoringSolution> getSchemeMonitoringSolutionsRecursively(
			final boolean usePool)
	throws ApplicationException {
		return Collections.unmodifiableSet(this.getSchemeMonitoringSolutionsRecursively0(usePool));
	}

	/**
	 * @param usePool
	 * @throws ApplicationException
	 */
	Set<SchemeMonitoringSolution> getSchemeMonitoringSolutionsRecursively0(
			final boolean usePool)
	throws ApplicationException {
		final Set<SchemeMonitoringSolution> schemeMonitoringSolutions =
				new HashSet<SchemeMonitoringSolution>();
		schemeMonitoringSolutions.addAll(this.getSchemeMonitoringSolutions(usePool));
		for (final SchemeOptimizeInfo schemeOptimizeInfo : this.getSchemeOptimizeInfos(usePool)) {
			schemeMonitoringSolutions.addAll(schemeOptimizeInfo.getSchemeMonitoringSolutions(usePool));
		}
		return schemeMonitoringSolutions;
	}

	/**
	 * @param usePool
	 * @throws ApplicationException
	 */
	public Set<SchemeLink> getSchemeLinksRecursively(final boolean usePool)
	throws ApplicationException {
		final Set<SchemeLink> schemeLinks = new HashSet<SchemeLink>();
		schemeLinks.addAll(this.getSchemeLinks0(usePool));
		for (final SchemeElement schemeElement : this.getSchemeElements0(usePool)) {
			schemeLinks.addAll(schemeElement.getSchemeLinksRecursively(usePool));
		}
		return Collections.unmodifiableSet(schemeLinks);
	}

	/**
	 * @param usePool
	 * @throws ApplicationException
	 */
	public Set<SchemeCableLink> getSchemeCableLinksRecursively(final boolean usePool)
	throws ApplicationException {
		final Set<SchemeCableLink> schemeCableLinks = new HashSet<SchemeCableLink>();
		schemeCableLinks.addAll(this.getSchemeCableLinks0(usePool));
		for (final SchemeElement schemeElement : this.getSchemeElements0(usePool)) {
			schemeCableLinks.addAll(schemeElement.getSchemeCableLinksRecursively(usePool));
		}
		return Collections.unmodifiableSet(schemeCableLinks);
	}

	/**
	 * To get the {@code Set} of {@code SchemePath}s for the current
	 * {@code SchemeMonitoringSolution} only, use
	 * {@link #getCurrentSchemeMonitoringSolution(boolean)}.{@link SchemeMonitoringSolution#getSchemePaths(boolean) getSchemePaths(boolean)}
	 * instead.
	 *
	 * @param usePool
	 * @throws ApplicationException
	 */
	public Set<SchemePath> getSchemePathsRecursively(final boolean usePool)
	throws ApplicationException {
		final Set<SchemePath> schemePaths = new HashSet<SchemePath>();
		for (final SchemeMonitoringSolution schemeMonitoringSolution : this.getSchemeMonitoringSolutionsRecursively(usePool)) {
			schemePaths.addAll(schemeMonitoringSolution.getSchemePaths(usePool));
		}
		return Collections.unmodifiableSet(schemePaths);
	}

	/*-********************************************************************
	 * Shitlets                                                           *
	 **********************************************************************/

	/**
	 * @param usePool
	 * @throws ApplicationException
	 * @deprecated
	 */
	@Shitlet
	@Deprecated
	public Set<SchemeCableLink> getTopologicalSchemeCableLinksRecursively(final boolean usePool)
	throws ApplicationException {
		final Set<SchemeCableLink> schemeCableLinks = new HashSet<SchemeCableLink>();
		schemeCableLinks.addAll(this.getSchemeCableLinks0(usePool));
		for (final SchemeElement schemeElement : this.getSchemeElements0(usePool)) {
			for (final Scheme scheme : schemeElement.getSchemes0(usePool)) {
				if (scheme.getKind() == CABLE_SUBNETWORK) {
					schemeCableLinks.addAll(scheme.getTopologicalSchemeCableLinksRecursively(usePool));
				}
			}
		}
		return Collections.unmodifiableSet(schemeCableLinks);
	}

	/**
	 * @param usePool
	 * @throws ApplicationException
	 * @deprecated
	 */
	@Shitlet
	@Deprecated
	public Set<SchemePath> getTopologicalSchemePathsRecursively(final boolean usePool)
	throws ApplicationException {
		final Set<SchemePath> schemePaths = new HashSet<SchemePath>();
		final SchemeMonitoringSolution currentSchemeMonitoringSolution = this.getCurrentSchemeMonitoringSolution(usePool);
		if (currentSchemeMonitoringSolution != null) {
			schemePaths.addAll(currentSchemeMonitoringSolution.getSchemePaths0(usePool));
		}
		for (final SchemeElement schemeElement : this.getSchemeElements0(usePool)) {
			for (final Scheme scheme : schemeElement.getSchemes0(usePool)) {
				if (scheme.getKind() == CABLE_SUBNETWORK) {
					schemePaths.addAll(scheme.getTopologicalSchemePathsRecursively(usePool));
				}
			}
		}
		return Collections.unmodifiableSet(schemePaths);
	}

	/**
	 * @param usePool
	 * @throws ApplicationException
	 * @deprecated
	 */
	@Shitlet
	@Deprecated
	public Set<SchemeElement> getTopLevelSchemeElementsRecursively(final boolean usePool)
	throws ApplicationException {
		final Set<SchemeElement> schemeElements = new HashSet<SchemeElement>();
		for (final SchemeElement schemeElement : this.getSchemeElements0(usePool)) {
			final Set<Scheme> schemes = schemeElement.getSchemes0(usePool);
			if (schemes.isEmpty()) {
				schemeElements.add(schemeElement);
			} else {
				for (final Scheme scheme : schemes) {
					schemeElements.addAll(scheme.getTopLevelSchemeElementsRecursively(usePool));
				}
			}
		}
		return Collections.unmodifiableSet(schemeElements);
	}

	/**
	 * @param usePool
	 * @throws ApplicationException
	 * @deprecated
	 */
	@Shitlet
	@Deprecated
	public Set<SchemeElement> getTopologicalSchemeElementsRecursively(final boolean usePool)
	throws ApplicationException {
		final Set<SchemeElement> schemeElements = new HashSet<SchemeElement>();
		for (final SchemeElement schemeElement : this.getSchemeElements0(usePool)) {
			final Set<Scheme> schemes = schemeElement.getSchemes0(usePool);
			if (schemes.isEmpty()) {
				schemeElements.add(schemeElement);
			} else {
				for (final Scheme scheme: schemes) {
					if (scheme.getKind() == CABLE_SUBNETWORK) {
						schemeElements.addAll(scheme.getTopologicalSchemeElementsRecursively(usePool));
					} else {
						schemeElements.add(schemeElement);
					}
				}
			}
		}
		return Collections.unmodifiableSet(schemeElements);
	}

	/**
	 * @param schemeElement
	 * @param usePool
	 * @throws ApplicationException
	 * @deprecated
	 */
	@Shitlet
	@Deprecated
	public SchemeElement getTopologicalSchemeElement(final SchemeElement schemeElement,
			final boolean usePool)
	throws ApplicationException {
		if (schemeElement.getParentSchemeId().equals(this)) {
			return schemeElement;
		}
		for (final SchemeElement schemeElement1 : this.getSchemeElements0(usePool)) {
			for (final Scheme scheme : schemeElement1.getSchemes0(usePool)) {
				if (schemeElement1.getChildSchemeElementsRecursively(usePool).contains(schemeElement)) {
					return schemeElement1;
				}
				final SchemeElement schemeElement2 = scheme.getTopologicalSchemeElement(schemeElement, usePool);
				if (schemeElement2 != null) {
					return scheme.getKind() == CABLE_SUBNETWORK ? schemeElement2 : schemeElement;
				}
			}
		}
		return null;
	}

	/**
	 * @param schemeLinkId
	 * @param usePool
	 * @throws ApplicationException
	 * @deprecated
	 */
	@Shitlet
	@Deprecated
	public boolean containsSchemeLink(final Identifier schemeLinkId,
			final boolean usePool)
	throws ApplicationException {
		if (this.getSchemeLinks0(usePool).contains(schemeLinkId)) {
			return true;
		}
		for (final SchemeElement schemeElement : this.getSchemeElements0(usePool)) {
			for (final Scheme scheme : schemeElement.getSchemes0(usePool)) {
				if (schemeElement.containsSchemeLink(schemeLinkId, usePool)
						|| scheme.containsSchemeLink(schemeLinkId, usePool)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * @param schemeCableLinkId
	 * @param usePool
	 * @throws ApplicationException
	 * @deprecated
	 */
	@Shitlet
	@Deprecated
	public boolean containsSchemeCableLink(final Identifier schemeCableLinkId,
			final boolean usePool)
	throws ApplicationException {
		if (this.getSchemeCableLinks0(usePool).contains(schemeCableLinkId)) {
			return true;
		}
		for (final SchemeElement schemeElement : this.getSchemeElements0(usePool)) {
			for (final Scheme scheme : schemeElement.getSchemes0(usePool)) {
				if (scheme.containsSchemeCableLink(schemeCableLinkId, usePool)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * @param schemeElement
	 * @param usePool
	 * @throws ApplicationException
	 * @deprecated
	 */
	@Shitlet
	@Deprecated
	public boolean containsSchemeElement(final SchemeElement schemeElement,
			final boolean usePool)
	throws ApplicationException {
		if (schemeElement.getParentSchemeId().equals(this)) {
			return true;
		}
		for (final SchemeElement schemeElement1 : this.getSchemeElements0(usePool)) {
			for (final Scheme scheme : schemeElement1.getSchemes0(usePool)) {
				if ((schemeElement1.containsSchemeElement(schemeElement, usePool))
						|| scheme.containsSchemeElement(schemeElement, usePool)) {
					return true;
				}
			}
		}
		return false;
	}
}
