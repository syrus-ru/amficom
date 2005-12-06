/*-
* $Id: LayoutItem.java,v 1.18 2005/12/06 09:44:56 bass Exp $
*
* Copyright ¿ 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.resource;

import static com.syrus.AMFICOM.general.ErrorMessages.NON_NULL_EXPECTED;
import static com.syrus.AMFICOM.general.ErrorMessages.REMOVAL_OF_AN_ABSENT_PROHIBITED;
import static com.syrus.AMFICOM.general.ObjectEntities.CHARACTERISTIC_CODE;

import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Characteristic;
import com.syrus.AMFICOM.general.Characterizable;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.ErrorMessages;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.Namable;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.general.corba.IdlStorableObject;
import com.syrus.AMFICOM.resource.corba.IdlLayoutItem;
import com.syrus.AMFICOM.resource.corba.IdlLayoutItemHelper;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.18 $
 * @author $Author: bass $
 * @module resource
 */

public final class LayoutItem extends StorableObject<LayoutItem>
		implements Characterizable, Namable {
	
	public static final String CHARACTERISCTIC_TYPE_X = "x";
	public static final String CHARACTERISCTIC_TYPE_Y = "y";
	public static final String CHARACTERISCTIC_TYPE_NAME = "name";
	
	private static final long serialVersionUID = 4050767108738528569L;

	private Identifier							parentId;
	private String								layoutName;
	private String								name;

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	public LayoutItem(final IdlLayoutItem ili) throws CreateObjectException {
		try {
			this.fromTransferable(ili);
		} catch (ApplicationException ae) {
			throw new CreateObjectException(ae);
		}
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	LayoutItem(final Identifier id,
			final Identifier creatorId,
			final StorableObjectVersion version,
			final Identifier parentId,
			final String layoutName,
			final String name) {
		super(id,
				new Date(System.currentTimeMillis()),
				new Date(System.currentTimeMillis()),
				creatorId,
				creatorId,
				version);
		this.parentId = parentId;
		this.layoutName = layoutName;
		this.name = name;
	}

	/**
	 * create new instance for client
	 * @param creatorId
	 * @param layoutName
	 * @throws CreateObjectException
	 */
	public static LayoutItem createInstance(final Identifier creatorId,
	        final Identifier parentId,
			final String layoutName,
			final String name) throws CreateObjectException {
		try {
			final LayoutItem layout = new LayoutItem(IdentifierPool.getGeneratedIdentifier(ObjectEntities.LAYOUT_ITEM_CODE),
					creatorId,
					StorableObjectVersion.INITIAL_VERSION,
					parentId,
					layoutName,
					name);

			assert layout.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;

			layout.markAsChanged();

			return layout;
		} catch (IdentifierGenerationException ige) {
			throw new CreateObjectException("Cannot generate identifier ", ige);
		}
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	@Override
	protected boolean isValid() {		
		return super.isValid() &&
			// parentId can be VOID_IDENTIFIER - just null pointer checking
			this.parentId != null && 
			this.layoutName != null && 
			this.layoutName.trim().length() > 0;
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	@Override
	protected synchronized void fromTransferable(final IdlStorableObject transferable) throws ApplicationException {
		final IdlLayoutItem ili = (IdlLayoutItem) transferable;
		try {
			super.fromTransferable(ili);
		} catch (ApplicationException ae) {
			// Never
			Log.errorMessage(ae);
		}
		
		this.parentId = new Identifier(ili.parentId);
		this.layoutName = ili.layoutName;
		this.name = ili.name;

		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;
	}
	
	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	@Override
	public IdlLayoutItem getIdlTransferable(final ORB orb) {
		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;

		return IdlLayoutItemHelper.init(orb,
				this.id.getIdlTransferable(),
				this.created.getTime(),
				this.modified.getTime(),
				this.creatorId.getIdlTransferable(),
				this.modifierId.getIdlTransferable(),
				this.version.longValue(),
				this.parentId.getIdlTransferable(),
				this.layoutName,
				this.name);
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	protected synchronized void setAttributes(final Date created,
			final Date modified,
			final Identifier creatorId,
			final Identifier modifierId,
			final StorableObjectVersion version,
			final Identifier parentId,
			final String layoutName,
			final String name) {
		super.setAttributes(created,
			modified,
			creatorId,
			modifierId,
			version);
		this.parentId = parentId;
		
		this.layoutName = layoutName;
		this.name = name;
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	@Override
	public Set<Identifiable> getDependencies() {
		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;
		if (!this.parentId.isVoid()) {
			return Collections.singleton((Identifiable)this.parentId);
		}		
		return Collections.emptySet();
	}

	
	public String getName() {
		return this.name;
	}
	
	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	protected void setName0(final String name) {
		this.name = name;
	}

	/**
	 * client setter for name
	 * @param name The name to set.
	 */
	public void setName(final String name) {
		if (this.name.intern() != name.intern()) {
			this.setName0(name);
			super.markAsChanged();
		}
	}
	
	public final String getLayoutName() {
		return this.layoutName;
	}
	
	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	final void setLayoutName0(final String layoutName) {
		this.layoutName = layoutName;
	}
	
	public final void setLayoutName(final String layoutName) {
		if (this.layoutName.intern() != layoutName.intern()) {
			this.setLayoutName0(layoutName);
			super.markAsChanged();
		}
	}

	
	public final Identifier getParentId() {
		return this.parentId;
	}

	final void setParentId0(final Identifier parentId) {
		this.parentId = parentId;
	}
	
	public final void setParentId(final Identifier parentId) {
		if(!this.parentId.equals(parentId)) {
			this.setParentId0(parentId);
			super.markAsChanged();
		}
	}

	/**
	 * @see com.syrus.AMFICOM.general.StorableObject#getWrapper()
	 */
	@Override
	protected LayoutItemWrapper getWrapper() {
		return LayoutItemWrapper.getInstance();
	}

	/*-********************************************************************
	 * Children manipulation: characteristics                             *
	 **********************************************************************/

	private transient StorableObjectContainerWrappee<Characteristic> characteristicContainerWrappee;

	/**
	 * @see Characterizable#getCharacteristicContainerWrappee()
	 */
	public StorableObjectContainerWrappee<Characteristic> getCharacteristicContainerWrappee() {
		return (this.characteristicContainerWrappee == null)
				? this.characteristicContainerWrappee = new StorableObjectContainerWrappee<Characteristic>(this, CHARACTERISTIC_CODE)
				: this.characteristicContainerWrappee;
	}

	/**
	 * @param characteristic
	 * @param usePool
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.general.Characterizable#addCharacteristic(com.syrus.AMFICOM.general.Characteristic, boolean)
	 */
	public void addCharacteristic(final Characteristic characteristic,
			final boolean usePool)
	throws ApplicationException {
		assert characteristic != null : NON_NULL_EXPECTED;
		characteristic.setParentCharacterizable(this, usePool);
	}

	/**
	 * @param characteristic
	 * @param usePool
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.general.Characterizable#removeCharacteristic(com.syrus.AMFICOM.general.Characteristic, boolean)
	 */
	public void removeCharacteristic(
			final Characteristic characteristic,
			final boolean usePool)
	throws ApplicationException {
		assert characteristic != null : NON_NULL_EXPECTED;
		assert characteristic.getParentCharacterizableId().equals(this) : REMOVAL_OF_AN_ABSENT_PROHIBITED;
		characteristic.setParentCharacterizable(this, usePool);
	}

	/**
	 * @param usePool
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.general.Characterizable#getCharacteristics(boolean)
	 */
	public Set<Characteristic> getCharacteristics(boolean usePool)
	throws ApplicationException {
		return Collections.unmodifiableSet(this.getCharacteristics0(usePool));
	}

	/**
	 * @param usePool
	 * @throws ApplicationException
	 */
	Set<Characteristic> getCharacteristics0(final boolean usePool)
	throws ApplicationException {
		return this.getCharacteristicContainerWrappee().getContainees(usePool);
	}

	/**
	 * @param characteristics
	 * @param usePool
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.general.Characterizable#setCharacteristics(Set, boolean)
	 */
	public void setCharacteristics(final Set<Characteristic> characteristics,
			final boolean usePool)
	throws ApplicationException {
		assert characteristics != null : NON_NULL_EXPECTED;

		final Set<Characteristic> oldCharacteristics = this.getCharacteristics0(usePool);

		final Set<Characteristic> toRemove = new HashSet<Characteristic>(oldCharacteristics);
		toRemove.removeAll(characteristics);
		for (final Characteristic characteristic : toRemove) {
			this.removeCharacteristic(characteristic, usePool);
		}

		final Set<Characteristic> toAdd = new HashSet<Characteristic>(characteristics);
		toAdd.removeAll(oldCharacteristics);
		for (final Characteristic characteristic : toAdd) {
			this.addCharacteristic(characteristic, usePool);
		}
	}
}
