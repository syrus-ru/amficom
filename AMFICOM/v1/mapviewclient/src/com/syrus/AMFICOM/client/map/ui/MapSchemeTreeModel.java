/**
 * $Id: MapSchemeTreeModel.java,v 1.22 2005/06/24 14:13:40 bass Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 */

package com.syrus.AMFICOM.client.map.ui;

import java.awt.Image;
import java.awt.Toolkit;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.swing.ImageIcon;

import com.syrus.AMFICOM.client.resource.LangModelMap;
import com.syrus.AMFICOM.logic.ChildrenFactory;
import com.syrus.AMFICOM.logic.Item;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.mapview.MapView;
import com.syrus.AMFICOM.scheme.Scheme;
import com.syrus.AMFICOM.scheme.SchemeCableLink;
import com.syrus.AMFICOM.scheme.SchemeElement;
import com.syrus.AMFICOM.scheme.SchemeLink;
import com.syrus.AMFICOM.scheme.SchemePath;
import com.syrus.AMFICOM.scheme.SchemeUtils;
import com.syrus.AMFICOM.scheme.corba.IdlSchemePackage.Kind;


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
 * @version $Revision: 1.22 $, $Date: 2005/06/24 14:13:40 $
 * @author $Author: bass $
 * @module mapviewclient_v1
 */
