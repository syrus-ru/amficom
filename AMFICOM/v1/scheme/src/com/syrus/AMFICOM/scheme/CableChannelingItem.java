/*
 * $Id: CableChannelingItem.java,v 1.1 2005/03/16 12:51:34 bass Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme;

import com.syrus.AMFICOM.general.*;
import com.syrus.AMFICOM.general.corba.*;
import com.syrus.AMFICOM.map.*;
import com.syrus.AMFICOM.map.corba.*;
import java.util.*;

/**
 * @author $Author: bass $
 * @version $Revision: 1.1 $, $Date: 2005/03/16 12:51:34 $
 * @module scheme_v1
 */
public final class CableChannelingItem extends AbstractCloneableStorableObject
		implements Describable {
	protected Identifier endSiteNodeId = null;

	protected Identifier physicalLinkId = null;

	protected Identifier startSiteNodeId = null;

	protected String thisDescription = null;

	protected double thisEndSpare = 0;

	protected String thisName = null;

	protected int thisPlaceY = 0;

	protected int thisRowX = 0;

	protected int thisSequentialNumber = 0;

	protected double thisStartSpare = 0;

	/**
	 * @param id
	 */
	protected CableChannelingItem(Identifier id) {
		super(id);
	}

	/**
	 * @param id
	 * @param created
	 * @param modified
	 * @param creatorId
	 * @param modifierId
	 * @param version
	 */
	protected CableChannelingItem(Identifier id, Date created,
			Date modified, Identifier creatorId,
			Identifier modifierId, long version) {
		super(id, created, modified, creatorId, modifierId, version);
	}

	/**
	 * @see Describable#getDescription()
	 */
	public String getDescription() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see Describable#setDescription(String)
	 */
	public void setDescription(String description) {
		throw new UnsupportedOperationException();
	}

	public SiteNode getEndSiteNode() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param endSiteNode
	 */
	public void setEndSiteNode(final SiteNode endSiteNode) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see CableChannelingItem#endSpare()
	 */
	public double endSpare() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see CableChannelingItem#endSpare(double)
	 */
	public void endSpare(double endSpare) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see com.syrus.AMFICOM.general.StorableObject#getDependencies()
	 */
	public List getDependencies() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see Namable#getName()
	 */
	public String getName() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see Namable#setName(String)
	 */
	public void setName(String name) {
		throw new UnsupportedOperationException();
	}

	public PhysicalLink getPhysicalLink() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param physicalLink
	 */
	public void setPhysicalLink(final PhysicalLink physicalLink) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see CableChannelingItem#placeY()
	 */
	public int placeY() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see CableChannelingItem#placeY(int)
	 */
	public void placeY(int placeY) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see CableChannelingItem#rowX()
	 */
	public int rowX() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see CableChannelingItem#rowX(int)
	 */
	public void rowX(int rowX) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see CableChannelingItem#sequentialNumber()
	 */
	public int sequentialNumber() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see CableChannelingItem#sequentialNumber(int)
	 */
	public void sequentialNumber(int sequentialNumber) {
		throw new UnsupportedOperationException();
	}

	public SiteNode getStartSiteNode() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param startSiteNode
	 */
	public void setStartSiteNode(final SiteNode startSiteNode) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see CableChannelingItem#startSpare()
	 */
	public double startSpare() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see CableChannelingItem#startSpare(double)
	 */
	public void startSpare(double startSpare) {
		throw new UnsupportedOperationException();
	}

	public Object clone() {
		final CableChannelingItem cableChannelingItem = (CableChannelingItem) super
				.clone();
		/**
		 * @todo Update the newly created object.
		 */
		return cableChannelingItem;
	}

	/**
	 * @see TransferableObject#getTransferable()
	 */
	public Object getTransferable() {
		throw new UnsupportedOperationException();
	}

	public static CableChannelingItem createInstance(
			final Identifier creatorId)
			throws CreateObjectException {
		assert creatorId != null;
		try {
			final Date created = new Date();
			final CableChannelingItem cableChannelingItem = new CableChannelingItem(
					IdentifierPool
							.getGeneratedIdentifier(ObjectEntities.CABLE_CHANNELING_ITEM_ENTITY_CODE),
					created, created, creatorId, creatorId,
					0L);
			cableChannelingItem.changed = true;
			return cableChannelingItem;
		} catch (final IllegalObjectEntityException ioee) {
			throw new CreateObjectException(
					"CableChanelingItem.createInstance | cannot generate identifier ", ioee); //$NON-NLS-1$
		}
	}

	/**
	 * @deprecated Use {@link #createInstance(Identifier)} instead.
	 */
	public static CableChannelingItem createInstance() {
		throw new UnsupportedOperationException();
	}
}
