/*
 * $Id: XMLMapObjectLoader.java,v 1.1 2005/02/07 16:18:10 krupenn Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.map;

import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.DatabaseException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectXML;
import com.syrus.AMFICOM.general.StorableObjectXMLDriver;
import com.syrus.AMFICOM.general.UpdateObjectException;
import com.syrus.AMFICOM.general.VersionCollisionException;

import java.io.File;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * @version $Revision: 1.1 $, $Date: 2005/02/07 16:18:10 $
 * @author $Author: krupenn $
 * @module configuration_v1
 */

public final class XMLMapObjectLoader implements MapObjectLoader {

	private StorableObjectXML mapXML;

	public XMLMapObjectLoader(final File path) {
		StorableObjectXMLDriver driver = new StorableObjectXMLDriver(path, "map");
		this.mapXML = new StorableObjectXML(driver);
	}

	public void delete(Identifier id) throws CommunicationException, DatabaseException {
		try {
			this.mapXML.delete(id);
		} catch (IllegalDataException e) {
			throw new CommunicationException("XMLMapObjectLoader.delete | caught " + e.getMessage(), e);
		}
		this.mapXML.flush();
	}

	public void delete(List ids) throws CommunicationException, DatabaseException {
		try {
			for (Iterator it = ids.iterator(); it.hasNext();) {
				Identifier id = (Identifier) it.next();
				this.mapXML.delete(id);
			}
		} catch (IllegalDataException e) {
			throw new CommunicationException("XMLMapObjectLoader.delete | caught " + e.getMessage(), e);
		}
		this.mapXML.flush();
	}

	public Set refresh(Set storableObjects) throws CommunicationException, DatabaseException {
		// TODO Auto-generated method stub
		return null;
	}

	private StorableObject loadStorableObject(Identifier id) throws CommunicationException {
		try {
			return this.mapXML.retrieve(id);
		} catch (ObjectNotFoundException e) {
			throw new CommunicationException("XMLMapObjectLoader.load"
					+ ObjectEntities.codeToString(id.getMajor()) + " | caught " + e.getMessage(), e);
		} catch (RetrieveObjectException e) {
			throw new CommunicationException("XMLMapObjectLoader.load"
					+ ObjectEntities.codeToString(id.getMajor()) + " | caught " + e.getMessage(), e);
		} catch (IllegalDataException e) {
			throw new CommunicationException("XMLMapObjectLoader.load"
					+ ObjectEntities.codeToString(id.getMajor()) + " | caught " + e.getMessage(), e);
		}
	}

	private List loadStorableObjectButIds(StorableObjectCondition condition, List ids) throws CommunicationException {
		try {
			return this.mapXML.retrieveByCondition(ids, condition);
		} catch (RetrieveObjectException e) {
			throw new CommunicationException("XMLMapObjectLoader.loadStorableObjectButIds | caught "
					+ e.getMessage(), e);
		} catch (IllegalDataException e) {
			throw new CommunicationException("XMLMapObjectLoader.loadStorableObjectButIds | caught "
					+ e.getMessage(), e);
		}
	}

	private void saveStorableObject(StorableObject storableObject) throws CommunicationException {
		Identifier id = storableObject.getId();
		try {
			this.mapXML.updateObject(storableObject);
		} catch (UpdateObjectException e) {
			throw new CommunicationException("XMLMapObjectLoader.save"
					+ ObjectEntities.codeToString(id.getMajor()) + " | caught " + e.getMessage(), e);
		} catch (IllegalDataException e) {
			throw new CommunicationException("XMLMapObjectLoader.save"
					+ ObjectEntities.codeToString(id.getMajor()) + " | caught " + e.getMessage(), e);
		} catch (VersionCollisionException e) {
			throw new CommunicationException("XMLMapObjectLoader.save"
					+ ObjectEntities.codeToString(id.getMajor()) + " | caught " + e.getMessage(), e);
		}
	}

	private void saveStorableObjects(List storableObjects) throws CommunicationException {
		for (Iterator it = storableObjects.iterator(); it.hasNext();) {
			StorableObject storableObject = (StorableObject) it.next();
			this.saveStorableObject(storableObject);
		}
		this.mapXML.flush();
	}

