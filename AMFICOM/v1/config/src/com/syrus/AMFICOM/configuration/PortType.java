/*-
 * $Id: PortType.java,v 1.93 2005/09/21 13:22:09 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.configuration;

import static com.syrus.AMFICOM.general.ErrorMessages.NON_VOID_EXPECTED;
import static com.syrus.AMFICOM.general.ErrorMessages.OBJECT_BADLY_INITIALIZED;
import static com.syrus.AMFICOM.general.Identifier.XmlConversionMode.MODE_RETURN_VOID_IF_ABSENT;
import static com.syrus.AMFICOM.general.ObjectEntities.CHARACTERISTIC_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.PORT_TYPE_CODE;
import static java.util.logging.Level.SEVERE;

import java.util.Collections;
import java.util.Date;
import java.util.Set;

import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.configuration.corba.IdlPortType;
import com.syrus.AMFICOM.configuration.corba.IdlPortTypeHelper;
import com.syrus.AMFICOM.configuration.corba.IdlPortTypePackage.PortTypeKind;
import com.syrus.AMFICOM.configuration.corba.IdlPortTypePackage.PortTypeSort;
import com.syrus.AMFICOM.configuration.xml.XmlPortType;
import com.syrus.AMFICOM.configuration.xml.XmlPortTypeKind;
import com.syrus.AMFICOM.configuration.xml.XmlPortTypeSort;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Characteristic;
import com.syrus.AMFICOM.general.Characterizable;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.LocalXmlIdentifierPool;
import com.syrus.AMFICOM.general.Namable;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectType;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.general.XmlBeansTransferable;
import com.syrus.AMFICOM.general.corba.IdlStorableObject;
import com.syrus.AMFICOM.general.xml.XmlIdentifier;
import com.syrus.util.Log;
import com.syrus.util.Shitlet;

/**
 * @version $Revision: 1.93 $, $Date: 2005/09/21 13:22:09 $
 * @author $Author: bass $
 * @author Tashoyan Arseniy Feliksovich
 * @module config
 */

public final class PortType extends StorableObjectType implements Characterizable, Namable, XmlBeansTransferable<XmlPortType> {
	private static final long serialVersionUID = -115251480084275101L;

	private String name;
	private int sort;
	private int kind;

	private transient StorableObjectContainerWrappee<Characteristic> characteristicContainerWrappee;

	public PortType(final IdlPortType ptt) throws CreateObjectException {
		try {
			this.fromTransferable(ptt);
		} catch (ApplicationException ae) {
			throw new CreateObjectException(ae);
		}
	}

