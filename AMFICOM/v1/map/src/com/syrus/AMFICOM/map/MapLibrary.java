/**
 * $Id: MapLibrary.java,v 1.1 2005/07/29 12:54:24 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
 */
package com.syrus.AMFICOM.map;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.xmlbeans.XmlObject;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.ClonedIdsPool;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.ErrorMessages;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.Namable;
import com.syrus.AMFICOM.general.XMLBeansTransferable;
import com.syrus.AMFICOM.general.logic.Library;
import com.syrus.AMFICOM.general.logic.LibraryEntry;
import com.syrus.AMFICOM.logic.Item;


/**
 * @version $Revision: 1.1 $, $Date: 2005/07/29 12:54:24 $
 * @author $Author: krupenn $
 * @module map
 */
public class MapLibrary implements Identifiable, Namable, Library, XMLBeansTransferable {

	private Identifier creatorId;

	private Identifier id;
	
	private String name;

	private String codename;

	private MapLibrary parent;

	private transient ArrayList physicalLinkTypes = new ArrayList();

	private transient ArrayList siteNodeTypes = new ArrayList();

	private transient ArrayList libraries = new ArrayList();

	public MapLibrary(
			Identifier creatorId,
			Identifier id,
			String name,
			MapLibrary parent) {
		this.creatorId = creatorId;
		this.id = id;
		this.name = name;
		this.parent = parent;
	}
	
	public void addChild(LibraryEntry libraryEntry) {
		assert libraryEntry != null: ErrorMessages.NON_NULL_EXPECTED;
		if(libraryEntry instanceof PhysicalLinkType)
			this.physicalLinkTypes.add(libraryEntry);
		else if(libraryEntry instanceof SiteNodeType)
			this.siteNodeTypes.add(libraryEntry);
		else if(libraryEntry instanceof MapLibrary)
			this.libraries.add(libraryEntry);		
		else
			throw new UnsupportedOperationException(ErrorMessages.UNSUPPORTED_CHILD_TYPE);
	}

	public void removeChild(LibraryEntry libraryEntry) {
		if(libraryEntry instanceof PhysicalLinkType)
			this.physicalLinkTypes.remove(libraryEntry);
		else if(libraryEntry instanceof SiteNodeType)
			this.siteNodeTypes.remove(libraryEntry);
		else if(libraryEntry instanceof MapLibrary)
			this.libraries.remove(libraryEntry);		
	}

	public void setParent(Library library) {
		assert library != this: ErrorMessages.CIRCULAR_DEPS_PROHIBITED;
		this.parent = (MapLibrary )library;
	}

	public String getName() {
		return this.name;
	}

	public List getChildren() {
		final List children = new ArrayList(this.libraries.size() + this.physicalLinkTypes.size() + this.siteNodeTypes.size());
		children.addAll(this.libraries);
		children.addAll(this.physicalLinkTypes);
		children.addAll(this.siteNodeTypes);
		return children;
	}

	public void setParent(Item parent) {
		assert parent != this: ErrorMessages.CIRCULAR_DEPS_PROHIBITED;
		this.parent = (MapLibrary )parent;
	}

	public Library getParent() {
		return this.parent;
	}

