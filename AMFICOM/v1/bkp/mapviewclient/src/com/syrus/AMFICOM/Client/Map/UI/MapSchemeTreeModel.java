/**
 * $Id: MapSchemeTreeModel.java,v 1.16 2005/04/19 09:01:49 bass Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Map.UI;

import java.awt.Image;
import java.awt.Toolkit;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.swing.ImageIcon;

import com.syrus.AMFICOM.Client.General.Lang.LangModelMap;
import com.syrus.AMFICOM.client_.general.ui_.tree_.IconedNode;
import com.syrus.AMFICOM.logic.Item;
import com.syrus.AMFICOM.mapview.MapView;
import com.syrus.AMFICOM.scheme.Scheme;
import com.syrus.AMFICOM.scheme.SchemeCableLink;
import com.syrus.AMFICOM.scheme.SchemeElement;
import com.syrus.AMFICOM.scheme.SchemeLink;
import com.syrus.AMFICOM.scheme.SchemePath;
import com.syrus.AMFICOM.scheme.SchemeUtils;
import com.syrus.AMFICOM.scheme.corba.Scheme_TransferablePackage.Kind;


/**
 * Модель дерева привязки элементов схемы к топологии. Привязка осуществляется 
 * с помощью операций drag-drop. Поскольку схемы могут быть вложены,
 * в дереве присутствует полная иерархия элементов, но поскольку не все
 * элементы наносятся на карту, для узла дерева вводится флаг 
 * {@link MapSchemeTreeNode#isTopological()}, который обозначает, что ветвь 
 * содержит наносимые на карту подэлементы. На карту наносятся:
 * 	<li>Элементы верхнего уровня для схемы
 *  <li>Элементы схем, если они не типа CABLESUBNETWORK
 *  <li>Внутренние элементы вложеннной схемы в соответствии с пп. 1, 2
 * 
 * <br>Структура (*) этмечены элементы, которые можно наносить на карты
 * <pre>
 * Вид - "вид1 "
 *  |____ "Схема 1"
 *  |____ "Схема 2"
 * 			|____ Вложенные схемы
 *  				|____ (*) "Схема 3"
 *					|____ "Схема 4" CABLESUBNETWORK
 * 							|____ Вложенные схемы
 * 									|____ (*) "Схема 5"
 * 									|____ "Схема 6" CABLESUBNETWORK
 *                                  		...
 * 							|____ Вложенные элементы
 * 									|____ (*) "e3"
 * 									|____ (*) "e4"
 * 							|____ Линии
 * 									|____ "cl1"
 * 									|____ "cl2"
 * 							|____ Кабельные линии
 * 									|____ (*) "cl1"
 *  								|____ (*) "cl2"
 * 							|____ Пути
 *  		       					|____ "path1"
 * 									|____ "path2"
 * 			|____ Вложенные элементы
 * 					|____ (*) "e1"
 * 					|____ (*) "e2"
 * 			|____ Линии
 *             		|____ "cl1"
 *             		|____ "cl2"
 * 			|____ Кабельные линии
 *             		|____ (*) "cl1"
 *             		|____ (*) "cl2"
 * 			|____ Пути
 *             		|____ (*) "path1"
 *             		|____ (*) "path2"
 * </pre>
 * @version $Revision: 1.16 $, $Date: 2005/04/19 09:01:49 $
 * @author $Author: bass $
 * @module mapviewclient_v1
 */
public class MapSchemeTreeModel {
	public static final String SCHEME_BRANCH = "innerschemes";
	public static final String ELEMENT_BRANCH = "schemeelements";
	public static final String LINK_BRANCH = "schemelinks";
	public static final String CABLE_BRANCH = "schemecablelinks";
	public static final String PATH_BRANCH = "schemepaths";
	public static final String NONAME_BRANCH = "noname";

	MapView mapView;

	Item root;

	static final int IMG_SIZE = 16;

	static ImageIcon placedSchemeIcon = new ImageIcon(Toolkit.getDefaultToolkit()
			.getImage("images/placedscheme.gif").getScaledInstance(
					IMG_SIZE,
					IMG_SIZE,
					Image.SCALE_SMOOTH));

