/*
 * $Id: XMLMapObjectLoader.java,v 1.5 2005/02/21 07:45:32 bob Exp $
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

import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.DatabaseException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.SessionContext;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectXML;
import com.syrus.AMFICOM.general.StorableObjectXMLDriver;
import com.syrus.AMFICOM.general.UpdateObjectException;
import com.syrus.AMFICOM.general.VersionCollisionException;

/**
 * @version $Revision: 1.5 $, $Date: 2005/02/21 07:45:32 $
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

	private Collection loadStorableObjectButIds(StorableObjectCondition condition, Collection ids) throws CommunicationException {
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

	private void saveStorableObject(StorableObject storableObject, boolean force) throws CommunicationException {
		Identifier id = storableObject.getId();
		try {
			this.mapXML.updateObject(storableObject, force, SessionContext.getAccessIdentity().getUserId());
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

	private void saveStorableObjects(Collection storableObjects, boolean force) throws CommunicationException {
		for (Iterator it = storableObjects.iterator(); it.hasNext();) {
			StorableObject storableObject = (StorableObject) it.next();
			this.saveStorableObject(storableObject, force);
		}
		this.mapXML.flush();
	}

	public Collector loadCollector(Identifier id) throws DatabaseException, CommunicationException
	{
		return (Collector) this.loadStorableObject(id);
	}

	public Collection loadCollectors(Collection ids) throws DatabaseException, CommunicationException
	{
		Collection list = new ArrayList(ids.size());
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

	public Collection loadMaps(Collection ids) throws DatabaseException, CommunicationException
	{
		Collection list = new ArrayList(ids.size());
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

	public Collection loadMarks(Collection ids) throws DatabaseException, CommunicationException
	{
		Collection list = new ArrayList(ids.size());
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

	public Collection loadNodeLinks(Collection ids) throws DatabaseException, CommunicationException
	{
		Collection list = new ArrayList(ids.size());
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

	public Collection loadPhysicalLinks(Collection ids) throws DatabaseException, CommunicationException
	{
		Collection list = new ArrayList(ids.size());
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

	public Collection loadPhysicalLinkTypes(Collection ids) throws DatabaseException, CommunicationException
	{
		Collection list = new ArrayList(ids.size());
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

	public Collection loadSiteNodes(Collection ids) throws DatabaseException, CommunicationException
	{
		Collection list = new ArrayList(ids.size());
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

	public Collection loadSiteNodeTypes(Collection ids) throws DatabaseException, CommunicationException
	{
		Collection list = new ArrayList(ids.size());
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

	public Collection loadTopologicalNodes(Collection ids) throws DatabaseException, CommunicationException
	{
		Collection list = new ArrayList(ids.size());
		for (Iterator it = ids.iterator(); it.hasNext();) {
			Identifier id = (Identifier) it.next();
			list.add(this.loadStorableObject(id));
		}
		return list;
	}

	public Collection loadCollectorsButIds(StorableObjectCondition condition, Collection ids) throws DatabaseException, CommunicationException
	{
		return this.loadStorableObjectButIds(condition, ids);
	}

	public Collection loadMapsButIds(StorableObjectCondition condition, Collection ids) throws DatabaseException, CommunicationException
	{
		return this.loadStorableObjectButIds(condition, ids);
	}

	public Collection loadMarksButIds(StorableObjectCondition condition, Collection ids) throws DatabaseException, CommunicationException
	{
		return this.loadStorableObjectButIds(condition, ids);
	}

	public Collection loadNodeLinksButIds(StorableObjectCondition condition, Collection ids) throws DatabaseException, CommunicationException
	{
		return this.loadStorableObjectButIds(condition, ids);
	}

	public Collection loadPhysicalLinksButIds(StorableObjectCondition condition, Collection ids) throws DatabaseException, CommunicationException
	{
		return this.loadStorableObjectButIds(condition, ids);
	}

	public Collection loadPhysicalLinkTypesButIds(StorableObjectCondition condition, Collection ids) throws DatabaseException, CommunicationException
	{
		return this.loadStorableObjectButIds(condition, ids);
	}

	public Collection loadSiteNodesButIds(StorableObjectCondition condition, Collection ids) throws DatabaseException, CommunicationException
	{
		return this.loadStorableObjectButIds(condition, ids);
	}

	public Collection loadSiteNodeTypesButIds(StorableObjectCondition condition, Collection ids) throws DatabaseException, CommunicationException
	{
		return this.loadStorableObjectButIds(condition, ids);
	}

	public Collection loadTopologicalNodesButIds(StorableObjectCondition condition, Collection ids) throws DatabaseException, CommunicationException
	{
		return this.loadStorableObjectButIds(condition, ids);
	}

	public void saveCollector(Collector collector, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException
	{
		this.saveStorableObject(collector, force);
		this.mapXML.flush();
	}

	public void saveMap(Map map, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException
	{
		this.saveStorableObject(map, force);
		this.mapXML.flush();
	}

	public void saveMark(Mark mark, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException
	{
		this.saveStorableObject(mark, force);
		this.mapXML.flush();
	}

	public void saveNodeLink(NodeLink nodeLink, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException
	{
		this.saveStorableObject(nodeLink, force);
		this.mapXML.flush();
	}

	public void savePhysicalLink(PhysicalLink physicalLink, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException
	{
		this.saveStorableObject(physicalLink, force);
		this.mapXML.flush();
	}

	public void savePhysicalLinkType(PhysicalLinkType physicalLinkType, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException
	{
		this.saveStorableObject(physicalLinkType, force);
		this.mapXML.flush();
	}

	public void saveSiteNode(SiteNode siteNode, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException
	{
		this.saveStorableObject(siteNode, force);
		this.mapXML.flush();
	}

	public void saveSiteNodeType(SiteNodeType siteNodeType, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException
	{
		this.saveStorableObject(siteNodeType, force);
		this.mapXML.flush();
	}

	public void saveTopologicalNode(TopologicalNode topologicalNode, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException
	{
		this.saveStorableObject(topologicalNode, force);
		this.mapXML.flush();
	}

	public void saveCollectors(Collection list, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException
	{
		this.saveStorableObjects(list, force);
	}

	public void saveMaps(Collection list, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException
	{
		this.saveStorableObjects(list, force);
	}

	public void saveMarks(Collection list, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException
	{
		this.saveStorableObjects(list, force);
	}

	public void saveNodeLinks(Collection list, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException
	{
		this.saveStorableObjects(list, force);
	}

	public void savePhysicalLinks(Collection list, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException
	{
		this.saveStorableObjects(list, force);
	}

	public void savePhysicalLinkTypes(Collection list, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException
	{
		this.saveStorableObjects(list, force);
	}

	public void saveSiteNodes(Collection list, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException
	{
		this.saveStorableObjects(list, force);
	}

	public void saveSiteNodeTypes(Collection list, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException
	{
		this.saveStorableObjects(list, force);
	}

	public void saveTopologicalNodes(Collection list, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException
	{
		this.saveStorableObjects(list, force);
	}
}
