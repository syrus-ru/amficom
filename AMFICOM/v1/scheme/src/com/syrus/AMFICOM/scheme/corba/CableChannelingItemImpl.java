/*
 * $Id: CableChannelingItemImpl.java,v 1.12 2005/03/04 19:25:01 bass Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme.corba;

import com.syrus.AMFICOM.general.*;
import com.syrus.AMFICOM.general.corba.*;
import com.syrus.AMFICOM.general.corba.StorableObject;
import com.syrus.AMFICOM.map.*;
import com.syrus.AMFICOM.map.corba.*;
import com.syrus.util.logging.ErrorHandler;

/**
 * @author $Author: bass $
 * @version $Revision: 1.12 $, $Date: 2005/03/04 19:25:01 $
 * @module scheme_v1
 */
final class CableChannelingItemImpl extends CableChannelingItem implements Cloneable {
	private static final ErrorHandler ERROR_HANDLER = ErrorHandler.getInstance();

	private static final long serialVersionUID = 3544387011336024880L;

	/**
	 * @todo Check whether constructor is invoked during deserialization.
	 */
	CableChannelingItemImpl() {
	}

	/**
	 * @see CableChannelingItem#cloneInstance()
	 */
	public CableChannelingItem cloneInstance() {
		try {
			return (CableChannelingItem) this.clone();
		} catch (CloneNotSupportedException cnse) {
			ERROR_HANDLER.error(cnse);
			return null;
		}
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
	 * @see StorableObject#getCreated()
	 */
	public long getCreated() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see StorableObject#getCreatorId()
	 */
	public Identifier getCreatorId() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see StorableObject#getDependencies()
	 */
	public Identifier[] getDependencies() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see StorableObject#getHeaderTransferable()
	 */
	public StorableObject_Transferable getHeaderTransferable() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see Identifiable#getId()
	 */
	public Identifier getId() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see StorableObject#getModified()
	 */
	public long getModified() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see StorableObject#getModifierId()
	 */
	public Identifier getModifierId() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see StorableObject#getVersion()
	 */
	public long getVersion() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see IStorableObject#isChanged()
	 */
	public boolean isChanged() {
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
	 * @param storableObjectFactory
	 * @param changed
	 * @see IStorableObject#setChanged(StorableObjectFactory, boolean)
	 */
	public void setChanged(final StorableObjectFactory storableObjectFactory, final boolean changed) {
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

	protected Object clone() throws CloneNotSupportedException {
		final CableChannelingItemImpl cableChannelingItem = (CableChannelingItemImpl) super.clone();
		/**
		 * @todo Update the newly created object.
		 */
		return cableChannelingItem;
	}
}
