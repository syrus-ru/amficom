/*-
 * $Id: SchemeElement.java,v 1.12 2005/04/12 18:12:19 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme;

import com.syrus.AMFICOM.configuration.Equipment;
import com.syrus.AMFICOM.configuration.EquipmentType;
import com.syrus.AMFICOM.configuration.KIS;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Characterizable;
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
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.TransferableObject;
import com.syrus.AMFICOM.general.corba.CharacteristicSort;
import com.syrus.AMFICOM.map.SiteNode;
import com.syrus.AMFICOM.resource.BitmapImageResource;
import com.syrus.AMFICOM.resource.SchemeImageResource;
import com.syrus.AMFICOM.scheme.corba.SchemeElement_Transferable;
import com.syrus.util.Log;

import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.omg.CORBA.portable.IDLEntity;

/**
 * #04 in hierarchy.
 *
 * @author $Author: bass $
 * @version $Revision: 1.12 $, $Date: 2005/04/12 18:12:19 $
 * @module scheme_v1
 */
public final class SchemeElement extends AbstractSchemeElement implements
		SchemeCellContainer {
	private static final long serialVersionUID = 3618977875802797368L;

	private Identifier equipmentId;

	private Identifier equipmentTypeId;

	private Identifier kisId;

	private String label;

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
	 * @see Characterizable#getCharacteristicSort()
	 */
	public CharacteristicSort getCharacteristicSort() {
		return CharacteristicSort.CHARACTERISTIC_SORT_SCHEMEELEMENT;
	}

	/**
	 * @see StorableObject#getDependencies()
	 */
	public Set getDependencies() {
		throw new UnsupportedOperationException();
	}

	public Equipment getEquipment() {
		throw new UnsupportedOperationException();
	}

	public EquipmentType getEquipmentType() {
		throw new UnsupportedOperationException();
	}

	public KIS getKis() {
		throw new UnsupportedOperationException();
	}

	public String getLabel() {
		throw new UnsupportedOperationException();
	}

	public SchemeElement getParentSchemeElement() {
		throw new UnsupportedOperationException();
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
	 * @see com.syrus.AMFICOM.scheme.SchemeCellContainer#getSchemeCell()
	 */
	public SchemeImageResource getSchemeCell() {
		throw new UnsupportedOperationException();
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
		throw new UnsupportedOperationException();
	}

	/**
	 * @see TransferableObject#getTransferable()
	 */
	public IDLEntity getTransferable() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see com.syrus.AMFICOM.scheme.SchemeCellContainer#getUgoCell()
	 */
	public SchemeImageResource getUgoCell() {
		throw new UnsupportedOperationException();
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

	public void setLabel(final String label) {
		throw new UnsupportedOperationException();
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
	 * @param schemeCellImpl
	 * @see com.syrus.AMFICOM.scheme.SchemeCellContainer#setSchemeCell(SchemeImageResource)
	 */
	public void setSchemeCell(SchemeImageResource schemeCellImpl) {
		throw new UnsupportedOperationException();
	}

	public void setSchemeDevices(final Set schemeDevices) {
		throw new UnsupportedOperationException();
	}

	public void setSchemeElements(final Set schemeElements) {
		throw new UnsupportedOperationException();
	}

	public void setSchemeLinks(final Set schemeLinks) {
		throw new UnsupportedOperationException();
	}

	public void setSchemes(final Set schemes) {
		/**
		 * @todo Check for circualr deps.
		 */
		throw new UnsupportedOperationException();
	}

	public void setSiteNode(final SiteNode siteNode) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param symbolImpl
	 * @see com.syrus.AMFICOM.scheme.SchemeSymbolContainer#setSymbol(BitmapImageResource)
	 */
	public void setSymbol(BitmapImageResource symbolImpl) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param ugoCellImpl
	 * @see com.syrus.AMFICOM.scheme.SchemeCellContainer#setUgoCell(SchemeImageResource)
	 */
	public void setUgoCell(final SchemeImageResource ugoCellImpl) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param transferable
	 * @throws ApplicationException
	 * @see StorableObject#fromTransferable(IDLEntity)
	 */
	protected void fromTransferable(final IDLEntity transferable) throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	/*-********************************************************************
	 * Non-model methods.                                                 *
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
		throw new UnsupportedOperationException();
	}

	public PathElement getAlarmedPathElement() {
		throw new UnsupportedOperationException();
	}

	public void setAlarmedPath(final SchemePath alarmedPath) {
		throw new UnsupportedOperationException();
	}

	public void setAlarmedPathElement(final PathElement alarmedPathElement) {
		throw new UnsupportedOperationException();
	}
}
