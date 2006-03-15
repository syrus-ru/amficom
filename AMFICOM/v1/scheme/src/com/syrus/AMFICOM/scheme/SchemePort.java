/*-
 * $Id: SchemePort.java,v 1.89 2006/03/15 15:49:10 arseniy Exp $
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
import static com.syrus.AMFICOM.general.ObjectEntities.SCHEMECABLETHREAD_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SCHEMELINK_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SCHEMEPORT_CODE;
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
import com.syrus.AMFICOM.general.corba.IdlStorableObject;
import com.syrus.AMFICOM.general.xml.XmlIdentifier;
import com.syrus.AMFICOM.measurement.MeasurementPort;
import com.syrus.AMFICOM.scheme.corba.IdlSchemePort;
import com.syrus.AMFICOM.scheme.corba.IdlSchemePortHelper;
import com.syrus.AMFICOM.scheme.corba.IdlAbstractSchemePortPackage.IdlDirectionType;
import com.syrus.AMFICOM.scheme.xml.XmlSchemePort;
import com.syrus.util.Log;
import com.syrus.util.transport.idl.IdlConversionException;
import com.syrus.util.transport.xml.XmlConversionException;
import com.syrus.util.transport.xml.XmlTransferableObject;

/**
 * #10 in hierarchy.
 *
 * @author $Author: arseniy $
 * @version $Revision: 1.89 $, $Date: 2006/03/15 15:49:10 $
 * @module scheme
 */
