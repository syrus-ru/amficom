/*
 * $Id: SchemePath.java,v 1.3 2005/03/17 12:52:55 bass Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme;

import com.syrus.AMFICOM.configuration.*;
import com.syrus.AMFICOM.general.*;
import com.syrus.AMFICOM.general.corba.CharacteristicSort;
import java.util.*;

/**
 * @author $Author: bass $
 * @version $Revision: 1.3 $, $Date: 2005/03/17 12:52:55 $
 * @module scheme_v1
 */
public final class SchemePath extends AbstractCloneableStorableObject implements Describable, Characterizable {
	private static final long serialVersionUID = 3257567312831132469L;

	private Identifier typeId = null;

	protected Identifier characteristicIds[] = null;

	protected Identifier endDeviceId = null;

	protected Identifier links[] = null;

	protected Identifier pathId = null;

	protected Identifier schemeId = null;

	protected Identifier startDeviceId = null;

	protected String thisDescription = null;

	protected String thisName = null;

	/**
	 * @param id
	 */
	protected SchemePath(final Identifier id) {
		super(id);
	}

	/**
	 * @param id
	 * @param created
	 * @param modified
	 * @param creatorId
	 * @param modifierId
	 * @param version
	 */
	protected SchemePath(final Identifier id, final Date created,
			final Date modified, final Identifier creatorId,
			final Identifier modifierId, final long version) {
		super(id, created, modified, creatorId, modifierId, version);
	}

	/**
	 * @param characteristic
	 * @see com.syrus.AMFICOM.general.Characterizable#addCharacteristic(Characteristic)
	 */
	public void addCharacteristic(final Characteristic characteristic) {
		throw new UnsupportedOperationException();
	}

	public Object clone() {
		final SchemePath schemePath = (SchemePath) super.clone();
		/**
		 * @todo Update the newly created object.
		 */
		return schemePath;
	}

	public String getDescription() {
		throw new UnsupportedOperationException();
	}

	public void setDescription(String description) {
		throw new UnsupportedOperationException();
	}

	public SchemeElement endDevice() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param newEndDevice
	 * @see com.syrus.AMFICOM.scheme.SchemePath#endDevice(com.syrus.AMFICOM.scheme.corba.SchemeElement)
	 */
	public void endDevice(SchemeElement newEndDevice) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see com.syrus.AMFICOM.general.Characterizable#getCharacteristics()
	 */
	public Collection getCharacteristics() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see com.syrus.AMFICOM.general.Characterizable#getCharacteristicSort()
	 */
	public CharacteristicSort getCharacteristicSort() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see StorableObject#getDependencies()
	 */
	public List getDependencies() {
		throw new UnsupportedOperationException();
	}

	public PathElement[] links() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param newLinks
	 * @see com.syrus.AMFICOM.scheme.SchemePath#links(com.syrus.AMFICOM.scheme.corba.PathElement[])
	 */
	public void links(PathElement[] newLinks) {
		throw new UnsupportedOperationException();
	}

	public String getName() {
		throw new UnsupportedOperationException();
	}

	public void setName(String name) {
		throw new UnsupportedOperationException();
	}

	public TransmissionPath getTransmissionPath() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param newPathImpl
	 */
	public void setTransmissionPath(TransmissionPath newPathImpl) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param characteristic
	 * @see com.syrus.AMFICOM.general.Characterizable#removeCharacteristic(Characteristic)
	 */
	public void removeCharacteristic(final Characteristic characteristic) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see SchemePath#scheme()
	 */
	public Scheme scheme() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param newScheme
	 * @see SchemePath#scheme(Scheme)
	 */
	public void scheme(final Scheme newScheme) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param characteristics
	 * @see com.syrus.AMFICOM.general.Characterizable#setCharacteristics(java.util.Collection)
	 */
	public void setCharacteristics(Collection characteristics) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param characteristics
	 * @see com.syrus.AMFICOM.general.Characterizable#setCharacteristics0(java.util.Collection)
	 */
	public void setCharacteristics0(Collection characteristics) {
		throw new UnsupportedOperationException();
	}

	public SchemeElement startDevice() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param newStartDevice
	 * @see com.syrus.AMFICOM.scheme.SchemePath#startDevice(com.syrus.AMFICOM.scheme.corba.SchemeElement)
	 */
	public void startDevice(SchemeElement newStartDevice) {
		throw new UnsupportedOperationException();
	}

	public TransmissionPathType getTransmissionPathType() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param newTypeImpl
	 */
	public void setTransmissionPathType(TransmissionPathType newTypeImpl) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see com.syrus.AMFICOM.general.TransferableObject#getTransferable()
	 */
	public Object getTransferable() {
		throw new UnsupportedOperationException();
	}

	public static SchemePath createInstance(final Identifier creatorId)
			throws CreateObjectException {
		assert creatorId != null;
		try {
			final Date created = new Date();
			final SchemePath schemePath = new SchemePath(
					IdentifierPool
							.getGeneratedIdentifier(ObjectEntities.SCHEME_PATH_ENTITY_CODE),
					created, created, creatorId, creatorId,
					0L);
			schemePath.changed = true;
			return schemePath;
		} catch (final IllegalObjectEntityException ioee) {
			throw new CreateObjectException(
					"SchemePath.createInstance | cannot generate identifier ", ioee); //$NON-NLS-1$
		}
	}

	/**
	 * @deprecated Use {@link #createInstance(Identifier)} instead.
	 */
	public static SchemePath createInstance() {
		throw new UnsupportedOperationException();
	}
}
