/*-
 * $Id: LinkType.java,v 1.77 2005/09/12 00:10:48 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.configuration;

import static com.syrus.AMFICOM.general.ErrorMessages.OBJECT_BADLY_INITIALIZED;
import static com.syrus.AMFICOM.general.Identifier.VOID_IDENTIFIER;
import static com.syrus.AMFICOM.general.ObjectEntities.LINK_TYPE_CODE;
import static java.util.logging.Level.SEVERE;

import java.util.Collections;
import java.util.Date;
import java.util.Set;

import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.configuration.corba.IdlLinkType;
import com.syrus.AMFICOM.configuration.corba.IdlLinkTypeHelper;
import com.syrus.AMFICOM.configuration.corba.IdlAbstractLinkTypePackage.LinkTypeSort;
import com.syrus.AMFICOM.configuration.xml.XmlLinkType;
import com.syrus.AMFICOM.configuration.xml.XmlLinkTypeSort;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.general.XmlBeansTransferable;
import com.syrus.AMFICOM.general.corba.IdlStorableObject;
import com.syrus.util.Log;
import com.syrus.util.Shitlet;

/**
 * @version $Revision: 1.77 $, $Date: 2005/09/12 00:10:48 $
 * @author $Author: bass $
 * @module config
 */

public final class LinkType extends AbstractLinkType implements XmlBeansTransferable<XmlLinkType> {

	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long	serialVersionUID	= 3257007652839372857L;

	private String name;
	private int sort;
	private String manufacturer;
	private String manufacturerCode;
	private Identifier imageId;

	public LinkType(final IdlLinkType ltt) throws CreateObjectException {
		try {
			this.fromTransferable(ltt);
		} catch (ApplicationException ae) {
			throw new CreateObjectException(ae);
		}
	}

	LinkType(final Identifier id,
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
	 * @param created
	 * @param creatorId
	 */
	private LinkType(final Identifier id,
			final Date created,
			final Identifier creatorId) {
		super(id,
				created,
				created,
				creatorId,
				creatorId,
				StorableObjectVersion.createInitial(),
				"",
				"");
	}

	/**
	 * @param creatorId
	 * @param importType
	 * @param xmlLinkType
	 * @throws CreateObjectException
	 */
	public static LinkType createInstance(
			final Identifier creatorId,
			final String importType,
			final XmlLinkType xmlLinkType)
	throws CreateObjectException {
		try {
			final Identifier id = Identifier.fromXmlTransferable(xmlLinkType.getId(), importType, LINK_TYPE_CODE);
			LinkType linkType = StorableObjectPool.getStorableObject(id, true);
			if (linkType == null) {
				linkType = new LinkType(id, new Date(), creatorId);
			}
			linkType.fromXmlTransferable(xmlLinkType, importType);
			assert linkType.isValid() : OBJECT_BADLY_INITIALIZED;
			linkType.markAsChanged();
			return linkType;
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
	public static LinkType createInstance(final Identifier creatorId,
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
				|| manufacturerCode == null
				|| imageId == null)
			throw new IllegalArgumentException("Argument is 'null'");

		try {
			final LinkType linkType = new LinkType(IdentifierPool.getGeneratedIdentifier(LINK_TYPE_CODE),
						creatorId,
						StorableObjectVersion.createInitial(),
						codename,
						description,
						name,
						sort.value(),
						manufacturer,
						manufacturerCode,
						imageId);

			assert linkType.isValid() : OBJECT_BADLY_INITIALIZED;

			linkType.markAsChanged();

			return linkType;
		} catch (IdentifierGenerationException ige) {
			throw new CreateObjectException("Cannot generate identifier ", ige);
		}
	}

	@Override
	protected synchronized void fromTransferable(final IdlStorableObject transferable) throws ApplicationException {
		final IdlLinkType ltt = (IdlLinkType) transferable;
		super.fromTransferable(ltt, ltt.codename, ltt.description);

		this.sort = ltt.sort.value();
		this.manufacturer = ltt.manufacturer;
		this.manufacturerCode = ltt.manufacturerCode;
		this.imageId = new Identifier(ltt.imageId);
		this.name = ltt.name;
	}

	/**
	 * @param linkType
	 * @param importType
	 * @throws ApplicationException
	 * @see XmlBeansTransferable#fromXmlTransferable(com.syrus.AMFICOM.general.xml.XmlStorableObject, String)
	 */
	@Shitlet
	public void fromXmlTransferable(final XmlLinkType linkType,
			final String importType)
	throws ApplicationException {
		this.name = linkType.getName();
		this.codename = linkType.getCodename();
		this.description = linkType.isSetDescription()
				? linkType.getDescription()
				: "";
		this.sort = linkType.getSort().intValue();
		this.manufacturer = linkType.isSetManufacturer()
				? linkType.getManufacturer()
				: "";
		this.manufacturerCode = linkType.isSetManufacturerCode() 
				? linkType.getManufacturerCode()
				: "";
		// TODO read imageId - see SiteNodeType.getImageId(Identifier userId, String codename) for example
		this.imageId = VOID_IDENTIFIER;
	}

	/**
	 * @param orb
	 * @see com.syrus.AMFICOM.general.TransferableObject#getTransferable(org.omg.CORBA.ORB)
	 */
	@Override
	public IdlLinkType getTransferable(final ORB orb) {

		return IdlLinkTypeHelper.init(orb,
				super.id.getTransferable(),
				super.created.getTime(),
				super.modified.getTime(),
				super.creatorId.getTransferable(),
				super.modifierId.getTransferable(),
				super.version.longValue(),
				super.codename,
				super.description != null ? super.description : "",
				this.name != null ? this.name : "",
				LinkTypeSort.from_int(this.sort), this.manufacturer, this.manufacturerCode,
				this.imageId.getTransferable());
	}

	/**
	 * @see XmlBeansTransferable#getXmlTransferable(String)
	 */
	@Shitlet
	public XmlLinkType getXmlTransferable(final String importType) {
		final XmlLinkType xmlLinkType = XmlLinkType.Factory.newInstance();
		xmlLinkType.setId(this.id.getXmlTransferable(importType));
		xmlLinkType.setName(this.name);
		xmlLinkType.setCodename(this.codename);
		xmlLinkType.setDescription(this.description);
		xmlLinkType.setSort(XmlLinkTypeSort.Enum.forInt(this.sort));
		xmlLinkType.setManufacturer(this.manufacturer);
		xmlLinkType.setManufacturerCode(this.manufacturerCode);
		// TODO write image to file
		return xmlLinkType;
	}

	@Override
	protected boolean isValid() {
		return super.isValid()
				&& this.name != null
				&& this.manufacturer != null
				&& this.manufacturerCode != null
				&& this.imageId != null;
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

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public void setName(final String name) {
		this.name= name;
		super.markAsChanged();
	}

	@Override
	public Set<Identifiable> getDependencies() {
		assert this.isValid() : OBJECT_BADLY_INITIALIZED;
		return Collections.emptySet();
	}

	/**
	 * @param imageId The imageId to set.
	 */
	public void setImageId(final Identifier imageId) {
		this.imageId = imageId;
		super.markAsChanged();
	}
	/**
	 * @param sort The sort to set.
	 */
	public void setSort(final LinkTypeSort sort) {
		this.sort = sort.value();
		super.markAsChanged();
	}
}
