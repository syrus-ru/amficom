/*-
 * $$Id: MapLibraryController.java,v 1.14 2006/04/14 12:04:07 arseniy Exp $$
 *
 * Copyright 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.map.controllers;

import java.util.Collection;
import java.util.Iterator;

import com.syrus.AMFICOM.client.resource.I18N;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.general.TypicalCondition;
import com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlTypicalConditionPackage.OperationSort;
import com.syrus.AMFICOM.map.MapLibrary;
import com.syrus.AMFICOM.map.PhysicalLinkType;
import com.syrus.AMFICOM.map.SiteNodeType;
import com.syrus.AMFICOM.map.corba.IdlPhysicalLinkTypePackage.PhysicalLinkTypeSort;
import com.syrus.AMFICOM.map.corba.IdlSiteNodeTypePackage.SiteNodeTypeSort;

/**
 * контроллер типа сетевого узла.
 * 
 * @version $Revision: 1.14 $, $Date: 2006/04/14 12:04:07 $
 * @author $Author: arseniy $
 * @author Andrei Kroupennikov
 * @module mapviewclient
 */
public class MapLibraryController {

	public static final String DEFAULT_LIBRARY = "defaultLibrary"; //$NON-NLS-1$

	/**
	 * Instance.
	 */
	private static MapLibraryController instance = null;

	/**
	 * Private constructor.
	 */
	protected MapLibraryController() {
		//empty
	}

	/**
	 * Get instance.
	 * 
	 * @return instance
	 */
	public static MapLibraryController getInstance() {
		if(instance == null)
			instance = new MapLibraryController();
		return instance;
	}

	/**
	 * Получить тип сетевого узла по кодовому имени. В случае, если такого типа
	 * нет, создается новый.
	 * 
	 * @param userId пользователь
	 * @param codename кодовое имя
	 * @return тип сетевого узла
	 * @throws ApplicationException 
	 * @throws CreateObjectException 
	 */
	private static MapLibrary getMapLibrary(
			Identifier userId,
			String codename) throws ApplicationException {
		MapLibrary library = getMapLibrary(codename);
		if(library == null) {
			library = MapLibrary.createInstance(
				userId,
				I18N.getString(codename),
				codename,
				"", //$NON-NLS-1$
				null);
				
			StorableObjectPool.flush(library, userId, true);
		}
		return library;
	}

	public static MapLibrary getDefaultMapLibrary() {
		return MapLibraryController.defaultLibrary;
	}
	
	private static MapLibrary defaultLibrary = null;

