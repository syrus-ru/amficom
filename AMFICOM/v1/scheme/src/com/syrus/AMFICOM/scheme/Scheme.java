/*-
 * $Id: Scheme.java,v 1.44 2005/06/27 09:49:24 max Exp $
 *
 * Copyright � 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme;

import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.omg.CORBA.ORB;
import org.omg.CORBA.portable.IDLEntity;

import com.syrus.AMFICOM.administration.AbstractCloneableDomainMember;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.DatabaseContext;
import com.syrus.AMFICOM.general.Describable;
import com.syrus.AMFICOM.general.ErrorMessages;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.resource.BitmapImageResource;
import com.syrus.AMFICOM.resource.SchemeImageResource;
import com.syrus.AMFICOM.scheme.corba.IdlScheme;
import com.syrus.AMFICOM.scheme.corba.IdlSchemePackage.Kind;
import com.syrus.util.Log;

/**
 * #03 in hierarchy.
 *
 * @author $Author: max $
 * @version $Revision: 1.44 $, $Date: 2005/06/27 09:49:24 $
 * @module scheme_v1
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

	private Kind kind;

	private Identifier mapId;

	private Identifier symbolId;

	private Identifier ugoCellId;

	private Identifier schemeCellId;

	private Identifier currentSchemeMonitoringSolutionId;

	Identifier parentSchemeElementId;

	private SchemeDatabase schemeDatabase;

	/**
	 * @param id
	 * @throws RetrieveObjectException
	 * @throws ObjectNotFoundException
	 */
	Scheme(final Identifier id) throws RetrieveObjectException, ObjectNotFoundException {
		super(id);

		this.schemeDatabase = (SchemeDatabase) DatabaseContext.getDatabase(ObjectEntities.SCHEME_CODE);
		try {
			this.schemeDatabase.retrieve(this);
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
	Scheme(final Identifier id, final Date created, final Date modified,
			final Identifier creatorId, final Identifier modifierId,
			final long version, final Identifier domainId,
			final String name, final String description,
			final String label, final int width, final int height,
			final Kind kind, final Map map,
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

		this.schemeDatabase = (SchemeDatabase) DatabaseContext.getDatabase(ObjectEntities.SCHEME_CODE);
	}

	/**
	 * @param transferable
	 */
	Scheme(final IdlScheme transferable) {
		this.schemeDatabase = (SchemeDatabase) DatabaseContext.getDatabase(ObjectEntities.SCHEME_CODE);
		fromTransferable(transferable);
	}

	/**
	 * A shorthand for
	 * {@link #createInstance(Identifier, String, String, String, int, int, Kind, Identifier, Map, BitmapImageResource, SchemeImageResource, SchemeImageResource, SchemeElement)}.
	 *
	 * @param creatorId
	 * @param name
	 * @param kind
	 * @param domainId
	 * @throws CreateObjectException
	 */
	public static Scheme createInstance(final Identifier creatorId,
			final String name, final Kind kind,
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
			final String name, final String description,
			final String label, final int width, final int height,
			final Kind kind, final Identifier domainId,
			final Map map, final BitmapImageResource symbol,
			final SchemeImageResource ugoCell,
			final SchemeImageResource schemeCell,
			final SchemeElement parentSchemeElement)
			throws CreateObjectException {
		assert creatorId != null && !creatorId.isVoid(): ErrorMessages.NON_VOID_EXPECTED;
		assert name != null && name.length() != 0: ErrorMessages.NON_EMPTY_EXPECTED;
		assert description != null: ErrorMessages.NON_NULL_EXPECTED;
		assert label != null: ErrorMessages.NON_NULL_EXPECTED;
		assert domainId != null && !domainId.isVoid(): ErrorMessages.NON_VOID_EXPECTED;
		assert kind != null: ErrorMessages.NON_NULL_EXPECTED;

		try {
			final Date created = new Date();
			final Scheme scheme = new Scheme(
					IdentifierPool
							.getGeneratedIdentifier(ObjectEntities.SCHEME_CODE),
					created, created, creatorId, creatorId,
					0L, domainId, name, description, label, width,
					height, kind, map, symbol,
					ugoCell, schemeCell,
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
		assert schemeCableLink != null: ErrorMessages.NON_NULL_EXPECTED;
		schemeCableLink.setParentScheme(this);
	}

	/**
	 * @param schemeElement cannot be <code>null</code>.
	 */
	public void addSchemeElement(final SchemeElement schemeElement) {
		assert schemeElement != null: ErrorMessages.NON_NULL_EXPECTED;
		schemeElement.setParentScheme(this);
	}

	/**
	 * @param schemeLink cannot be <code>null</code>.
	 */
	public void addSchemeLink(final SchemeLink schemeLink) {
		assert schemeLink != null: ErrorMessages.NON_NULL_EXPECTED;
		schemeLink.setParentScheme(this);
	}

	/**
	 * @param schemeOptimizeInfo cannot be <code>null</code>.
	 */
	public void addSchemeOptimizeInfo(final SchemeOptimizeInfo schemeOptimizeInfo) {
		assert schemeOptimizeInfo != null: ErrorMessages.NON_NULL_EXPECTED;
		schemeOptimizeInfo.setParentScheme(this);
	}

	@Override
	public Scheme clone() {
		final Scheme scheme = (Scheme) super.clone();
		/**
		 * @todo Update the newly created object.
		 */
		return scheme;
	}

	public SchemeMonitoringSolution getCurrentSchemeMonitoringSolution() {
		throw new UnsupportedOperationException();
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
				&& this.currentSchemeMonitoringSolutionId != null
				&& this.parentSchemeElementId != null: ErrorMessages.OBJECT_NOT_INITIALIZED;
		final Set<Identifiable> dependencies = new HashSet<Identifiable>();
		dependencies.add(this.mapId);
		dependencies.add(this.symbolId);
		dependencies.add(this.ugoCellId);
		dependencies.add(this.schemeCellId);
		dependencies.add(this.parentSchemeElementId);
		dependencies.remove(null);
		dependencies.remove(Identifier.VOID_IDENTIFIER);
		return Collections.unmodifiableSet(dependencies);
	}

	/**
	 * @see Describable#getDescription()
	 */
	public String getDescription() {
		assert this.description != null : ErrorMessages.OBJECT_NOT_INITIALIZED;
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
		assert this.label != null: ErrorMessages.OBJECT_NOT_INITIALIZED;
		return this.label;
	}

	public Map getMap() {
		assert this.mapId != null: ErrorMessages.OBJECT_NOT_INITIALIZED;
		try {
			return (Map) StorableObjectPool.getStorableObject(this.mapId, true);
		} catch (final ApplicationException ae) {
			Log.debugException(ae, Log.SEVERE);
			return null;
		}
	}

	/**
	 * @see com.syrus.AMFICOM.general.Namable#getName()
	 */
	public String getName() {
		assert this.name != null && this.name.length() != 0 : ErrorMessages.OBJECT_NOT_INITIALIZED;
		return this.name;
	}

	public SchemeElement getParentSchemeElement() {
		assert this.parentSchemeElementId != null: ErrorMessages.OBJECT_NOT_INITIALIZED;
		try {
			return (SchemeElement) StorableObjectPool.getStorableObject(this.parentSchemeElementId, true);
		} catch (final ApplicationException ae) {
			Log.debugException(ae, Log.SEVERE);
			return null;
		}
	}

	/**
	 * @todo parameter breakOnLoadError to StorableObjectPool.getStorableObjectsByCondition
	 */
	public Set getSchemeCableLinks() {
		try {
			return Collections.unmodifiableSet(StorableObjectPool.getStorableObjectsByCondition(new LinkedIdsCondition(this.id, ObjectEntities.SCHEMECABLELINK_CODE), true, true));
		} catch (final ApplicationException ae) {
			Log.debugException(ae, Log.SEVERE);
			return Collections.EMPTY_SET;
		}
	}

	/**
	 * @see SchemeCellContainer#getSchemeCell()
	 */
	public SchemeImageResource getSchemeCell() {
		assert this.schemeCellId != null: ErrorMessages.OBJECT_NOT_INITIALIZED;
		try {
			return (SchemeImageResource) StorableObjectPool
					.getStorableObject(this.schemeCellId, true);
		} catch (final ApplicationException ae) {
			Log.debugException(ae, Log.SEVERE);
			return null;
		}
	}

	/**
	 * @todo parameter breakOnLoadError to StorableObjectPool.getStorableObjectsByCondition
	 */
	public Set getSchemeElements() {
		try {
			return Collections.unmodifiableSet(StorableObjectPool.getStorableObjectsByCondition(new LinkedIdsCondition(this.id, ObjectEntities.SCHEMEELEMENT_CODE), true, true));
		} catch (final ApplicationException ae) {
			Log.debugException(ae, Log.SEVERE);
			return Collections.EMPTY_SET;
		}
	}

	public Kind getKind() {
		assert this.kind != null: ErrorMessages.OBJECT_NOT_INITIALIZED;
		return this.kind;
	}

	/**
	 * @todo parameter breakOnLoadError to StorableObjectPool.getStorableObjectsByCondition
	 */
	public Set getSchemeLinks() {
		try {
			return Collections.unmodifiableSet(StorableObjectPool.getStorableObjectsByCondition(new LinkedIdsCondition(this.id, ObjectEntities.SCHEMELINK_CODE), true, true));
		} catch (final ApplicationException ae) {
			Log.debugException(ae, Log.SEVERE);
			return Collections.EMPTY_SET;
		}
	}

	/**
	 * @todo parameter breakOnLoadError to StorableObjectPool.getStorableObjectsByCondition
	 */
	public Set getSchemeOptimizeInfos() {
		try {
			return Collections.unmodifiableSet(StorableObjectPool.getStorableObjectsByCondition(new LinkedIdsCondition(this.id, ObjectEntities.SCHEMEOPTIMIZEINFO_CODE), true, true));
		} catch (final ApplicationException ae) {
			Log.debugException(ae, Log.SEVERE);
			return Collections.EMPTY_SET;
		}
	}

	/**
	 * @see SchemeSymbolContainer#getSymbol()
	 */
	public BitmapImageResource getSymbol() {
		assert this.symbolId != null: ErrorMessages.OBJECT_NOT_INITIALIZED;
		try {
			return (BitmapImageResource) StorableObjectPool
					.getStorableObject(this.symbolId, true);
		} catch (final ApplicationException ae) {
			Log.debugException(ae, Log.SEVERE);
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
		return new IdlScheme(super.getHeaderTransferable(orb),
				this.name, this.description, this.label,
				this.width, this.height, this.kind,
				super.getDomainId().getTransferable(),
				this.mapId.getTransferable(),
				this.symbolId.getTransferable(),
				this.ugoCellId.getTransferable(),
				this.schemeCellId.getTransferable(),
				this.parentSchemeElementId.getTransferable());
	}

	/**
	 * @see SchemeCellContainer#getUgoCell()
	 */
	public SchemeImageResource getUgoCell() {
		assert this.ugoCellId != null: ErrorMessages.OBJECT_NOT_INITIALIZED;
		try {
			return (SchemeImageResource) StorableObjectPool
					.getStorableObject(this.ugoCellId, true);
		} catch (final ApplicationException ae) {
			Log.debugException(ae, Log.SEVERE);
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
		assert schemeCableLink != null: ErrorMessages.NON_NULL_EXPECTED;
		assert getSchemeCableLinks().contains(schemeCableLink): ErrorMessages.REMOVAL_OF_AN_ABSENT_PROHIBITED;
		schemeCableLink.setParentScheme(null);
	}

	/**
	 * The <code>SchemeElement</code> must belong to this
	 * <code>Scheme</code>, or crap will meet the fan.
	 *
	 * @param schemeElement
	 */
	public void removeSchemeElement(final SchemeElement schemeElement) {
		assert schemeElement != null: ErrorMessages.NON_NULL_EXPECTED;
		assert getSchemeElements().contains(schemeElement): ErrorMessages.REMOVAL_OF_AN_ABSENT_PROHIBITED;
		schemeElement.setParentScheme(null);
	}

	/**
	 * The <code>SchemeLink</code> must belong to this <code>Scheme</code>,
	 * or crap will meet the fan.
	 *
	 * @param schemeLink
	 */
	public void removeSchemeLink(final SchemeLink schemeLink) {
		assert schemeLink != null: ErrorMessages.NON_NULL_EXPECTED;
		assert getSchemeLinks().contains(schemeLink): ErrorMessages.REMOVAL_OF_AN_ABSENT_PROHIBITED;
		schemeLink.setParentScheme(null);
	}

	/**
	 * The <code>SchemeOptimizeInfo</code> must belong to this
	 * <code>Scheme</code>, or crap will meet the fan.
	 *
	 * @param schemeOptimizeInfo
	 */
	public void removeSchemeOptimizeInfo(final SchemeOptimizeInfo schemeOptimizeInfo) {
		assert schemeOptimizeInfo != null: ErrorMessages.NON_NULL_EXPECTED;
		assert getSchemeOptimizeInfos().contains(schemeOptimizeInfo): ErrorMessages.REMOVAL_OF_AN_ABSENT_PROHIBITED;
		schemeOptimizeInfo.setParentScheme(null);
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
	synchronized void setAttributes(final Date created, final Date modified,
			final Identifier creatorId,
			final Identifier modifierId, final long version,
			final String name, final String description,
			final String label, final int width, final int height,
			final Kind kind, final Identifier domainId,
			final Identifier mapId, final Identifier symbolId,
			final Identifier ugoCellId,
			final Identifier schemeCellId,
			final Identifier parentSchemeElementId) {
		super.setAttributes(created, modified, creatorId, modifierId, version, domainId);

		assert name != null && name.length() != 0: ErrorMessages.NON_EMPTY_EXPECTED;
		assert description != null: ErrorMessages.NON_NULL_EXPECTED;
		assert label != null: ErrorMessages.NON_NULL_EXPECTED;
		assert kind != null: ErrorMessages.NON_NULL_EXPECTED;
		assert mapId != null: ErrorMessages.NON_NULL_EXPECTED;
		assert symbolId != null: ErrorMessages.NON_NULL_EXPECTED;
		assert ugoCellId != null: ErrorMessages.NON_NULL_EXPECTED;
		assert schemeCellId != null: ErrorMessages.NON_NULL_EXPECTED;
		assert parentSchemeElementId != null: ErrorMessages.NON_NULL_EXPECTED;

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
		throw new UnsupportedOperationException();
	}

	/**
	 * @see Describable#setDescription(String)
	 */
	public void setDescription(final String description) {
		assert this.description != null : ErrorMessages.OBJECT_NOT_INITIALIZED;
		assert description != null : ErrorMessages.NON_NULL_EXPECTED;
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
		assert this.label != null: ErrorMessages.OBJECT_NOT_INITIALIZED;
		assert label != null: ErrorMessages.NON_NULL_EXPECTED;
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
		assert this.name != null && this.name.length() != 0 : ErrorMessages.OBJECT_NOT_INITIALIZED;
		assert name != null && name.length() != 0 : ErrorMessages.NON_EMPTY_EXPECTED;
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

	public void setSchemeCableLinks(final Set schemeCableLinks) {
		assert schemeCableLinks != null: ErrorMessages.NON_NULL_EXPECTED;
		for (final Iterator oldSchemeCableLinkIterator = getSchemeCableLinks().iterator(); oldSchemeCableLinkIterator.hasNext();) {
			final SchemeCableLink oldSchemeCableLink = (SchemeCableLink) oldSchemeCableLinkIterator.next();
			/*
			 * Check is made to prevent SchemeCableLinks from
			 * permanently losing their parents.
			 */
			assert !schemeCableLinks.contains(oldSchemeCableLink);
			removeSchemeCableLink(oldSchemeCableLink);
		}
		for (final Iterator schemeCableLinkIterator = schemeCableLinks.iterator(); schemeCableLinkIterator.hasNext();)
			addSchemeCableLink((SchemeCableLink) schemeCableLinkIterator.next());
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

	public void setSchemeElements(final Set schemeElements) {
		assert schemeElements != null: ErrorMessages.NON_NULL_EXPECTED;
		for (final Iterator oldSchemeElementIterator = getSchemeElements().iterator(); oldSchemeElementIterator.hasNext();) {
			final SchemeElement oldSchemeElement = (SchemeElement) oldSchemeElementIterator.next();
			/*
			 * Check is made to prevent SchemeElements from
			 * permanently losing their parents.
			 */
			assert !schemeElements.contains(oldSchemeElement);
			removeSchemeElement(oldSchemeElement);
		}
		for (final Iterator schemeElementIterator = schemeElements.iterator(); schemeElementIterator.hasNext();)
			addSchemeElement((SchemeElement) schemeElementIterator.next());
	}

	/**
	 * @param kind
	 */
	public void setKind(final Kind kind) {
		assert this.kind != null: ErrorMessages.OBJECT_NOT_INITIALIZED;
		assert kind != null: ErrorMessages.NON_NULL_EXPECTED;
		if (this.kind.value() == kind.value())
			return;
		this.kind = kind;
		super.markAsChanged();
	}

	public void setSchemeLinks(final Set schemeLinks) {
		assert schemeLinks != null: ErrorMessages.NON_NULL_EXPECTED;
		for (final Iterator oldSchemeLinkIterator = getSchemeLinks().iterator(); oldSchemeLinkIterator.hasNext();) {
			final SchemeLink oldSchemeLink = (SchemeLink) oldSchemeLinkIterator.next();
			/*
			 * Check is made to prevent SchemeLinks from
			 * permanently losing their parents.
			 */
			assert !schemeLinks.contains(oldSchemeLink);
			removeSchemeLink(oldSchemeLink);
		}
		for (final Iterator schemeLinkIterator = schemeLinks.iterator(); schemeLinkIterator.hasNext();)
			addSchemeLink((SchemeLink) schemeLinkIterator.next());
	}

	public void setSchemeOptimizeInfos(final Set schemeOptimizeInfos) {
		assert schemeOptimizeInfos != null: ErrorMessages.NON_NULL_EXPECTED;
		for (final Iterator oldSchemeOptimizeInfoIterator = getSchemeOptimizeInfos().iterator(); oldSchemeOptimizeInfoIterator.hasNext();) {
			final SchemeOptimizeInfo oldSchemeOptimizeInfo = (SchemeOptimizeInfo) oldSchemeOptimizeInfoIterator.next();
			/*
			 * Check is made to prevent SchemeOptimizeInfos from
			 * permanently losing their parents.
			 */
			assert !schemeOptimizeInfos.contains(oldSchemeOptimizeInfo);
			removeSchemeOptimizeInfo(oldSchemeOptimizeInfo);
		}
		for (final Iterator schemeOptimizeInfoIterator = schemeOptimizeInfos.iterator(); schemeOptimizeInfoIterator.hasNext();)
			addSchemeOptimizeInfo((SchemeOptimizeInfo) schemeOptimizeInfoIterator.next());
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
	 * @see com.syrus.AMFICOM.general.StorableObject#fromTransferable(IDLEntity)
	 */
	protected void fromTransferable(final IDLEntity transferable) {
		final IdlScheme scheme = (IdlScheme) transferable;
		super.fromTransferable(scheme.header, new Identifier(scheme.domainId));
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

	public void addSchemePath(final SchemePath schemePath) {
		assert schemePath != null : ErrorMessages.NON_NULL_EXPECTED;
		schemePath.setParentScheme(this);
	}

	public void removeSchemePath(final SchemePath schemePath) {
		assert schemePath != null : ErrorMessages.NON_NULL_EXPECTED;
		assert this.getSchemePaths().contains(schemePath) : ErrorMessages.REMOVAL_OF_AN_ABSENT_PROHIBITED;
		schemePath.setParentScheme(null);
	}

	public Set getSchemePaths() {
		try {
			return Collections.unmodifiableSet(StorableObjectPool.getStorableObjectsByCondition(new LinkedIdsCondition(this.id, ObjectEntities.SCHEMEPATH_CODE), true));
		} catch (final ApplicationException ae) {
			Log.debugException(ae, Log.SEVERE);
			return Collections.EMPTY_SET;
		}
	}

	public void setSchemePaths(final Set schemePaths) {
		assert schemePaths != null : ErrorMessages.NON_NULL_EXPECTED;
		for (final Iterator oldSchemePathIterator = this.getSchemePaths().iterator(); oldSchemePathIterator.hasNext();) {
			final SchemePath oldSchemePath = (SchemePath) oldSchemePathIterator.next();
			/*
			 * Check is made to prevent SchemePaths from
			 * permanently losing their parents.
			 */
			assert !schemePaths.contains(oldSchemePath);
			this.removeSchemePath(oldSchemePath);
		}
		for (final Iterator schemePathIterator = schemePaths.iterator(); schemePathIterator.hasNext();)
			this.addSchemePath((SchemePath) schemePathIterator.next());
	}
}