	public Collector loadCollector(Identifier id) throws DatabaseException, CommunicationException
	{
		return (Collector) this.loadStorableObject(id);
	}

	public List loadCollectors(List ids) throws DatabaseException, CommunicationException
	{
		List list = new ArrayList(ids.size());
		for (Iterator it = ids.iterator(); it.hasNext();) {
			Identifier id = (Identifier) it.next();
			list.add(this.loadStorableObject(id));
		}
		return list;
	}

	public Map loadMap(Identifier id) throws DatabaseException, CommunicationException
	{
		return (Map) this.loadStorableObject(id);
	}

	public List loadMaps(List ids) throws DatabaseException, CommunicationException
	{
		List list = new ArrayList(ids.size());
		for (Iterator it = ids.iterator(); it.hasNext();) {
			Identifier id = (Identifier) it.next();
			list.add(this.loadStorableObject(id));
		}
		return list;
	}

	public Mark loadMark(Identifier id) throws DatabaseException, CommunicationException
	{
		return (Mark) this.loadStorableObject(id);
	}

	public List loadMarks(List ids) throws DatabaseException, CommunicationException
	{
		List list = new ArrayList(ids.size());
		for (Iterator it = ids.iterator(); it.hasNext();) {
			Identifier id = (Identifier) it.next();
			list.add(this.loadStorableObject(id));
		}
		return list;
	}

	public NodeLink loadNodeLink(Identifier id) throws DatabaseException, CommunicationException
	{
		return (NodeLink) this.loadStorableObject(id);
	}

	public List loadNodeLinks(List ids) throws DatabaseException, CommunicationException
	{
		List list = new ArrayList(ids.size());
		for (Iterator it = ids.iterator(); it.hasNext();) {
			Identifier id = (Identifier) it.next();
			list.add(this.loadStorableObject(id));
		}
		return list;
	}

	public PhysicalLink loadPhysicalLink(Identifier id) throws DatabaseException, CommunicationException
	{
		return (PhysicalLink) this.loadStorableObject(id);
	}

	public List loadPhysicalLinks(List ids) throws DatabaseException, CommunicationException
	{
		List list = new ArrayList(ids.size());
		for (Iterator it = ids.iterator(); it.hasNext();) {
			Identifier id = (Identifier) it.next();
			list.add(this.loadStorableObject(id));
		}
		return list;
	}

	public PhysicalLinkType loadPhysicalLinkType(Identifier id) throws DatabaseException, CommunicationException
	{
		return (PhysicalLinkType) this.loadStorableObject(id);
	}

	public List loadPhysicalLinkTypes(List ids) throws DatabaseException, CommunicationException
	{
		List list = new ArrayList(ids.size());
		for (Iterator it = ids.iterator(); it.hasNext();) {
			Identifier id = (Identifier) it.next();
			list.add(this.loadStorableObject(id));
		}
		return list;
	}

	public SiteNode loadSiteNode(Identifier id) throws DatabaseException, CommunicationException
	{
		return (SiteNode) this.loadStorableObject(id);
	}

	public List loadSiteNodes(List ids) throws DatabaseException, CommunicationException
	{
		List list = new ArrayList(ids.size());
		for (Iterator it = ids.iterator(); it.hasNext();) {
			Identifier id = (Identifier) it.next();
			list.add(this.loadStorableObject(id));
		}
		return list;
	}

	public SiteNodeType loadSiteNodeType(Identifier id) throws DatabaseException, CommunicationException
	{
		return (SiteNodeType) this.loadStorableObject(id);
	}

	public List loadSiteNodeTypes(List ids) throws DatabaseException, CommunicationException
	{
		List list = new ArrayList(ids.size());
		for (Iterator it = ids.iterator(); it.hasNext();) {
			Identifier id = (Identifier) it.next();
			list.add(this.loadStorableObject(id));
		}
		return list;
	}

	public TopologicalNode loadTopologicalNode(Identifier id) throws DatabaseException, CommunicationException
	{
		return (TopologicalNode) this.loadStorableObject(id);
	}

	public List loadTopologicalNodes(List ids) throws DatabaseException, CommunicationException
	{
		List list = new ArrayList(ids.size());
		for (Iterator it = ids.iterator(); it.hasNext();) {
			Identifier id = (Identifier) it.next();
			list.add(this.loadStorableObject(id));
		}
		return list;
	}

