/*-
 * $Id: SchemeCablePort.java,v 1.89.6.1 2006/05/18 17:50:00 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme;

import static com.syrus.AMFICOM.general.ErrorMessages.EXACTLY_ONE_PARENT_REQUIRED;
import static com.syrus.AMFICOM.general.ErrorMessages.NATURE_INVALID;
import static com.syrus.AMFICOM.general.ErrorMessages.NON_EMPTY_EXPECTED;
import static com.syrus.AMFICOM.general.ErrorMessages.NON_NULL_EXPECTED;
import static com.syrus.AMFICOM.general.ErrorMessages.NON_VOID_EXPECTED;
import static com.syrus.AMFICOM.general.ErrorMessages.OBJECT_BADLY_INITIALIZED;
import static com.syrus.AMFICOM.general.ErrorMessages.OBJECT_NOT_INITIALIZED;
import static com.syrus.AMFICOM.general.ErrorMessages.OBJECT_STATE_ILLEGAL;
import static com.syrus.AMFICOM.general.ErrorMessages.OBJECT_WILL_DELETE_ITSELF_FROM_POOL;
import static com.syrus.AMFICOM.general.ErrorMessages.XML_BEAN_NOT_COMPLETE;
import static com.syrus.AMFICOM.general.Identifier.VOID_IDENTIFIER;
import static com.syrus.AMFICOM.general.Identifier.XmlConversionMode.MODE_RETURN_VOID_IF_ABSENT;
import static com.syrus.AMFICOM.general.Identifier.XmlConversionMode.MODE_THROW_IF_ABSENT;
import static com.syrus.AMFICOM.general.ObjectEntities.SCHEMECABLELINK_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SCHEMECABLEPORT_CODE;
import static com.syrus.AMFICOM.general.StorableObjectVersion.INITIAL_VERSION;
import static com.syrus.AMFICOM.general.XmlComplementor.ComplementationMode.EXPORT;
import static com.syrus.AMFICOM.general.XmlComplementor.ComplementationMode.POST_IMPORT;
import static com.syrus.AMFICOM.general.XmlComplementor.ComplementationMode.PRE_IMPORT;
import static java.util.logging.Level.SEVERE;
import static java.util.logging.Level.WARNING;

import java.util.Date;
import java.util.Set;

import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.configuration.Port;
import com.syrus.AMFICOM.configuration.PortType;
import com.syrus.AMFICOM.configuration.corba.IdlPortTypePackage.PortTypeKind;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.LocalXmlIdentifierPool;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.general.XmlComplementorRegistry;
import com.syrus.AMFICOM.general.xml.XmlIdentifier;
import com.syrus.AMFICOM.measurement.MeasurementPort;
import com.syrus.AMFICOM.scheme.corba.IdlSchemeCablePort;
import com.syrus.AMFICOM.scheme.corba.IdlSchemeCablePortHelper;
import com.syrus.AMFICOM.scheme.corba.IdlAbstractSchemePortPackage.IdlDirectionType;
import com.syrus.AMFICOM.scheme.xml.XmlSchemeCablePort;
import com.syrus.util.Log;
import com.syrus.util.transport.idl.IdlConversionException;
import com.syrus.util.transport.idl.IdlTransferableObjectExt;
import com.syrus.util.transport.xml.XmlConversionException;
import com.syrus.util.transport.xml.XmlTransferableObject;

/**
 * #11 in hierarchy.
 *
 * @author $Author: bass $
 * @version $Revision: 1.89.6.1 $, $Date: 2006/05/18 17:50:00 $
 * @module scheme
 */
