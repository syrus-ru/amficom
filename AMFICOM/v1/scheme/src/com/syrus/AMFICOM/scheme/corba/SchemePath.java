/*
 * $Id: SchemePath.java,v 1.3 2005/03/11 17:26:58 bass Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme.corba;

import com.syrus.AMFICOM.configuration.*;
import com.syrus.AMFICOM.configuration.corba.*;
import com.syrus.AMFICOM.general.*;
import com.syrus.AMFICOM.general.corba.*;
import com.syrus.util.Log;
import java.util.*;

/**
 * @author $Author: bass $
 * @version $Revision: 1.3 $, $Date: 2005/03/11 17:26:58 $
 * @module scheme_v1
 */
public final class SchemePath extends CloneableStorableObject implements Namable,
		Describable, Characterizable,
		ComSyrusAmficomConfigurationSchemePath {
	protected Identifier _typeId = null;

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

	public String description() {
		throw new UnsupportedOperationException();
	}

	public void description(String description) {
		throw new UnsupportedOperationException();
	}

	public SchemeElement endDevice() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param newEndDevice
	 * @see com.syrus.AMFICOM.scheme.corba.SchemePath#endDevice(com.syrus.AMFICOM.scheme.corba.SchemeElement)
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

	public Date getCreated() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see StorableObject#getCreatorId()
	 */
	public Identifier getCreatorId() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see StorableObject#getDependencies()
	 */
	public List getDependencies() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see StorableObject#getHeaderTransferable()
	 */
	public StorableObject_Transferable getHeaderTransferable() {
		throw new UnsupportedOperationException();
	}

	public Identifier getId() {
		throw new UnsupportedOperationException();
	}

	public Date getModified() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see StorableObject#getModifierId()
	 */
	public Identifier getModifierId() {
		throw new UnsupportedOperationException();
	}

	public long getVersion() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see StorableObject#isChanged()
	 */
	public boolean isChanged() {
		throw new UnsupportedOperationException();
	}

	public PathElement[] links() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param newLinks
	 * @see com.syrus.AMFICOM.scheme.corba.SchemePath#links(com.syrus.AMFICOM.scheme.corba.PathElement[])
	 */
	public void links(PathElement[] newLinks) {
		throw new UnsupportedOperationException();
	}

	public String name() {
		throw new UnsupportedOperationException();
	}

	public void name(String name) {
		throw new UnsupportedOperationException();
	}

	public TransmissionPath_Transferable path() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param newPath
	 * @see com.syrus.AMFICOM.configuration.ComSyrusAmficomConfigurationSchemePath#path(com.syrus.AMFICOM.configuration.corba.TransmissionPath_Transferable)
	 */
	public void path(TransmissionPath_Transferable newPath) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see com.syrus.AMFICOM.configuration.ComSyrusAmficomConfigurationSchemePath#pathImpl()
	 */
	public TransmissionPath pathImpl() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param newPathImpl
	 * @see com.syrus.AMFICOM.configuration.ComSyrusAmficomConfigurationSchemePath#pathImpl(com.syrus.AMFICOM.configuration.TransmissionPath)
	 */
	public void pathImpl(TransmissionPath newPathImpl) {
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
	 * @see com.syrus.AMFICOM.scheme.corba.SchemePath#startDevice(com.syrus.AMFICOM.scheme.corba.SchemeElement)
	 */
	public void startDevice(SchemeElement newStartDevice) {
		throw new UnsupportedOperationException();
	}

	public TransmissionPathType_Transferable type() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param newType
	 * @see com.syrus.AMFICOM.configuration.ComSyrusAmficomConfigurationSchemePath#type(com.syrus.AMFICOM.configuration.corba.TransmissionPathType_Transferable)
	 */
	public void type(TransmissionPathType_Transferable newType) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see com.syrus.AMFICOM.configuration.ComSyrusAmficomConfigurationSchemePath#typeImpl()
	 */
	public TransmissionPathType typeImpl() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param newTypeImpl
	 * @see com.syrus.AMFICOM.configuration.ComSyrusAmficomConfigurationSchemePath#typeImpl(com.syrus.AMFICOM.configuration.TransmissionPathType)
	 */
	public void typeImpl(TransmissionPathType newTypeImpl) {
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
