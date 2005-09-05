/*-
 * $Id: SchemeCablePort.java,v 1.55 2005/09/05 17:43:16 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme;

import static com.syrus.AMFICOM.general.ErrorMessages.NATURE_INVALID;
import static com.syrus.AMFICOM.general.ErrorMessages.NON_EMPTY_EXPECTED;
import static com.syrus.AMFICOM.general.ErrorMessages.NON_NULL_EXPECTED;
import static com.syrus.AMFICOM.general.ErrorMessages.NON_VOID_EXPECTED;
import static com.syrus.AMFICOM.general.ErrorMessages.OBJECT_BADLY_INITIALIZED;
import static com.syrus.AMFICOM.general.ObjectEntities.SCHEMECABLELINK_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SCHEMECABLEPORT_CODE;
import static java.util.logging.Level.SEVERE;

import java.util.Date;
import java.util.Set;

import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.configuration.Port;
import com.syrus.AMFICOM.configuration.PortType;
import com.syrus.AMFICOM.configuration.corba.IdlPortTypePackage.PortTypeKind;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.DatabaseContext;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.general.XmlBeansTransferable;
import com.syrus.AMFICOM.general.corba.IdlStorableObject;
import com.syrus.AMFICOM.measurement.MeasurementPort;
import com.syrus.AMFICOM.scheme.corba.IdlSchemeCablePort;
import com.syrus.AMFICOM.scheme.corba.IdlSchemeCablePortHelper;
import com.syrus.AMFICOM.scheme.corba.IdlAbstractSchemePortPackage.IdlDirectionType;
import com.syrus.AMFICOM.scheme.xml.XmlSchemeCablePort;
import com.syrus.util.Log;

/**
 * #11 in hierarchy.
 *
 * @author $Author: bass $
 * @version $Revision: 1.55 $, $Date: 2005/09/05 17:43:16 $
 * @module scheme
 */