public final class SchemeCablePort extends AbstractSchemePort
		implements XmlTransferableObject<XmlSchemeCablePort>,
		IdlTransferableObjectExt<IdlSchemeCablePort> {
	private static final long serialVersionUID = 4050767078690534455L;

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

		assert portType == null || portType.getKind() == PortTypeKind.PORT_KIND_CABLE;
		assert port == null || port.getType().getKind() == PortTypeKind.PORT_KIND_CABLE;
	}

	/**
	 * Minimalistic constructor used when importing from XML.
	 *
	 * @param id
	 * @param importType
	 * @param created
	 * @param creatorId
	 * @throws IdentifierGenerationException
	 */
	private SchemeCablePort(final XmlIdentifier id,
			final String importType,
			final Date created,
			final Identifier creatorId)
	throws IdentifierGenerationException {
		super(id, importType, SCHEMECABLEPORT_CODE, created, creatorId);
	}

	/**
	 * @param transferable
	 * @throws CreateObjectException
	 */
	public SchemeCablePort(final IdlSchemeCablePort transferable) throws CreateObjectException {
		try {
			fromIdlTransferable(transferable);
		} catch (final IdlConversionException ice) {
			throw new CreateObjectException(ice);
		}
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
			final SchemeDevice parentSchemeDevice)
	throws CreateObjectException {
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
					INITIAL_VERSION,
					name,
					description,
					directionType,
					portType,
					port,
					measurementPort,
					parentSchemeDevice);
			parentSchemeDevice.getSchemeCablePortContainerWrappee().addToCache(schemeCablePort);
			
			schemeCablePort.portTypeSet = (port != null || portType != null);

			schemeCablePort.markAsChanged();
			return schemeCablePort;
		} catch (final CreateObjectException coe) {
			throw coe;
		} catch (final IdentifierGenerationException ige) {
			throw new CreateObjectException("SchemeCablePort.createInstance | cannot generate identifier ", ige);
		} catch (final ApplicationException ae) {
			throw new CreateObjectException(ae);
		}
	}

	/**
	 * @param creatorId
	 * @param xmlSchemeCablePort
	 * @param importType
	 * @throws CreateObjectException
	 */
	public static SchemeCablePort createInstance(final Identifier creatorId,
			final XmlSchemeCablePort xmlSchemeCablePort,
			final String importType)
	throws CreateObjectException {
		assert creatorId != null && !creatorId.isVoid() : NON_VOID_EXPECTED;

		try {
			final XmlIdentifier xmlId = xmlSchemeCablePort.getId();
			final Date created = new Date();
			final Identifier id = Identifier.fromXmlTransferable(xmlId, importType, MODE_RETURN_VOID_IF_ABSENT);
			SchemeCablePort schemeCablePort;
			if (id.isVoid()) {
				schemeCablePort = new SchemeCablePort(xmlId,
						importType,
						created,
						creatorId);
			} else {
				schemeCablePort = StorableObjectPool.getStorableObject(id, true);
				if (schemeCablePort == null) {
					LocalXmlIdentifierPool.remove(xmlId, importType);
					schemeCablePort = new SchemeCablePort(xmlId,
							importType,
							created,
							creatorId);
				}
			}
			schemeCablePort.fromXmlTransferable(xmlSchemeCablePort, importType);
			assert schemeCablePort.isValid() : OBJECT_BADLY_INITIALIZED;
			schemeCablePort.markAsChanged();
			return schemeCablePort;
		} catch (final CreateObjectException coe) {
			throw coe;
		} catch (final ApplicationException ae) {
			throw new CreateObjectException(ae);
		} catch (final XmlConversionException xce) {
			throw new CreateObjectException(xce);
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
			assert schemeCableLinks != null : NON_NULL_EXPECTED; 
			assert schemeCableLinks.size() <= 1;
			return schemeCableLinks.isEmpty()
					? null
					: schemeCableLinks.iterator().next();
		} catch (final ApplicationException ae) {
			Log.debugMessage(ae, SEVERE);
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
	 * @see com.syrus.util.transport.idl.IdlTransferableObject#getIdlTransferable(org.omg.CORBA.ORB)
	 */
	@Override
	public IdlSchemeCablePort getIdlTransferable(final ORB orb) {
		assert this.isValid() : OBJECT_STATE_ILLEGAL;

		return IdlSchemeCablePortHelper.init(orb,
				this.id.getIdlTransferable(),
				this.created.getTime(),
				this.modified.getTime(),
				this.creatorId.getIdlTransferable(),
				this.modifierId.getIdlTransferable(),
				this.version.longValue(),
				super.getName(),
				super.getDescription(),
				super.getDirectionType(),
				super.portTypeId.getIdlTransferable(),
				super.portId.getIdlTransferable(),
				super.measurementPortId.getIdlTransferable(),
				super.parentSchemeDeviceId.getIdlTransferable());
	}

	/**
	 * @param schemeCablePort
	 * @param importType
	 * @throws XmlConversionException
	 * @see com.syrus.util.transport.xml.XmlTransferableObject#getXmlTransferable(org.apache.xmlbeans.XmlObject, String)
	 */
	public void getXmlTransferable(
			final XmlSchemeCablePort schemeCablePort,
			final String importType)
	throws XmlConversionException {
		try {
			super.getXmlTransferable(schemeCablePort, importType);
			if (schemeCablePort.isSetCablePortTypeId()) {
				schemeCablePort.unsetCablePortTypeId();
			}
			if (!super.portTypeId.isVoid()) {
				super.portTypeId.getXmlTransferable(schemeCablePort.addNewCablePortTypeId(), importType);
			}
			if (schemeCablePort.isSetCablePortId()) {
				schemeCablePort.unsetCablePortId();
			}
			if (!super.portId.isVoid()) {
				super.portId.getXmlTransferable(schemeCablePort.addNewCablePortId(), importType);
			}
			XmlComplementorRegistry.complementStorableObject(schemeCablePort, SCHEMECABLEPORT_CODE, importType, EXPORT);
		} catch (final ApplicationException ae) {
			throw new XmlConversionException(ae);
		}
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
	 * @param schemeCablePort
	 * @throws IdlConversionException
	 * @see com.syrus.AMFICOM.general.StorableObject#fromIdlTransferable(com.syrus.AMFICOM.general.corba.IdlStorableObject)
	 */
	public void fromIdlTransferable(final IdlSchemeCablePort schemeCablePort)
	throws IdlConversionException {
		synchronized (this) {
			super.fromIdlTransferable(schemeCablePort,
					schemeCablePort.cablePortTypeId,
					schemeCablePort.cablePortId);
		}

		assert this.isValid() : OBJECT_STATE_ILLEGAL;
	}

	/**
	 * @param schemeCablePort
	 * @param importType
	 * @throws XmlConversionException
	 * @see XmlTransferableObject#fromXmlTransferable(org.apache.xmlbeans.XmlObject, String)
	 */
	public void fromXmlTransferable(final XmlSchemeCablePort schemeCablePort,
			final String importType)
	throws XmlConversionException {
		try {
			XmlComplementorRegistry.complementStorableObject(schemeCablePort, SCHEMECABLEPORT_CODE, importType, PRE_IMPORT);
	
			super.fromXmlTransferable(schemeCablePort, importType);
	
			final boolean setCablePortTypeId = schemeCablePort.isSetCablePortTypeId();
			final boolean setCablePortId = schemeCablePort.isSetCablePortId();
			if (setCablePortTypeId) {
				assert !setCablePortId : OBJECT_STATE_ILLEGAL;
	
				super.portTypeId = Identifier.fromXmlTransferable(schemeCablePort.getCablePortTypeId(), importType, MODE_THROW_IF_ABSENT);
				super.portId = VOID_IDENTIFIER;
			} else if (setCablePortId) {
				assert !setCablePortTypeId : OBJECT_STATE_ILLEGAL;
	
				super.portTypeId = VOID_IDENTIFIER;
				super.portId = Identifier.fromXmlTransferable(schemeCablePort.getCablePortId(), importType, MODE_THROW_IF_ABSENT);
			} else {
				throw new XmlConversionException(
						"SchemeCablePort.fromXmlTransferable() | "
						+ XML_BEAN_NOT_COMPLETE);
			}
	
			XmlComplementorRegistry.complementStorableObject(schemeCablePort, SCHEMECABLEPORT_CODE, importType, POST_IMPORT);
		} catch (final ApplicationException ae) {
			throw new XmlConversionException(ae);
		}
	}

	/**
	 * @param parentSchemeDevice
	 * @throws ApplicationException
	 */
	@Override
	public final void setParentSchemeDevice(
			final SchemeDevice parentSchemeDevice)
	throws ApplicationException {
		assert this.parentSchemeDeviceId != null: OBJECT_NOT_INITIALIZED;
		assert !this.parentSchemeDeviceId.isVoid(): EXACTLY_ONE_PARENT_REQUIRED;

		final Identifier newParentSchemeDeviceId = Identifier.possiblyVoid(parentSchemeDevice);
		if (this.parentSchemeDeviceId.equals(newParentSchemeDeviceId)) {
			return;
		}

		this.getParentSchemeDevice().getSchemeCablePortContainerWrappee().removeFromCache(this);

		if (parentSchemeDevice == null) {
			Log.debugMessage(OBJECT_WILL_DELETE_ITSELF_FROM_POOL, WARNING);
			StorableObjectPool.delete(this.getReverseDependencies());
		} else {
			parentSchemeDevice.getSchemeCablePortContainerWrappee().addToCache(this);
		}

		this.parentSchemeDeviceId = newParentSchemeDeviceId;
		super.markAsChanged();
	}

	/**
	 * @see com.syrus.AMFICOM.general.StorableObject#getWrapper()
	 */
	@Override
	protected SchemeCablePortWrapper getWrapper() {
		return SchemeCablePortWrapper.getInstance();
	}
}