	static ImageIcon schemeIcon = new ImageIcon(Toolkit.getDefaultToolkit()
			.getImage("images/scheme.gif").getScaledInstance(
					IMG_SIZE,
					IMG_SIZE,
					Image.SCALE_SMOOTH));

	static ImageIcon placedElementIcon = new ImageIcon(Toolkit.getDefaultToolkit()
			.getImage("images/placedelement.gif").getScaledInstance(
					IMG_SIZE,
					IMG_SIZE,
					Image.SCALE_SMOOTH));

	static ImageIcon elementIcon = new ImageIcon(Toolkit.getDefaultToolkit()
			.getImage("images/device.gif").getScaledInstance(
					IMG_SIZE,
					IMG_SIZE,
					Image.SCALE_SMOOTH));

	static ImageIcon cableIcon = new ImageIcon(Toolkit.getDefaultToolkit()
			.getImage("images/linkmode.gif").getScaledInstance(
					IMG_SIZE,
					IMG_SIZE,
					Image.SCALE_SMOOTH));

	static ImageIcon pathIcon = new ImageIcon(Toolkit.getDefaultToolkit()
			.getImage("images/pathmode.gif").getScaledInstance(
					IMG_SIZE,
					IMG_SIZE,
					Image.SCALE_SMOOTH));

	public MapSchemeTreeModel(Item root)
	{
		this.root = root;
	}
	
	public void setMapView(MapView mapView)
	{
		this.mapView = mapView;
		buildTree(mapView);
	}

	public Item getRoot() {
		return this.root;
	}

	public String getObjectName(Object object) {
		if(object instanceof String)
			return LangModelMap.getString((String )object);
		else
		if(object instanceof Scheme) {
			Scheme scheme = (Scheme )object;
			return scheme.getName();
		}
		else
		if(object instanceof SchemeElement) {
			SchemeElement schemeElement = (SchemeElement )object;
			return schemeElement.getName();
		}
		else
		if(object instanceof SchemeLink) {
			SchemeLink schemeLink = (SchemeLink )object;
			return schemeLink.getName();
		}
		else
		if(object instanceof SchemeCableLink) {
			SchemeCableLink schemeCableLink = (SchemeCableLink )object;
			return schemeCableLink.getName();
		}
		else
			if(object instanceof SchemePath) {
				SchemePath schemePath = (SchemePath )object;
				return schemePath.getName();
			}
		return LangModelMap.getString(NONAME_BRANCH);
	}

	public void buildTree(MapView mapView)
	{
		this.mapView = mapView;

		Item root = getRoot();
		root.getChildren().clear();

		Set schemes = this.mapView.getSchemes();

		for(Iterator it = schemes.iterator(); it.hasNext();)
		{
			Scheme scheme = (Scheme )it.next();
			
			root.addChild(buildSchemeTree(scheme, false, true));
		}
	}
	
	Item buildSchemeTree(Scheme scheme, boolean isDragDropEnabled, boolean isTopological) {
		
		MapSchemeTreeNode treeNode = new MapSchemeTreeNode(
					scheme, 
					scheme.getName(), 
					(isDragDropEnabled) 
						? placedSchemeIcon 
						: schemeIcon,
					true);
		treeNode.setTopological(isTopological);

		treeNode.addChild(buildInternalSchemesTree(scheme, isTopological));
		treeNode.addChild(buildElementsTree(scheme, isTopological));
		treeNode.addChild(buildLinksTree(scheme, isTopological));
		treeNode.addChild(buildCablesTree(scheme, isTopological));
		treeNode.addChild(buildPathsTree(scheme, isTopological));

		return treeNode;
	}

