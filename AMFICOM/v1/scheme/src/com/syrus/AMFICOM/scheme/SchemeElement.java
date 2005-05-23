/*-
 * $Id: SchemeElement.java,v 1.30 2005/05/23 10:01:26 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme;

import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.omg.CORBA.portable.IDLEntity;

import com.syrus.AMFICOM.configuration.Equipment;
import com.syrus.AMFICOM.configuration.EquipmentType;
import com.syrus.AMFICOM.configuration.KIS;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.ErrorMessages;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.corba.CharacteristicSort;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.map.SiteNode;
import com.syrus.AMFICOM.resource.BitmapImageResource;
import com.syrus.AMFICOM.resource.SchemeImageResource;
import com.syrus.AMFICOM.scheme.corba.SchemeElement_Transferable;
import com.syrus.util.Log;

/**
 * #04 in hierarchy.
 *
 * @author $Author: bass $
 * @version $Revision: 1.30 $, $Date: 2005/05/23 10:01:26 $
 * @module scheme_v1
 */
public final class SchemeElement extends AbstractSchemeElement implements
		SchemeCellContainer {
	private static final long serialVersionUID = 3618977875802797368L;

	private Identifier equipmentId;

	private Identifier equipmentTypeId;

	private Identifier kisId;

	private String label;

	private Identifier parentSchemeElementId;

	/**
	 * Takes non-null value at pack time.
	 */
	private Identifier schemeCellId;

	private Identifier siteNodeId;

	private Identifier symbolId;

	/**
	 * Takes non-null value at pack time.
	 */
	private Identifier ugoCellId;

	private SchemeElementDatabase schemeElementDatabase;

	private boolean equipmentTypeSet = false;

	/**
	 * @param id
	 * @throws RetrieveObjectException
	 * @throws ObjectNotFoundException
	 */
	SchemeElement(final Identifier id) throws RetrieveObjectException, ObjectNotFoundException {
		super(id);

		this.schemeElementDatabase = SchemeDatabaseContext.getSchemeElementDatabase();
		try {
			this.schemeElementDatabase.retrieve(this);
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
	 * @param name
	 * @param description
	 * @param label
	 * @param equipmentType
	 * @param equipment
	 * @param kis
	 * @param siteNode
	 * @param symbol
	 * @param ugoCell
	 * @param schemeCell
	 * @param parentScheme
	 * @param parentSchemeElement
	 */
	SchemeElement(final Identifier id, final Date created,
			final Date modified, final Identifier creatorId,
			final Identifier modifierId, final long version,
			final String name, final String description,
			final String label, final EquipmentType equipmentType,
			final Equipment equipment, final KIS kis,
			final SiteNode siteNode, final BitmapImageResource symbol,
			final SchemeImageResource ugoCell,
			final SchemeImageResource schemeCell,
			final Scheme parentScheme,
			final SchemeElement parentSchemeElement) {
		super(id, created, modified, creatorId, modifierId, version,
				name, description, parentScheme);
		this.label = label;

		assert equipmentType == null || equipment == null;
		this.equipmentTypeId = Identifier.possiblyVoid(equipmentType);
		this.equipmentId = Identifier.possiblyVoid(equipment);

		this.kisId = Identifier.possiblyVoid(kis);
		this.siteNodeId = Identifier.possiblyVoid(siteNode);
		this.symbolId = Identifier.possiblyVoid(symbol);
		this.ugoCellId = Identifier.possiblyVoid(ugoCell);
		this.schemeCellId = Identifier.possiblyVoid(schemeCell);

		assert parentScheme == null || parentSchemeElement == null: ErrorMessages.EXACTLY_ONE_PARENT_REQUIRED;
		this.parentSchemeElementId = Identifier.possiblyVoid(parentSchemeElement);

		this.schemeElementDatabase = SchemeDatabaseContext.getSchemeElementDatabase();
	}

	/**
	 * @param transferable
	 * @throws CreateObjectException
	 */
	SchemeElement(final SchemeElement_Transferable transferable) throws CreateObjectException {
		this.schemeElementDatabase = SchemeDatabaseContext.getSchemeElementDatabase();
		fromTransferable(transferable);
	}

	/**
	 * A shorthand for
	 * {@link #createInstance(Identifier, String, String, String, EquipmentType, Equipment, KIS, SiteNode, BitmapImageResource, SchemeImageResource, SchemeImageResource, Scheme)}.
	 *
	 * @param creatorId
	 * @param name
	 * @param parentScheme
	 * @throws CreateObjectException
	 */
	public static SchemeElement createInstance(final Identifier creatorId,
			final String name, final Scheme parentScheme)
			throws CreateObjectException {
		return createInstance(creatorId, name, "", "", null, null,
				null, null, null, null, null, parentScheme);
	}

	/**
	 * A shorthand for
	 * {@link #createInstance(Identifier, String, String, String, EquipmentType, Equipment, KIS, SiteNode, BitmapImageResource, SchemeImageResource, SchemeImageResource, SchemeElement)}.
	 *
	 * @param creatorId
	 * @param name
	 * @param parentSchemeElement
	 * @throws CreateObjectException
	 */
	public static SchemeElement createInstance(final Identifier creatorId,
			final String name,
			final SchemeElement parentSchemeElement)
			throws CreateObjectException {
		return createInstance(creatorId, name, "", "", null, null,
				null, null, null, null, null, parentSchemeElement);
	}

	/**
	 * @param creatorId
	 * @param name can be neither <code>null</code> nor empty.
	 * @param description cannot be <code>null</code>, but can be empty.
	 * @param label cannot be <code>null</code>, but can be empty.
	 * @param equipmentType
	 * @param equipment
	 * @param kis
	 * @param siteNode
	 * @param symbol
	 * @param ugoCell
	 * @param schemeCell
	 * @param parentScheme
	 * @throws CreateObjectException
	 */
	public static SchemeElement createInstance(final Identifier creatorId, final String name,
			final String description, final String label,
			final EquipmentType equipmentType,
			final Equipment equipment,
			final KIS kis,
			final SiteNode siteNode,
			final BitmapImageResource symbol,
			final SchemeImageResource ugoCell,
			final SchemeImageResource schemeCell,
			final Scheme parentScheme)
			throws CreateObjectException {
		assert creatorId != null && !creatorId.isVoid(): ErrorMessages.NON_VOID_EXPECTED;
		assert name != null && name.length() != 0: ErrorMessages.NON_EMPTY_EXPECTED;
		assert description != null: ErrorMessages.NON_NULL_EXPECTED;
		assert label != null: ErrorMessages.NON_NULL_EXPECTED;
		assert parentScheme != null: ErrorMessages.NON_NULL_EXPECTED;

		try {
			final Date created = new Date();
			final SchemeElement schemeElement = new SchemeElement(
					IdentifierPool
							.getGeneratedIdentifier(ObjectEntities.SCHEME_ELEMENT_ENTITY_CODE),
					created, created, creatorId, creatorId,
					0L, name, description, label, equipmentType, equipment, kis, siteNode, symbol, ugoCell, schemeCell, parentScheme, null);
			schemeElement.changed = true;
			if (equipment != null || equipmentType != null)
				schemeElement.equipmentTypeSet = true;
			return schemeElement;
		} catch (final IdentifierGenerationException ige) {
			throw new CreateObjectException(
					"SchemeElement.createInstance | cannot generate identifier ", ige);
		}
	}

	/**
	 * @param creatorId
	 * @param name can be neither <code>null</code> nor empty.
	 * @param description cannot be <code>null</code>, but can be empty.
	 * @param label cannot be <code>null</code>, but can be empty.
	 * @param equipmentType
	 * @param equipment
	 * @param kis
	 * @param siteNode
	 * @param symbol
	 * @param ugoCell
	 * @param schemeCell
	 * @param parentSchemeElement
	 * @throws CreateObjectException
	 */
	public static SchemeElement createInstance(final Identifier creatorId,
			final String name, final String description,
			final String label, final EquipmentType equipmentType,
			final Equipment equipment, final KIS kis,
			final SiteNode siteNode,
			final BitmapImageResource symbol,
			final SchemeImageResource ugoCell,
			final SchemeImageResource schemeCell,
			final SchemeElement parentSchemeElement)
			throws CreateObjectException {
		assert creatorId != null && !creatorId.isVoid(): ErrorMessages.NON_VOID_EXPECTED;
		assert name != null && name.length() != 0: ErrorMessages.NON_EMPTY_EXPECTED;
		assert description != null: ErrorMessages.NON_NULL_EXPECTED;
		assert label != null: ErrorMessages.NON_NULL_EXPECTED;
		assert parentSchemeElement != null: ErrorMessages.NON_NULL_EXPECTED;

		try {
			final Date created = new Date();
			final SchemeElement schemeElement = new SchemeElement(
					IdentifierPool
							.getGeneratedIdentifier(ObjectEntities.SCHEME_ELEMENT_ENTITY_CODE),
					created, created, creatorId, creatorId,
					0L, name, description, label, equipmentType, equipment, kis, siteNode, symbol, ugoCell, schemeCell, null, parentSchemeElement);
			schemeElement.changed = true;
			if (equipment != null || equipmentType != null)
				schemeElement.equipmentTypeSet = true;
			return schemeElement;
		} catch (final IdentifierGenerationException ige) {
			throw new CreateObjectException(
					"SchemeElement.createInstance | cannot generate identifier ", ige);
		}
	}

	/**
	 * @param scheme cannot be <code>null</code>.
	 */
	public void addScheme(final Scheme scheme) {
		assert scheme != null: ErrorMessages.NON_NULL_EXPECTED;
		scheme.setParentSchemeElement(this);
	}

	/**
	 * @param schemeDevice cannot be <code>null</code>.
	 */
	public void addSchemeDevice(final SchemeDevice schemeDevice) {
		assert schemeDevice != null: ErrorMessages.NON_NULL_EXPECTED;
		schemeDevice.setParentSchemeElement(this);
	}

	/**
	 * @param schemeElement can be neither <code>null</code> nor
	 *        <code>this</code>.
	 */
	public void addSchemeElement(final SchemeElement schemeElement) {
		assert schemeElement != null: ErrorMessages.NON_NULL_EXPECTED;
		assert schemeElement != this: ErrorMessages.CIRCULAR_DEPS_PROHIBITED;
		schemeElement.setParentSchemeElement(this);
	}

	/**
	 * @param schemeLink cannot be <code>null</code>.
	 */
	public void addSchemeLink(final SchemeLink schemeLink) {
		assert schemeLink != null: ErrorMessages.NON_NULL_EXPECTED;
		schemeLink.setParentSchemeElement(this);
	}

	public Object clone() {
		final SchemeElement schemeElement = (SchemeElement) super
				.clone();
		/**
		 * @todo Update the newly created object.
		 */
		return schemeElement;
	}

	/**
	 * @see com.syrus.AMFICOM.general.Characterizable#getCharacteristicSort()
	 */
	public CharacteristicSort getCharacteristicSort() {
		return CharacteristicSort.CHARACTERISTIC_SORT_SCHEMEELEMENT;
	}

	/**
	 * @see com.syrus.AMFICOM.general.StorableObject#getDependencies()
	 */
	public Set getDependencies() {
		assert this.equipmentId != null && this.equipmentTypeId != null
				&& this.kisId != null
				&& this.parentSchemeElementId != null
				&& this.schemeCellId != null
				&& this.siteNodeId != null && this.symbolId != null
				&& this.ugoCellId != null: ErrorMessages.OBJECT_NOT_INITIALIZED;
		final Set dependencies = new HashSet();
		dependencies.addAll(super.getDependencies());
		dependencies.add(this.equipmentId);
		dependencies.add(this.equipmentTypeId);
		dependencies.add(this.kisId);
		dependencies.add(this.parentSchemeElementId);
		dependencies.add(this.schemeCellId);
		dependencies.add(this.siteNodeId);
		dependencies.add(this.symbolId);
		dependencies.add(this.ugoCellId);
		dependencies.remove(null);
		dependencies.remove(Identifier.VOID_IDENTIFIER);
		return Collections.unmodifiableSet(dependencies);
	}

	public Equipment getEquipment() {
		assert this.assertEquipmentTypeSetStrict(): ErrorMessages.OBJECT_BADLY_INITIALIZED;

		try {
			return (Equipment) StorableObjectPool.getStorableObject(this.equipmentId, true);
		} catch (final ApplicationException ae) {
			Log.debugException(ae, Log.SEVERE);
			return null;
		}
	}

	public EquipmentType getEquipmentType() {
		assert this.assertEquipmentTypeSetStrict(): ErrorMessages.OBJECT_BADLY_INITIALIZED;

		if (!this.equipmentId.isVoid())
			return (EquipmentType) getEquipment().getType();

		try {
			return (EquipmentType) StorableObjectPool.getStorableObject(this.equipmentTypeId, true);
		} catch (final ApplicationException ae) {
			Log.debugException(ae, Log.SEVERE);
			return null;
		}
	}

	public KIS getKis() {
		assert this.kisId != null: ErrorMessages.OBJECT_NOT_INITIALIZED;
		try {
			return (KIS) StorableObjectPool.getStorableObject(this.kisId, true);
		} catch (final ApplicationException ae) {
			Log.debugException(ae, Log.SEVERE);
			return null;
		}
	}

	/**
	 * @return this <code>SchemeElement</code>&apos;s label, or
	 *         empty string if none. Never returns <code>null</code>s.
	 */
	public String getLabel() {
		assert this.label != null: ErrorMessages.OBJECT_NOT_INITIALIZED;
		return this.label;
	}

	/**
	 * @see AbstractSchemeElement#getParentScheme()
	 */
	public Scheme getParentScheme() {
		assert super.parentSchemeId != null && this.parentSchemeElementId != null: ErrorMessages.OBJECT_NOT_INITIALIZED;
		assert super.parentSchemeId.isVoid() ^ this.parentSchemeElementId.isVoid(): ErrorMessages.EXACTLY_ONE_PARENT_REQUIRED;

		if (super.parentSchemeId.isVoid()) {
			Log.debugMessage("SchemeElement.getParentScheme() | Parent Scheme was requested, while parent is a SchemeElement; returning null",
					Log.FINE);
			return null;
		}

		return super.getParentScheme();
	}

	public SchemeElement getParentSchemeElement() {
		assert super.parentSchemeId != null && this.parentSchemeElementId != null: ErrorMessages.OBJECT_NOT_INITIALIZED;
		assert super.parentSchemeId.isVoid() ^ this.parentSchemeElementId.isVoid(): ErrorMessages.EXACTLY_ONE_PARENT_REQUIRED;

		if (this.parentSchemeElementId.isVoid()) {
			Log.debugMessage("SchemeElement.getParentSchemeElement() | Parent SchemeElement was requested, while parent is a Scheme; returnung null",
					Log.FINE);
			return null;
		}
		
		try {
			return (SchemeElement) StorableObjectPool.getStorableObject(this.parentSchemeElementId, true);
		} catch (final ApplicationException ae) {
			Log.debugException(ae, Log.SEVERE);
			return null;
		}
	}

	/**
	 * @return <em>the first</em> <code>Scheme</code> inner to this
	 *         <code>SchemeElement</code>, or <code>null</code> if
	 *         none.
	 */
	public Scheme getScheme() {
		final Iterator schemeIterator = getSchemes().iterator();
		if (schemeIterator.hasNext())
			return (Scheme) schemeIterator.next();
		return null;
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
	 * @return an immutable set.
	 */
	public Set getSchemeDevices() {
		try {
			return Collections.unmodifiableSet(StorableObjectPool.getStorableObjectsByCondition(new LinkedIdsCondition(this.id, ObjectEntities.SCHEME_DEVICE_ENTITY_CODE), true));
		} catch (final ApplicationException ae) {
			Log.debugException(ae, Log.SEVERE);
			return Collections.EMPTY_SET;
		}
	}

	/**
	 * @return an immutable set.
	 */
	public Set getSchemeElements() {
		try {
			return Collections.unmodifiableSet(StorableObjectPool.getStorableObjectsByCondition(new LinkedIdsCondition(this.id, ObjectEntities.SCHEME_ELEMENT_ENTITY_CODE), true));
		} catch (final ApplicationException ae) {
			Log.debugException(ae, Log.SEVERE);
			return Collections.EMPTY_SET;
		}
	}

	/**
	 * @return an immutable set.
	 */
	public Set getSchemeLinks() {
		try {
			return Collections.unmodifiableSet(StorableObjectPool.getStorableObjectsByCondition(new LinkedIdsCondition(this.id, ObjectEntities.SCHEME_LINK_ENTITY_CODE), true));
		} catch (final ApplicationException ae) {
			Log.debugException(ae, Log.SEVERE);
			return Collections.EMPTY_SET;
		}
	}

	/**
	 * @return an immutable set.
	 */
	public Set getSchemes() {
		try {
			return Collections.unmodifiableSet(StorableObjectPool.getStorableObjectsByCondition(new LinkedIdsCondition(this.id, ObjectEntities.SCHEME_ENTITY_CODE), true));
		} catch (final ApplicationException ae) {
			Log.debugException(ae, Log.SEVERE);
			return Collections.EMPTY_SET;
		}
	}

	public SiteNode getSiteNode() {
		assert this.siteNodeId != null: ErrorMessages.OBJECT_NOT_INITIALIZED;
		try {
			return (SiteNode) StorableObjectPool.getStorableObject(this.siteNodeId, true);
		} catch (final ApplicationException ae) {
			Log.debugException(ae, Log.SEVERE);
			return null;
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
	 * @see com.syrus.AMFICOM.general.TransferableObject#getTransferable()
	 */
	public IDLEntity getTransferable() {
		return new SchemeElement_Transferable(getHeaderTransferable(),
				super.getName(),
				super.getDescription(),
				this.label,
				(Identifier_Transferable) this.equipmentTypeId.getTransferable(),
				(Identifier_Transferable) this.equipmentId.getTransferable(),
				(Identifier_Transferable) this.kisId.getTransferable(),
				(Identifier_Transferable) this.siteNodeId.getTransferable(),
				(Identifier_Transferable) this.symbolId.getTransferable(),
				(Identifier_Transferable) this.ugoCellId.getTransferable(),
				(Identifier_Transferable) this.schemeCellId.getTransferable(),
				(Identifier_Transferable) super.parentSchemeId.getTransferable(),
				(Identifier_Transferable) this.parentSchemeElementId.getTransferable(),
				Identifier.createTransferables(super.getCharacteristics()));
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

	/**
	 * The <code>Scheme</code> must belong to this
	 * <code>SchemeElement</code>, or crap will meet the fan.
	 *
	 * @param scheme
	 */
	public void removeScheme(final Scheme scheme) {
		assert scheme != null: ErrorMessages.NON_NULL_EXPECTED;
		assert getSchemes().contains(scheme): ErrorMessages.REMOVAL_OF_AN_ABSENT_PROHIBITED;
		scheme.setParentSchemeElement(null);
	}

	/**
	 * The <code>SchemeDevice</code> must belong to this
	 * <code>SchemeElement</code>, or crap will meet the fan.
	 *
	 * @param schemeDevice
	 */
	public void removeSchemeDevice(final SchemeDevice schemeDevice) {
		assert schemeDevice != null: ErrorMessages.NON_NULL_EXPECTED;
		assert getSchemeDevices().contains(schemeDevice): ErrorMessages.REMOVAL_OF_AN_ABSENT_PROHIBITED;
		schemeDevice.setParentSchemeElement(null);
	}

	/**
	 * The <code>SchemeElement</code> must belong to this
	 * <code>SchemeElement</code>, or crap will meet the fan.
	 *
	 * @param schemeElement
	 */
	public void removeSchemeElement(final SchemeElement schemeElement) {
		assert schemeElement != null: ErrorMessages.NON_NULL_EXPECTED;
		assert getSchemeElements().contains(schemeElement): ErrorMessages.REMOVAL_OF_AN_ABSENT_PROHIBITED;
		schemeElement.setParentSchemeElement(null);
	}

	/**
	 * The <code>SchemeLink</code> must belong to this
	 * <code>SchemeElement</code>, or crap will meet the fan.
	 *
	 * @param schemeLink
	 */
	public void removeSchemeLink(final SchemeLink schemeLink) {
		assert schemeLink != null: ErrorMessages.NON_NULL_EXPECTED;
		assert getSchemeLinks().contains(schemeLink): ErrorMessages.REMOVAL_OF_AN_ABSENT_PROHIBITED;
		schemeLink.setParentSchemeElement(null);
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
	 * @param equipmentTypeId
	 * @param equipmentId
	 * @param kisId
	 * @param siteNodeId
	 * @param symbolId
	 * @param ugoCellId
	 * @param schemeCellId
	 * @param parentSchemeId
	 * @param parentSchemeElementId
	 */
	synchronized void setAttributes(final Date created, final Date modified,
			final Identifier creatorId,
			final Identifier modifierId, final long version,
			final String name, final String description,
			final String label, final Identifier equipmentTypeId,
			final Identifier equipmentId, final Identifier kisId,
			final Identifier siteNodeId,
			final Identifier symbolId, final Identifier ugoCellId,
			final Identifier schemeCellId,
			final Identifier parentSchemeId,
			final Identifier parentSchemeElementId) {
		super.setAttributes(created, modified, creatorId, modifierId, version, name, description, parentSchemeId);

		assert label != null: ErrorMessages.NON_NULL_EXPECTED;

		assert equipmentTypeId != null: ErrorMessages.NON_NULL_EXPECTED;
		assert equipmentId != null: ErrorMessages.NON_NULL_EXPECTED;
		assert equipmentTypeId.isVoid() ^ equipmentId.isVoid();

		assert kisId != null: ErrorMessages.NON_NULL_EXPECTED;
		assert siteNodeId != null: ErrorMessages.NON_NULL_EXPECTED;
		assert symbolId != null: ErrorMessages.NON_NULL_EXPECTED;
		assert ugoCellId != null: ErrorMessages.NON_NULL_EXPECTED;
		assert schemeCellId != null: ErrorMessages.NON_NULL_EXPECTED;
		
		assert parentSchemeElementId != null: ErrorMessages.NON_NULL_EXPECTED;
		assert parentSchemeId.isVoid() ^ parentSchemeElementId.isVoid();
		
		this.label = label;
		this.equipmentTypeId = equipmentTypeId;
		this.equipmentId = equipmentId;
		this.kisId = kisId;
		this.siteNodeId = siteNodeId;
		this.symbolId = symbolId;
		this.ugoCellId = ugoCellId;
		this.schemeCellId = schemeCellId;
		this.parentSchemeElementId = parentSchemeElementId;
	}

	/**
	 * @param equipment
	 */
	public void setEquipment(final Equipment equipment) {
		assert this.assertEquipmentTypeSetNonStrict(): ErrorMessages.OBJECT_BADLY_INITIALIZED;

		final Identifier newEquipmentId = Identifier.possiblyVoid(equipment);
		if (this.equipmentId.equals(newEquipmentId)) {
			Log.debugMessage(ErrorMessages.ACTION_WILL_RESULT_IN_NOTHING, Log.INFO);
			return;
		}

		if (this.equipmentId.isVoid())
			/*
			 * Erasing old object-type value, setting new object
			 * value.
			 */
			this.equipmentTypeId = Identifier.VOID_IDENTIFIER;
		else if (newEquipmentId.isVoid())
			/*
			 * Erasing old object value, preserving old object-type
			 * value. This point is not assumed to be reached unless
			 * initial object value has already been set (i. e.
			 * there already is object-type value to preserve).
			 */
			this.equipmentTypeId = this.getEquipment().getType().getId();
		this.equipmentId = newEquipmentId;
		this.changed = true;
	}

	/**
	 * @param equipmentType
	 */
	public void setEquipmentType(final EquipmentType equipmentType) {
		assert this.assertEquipmentTypeSetNonStrict(): ErrorMessages.OBJECT_BADLY_INITIALIZED;
		assert equipmentType != null: ErrorMessages.NON_NULL_EXPECTED;

		if (!this.equipmentId.isVoid())
			this.getEquipment().setType(equipmentType);
		else {
			final Identifier newEquipmentTypeId = equipmentType.getId();
			if (this.equipmentTypeId.equals(newEquipmentTypeId)) {
				Log.debugMessage(ErrorMessages.ACTION_WILL_RESULT_IN_NOTHING, Log.INFO);
				return;
			}
			this.equipmentTypeId = newEquipmentTypeId;
			this.changed = true;
		}
	}

	/**
	 * @param kis
	 */
	public void setKis(final KIS kis) {
		final Identifier newKisId = Identifier.possiblyVoid(kis);
		if (this.kisId.equals(newKisId))
			return;
		this.kisId = newKisId;
		this.changed = true;
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

	/**
	 * @param parentScheme
	 * @see AbstractSchemeElement#setParentScheme(Scheme)
	 */
	public void setParentScheme(final Scheme parentScheme) {
		assert super.parentSchemeId != null && this.parentSchemeElementId != null: ErrorMessages.OBJECT_NOT_INITIALIZED;
		assert super.parentSchemeId.isVoid() ^ this.parentSchemeElementId.isVoid(): ErrorMessages.EXACTLY_ONE_PARENT_REQUIRED;

		if (!super.parentSchemeId.isVoid())
			/*
			 * Moving from a scheme to another scheme.
			 */
			super.setParentScheme(parentScheme);
		else {
			/*
			 * Moving from a scheme element to a scheme.
			 */
			if (parentScheme == null) {
				Log.debugMessage(ErrorMessages.ACTION_WILL_RESULT_IN_NOTHING, Log.INFO);
				return;
			}
			super.parentSchemeId = parentScheme.getId();
			this.parentSchemeElementId = Identifier.VOID_IDENTIFIER;
			this.changed = true;
		}
	}

	public void setParentSchemeElement(final SchemeElement parentSchemeElement) {
		assert super.parentSchemeId != null && this.parentSchemeElementId != null: ErrorMessages.OBJECT_NOT_INITIALIZED;
		assert super.parentSchemeId.isVoid() ^ this.parentSchemeElementId.isVoid(): ErrorMessages.EXACTLY_ONE_PARENT_REQUIRED;

		Identifier newParentSchemeElementId;
		if (!super.parentSchemeId.isVoid()) {
			/*
			 * Moving from a scheme to a scheme element.
			 */
			if (parentSchemeElement == null) {
				Log.debugMessage(ErrorMessages.ACTION_WILL_RESULT_IN_NOTHING, Log.INFO);
				return;
			}
			newParentSchemeElementId = parentSchemeElement.getId();
			super.parentSchemeId = Identifier.VOID_IDENTIFIER;
		} else {
			/*
			 * Moving from a scheme element to another scheme element.
			 */
			if (parentSchemeElement == null) {
				Log.debugMessage(ErrorMessages.OBJECT_WILL_DELETE_ITSELF_FROM_POOL, Log.WARNING);
				StorableObjectPool.delete(this.id);
				return;
			}
			newParentSchemeElementId = parentSchemeElement.getId();
			if (this.parentSchemeElementId.equals(newParentSchemeElementId))
				return;
		}
		this.parentSchemeElementId = newParentSchemeElementId;
		this.changed = true;
	}

	public void setScheme(final Scheme scheme) {
		setSchemes(scheme == null
				? Collections.EMPTY_SET
				: Collections.singleton(scheme));
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

	public void setSchemeDevices(final Set schemeDevices) {
		assert schemeDevices != null: ErrorMessages.NON_NULL_EXPECTED;
		for (final Iterator oldSchemeDeviceIterator = getSchemeDevices().iterator(); oldSchemeDeviceIterator.hasNext();) {
			final SchemeDevice oldSchemeDevice = (SchemeDevice) oldSchemeDeviceIterator.next();
			/*
			 * Check is made to prevent SchemeDevices from
			 * permanently losing their parents.
			 */
			assert !schemeDevices.contains(oldSchemeDevice);
			removeSchemeDevice(oldSchemeDevice);
		}
		for (final Iterator schemeDeviceIterator = schemeDevices.iterator(); schemeDeviceIterator.hasNext();)
			addSchemeDevice((SchemeDevice) schemeDeviceIterator.next());
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

	/**
	 * @param schemes
	 * @see Scheme#setSchemeElements(Set)
	 * @todo Check for circular depsendencies.
	 */
	public void setSchemes(final Set schemes) {
		assert schemes != null: ErrorMessages.NON_NULL_EXPECTED;
		for (final Iterator oldSchemeIterator = getSchemes().iterator(); oldSchemeIterator.hasNext();)
			removeScheme((Scheme) oldSchemeIterator.next());
		for (final Iterator schemeIterator = schemes.iterator(); schemeIterator.hasNext();)
			addScheme((Scheme) schemeIterator.next());
	}

	public void setSiteNode(final SiteNode siteNode) {
		final Identifier newSiteNodeId = Identifier.possiblyVoid(siteNode);
		if (this.siteNodeId.equals(newSiteNodeId))
			return;
		this.siteNodeId = newSiteNodeId;
		this.changed = true;
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

	/**
	 * @param transferable
	 * @throws CreateObjectException
	 * @see com.syrus.AMFICOM.general.StorableObject#fromTransferable(IDLEntity)
	 */
	protected void fromTransferable(final IDLEntity transferable) throws CreateObjectException {
		final SchemeElement_Transferable schemeElement = (SchemeElement_Transferable) transferable;
		super.fromTransferable(schemeElement.header, schemeElement.name,
				schemeElement.description,
				schemeElement.parentSchemeId,
				schemeElement.characteristicIds);
		this.label = schemeElement.label;
		this.equipmentTypeId = new Identifier(schemeElement.equipmentTypeId);
		this.equipmentId = new Identifier(schemeElement.equipmentId);
		this.kisId = new Identifier(schemeElement.kisId);
		this.siteNodeId = new Identifier(schemeElement.siteNodeId);
		this.symbolId = new Identifier(schemeElement.symbolId);
		this.ugoCellId = new Identifier(schemeElement.ugoCellId);
		this.schemeCellId = new Identifier(schemeElement.schemeCellId);
		this.parentSchemeElementId = new Identifier(schemeElement.parentSchemeElementId);
	}

	/*-********************************************************************
	 * Non-model members.                                                 *
	 **********************************************************************/

	/**
	 * Invoked by modifier methods.
	 */
	private boolean assertEquipmentTypeSetNonStrict() {
		if (this.equipmentTypeSet)
			return this.assertEquipmentTypeSetStrict();
		this.equipmentTypeSet = true;
		return this.equipmentId != null
				&& this.equipmentTypeId != null
				&& this.equipmentId.isVoid()
				&& this.equipmentTypeId.isVoid();
	}

	/**
	 * Invoked by accessor methods (it is assumed that object is already
	 * initialized).
	 */
	private boolean assertEquipmentTypeSetStrict() {
		return this.equipmentId != null
				&& this.equipmentTypeId != null
				&& (this.equipmentId.isVoid() ^ this.equipmentTypeId.isVoid());
	}

	public Set getSchemeCablePorts() {
		final Set schemeCablePorts = new HashSet();
		for (final Iterator schemeDeviceIterator = getSchemeDevices().iterator(); schemeDeviceIterator.hasNext();)
			schemeCablePorts.addAll(((SchemeDevice) schemeDeviceIterator.next()).getSchemeCablePorts());
		return Collections.unmodifiableSet(schemeCablePorts);
	}

	public Set getSchemePorts() {
		final Set schemePorts = new HashSet();
		for (final Iterator schemeDeviceIterator = getSchemeDevices().iterator(); schemeDeviceIterator.hasNext();)
			schemePorts.addAll(((SchemeDevice) schemeDeviceIterator.next()).getSchemePorts());
		return Collections.unmodifiableSet(schemePorts);
	}

	public SchemePath getAlarmedPath() {
		throw new UnsupportedOperationException("Method not implemented");
	}

	public PathElement getAlarmedPathElement() {
		throw new UnsupportedOperationException("Method not implemented");
	}

	public void setAlarmedPath(final SchemePath alarmedPath) {
		throw new UnsupportedOperationException("Method not implemented");
	}

	public void setAlarmedPathElement(final PathElement alarmedPathElement) {
		throw new UnsupportedOperationException("Method not implemented");
	}
}
