/*-
 * $Id: Scheme.java,v 1.19 2005/04/19 17:45:16 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme;

import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

import org.omg.CORBA.portable.IDLEntity;

import com.syrus.AMFICOM.administration.AbstractCloneableDomainMember;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Describable;
import com.syrus.AMFICOM.general.ErrorMessages;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.resource.BitmapImageResource;
import com.syrus.AMFICOM.resource.ResourceStorableObjectPool;
import com.syrus.AMFICOM.resource.SchemeImageResource;
import com.syrus.AMFICOM.scheme.corba.Scheme_Transferable;
import com.syrus.AMFICOM.scheme.corba.Scheme_TransferablePackage.Kind;
import com.syrus.util.Log;

/**
 * #03 in hierarchy.
 *
 * @author $Author: bass $
 * @version $Revision: 1.19 $, $Date: 2005/04/19 17:45:16 $
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

	private Identifier parentSchemeElementId;

	private SchemeDatabase schemeDatabase;

	/**
	 * @param id
	 * @throws RetrieveObjectException
	 * @throws ObjectNotFoundException
	 */
	Scheme(final Identifier id) throws RetrieveObjectException, ObjectNotFoundException {
		super(id);

		this.schemeDatabase = SchemeDatabaseContext.getSchemeDatabase();
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
	 * @param currentSchemeMonitoringSolution
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
			final SchemeMonitoringSolution currentSchemeMonitoringSolution,
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
		this.currentSchemeMonitoringSolutionId = Identifier.possiblyVoid(currentSchemeMonitoringSolution);
		this.parentSchemeElementId = Identifier.possiblyVoid(parentSchemeElement);

		this.schemeDatabase = SchemeDatabaseContext.getSchemeDatabase();
	}

	/**
	 * @param transferable
	 * @throws CreateObjectException
	 */
	Scheme(final Scheme_Transferable transferable) throws CreateObjectException {
		this.schemeDatabase = SchemeDatabaseContext.getSchemeDatabase();
		fromTransferable(transferable);
	}

	/**
	 * A shorthand for
	 * {@link #createInstance(Identifier, String, String, String, int, int, Kind, Identifier, Map, BitmapImageResource, SchemeImageResource, SchemeImageResource, SchemeMonitoringSolution, SchemeElement)}.
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
		return createInstance(creatorId, name, "", "", DEFAULT_WIDTH, //$NON-NLS-1$ //$NON-NLS-2$
				DEFAULT_HEIGHT, kind, domainId, null, null,
				null, null, null, null);
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
	 * @param currentSchemeMonitoringSolution
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
			final SchemeMonitoringSolution currentSchemeMonitoringSolution,
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
							.getGeneratedIdentifier(ObjectEntities.SCHEME_ENTITY_CODE),
					created, created, creatorId, creatorId,
					0L, domainId, name, description, label, width,
					height, kind, map, symbol,
					ugoCell, schemeCell,
					currentSchemeMonitoringSolution,
					parentSchemeElement);
			scheme.changed = true;
			return scheme;
		} catch (final IdentifierGenerationException ige) {
			throw new CreateObjectException(
					"Scheme.createInstance | cannot generate identifier ", ige); //$NON-NLS-1$
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

	public Object clone() {
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
	public Set getDependencies() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see Describable#getDescription()
	 */
	public String getDescription() {
		assert this.description != null : ErrorMessages.OBJECT_NOT_INITIALIZED;
		return this.description;
	}

	public int getHeight() {
		throw new UnsupportedOperationException();
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
		throw new UnsupportedOperationException();
	}

	/**
	 * @see com.syrus.AMFICOM.general.Namable#getName()
	 */
	public String getName() {
		assert this.name != null && this.name.length() != 0 : ErrorMessages.OBJECT_NOT_INITIALIZED;
		return this.name;
	}

	public SchemeElement getParentSchemeElement() {
		throw new UnsupportedOperationException();
	}

	public Set getSchemeCableLinks() {
		try {
			return Collections.unmodifiableSet(SchemeStorableObjectPool.getStorableObjectsByCondition(new LinkedIdsCondition(this.id, ObjectEntities.SCHEME_CABLE_LINK_ENTITY_CODE), true));
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
		if (this.schemeCellId.isVoid())
			return null;
		try {
			return (SchemeImageResource) ResourceStorableObjectPool
					.getStorableObject(this.schemeCellId, true);
		} catch (final ApplicationException ae) {
			Log.debugException(ae, Log.SEVERE);
			return null;
		}
	}

	public Set getSchemeElements() {
		try {
			return Collections.unmodifiableSet(SchemeStorableObjectPool.getStorableObjectsByCondition(new LinkedIdsCondition(this.id, ObjectEntities.SCHEME_ELEMENT_ENTITY_CODE), true));
		} catch (final ApplicationException ae) {
			Log.debugException(ae, Log.SEVERE);
			return Collections.EMPTY_SET;
		}
	}

	public Kind getKind() {
		throw new UnsupportedOperationException();
	}

	public Set getSchemeLinks() {
		try {
			return Collections.unmodifiableSet(SchemeStorableObjectPool.getStorableObjectsByCondition(new LinkedIdsCondition(this.id, ObjectEntities.SCHEME_LINK_ENTITY_CODE), true));
		} catch (final ApplicationException ae) {
			Log.debugException(ae, Log.SEVERE);
			return Collections.EMPTY_SET;
		}
	}

	public Set getSchemeOptimizeInfos() {
		try {
			return Collections.unmodifiableSet(SchemeStorableObjectPool.getStorableObjectsByCondition(new LinkedIdsCondition(this.id, ObjectEntities.SCHEME_OPTIMIZE_INFO_ENTITY_CODE), true));
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
		if (this.symbolId.isVoid())
			return null;
		try {
			return (BitmapImageResource) ResourceStorableObjectPool
					.getStorableObject(this.symbolId, true);
		} catch (final ApplicationException ae) {
			Log.debugException(ae, Log.SEVERE);
			return null;
		}
	}

	/**
	 * @see com.syrus.AMFICOM.general.TransferableObject#getTransferable()
	 */
	public IDLEntity getTransferable() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see SchemeCellContainer#getUgoCell()
	 */
	public SchemeImageResource getUgoCell() {
		assert this.ugoCellId != null: ErrorMessages.OBJECT_NOT_INITIALIZED;
		if (this.ugoCellId.isVoid())
			return null;
		try {
			return (SchemeImageResource) ResourceStorableObjectPool
					.getStorableObject(this.ugoCellId, true);
		} catch (final ApplicationException ae) {
			Log.debugException(ae, Log.SEVERE);
			return null;
		}
	}

	public int getWidth() {
		throw new UnsupportedOperationException();
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
	 * @param currentSchemeMonitoringSolutionId
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
			final Identifier currentSchemeMonitoringSolutionId,
			final Identifier parentSchemeElementId) {
		assert name != null && name.length() != 0: ErrorMessages.NON_EMPTY_EXPECTED;
		assert description != null: ErrorMessages.NON_NULL_EXPECTED;
		assert label != null: ErrorMessages.NON_NULL_EXPECTED;
		assert kind != null: ErrorMessages.NON_NULL_EXPECTED;
		assert mapId != null: ErrorMessages.NON_NULL_EXPECTED;
		assert symbolId != null: ErrorMessages.NON_NULL_EXPECTED;
		assert ugoCellId != null: ErrorMessages.NON_NULL_EXPECTED;
		assert schemeCellId != null: ErrorMessages.NON_NULL_EXPECTED;
		assert currentSchemeMonitoringSolutionId != null: ErrorMessages.NON_NULL_EXPECTED;
		assert parentSchemeElementId != null: ErrorMessages.NON_NULL_EXPECTED;
		super.setAttributes(created, modified, creatorId, modifierId, version, domainId);
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
		this.currentSchemeMonitoringSolutionId = currentSchemeMonitoringSolutionId;
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
		this.changed = true;
	}

	public void setHeight(final int height) {
		throw new UnsupportedOperationException();
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
		this.changed = true;
	}

	public void setMap(final Map map) {
		throw new UnsupportedOperationException();
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
		this.changed = true;
	}

	public void setParentSchemeElement(final SchemeElement parentSchemeElement) {
		throw new UnsupportedOperationException();
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
		this.changed = true;
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

	public void setKind(final Kind schemeKind) {
		throw new UnsupportedOperationException();
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
		this.changed = true;
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
		this.changed = true;
	}

	public void setWidth(final int width) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param transferable
	 * @throws CreateObjectException 
	 * @see com.syrus.AMFICOM.general.StorableObject#fromTransferable(IDLEntity)
	 */
	protected void fromTransferable(final IDLEntity transferable) throws CreateObjectException {
		final Scheme_Transferable scheme = (Scheme_Transferable) transferable;
		try {
			super.fromTransferable(scheme.header, new Identifier(scheme.domainId));
		} catch (final ApplicationException ae) {
			throw new CreateObjectException(ae);
		}
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
		this.currentSchemeMonitoringSolutionId = new Identifier(scheme.currentSchemeMonitoringSolutionId);
		this.parentSchemeElementId = new Identifier(scheme.parentSchemeElementId);
	}
}
