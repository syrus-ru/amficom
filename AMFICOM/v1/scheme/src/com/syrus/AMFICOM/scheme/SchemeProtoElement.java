/*-
 * $Id: SchemeProtoElement.java,v 1.33 2005/05/23 10:01:25 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.omg.CORBA.portable.IDLEntity;

import com.syrus.AMFICOM.configuration.EquipmentType;
import com.syrus.AMFICOM.general.AbstractCloneableStorableObject;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Characteristic;
import com.syrus.AMFICOM.general.Characterizable;
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
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.corba.CharacteristicSort;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.logic.Item;
import com.syrus.AMFICOM.logic.ItemListener;
import com.syrus.AMFICOM.resource.BitmapImageResource;
import com.syrus.AMFICOM.resource.SchemeImageResource;
import com.syrus.AMFICOM.scheme.corba.SchemeProtoElement_Transferable;
import com.syrus.AMFICOM.scheme.logic.Library;
import com.syrus.AMFICOM.scheme.logic.LibraryEntry;
import com.syrus.util.Log;

/**
 * #02 in hierarchy.
 *
 * @author $Author: bass $
 * @version $Revision: 1.33 $, $Date: 2005/05/23 10:01:25 $
 * @module scheme_v1
 * @todo Implement fireParentChanged() and call it on any setParent*() invocation.
 */
