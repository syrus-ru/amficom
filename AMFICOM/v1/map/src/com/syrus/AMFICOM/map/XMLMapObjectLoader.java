/*
 * $Id: XMLMapObjectLoader.java,v 1.6 2005/02/25 06:57:15 bob Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.map;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.SessionContext;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectXML;
import com.syrus.AMFICOM.general.StorableObjectXMLDriver;

/**
 * @version $Revision: 1.6 $, $Date: 2005/02/25 06:57:15 $
 * @author $Author: bob $
 * @module configuration_v1
 */

public final class XMLMapObjectLoader implements MapObjectLoader {

	private StorableObjectXML mapXML;

	public XMLMapObjectLoader(final File path) {
		StorableObjectXMLDriver driver = new StorableObjectXMLDriver(path, "map");
		this.mapXML = new StorableObjectXML(driver);
	}

	public void delete(Identifier id) throws IllegalDataException {
		this.mapXML.delete(id);
		this.mapXML.flush();
	}

	public void delete(Collection ids) throws IllegalDataException {
		for (Iterator it = ids.iterator(); it.hasNext();) {
			Identifier id = (Identifier) it.next();
			this.mapXML.delete(id);
		}
		this.mapXML.flush();
	}

	public Set refresh(Set storableObjects) throws ApplicationException {
		// TODO Auto-generated method stub
		return null;
	}

	private StorableObject loadStorableObject(Identifier id) throws ApplicationException {
		return this.mapXML.retrieve(id);
	}

	private Collection loadStorableObjectButIds(StorableObjectCondition condition, Collection ids) throws ApplicationException {
		return this.mapXML.retrieveByCondition(ids, condition);
	}

	private void saveStorableObject(StorableObject storableObject, boolean force) throws ApplicationException {
		this.mapXML.updateObject(storableObject, force, SessionContext.getAccessIdentity().getUserId());
	}

	private void saveStorableObjects(Collection storableObjects, boolean force) throws ApplicationException {
		for (Iterator it = storableObjects.iterator(); it.hasNext();) {
			StorableObject storableObject = (StorableObject) it.next();
			this.saveStorableObject(storableObject, force);
		}
		this.mapXML.flush();
	}

	public Collector loadCollector(Identifier id) throws ApplicationException
	{
		return (Collector) this.loadStorableObject(id);
	}

	public Collection loadCollectors(Collection ids) throws ApplicationException
	{
		Collection list = new ArrayList(ids.size());
		for (Iterator it = ids.iterator(); it.hasNext();) {
			Identifier id = (Identifier) it.next();
			list.add(this.loadStorableObject(id));
		}
		return list;
	}

	public Map loadMap(Identifier id) throws ApplicationException
	{
		return (Map) this.loadStorableObject(id);
	}

	public Collection loadMaps(Collection ids) throws ApplicationException
	{
		Collection list = new ArrayList(ids.size());
		for (Iterator it = ids.iterator(); it.hasNext();) {
			Identifier id = (Identifier) it.next();
			list.add(this.loadStorableObject(id));
		}
		return list;
	}

	public Mark loadMark(Identifier id) throws ApplicationException
	{
		return (Mark) this.loadStorableObject(id);
	}

	public Collection loadMarks(Collection ids) throws ApplicationException
	{
		Collection list = new ArrayList(ids.size());
		for (Iterator it = ids.iterator(); it.hasNext();) {
			Identifier id = (Identifier) it.next();
			list.add(this.loadStorableObject(id));
		}
		return list;
	}

	public NodeLink loadNodeLink(Identifier id) throws ApplicationException
	{
		return (NodeLink) this.loadStorableObject(id);
	}

	public Collection loadNodeLinks(Collection ids) throws ApplicationException
	{
		Collection list = new ArrayList(ids.size());
		for (Iterator it = ids.iterator(); it.hasNext();) {
			Identifier id = (Identifier) it.next();
			list.add(this.loadStorableObject(id));
		}
		return list;
	}

	public PhysicalLink loadPhysicalLink(Identifier id) throws ApplicationException
	{
		return (PhysicalLink) this.loadStorableObject(id);
	}

	public Collection loadPhysicalLinks(Collection ids) throws ApplicationException
	{
		Collection list = new ArrayList(ids.size());
		for (Iterator it = ids.iterator(); it.hasNext();) {
			Identifier id = (Identifier) it.next();
			list.add(this.loadStorableObject(id));
		}
		return list;
	}

	public PhysicalLinkType loadPhysicalLinkType(Identifier id) throws ApplicationException
	{
		return (PhysicalLinkType) this.loadStorableObject(id);
	}

	public Collection loadPhysicalLinkTypes(Collection ids) throws ApplicationException
	{
		Collection list = new ArrayList(ids.size());
		for (Iterator it = ids.iterator(); it.hasNext();) {
			Identifier id = (Identifier) it.next();
			list.add(this.loadStorableObject(id));
		}
		return list;
	}

	public SiteNode loadSiteNode(Identifier id) throws ApplicationException
	{
		return (SiteNode) this.loadStorableObject(id);
	}

	public Collection loadSiteNodes(Collection ids) throws ApplicationException
	{
		Collection list = new ArrayList(ids.size());
		for (Iterator it = ids.iterator(); it.hasNext();) {
			Identifier id = (Identifier) it.next();
			list.add(this.loadStorableObject(id));
		}
		return list;
	}

