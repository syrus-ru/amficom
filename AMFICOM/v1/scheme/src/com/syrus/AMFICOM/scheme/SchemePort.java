/*
 * $Id: SchemePort.java,v 1.2 2005/03/17 09:40:22 bass Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme;

import com.syrus.AMFICOM.configuration.*;
import com.syrus.AMFICOM.general.*;
import com.syrus.AMFICOM.general.corba.CharacteristicSort;
import com.syrus.AMFICOM.scheme.corba.AbstractSchemePortPackage.DirectionType;
import java.util.*;

/**
 * @author $Author: bass $
 * @version $Revision: 1.2 $, $Date: 2005/03/17 09:40:22 $
 * @module scheme_v1
 */
public final class SchemePort extends AbstractSchemePort {
	private static final long serialVersionUID = 3256436993469658930L;

	protected Identifier schemeCableThreadId = null;

	/**
	 * @param id
	 */
	protected SchemePort(Identifier id) {
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
	protected SchemePort(Identifier id, Date created, Date modified,
			Identifier creatorId, Identifier modifierId,
			long version) {
		super(id, created, modified, creatorId, modifierId, version);
	}

	public AbstractSchemeLink abstractSchemeLink() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param newAbstractSchemeLink
	 * @see com.syrus.AMFICOM.scheme.AbstractSchemePort#abstractSchemeLink(com.syrus.AMFICOM.scheme.corba.AbstractSchemeLink)
	 */
	public void abstractSchemeLink(AbstractSchemeLink newAbstractSchemeLink) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param characteristic
	 * @see com.syrus.AMFICOM.general.Characterizable#addCharacteristic(Characteristic)
	 */
	public void addCharacteristic(final Characteristic characteristic) {
		throw new UnsupportedOperationException();
	}

	public String getDescription() {
		throw new UnsupportedOperationException();
	}

	public void setDescription(String description) {
		throw new UnsupportedOperationException();
	}

	public DirectionType directionType() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param newDirectionType
	 * @see com.syrus.AMFICOM.scheme.AbstractSchemePort#directionType(com.syrus.AMFICOM.scheme.corba.AbstractSchemePortPackage.DirectionType)
	 */
	public void directionType(DirectionType newDirectionType) {
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

	public MeasurementPort getMeasurementPort() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param newMeasurementPortImpl
	 */
	public void setMeasurementPort(MeasurementPort newMeasurementPortImpl) {
		throw new UnsupportedOperationException();
	}

	/**
	 */
	public MeasurementPortType getMeasurementPortType() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param newMeasurementPortTypeImpl
	 */
	public void setMeasurementPortType(
			MeasurementPortType newMeasurementPortTypeImpl) {
		throw new UnsupportedOperationException();
	}

	public String getName() {
		throw new UnsupportedOperationException();
	}

	public void setName(String name) {
		throw new UnsupportedOperationException();
	}

	public Port getPort() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param newPortImpl
	 */
	public void setPort(Port newPortImpl) {
		throw new UnsupportedOperationException();
	}

	public PortType getPortType() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param newPortTypeImpl
	 */
	public void setPortType(PortType newPortTypeImpl) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param characteristic
	 * @see com.syrus.AMFICOM.general.Characterizable#removeCharacteristic(Characteristic)
	 */
	public void removeCharacteristic(final Characteristic characteristic) {
		throw new UnsupportedOperationException();
	}

	public SchemeCableThread schemeCableThread() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param newSchemeCableThread
	 * @see com.syrus.AMFICOM.scheme.SchemePort#schemeCableThread(com.syrus.AMFICOM.scheme.corba.SchemeCableThread)
	 */
	public void schemeCableThread(SchemeCableThread newSchemeCableThread) {
		throw new UnsupportedOperationException();
	}

	public SchemeDevice schemeDevice() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param newSchemeDevice
	 * @see com.syrus.AMFICOM.scheme.AbstractSchemePort#schemeDevice(com.syrus.AMFICOM.scheme.corba.SchemeDevice)
	 */
	public void schemeDevice(SchemeDevice newSchemeDevice) {
		throw new UnsupportedOperationException();
	}

	public SchemeLink schemeLink() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param newSchemeLink
	 * @see com.syrus.AMFICOM.scheme.SchemePort#schemeLink(com.syrus.AMFICOM.scheme.corba.SchemeLink)
	 */
	public void schemeLink(SchemeLink newSchemeLink) {
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
		final SchemePort schemePort = (SchemePort) super.clone();
		/**
		 * @todo Update the newly created object.
		 */
		return schemePort;
	}

	/**
	 * @see com.syrus.AMFICOM.general.TransferableObject#getTransferable()
	 */
	public Object getTransferable() {
		throw new UnsupportedOperationException();
	}

	public static SchemePort createInstance(final Identifier creatorId)
			throws CreateObjectException {
		assert creatorId != null;
		try {
			final Date created = new Date();
			final SchemePort schemePort = new SchemePort(
					IdentifierPool
							.getGeneratedIdentifier(ObjectEntities.SCHEME_PORT_ENTITY_CODE),
					created, created, creatorId, creatorId,
					0L);
			schemePort.changed = true;
			return schemePort;
		} catch (final IllegalObjectEntityException ioee) {
			throw new CreateObjectException(
					"SchemePort.createInstance | cannot generate identifier ", ioee); //$NON-NLS-1$
		}
	}

	/**
	 * @deprecated Use {@link #createInstance(Identifier)}instead.
	 */
	public static SchemePort createInstance() {
		throw new UnsupportedOperationException();
	}
}