	PortType(final Identifier id,
			final Identifier creatorId,
			final StorableObjectVersion version,
			final String codename,
			final String description,
			final String name,
			final int sort,
			final int kind) {
		super(id,
				new Date(System.currentTimeMillis()),
				new Date(System.currentTimeMillis()),
				creatorId,
				creatorId,
				version,
				codename,
				description);
		this.name = name;
		this.sort = sort;
		this.kind = kind;
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
	private PortType(final XmlIdentifier id,
			final String importType,
			final Date created,
			final Identifier creatorId)
	throws IdentifierGenerationException {
		super(Identifier.fromXmlTransferable(id, importType, PORT_TYPE_CODE),
				created,
				created,
				creatorId,
				creatorId,
				StorableObjectVersion.createInitial(),
				null,
				null);
	}

	/**
	 * @param creatorId
	 * @param importType
	 * @param xmlPortType
	 * @throws CreateObjectException
	 */
	public  static PortType createInstance(
			final Identifier creatorId,
			final String importType,
			final XmlPortType xmlPortType)
	throws CreateObjectException {
		assert creatorId != null && !creatorId.isVoid() : NON_VOID_EXPECTED;

		try {
			final XmlIdentifier xmlId = xmlPortType.getId();
			final Date created = new Date();
			final Identifier id = Identifier.fromXmlTransferable(xmlId, importType, MODE_RETURN_VOID_IF_ABSENT);
			PortType portType;
			if (id.isVoid()) {
				portType = new PortType(xmlId,
						importType,
						created,
						creatorId);
			} else {
				portType = StorableObjectPool.getStorableObject(id, true);
				if (portType == null) {
					LocalXmlIdentifierPool.remove(xmlId, importType);
					portType = new PortType(xmlId,
							importType,
							created,
							creatorId);
				}
			}
			portType.fromXmlTransferable(xmlPortType, importType);
			assert portType.isValid() : OBJECT_BADLY_INITIALIZED;
			portType.markAsChanged();
			return portType;
		} catch (final CreateObjectException coe) {
			throw coe;
		} catch (final ApplicationException ae) {
			Log.debugException(ae, SEVERE);
			throw new CreateObjectException(ae);
		}
	}

	/**
	 * create new instance for client
	 * @param creatorId
	 * @param codename
	 * @param description
	 * @param name
	 * @param sort
	 * @param kind
	 * @throws CreateObjectException
	 */
	public static PortType createInstance(final Identifier creatorId,
			final String codename,
			final String description,
			final String name,
			final PortTypeSort sort,
			final PortTypeKind kind) throws CreateObjectException{
		if (creatorId == null || codename == null || name == null || description == null ||
				sort == null || kind == null)
			throw new IllegalArgumentException("Argument is 'null'");

		try {
			final PortType portType = new PortType(IdentifierPool.getGeneratedIdentifier(PORT_TYPE_CODE),
					creatorId,
					StorableObjectVersion.createInitial(),
					codename,
					description,
					name,
					sort.value(),
					kind.value());

			assert portType.isValid() : OBJECT_BADLY_INITIALIZED;

			portType.markAsChanged();

			return portType;
		} catch (IdentifierGenerationException ige) {
			throw new CreateObjectException("Cannot generate identifier ", ige);
		}
	}

	@Override
	protected synchronized void fromTransferable(final IdlStorableObject transferable) throws ApplicationException {
		final IdlPortType ptt = (IdlPortType) transferable;
		super.fromTransferable(ptt, ptt.codename, ptt.description);
		this.name = ptt.name;
		this.sort = ptt.sort.value();
		this.kind = ptt.kind.value();
	}

	/**
	 * @param portType
	 * @param importType
	 * @throws ApplicationException
	 * @see XmlBeansTransferable#fromXmlTransferable(com.syrus.AMFICOM.general.xml.XmlStorableObject, String)
	 */
	@Shitlet
	public void fromXmlTransferable(final XmlPortType portType,
			final String importType)
	throws ApplicationException {
		this.name = portType.getName();
		this.codename = portType.getCodename();
		this.description = portType.isSetDescription()
				? portType.getDescription()
				: "";
		this.sort = portType.getSort().intValue();
		this.kind = portType.getKind().intValue();
	}

	/**
	 * @param orb
	 * @see com.syrus.AMFICOM.general.TransferableObject#getTransferable(org.omg.CORBA.ORB)
	 */
	@Override
	public IdlPortType getTransferable(final ORB orb) {
		return IdlPortTypeHelper.init(orb,
				super.id.getTransferable(),
				super.created.getTime(),
				super.modified.getTime(),
				super.creatorId.getTransferable(),
				super.modifierId.getTransferable(),
				super.version.longValue(),
				super.codename,
				super.description != null ? super.description : "",
				this.name != null ? this.name : "",
				PortTypeSort.from_int(this.sort),
				PortTypeKind.from_int(this.kind));
	}

	/**
	 * @param portType
	 * @param importType
	 * @throws ApplicationException
	 * @see XmlBeansTransferable#getXmlTransferable(com.syrus.AMFICOM.general.xml.XmlStorableObject, String)
	 */
	@Shitlet
	public void getXmlTransferable(final XmlPortType portType,
			final String importType)
	throws ApplicationException {
		this.id.getXmlTransferable(portType.addNewId(), importType);
		portType.setName(this.name);
		portType.setCodename(this.codename);
		if (portType.isSetDescription()) {
			portType.unsetDescription();
		}
		if (this.description.length() != 0) {
			portType.setDescription(this.description);
		}
		portType.setSort(XmlPortTypeSort.Enum.forInt(this.getSort().value() + 1));
		portType.setKind(XmlPortTypeKind.Enum.forInt(this.getKind().value() + 1));
	}

	protected synchronized void setAttributes(final Date created,
			final Date modified,
			final Identifier creatorId,
			final Identifier modifierId,
			final StorableObjectVersion version,
			final String codename,
			final String description,
			final String name,
			final int sort,
			final int kind) {
		super.setAttributes(created,
				modified,
				creatorId,
				modifierId,
				version,
				codename,
				description);
		this.name = name;
		this.sort = sort;
		this.kind = kind;
	}

	public String getName() {
		return this.name;
	}

	public PortTypeSort getSort() {
		return PortTypeSort.from_int(this.sort);
	}

	public void setSort(final PortTypeSort sort) {
		this.sort = sort.value();
	}

	public PortTypeKind getKind() {
		return PortTypeKind.from_int(this.kind);
	}

	public void setName(final String name) {
		this.name = name;
		super.markAsChanged();
	}

	@Override
	public Set<Identifiable> getDependencies() {
		assert this.isValid() : OBJECT_BADLY_INITIALIZED;

		return Collections.emptySet();
	}

	public Set<Characteristic> getCharacteristics(final boolean usePool) throws ApplicationException {
		if (this.characteristicContainerWrappee == null) {
			this.characteristicContainerWrappee = new StorableObjectContainerWrappee<Characteristic>(this, CHARACTERISTIC_CODE);
		}
		return this.characteristicContainerWrappee.getContainees(usePool);
	}

}