	public List loadCollectorsButIds(StorableObjectCondition condition, List ids) throws DatabaseException, CommunicationException
	{
		return this.loadStorableObjectButIds(condition, ids);
	}

	public List loadMapsButIds(StorableObjectCondition condition, List ids) throws DatabaseException, CommunicationException
	{
		return this.loadStorableObjectButIds(condition, ids);
	}

	public List loadMarksButIds(StorableObjectCondition condition, List ids) throws DatabaseException, CommunicationException
	{
		return this.loadStorableObjectButIds(condition, ids);
	}

	public List loadNodeLinksButIds(StorableObjectCondition condition, List ids) throws DatabaseException, CommunicationException
	{
		return this.loadStorableObjectButIds(condition, ids);
	}

	public List loadPhysicalLinksButIds(StorableObjectCondition condition, List ids) throws DatabaseException, CommunicationException
	{
		return this.loadStorableObjectButIds(condition, ids);
	}

	public List loadPhysicalLinkTypesButIds(StorableObjectCondition condition, List ids) throws DatabaseException, CommunicationException
	{
		return this.loadStorableObjectButIds(condition, ids);
	}

	public List loadSiteNodesButIds(StorableObjectCondition condition, List ids) throws DatabaseException, CommunicationException
	{
		return this.loadStorableObjectButIds(condition, ids);
	}

	public List loadSiteNodeTypesButIds(StorableObjectCondition condition, List ids) throws DatabaseException, CommunicationException
	{
		return this.loadStorableObjectButIds(condition, ids);
	}

	public List loadTopologicalNodesButIds(StorableObjectCondition condition, List ids) throws DatabaseException, CommunicationException
	{
		return this.loadStorableObjectButIds(condition, ids);
	}

	public void saveCollector(Collector collector, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException
	{
		this.saveStorableObject(collector);
		this.mapXML.flush();
	}

	public void saveMap(Map map, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException
	{
		this.saveStorableObject(map);
		this.mapXML.flush();
	}

	public void saveMark(Mark mark, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException
	{
		this.saveStorableObject(mark);
		this.mapXML.flush();
	}

	public void saveNodeLink(NodeLink nodeLink, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException
	{
		this.saveStorableObject(nodeLink);
		this.mapXML.flush();
	}

	public void savePhysicalLink(PhysicalLink physicalLink, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException
	{
		this.saveStorableObject(physicalLink);
		this.mapXML.flush();
	}

	public void savePhysicalLinkType(PhysicalLinkType physicalLinkType, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException
	{
		this.saveStorableObject(physicalLinkType);
		this.mapXML.flush();
	}

	public void saveSiteNode(SiteNode siteNode, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException
	{
		this.saveStorableObject(siteNode);
		this.mapXML.flush();
	}

	public void saveSiteNodeType(SiteNodeType siteNodeType, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException
	{
		this.saveStorableObject(siteNodeType);
		this.mapXML.flush();
	}

	public void saveTopologicalNode(TopologicalNode topologicalNode, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException
	{
		this.saveStorableObject(topologicalNode);
		this.mapXML.flush();
	}

	public void saveCollectors(List list, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException
	{
		this.saveStorableObjects(list);
	}

	public void saveMaps(List list, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException
	{
		this.saveStorableObjects(list);
	}

	public void saveMarks(List list, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException
	{
		this.saveStorableObjects(list);
	}

	public void saveNodeLinks(List list, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException
	{
		this.saveStorableObjects(list);
	}

	public void savePhysicalLinks(List list, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException
	{
		this.saveStorableObjects(list);
	}

	public void savePhysicalLinkTypes(List list, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException
	{
		this.saveStorableObjects(list);
	}

	public void saveSiteNodes(List list, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException
	{
		this.saveStorableObjects(list);
	}

	public void saveSiteNodeTypes(List list, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException
	{
		this.saveStorableObjects(list);
	}

	public void saveTopologicalNodes(List list, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException
	{
		this.saveStorableObjects(list);
	}
}
