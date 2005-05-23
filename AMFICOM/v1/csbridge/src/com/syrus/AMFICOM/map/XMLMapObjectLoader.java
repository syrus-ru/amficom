/*
 * $Id: XMLMapObjectLoader.java,v 1.5 2005/05/23 13:51:17 bass Exp $
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
 * @version $Revision: 1.5 $, $Date: 2005/05/23 13:51:17 $
 * @author $Author: bass $
 * @module csbridge_v1
 */

public final class XMLMapObjectLoader extends XMLObjectLoader implements MapObjectLoader {

	private StorableObjectXML mapXML;

	public XMLMapObjectLoader(final File path) {
		StorableObjectXMLDriver driver = new StorableObjectXMLDriver(path, "map");
		this.mapXML = new StorableObjectXML(driver);
	}

	public void delete(final Set identifiables) {
		for (Iterator it = identifiables.iterator(); it.hasNext();) {
			Identifier id = (Identifier) it.next();
			this.mapXML.delete(id);
		}
		this.mapXML.flush();
	}

	public Set refresh(final Set storableObjects) throws ApplicationException {
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

	public Set loadCollectors(Set ids) throws ApplicationException
	{
		Set list = new HashSet(ids.size());
		for (Iterator it = ids.iterator(); it.hasNext();) {
			Identifier id = (Identifier) it.next();
			list.add(this.loadStorableObject(id));
		}
		return list;
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

	public Set loadMarks(Set ids) throws ApplicationException
	{
		Set list = new HashSet(ids.size());
		for (Iterator it = ids.iterator(); it.hasNext();) {
			Identifier id = (Identifier) it.next();
			list.add(this.loadStorableObject(id));
		}
		return list;
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

	public Set loadPhysicalLinks(Set ids) throws ApplicationException
	{
		Set list = new HashSet(ids.size());
		for (Iterator it = ids.iterator(); it.hasNext();) {
			Identifier id = (Identifier) it.next();
			list.add(this.loadStorableObject(id));
		}
		return list;
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

	public Set loadSiteNodes(Set ids) throws ApplicationException
	{
		Set list = new HashSet(ids.size());
		for (Iterator it = ids.iterator(); it.hasNext();) {
			Identifier id = (Identifier) it.next();
			list.add(this.loadStorableObject(id));
		}
		return list;
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
