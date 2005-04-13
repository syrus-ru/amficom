/*-
 * $Id: SchemeProtoElement.java,v 1.17 2005/04/13 12:27:25 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme;

import com.syrus.AMFICOM.configuration.ConfigurationStorableObjectPool;
import com.syrus.AMFICOM.configuration.EquipmentType;
import com.syrus.AMFICOM.general.AbstractCloneableStorableObject;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Characteristic;
import com.syrus.AMFICOM.general.Characterizable;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Describable;
import com.syrus.AMFICOM.general.ErrorMessages;
import com.syrus.AMFICOM.general.GeneralStorableObjectPool;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.Namable;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.TransferableObject;
import com.syrus.AMFICOM.general.corba.CharacteristicSort;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.logic.Item;
import com.syrus.AMFICOM.logic.ItemListener;
import com.syrus.AMFICOM.resource.BitmapImageResource;
import com.syrus.AMFICOM.resource.ResourceStorableObjectPool;
import com.syrus.AMFICOM.resource.SchemeImageResource;
import com.syrus.AMFICOM.scheme.corba.SchemeProtoElement_Transferable;
import com.syrus.AMFICOM.scheme.logic.Library;
import com.syrus.AMFICOM.scheme.logic.LibraryEntry;
import com.syrus.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.omg.CORBA.portable.IDLEntity;

/**
 * #02 in hierarchy.
 *
 * @author $Author: bass $
 * @version $Revision: 1.17 $, $Date: 2005/04/13 12:27:25 $
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
		this.equipmentTypeId
				= equipmentType == null
				? Identifier.VOID_IDENTIFIER
				: equipmentType.getId();
		this.symbolId
				= symbol == null
				? Identifier.VOID_IDENTIFIER
				: symbol.getId();
		this.ugoCellId
				= ugoCell == null
				? Identifier.VOID_IDENTIFIER
				: ugoCell.getId();
		this.schemeCellId
				= schemeCell == null
				? Identifier.VOID_IDENTIFIER
				: schemeCell.getId();
		assert parentSchemeProtoGroup == null || parentSchemeProtoElement == null: ErrorMessages.MULTIPLE_PARENTS_PROHIBITED;
		this.parentSchemeProtoGroupId
				= parentSchemeProtoGroup == null
				? Identifier.VOID_IDENTIFIER
				: parentSchemeProtoGroup.getId();
		this.parentSchemeProtoElementId
				= parentSchemeProtoElement == null
				? Identifier.VOID_IDENTIFIER
				: parentSchemeProtoElement.id;

		this.characteristics = new HashSet();
		this.schemeProtoElementDatabase = SchemeDatabaseContext.getSchemeProtoElementDatabase();
	}

	/**
	 * @param transferable
	 * @throws CreateObjectException
	 */
	SchemeProtoElement(final SchemeProtoElement_Transferable transferable) throws CreateObjectException {
		try {
			this.schemeProtoElementDatabase = SchemeDatabaseContext.getSchemeProtoElementDatabase();
			fromTransferable(transferable);
		} catch (final ApplicationException ae) {
			throw new CreateObjectException(ae);
		}
	}

	/**
	 * @param creatorId cannot be <code>null</code>.
	 * @param name cannot be <code>null</code>.
	 * @throws CreateObjectException
	 */
	public static SchemeProtoElement createInstance(final Identifier creatorId, final String name) throws CreateObjectException {
		return createInstance(creatorId, name, "", "", null, null, null, null);  //$NON-NLS-1$//$NON-NLS-2$
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
		assert creatorId != null && !creatorId.equals(Identifier.VOID_IDENTIFIER): ErrorMessages.NON_VOID_EXPECTED;
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
		} catch (final IllegalObjectEntityException ioee) {
			throw new CreateObjectException(
					"SchemeProtoElement.createInstance | cannot generate identifier ", ioee); //$NON-NLS-1$
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
		assert creatorId != null && !creatorId.equals(Identifier.VOID_IDENTIFIER): ErrorMessages.NON_VOID_EXPECTED;
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
			return schemeProtoElement;
		} catch (final IllegalObjectEntityException ioee) {
			throw new CreateObjectException(
					"SchemeProtoElement.createInstance | cannot generate identifier ", ioee); //$NON-NLS-1$
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
		assert creatorId != null && !creatorId.equals(Identifier.VOID_IDENTIFIER): ErrorMessages.NON_VOID_EXPECTED;
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
			return schemeProtoElement;
		} catch (final IllegalObjectEntityException ioee) {
			throw new CreateObjectException(
					"SchemeProtoElement.createInstance | cannot generate identifier ", ioee); //$NON-NLS-1$
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
		assert !getCharacteristics().contains(characteristic): ErrorMessages.COLLECTION_IS_A_SET;
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
	 * @see StorableObject#getDependencies()
	 */
	public Set getDependencies() {
		assert this.equipmentTypeId != null
				&& this.symbolId != null
				&& this.ugoCellId != null
				&& this.schemeCellId != null
				&& this.parentSchemeProtoGroupId != null
				&& this.parentSchemeProtoElementId != null: ErrorMessages.OBJECT_NOT_INITIALIZED;
		final Set dependencies = new HashSet(5);
		if (!this.equipmentTypeId.equals(Identifier.VOID_IDENTIFIER))
			dependencies.add(this.equipmentTypeId);
		if (!this.symbolId.equals(Identifier.VOID_IDENTIFIER))
			dependencies.add(this.symbolId);
		if (!this.ugoCellId.equals(Identifier.VOID_IDENTIFIER))
			dependencies.add(this.ugoCellId);
		if (!this.schemeCellId.equals(Identifier.VOID_IDENTIFIER))
			dependencies.add(this.schemeCellId);
		if (!this.parentSchemeProtoGroupId.equals(Identifier.VOID_IDENTIFIER)) {
			assert this.parentSchemeProtoElementId.equals(Identifier.VOID_IDENTIFIER): ErrorMessages.MULTIPLE_PARENTS_PROHIBITED;
			dependencies.add(this.parentSchemeProtoGroupId);
		} else {
			assert !this.parentSchemeProtoElementId.equals(Identifier.VOID_IDENTIFIER): ErrorMessages.PARENTLESS_CHILD_PROHIBITED;
			dependencies.add(this.parentSchemeProtoElementId);
		}
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
		if (this.equipmentTypeId.equals(Identifier.VOID_IDENTIFIER))
			return null;
		try {
			return (EquipmentType) ConfigurationStorableObjectPool
					.getStorableObject(this.equipmentTypeId, true);
		} catch (final ApplicationException ae) {
			Log.debugException(ae, Log.SEVERE);
			return null;
		}
	}

	/**
	 * @return this <code>schemeProtoElement</code>&apos;s label, or
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
	 * @see Namable#getName()
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
				throw new UnsupportedOperationException(ErrorMessages.PARENTLESS_CHILD_PROHIBITED);
			throw new UnsupportedOperationException(ErrorMessages.OUT_OF_LIBRARY_HIERARCHY);
		}
		return parentSchemeProtoGroup;
	}

	public SchemeProtoElement getParentSchemeProtoElement() {
		assert this.parentSchemeProtoElementId != null && this.parentSchemeProtoGroupId != null: ErrorMessages.OBJECT_NOT_INITIALIZED;

		if (this.parentSchemeProtoElementId.equals(Identifier.VOID_IDENTIFIER)) {
			assert !this.parentSchemeProtoGroupId.equals(Identifier.VOID_IDENTIFIER): ErrorMessages.PARENTLESS_CHILD_PROHIBITED;
			Log.debugMessage("SchemeProtoElement.getParentSchemeProtoElement() | Parent SchemeProtoElement was requested, while parent is a SchemeProtoGroup; returning null.", //$NON-NLS-1$
					Log.FINE);
			return null;
		}

		try {
			assert this.parentSchemeProtoGroupId.equals(Identifier.VOID_IDENTIFIER): ErrorMessages.MULTIPLE_PARENTS_PROHIBITED;
			return (SchemeProtoElement) SchemeStorableObjectPool.getStorableObject(this.parentSchemeProtoElementId, true);
		} catch (final ApplicationException ae) {
			Log.debugException(ae, Log.SEVERE);
			return null;
		}
	}

	public SchemeProtoGroup getParentSchemeProtoGroup() {
		assert this.parentSchemeProtoElementId != null && this.parentSchemeProtoGroupId != null: ErrorMessages.OBJECT_NOT_INITIALIZED;
		
		if (this.parentSchemeProtoGroupId.equals(Identifier.VOID_IDENTIFIER)) {
			assert !this.parentSchemeProtoElementId.equals(Identifier.VOID_IDENTIFIER): ErrorMessages.PARENTLESS_CHILD_PROHIBITED;
			Log.debugMessage("SchemeProtoElement.getParentSchemeProtoGroup() | Parent SchemeProtoGroup was requested, while parent is a SchemeProtoElement; returnning null", //$NON-NLS-1$
					Log.FINE);
			return null;
		}

		try {
			assert this.parentSchemeProtoElementId.equals(Identifier.VOID_IDENTIFIER): ErrorMessages.MULTIPLE_PARENTS_PROHIBITED;
			return (SchemeProtoGroup) SchemeStorableObjectPool.getStorableObject(this.parentSchemeProtoGroupId, true);
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
		if (this.schemeCellId.equals(Identifier.VOID_IDENTIFIER))
			return null;
		try {
			return (SchemeImageResource) ResourceStorableObjectPool
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
			return Collections.unmodifiableSet(SchemeStorableObjectPool.getStorableObjectsByCondition(new LinkedIdsCondition(this.id, ObjectEntities.SCHEME_DEVICE_ENTITY_CODE), true));
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
			return Collections.unmodifiableSet(SchemeStorableObjectPool.getStorableObjectsByCondition(new LinkedIdsCondition(this.id, ObjectEntities.SCHEME_LINK_ENTITY_CODE), true));
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
			return Collections.unmodifiableSet(SchemeStorableObjectPool.getStorableObjectsByCondition(new LinkedIdsCondition(this.id, ObjectEntities.SCHEME_PROTO_ELEMENT_ENTITY_CODE), true));
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
		if (this.symbolId.equals(Identifier.VOID_IDENTIFIER))
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
	 * @see TransferableObject#getTransferable()
	 */
	public IDLEntity getTransferable() {
		return new SchemeProtoElement_Transferable(
				getHeaderTransferable(),
				this.name,
				this.description,
				this.label,
				(Identifier_Transferable) this.equipmentTypeId.getTransferable(),
				(Identifier_Transferable) this.symbolId.getTransferable(),
				(Identifier_Transferable) this.ugoCellId.getTransferable(),
				(Identifier_Transferable) this.schemeCellId.getTransferable(),
				(Identifier_Transferable) this.parentSchemeProtoGroupId.getTransferable(),
				(Identifier_Transferable) this.parentSchemeProtoElementId.getTransferable(),
				Identifier.getTransferables(this.characteristics));
	}

	/**
	 * @see SchemeCellContainer#getUgoCell()
	 */
	public SchemeImageResource getUgoCell() {
		assert this.ugoCellId != null: ErrorMessages.OBJECT_NOT_INITIALIZED;
		if (this.ugoCellId.equals(Identifier.VOID_IDENTIFIER))
			return null;
		try {
			return (SchemeImageResource) ResourceStorableObjectPool
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
		Identifier newEquipmentTypeId;
		if (equipmentType == null)
			newEquipmentTypeId = Identifier.VOID_IDENTIFIER;
		else
			newEquipmentTypeId = equipmentType.getId();
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
	 * @see Namable#setName(String)
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
				throw new UnsupportedOperationException(ErrorMessages.PARENTLESS_CHILD_PROHIBITED);
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
		assert this.parentSchemeProtoGroupId != null
				&& this.parentSchemeProtoElementId != null: ErrorMessages.OBJECT_NOT_INITIALIZED;
		assert parentSchemeProtoElement != this: ErrorMessages.CIRCULAR_DEPS_PROHIBITED;
		Identifier newParentSchemeProtoElementId;
		if (this.parentSchemeProtoGroupId.equals(Identifier.VOID_IDENTIFIER)) {
			/*
			 * Moving from an element to another element.
			 */
			assert !this.parentSchemeProtoElementId.equals(Identifier.VOID_IDENTIFIER): ErrorMessages.PARENTLESS_CHILD_PROHIBITED;
			if (parentSchemeProtoElement == null) {
				Log.debugMessage(ErrorMessages.OBJECT_WILL_DELETE_ITSELF_FROM_POOL, Log.WARNING);
				SchemeStorableObjectPool.delete(this.id);
				return;
			}
			newParentSchemeProtoElementId = parentSchemeProtoElement.id;
			if (this.parentSchemeProtoElementId.equals(newParentSchemeProtoElementId))
				return;
		} else {
			/*
			 * Moving from a group to an element.
			 */
			assert this.parentSchemeProtoElementId.equals(Identifier.VOID_IDENTIFIER): ErrorMessages.MULTIPLE_PARENTS_PROHIBITED;
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
		assert this.parentSchemeProtoGroupId != null
				&& this.parentSchemeProtoElementId != null: ErrorMessages.OBJECT_NOT_INITIALIZED;
		Identifier newParentSchemeProtoGroupId;
		if (this.parentSchemeProtoElementId.equals(Identifier.VOID_IDENTIFIER)) {
			/*
			 * Moving from a group to another group.
			 */
			assert !this.parentSchemeProtoGroupId.equals(Identifier.VOID_IDENTIFIER): ErrorMessages.PARENTLESS_CHILD_PROHIBITED;
			if (parentSchemeProtoGroup == null) {
				Log.debugMessage(ErrorMessages.OBJECT_WILL_DELETE_ITSELF_FROM_POOL, Log.WARNING);
				SchemeStorableObjectPool.delete(this.id);
				return;
			}
			newParentSchemeProtoGroupId = parentSchemeProtoGroup.getId();
			if (this.parentSchemeProtoGroupId.equals(newParentSchemeProtoGroupId))
				return;
		} else {
			/*
			 * Moving from an element to a group.
			 */
			assert this.parentSchemeProtoGroupId.equals(Identifier.VOID_IDENTIFIER): ErrorMessages.MULTIPLE_PARENTS_PROHIBITED;
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
		Identifier newSchemeCellId;
		if (schemeCell == null)
			newSchemeCellId = Identifier.VOID_IDENTIFIER;
		else
			newSchemeCellId = schemeCell.getId();
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
		for (final Iterator oldSchemeDeviceIterator = getSchemeDevices().iterator(); oldSchemeDeviceIterator.hasNext();)
			removeSchemeDevice((SchemeDevice) oldSchemeDeviceIterator.next());
		for (final Iterator schemeDeviceIterator = schemeDevices.iterator(); schemeDeviceIterator.hasNext();)
			addSchemeDevice((SchemeDevice) schemeDeviceIterator.next());
	}

	/**
	 * @param schemeLinks
	 */
	public void setSchemeLinks(final Set schemeLinks) {
		assert schemeLinks != null: ErrorMessages.NON_NULL_EXPECTED;
		for (final Iterator oldSchemeLinkIterator = getSchemeLinks().iterator(); oldSchemeLinkIterator.hasNext();)
			removeSchemeLink((SchemeLink) oldSchemeLinkIterator.next());
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
	 * @see SchemeSymbolContainer#setSymbol(BitmapImageResource)
	 */
	public void setSymbol(final BitmapImageResource symbol) {
		Identifier newSymbolId;
		if (symbol == null)
			newSymbolId = Identifier.VOID_IDENTIFIER;
		else
			newSymbolId = symbol.getId();
		if (this.symbolId.equals(newSymbolId))
			return;
		this.symbolId = newSymbolId;
		this.changed = true;
	}

	/**
	 * @see SchemeCellContainer#setUgoCell(SchemeImageResource)
	 */
	public void setUgoCell(final SchemeImageResource ugoCell) {
		Identifier newUgoCellId;
		if (ugoCell == null)
			newUgoCellId = Identifier.VOID_IDENTIFIER;
		else
			newUgoCellId = ugoCell.getId();
		if (this.ugoCellId.equals(newUgoCellId))
			return;
		this.ugoCellId = newUgoCellId;
		this.changed = true;
	}

	/**
	 * @param transferable
	 * @throws ApplicationException 
	 * @see StorableObject#fromTransferable(IDLEntity)
	 */
	protected void fromTransferable(final IDLEntity transferable) throws ApplicationException {
		final SchemeProtoElement_Transferable schemeProtoElement = (SchemeProtoElement_Transferable) transferable;
		super.fromTransferable(schemeProtoElement.header);
		this.name = schemeProtoElement.name;
		this.description = schemeProtoElement.description;
		this.label = schemeProtoElement.label;
		this.equipmentTypeId = new Identifier(schemeProtoElement.equipmentTypeId);
		this.symbolId = new Identifier(schemeProtoElement.symbolId);
		this.ugoCellId = new Identifier(schemeProtoElement.ugoCellId);
		this.schemeCellId = new Identifier(schemeProtoElement.schemeCellId);
		this.parentSchemeProtoGroupId = new Identifier(schemeProtoElement.parentSchemeProtoGroupId);
		this.parentSchemeProtoElementId = new Identifier(schemeProtoElement.parentSchemeProtoElementId);
		try {
			final Identifier_Transferable characteristicIds[] = schemeProtoElement.characteristicIds;
			final int length = characteristicIds.length;
			if (this.characteristics == null)
				this.characteristics = new HashSet(length);
			else
				this.characteristics.clear();
			for (int i = 0; i < length; i++)
				this.characteristics.add(GeneralStorableObjectPool.getStorableObject(new Identifier(characteristicIds[i]), true));
		} catch (final ApplicationException ae) {
			throw new CreateObjectException(ae);
		}
	}

	/*-********************************************************************
	 * Non-model members.                                                 *
	 **********************************************************************/

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
