/**
 * $Id: NodeTypeController.java,v 1.36 2005/08/08 13:06:24 krupenn Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: ������� ������������������ �������������������
 *         ���������������� �������� ���������� �����������
 */

package com.syrus.AMFICOM.client.map.controllers;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.geom.Rectangle2D;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;

import com.syrus.AMFICOM.client.map.MapConnectionException;
import com.syrus.AMFICOM.client.map.MapDataException;
import com.syrus.AMFICOM.client.map.MapPropertiesManager;
import com.syrus.AMFICOM.client.resource.LangModelMap;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.EquivalentCondition;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.general.TypicalCondition;
import com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlTypicalConditionPackage.OperationSort;
import com.syrus.AMFICOM.map.MapElement;
import com.syrus.AMFICOM.map.MapLibrary;
import com.syrus.AMFICOM.map.SiteNodeType;
import com.syrus.AMFICOM.map.corba.IdlSiteNodeTypePackage.SiteNodeTypeSort;
import com.syrus.AMFICOM.resource.FileImageResource;
import com.syrus.AMFICOM.resource.ImageResourceWrapper;
import com.syrus.AMFICOM.resource.corba.IdlImageResourcePackage.IdlImageResourceDataPackage.ImageResourceSort;

/**
 * ���������� ���� �������� ����.
 * @author $Author: krupenn $
 * @version $Revision: 1.36 $, $Date: 2005/08/08 13:06:24 $
 * @module mapviewclient_v1
 */
public class NodeTypeController extends AbstractNodeController {

	private static final String DEFAULT_IMAGE_CODENAME = "defaultimageresource";

	private static final String DEFAULT_IMAGE_FILENAME = "images/defaultsite.gif";

	/**
	 * Instance.
	 */
	private static NodeTypeController instance = null;

	/** ���-������� ���� ���������� ��� ����������������� ����� �����. */
	private static java.util.Map imageFileNames = new HashMap();

	private static Identifier defaultImageId = null;
	
	static {
		imageFileNames.put(SiteNodeType.DEFAULT_UNBOUND, "images/unbound.gif");
		imageFileNames.put(SiteNodeType.DEFAULT_ATS, "images/ats.gif");
		imageFileNames.put(SiteNodeType.DEFAULT_BUILDING, "images/building.gif");
		imageFileNames.put(SiteNodeType.DEFAULT_PIQUET, "images/piquet.gif");
		imageFileNames.put(SiteNodeType.DEFAULT_WELL, "images/well.gif");
		imageFileNames.put(SiteNodeType.DEFAULT_CABLE_INLET, "images/cableinlet.gif");
		imageFileNames.put(SiteNodeType.DEFAULT_TOWER, "images/tower.gif");
	}

	/**
	 * Private constructor.
	 */
	protected NodeTypeController() {
		super(null);
	}

	/**
	 * Get instance.
	 * 
	 * @return instance
	 */
	public static MapElementController getInstance() {
		if(instance == null)
			instance = new NodeTypeController();
		return instance;
	}

	/**
	 * {@inheritDoc}
	 * Suppress since SiteNodeType is not really a Map Element
	 */
	public boolean isElementVisible(
			MapElement me,
			Rectangle2D.Double visibleBounds)
			throws MapConnectionException, MapDataException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc} Suppress since SiteNodeType is not really a Map Element
	 */
	public void paint(
			MapElement me,
			Graphics g,
			Rectangle2D.Double visibleBounds)
			throws MapConnectionException, MapDataException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc} Suppress since SiteNodeType is not really a Map Element
	 */
	public boolean isMouseOnElement(MapElement me, Point currentMousePoint)
			throws MapConnectionException, MapDataException {
		throw new UnsupportedOperationException();
	}

	/**
	 * �������� ��� ����������� �� �������� ����� ��� ������������������ ����
	 * �������� ����.
	 * 
	 * @param codename ������� ���
	 * @return ��� �����������
	 */
	public static String getImageFileName(String codename) {
		return (String )imageFileNames.get(codename);
	}

	public static Image getImage(SiteNodeType type) {
		return MapPropertiesManager.getImage(type.getImageId());
	}

	/**
	 * �������� ����������� �� �������� ����� ��� ������������������ ����
	 * �������� ����. ���� ����������� �� ����������, ��� ���������.
	 * 
	 * @param userId ������������
	 * @param codename ������� ���
	 * @param filename ���� �����������
	 * @return ������������� ����������� ({@link com.syrus.AMFICOM.resource.AbstractImageResource})
	 * @throws ApplicationException 
	 */
	public static Identifier getImageId(
			Identifier userId,
			String codename, 
			String filename) throws ApplicationException {
		StorableObjectCondition condition = new TypicalCondition(
			String.valueOf(ImageResourceSort._FILE),
			OperationSort.OPERATION_EQUALS,
			ObjectEntities.IMAGERESOURCE_CODE,
			ImageResourceWrapper.COLUMN_SORT);
		Collection bitMaps = StorableObjectPool.getStorableObjectsByCondition(condition, true);

		for(Iterator it = bitMaps.iterator(); it.hasNext();) {
			FileImageResource ir = (FileImageResource )it.next();
			if(ir.getCodename().equals(codename))
				return ir.getId();
		}
		FileImageResource ir = FileImageResource.createInstance(
				userId,
				codename,
				filename);
		StorableObjectPool.flush(ir, userId, true);
//		FileImageResource ir = (FileImageResource )bitMaps.iterator().next();
		return ir.getId();
	}

	/**
	 * �������� ��� �������� ���� �� �������� �����.
	 * 
	 * @param codename ������� ���
	 * @return ��� �������� ����
	 * @throws ApplicationException 
	 */
	public static SiteNodeType getSiteNodeType(String codename) throws ApplicationException {
		StorableObjectCondition pTypeCondition = new TypicalCondition(
				codename,
				OperationSort.OPERATION_EQUALS,
				ObjectEntities.SITENODE_TYPE_CODE,
				StorableObjectWrapper.COLUMN_CODENAME);

		Collection pTypes = 
			StorableObjectPool.getStorableObjectsByCondition(pTypeCondition, true);
		for(Iterator it = pTypes.iterator(); it.hasNext();) {
			SiteNodeType type = (SiteNodeType )it.next();
			if(type.getCodename().equals(codename))
				return type;
		}
		return null;
	}

	/**
	 * �������� ��� �������� ���� �� �������� �����. � ������, ���� ������ ����
	 * ���, ��������� �����.
	 * @param userId ������������
	 * @param codename ������� ���
	 * @param isTopological TODO
	 * 
	 * @return ��� �������� ����
	 * @throws ApplicationException 
	 * @throws CreateObjectException 
	 */
	static SiteNodeType getSiteNodeType(
			MapLibrary mapLibrary,
			Identifier userId,
			SiteNodeTypeSort sort,
			String codename,
			boolean isTopological) throws ApplicationException {
		SiteNodeType type = getSiteNodeType(codename);
		if(type == null) {
			type = SiteNodeType.createInstance(
				userId,
				sort,
				codename,
				LangModelMap.getString(codename),
				"",
				NodeTypeController.getImageId(
						userId, 
						codename, 
						NodeTypeController.getImageFileName(codename)),
						isTopological,
				mapLibrary.getId());
				
			StorableObjectPool.putStorableObject(type);
			StorableObjectPool.flush(type, userId, true);
		}
		return type;
	}
