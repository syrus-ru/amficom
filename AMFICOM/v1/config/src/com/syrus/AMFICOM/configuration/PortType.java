/*
 * $Id: PortType.java,v 1.81 2005/08/30 16:35:08 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.configuration;

import static com.syrus.AMFICOM.general.ErrorMessages.OBJECT_STATE_ILLEGAL;
import static com.syrus.AMFICOM.general.ObjectEntities.PORT_TYPE_CODE;

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
import com.syrus.AMFICOM.general.CharacterizableDelegate;
import com.syrus.AMFICOM.general.ClonedIdsPool;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.DatabaseContext;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ImportUidMapDatabase;
import com.syrus.AMFICOM.general.Namable;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectType;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.general.XmlBeansTransferable;
import com.syrus.AMFICOM.general.corba.IdlStorableObject;
import com.syrus.AMFICOM.general.xml.XmlIdentifier;
import com.syrus.util.Shitlet;

/**
 * @version $Revision: 1.81 $, $Date: 2005/08/30 16:35:08 $
 * @author $Author: bass $
 * @module config
 */

public final class PortType extends StorableObjectType implements Characterizable, Namable, XmlBeansTransferable<XmlPortType> {
	private static final long serialVersionUID = -115251480084275101L;

	private String name;
	private int sort;
	private int kind;

	private transient CharacterizableDelegate characterizableDelegate;

	PortType(final Identifier id) throws ObjectNotFoundException, RetrieveObjectException {
		super(id);

		try {
			DatabaseContext.getDatabase(PORT_TYPE_CODE).retrieve(this);
		} catch (IllegalDataException ide) {
			throw new RetrieveObjectException(ide.getMessage(), ide);
		}
	}

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

	@Shitlet
	private PortType(final Identifier creatorId,
			final StorableObjectVersion version,
			final XmlPortType xmlPortType,
			final ClonedIdsPool clonedIdsPool,
			final String importType) throws CreateObjectException, ApplicationException {

		super(clonedIdsPool.getClonedId(PORT_TYPE_CODE, xmlPortType.getId().getStringValue()),
				new Date(System.currentTimeMillis()),
				new Date(System.currentTimeMillis()),
				creatorId,
				creatorId,
				version,
				"",
				"");
		this.fromXmlTransferable(xmlPortType, clonedIdsPool, importType);
	}

	@SuppressWarnings("unused")
	@Shitlet
	private static PortType createInstance(
			final Identifier creatorId,
			final String importType,
			final XmlPortType xmlPortType,
			final ClonedIdsPool clonedIdsPool) throws CreateObjectException {

		try {
			String uid = xmlPortType.getId().getStringValue();
			Identifier existingIdentifier = ImportUidMapDatabase.retrieve(importType, uid);
			PortType portType = null;
			if(existingIdentifier != null) {
				portType = StorableObjectPool.getStorableObject(existingIdentifier, true);
				if(portType != null) {
					portType.fromXmlTransferable(xmlPortType, clonedIdsPool, importType);
				}
				else{
					ImportUidMapDatabase.delete(importType, uid);
				}
			}
			if(portType == null) {
				portType = portType = new PortType(
						creatorId,
						StorableObjectVersion.createInitial(),
						xmlPortType,
						clonedIdsPool,
						importType);
				ImportUidMapDatabase.insert(importType, uid, portType.id);
			}
			assert portType.isValid() : OBJECT_STATE_ILLEGAL;
			portType.markAsChanged();
			return portType;
		} catch (Exception e) {
			System.out.println(xmlPortType);
			throw new CreateObjectException("PortType.createInstance |  ", e);
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

			assert portType.isValid() : OBJECT_STATE_ILLEGAL;

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
	 * @param xmlPortType
	 * @param clonedIdsPool
	 * @param importType
	 * @throws ApplicationException
	 * @see XmlBeansTransferable#fromXmlTransferable(com.syrus.AMFICOM.general.xml.XmlStorableObject, ClonedIdsPool, String)
	 */
	@Shitlet
	public void fromXmlTransferable(final XmlPortType xmlPortType,
			final ClonedIdsPool clonedIdsPool,
			final String importType)
	throws ApplicationException {
		this.name = xmlPortType.getName();
		this.codename = xmlPortType.getCodename();
		this.description = xmlPortType.getDescription();
		this.sort = xmlPortType.getSort().intValue();
		this.kind = xmlPortType.getKind().intValue();
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
	 * @see XmlBeansTransferable#getXmlTransferable()
	 */
	@Shitlet
	public XmlPortType getXmlTransferable() {
		final XmlPortType xmlPortType = XmlPortType.Factory.newInstance();
		XmlIdentifier uid = xmlPortType.addNewId();
		uid.setStringValue(this.id.toString());
		xmlPortType.setName(this.name);
		xmlPortType.setCodename(this.codename);
		xmlPortType.setDescription(this.description);
		xmlPortType.setSort(XmlPortTypeSort.Enum.forInt(this.sort));
		xmlPortType.setKind(XmlPortTypeKind.Enum.forInt(this.kind));
		return xmlPortType;
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
		assert this.isValid() : OBJECT_STATE_ILLEGAL;

		return Collections.emptySet();
	}

	public Set<Characteristic> getCharacteristics(final boolean usePool) throws ApplicationException {
		if (this.characterizableDelegate == null) {
			this.characterizableDelegate = new CharacterizableDelegate(this.id);
		}
		return this.characterizableDelegate.getCharacteristics(usePool);
	}

}
