/*
 * $Id: CableChannelingItem.java,v 1.4 2005/03/15 17:47:57 bass Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme.corba;

import com.syrus.AMFICOM.general.*;
import com.syrus.AMFICOM.general.corba.*;
import com.syrus.AMFICOM.map.*;
import com.syrus.AMFICOM.map.corba.*;
import java.util.*;

/**
 * @author $Author: bass $
 * @version $Revision: 1.4 $, $Date: 2005/03/15 17:47:57 $
 * @module scheme_v1
 */
public final class CableChannelingItem extends AbstractCloneableStorableObject implements
		Namable, Describable, ComSyrusAmficomMapCableChannelingItem {

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
	 * @see Describable#description()
	 */
	public String description() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see Describable#description(String)
	 */
	public void description(String description) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see com.syrus.AMFICOM.map.ComSyrusAmficomMapCableChannelingItem#endSiteNode()
	 */
	public SiteNode_Transferable endSiteNode() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param endSiteNode
	 * @see com.syrus.AMFICOM.map.ComSyrusAmficomMapCableChannelingItem#endSiteNode(SiteNode_Transferable)
	 */
	public void endSiteNode(final SiteNode_Transferable endSiteNode) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see com.syrus.AMFICOM.map.ComSyrusAmficomMapCableChannelingItem#endSiteNodeImpl()
	 */
	public SiteNode endSiteNodeImpl() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param endSiteNode
	 * @see com.syrus.AMFICOM.map.ComSyrusAmficomMapCableChannelingItem#endSiteNodeImpl(SiteNode)
	 */
	public void endSiteNodeImpl(final SiteNode endSiteNode) {
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
	 * @see Namable#name()
	 */
	public String name() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see Namable#name(String)
	 */
	public void name(String name) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see CableChannelingItem#physicalLink()
	 */
	public PhysicalLink_Transferable physicalLink() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see CableChannelingItem#physicalLink(PhysicalLink_Transferable)
	 */
	public void physicalLink(final PhysicalLink_Transferable physicalLink) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see ComSyrusAmficomMapCableChannelingItem#physicalLinkImpl()
	 */
	public PhysicalLink physicalLinkImpl() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param physicalLink
	 * @see ComSyrusAmficomMapCableChannelingItem#physicalLinkImpl(PhysicalLink)
	 */
	public void physicalLinkImpl(final PhysicalLink physicalLink) {
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

	/**
	 * @see com.syrus.AMFICOM.map.ComSyrusAmficomMapCableChannelingItem#startSiteNode()
	 */
	public SiteNode_Transferable startSiteNode() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param startSiteNode
	 * @see com.syrus.AMFICOM.map.ComSyrusAmficomMapCableChannelingItem#startSiteNode(SiteNode_Transferable)
	 */
	public void startSiteNode(final SiteNode_Transferable startSiteNode) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see com.syrus.AMFICOM.map.ComSyrusAmficomMapCableChannelingItem#startSiteNodeImpl()
	 */
	public SiteNode startSiteNodeImpl() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param startSiteNode
	 * @see com.syrus.AMFICOM.map.ComSyrusAmficomMapCableChannelingItem#startSiteNodeImpl(SiteNode)
	 */
	public void startSiteNodeImpl(final SiteNode startSiteNode) {
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
