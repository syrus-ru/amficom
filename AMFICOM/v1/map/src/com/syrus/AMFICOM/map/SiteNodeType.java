/*
 * $Id: SiteNodeType.java,v 1.3 2004/11/28 14:34:48 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.map;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.syrus.AMFICOM.configuration.Characteristic;
import com.syrus.AMFICOM.configuration.Characterized;
import com.syrus.AMFICOM.configuration.ConfigurationStorableObjectPool;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.StorableObjectType;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.map.corba.SiteNodeType_Transferable;

/**
 * @version $Revision: 1.3 $, $Date: 2004/11/28 14:34:48 $
 * @author $Author: bob $
 * @module map_v1
 */
public class SiteNodeType extends StorableObjectType implements Characterized {

	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long	serialVersionUID	= 3690481316080464696L;

	private List					characteristics;

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

			this.characteristics.addAll(ConfigurationStorableObjectPool.getStorableObjects(characteristicIds, true));
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
		super.description = description;
		this.name = name;
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

	public List getDependencies() {
		List dependencies = new LinkedList();
		dependencies.add(this.imageId);
		dependencies.addAll(this.characteristics);
		return dependencies;
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

	public Object getTransferable() {
		int i = 0;
		Identifier_Transferable[] charIds = new Identifier_Transferable[this.characteristics.size()];
		for (Iterator iterator = this.characteristics.iterator(); iterator.hasNext();)
			charIds[i++] = (Identifier_Transferable) ((Characteristic) iterator.next()).getId().getTransferable();
		return new SiteNodeType_Transferable(super.getHeaderTransferable(), this.codename, this.name, this.description,
												(Identifier_Transferable) this.imageId.getTransferable(),
												this.topological, charIds);
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
		super.description = description;
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
