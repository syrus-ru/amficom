/*-
 * $Id: SchemeProtoElement.java,v 1.9 2005/03/24 09:40:15 bass Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme;

import com.syrus.AMFICOM.configuration.*;
import com.syrus.AMFICOM.general.*;
import com.syrus.AMFICOM.general.corba.CharacteristicSort;
import com.syrus.AMFICOM.logic.*;
import com.syrus.AMFICOM.resource.*;
import com.syrus.AMFICOM.scheme.logic.*;
import com.syrus.util.Log;
import java.util.*;

/**
 * #02 in hierarchy.
 *
 * @author $Author: bass $
 * @version $Revision: 1.9 $, $Date: 2005/03/24 09:40:15 $
 * @module scheme_v1
 */
public final class SchemeProtoElement extends AbstractCloneableStorableObject
		implements Describable, SchemeCellContainer, Characterizable,
		LibraryEntry {
	private static final long serialVersionUID = 3689348806202569782L;

	private Collection characteristics;

	private String description;

	private Identifier equipmentTypeId;

	private String label;

	private String name;

	private Identifier parentSchemeProtoElementId;

	private Identifier parentSchemeProtoGroupId;

	private Identifier schemeCellId;

	private StorableObjectDatabase schemeProtoElementDatabase; 

	private Identifier symbolId;

	private Identifier ugoCellId;

	/**
	 * @param id
	 * @throws RetrieveObjectException
	 * @throws ObjectNotFoundException
	 */
	SchemeProtoElement(final Identifier id) throws RetrieveObjectException, ObjectNotFoundException {
		super(id);

		this.characteristics = new ArrayList();
		this.schemeProtoElementDatabase = SchemeDatabaseContext.schemeProtoElementDatabase;
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
				: parentSchemeProtoElement.getId();

		this.characteristics = new ArrayList();

		this.schemeProtoElementDatabase = SchemeDatabaseContext.schemeProtoElementDatabase;
	}

	/**
	 * @deprecated Use one of
	 *             {@link #createInstance(Identifier, String, String, String, EquipmentType, BitmapImageResource, SchemeImageResource, SchemeImageResource)},
	 *             {@link #createInstance(Identifier, String, String, String, EquipmentType, BitmapImageResource, SchemeImageResource, SchemeImageResource, SchemeProtoGroup)},
	 *             {@link #createInstance(Identifier, String, String, String, EquipmentType, BitmapImageResource, SchemeImageResource, SchemeImageResource, SchemeProtoElement)}
	 *             instead.
	 */
	public static SchemeProtoElement createInstance() {
		throw new UnsupportedOperationException();
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
		throw new UnsupportedOperationException();
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

	public void addSchemeDevice(final SchemeDevice schemeDevice) {
		throw new UnsupportedOperationException();
	}

	public void addSchemeLink(final SchemeLink schemeLink) {
		throw new UnsupportedOperationException();
	}

	public void addSchemeProtoElement(final SchemeProtoElement schemeProtoElement) {
		throw new UnsupportedOperationException();
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
	public Collection getCharacteristics() {
		return Collections.unmodifiableCollection(this.characteristics);
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
	public List getDependencies() {
		assert this.equipmentTypeId != null
				&& this.symbolId != null
				&& this.ugoCellId != null
				&& this.schemeCellId != null
				&& this.parentSchemeProtoGroupId != null
				&& this.parentSchemeProtoElementId != null: ErrorMessages.OBJECT_NOT_INITIALIZED;
		final List dependencies = new ArrayList(5);
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
			assert !this.parentSchemeProtoElementId.equals(Identifier.VOID_IDENTIFIER): ErrorMessages.HEADLESS_CHILD_PROHIBITED;
			dependencies.add(this.parentSchemeProtoElementId);
		}
		return Collections.unmodifiableList(dependencies);
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
				throw new UnsupportedOperationException(ErrorMessages.HEADLESS_CHILD_PROHIBITED);
			throw new UnsupportedOperationException(ErrorMessages.OUT_OF_LIBRARY_HIERARCHY);
		}
		return parentSchemeProtoGroup;
	}

	public SchemeProtoElement getParentSchemeProtoElement() {
		throw new UnsupportedOperationException();
	}

	public SchemeProtoGroup getParentSchemeProtoGroup() {
		throw new UnsupportedOperationException();
	}

	/**
	 * Returns <code>schemeCablePort</code> s (as an unmodifiable
	 * collection) for this <code>schemeProtoElement</code>, recursively.
	 */
	public Collection getSchemeCablePortsRecursively() {
		final Collection schemeDevices = getSchemeDevices();
		final Iterator schemeDeviceIterator = schemeDevices.iterator();
		if (schemeDevices.size() == 1)
			return ((SchemeDevice) schemeDeviceIterator.next()).getSchemeCablePorts();
		final Collection schemeCablePorts = new LinkedList();
		for (; schemeDeviceIterator.hasNext();)
			schemeCablePorts.addAll(((SchemeDevice) schemeDeviceIterator.next()).getSchemeCablePorts());
		return Collections.unmodifiableCollection(schemeCablePorts);
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

	public Collection getSchemeDevices() {
		throw new UnsupportedOperationException();
	}

	public Collection getSchemeLinks() {
		throw new UnsupportedOperationException();
	}

	/**
	 * Returns <code>schemePort</code> s (as an unmodifiable collection)
	 * for this <code>schemeProtoElement</code>, recursively.
	 */
	public Collection getSchemePortsRecursively() {
		final Collection schemeDevices = getSchemeDevices();
		final Iterator schemeDeviceIterator = schemeDevices.iterator();
		if (schemeDevices.size() == 1)
			return ((SchemeDevice) schemeDeviceIterator.next()).getSchemePorts();
		final Collection schemePorts = new LinkedList();
		for (; schemeDeviceIterator.hasNext();)
			schemePorts.addAll(((SchemeDevice) schemeDeviceIterator.next()).getSchemePorts());
		return Collections.unmodifiableCollection(schemePorts);
	}

	public Collection getSchemeProtoElements() {
		throw new UnsupportedOperationException();
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
	 * @todo Implement.
	 */
	public Object getTransferable() {
		throw new UnsupportedOperationException();
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
		throw new UnsupportedOperationException();
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

	public void removeSchemeDevice(final SchemeDevice schemeDevice) {
		throw new UnsupportedOperationException();
	}

	public void removeSchemeLink(final SchemeLink schemeLink) {
		throw new UnsupportedOperationException();
	}

	public void removeSchemeProtoElement(final SchemeProtoElement schemeProtoElement) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param characteristics
	 * @see Characterizable#setCharacteristics(Collection)
	 */
	public void setCharacteristics(final Collection characteristics) {
		setCharacteristics0(characteristics);
		this.changed = true;
	}

	/**
	 * @param characteristics
	 * @see Characterizable#setCharacteristics0(Collection)
	 */
	public void setCharacteristics0(final Collection characteristics) {
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
				throw new UnsupportedOperationException(ErrorMessages.HEADLESS_CHILD_PROHIBITED);
			throw new UnsupportedOperationException(ErrorMessages.OUT_OF_LIBRARY_HIERARCHY);
		}
		setParentSchemeProtoGroup((SchemeProtoGroup) library);
	}

	public void setParentSchemeProtoElement(final SchemeProtoElement parentSchemeProtoElement) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param parentSchemeProtoGroup
	 */
	public void setParentSchemeProtoGroup(final SchemeProtoGroup parentSchemeProtoGroup) {
		throw new UnsupportedOperationException();
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

	public void setSchemeDevices(final Collection schemeDevices) {
		throw new UnsupportedOperationException();
	}

	public void setSchemeLinks(final Collection schemeLinks) {
		throw new UnsupportedOperationException();
	}

	public void setSchemeProtoElements(final Collection schemeProtoElements) {
		throw new UnsupportedOperationException();
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
}
