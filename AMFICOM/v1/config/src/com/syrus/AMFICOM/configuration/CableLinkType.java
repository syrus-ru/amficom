/*-
 * $Id: CableLinkType.java,v 1.75 2005/09/20 10:42:01 bass Exp $
 *
 * Copyright � 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.configuration;

import static com.syrus.AMFICOM.general.ErrorMessages.OBJECT_BADLY_INITIALIZED;
import static com.syrus.AMFICOM.general.Identifier.VOID_IDENTIFIER;
import static com.syrus.AMFICOM.general.Identifier.XmlConversionMode.MODE_RETURN_VOID_IF_ABSENT;
import static com.syrus.AMFICOM.general.ObjectEntities.CABLELINK_TYPE_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.CABLETHREAD_TYPE_CODE;
import static java.util.logging.Level.SEVERE;

import java.util.Collections;
import java.util.Date;
import java.util.Set;

import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.configuration.corba.IdlCableLinkType;
import com.syrus.AMFICOM.configuration.corba.IdlCableLinkTypeHelper;
import com.syrus.AMFICOM.configuration.corba.IdlAbstractLinkTypePackage.LinkTypeSort;
import com.syrus.AMFICOM.configuration.xml.XmlCableLinkType;
import com.syrus.AMFICOM.configuration.xml.XmlCableThreadType;
import com.syrus.AMFICOM.configuration.xml.XmlCableThreadTypeSeq;
import com.syrus.AMFICOM.configuration.xml.XmlLinkTypeSort;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.LocalXmlIdentifierPool;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.general.XmlBeansTransferable;
import com.syrus.AMFICOM.general.corba.IdlStorableObject;
import com.syrus.AMFICOM.general.xml.XmlIdentifier;
import com.syrus.util.Log;
import com.syrus.util.Shitlet;

/**
 * @version $Revision: 1.75 $, $Date: 2005/09/20 10:42:01 $
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

	/**
	 * Minimalistic constructor used when importing from XML.
	 *
	 * @param id
	 * @param importType
	 * @param created
	 * @param creatorId
	 * @throws IdentifierGenerationException
	 */
	private CableLinkType(final XmlIdentifier id,
			final String importType,
			final Date created,
			final Identifier creatorId)
	throws IdentifierGenerationException {
		super(Identifier.fromXmlTransferable(id, importType, CABLELINK_TYPE_CODE),
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
	 * @param xmlCableLinkType
	 * @param importType
	 * @throws CreateObjectException
	 */
	public static CableLinkType createInstance(
			final Identifier creatorId,
			final XmlCableLinkType xmlCableLinkType,
			final String importType)
	throws CreateObjectException {
		try {
			final XmlIdentifier xmlId = xmlCableLinkType.getId();
			final Date created = new Date();
			final Identifier id = Identifier.fromXmlTransferable(xmlId, importType, MODE_RETURN_VOID_IF_ABSENT);
			CableLinkType cableLinkType;
			if (id.isVoid()) {
				cableLinkType = new CableLinkType(xmlId,
						importType,
						created,
						creatorId);
			} else {
				cableLinkType = StorableObjectPool.getStorableObject(id, true);
				if (cableLinkType == null) {
					LocalXmlIdentifierPool.remove(xmlId, importType);
					cableLinkType = new CableLinkType(xmlId,
							importType,
							created,
							creatorId);
				}
			}
			cableLinkType.fromXmlTransferable(xmlCableLinkType, importType);
			assert cableLinkType.isValid() : OBJECT_BADLY_INITIALIZED;
			cableLinkType.markAsChanged();
			return cableLinkType;
		} catch (final CreateObjectException coe) {
			throw coe;
		} catch (final ApplicationException ae) {
			Log.debugException(ae, SEVERE);
			throw new CreateObjectException(ae);
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
			final CableLinkType cableLinkType = new CableLinkType(IdentifierPool.getGeneratedIdentifier(CABLELINK_TYPE_CODE),
					creatorId,
					StorableObjectVersion.createInitial(),
					codename,
					description,
					name,
					sort.value(),
					manufacturer,
					manufacturerCode,
					imageId);

			assert cableLinkType.isValid() : OBJECT_BADLY_INITIALIZED;

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
	 * @param cableLinkType
	 * @param importType
	 * @throws ApplicationException
	 * @see XmlBeansTransferable#fromXmlTransferable(com.syrus.AMFICOM.general.xml.XmlStorableObject, String)
	 */
	@Shitlet
	public void fromXmlTransferable(final XmlCableLinkType cableLinkType,
			final String importType)
	throws ApplicationException {
		this.name = cableLinkType.getName();
		this.codename = cableLinkType.getCodename();
		this.description = cableLinkType.isSetDescription()
				? cableLinkType.getDescription()
				: "";
		this.sort = cableLinkType.getSort().intValue();
		this.manufacturer = cableLinkType.isSetManufacturer()
				? cableLinkType.getManufacturer()
				: "";
		this.manufacturerCode = cableLinkType.isSetManufacturerCode()
				? cableLinkType.getManufacturerCode()
				: "";
		// TODO read imageId - see SiteNodeType.getImageId(Identifier userId, String codename) for example
		this.imageId = VOID_IDENTIFIER;
		if (cableLinkType.isSetCableThreadTypes()) {
			for (final XmlCableThreadType cableThreadType : cableLinkType.getCableThreadTypes().getCableThreadTypeArray()) {
				CableThreadType.createInstance(this.creatorId, cableThreadType, importType);
			}
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
	 * @param cableLinkType
	 * @param importType
	 * @throws ApplicationException
	 * @see XmlBeansTransferable#getXmlTransferable(com.syrus.AMFICOM.general.xml.XmlStorableObject, String)
	 */
	@Shitlet
	public void getXmlTransferable(
			final XmlCableLinkType cableLinkType,
			final String importType)
	throws ApplicationException {
		super.id.getXmlTransferable(cableLinkType.addNewId(), importType);
		cableLinkType.setName(this.name);
		cableLinkType.setCodename(this.codename);
		cableLinkType.setDescription(this.description);
		cableLinkType.setSort(XmlLinkTypeSort.Enum.forInt(this.sort));
		cableLinkType.setManufacturer(this.manufacturer);
		cableLinkType.setManufacturerCode(this.manufacturerCode);
		// TODO write image to file

		if (cableLinkType.isSetCableThreadTypes()) {
			cableLinkType.unsetCableThreadTypes();
		}
		final Set<CableThreadType> cableThreadTypes = this.getCableThreadTypes(true);
		if (!cableThreadTypes.isEmpty()) {
			final XmlCableThreadTypeSeq cableThreadTypeSeq = cableLinkType.addNewCableThreadTypes();
			for (final CableThreadType cableThreadType : cableThreadTypes) {
				cableThreadType.getXmlTransferable(cableThreadTypeSeq.addNewCableThreadType(), importType);
			}
		}
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
		final LinkedIdsCondition lic = new LinkedIdsCondition(this.id, CABLETHREAD_TYPE_CODE);
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
		assert this.isValid() : OBJECT_BADLY_INITIALIZED;
		return Collections.emptySet();
	}

}
