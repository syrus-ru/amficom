/*
 * $Id: SchemeDevice.java,v 1.4 2005/03/15 17:47:57 bass Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme.corba;

import com.syrus.AMFICOM.general.*;
import com.syrus.AMFICOM.general.corba.*;
import java.util.*;

/**
 * @author $Author: bass $
 * @version $Revision: 1.4 $, $Date: 2005/03/15 17:47:57 $
 * @module scheme_v1
 */
public final class SchemeDevice extends AbstractCloneableStorableObject implements Namable,
		Describable, Characterizable {

	protected Identifier characteristicIds[] = null;

	protected Identifier schemeCablePortIds[] = null;

	protected Identifier schemePortIds[] = null;

	protected String thisDescription = null;

	protected String thisName = null;

	/**
	 * @param id
	 */
	protected SchemeDevice(Identifier id) {
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
	protected SchemeDevice(Identifier id, Date created, Date modified,
			Identifier creatorId, Identifier modifierId,
			long version) {
		super(id, created, modified, creatorId, modifierId, version);
	}

	/**
	 * @param characteristic
	 * @see com.syrus.AMFICOM.general.Characterizable#addCharacteristic(Characteristic)
	 */
	public void addCharacteristic(final Characteristic characteristic) {
		throw new UnsupportedOperationException();
	}

	public String description() {
		throw new UnsupportedOperationException();
	}

	public void description(String description) {
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

	public String name() {
		throw new UnsupportedOperationException();
	}

	public void name(String name) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param characteristic
	 * @see com.syrus.AMFICOM.general.Characterizable#removeCharacteristic(Characteristic)
	 */
	public void removeCharacteristic(final Characteristic characteristic) {
		throw new UnsupportedOperationException();
	}

	public SchemeCablePort[] schemeCablePorts() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param newSchemeCablePorts
	 * @see com.syrus.AMFICOM.scheme.corba.SchemeDevice#schemeCablePorts(com.syrus.AMFICOM.scheme.corba.SchemeCablePort[])
	 */
	public void schemeCablePorts(SchemeCablePort[] newSchemeCablePorts) {
		throw new UnsupportedOperationException();
	}

	public SchemePort[] schemePorts() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param newSchemePorts
	 * @see com.syrus.AMFICOM.scheme.corba.SchemeDevice#schemePorts(com.syrus.AMFICOM.scheme.corba.SchemePort[])
	 */
	public void schemePorts(SchemePort[] newSchemePorts) {
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

	public Object clone() {
		final SchemeDevice schemeDevice = (SchemeDevice) super.clone();
		/**
		 * @todo Update the newly created object.
		 */
		return schemeDevice;
	}

	/**
	 * @see com.syrus.AMFICOM.general.TransferableObject#getTransferable()
	 */
	public Object getTransferable() {
		throw new UnsupportedOperationException();
	}

	public static SchemeDevice createInstance(final Identifier creatorId)
			throws CreateObjectException {
		assert creatorId != null;
		try {
			final Date created = new Date();
			final SchemeDevice schemeDevice = new SchemeDevice(
					IdentifierPool
							.getGeneratedIdentifier(ObjectEntities.SCHEME_DEVICE_ENTITY_CODE),
					created, created, creatorId, creatorId,
					0L);
			schemeDevice.changed = true;
			return schemeDevice;
		} catch (final IllegalObjectEntityException ioee) {
			throw new CreateObjectException(
					"SchemeDevice.createInstance | cannot generate identifier ", ioee); //$NON-NLS-1$
		}
	}

	/**
	 * @deprecated Use {@link #createInstance(Identifier)}instead.
	 */
	public static SchemeDevice createInstance() {
		throw new UnsupportedOperationException();
	}
}
