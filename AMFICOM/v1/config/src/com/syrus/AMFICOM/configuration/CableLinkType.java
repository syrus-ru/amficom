/*
 * $Id: CableLinkType.java,v 1.65 2005/08/30 16:05:28 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.configuration;

import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.configuration.corba.IdlCableLinkType;
import com.syrus.AMFICOM.configuration.corba.IdlCableLinkTypeHelper;
import com.syrus.AMFICOM.configuration.corba.IdlAbstractLinkTypePackage.LinkTypeSort;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.ClonedIdsPool;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.DatabaseContext;
import com.syrus.AMFICOM.general.ErrorMessages;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ImportUidMapDatabase;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.general.XmlBeansTransferable;
import com.syrus.AMFICOM.general.corba.IdlStorableObject;
import com.syrus.AMFICOM.configuration.xml.XmlCableLinkType;
import com.syrus.AMFICOM.configuration.xml.XmlCableThreadType;
import com.syrus.AMFICOM.configuration.xml.XmlCableThreadTypeSeq;
import com.syrus.AMFICOM.configuration.xml.XmlLinkTypeSort;
import com.syrus.AMFICOM.general.xml.XmlIdentifier;
import com.syrus.util.Log;
import com.syrus.util.Shitlet;

/**
 * @version $Revision: 1.65 $, $Date: 2005/08/30 16:05:28 $
 * @author $Author: bass $
 * @module config
 */
public final class CableLinkType extends AbstractLinkType implements XmlBeansTransferable<XmlCableLinkType> {

	private static final long serialVersionUID = 3257007652839372857L;

	private String name;
	private int sort;
	private String manufacturer;
	private String manufacturerCode;
	private Identifier imageId;

	CableLinkType(final Identifier id) throws ObjectNotFoundException, RetrieveObjectException {
		super(id);

		try {
			DatabaseContext.getDatabase(ObjectEntities.CABLELINK_TYPE_CODE).retrieve(this);
		} catch (IllegalDataException ide) {
			throw new RetrieveObjectException(ide.getMessage(), ide);
		}
	}

	public CableLinkType(final IdlCableLinkType cltt) throws CreateObjectException {
		try {
			this.fromTransferable(cltt);
		} catch (ApplicationException ae) {
			throw new CreateObjectException(ae);
		}
	}

	CableLinkType(final Identifier id,
			final Identifier creatorId,
			final StorableObjectVersion version,
			final String codename,
			final String description,
			final String name,
			final int sort,
			final String manufacturer,
			final String manufacturerCode,
			final Identifier imageId) {
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
		this.manufacturer = manufacturer;
		this.manufacturerCode = manufacturerCode;
		this.imageId = imageId;
	}

	@Shitlet
	private CableLinkType(final Identifier creatorId,
			final StorableObjectVersion version,
			final XmlCableLinkType xmlCableLinkType,
			final ClonedIdsPool clonedIdsPool,
			final String importType) throws CreateObjectException, ApplicationException {

		super(clonedIdsPool.getClonedId(ObjectEntities.LINK_TYPE_CODE, xmlCableLinkType.getId().getStringValue()),
				new Date(System.currentTimeMillis()),
				new Date(System.currentTimeMillis()),
				creatorId,
				creatorId,
				version,
				"",
				"");
		this.fromXmlTransferable(xmlCableLinkType, clonedIdsPool, importType);
	}