	Item buildInternalSchemesTree(Scheme parentScheme, boolean isTopological) {
		
		MapSchemeTreeNode treeNode = new MapSchemeTreeNode(SCHEME_BRANCH, getObjectName(SCHEME_BRANCH), true);
		MapSchemeTreeNode childNode;
		treeNode.setTopological(isTopological);

		List compoundElements = new LinkedList();
		for (final Iterator schemeElementIterator = parentScheme.getSchemeElements().iterator(); schemeElementIterator.hasNext();)
		{
			final SchemeElement schemeElement = (SchemeElement) schemeElementIterator.next();
			if (schemeElement.getScheme() != null)
				compoundElements.add(schemeElement);
		}
		
		if (compoundElements.size() > 0)
		{
			for(Iterator it = compoundElements.iterator(); it.hasNext();)
			{
				SchemeElement schemeElement = (SchemeElement )it.next();
				Scheme internalScheme = schemeElement.getScheme();

				if(	internalScheme.getKind().value() != Kind._CABLE_SUBNETWORK) {
					if(isTopological)
						childNode = (MapSchemeTreeNode )buildSchemeTree(internalScheme, true, false);
					else
						childNode = (MapSchemeTreeNode )buildSchemeTree(internalScheme, false, false);
				}
				else
					childNode = (MapSchemeTreeNode )buildSchemeTree(internalScheme, false, isTopological);
				treeNode.addChild(childNode);
			}
		}
		
		return treeNode;
	}

	Item buildElementsTree(Scheme scheme, boolean isTopological) {
		
		MapSchemeTreeNode treeNode = new MapSchemeTreeNode(ELEMENT_BRANCH, getObjectName(ELEMENT_BRANCH), true);
		MapSchemeTreeNode childNode;
		treeNode.setTopological(isTopological);

		Set compoundElements = scheme.getSchemeElements();

		if (compoundElements.size() > 0)
		{
			for(Iterator it = compoundElements.iterator(); it.hasNext();)
			{
				SchemeElement element = (SchemeElement)it.next();
				boolean allowsChildren = (element.getSchemeLinks().size() != 0 || element.getSchemeElements().size() != 0);

				Scheme internalScheme = element.getScheme();
				
				if (internalScheme != null)
				{
					childNode = (MapSchemeTreeNode )buildSchemeTree(internalScheme, internalScheme.getKind().equals(Kind.CABLE_SUBNETWORK), isTopological); 
				}
				else
				if (element.getScheme() == null && isTopological)
				{
					childNode = (MapSchemeTreeNode )buildElementTree(element, true, isTopological, allowsChildren); 
				}
				else
					childNode = (MapSchemeTreeNode )buildElementTree(element, false, isTopological, allowsChildren); 
				treeNode.addChild(childNode);
			}
		}

		return treeNode;
	}
	
	Item buildElementTree(SchemeElement element, boolean isDragDropEnabled, boolean isTopological, boolean allowsChildren) {
		MapSchemeTreeNode treeNode = new MapSchemeTreeNode(
				element, 
				getObjectName(element), 
				(isDragDropEnabled) 
					? placedElementIcon 
					: elementIcon,
				allowsChildren);
		treeNode.setTopological(isTopological);

		treeNode.addChild(buildElementsTree(element, isTopological));//new MapSchemeTreeNode(ELEMENT_BRANCH, "Вложенные элементы", true));
		treeNode.addChild(buildLinksTree(element, isTopological));//new MapSchemeTreeNode(LINK_BRANCH, "Линии", true));

		return treeNode;
	}

	Item buildElementsTree(SchemeElement schemeElement, boolean isTopological) {
		
		MapSchemeTreeNode treeNode = new MapSchemeTreeNode(ELEMENT_BRANCH, getObjectName(ELEMENT_BRANCH), true);
		MapSchemeTreeNode childNode;
		treeNode.setTopological(isTopological);

		Set compoundElements = schemeElement.getSchemeElements();

		if (compoundElements.size() > 0)
		{
			for(Iterator it = compoundElements.iterator(); it.hasNext();)
			{
				SchemeElement element = (SchemeElement)it.next();
				boolean allowsChildren = (element.getSchemeLinks().size() != 0 || element.getSchemeElements().size() != 0);

				if (element.getScheme() == null && isTopological)
				{
					childNode = (MapSchemeTreeNode )buildElementTree(element, true, isTopological, allowsChildren); 
				}
				else
					childNode = (MapSchemeTreeNode )buildElementTree(element, false, isTopological, allowsChildren); 
				treeNode.addChild(childNode);
			}
		}

		return treeNode;
	}

