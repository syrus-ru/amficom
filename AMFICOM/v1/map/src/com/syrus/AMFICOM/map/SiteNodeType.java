/*
 * $Id: SiteNodeType.java,v 1.1 2004/11/24 12:30:24 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.map;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import com.syrus.AMFICOM.configuration.ConfigurationStorableObjectPool;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.StorableObjectType;
import com.syrus.AMFICOM.map.corba.SiteNodeType_Transferable;

/**
 * @version $Revision: 1.1 $, $Date: 2004/11/24 12:30:24 $
 * @author $Author: bob $
 * @module map_v1
 */
public class SiteNodeType extends StorableObjectType {

	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long	serialVersionUID	= 4050767082851217457L;
	private List					characteristics;
	private String					description;

	private Identifier				imageId;

	private String					name;

	private StorableObjectDatabase	siteNodeTypeDatabase;
	private boolean					topological;

	public SiteNodeType(Identifier id) throws RetrieveObjectException, ObjectNotFoundException {
		super(id);

		this.siteNodeTypeDatabase = MapDatabaseContext.siteNodeTypeDatabase;
		try {
			this.siteNodeTypeDatabase.retrieve(this);
		} catch (IllegalDataException e) {
			throw new RetrieveObjectException(e.getMessage(), e);
		}
	}

	public SiteNodeType(SiteNodeType_Transferable sntt) throws CreateObjectException {
		super(sntt.header, sntt.codename, sntt.description);
		this.name = sntt.name;
		this.imageId = new Identifier(sntt.imageId);
		this.topological = sntt.topological;

		try {
			this.characteristics = new ArrayList(sntt.characteristicIds.length);
			ArrayList characteristicIds = new ArrayList(sntt.characteristicIds.length);
			for (int i = 0; i < sntt.characteristicIds.length; i++)
				characteristicIds.add(new Identifier(sntt.characteristicIds[i]));

			this.characteristics = ConfigurationStorableObjectPool.getStorableObjects(characteristicIds, true);
		} catch (ApplicationException ae) {
			throw new CreateObjectException(ae);
		}
	}

	protected SiteNodeType(final Identifier id,
						   final Identifier creatorId,
						   final String codename,
						   final String name,
						   final String description,
						   final Identifier imageId,
						   final boolean topological) {
		super(id);
		long time = System.currentTimeMillis();
		super.created = new Date(time);
		super.modified = new Date(time);
		super.creatorId = creatorId;
		super.modifierId = creatorId;
		super.codename = codename;
		this.name = name;
		this.description = description;
		this.imageId = imageId;
		this.topological = topological;

		this.characteristics = new LinkedList();

		super.currentVersion = super.getNextVersion();

		this.siteNodeTypeDatabase = MapDatabaseContext.siteNodeTypeDatabase;
	}

	public static SiteNodeType getInstance(SiteNodeType_Transferable sntt) throws CreateObjectException {
		SiteNodeType siteNodeType = new SiteNodeType(sntt);

		siteNodeType.siteNodeTypeDatabase = MapDatabaseContext.siteNodeTypeDatabase;
		try {
			if (siteNodeType.siteNodeTypeDatabase != null)
				siteNodeType.siteNodeTypeDatabase.insert(siteNodeType);
		} catch (IllegalDataException e) {
			throw new CreateObjectException(e.getMessage(), e);
		}

		return siteNodeType;
	}

	public List getCharacteristics() {
		return this.characteristics;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.syrus.AMFICOM.general.StorableObject#getDependencies()
	 */
	public List getDependencies() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getDescription() {
		return this.description;
	}

	public Identifier getImageId() {
		return this.imageId;
	}

	public String getName() {
		return this.name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.syrus.AMFICOM.general.TransferableObject#getTransferable()
	 */
	public Object getTransferable() {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isTopological() {
		return this.topological;
	}

	public void setCharacteristics(final List characteristics) {
		this.characteristics.clear();
		if (characteristics != null)
			this.characteristics.addAll(characteristics);
		super.currentVersion = super.getNextVersion();
	}

	public void setDescription(final String description) {
		this.description = description;
		super.currentVersion = super.getNextVersion();
	}

	public void setImageId(final Identifier imageId) {
		this.imageId = imageId;
		super.currentVersion = super.getNextVersion();
	}

	public void setName(final String name) {
		this.name = name;
		super.currentVersion = super.getNextVersion();
	}

	public void setTopological(final boolean topological) {
		this.topological = topological;
		super.currentVersion = super.getNextVersion();
	}
}