	public static void createDefaults() throws ApplicationException {
		defaultLibrary = MapLibraryController.getMapLibrary(LoginManager.getUserId(), DEFAULT_LIBRARY);

		NodeTypeController.createDefaults();
		
		SiteNodeType siteNodeType;
		// make sure SiteNodeType.ATS is created
		siteNodeType = NodeTypeController.getSiteNodeType(defaultLibrary, SiteNodeTypeSort.ATS, SiteNodeType.DEFAULT_ATS, true);
		siteNodeType.setMapLibrary(defaultLibrary);
		// make sure SiteNodeType.BUILDING is created
		siteNodeType = NodeTypeController.getSiteNodeType(defaultLibrary, SiteNodeTypeSort.BUILDING, SiteNodeType.DEFAULT_BUILDING, true);
		siteNodeType.setMapLibrary(defaultLibrary);
		// make sure SiteNodeType.PIQUET is created
		siteNodeType = NodeTypeController.getSiteNodeType(defaultLibrary, SiteNodeTypeSort.PIQUET, SiteNodeType.DEFAULT_PIQUET, true);
		siteNodeType.setMapLibrary(defaultLibrary);
		// make sure SiteNodeType.WELL is created
		siteNodeType = NodeTypeController.getSiteNodeType(defaultLibrary, SiteNodeTypeSort.WELL, SiteNodeType.DEFAULT_WELL, true);
		siteNodeType.setMapLibrary(defaultLibrary);
		// make sure SiteNodeType.CABLE_INLET is created
		siteNodeType = NodeTypeController.getSiteNodeType(defaultLibrary, SiteNodeTypeSort.CABLE_INLET, SiteNodeType.DEFAULT_CABLE_INLET, false);
		siteNodeType.setMapLibrary(defaultLibrary);
		// make sure SiteNodeType.UNBOUND is created
		siteNodeType = NodeTypeController.getSiteNodeType(defaultLibrary, SiteNodeTypeSort.UNBOUND, SiteNodeType.DEFAULT_UNBOUND, false);
		siteNodeType.setMapLibrary(defaultLibrary);
		// make sure SiteNodeType.CABLE_INLET is created
		siteNodeType = NodeTypeController.getSiteNodeType(defaultLibrary, SiteNodeTypeSort.TOWER, SiteNodeType.DEFAULT_TOWER, true);
		siteNodeType.setMapLibrary(defaultLibrary);

		PhysicalLinkType physicalLinkType;
		// make sure PhysicalLinkType.TUNNEL is created
		physicalLinkType = LinkTypeController.getPhysicalLinkType(defaultLibrary, PhysicalLinkTypeSort.TUNNEL, PhysicalLinkType.DEFAULT_TUNNEL, true);
		physicalLinkType.setMapLibrary(defaultLibrary); 
		// make sure PhysicalLinkType.COLLECTOR is created
		physicalLinkType = LinkTypeController.getPhysicalLinkType(defaultLibrary, PhysicalLinkTypeSort.COLLECTOR, PhysicalLinkType.DEFAULT_COLLECTOR, true);
		physicalLinkType.setMapLibrary(defaultLibrary); 
		// make sure PhysicalLinkType.INDOOR is created
		physicalLinkType = LinkTypeController.getPhysicalLinkType(defaultLibrary, PhysicalLinkTypeSort.INDOOR, PhysicalLinkType.DEFAULT_INDOOR, false);
		physicalLinkType.setMapLibrary(defaultLibrary); 
		// make sure PhysicalLinkType.SUBMARINE is created
		physicalLinkType = LinkTypeController.getPhysicalLinkType(defaultLibrary, PhysicalLinkTypeSort.SUBMARINE, PhysicalLinkType.DEFAULT_SUBMARINE, true);
		physicalLinkType.setMapLibrary(defaultLibrary); 
		// make sure PhysicalLinkType.OVERHEAD is created
		physicalLinkType = LinkTypeController.getPhysicalLinkType(defaultLibrary, PhysicalLinkTypeSort.OVERHEAD, PhysicalLinkType.DEFAULT_OVERHEAD, true);
		physicalLinkType.setMapLibrary(defaultLibrary); 
		// make sure PhysicalLinkType.UNBOUND is created
		physicalLinkType = LinkTypeController.getPhysicalLinkType(defaultLibrary, PhysicalLinkTypeSort.UNBOUND, PhysicalLinkType.DEFAULT_UNBOUND, false);
		physicalLinkType.setMapLibrary(defaultLibrary); 
	}

	/**
	 * Получить тип линии по кодовому имени.
	 * @param codename кодовое имя
	 * @return тип линии
	 * @throws ApplicationException 
	 */
	public static MapLibrary getMapLibrary(
			String codename) throws ApplicationException
	{
		StorableObjectCondition pTypeCondition = new TypicalCondition(
				codename, 
				OperationSort.OPERATION_EQUALS,
				ObjectEntities.MAPLIBRARY_CODE,
				StorableObjectWrapper.COLUMN_CODENAME);

		Collection pTypes =
			StorableObjectPool.getStorableObjectsByCondition(pTypeCondition, true);
		for (Iterator it = pTypes.iterator(); it.hasNext();)
		{
			MapLibrary library = (MapLibrary )it.next();
			if (library.getCodename().equals(codename))
				return library;
		}

		return null;
	}

}