	public SiteNodeType loadSiteNodeType(Identifier id) throws ApplicationException
	{
		return (SiteNodeType) this.loadStorableObject(id);
	}

	public Collection loadSiteNodeTypes(Collection ids) throws ApplicationException
	{
		Collection list = new ArrayList(ids.size());
		for (Iterator it = ids.iterator(); it.hasNext();) {
			Identifier id = (Identifier) it.next();
			list.add(this.loadStorableObject(id));
		}
		return list;
	}

	public TopologicalNode loadTopologicalNode(Identifier id) throws ApplicationException
	{
		return (TopologicalNode) this.loadStorableObject(id);
	}

	public Collection loadTopologicalNodes(Collection ids) throws ApplicationException
	{
		Collection list = new ArrayList(ids.size());
		for (Iterator it = ids.iterator(); it.hasNext();) {
			Identifier id = (Identifier) it.next();
			list.add(this.loadStorableObject(id));
		}
		return list;
	}

	public Collection loadCollectorsButIds(StorableObjectCondition condition, Collection ids) throws ApplicationException
	{
		return this.loadStorableObjectButIds(condition, ids);
	}

	public Collection loadMapsButIds(StorableObjectCondition condition, Collection ids) throws ApplicationException
	{
		return this.loadStorableObjectButIds(condition, ids);
	}

	public Collection loadMarksButIds(StorableObjectCondition condition, Collection ids) throws ApplicationException
	{
		return this.loadStorableObjectButIds(condition, ids);
	}

	public Collection loadNodeLinksButIds(StorableObjectCondition condition, Collection ids) throws ApplicationException
	{
		return this.loadStorableObjectButIds(condition, ids);
	}

	public Collection loadPhysicalLinksButIds(StorableObjectCondition condition, Collection ids) throws ApplicationException
	{
		return this.loadStorableObjectButIds(condition, ids);
	}

	public Collection loadPhysicalLinkTypesButIds(StorableObjectCondition condition, Collection ids) throws ApplicationException
	{
		return this.loadStorableObjectButIds(condition, ids);
	}

	public Collection loadSiteNodesButIds(StorableObjectCondition condition, Collection ids) throws ApplicationException
	{
		return this.loadStorableObjectButIds(condition, ids);
	}

	public Collection loadSiteNodeTypesButIds(StorableObjectCondition condition, Collection ids) throws ApplicationException
	{
		return this.loadStorableObjectButIds(condition, ids);
	}

	public Collection loadTopologicalNodesButIds(StorableObjectCondition condition, Collection ids) throws ApplicationException
	{
		return this.loadStorableObjectButIds(condition, ids);
	}

	public void saveCollector(Collector collector, boolean force) throws ApplicationException
	{
		this.saveStorableObject(collector, force);
		this.mapXML.flush();
	}

	public void saveMap(Map map, boolean force) throws ApplicationException
	{
		this.saveStorableObject(map, force);
		this.mapXML.flush();
	}

	public void saveMark(Mark mark, boolean force) throws ApplicationException
	{
		this.saveStorableObject(mark, force);
		this.mapXML.flush();
	}

	public void saveNodeLink(NodeLink nodeLink, boolean force) throws ApplicationException
	{
		this.saveStorableObject(nodeLink, force);
		this.mapXML.flush();
	}

	public void savePhysicalLink(PhysicalLink physicalLink, boolean force) throws ApplicationException
	{
		this.saveStorableObject(physicalLink, force);
		this.mapXML.flush();
	}

	public void savePhysicalLinkType(PhysicalLinkType physicalLinkType, boolean force) throws ApplicationException
	{
		this.saveStorableObject(physicalLinkType, force);
		this.mapXML.flush();
	}

	public void saveSiteNode(SiteNode siteNode, boolean force) throws ApplicationException
	{
		this.saveStorableObject(siteNode, force);
		this.mapXML.flush();
	}

	public void saveSiteNodeType(SiteNodeType siteNodeType, boolean force) throws ApplicationException
	{
		this.saveStorableObject(siteNodeType, force);
		this.mapXML.flush();
	}

	public void saveTopologicalNode(TopologicalNode topologicalNode, boolean force) throws ApplicationException
	{
		this.saveStorableObject(topologicalNode, force);
		this.mapXML.flush();
	}

	public void saveCollectors(Collection list, boolean force) throws ApplicationException
	{
		this.saveStorableObjects(list, force);
	}

	public void saveMaps(Collection list, boolean force) throws ApplicationException
	{
		this.saveStorableObjects(list, force);
	}

	public void saveMarks(Collection list, boolean force) throws ApplicationException
	{
		this.saveStorableObjects(list, force);
	}

	public void saveNodeLinks(Collection list, boolean force) throws ApplicationException
	{
		this.saveStorableObjects(list, force);
	}

	public void savePhysicalLinks(Collection list, boolean force) throws ApplicationException
	{
		this.saveStorableObjects(list, force);
	}

	public void savePhysicalLinkTypes(Collection list, boolean force) throws ApplicationException
	{
		this.saveStorableObjects(list, force);
	}

	public void saveSiteNodes(Collection list, boolean force) throws ApplicationException
	{
		this.saveStorableObjects(list, force);
	}

	public void saveSiteNodeTypes(Collection list, boolean force) throws ApplicationException
	{
		this.saveStorableObjects(list, force);
	}

	public void saveTopologicalNodes(Collection list, boolean force) throws ApplicationException
	{
		this.saveStorableObjects(list, force);
	}
}
