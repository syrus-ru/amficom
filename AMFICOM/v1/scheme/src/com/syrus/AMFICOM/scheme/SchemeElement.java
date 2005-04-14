/*-
 * $Id: SchemeElement.java,v 1.15 2005/04/14 18:20:27 bass Exp $
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

import com.syrus.AMFICOM.configuration.ConfigurationStorableObjectPool;
import com.syrus.AMFICOM.configuration.Equipment;
import com.syrus.AMFICOM.configuration.EquipmentType;
import com.syrus.AMFICOM.configuration.KIS;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.ErrorMessages;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.corba.CharacteristicSort;
import com.syrus.AMFICOM.map.SiteNode;
import com.syrus.AMFICOM.resource.BitmapImageResource;
import com.syrus.AMFICOM.resource.ResourceStorableObjectPool;
import com.syrus.AMFICOM.resource.SchemeImageResource;
import com.syrus.AMFICOM.scheme.corba.SchemeElement_Transferable;
import com.syrus.util.Log;

/**
 * #04 in hierarchy.
 *
 * @author $Author: bass $
 * @version $Revision: 1.15 $, $Date: 2005/04/14 18:20:27 $
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

	private Identifier siteId;

	private Identifier symbolId;

	/**
	 * Takes non-null value at pack time.
	 */
	private Identifier ugoCellId;

	private SchemeElementDatabase schemeElementDatabase;

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
	 */
	SchemeElement(Identifier id, Date created, Date modified,
			Identifier creatorId, Identifier modifierId,
			long version) {
		super(id, created, modified, creatorId, modifierId, version);
	}

	/**
	 * @param transferable
	 * @throws CreateObjectException
	 */
	SchemeElement(final SchemeElement_Transferable transferable) throws CreateObjectException {
		try {
			this.schemeElementDatabase = SchemeDatabaseContext.getSchemeElementDatabase();
			fromTransferable(transferable);
		} catch (final ApplicationException ae) {
			throw new CreateObjectException(ae);
		}
	}

	public static SchemeElement createInstance(final Identifier creatorId)
			throws CreateObjectException {
		assert creatorId != null;
		try {
			final Date created = new Date();
			final SchemeElement schemeElement = new SchemeElement(
					IdentifierPool
							.getGeneratedIdentifier(ObjectEntities.SCHEME_ELEMENT_ENTITY_CODE),
					created, created, creatorId, creatorId,
					0L);
			schemeElement.changed = true;
			return schemeElement;
		} catch (final IllegalObjectEntityException ioee) {
			throw new CreateObjectException(
					"SchemeElement.createInstance | cannot generate identifier ", ioee); //$NON-NLS-1$
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
				&& this.siteId != null && this.symbolId != null
				&& this.ugoCellId != null: ErrorMessages.OBJECT_NOT_INITIALIZED;
		final Set dependencies = new HashSet();
		dependencies.addAll(super.getDependencies());
		dependencies.add(this.equipmentId);
		dependencies.add(this.equipmentTypeId);
		dependencies.add(this.kisId);
		dependencies.add(this.parentSchemeElementId);
		dependencies.add(this.schemeCellId);
		dependencies.add(this.siteId);
		dependencies.add(this.symbolId);
		dependencies.add(this.ugoCellId);
		dependencies.remove(null);
		dependencies.remove(Identifier.VOID_IDENTIFIER);
		return Collections.unmodifiableSet(dependencies);
	}

	public Equipment getEquipment() {
		assert this.equipmentId != null && this.equipmentTypeId != null: ErrorMessages.OBJECT_NOT_INITIALIZED;
		if (this.equipmentId.isVoid()) {
			assert !this.equipmentTypeId.isVoid(): ErrorMessages.OBJECT_BADLY_INITIALIZED;
			return null;
		}

		try {
			assert this.equipmentTypeId.isVoid(): ErrorMessages.OBJECT_BADLY_INITIALIZED;
			return (Equipment) ConfigurationStorableObjectPool.getStorableObject(this.equipmentId, true);
		} catch (final ApplicationException ae) {
			Log.debugException(ae, Log.SEVERE);
			return null;
		}
	}

	public EquipmentType getEquipmentType() {
		assert this.equipmentId != null && this.equipmentTypeId != null: ErrorMessages.OBJECT_NOT_INITIALIZED;
		if (this.equipmentTypeId.isVoid()) {
			assert !this.equipmentId.isVoid(): ErrorMessages.OBJECT_BADLY_INITIALIZED;
			return (EquipmentType) getEquipment().getType();
		}

		try {
			assert this.equipmentId.isVoid(): ErrorMessages.OBJECT_BADLY_INITIALIZED;
			return (EquipmentType) ConfigurationStorableObjectPool.getStorableObject(this.equipmentTypeId, true);
		} catch (final ApplicationException ae) {
			Log.debugException(ae, Log.SEVERE);
			return null;
		}
	}

	public KIS getKis() {
		assert this.kisId != null: ErrorMessages.OBJECT_NOT_INITIALIZED;
		if (this.kisId.isVoid())
			return null;
		try {
			return (KIS) ConfigurationStorableObjectPool.getStorableObject(this.kisId, true);
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
		assert this.parentSchemeId != null && this.parentSchemeElementId != null: ErrorMessages.OBJECT_NOT_INITIALIZED;

		if (this.parentSchemeId.isVoid()) {
			assert !this.parentSchemeElementId.isVoid(): ErrorMessages.PARENTLESS_CHILD_PROHIBITED;
			Log.debugMessage("SchemeElement.getParentScheme() | Parent Scheme was requested, while parent is a SchemeElement; returning null", //$NON-NLS-1$
					Log.FINE);
			return null;
		}

		assert this.parentSchemeElementId.isVoid(): ErrorMessages.MULTIPLE_PARENTS_PROHIBITED;
		return super.getParentScheme();
	}

	public SchemeElement getParentSchemeElement() {
		assert this.parentSchemeId != null && this.parentSchemeElementId != null: ErrorMessages.OBJECT_NOT_INITIALIZED;

		if (this.parentSchemeElementId.isVoid()) {
			assert !this.parentSchemeId.isVoid(): ErrorMessages.PARENTLESS_CHILD_PROHIBITED;
			Log.debugMessage("SchemeElement.getParentSchemeElement() | Parent SchemeElement was requested, while parent is a Scheme; returnung null", //$NON-NLS-1$
					Log.FINE);
			return null;
		}
		
		try {
			assert this.parentSchemeId.isVoid(): ErrorMessages.MULTIPLE_PARENTS_PROHIBITED;
			return (SchemeElement) SchemeStorableObjectPool.getStorableObject(this.parentSchemeElementId, true);
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
	public Set getSchemeElements() {
		try {
			return Collections.unmodifiableSet(SchemeStorableObjectPool.getStorableObjectsByCondition(new LinkedIdsCondition(this.id, ObjectEntities.SCHEME_ELEMENT_ENTITY_CODE), true));
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
	public Set getSchemes() {
		try {
			return Collections.unmodifiableSet(SchemeStorableObjectPool.getStorableObjectsByCondition(new LinkedIdsCondition(this.id, ObjectEntities.SCHEME_ENTITY_CODE), true));
		} catch (final ApplicationException ae) {
			Log.debugException(ae, Log.SEVERE);
			return Collections.EMPTY_SET;
		}
	}

	public SiteNode getSiteNode() {
		throw new UnsupportedOperationException();
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
	 * @param newEquipmentImpl
	 */
	public void setEquipment(Equipment newEquipmentImpl) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param newEquipmentTypeImpl
	 */
	public void setEquipmentType(EquipmentType newEquipmentTypeImpl) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param newRtuImpl
	 */
	public void setKis(KIS newRtuImpl) {
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

	public void setParentSchemeElement(final SchemeElement parentSchemeElement) {
		throw new UnsupportedOperationException();
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
		throw new UnsupportedOperationException();
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
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.general.StorableObject#fromTransferable(IDLEntity)
	 */
	protected void fromTransferable(final IDLEntity transferable) throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	/*-********************************************************************
	 * Non-model members.                                                 *
	 **********************************************************************/

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
		throw new UnsupportedOperationException("Method not implemented"); //$NON-NLS-1$
	}

	public PathElement getAlarmedPathElement() {
		throw new UnsupportedOperationException("Method not implemented"); //$NON-NLS-1$
	}

	public void setAlarmedPath(final SchemePath alarmedPath) {
		throw new UnsupportedOperationException("Method not implemented"); //$NON-NLS-1$
	}

	public void setAlarmedPathElement(final PathElement alarmedPathElement) {
		throw new UnsupportedOperationException("Method not implemented"); //$NON-NLS-1$
	}
}