	Item buildLinksTree(SchemeElement schemeElement, boolean isTopological) {
		
		MapSchemeTreeNode treeNode = new MapSchemeTreeNode(LINK_BRANCH, getObjectName(LINK_BRANCH), true);
		MapSchemeTreeNode childNode;
		treeNode.setTopological(isTopological);

		for(Iterator iter = schemeElement.getSchemeLinks().iterator(); iter.hasNext();) {
			SchemeLink schemeLink = (SchemeLink )iter.next();
			childNode = new MapSchemeTreeNode(schemeLink, getObjectName(schemeLink), false);
			treeNode.addChild(childNode);
		}
		
		return treeNode;
	}

	Item buildLinksTree(Scheme scheme, boolean isTopological) {
		
		MapSchemeTreeNode treeNode = new MapSchemeTreeNode(LINK_BRANCH, getObjectName(LINK_BRANCH), true);
		MapSchemeTreeNode childNode;
		treeNode.setTopological(isTopological);

		for(Iterator iter = scheme.getSchemeLinks().iterator(); iter.hasNext();) {
			SchemeLink schemeLink = (SchemeLink )iter.next();
			childNode = new MapSchemeTreeNode(schemeLink, getObjectName(schemeLink), false);
			treeNode.addChild(childNode);
		}
		
		return treeNode;
	}

	Item buildCablesTree(Scheme parentScheme, boolean isTopological) {
		
		MapSchemeTreeNode treeNode = new MapSchemeTreeNode(CABLE_BRANCH, getObjectName(CABLE_BRANCH), true);
		MapSchemeTreeNode childNode;
		treeNode.setTopological(isTopological);

		for (final Iterator schemeCableLinkIterator = parentScheme.getSchemeCableLinks().iterator(); schemeCableLinkIterator.hasNext();)
		{
			final SchemeCableLink schemeCableLink = (SchemeCableLink) schemeCableLinkIterator.next();
			if(isTopological)
			{
				childNode = new MapSchemeTreeNode(schemeCableLink, getObjectName(schemeCableLink), cableIcon, false);
				childNode.setDragDropEnabled(true);
			}
			else
				childNode = new MapSchemeTreeNode(schemeCableLink, getObjectName(schemeCableLink), false);
			treeNode.addChild(childNode);
		}
		return treeNode;
	}

	Item buildPathsTree(Scheme parentScheme, boolean isTopological) {
		
		MapSchemeTreeNode treeNode = new MapSchemeTreeNode(PATH_BRANCH, getObjectName(PATH_BRANCH), true);
		MapSchemeTreeNode childNode;
		treeNode.setTopological(isTopological);

		for(Iterator it = SchemeUtils.getTopologicalPaths(parentScheme).iterator(); it.hasNext();)
		{
			SchemePath schemePath = (SchemePath )it.next();
			if(isTopological)
			{
				childNode = new MapSchemeTreeNode(schemePath, getObjectName(schemePath), pathIcon, false);
				childNode.setDragDropEnabled(true);
			}
			else
				childNode = new MapSchemeTreeNode(schemePath, getObjectName(schemePath), false);
			treeNode.addChild(childNode);
		}
		
		return treeNode;
	}

	private class MapSchemeTreeNode extends IconedNode
	{
		boolean topological = false;
		boolean dragDropEnabled = false;
		
		public MapSchemeTreeNode(Object obj, String name)
		{
			super (obj, name);
		}
	
		public MapSchemeTreeNode(Object obj, String name, boolean allowsChildren)
		{
			super(obj, name, allowsChildren);
		}
	
		public MapSchemeTreeNode(Object obj, String name, ImageIcon ii)
		{
			super(obj, name, ii);
		}
	
		public MapSchemeTreeNode(Object obj, String name, ImageIcon ii, boolean allowsChildren)
		{
			super(obj, name, ii, allowsChildren);
		}
	
		public void setTopological(boolean t)
		{
			this.topological = t;
		}
		
		public boolean isTopological()
		{
			return this.topological;
		}

		public boolean isDragDropEnabled() {
			return this.dragDropEnabled;
		}

		public void setDragDropEnabled(boolean dragDropEnabled) {
			this.dragDropEnabled = dragDropEnabled;
		}
	}
}