public final class SchemeProtoElement extends AbstractCloneableStorableObject
		implements Describable, SchemeCellContainer, Characterizable,
		LibraryEntry {
	private static final long serialVersionUID = 3689348806202569782L;

	private String name;

	private String description;

	private String label;

	private Identifier equipmentTypeId;

	private Identifier symbolId;

	private Identifier ugoCellId;

	private Identifier schemeCellId;

	private Identifier parentSchemeProtoGroupId;

	private Identifier parentSchemeProtoElementId;

	private SchemeProtoElementDatabase schemeProtoElementDatabase;

	private Set characteristics;

	private boolean parentSet = false;

	private ArrayList itemListeners = new ArrayList();

	/**
	 * @param id
	 * @throws RetrieveObjectException
	 * @throws ObjectNotFoundException
	 */
	SchemeProtoElement(final Identifier id) throws RetrieveObjectException, ObjectNotFoundException {
		super(id);

		this.characteristics = new HashSet();
		this.schemeProtoElementDatabase = SchemeDatabaseContext.getSchemeProtoElementDatabase();
		try {
			this.schemeProtoElementDatabase.retrieve(this);
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
	 * @param symbol
	 * @param ugoCell
	 * @param schemeCell
	 * @param parentSchemeProtoGroup
	 * @param parentSchemeProtoElement
	 */
	SchemeProtoElement(final Identifier id, final Date created,
			final Date modified, final Identifier creatorId,
			final Identifier modifierId, final long version,
			final String name, final String description,
			final String label, final EquipmentType equipmentType,
			final BitmapImageResource symbol,
			final SchemeImageResource ugoCell,
			final SchemeImageResource schemeCell,
			final SchemeProtoGroup parentSchemeProtoGroup,
			final SchemeProtoElement parentSchemeProtoElement) {
		super(id, created, modified, creatorId, modifierId, version);
		this.name = name;
		this.description = description;
		this.label = label;
		this.equipmentTypeId = Identifier.possiblyVoid(equipmentType);
		this.symbolId = Identifier.possiblyVoid(symbol);
		this.ugoCellId = Identifier.possiblyVoid(ugoCell);
		this.schemeCellId = Identifier.possiblyVoid(schemeCell);

		assert parentSchemeProtoGroup == null || parentSchemeProtoElement == null: ErrorMessages.EXACTLY_ONE_PARENT_REQUIRED;
		this.parentSchemeProtoGroupId = Identifier.possiblyVoid(parentSchemeProtoGroup);
		this.parentSchemeProtoElementId = Identifier.possiblyVoid(parentSchemeProtoElement);

		this.characteristics = new HashSet();
		this.schemeProtoElementDatabase = SchemeDatabaseContext.getSchemeProtoElementDatabase();
	}

	/**
	 * @param transferable
	 * @throws CreateObjectException
	 */
	SchemeProtoElement(final SchemeProtoElement_Transferable transferable) throws CreateObjectException {
		this.schemeProtoElementDatabase = SchemeDatabaseContext.getSchemeProtoElementDatabase();
		fromTransferable(transferable);
	}

	/**
	 * A shorthand for
	 * {@link #createInstance(Identifier, String, String, String, EquipmentType, BitmapImageResource, SchemeImageResource, SchemeImageResource)}.
	 * This method breaks some assertions, so clients should consider using
	 * other ones to create a new instance.
	 *
	 * @param creatorId cannot be <code>null</code>.
	 * @param name cannot be <code>null</code>.
	 * @throws CreateObjectException
	 */
	public static SchemeProtoElement createInstance(
			final Identifier creatorId, final String name)
			throws CreateObjectException {
		return createInstance(creatorId, name, "", "", null, null, null,
				null);
	}

	/**
	 * A shorthand for
	 * {@link #createInstance(Identifier, String, String, String, EquipmentType, BitmapImageResource, SchemeImageResource, SchemeImageResource, SchemeProtoElement)}.
	 *
	 * @param creatorId
	 * @param name
	 * @param parentSchemeProtoElement
	 * @throws CreateObjectException
	 */
	public static SchemeProtoElement createInstance(
			final Identifier creatorId, final String name,
			final SchemeProtoElement parentSchemeProtoElement)
			throws CreateObjectException {
		return createInstance(creatorId, name, "", "", null, null, null,
				null, parentSchemeProtoElement);
	}

	/**
	 * A shorthand for
	 * {@link #createInstance(Identifier, String, String, String, EquipmentType, BitmapImageResource, SchemeImageResource, SchemeImageResource, SchemeProtoGroup)}.
	 *
	 * @param creatorId
	 * @param name
	 * @param parentSchemeProtoGroup
	 * @throws CreateObjectException
	 */
	public static SchemeProtoElement createInstance(
			final Identifier creatorId, final String name,
			final SchemeProtoGroup parentSchemeProtoGroup)
			throws CreateObjectException {
		return createInstance(creatorId, name, "", "", null, null, null,
				null, parentSchemeProtoGroup);
	}

	/**
	 * This method breaks some assertions, so clients should consider using
	 * other ones to create a new instance.
	 *
	 * @param creatorId cannot be <code>null</code>.
	 * @param name can be neither <code>null</code> nor empty.
	 * @param description cannot be <code>null</code>, but can be empty.
	 * @param label cannot be <code>null</code>, but can be empty.
	 * @param equipmentType may be <code>null</code>.
	 * @param symbol may be <code>null</code>.
	 * @param ugoCell may be <code>null</code>.
	 * @param schemeCell may be <code>null</code>.
	 * @throws CreateObjectException
	 */
	public static SchemeProtoElement createInstance(
			final Identifier creatorId, final String name,
			final String description, final String label,
			final EquipmentType equipmentType,
			final BitmapImageResource symbol,
			final SchemeImageResource ugoCell,
			final SchemeImageResource schemeCell)
			throws CreateObjectException {
		assert creatorId != null && !creatorId.isVoid(): ErrorMessages.NON_VOID_EXPECTED;
		assert name != null && name.length() != 0: ErrorMessages.NON_EMPTY_EXPECTED;
		assert description != null: ErrorMessages.NON_NULL_EXPECTED;
		assert label != null: ErrorMessages.NON_NULL_EXPECTED;

		try {
			final Date created = new Date();
			final SchemeProtoElement schemeProtoElement = new SchemeProtoElement(
					IdentifierPool
							.getGeneratedIdentifier(ObjectEntities.SCHEME_PROTO_ELEMENT_ENTITY_CODE),
					created, created, creatorId, creatorId,
					0L, name, description, label,
					equipmentType, symbol, ugoCell,
					schemeCell, null, null);
			schemeProtoElement.changed = true;
			return schemeProtoElement;
		} catch (final IdentifierGenerationException ige) {
			throw new CreateObjectException(
					"SchemeProtoElement.createInstance | cannot generate identifier ", ige);
		}
	}

	/**
	 * @param creatorId cannot be <code>null</code>.
	 * @param name cannot be <code>null</code>.
	 * @param description cannot be <code>null</code>, but can be empty.
	 * @param label cannot be <code>null</code>, but can be empty.
	 * @param equipmentType may be <code>null</code>.
	 * @param symbol may be <code>null</code>.
	 * @param ugoCell may be <code>null</code>.
	 * @param schemeCell may be <code>null</code>.
	 * @param parentSchemeProtoElement cannot be <code>null</code>.
	 * @throws CreateObjectException
	 */
	public static SchemeProtoElement createInstance(
			final Identifier creatorId, final String name,
			final String description, final String label,
			final EquipmentType equipmentType,
			final BitmapImageResource symbol,
			final SchemeImageResource ugoCell,
			final SchemeImageResource schemeCell,
			final SchemeProtoElement parentSchemeProtoElement)
			throws CreateObjectException {
		assert creatorId != null && !creatorId.isVoid(): ErrorMessages.NON_VOID_EXPECTED;
		assert name != null && name.length() != 0: ErrorMessages.NON_EMPTY_EXPECTED;
		assert description != null: ErrorMessages.NON_NULL_EXPECTED;
		assert label != null: ErrorMessages.NON_NULL_EXPECTED;
		assert parentSchemeProtoElement != null: ErrorMessages.NON_NULL_EXPECTED;

		try {
			final Date created = new Date();
			final SchemeProtoElement schemeProtoElement = new SchemeProtoElement(
					IdentifierPool
							.getGeneratedIdentifier(ObjectEntities.SCHEME_PROTO_ELEMENT_ENTITY_CODE),
					created, created, creatorId, creatorId,
					0L, name, description, label,
					equipmentType, symbol, ugoCell,
					schemeCell, null,
					parentSchemeProtoElement);
			schemeProtoElement.changed = true;
			schemeProtoElement.parentSet = true;
			return schemeProtoElement;
		} catch (final IdentifierGenerationException ige) {
			throw new CreateObjectException(
					"SchemeProtoElement.createInstance | cannot generate identifier ", ige);
		}
	}

	/**
	 * @param creatorId cannot be <code>null</code>.
	 * @param name cannot be <code>null</code>.
	 * @param description cannot be <code>null</code>, but can be empty.
	 * @param label cannot be <code>null</code>, but can be empty.
	 * @param equipmentType may be <code>null</code>.
	 * @param symbol may be <code>null</code>.
	 * @param ugoCell may be <code>null</code>.
	 * @param schemeCell may be <code>null</code>.
	 * @param parentSchemeProtoGroup cannot be <code>null</code>.
	 * @throws CreateObjectException
	 */
	public static SchemeProtoElement createInstance(
			final Identifier creatorId, final String name,
			final String description, final String label,
			final EquipmentType equipmentType,
			final BitmapImageResource symbol,
			final SchemeImageResource ugoCell,
			final SchemeImageResource schemeCell,
			final SchemeProtoGroup parentSchemeProtoGroup)
			throws CreateObjectException {
		assert creatorId != null && !creatorId.isVoid(): ErrorMessages.NON_VOID_EXPECTED;
		assert name != null && name.length() != 0: ErrorMessages.NON_EMPTY_EXPECTED;
		assert description != null: ErrorMessages.NON_NULL_EXPECTED;
		assert label != null: ErrorMessages.NON_NULL_EXPECTED;
		assert parentSchemeProtoGroup != null: ErrorMessages.NON_NULL_EXPECTED;

		try {
			final Date created = new Date();
			final SchemeProtoElement schemeProtoElement = new SchemeProtoElement(
					IdentifierPool
							.getGeneratedIdentifier(ObjectEntities.SCHEME_PROTO_ELEMENT_ENTITY_CODE),
					created, created, creatorId, creatorId,
					0L, name, description, label,
					equipmentType, symbol, ugoCell,
					schemeCell, parentSchemeProtoGroup,
					null);
			schemeProtoElement.changed = true;
			schemeProtoElement.parentSet = true;
			return schemeProtoElement;
		} catch (final IdentifierGenerationException ige) {
			throw new CreateObjectException(
					"SchemeProtoElement.createInstance | cannot generate identifier ", ige);
		}
	}

	/**
	 * @param itemListener
	 * @see Item#addChangeListener(ItemListener)
	 */
	public void addChangeListener(final ItemListener itemListener) {
		assert !this.itemListeners.contains(itemListener);
		this.itemListeners.add(0, itemListener);
	}

	/**
	 * @param characteristic
	 * @see Characterizable#addCharacteristic(Characteristic)
	 */
	public void addCharacteristic(final Characteristic characteristic) {
		assert characteristic != null: ErrorMessages.NON_NULL_EXPECTED;
		this.characteristics.add(characteristic);
		this.changed = true;
	}

	/**
	 * @param childItem
	 * @see Item#addChild(Item)
	 */
	public void addChild(final Item childItem) {
		throw new UnsupportedOperationException(ErrorMessages.CHILDREN_PROHIBITED);
	}

	/**
	 * @param schemeDevice cannot be <code>null</code>.
	 */
	public void addSchemeDevice(final SchemeDevice schemeDevice) {
		assert schemeDevice != null: ErrorMessages.NON_NULL_EXPECTED;
		schemeDevice.setParentSchemeProtoElement(this);
	}

	/**
	 * @param schemeLink cannot be <code>null</code>.
	 */
	public void addSchemeLink(final SchemeLink schemeLink) {
		assert schemeLink != null: ErrorMessages.NON_NULL_EXPECTED;
		schemeLink.setParentSchemeProtoElement(this);
	}

	/**
	 * @param schemeProtoElement can be neither <code>null</code> nor
	 *        <code>this</code>.
	 */
	public void addSchemeProtoElement(final SchemeProtoElement schemeProtoElement) {
		assert schemeProtoElement != null: ErrorMessages.NON_NULL_EXPECTED;
		assert schemeProtoElement != this: ErrorMessages.CIRCULAR_DEPS_PROHIBITED;
		schemeProtoElement.setParentSchemeProtoElement(this);
	}

	/**
	 * @see Item#canHaveChildren()
	 */
	public boolean canHaveChildren() {
		return getMaxChildrenCount() != 0;
	}

	/**
	 * @see Item#canHaveParent()
	 */
	public boolean canHaveParent() {
		return true;
	}

	public Object clone() {
		final SchemeProtoElement schemeProtoElement = (SchemeProtoElement) super
				.clone();
		/**
		 * @todo Update the newly created object.
		 */
		return schemeProtoElement;
	}

	/**
	 * @see Characterizable#getCharacteristics()
	 */
	public Set getCharacteristics() {
		return Collections.unmodifiableSet(this.characteristics);
	}

	/**
	 * @see Characterizable#getCharacteristicSort()
	 */
	public CharacteristicSort getCharacteristicSort() {
		return CharacteristicSort.CHARACTERISTIC_SORT_SCHEMEPROTOELEMENT;
	}

	/**
	 * @see Item#getChildren()
	 */
	public List getChildren() {
		throw new UnsupportedOperationException(ErrorMessages.CHILDREN_PROHIBITED);
	}

	/**
	 * @see com.syrus.AMFICOM.general.StorableObject#getDependencies()
	 */
	public Set getDependencies() {
		assert this.equipmentTypeId != null
				&& this.symbolId != null
				&& this.ugoCellId != null
				&& this.schemeCellId != null
				&& this.parentSchemeProtoGroupId != null
				&& this.parentSchemeProtoElementId != null: ErrorMessages.OBJECT_NOT_INITIALIZED;
		assert this.parentSchemeProtoGroupId.isVoid() ^ this.parentSchemeProtoElementId.isVoid(): ErrorMessages.EXACTLY_ONE_PARENT_REQUIRED;

		final Set dependencies = new HashSet();
		dependencies.add(this.equipmentTypeId);
		dependencies.add(this.symbolId);
		dependencies.add(this.ugoCellId);
		dependencies.add(this.schemeCellId);
		dependencies.add(this.parentSchemeProtoGroupId);
		dependencies.add(this.parentSchemeProtoElementId);
		dependencies.remove(null);
		dependencies.remove(Identifier.VOID_IDENTIFIER);
		return Collections.unmodifiableSet(dependencies);
	}

	/**
	 * @see Describable#getDescription()
	 */
	public String getDescription() {
		assert this.description != null: ErrorMessages.OBJECT_NOT_INITIALIZED;
		return this.description;
	}

	/**
	 * @return <code>equipmentType</code> associated with this
	 *         <code>schemeProtoElement</code>, or <code>null</code> if
	 *         none.
	 */
	public EquipmentType getEquipmentType() {
		assert this.equipmentTypeId != null: ErrorMessages.OBJECT_NOT_INITIALIZED;
		try {
			return (EquipmentType) StorableObjectPool
					.getStorableObject(this.equipmentTypeId, true);
		} catch (final ApplicationException ae) {
			Log.debugException(ae, Log.SEVERE);
			return null;
		}
	}

	/**
	 * @return this <code>SchemeProtoElement</code>&apos;s label, or
	 *         empty string if none. Never returns <code>null</code>s.
	 */
	public String getLabel() {
		assert this.label != null: ErrorMessages.OBJECT_NOT_INITIALIZED;
		return this.label;
	}

	/**
	 * @see Item#getMaxChildrenCount()
	 */
	public int getMaxChildrenCount() {
		return 0;
	}

	/**
	 * @see com.syrus.AMFICOM.general.Namable#getName()
	 */
	public String getName() {
		assert this.name != null && this.name.length() != 0: ErrorMessages.OBJECT_NOT_INITIALIZED;
		return this.name;
	}

	/**
	 * @return <code>this</code>.
	 * @see Item#getObject()
	 */
	public Object getObject() {
		return this;
	}

	/**
	 * @throws UnsupportedOperationException if this
	 *         <code>schemeProtoElement</code> has no parent
	 *         <code>schemeProtoGroup</code> (i. e. is either orphan
	 *         (which is invalid) or enclosed by another
	 *         <code>schemeProtoElement</code> (which has no relation with
	 *         library hierarchy)).
	 * @see Item#getParent()
	 */
	public Item getParent() {
		final SchemeProtoGroup parentSchemeProtoGroup = getParentSchemeProtoGroup();
		if (parentSchemeProtoGroup == null) {
			if (getParentSchemeProtoElement() == null)
				throw new UnsupportedOperationException(ErrorMessages.EXACTLY_ONE_PARENT_REQUIRED);
			throw new UnsupportedOperationException(ErrorMessages.OUT_OF_LIBRARY_HIERARCHY);
		}
		return parentSchemeProtoGroup;
	}

	public SchemeProtoElement getParentSchemeProtoElement() {
		assert this.assertParentSetStrict(): ErrorMessages.OBJECT_BADLY_INITIALIZED;

		if (this.parentSchemeProtoElementId.isVoid()) {
			Log.debugMessage("SchemeProtoElement.getParentSchemeProtoElement() | Parent SchemeProtoElement was requested, while parent is a SchemeProtoGroup; returning null.",
					Log.FINE);
			return null;
		}

		try {
			return (SchemeProtoElement) StorableObjectPool.getStorableObject(this.parentSchemeProtoElementId, true);
		} catch (final ApplicationException ae) {
			Log.debugException(ae, Log.SEVERE);
			return null;
		}
	}

	public SchemeProtoGroup getParentSchemeProtoGroup() {
		assert this.assertParentSetStrict(): ErrorMessages.OBJECT_BADLY_INITIALIZED;
		
		if (this.parentSchemeProtoGroupId.isVoid()) {
			Log.debugMessage("SchemeProtoElement.getParentSchemeProtoGroup() | Parent SchemeProtoGroup was requested, while parent is a SchemeProtoElement; returnning null",
					Log.FINE);
			return null;
		}

		try {
			return (SchemeProtoGroup) StorableObjectPool.getStorableObject(this.parentSchemeProtoGroupId, true);
		} catch (final ApplicationException ae) {
			Log.debugException(ae, Log.SEVERE);
			return null;
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
	public Set getSchemeProtoElements() {
		try {
			return Collections.unmodifiableSet(StorableObjectPool.getStorableObjectsByCondition(new LinkedIdsCondition(this.id, ObjectEntities.SCHEME_PROTO_ELEMENT_ENTITY_CODE), true));
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
	 * @see com.syrus.AMFICOM.general.TransferableObject#getTransferable()
	 */
	public IDLEntity getTransferable() {
		return new SchemeProtoElement_Transferable(
				super.getHeaderTransferable(),
				this.name,
				this.description,
				this.label,
				(Identifier_Transferable) this.equipmentTypeId.getTransferable(),
				(Identifier_Transferable) this.symbolId.getTransferable(),
				(Identifier_Transferable) this.ugoCellId.getTransferable(),
				(Identifier_Transferable) this.schemeCellId.getTransferable(),
				(Identifier_Transferable) this.parentSchemeProtoGroupId.getTransferable(),
				(Identifier_Transferable) this.parentSchemeProtoElementId.getTransferable(),
				Identifier.createTransferables(this.characteristics));
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
	 * @see Item#isService()
	 */
	public boolean isService() {
		return false;
	}

	/**
	 * @param itemListener
	 * @see Item#removeChangeListener(ItemListener)
	 */
	public void removeChangeListener(final ItemListener itemListener) {
		assert this.itemListeners.contains(itemListener);
		this.itemListeners.remove(itemListener);
	}

	/**
	 * @param characteristic
	 * @see Characterizable#removeCharacteristic(Characteristic)
	 */
	public void removeCharacteristic(final Characteristic characteristic) {
		assert characteristic != null: ErrorMessages.NON_NULL_EXPECTED;
		assert getCharacteristics().contains(characteristic): ErrorMessages.REMOVAL_OF_AN_ABSENT_PROHIBITED;
		this.characteristics.remove(characteristic);
		this.changed = true;
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
		schemeDevice.setParentSchemeProtoElement(null);
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
		schemeLink.setParentSchemeProtoElement(null);
	}

	/**
	 * The <code>SchemeProtoElement</code> must belong to this
	 * <code>SchemeElement</code>, or crap will meet the fan.
	 *
	 * @param schemeProtoElement
	 */
	public void removeSchemeProtoElement(final SchemeProtoElement schemeProtoElement) {
		assert schemeProtoElement != null: ErrorMessages.NON_NULL_EXPECTED;
		assert getSchemeProtoElements().contains(schemeProtoElement): ErrorMessages.REMOVAL_OF_AN_ABSENT_PROHIBITED;
		schemeProtoElement.setParentSchemeProtoElement(null);
	}

	/**
	 * @param created
	 * @param modified
	 * @param creatorId
	 * @param modifierId
	 * @param version
	 * @param name can be neither <code>null</code> nor an empty string.
	 * @param description cannot be <code>null</code>. For this purpose,
	 *        supply an empty string as an argument.
	 * @param label
	 * @param equipmentTypeId
	 * @param symbolId cannot be <code>null</code>. For this purpose,
	 *        supply {@link Identifier#VOID_IDENTIFIER} as an argument.
	 * @param ugoCellId
	 * @param schemeCellId
	 * @param parentSchemeProtoGroupId
	 * @param parentSchemeProtoElementId
	 */
	synchronized void setAttributes(final Date created, final Date modified,
			final Identifier creatorId,
			final Identifier modifierId, final long version,
			final String name, final String description,
			final String label, final Identifier equipmentTypeId,
			final Identifier symbolId, final Identifier ugoCellId,
			final Identifier schemeCellId,
			final Identifier parentSchemeProtoGroupId,
			final Identifier parentSchemeProtoElementId) {
		super.setAttributes(created, modified, creatorId, modifierId, version);

		assert name != null && name.length() != 0: ErrorMessages.NON_EMPTY_EXPECTED;
		assert description != null: ErrorMessages.NON_NULL_EXPECTED;
		assert label != null: ErrorMessages.NON_NULL_EXPECTED;
		assert equipmentTypeId != null: ErrorMessages.NON_NULL_EXPECTED;
		assert symbolId != null: ErrorMessages.NON_NULL_EXPECTED;
		assert ugoCellId != null: ErrorMessages.NON_NULL_EXPECTED;
		assert schemeCellId != null: ErrorMessages.NON_NULL_EXPECTED;

		assert parentSchemeProtoGroupId != null: ErrorMessages.NON_NULL_EXPECTED;
		assert parentSchemeProtoElementId != null: ErrorMessages.NON_NULL_EXPECTED;
		assert parentSchemeProtoGroupId.isVoid() ^ parentSchemeProtoElementId.isVoid();

		this.name = name;
		this.description = description;
		this.label = label;
		this.equipmentTypeId = equipmentTypeId;
		this.symbolId = symbolId;
		this.ugoCellId = ugoCellId;
		this.schemeCellId = schemeCellId;
		this.parentSchemeProtoGroupId = parentSchemeProtoGroupId;
		this.parentSchemeProtoElementId = parentSchemeProtoElementId;
	}

	/**
	 * @param characteristics
	 * @see Characterizable#setCharacteristics(Set)
	 */
	public void setCharacteristics(final Set characteristics) {
		setCharacteristics0(characteristics);
		this.changed = true;
	}

	/**
	 * @param characteristics
	 * @see Characterizable#setCharacteristics0(Set)
	 */
	public void setCharacteristics0(final Set characteristics) {
		assert characteristics != null: ErrorMessages.NON_NULL_EXPECTED;
		if (this.characteristics == null)
			this.characteristics = new HashSet(characteristics.size());
		else
			this.characteristics.clear();
		this.characteristics.addAll(characteristics);
	}

	/**
	 * @see Describable#setDescription(String)
	 */
	public void setDescription(final String description) {
		assert this.description != null: ErrorMessages.OBJECT_NOT_INITIALIZED;
		assert description != null: ErrorMessages.NON_NULL_EXPECTED;
		if (this.description.equals(description))
			return;
		this.description = description;
		this.changed = true;
	}

	/**
	 * @param equipmentType can be <code>null</code>.
	 */
	public void setEquipmentType(final EquipmentType equipmentType) {
		final Identifier newEquipmentTypeId = Identifier.possiblyVoid(equipmentType);
		if (this.equipmentTypeId.equals(newEquipmentTypeId))
			return;
		this.equipmentTypeId = newEquipmentTypeId;
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
	 * @see com.syrus.AMFICOM.general.Namable#setName(String)
	 */
	public void setName(final String name) {
		assert this.name != null && this.name.length() != 0: ErrorMessages.OBJECT_NOT_INITIALIZED;
		assert name != null && name.length() != 0: ErrorMessages.NON_EMPTY_EXPECTED;
		if (this.name.equals(name))
			return;
		this.name = name;
		this.changed = true;
	}

	/**
	 * @param library
	 * @see Item#setParent(Item)
	 */
	public void setParent(final Item library) {
		setParent((Library) library);
	}

	/**
	 * @param library
	 * @throws UnsupportedOperationException if this
	 *         <code>schemeProtoElement</code> has no parent
	 *         <code>schemeProtoGroup</code> (i. e. is either orphan
	 *         (which is invalid) or enclosed by another
	 *         <code>schemeProtoElement</code> (which has no relation with
	 *         library hierarchy)).
	 * @see LibraryEntry#setParent(Library)
	 */
	public void setParent(final Library library) {
		if (getParentSchemeProtoGroup() == null) {
			if (getParentSchemeProtoElement() == null)
				throw new UnsupportedOperationException(ErrorMessages.EXACTLY_ONE_PARENT_REQUIRED);
			throw new UnsupportedOperationException(ErrorMessages.OUT_OF_LIBRARY_HIERARCHY);
		}
		setParentSchemeProtoGroup((SchemeProtoGroup) library);
	}

	/**
	 * If this <code>SchemeProtoElement</code> is initially inside another
	 * <code>SchemeProtoElement</code>, and
	 * <code>parentSchemeProtoElement</code> is <code>null</code>, then
	 * this <code>SchemeProtoElement</code> will delete itself from the
	 * pool. Alternatively, if this <code>SchemeProtoElement</code> is
	 * initially inside a <code>SchemeProtoGroup</code>, and
	 * <code>parentSchemeProtoElement</code> is <code>null</code>, then
	 * no action is taken.
	 *
	 * @param parentSchemeProtoElement
	 */
	public void setParentSchemeProtoElement(final SchemeProtoElement parentSchemeProtoElement) {
		assert this.assertParentSetNonStrict(): ErrorMessages.OBJECT_BADLY_INITIALIZED;
		assert parentSchemeProtoElement != this: ErrorMessages.CIRCULAR_DEPS_PROHIBITED;

		Identifier newParentSchemeProtoElementId;
		if (this.parentSchemeProtoGroupId.isVoid()) {
			/*
			 * Moving from an element to another element.
			 */
			if (parentSchemeProtoElement == null) {
				Log.debugMessage(ErrorMessages.OBJECT_WILL_DELETE_ITSELF_FROM_POOL, Log.WARNING);
				StorableObjectPool.delete(super.id);
				return;
			}
			newParentSchemeProtoElementId = parentSchemeProtoElement.id;
			if (this.parentSchemeProtoElementId.equals(newParentSchemeProtoElementId))
				return;
		} else {
			/*
			 * Moving from a group to an element.
			 */
			if (parentSchemeProtoElement == null) {
				Log.debugMessage(ErrorMessages.ACTION_WILL_RESULT_IN_NOTHING, Log.INFO);
				return;
			}
			newParentSchemeProtoElementId = parentSchemeProtoElement.id;
			this.parentSchemeProtoGroupId = Identifier.VOID_IDENTIFIER;
		}
		this.parentSchemeProtoElementId = newParentSchemeProtoElementId;
		this.changed = true;
	}

	/**
	 * If this <code>SchemeProtoElement</code> is initially inside another
	 * <code>SchemeProtoElement</code>, and
	 * <code>parentSchemeProtoGroup</code> is <code>null</code>, then
	 * no action is taken. Alternatively, if this
	 * <code>SchemeProtoElement</code> is initially inside a
	 * <code>SchemeProtoGroup</code>, and
	 * <code>parentSchemeProtoGroup</code> is <code>null</code>, then
	 * this <code>SchemeProtoElement</code> will delete itself from the
	 * pool.
	 *
	 * @param parentSchemeProtoGroup
	 */
	public void setParentSchemeProtoGroup(final SchemeProtoGroup parentSchemeProtoGroup) {
		assert this.assertParentSetNonStrict(): ErrorMessages.OBJECT_BADLY_INITIALIZED;

		Identifier newParentSchemeProtoGroupId;
		if (this.parentSchemeProtoElementId.isVoid()) {
			/*
			 * Moving from a group to another group.
			 */
			if (parentSchemeProtoGroup == null) {
				Log.debugMessage(ErrorMessages.OBJECT_WILL_DELETE_ITSELF_FROM_POOL, Log.WARNING);
				StorableObjectPool.delete(super.id);
				return;
			}
			newParentSchemeProtoGroupId = parentSchemeProtoGroup.getId();
			if (this.parentSchemeProtoGroupId.equals(newParentSchemeProtoGroupId))
				return;
		} else {
			/*
			 * Moving from an element to a group.
			 */
			if (parentSchemeProtoGroup == null) {
				Log.debugMessage(ErrorMessages.ACTION_WILL_RESULT_IN_NOTHING, Log.INFO);
				return;
			}
			newParentSchemeProtoGroupId = parentSchemeProtoGroup.getId();
			this.parentSchemeProtoElementId = Identifier.VOID_IDENTIFIER;
		}
		this.parentSchemeProtoGroupId = newParentSchemeProtoGroupId;
		this.changed = true;
	}

	/**
	 * @see SchemeCellContainer#setSchemeCell(SchemeImageResource)
	 */
	public void setSchemeCell(final SchemeImageResource schemeCell) {
		final Identifier newSchemeCellId = Identifier.possiblyVoid(schemeCell);
		if (this.schemeCellId.equals(newSchemeCellId))
			return;
		this.schemeCellId = newSchemeCellId;
		this.changed = true;
	}

	/**
	 * @param schemeDevices
	 */
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

	/**
	 * @param schemeLinks
	 */
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
	 * @param schemeProtoElements
	 */
	public void setSchemeProtoElements(final Set schemeProtoElements) {
		assert schemeProtoElements != null: ErrorMessages.NON_NULL_EXPECTED;
		for (final Iterator oldSchemeProtoElementIterator = getSchemeProtoElements().iterator(); oldSchemeProtoElementIterator.hasNext();) {
			final SchemeProtoElement oldSchemeProtoElement = (SchemeProtoElement) oldSchemeProtoElementIterator.next();
			/*
			 * Check is made to prevent SchemeProtoElements from
			 * permanently losing their parents.
			 */
			assert !schemeProtoElements.contains(oldSchemeProtoElement);
			removeSchemeProtoElement(oldSchemeProtoElement);
		}
		for (final Iterator schemeProtoElementIterator = schemeProtoElements.iterator(); schemeProtoElementIterator.hasNext();)
			addSchemeProtoElement((SchemeProtoElement) schemeProtoElementIterator.next());
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
		final SchemeProtoElement_Transferable schemeProtoElement = (SchemeProtoElement_Transferable) transferable;
		try {
			super.fromTransferable(schemeProtoElement.header);
			this.setCharacteristics0(StorableObjectPool.getStorableObjects(Identifier.fromTransferables(schemeProtoElement.characteristicIds), true));
		} catch (final ApplicationException ae) {
			throw new CreateObjectException(ae);
		}
		this.name = schemeProtoElement.name;
		this.description = schemeProtoElement.description;
		this.label = schemeProtoElement.label;
		this.equipmentTypeId = new Identifier(schemeProtoElement.equipmentTypeId);
		this.symbolId = new Identifier(schemeProtoElement.symbolId);
		this.ugoCellId = new Identifier(schemeProtoElement.ugoCellId);
		this.schemeCellId = new Identifier(schemeProtoElement.schemeCellId);
		this.parentSchemeProtoGroupId = new Identifier(schemeProtoElement.parentSchemeProtoGroupId);
		this.parentSchemeProtoElementId = new Identifier(schemeProtoElement.parentSchemeProtoElementId);
	}

	/*-********************************************************************
	 * Non-model members.                                                 *
	 **********************************************************************/

	/**
	 * Invoked by modifier methods.
	 */
	private boolean assertParentSetNonStrict() {
		if (this.parentSet)
			return this.assertParentSetStrict();
		this.parentSet = true;
		return this.parentSchemeProtoGroupId != null
				&& this.parentSchemeProtoElementId != null
				&& this.parentSchemeProtoGroupId.isVoid()
				&& this.parentSchemeProtoElementId.isVoid();
	}

	/**
	 * Invoked by accessor methods (it is assumed that object is already
	 * initialized).
	 */
	private boolean assertParentSetStrict() {
		return this.parentSchemeProtoGroupId != null
				&& this.parentSchemeProtoElementId != null
				&& (this.parentSchemeProtoGroupId.isVoid() ^ this.parentSchemeProtoElementId.isVoid());
	}

	/**
	 * Returns <code>SchemeCablePort</code>s (as an unmodifiable set) for
	 * this <code>schemeProtoElement</code>, recursively.
	 */
	public Set getSchemeCablePortsRecursively() {
		final Set schemeDevices = getSchemeDevices();
		final Iterator schemeDeviceIterator = schemeDevices.iterator();
		if (schemeDevices.size() == 1)
			return ((SchemeDevice) schemeDeviceIterator.next()).getSchemeCablePorts();
		final Set schemeCablePorts = new HashSet();
		for (; schemeDeviceIterator.hasNext();)
			schemeCablePorts.addAll(((SchemeDevice) schemeDeviceIterator.next()).getSchemeCablePorts());
		return Collections.unmodifiableSet(schemeCablePorts);
	}

	/**
	 * Returns <code>SchemePort</code>s (as an unmodifiable set) for this
	 * <code>SchemeProtoElement</code>, recursively.
	 */
	public Set getSchemePortsRecursively() {
		final Set schemeDevices = getSchemeDevices();
		final Iterator schemeDeviceIterator = schemeDevices.iterator();
		if (schemeDevices.size() == 1)
			return ((SchemeDevice) schemeDeviceIterator.next()).getSchemePorts();
		final Set schemePorts = new HashSet();
		for (; schemeDeviceIterator.hasNext();)
			schemePorts.addAll(((SchemeDevice) schemeDeviceIterator.next()).getSchemePorts());
		return Collections.unmodifiableSet(schemePorts);
	}
}