/*
	public static void createDefaults(Identifier creatorId) throws ApplicationException {
		// make sure SiteNodeType.ATS is created
		NodeTypeController.getSiteNodeType(creatorId, SiteNodeTypeSort.ATS, SiteNodeType.DEFAULT_ATS);
		// make sure SiteNodeType.BUILDING is created
		NodeTypeController.getSiteNodeType(creatorId, SiteNodeTypeSort.BUILDING, SiteNodeType.DEFAULT_BUILDING);
		// make sure SiteNodeType.PIQUET is created
		NodeTypeController.getSiteNodeType(creatorId, SiteNodeTypeSort.PIQUET, SiteNodeType.DEFAULT_PIQUET);
		// make sure SiteNodeType.WELL is created
		NodeTypeController.getSiteNodeType(creatorId, SiteNodeTypeSort.WELL, SiteNodeType.DEFAULT_WELL);
		// make sure SiteNodeType.CABLE_INLET is created
		NodeTypeController.getSiteNodeType(creatorId, SiteNodeTypeSort.CABLE_INLET, SiteNodeType.DEFAULT_CABLE_INLET);
		// make sure SiteNodeType.UNBOUND is created
		NodeTypeController.getSiteNodeType(creatorId, SiteNodeTypeSort.UNBOUND, SiteNodeType.DEFAULT_UNBOUND);
		// make sure SiteNodeType.CABLE_INLET is created
		NodeTypeController.getSiteNodeType(creatorId, SiteNodeTypeSort.TOWER, SiteNodeType.DEFAULT_TOWER);
	}
*/
	/**
	 * �������� ������ ���� ����� ������� �����.
	 * @return ������ ����� ������� ����� &lt;{@link SiteNodeType}&gt;
	 */
	public static Collection getTopologicalNodeTypes() {
		Collection list = Collections.EMPTY_LIST;
		StorableObjectCondition pTypeCondition = new EquivalentCondition(ObjectEntities.SITENODE_TYPE_CODE);
		
		//todo getTopologicalNodeTypes should get only included libraries
//		Set<Identifier> libIds = new HashSet<Identifier>();
//		for(Iterator iter = map.getMapLibraries().iterator(); iter.hasNext();) {
//			MapLibrary library = (MapLibrary )iter.next();
//			libIds.add(library.getId());
//		}
//		
//		StorableObjectCondition pTypeCondition = new LinkedIdsCondition(libIds, ObjectEntities.SITENODE_TYPE_CODE);

		try {
			list = StorableObjectPool.getStorableObjectsByCondition(
					pTypeCondition,
					true);

			list.remove(getUnboundNodeType());

			for(Iterator it = list.iterator(); it.hasNext();) {
				SiteNodeType mnpe = (SiteNodeType )it.next();
				if(!mnpe.isTopological())
					it.remove();
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * �������� ��� �������������� �������� ���� ({@link SiteNodeType#DEFAULT_UNBOUND}).
	 * @return ��� �������� ����
	 * @throws ApplicationException 
	 */
	public static SiteNodeType getUnboundNodeType() throws ApplicationException {
		return NodeTypeController.getSiteNodeType(SiteNodeType.DEFAULT_UNBOUND);
	}

	public static Identifier getDefaultImageId() {
		return NodeTypeController.defaultImageId;
	}

	public static void createDefaults(Identifier creatorId) throws ApplicationException {
		NodeTypeController.defaultImageId = getImageId(
				creatorId,
				DEFAULT_IMAGE_CODENAME, 
				DEFAULT_IMAGE_FILENAME);
		// TODO Auto-generated method stub
		
	}

}
