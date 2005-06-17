/*-
 * $Id: SchemeCablePort.java,v 1.32 2005/06/17 13:06:53 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme;

import java.util.Date;
import java.util.Set;

import org.omg.CORBA.portable.IDLEntity;

import com.syrus.AMFICOM.configuration.MeasurementPort;
import com.syrus.AMFICOM.configuration.Port;
import com.syrus.AMFICOM.configuration.PortType;
import com.syrus.AMFICOM.configuration.corba.PortSort;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.DatabaseContext;
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
import com.syrus.AMFICOM.general.corba.IdlIdentifier;
import com.syrus.AMFICOM.scheme.corba.AbstractSchemePortDirectionType;
import com.syrus.AMFICOM.scheme.corba.SchemeCablePort_Transferable;
import com.syrus.util.Log;

/**
 * #09 in hierarchy.
 *
 * @author $Author: bass $
 * @version $Revision: 1.32 $, $Date: 2005/06/17 13:06:53 $
 * @module scheme_v1
 */
public final class SchemeCablePort extends AbstractSchemePort {
	private static final long serialVersionUID = 4050767078690534455L;

	private SchemeCablePortDatabase schemeCablePortDatabase;

	/**
	 * @param id
	 * @throws RetrieveObjectException
	 * @throws ObjectNotFoundException
	 */
	SchemeCablePort(final Identifier id) throws RetrieveObjectException, ObjectNotFoundException {
		super(id);
		
		this.schemeCablePortDatabase = (SchemeCablePortDatabase) DatabaseContext.getDatabase(ObjectEntities.SCHEMECABLEPORT_CODE);
		try {
			this.schemeCablePortDatabase.retrieve(this);
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
	 * @param directionType
	 * @param portType
	 * @param port
	 * @param measurementPort
	 * @param parentSchemeDevice
	 */
	SchemeCablePort(final Identifier id, final Date created,
			final Date modified, final Identifier creatorId,
			final Identifier modifierId, final long version,
			final String name, final String description,
			final AbstractSchemePortDirectionType directionType,
			final PortType portType, final Port port,
			final MeasurementPort measurementPort,
			final SchemeDevice parentSchemeDevice) {
		super(id, created, modified, creatorId, modifierId, version,
				name, description, directionType, portType, port,
				measurementPort, parentSchemeDevice);

		assert port == null || port.getSort().value() == PortSort._PORT_SORT_CABLE_PORT;

		this.schemeCablePortDatabase = (SchemeCablePortDatabase) DatabaseContext.getDatabase(ObjectEntities.SCHEMECABLEPORT_CODE);
	}

	/**
	 * @param transferable
	 * @throws CreateObjectException
	 */
	SchemeCablePort(final SchemeCablePort_Transferable transferable) throws CreateObjectException {
		this.schemeCablePortDatabase = (SchemeCablePortDatabase) DatabaseContext.getDatabase(ObjectEntities.SCHEMECABLEPORT_CODE);
		fromTransferable(transferable);
	}

	/**
	 * A shorthand for
	 * {@link #createInstance(Identifier, String, String, AbstractSchemePortDirectionType, PortType, Port, MeasurementPort, SchemeDevice)}.
	 *
	 * @param creatorId
	 * @param name
	 * @param directionType
	 * @param parentSchemeDevice
	 * @throws CreateObjectException
	 */
	public static SchemeCablePort createInstance(final Identifier creatorId,
			final String name,
			final AbstractSchemePortDirectionType directionType,
			final SchemeDevice parentSchemeDevice)
			throws CreateObjectException {
		return createInstance(creatorId, name, "", directionType, null,
				null, null, parentSchemeDevice);
	}

	/**
	 * @param creatorId
	 * @param name
	 * @param description
	 * @param directionType
	 * @param portType
	 * @param port
	 * @param measurementPort
	 * @param parentSchemeDevice
	 * @throws CreateObjectException
	 */
	public static SchemeCablePort createInstance(final Identifier creatorId,
			final String name, final String description,
			final AbstractSchemePortDirectionType directionType,
			final PortType portType, final Port port,
			final MeasurementPort measurementPort,
			final SchemeDevice parentSchemeDevice)
			throws CreateObjectException {
		assert creatorId != null && !creatorId.isVoid(): ErrorMessages.NON_VOID_EXPECTED;
		assert name != null && name.length() != 0: ErrorMessages.NON_EMPTY_EXPECTED;
		assert description != null: ErrorMessages.NON_NULL_EXPECTED;
		assert directionType != null: ErrorMessages.NON_NULL_EXPECTED;
		assert parentSchemeDevice != null: ErrorMessages.NON_NULL_EXPECTED;

		try {
			final Date created = new Date();
			final SchemeCablePort schemeCablePort = new SchemeCablePort(
					IdentifierPool
							.getGeneratedIdentifier(ObjectEntities.SCHEMECABLEPORT_CODE),
					created, created, creatorId, creatorId,
					0L, name, description, directionType,
					portType, port, measurementPort,
					parentSchemeDevice);
			schemeCablePort.markAsChanged();
			if (port != null || portType != null)
				schemeCablePort.portTypeSet = true;
			return schemeCablePort;
		} catch (final IdentifierGenerationException ige) {
			throw new CreateObjectException(
					"SchemeCablePort.createInstance | cannot generate identifier ", ige);
		}
	}

	public Object clone() {
		final SchemeCablePort schemeCablePort = (SchemeCablePort) super
				.clone();
		/**
		 * @todo Update the newly created object.
		 */
		return schemeCablePort;
	}

	/**
	 * @see AbstractSchemePort#getAbstractSchemeLink()
	 */
	public AbstractSchemeLink getAbstractSchemeLink() {
		return getSchemeCableLink();
	}

	/**
	 * @see AbstractSchemePort#getPort()
	 */
	public Port getPort() {
		final Port port = super.getPort();
		assert port == null || port.getSort().value() == PortSort._PORT_SORT_CABLE_PORT: ErrorMessages.OBJECT_BADLY_INITIALIZED;
		return port;
	}

	/**
	 * @todo parameter breakOnLoadError to StorableObjectPool.getStorableObjectsByCondition
	 */
	public SchemeCableLink getSchemeCableLink() {
		try {
			final Set schemeCableLinks = StorableObjectPool.getStorableObjectsByCondition(new LinkedIdsCondition(super.id, ObjectEntities.SCHEMECABLELINK_CODE), true, true);
			assert schemeCableLinks != null && schemeCableLinks.size() <= 1;
			return schemeCableLinks.isEmpty()
					? null
					: (SchemeCableLink) schemeCableLinks.iterator().next();
		} catch (final ApplicationException ae) {
			Log.debugException(ae, Log.SEVERE);
			return null;
		}
	}

	/**
	 * @see com.syrus.AMFICOM.general.TransferableObject#getTransferable()
	 */
	public IDLEntity getTransferable() {
		return new SchemeCablePort_Transferable(
				super.getHeaderTransferable(), super.getName(),
				super.getDescription(),
				super.getDirectionType(),
				(IdlIdentifier) super.portTypeId.getTransferable(),
				(IdlIdentifier) super.portId.getTransferable(),
				(IdlIdentifier) super.measurementPortId.getTransferable(),
				(IdlIdentifier) super.parentSchemeDeviceId.getTransferable(),
				Identifier.createTransferables(super.getCharacteristics()));
	}

	/**
	 * @param port
	 * @see AbstractSchemePort#setPort(Port)
	 */
	public void setPort(final Port port) {
		assert port == null || port.getSort().value() == PortSort._PORT_SORT_CABLE_PORT: ErrorMessages.NATURE_INVALID;
		super.setPort(port);
	}

	/**
	 * @param transferable
	 * @throws CreateObjectException
	 * @see com.syrus.AMFICOM.general.StorableObject#fromTransferable(IDLEntity)
	 */
	protected void fromTransferable(final IDLEntity transferable) throws CreateObjectException {
		final SchemeCablePort_Transferable schemeCablePort = (SchemeCablePort_Transferable) transferable;
		super.fromTransferable(schemeCablePort.header,
				schemeCablePort.name,
				schemeCablePort.description,
				schemeCablePort.directionType,
				schemeCablePort.cablePortTypeId,
				schemeCablePort.cablePortId,
				schemeCablePort.measurementPortId,
				schemeCablePort.parentSchemeDeviceId,
				schemeCablePort.characteristicIds);
	}
}