public final class SchemeCablePort extends AbstractSchemePort
		implements XmlBeansTransferable<XmlSchemeCablePort> {
	private static final long serialVersionUID = 4050767078690534455L;

	/**
	 * @param id
	 * @throws RetrieveObjectException
	 * @throws ObjectNotFoundException
	 */
	SchemeCablePort(final Identifier id) throws RetrieveObjectException, ObjectNotFoundException {
		super(id);
		
		try {
			DatabaseContext.getDatabase(SCHEMECABLEPORT_CODE).retrieve(this);
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
	SchemeCablePort(final Identifier id,
			final Date created,
			final Date modified,
			final Identifier creatorId,
			final Identifier modifierId,
			final StorableObjectVersion version,
			final String name,
			final String description,
			final IdlDirectionType directionType,
			final PortType portType,
			final Port port,
			final MeasurementPort measurementPort,
			final SchemeDevice parentSchemeDevice) {
		super(id,
				created,
				modified,
				creatorId,
				modifierId,
				version,
				name,
				description,
				directionType,
				portType,
				port,
				measurementPort,
				parentSchemeDevice);

		assert port == null || port.getType().getKind().value() == PortTypeKind._PORT_KIND_CABLE;
	}

	/**
	 * @param transferable
	 * @throws CreateObjectException
	 */
	public SchemeCablePort(final IdlSchemeCablePort transferable) throws CreateObjectException {
		fromTransferable(transferable);
	}

	/**
	 * A shorthand for
	 * {@link #createInstance(Identifier, String, String, IdlDirectionType, PortType, Port, MeasurementPort, SchemeDevice)}.
	 *
	 * @param creatorId
	 * @param name
	 * @param directionType
	 * @param parentSchemeDevice
	 * @throws CreateObjectException
	 */
	public static SchemeCablePort createInstance(final Identifier creatorId,
			final String name,
			final IdlDirectionType directionType,
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
			final String name,
			final String description,
			final IdlDirectionType directionType,
			final PortType portType,
			final Port port,
			final MeasurementPort measurementPort,
			final SchemeDevice parentSchemeDevice) throws CreateObjectException {
		assert creatorId != null && !creatorId.isVoid() : NON_VOID_EXPECTED;
		assert name != null && name.length() != 0 : NON_EMPTY_EXPECTED;
		assert description != null : NON_NULL_EXPECTED;
		assert directionType != null : NON_NULL_EXPECTED;
		assert parentSchemeDevice != null : NON_NULL_EXPECTED;

		try {
			final Date created = new Date();
			final SchemeCablePort schemeCablePort = new SchemeCablePort(IdentifierPool.getGeneratedIdentifier(SCHEMECABLEPORT_CODE),
					created,
					created,
					creatorId,
					creatorId,
					StorableObjectVersion.createInitial(),
					name,
					description,
					directionType,
					portType,
					port,
					measurementPort,
					parentSchemeDevice);
			schemeCablePort.markAsChanged();
			if (port != null || portType != null)
				schemeCablePort.portTypeSet = true;
			return schemeCablePort;
		} catch (final IdentifierGenerationException ige) {
			throw new CreateObjectException("SchemeCablePort.createInstance | cannot generate identifier ", ige);
		}
	}

	@Override
	public SchemeCablePort clone() throws CloneNotSupportedException {
		return (SchemeCablePort) super.clone();
	}

	/**
	 * @see AbstractSchemePort#getAbstractSchemeLink()
	 */
	@Override
	public SchemeCableLink getAbstractSchemeLink() {
		try {
			final Set<SchemeCableLink> schemeCableLinks = StorableObjectPool.getStorableObjectsByCondition(new LinkedIdsCondition(super.id, SCHEMECABLELINK_CODE), true);
			assert schemeCableLinks != null && schemeCableLinks.size() <= 1;
			return schemeCableLinks.isEmpty()
					? null
					: schemeCableLinks.iterator().next();
		} catch (final ApplicationException ae) {
			Log.debugException(ae, SEVERE);
			return null;
		}
	}

	/**
	 * @see AbstractSchemePort#getPort()
	 */
	@Override
	public Port getPort() {
		final Port port = super.getPort();
		assert port == null || port.getType().getKind().value() == PortTypeKind._PORT_KIND_CABLE : OBJECT_BADLY_INITIALIZED;
		return port;
	}

	/**
	 * @param orb
	 * @see com.syrus.AMFICOM.general.TransferableObject#getTransferable(org.omg.CORBA.ORB)
	 */
	@Override
	public IdlSchemeCablePort getTransferable(final ORB orb) {
		return IdlSchemeCablePortHelper.init(orb,
				this.id.getTransferable(),
				this.created.getTime(),
				this.modified.getTime(),
				this.creatorId.getTransferable(),
				this.modifierId.getTransferable(),
				this.version.longValue(),
				super.getName(),
				super.getDescription(),
				super.getDirectionType(),
				super.portTypeId.getTransferable(),
				super.portId.getTransferable(),
				super.measurementPortId.getTransferable(),
				super.parentSchemeDeviceId.getTransferable());
	}

	/**
	 * @see XmlBeansTransferable#getXmlTransferable(String)
	 */
	public XmlSchemeCablePort getXmlTransferable(final String importType) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param port
	 * @see AbstractSchemePort#setPort(Port)
	 */
	@Override
	public void setPort(final Port port) {
		assert port == null || port.getType().getKind().value() == PortTypeKind._PORT_KIND_CABLE : NATURE_INVALID;
		super.setPort(port);
	}

	/**
	 * @param transferable
	 * @throws CreateObjectException
	 * @see com.syrus.AMFICOM.general.StorableObject#fromTransferable(IdlStorableObject)
	 */
	@Override
	protected synchronized void fromTransferable(final IdlStorableObject transferable) throws CreateObjectException {
		final IdlSchemeCablePort schemeCablePort = (IdlSchemeCablePort) transferable;
		super.fromTransferable(schemeCablePort,
				schemeCablePort.name,
				schemeCablePort.description,
				schemeCablePort.directionType,
				schemeCablePort.cablePortTypeId,
				schemeCablePort.cablePortId,
				schemeCablePort.measurementPortId,
				schemeCablePort.parentSchemeDeviceId);
	}

	/**
	 * @param xmlSchemeCablePort
	 * @param importType
	 * @throws ApplicationException
	 * @see XmlBeansTransferable#fromXmlTransferable(com.syrus.AMFICOM.general.xml.XmlStorableObject, String)
	 */
	public void fromXmlTransferable(
			final XmlSchemeCablePort xmlSchemeCablePort,
			final String importType)
	throws ApplicationException {
		throw new UnsupportedOperationException();
	}
}
