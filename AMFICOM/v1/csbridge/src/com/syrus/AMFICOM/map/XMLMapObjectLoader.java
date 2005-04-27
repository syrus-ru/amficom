/*
 * $Id: XMLMapObjectLoader.java,v 1.2 2005/04/27 15:39:00 arseniy Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.map;

import java.io.File;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectXML;
import com.syrus.AMFICOM.general.StorableObjectXMLDriver;
import com.syrus.AMFICOM.general.XMLObjectLoader;

/**
 * @version $Revision: 1.2 $, $Date: 2005/04/27 15:39:00 $
 * @author $Author: arseniy $
 * @module csbridge_v1
 */

public final class XMLMapObjectLoader extends XMLObjectLoader implements MapObjectLoader {

	private StorableObjectXML mapXML;

	public XMLMapObjectLoader(final File path) {
		StorableObjectXMLDriver driver = new StorableObjectXMLDriver(path, "map");
		this.mapXML = new StorableObjectXML(driver);
	}

	public void delete(Identifier id) {
		this.mapXML.delete(id);
		this.mapXML.flush();
	}

	public void delete(final Set identifiables) {
		for (Iterator it = identifiables.iterator(); it.hasNext();) {
			Identifier id = (Identifier) it.next();
			this.mapXML.delete(id);
		}
		this.mapXML.flush();
	}

	public Set refresh(Set storableObjects) throws ApplicationException {
		return Collections.EMPTY_SET;
	}

	private StorableObject loadStorableObject(Identifier id) throws ApplicationException {
		return this.mapXML.retrieve(id);
	}

	private Set loadStorableObjectButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		return this.mapXML.retrieveByCondition(ids, condition);
	}

	private void saveStorableObject(StorableObject storableObject, boolean force) throws ApplicationException {
		this.mapXML.updateObject(storableObject, force);
	}

	private void saveStorableObjects(Set storableObjects, boolean force) throws ApplicationException {
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

	public Set loadCollectors(Set ids) throws ApplicationException
	{
		Set list = new HashSet(ids.size());
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

	public Set loadMaps(Set ids) throws ApplicationException
	{
		Set list = new HashSet(ids.size());
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

	public Set loadMarks(Set ids) throws ApplicationException
	{
		Set list = new HashSet(ids.size());
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

	public Set loadNodeLinks(Set ids) throws ApplicationException
	{
		Set list = new HashSet(ids.size());
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

	public Set loadPhysicalLinks(Set ids) throws ApplicationException
	{
		Set list = new HashSet(ids.size());
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

	public Set loadPhysicalLinkTypes(Set ids) throws ApplicationException
	{
		Set list = new HashSet(ids.size());
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

	public Set loadSiteNodes(Set ids) throws ApplicationException
	{
		Set list = new HashSet(ids.size());
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

	public Set loadSiteNodeTypes(Set ids) throws ApplicationException
	{
		Set list = new HashSet(ids.size());
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

	public Set loadTopologicalNodes(Set ids) throws ApplicationException
	{
		Set list = new HashSet(ids.size());
		for (Iterator it = ids.iterator(); it.hasNext();) {
			Identifier id = (Identifier) it.next();
			list.add(this.loadStorableObject(id));
		}
		return list;
	}

	public Set loadCollectorsButIds(StorableObjectCondition condition, Set ids) throws ApplicationException
	{
		return this.loadStorableObjectButIds(condition, ids);
	}

	public Set loadMapsButIds(StorableObjectCondition condition, Set ids) throws ApplicationException
	{
		return this.loadStorableObjectButIds(condition, ids);
	}

	public Set loadMarksButIds(StorableObjectCondition condition, Set ids) throws ApplicationException
	{
		return this.loadStorableObjectButIds(condition, ids);
	}

	public Set loadNodeLinksButIds(StorableObjectCondition condition, Set ids) throws ApplicationException
	{
		return this.loadStorableObjectButIds(condition, ids);
	}

	public Set loadPhysicalLinksButIds(StorableObjectCondition condition, Set ids) throws ApplicationException
	{
		return this.loadStorableObjectButIds(condition, ids);
	}

	public Set loadPhysicalLinkTypesButIds(StorableObjectCondition condition, Set ids) throws ApplicationException
	{
		return this.loadStorableObjectButIds(condition, ids);
	}

	public Set loadSiteNodesButIds(StorableObjectCondition condition, Set ids) throws ApplicationException
	{
		return this.loadStorableObjectButIds(condition, ids);
	}

	public Set loadSiteNodeTypesButIds(StorableObjectCondition condition, Set ids) throws ApplicationException
	{
		return this.loadStorableObjectButIds(condition, ids);
	}

	public Set loadTopologicalNodesButIds(StorableObjectCondition condition, Set ids) throws ApplicationException
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

	public void saveCollectors(Set list, boolean force) throws ApplicationException
	{
		this.saveStorableObjects(list, force);
	}

	public void saveMaps(Set list, boolean force) throws ApplicationException
	{
		this.saveStorableObjects(list, force);
	}

	public void saveMarks(Set list, boolean force) throws ApplicationException
	{
		this.saveStorableObjects(list, force);
	}

	public void saveNodeLinks(Set list, boolean force) throws ApplicationException
	{
		this.saveStorableObjects(list, force);
	}

	public void savePhysicalLinks(Set list, boolean force) throws ApplicationException
	{
		this.saveStorableObjects(list, force);
	}

	public void savePhysicalLinkTypes(Set list, boolean force) throws ApplicationException
	{
		this.saveStorableObjects(list, force);
	}

	public void saveSiteNodes(Set list, boolean force) throws ApplicationException
	{
		this.saveStorableObjects(list, force);
	}

	public void saveSiteNodeTypes(Set list, boolean force) throws ApplicationException
	{
		this.saveStorableObjects(list, force);
	}

	public void saveTopologicalNodes(Set list, boolean force) throws ApplicationException
	{
		this.saveStorableObjects(list, force);
	}
}
