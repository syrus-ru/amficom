/*-
 * $Id: SchemePort.java,v 1.26 2005/05/24 13:25:02 bass Exp $
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
import com.syrus.AMFICOM.general.corba.CharacteristicSort;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.scheme.corba.AbstractSchemePortDirectionType;
import com.syrus.AMFICOM.scheme.corba.SchemePort_Transferable;
import com.syrus.util.Log;

/**
 * #08 in hierarchy.
 *
 * @author $Author: bass $
 * @version $Revision: 1.26 $, $Date: 2005/05/24 13:25:02 $
 * @module scheme_v1
 */
public final class SchemePort extends AbstractSchemePort {
	private static final long serialVersionUID = 3256436993469658930L;

	private SchemePortDatabase schemePortDatabase;

	/**
	 * @param id
	 * @throws RetrieveObjectException
	 * @throws ObjectNotFoundException
	 */
	SchemePort(final Identifier id) throws RetrieveObjectException, ObjectNotFoundException {
		super(id);

		this.schemePortDatabase = (SchemePortDatabase) DatabaseContext.getDatabase(ObjectEntities.SCHEME_PORT_ENTITY_CODE);
		try {
			this.schemePortDatabase.retrieve(this);
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
	SchemePort(final Identifier id, final Date created,
			final Date modified, final Identifier creatorId,
			final Identifier modifierId, final long version,
			final String name, final String description,
			final AbstractSchemePortDirectionType directionType,
			final PortType portType, final Port port,
			final MeasurementPort measurementPort,
			final SchemeDevice parentSchemeDevice) {
		super(id, created, modified, creatorId, modifierId, version,
				name, description, directionType, portType,
				port, measurementPort,
				parentSchemeDevice);

		assert port == null || port.getSort().value() == PortSort._PORT_SORT_PORT;

		this.schemePortDatabase = (SchemePortDatabase) DatabaseContext.getDatabase(ObjectEntities.SCHEME_PORT_ENTITY_CODE);
	}

	/**
	 * @param transferable
	 * @throws CreateObjectException
	 */
	SchemePort(final SchemePort_Transferable transferable) throws CreateObjectException {
		this.schemePortDatabase = (SchemePortDatabase) DatabaseContext.getDatabase(ObjectEntities.SCHEME_PORT_ENTITY_CODE);
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
	public static SchemePort createInstance(final Identifier creatorId,
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
	public static SchemePort createInstance(final Identifier creatorId,
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
			final SchemePort schemePort = new SchemePort(
					IdentifierPool
							.getGeneratedIdentifier(ObjectEntities.SCHEME_PORT_ENTITY_CODE),
					created, created, creatorId, creatorId,
					0L, name, description, directionType,
					portType, port, measurementPort,
					parentSchemeDevice);
			schemePort.changed = true;
			if (port != null || portType != null)
				schemePort.portTypeSet = true;
			return schemePort;
		} catch (final IdentifierGenerationException ige) {
			throw new CreateObjectException(
					"SchemePort.createInstance | cannot generate identifier ", ige);
		}
	}

	public Object clone() {
		final SchemePort schemePort = (SchemePort) super.clone();
		/**
		 * @todo Update the newly created object.
		 */
		return schemePort;
	}

	/**
	 * @see AbstractSchemePort#getAbstractSchemeLink()
	 */
	public AbstractSchemeLink getAbstractSchemeLink() {
		return getSchemeLink();
	}

	/**
	 * @see com.syrus.AMFICOM.general.Characterizable#getCharacteristicSort()
	 */
	public CharacteristicSort getCharacteristicSort() {
		return CharacteristicSort.CHARACTERISTIC_SORT_SCHEMEPORT;
	}

	/**
	 * @see AbstractSchemePort#getPort()
	 */
	public Port getPort() {
		final Port port = super.getPort();
		assert port == null || port.getSort().value() == PortSort._PORT_SORT_PORT: ErrorMessages.OBJECT_BADLY_INITIALIZED;
		return port;
	}

	public SchemeCableThread getSchemeCableThread() {
		try {
			final Set schemeCableThreads = StorableObjectPool.getStorableObjectsByCondition(new LinkedIdsCondition(super.id, ObjectEntities.SCHEME_CABLE_THREAD_ENTITY_CODE), true);
			assert schemeCableThreads != null && schemeCableThreads.size() <= 1;
			return schemeCableThreads.isEmpty()
					? null
					: (SchemeCableThread) schemeCableThreads.iterator().next();
		} catch (final ApplicationException ae) {
			Log.debugException(ae, Log.SEVERE);
			return null;
		}
	}

	public SchemeLink getSchemeLink() {
		try {
			final Set schemeLinks = StorableObjectPool.getStorableObjectsByCondition(new LinkedIdsCondition(super.id, ObjectEntities.SCHEME_LINK_ENTITY_CODE), true);
			assert schemeLinks != null && schemeLinks.size() <= 1;
			return schemeLinks.isEmpty()
					? null
					: (SchemeLink) schemeLinks.iterator().next();
		} catch (final ApplicationException ae) {
			Log.debugException(ae, Log.SEVERE);
			return null;
		}
	}

	/**
	 * @see com.syrus.AMFICOM.general.TransferableObject#getTransferable()
	 */
	public IDLEntity getTransferable() {
		return new SchemePort_Transferable(
				super.getHeaderTransferable(), super.getName(),
				super.getDescription(),
				super.getDirectionType(),
				(Identifier_Transferable) super.portTypeId.getTransferable(),
				(Identifier_Transferable) super.portId.getTransferable(),
				(Identifier_Transferable) super.measurementPortId.getTransferable(),
				(Identifier_Transferable) super.parentSchemeDeviceId.getTransferable(),
				Identifier.createTransferables(super.getCharacteristics()));
	}

	/**
	 * @param port
	 * @see AbstractSchemePort#setPort(Port)
	 */
	public void setPort(final Port port) {
		assert port == null || port.getSort().value() == PortSort._PORT_SORT_PORT: ErrorMessages.NATURE_INVALID;
		super.setPort(port);
	}

	/**
	 * @param transferable
	 * @throws CreateObjectException
	 * @see com.syrus.AMFICOM.general.StorableObject#fromTransferable(IDLEntity)
	 */
	protected void fromTransferable(final IDLEntity transferable) throws CreateObjectException {
		final SchemePort_Transferable schemePort = (SchemePort_Transferable) transferable;
		super.fromTransferable(schemePort.header, schemePort.name,
				schemePort.description,
				schemePort.directionType, schemePort.portTypeId,
				schemePort.portId, schemePort.measurementPortId,
				schemePort.parentSchemeDeviceId,
				schemePort.characteristicIds);
	}
}