public final class SchemePort extends AbstractSchemePort
		implements XmlTransferableObject<XmlSchemePort> {
	private static final long serialVersionUID = 3256436993469658930L;

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
	SchemePort(final Identifier id,
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

		assert portType == null || portType.getKind() == PortTypeKind.PORT_KIND_SIMPLE;
		assert port == null || port.getType().getKind() == PortTypeKind.PORT_KIND_SIMPLE;
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
	private SchemePort(final XmlIdentifier id,
			final String importType,
			final Date created,
			final Identifier creatorId)
	throws IdentifierGenerationException {
		super(id, importType, SCHEMEPORT_CODE, created, creatorId);
	}

	/**
	 * @param transferable
	 * @throws CreateObjectException
	 */
	public SchemePort(final IdlSchemePort transferable) throws CreateObjectException {
		try {
			this.fromIdlTransferable(transferable);
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
	public static SchemePort createInstance(final Identifier creatorId,
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
	@ParameterizationPending(value = {"final boolean usePool"})
	public static SchemePort createInstance(final Identifier creatorId,
			final String name,
			final String description,
			final IdlDirectionType directionType,
			final PortType portType,
			final Port port,
			final MeasurementPort measurementPort,
			final SchemeDevice parentSchemeDevice)
	throws CreateObjectException {
		final boolean usePool = false;

		assert creatorId != null && !creatorId.isVoid() : NON_VOID_EXPECTED;
		assert name != null && name.length() != 0 : NON_EMPTY_EXPECTED;
		assert description != null : NON_NULL_EXPECTED;
		assert directionType != null : NON_NULL_EXPECTED;
		assert parentSchemeDevice != null : NON_NULL_EXPECTED;

		try {
			final Date created = new Date();
			final SchemePort schemePort = new SchemePort(IdentifierPool.getGeneratedIdentifier(SCHEMEPORT_CODE),
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
			parentSchemeDevice.getSchemePortContainerWrappee().addToCache(schemePort, usePool);

			schemePort.portTypeSet = (port != null || portType != null);

			schemePort.markAsChanged();
			return schemePort;
		} catch (final CreateObjectException coe) {
			throw coe;
		} catch (final IdentifierGenerationException ige) {
			throw new CreateObjectException("SchemePort.createInstance | cannot generate identifier ", ige);
		} catch (final ApplicationException ae) {
			throw new CreateObjectException(ae);
		}
	}

	/**
	 * @param creatorId
	 * @param xmlSchemePort
	 * @param importType
	 * @throws CreateObjectException
	 */
	public static SchemePort createInstance(final Identifier creatorId,
			final XmlSchemePort xmlSchemePort,
			final String importType)
	throws CreateObjectException {
		assert creatorId != null && !creatorId.isVoid() : NON_VOID_EXPECTED;

		try {
			final XmlIdentifier xmlId = xmlSchemePort.getId();
			final Date created = new Date();
			final Identifier id = Identifier.fromXmlTransferable(xmlId, importType, MODE_RETURN_VOID_IF_ABSENT);
			SchemePort schemePort;
			if (id.isVoid()) {
				schemePort = new SchemePort(xmlId,
						importType,
						created,
						creatorId);
			} else {
				schemePort = StorableObjectPool.getStorableObject(id, true);
				if (schemePort == null) {
					LocalXmlIdentifierPool.remove(xmlId, importType);
					schemePort = new SchemePort(xmlId,
							importType,
							created,
							creatorId);
				}
			}
			schemePort.fromXmlTransferable(xmlSchemePort, importType);
			assert schemePort.isValid() : OBJECT_BADLY_INITIALIZED;
			schemePort.markAsChanged();
			return schemePort;
		} catch (final CreateObjectException coe) {
			throw coe;
		} catch (final ApplicationException ae) {
			throw new CreateObjectException(ae);
		} catch (final XmlConversionException xce) {
			throw new CreateObjectException(xce);
		}
	}

	@Override
	public SchemePort clone() throws CloneNotSupportedException {
		return (SchemePort) super.clone();
	}

	/**
	 * @see AbstractSchemePort#getAbstractSchemeLink()
	 */
	@Override
	public SchemeLink getAbstractSchemeLink() {
		try {
			final Set<SchemeLink> schemeLinks = StorableObjectPool.getStorableObjectsByCondition(new LinkedIdsCondition(super.id, SCHEMELINK_CODE), true);
			assert schemeLinks != null : NON_NULL_EXPECTED;
			assert schemeLinks.size() <= 1;
			return schemeLinks.isEmpty()
					? null
					: schemeLinks.iterator().next();
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
		assert port == null || port.getType().getKind().value() == PortTypeKind._PORT_KIND_SIMPLE : OBJECT_BADLY_INITIALIZED;
		return port;
	}

	public SchemeCableThread getSchemeCableThread() {
		try {
			final Set<SchemeCableThread> schemeCableThreads = StorableObjectPool.getStorableObjectsByCondition(new LinkedIdsCondition(super.id, SCHEMECABLETHREAD_CODE), true);
			assert schemeCableThreads != null && schemeCableThreads.size() <= 1;
			return schemeCableThreads.isEmpty()
					? null
					: schemeCableThreads.iterator().next();
		} catch (final ApplicationException ae) {
			Log.debugMessage(ae, SEVERE);
			return null;
		}
	}

	/**
	 * @param orb
	 * @see com.syrus.util.transport.idl.IdlTransferableObject#getIdlTransferable(org.omg.CORBA.ORB)
	 */
	@Override
	public IdlSchemePort getIdlTransferable(final ORB orb) {
		assert this.isValid() : OBJECT_STATE_ILLEGAL;

		return IdlSchemePortHelper.init(orb,
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
	 * @param schemePort
	 * @param importType
	 * @param usePool
	 * @throws XmlConversionException
	 * @see com.syrus.util.transport.xml.XmlTransferableObject#getXmlTransferable(org.apache.xmlbeans.XmlObject, String, boolean)
	 */
	public void getXmlTransferable(final XmlSchemePort schemePort,
			final String importType,
			final boolean usePool)
	throws XmlConversionException {
		try {
			super.getXmlTransferable(schemePort, importType, usePool);
			if (schemePort.isSetPortTypeId()) {
				schemePort.unsetPortTypeId();
			}
			if (!super.portTypeId.isVoid()) {
				super.portTypeId.getXmlTransferable(schemePort.addNewPortTypeId(), importType);
			}
			if (schemePort.isSetPortId()) {
				schemePort.unsetPortId();
			}
			if (!super.portId.isVoid()) {
				super.portId.getXmlTransferable(schemePort.addNewPortId(), importType);
			}
			XmlComplementorRegistry.complementStorableObject(schemePort, SCHEMEPORT_CODE, importType, EXPORT);
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
		assert port == null || port.getType().getKind().value() == PortTypeKind._PORT_KIND_SIMPLE : NATURE_INVALID;
		super.setPort(port);
	}

	/**
	 * @param transferable
	 * @throws IdlConversionException
	 * @see com.syrus.AMFICOM.general.StorableObject#fromIdlTransferable(IdlStorableObject)
	 */
	@Override
	protected void fromIdlTransferable(final IdlStorableObject transferable)
	throws IdlConversionException {
		synchronized (this) {
			final IdlSchemePort schemePort = (IdlSchemePort) transferable;
			super.fromTransferable(schemePort,
					schemePort.portTypeId,
					schemePort.portId);
		}

		assert this.isValid() : OBJECT_STATE_ILLEGAL;
	}

	/**
	 * @param schemePort
	 * @param importType
	 * @throws XmlConversionException
	 * @see XmlTransferableObject#fromXmlTransferable(org.apache.xmlbeans.XmlObject, String)
	 */
	public void fromXmlTransferable(final XmlSchemePort schemePort,
			final String importType)
	throws XmlConversionException {
		try {
			XmlComplementorRegistry.complementStorableObject(schemePort, SCHEMEPORT_CODE, importType, PRE_IMPORT);
	
			super.fromXmlTransferable(schemePort, importType);
	
			final boolean setPortTypeId = schemePort.isSetPortTypeId();
			final boolean setPortId = schemePort.isSetPortId();
			if (setPortTypeId) {
				assert !setPortId : OBJECT_STATE_ILLEGAL;
	
				super.portTypeId = Identifier.fromXmlTransferable(schemePort.getPortTypeId(), importType, MODE_THROW_IF_ABSENT);
				super.portId = VOID_IDENTIFIER;
			} else if (setPortId) {
				assert !setPortTypeId : OBJECT_STATE_ILLEGAL;
	
				super.portTypeId = VOID_IDENTIFIER;
				super.portId = Identifier.fromXmlTransferable(schemePort.getPortId(), importType, MODE_THROW_IF_ABSENT);
			} else {
				throw new XmlConversionException(
						"SchemePort.fromXmlTransferable() | "
						+ XML_BEAN_NOT_COMPLETE);
			}
	
			XmlComplementorRegistry.complementStorableObject(schemePort, SCHEMEPORT_CODE, importType, POST_IMPORT);
		} catch (final ApplicationException ae) {
			throw new XmlConversionException(ae);
		}
	}

	/**
	 * @param parentSchemeDevice
	 * @param usePool
	 * @throws ApplicationException
	 */
	@Override
	public final void setParentSchemeDevice(
			final SchemeDevice parentSchemeDevice,
			final boolean usePool)
	throws ApplicationException {
		assert this.parentSchemeDeviceId != null: OBJECT_NOT_INITIALIZED;
		assert !this.parentSchemeDeviceId.isVoid(): EXACTLY_ONE_PARENT_REQUIRED;

		final Identifier newParentSchemeDeviceId = Identifier.possiblyVoid(parentSchemeDevice);
		if (this.parentSchemeDeviceId.equals(newParentSchemeDeviceId)) {
			return;
		}

		this.getParentSchemeDevice().getSchemePortContainerWrappee().removeFromCache(this, usePool);

		if (parentSchemeDevice == null) {
			Log.debugMessage(OBJECT_WILL_DELETE_ITSELF_FROM_POOL, WARNING);
			StorableObjectPool.delete(this.getReverseDependencies(usePool));
		} else {
			parentSchemeDevice.getSchemePortContainerWrappee().addToCache(this, usePool);
		}

		this.parentSchemeDeviceId = newParentSchemeDeviceId;
		super.markAsChanged();
	}

	/**
	 * @see com.syrus.AMFICOM.general.StorableObject#getWrapper()
	 */
	@Override
	protected SchemePortWrapper getWrapper() {
		return SchemePortWrapper.getInstance();
	}
}
