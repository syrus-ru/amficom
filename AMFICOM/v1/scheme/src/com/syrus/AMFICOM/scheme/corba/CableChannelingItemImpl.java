/*
 * $Id: CableChannelingItemImpl.java,v 1.8 2004/12/20 10:10:09 bass Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme.corba;

import com.syrus.AMFICOM.general.corba.*;
import com.syrus.AMFICOM.map.*;
import com.syrus.AMFICOM.map.corba.*;
import com.syrus.util.logging.ErrorHandler;
import java.util.*;

/**
 * @author $Author: bass $
 * @version $Revision: 1.8 $, $Date: 2004/12/20 10:10:09 $
 * @module schemecommon_v1
 */
final class CableChannelingItemImpl extends CableChannelingItem implements Cloneable {
	private static final ErrorHandler ERROR_HANDLER = ErrorHandler.getInstance();

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
	 * @see StorableObject#created()
	 */
	public long created() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see java.util.JavaUtilIStorableObject#createdImpl()
	 */
	public Date createdImpl() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see StorableObject#creatorId()
	 */
	public Identifier creatorId() {
		throw new UnsupportedOperationException();
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
	 * @see JavaUtilIStorableObject#getCreated()
	 */
	public Date getCreated() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see JavaUtilIStorableObject#getDependencies()
	 */
	public List getDependencies() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see IStorableObject#getHeaderTransferable()
	 */
	public StorableObject_Transferable getHeaderTransferable() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see JavaUtilIStorableObject#getModified()
	 */
	public Date getModified() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see Identifiable#id()
	 */
	public Identifier id() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see IStorableObject#isChanged()
	 */
	public boolean isChanged() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see StorableObject#modified()
	 */
	public long modified() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see java.util.JavaUtilIStorableObject#modifiedImpl()
	 */
	public Date modifiedImpl() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see StorableObject#modifierId()
	 */
	public Identifier modifierId() {
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

	/**
	 * @see StorableObject#version()
	 */
	public long version() {
		throw new UnsupportedOperationException();
	}

	protected Object clone() throws CloneNotSupportedException {
		throw new UnsupportedOperationException();
	}
}