	public Identifier getId() {
		return this.id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public XmlObject getXMLTransferable() {
		com.syrus.amficom.map.xml.MapLibrary xmlMapLibrary = com.syrus.amficom.map.xml.MapLibrary.Factory.newInstance();
		fillXMLTransferable(xmlMapLibrary);
		return xmlMapLibrary;
	}

	public void fillXMLTransferable(final XmlObject xmlObject) {
		com.syrus.amficom.map.xml.MapLibrary xmlMapLibrary = (com.syrus.amficom.map.xml.MapLibrary )xmlObject; 

		com.syrus.amficom.map.xml.PhysicalLinkTypes xmlPhysicallinktypes = xmlMapLibrary.addNewPhysicallinktypes();
		Collection xmlPhysicalLinkTypesArray = new LinkedList();
		for(Iterator iter = this.physicalLinkTypes.iterator(); iter.hasNext();) {
			PhysicalLinkType physicalLinkType = (PhysicalLinkType )iter.next();
			xmlPhysicalLinkTypesArray.add(physicalLinkType.getXMLTransferable());
		}
		xmlPhysicallinktypes.setPhysicallinktypeArray(
				(com.syrus.amficom.map.xml.PhysicalLinkType[] )
				xmlPhysicalLinkTypesArray.toArray(
						new com.syrus.amficom.map.xml.PhysicalLinkType[xmlPhysicalLinkTypesArray.size()]));
		

		com.syrus.amficom.map.xml.SiteNodeTypes xmlSitenodetypes = xmlMapLibrary.addNewSitenodetypes();
		Collection xmlSiteNodeTypesArray = new LinkedList();
		for(Iterator iter = this.siteNodeTypes.iterator(); iter.hasNext();) {
			SiteNodeType siteNodeType = (SiteNodeType )iter.next();
			xmlSiteNodeTypesArray.add(siteNodeType.getXMLTransferable());
		}
		xmlSitenodetypes.setSitenodetypeArray(
				(com.syrus.amficom.map.xml.SiteNodeType[] )
				xmlSiteNodeTypesArray.toArray(
						new com.syrus.amficom.map.xml.SiteNodeType[xmlSiteNodeTypesArray.size()]));
	}

	MapLibrary(
			final Identifier creatorId, 
			final com.syrus.amficom.map.xml.MapLibrary xmlMapLibrary, 
			final ClonedIdsPool clonedIdsPool) 
		throws CreateObjectException, ApplicationException {

//		super(
//				clonedIdsPool.getClonedId(
//						ObjectEntities.SITENODE_CODE, 
//						xmlSiteNodeType.getUid().getStringValue()),
//				new Date(System.currentTimeMillis()),
//				new Date(System.currentTimeMillis()),
//				creatorId,
//				creatorId,
//				0);
	this.creatorId = creatorId;
		this.fromXMLTransferable(xmlMapLibrary, clonedIdsPool);
	}

	public void fromXMLTransferable(final XmlObject xmlObject, final ClonedIdsPool clonedIdsPool) throws ApplicationException {
		com.syrus.amficom.map.xml.MapLibrary xmlMapLibrary = (com.syrus.amficom.map.xml.MapLibrary )xmlObject;

		com.syrus.amficom.map.xml.PhysicalLinkType[] xmlPhysicalLinkTypesArray = 
			xmlMapLibrary.getPhysicallinktypes().getPhysicallinktypeArray();
		this.physicalLinkTypes = new ArrayList(xmlPhysicalLinkTypesArray.length);
		for(int i = 0; i < xmlPhysicalLinkTypesArray.length; i++) {
			com.syrus.amficom.map.xml.PhysicalLinkType xmlPhysicalLinkType = 
				xmlPhysicalLinkTypesArray[i];
			this.addChild(PhysicalLinkType.createInstance(this.creatorId, xmlPhysicalLinkType, clonedIdsPool));
		}

		com.syrus.amficom.map.xml.SiteNodeType[] xmlSiteNodeTypesArray = 
			xmlMapLibrary.getSitenodetypes().getSitenodetypeArray();
		this.siteNodeTypes = new ArrayList(xmlSiteNodeTypesArray.length);
		for(int i = 0; i < xmlSiteNodeTypesArray.length; i++) {
			com.syrus.amficom.map.xml.SiteNodeType xmlSiteNodeType = 
				xmlSiteNodeTypesArray[i];
			this.addChild(SiteNodeType.createInstance(this.creatorId, xmlSiteNodeType, clonedIdsPool));
		}
	}

	public static MapLibrary createInstance(final Identifier creatorId, final XmlObject xmlObject, final ClonedIdsPool clonedIdsPool)
			throws CreateObjectException {

		com.syrus.amficom.map.xml.MapLibrary xmlMapLibrary = (com.syrus.amficom.map.xml.MapLibrary )xmlObject;

		try {
			MapLibrary mapLibrary = new MapLibrary(
					creatorId, 
					xmlMapLibrary, 
					clonedIdsPool);
//			assert mapLibrary.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;
//			mapLibrary.markAsChanged();
			return mapLibrary;
		} catch (Exception e) {
			throw new CreateObjectException("MapLibrary.createInstance |  ", e);
		}
	}

	public Identifier getCreatorId() {
		// TODO Auto-generated method stub
		return null;
	}

	public Object getCreated() {
		// TODO Auto-generated method stub
		return null;
	}

	public Object getModified() {
		// TODO Auto-generated method stub
		return null;
	}

}