	@SuppressWarnings("unused")
	@Shitlet
	private static CableLinkType createInstance(
			final Identifier creatorId,
			final String importType,
			final XmlCableLinkType xmlCableLinkType,
			final ClonedIdsPool clonedIdsPool) throws CreateObjectException {

		try {
			String uid = xmlCableLinkType.getId().getStringValue();
			Identifier existingIdentifier = ImportUidMapDatabase.retrieve(importType, uid);
			CableLinkType cableLinkType = null;
			if(existingIdentifier != null) {
				cableLinkType = StorableObjectPool.getStorableObject(existingIdentifier, true);
				if(cableLinkType != null) {
					cableLinkType.fromXmlTransferable(xmlCableLinkType, clonedIdsPool, importType);
				}
				else{
					ImportUidMapDatabase.delete(importType, uid);
				}
			}
			if(cableLinkType == null) {
				cableLinkType = cableLinkType = new CableLinkType(
						creatorId,
						StorableObjectVersion.createInitial(),
						xmlCableLinkType,
						clonedIdsPool,
						importType);
				ImportUidMapDatabase.insert(importType, uid, cableLinkType.id);
			}
			assert cableLinkType.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;
			cableLinkType.markAsChanged();
			return cableLinkType;
		} catch (Exception e) {
			System.out.println(xmlCableLinkType);
			throw new CreateObjectException("CableLinkType.createInstance |  ", e);
		}
	}

	/**
	 * create new instance for client
	 * @throws CreateObjectException
	 */
	public static CableLinkType createInstance(final Identifier creatorId,
			final String codename,
			final String description,
			final String name,
			final LinkTypeSort sort,
			final String manufacturer,
			final String manufacturerCode,
			final Identifier imageId) throws CreateObjectException {
		if (creatorId == null
				|| codename == null
				|| description == null
				|| name == null
				|| sort == null
				|| manufacturer == null
				|| manufacturerCode == null)
			throw new IllegalArgumentException("Argument is 'null'");

		try {
			final CableLinkType cableLinkType = new CableLinkType(IdentifierPool.getGeneratedIdentifier(ObjectEntities.CABLELINK_TYPE_CODE),
					creatorId,
					StorableObjectVersion.createInitial(),
					codename,
					description,
					name,
					sort.value(),
					manufacturer,
					manufacturerCode,
					imageId);

			assert cableLinkType.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;

			cableLinkType.markAsChanged();

			return cableLinkType;
		} catch (IdentifierGenerationException ige) {
			throw new CreateObjectException("Cannot generate identifier ", ige);
		}
	}

	@Override
	protected synchronized void fromTransferable(final IdlStorableObject transferable) throws ApplicationException {
		final IdlCableLinkType cltt = (IdlCableLinkType) transferable;
		super.fromTransferable(cltt, cltt.codename, cltt.description);
		this.sort = cltt.sort.value();
		this.manufacturer = cltt.manufacturer;
		this.manufacturerCode = cltt.manufacturerCode;
		this.imageId = new Identifier(cltt.imageId);
		this.name = cltt.name;
	}

	/**
	 * @param xmlCableLinkType
	 * @param clonedIdsPool
	 * @param importType
	 * @throws ApplicationException
	 * @see XmlBeansTransferable#fromXmlTransferable(com.syrus.AMFICOM.general.xml.XmlStorableObject, ClonedIdsPool, String)
	 */
	@Shitlet
	public void fromXmlTransferable(final XmlCableLinkType xmlCableLinkType,
			final ClonedIdsPool clonedIdsPool,
			final String importType)
	throws ApplicationException {
		this.name = xmlCableLinkType.getName();
		this.codename = xmlCableLinkType.getCodename();
		this.description = xmlCableLinkType.getDescription();
		this.sort = xmlCableLinkType.getSort().intValue();
		this.manufacturer = xmlCableLinkType.getManufacturer();
		this.manufacturerCode = xmlCableLinkType.getManufacturerCode();
		// TODO read imageId - see SiteNodeType.getImageId(Identifier userId, String codename) for example

		final List<XmlCableThreadType> xmlCableThreadTypeList = xmlCableLinkType.getCableThreadTypes().getCableThreadTypeList();
		for (XmlCableThreadType xmlCableThreadType : xmlCableThreadTypeList) {
			CableThreadType.createInstance(this.creatorId, importType, xmlCableThreadType, clonedIdsPool);
		}
	}