public class MapSchemeTreeModel 
		implements ChildrenFactory {

	public static final String SCHEME_BRANCH = "innerschemes";
	public static final String ELEMENT_BRANCH = "schemeelements";
	public static final String LINK_BRANCH = "schemelinks";
	public static final String CABLE_BRANCH = "schemecablelinks";
	public static final String PATH_BRANCH = "schemepaths";
	public static final String NONAME_BRANCH = "noname";

	MapView mapView;

	Item root;

	public static SchemeComparator schemeComparator = new SchemeComparator();

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

	private static MapSchemeTreeModel instance;
	
	protected MapSchemeTreeModel() {
		// empty
	}
	
	public static MapSchemeTreeModel getInstance() {
		if(instance == null)
			instance = new MapSchemeTreeModel();
		return instance;
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

	public void populate(Item node) {
		if(node.getObject() instanceof Scheme) {
			populateSchemeNode((MapSchemeTreeNode )node);
		}
	}

	public void populateSchemeNode(MapSchemeTreeNode node)
	{
		if(node.isPopulated())
			return;
		
		buildSchemeTree(node);
	}
	
	void buildSchemeTree(MapSchemeTreeNode treeNode) {
		
		Scheme scheme = (Scheme )treeNode.getObject(); 

		treeNode.addChild(buildInternalSchemesTree(scheme, true));
		treeNode.addChild(buildElementsTree(scheme, true));
		treeNode.addChild(buildLinksTree(scheme, true));
		treeNode.addChild(buildCablesTree(scheme, true));
		treeNode.addChild(buildPathsTree(scheme, true));
	}

	MapSchemeTreeNode buildSchemeTree(Scheme scheme, boolean isDragDropEnabled, boolean isTopological) {
		
		MapSchemeTreeNode treeNode = new MapSchemeTreeNode(null,
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
		
		MapSchemeTreeNode treeNode = new MapSchemeTreeNode(null,SCHEME_BRANCH, getObjectName(SCHEME_BRANCH), true);
		MapSchemeTreeNode childNode;
		treeNode.setTopological(isTopological);

		List compoundElements = new LinkedList();
		for (final Iterator schemeElementIterator = parentScheme.getSchemeElements().iterator(); schemeElementIterator.hasNext();) {
			final SchemeElement schemeElement = (SchemeElement) 
					schemeElementIterator.next();
			if (schemeElement.getScheme() != null)
				compoundElements.add(schemeElement);
		}
		
		if(compoundElements.size() > 0) {
			for(Iterator it = compoundElements.iterator(); it.hasNext();) {
				SchemeElement schemeElement = (SchemeElement )it.next();
				Scheme internalScheme = schemeElement.getScheme();

				if(	internalScheme.getKind().value() != Kind._CABLE_SUBNETWORK) {
					if(isTopological)
						childNode = buildSchemeTree(internalScheme, true, false);
					else
						childNode = buildSchemeTree(internalScheme, false, false);
				}
				else
					childNode = buildSchemeTree(internalScheme, false, isTopological);
				treeNode.addChild(childNode);
			}
		}
		
		return treeNode;
	}

	Item buildElementsTree(Scheme scheme, boolean isTopological) {
		
		MapSchemeTreeNode treeNode = new MapSchemeTreeNode(null,ELEMENT_BRANCH, getObjectName(ELEMENT_BRANCH), true);
		MapSchemeTreeNode childNode;
		treeNode.setTopological(isTopological);

		Set compoundElements = scheme.getSchemeElements();

		if(compoundElements.size() > 0) {
			for(Iterator it = compoundElements.iterator(); it.hasNext();) {
				SchemeElement element = (SchemeElement)it.next();
				boolean allowsChildren = (element.getSchemeLinks().size() != 0 || element.getSchemeElements().size() != 0);

				Scheme internalScheme = element.getScheme();
				
				if(internalScheme != null) {
					childNode = buildSchemeTree(
							internalScheme, 
							internalScheme.getKind().equals(Kind.CABLE_SUBNETWORK), 
							isTopological); 
				}
				else if(element.getScheme() == null && isTopological) {
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
		MapSchemeTreeNode treeNode = new MapSchemeTreeNode(null,
				element, 
				getObjectName(element), 
				(isDragDropEnabled) 
					? placedElementIcon 
					: elementIcon,
				allowsChildren);
		treeNode.setTopological(isTopological);

		treeNode.addChild(buildElementsTree(element, isTopological));//new MapSchemeTreeNode(null,ELEMENT_BRANCH, "Вложенные элементы", true));
		treeNode.addChild(buildLinksTree(element, isTopological));//new MapSchemeTreeNode(null,LINK_BRANCH, "Линии", true));

		return treeNode;
	}

	Item buildElementsTree(SchemeElement schemeElement, boolean isTopological) {
		
		MapSchemeTreeNode treeNode = new MapSchemeTreeNode(null,ELEMENT_BRANCH, getObjectName(ELEMENT_BRANCH), true);
		MapSchemeTreeNode childNode;
		treeNode.setTopological(isTopological);

		Set compoundElements = schemeElement.getSchemeElements();

		if(compoundElements.size() > 0) {
			for(Iterator it = compoundElements.iterator(); it.hasNext();) {
				SchemeElement element = (SchemeElement)it.next();
				boolean allowsChildren = (element.getSchemeLinks().size() != 0 || element.getSchemeElements().size() != 0);

				if(element.getScheme() == null && isTopological) {
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
		
		MapSchemeTreeNode treeNode = new MapSchemeTreeNode(null,LINK_BRANCH, getObjectName(LINK_BRANCH), true);
		MapSchemeTreeNode childNode;
		treeNode.setTopological(isTopological);

		for(Iterator iter = schemeElement.getSchemeLinks().iterator(); iter.hasNext();) {
			SchemeLink schemeLink = (SchemeLink )iter.next();
			childNode = new MapSchemeTreeNode(null,schemeLink, getObjectName(schemeLink), false);
			treeNode.addChild(childNode);
		}
		
		return treeNode;
	}

	Item buildLinksTree(Scheme scheme, boolean isTopological) {
		
		MapSchemeTreeNode treeNode = new MapSchemeTreeNode(null,LINK_BRANCH, getObjectName(LINK_BRANCH), true);
		MapSchemeTreeNode childNode;
		treeNode.setTopological(isTopological);

		for(Iterator iter = scheme.getSchemeLinks().iterator(); iter.hasNext();) {
			SchemeLink schemeLink = (SchemeLink )iter.next();
			childNode = new MapSchemeTreeNode(null,schemeLink, getObjectName(schemeLink), false);
			treeNode.addChild(childNode);
		}
		
		return treeNode;
	}

	Item buildCablesTree(Scheme parentScheme, boolean isTopological) {
		
		MapSchemeTreeNode treeNode = new MapSchemeTreeNode(null,CABLE_BRANCH, getObjectName(CABLE_BRANCH), true);
		MapSchemeTreeNode childNode;
		treeNode.setTopological(isTopological);

		for (final Iterator schemeCableLinkIterator = parentScheme.getSchemeCableLinks().iterator(); schemeCableLinkIterator.hasNext();) {
			final SchemeCableLink schemeCableLink = (SchemeCableLink) schemeCableLinkIterator.next();
			if(isTopological) {
				childNode = new MapSchemeTreeNode(null,schemeCableLink, getObjectName(schemeCableLink), cableIcon, false);
				childNode.setDragDropEnabled(true);
			}
			else
				childNode = new MapSchemeTreeNode(null,schemeCableLink, getObjectName(schemeCableLink), false);
			treeNode.addChild(childNode);
		}
		return treeNode;
	}

	Item buildPathsTree(Scheme parentScheme, boolean isTopological) {
		
		MapSchemeTreeNode treeNode = new MapSchemeTreeNode(null,PATH_BRANCH, getObjectName(PATH_BRANCH), true);
		MapSchemeTreeNode childNode;
		treeNode.setTopological(isTopological);

		for(Iterator it = SchemeUtils.getTopologicalPaths(parentScheme).iterator(); it.hasNext();) {
			SchemePath schemePath = (SchemePath )it.next();
			if(isTopological) {
				childNode = new MapSchemeTreeNode(null,schemePath, getObjectName(schemePath), pathIcon, false);
				childNode.setDragDropEnabled(true);
			}
			else
				childNode = new MapSchemeTreeNode(null,schemePath, getObjectName(schemePath), false);
			treeNode.addChild(childNode);
		}
		
		return treeNode;
	}
}

final class SchemeComparator implements Comparator {
	public int compare(Object o1, Object o2) {
		Scheme scheme1 = (Scheme )o1;
		Scheme scheme2 = (Scheme )o2;
		return scheme1.getName().compareTo(scheme2.getName());
	}

	public boolean equals(Object obj) {
		return (obj instanceof SchemeComparator);
	}
}