	/**
	 * @param orb
	 * @see com.syrus.AMFICOM.general.TransferableObject#getTransferable(org.omg.CORBA.ORB)
	 */
	@Override
	public IdlCableLinkType getTransferable(final ORB orb) {

		return IdlCableLinkTypeHelper.init(orb,
				super.id.getTransferable(),
				super.created.getTime(),
				super.modified.getTime(),
				super.creatorId.getTransferable(),
				super.modifierId.getTransferable(),
				super.version.longValue(),
				super.codename,
				super.description != null ? super.description : "",
				this.name != null ? this.name : "",
				LinkTypeSort.from_int(this.sort),
				this.manufacturer,
				this.manufacturerCode,
				this.imageId.getTransferable());
	}

	/**
	 * @see XmlBeansTransferable#getXmlTransferable()
	 */
	@Shitlet
	public XmlCableLinkType getXmlTransferable() {
		final XmlCableLinkType xmlCableLinkType = XmlCableLinkType.Factory.newInstance();
		XmlIdentifier uid = xmlCableLinkType.addNewId();
		uid.setStringValue(this.id.toString());
		xmlCableLinkType.setName(this.name);
		xmlCableLinkType.setCodename(this.codename);
		xmlCableLinkType.setDescription(this.description);
		xmlCableLinkType.setSort(XmlLinkTypeSort.Enum.forInt(this.sort));
		xmlCableLinkType.setManufacturer(this.manufacturer);
		xmlCableLinkType.setManufacturerCode(this.manufacturerCode);
		// TODO write image to file
		
		final List<XmlCableThreadType> xmlCableThreadTypeList = new LinkedList<XmlCableThreadType>();
		for (final CableThreadType cableThreadType : this.getCableThreadTypes(true)) {
			xmlCableThreadTypeList.add(cableThreadType.getXmlTransferable());
		}
		final XmlCableThreadTypeSeq xmlCableThreadTypes = xmlCableLinkType.addNewCableThreadTypes();
		xmlCableThreadTypes.setCableThreadTypeArray(xmlCableThreadTypeList.toArray(new XmlCableThreadType[xmlCableThreadTypeList.size()]));
		return xmlCableLinkType;
	}

	@Override
	protected boolean isValid() {
		return super.isValid() && this.name != null && this.manufacturer != null && this.manufacturerCode != null;
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
			final String manufacturer,
			final String manufacturerCode,
			final Identifier imageId) {
			super.setAttributes(created, modified, creatorId, modifierId, version, codename, description);
			this.name = name;
			this.sort = sort;
			this.manufacturer = manufacturer;
			this.manufacturerCode = manufacturerCode;
			this.imageId = imageId;
	}

	@Override
	public Identifier getImageId() {
		return this.imageId;
	}

	public void setImageId(final Identifier imageId) {
		this.imageId = imageId;
		super.markAsChanged();
	}

	
	@Override
	public String getManufacturer() {
		return this.manufacturer;
	}

	@Override
	public void setManufacturer(final String manufacturer) {
		this.manufacturer = manufacturer;
		super.markAsChanged();
	}

	@Override
	public String getManufacturerCode() {
		return this.manufacturerCode;
	}

	@Override
	public void setManufacturerCode(final String manufacturerCode) {
		this.manufacturerCode = manufacturerCode;
		super.markAsChanged();
	}

	@Override
	public LinkTypeSort getSort() {
		return LinkTypeSort.from_int(this.sort);
	}
	
	public void setSort(final LinkTypeSort sort) {
		this.sort = sort.value();
		super.markAsChanged();
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public void setName(final String name) {
		this.name = name;
		super.markAsChanged();
	}

	public Set<CableThreadType> getCableThreadTypes(final boolean breakOnLoadError) {
		final LinkedIdsCondition lic = new LinkedIdsCondition(this.id, ObjectEntities.CABLETHREAD_TYPE_CODE);
		Set<CableThreadType> cableThreadTypes;
		try {
			cableThreadTypes = StorableObjectPool.getStorableObjectsByCondition(lic, true, breakOnLoadError);
			return cableThreadTypes;
		} catch (final ApplicationException ae) {
			Log.errorException(ae);
			return Collections.emptySet();
		}
	}

	@Override
	public Set<Identifiable> getDependencies() {
		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;
		return Collections.emptySet();
	}

}
