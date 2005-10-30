/*-
 * $$Id: MapSchemeTreeModel.java,v 1.43 2005/10/30 15:20:32 bass Exp $$
 *
 * Copyright 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.map.ui;

import static java.util.logging.Level.SEVERE;

import java.awt.Image;
import java.awt.Toolkit;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.swing.ImageIcon;

import com.syrus.AMFICOM.client.resource.I18N;
import com.syrus.AMFICOM.client.resource.MapEditorResourceKeys;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.logic.ChildrenFactory;
import com.syrus.AMFICOM.logic.Item;
import com.syrus.AMFICOM.mapview.MapView;
import com.syrus.AMFICOM.scheme.Scheme;
import com.syrus.AMFICOM.scheme.SchemeCableLink;
import com.syrus.AMFICOM.scheme.SchemeElement;
import com.syrus.AMFICOM.scheme.SchemeLink;
import com.syrus.AMFICOM.scheme.SchemePath;
import com.syrus.AMFICOM.scheme.SchemeWrapper;
import com.syrus.AMFICOM.scheme.corba.IdlSchemePackage.IdlKind;
import com.syrus.util.Log;
import com.syrus.util.WrapperComparator;


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
 * 
 * @version $Revision: 1.43 $, $Date: 2005/10/30 15:20:32 $
 * @author $Author: bass $
 * @author Andrei Kroupennikov
 * @module mapviewclient
 */
public class MapSchemeTreeModel 
		implements ChildrenFactory {

	public static final String SCHEME_BRANCH = MapEditorResourceKeys.TREE_INNER_SCHEMES;
	public static final String ELEMENT_BRANCH = MapEditorResourceKeys.TREE_SCHEME_ELEMENTS;
	public static final String LINK_BRANCH = MapEditorResourceKeys.TREE_SCHEME_LINKS;
	public static final String CABLE_BRANCH = MapEditorResourceKeys.TREE_SCHEME_CABLE_LINKS;
	public static final String PATH_BRANCH = MapEditorResourceKeys.TREE_SCHEME_PATHS;
	MapView mapView;

	Item root;

	public static Comparator schemeComparator = new WrapperComparator(
			SchemeWrapper.getInstance(),
			StorableObjectWrapper.COLUMN_NAME);

	static final int IMG_SIZE = 16;

	static ImageIcon placedSchemeIcon = new ImageIcon(Toolkit.getDefaultToolkit()
			.getImage("images/placedscheme.gif").getScaledInstance( //$NON-NLS-1$
					IMG_SIZE,
					IMG_SIZE,
					Image.SCALE_SMOOTH));

	static ImageIcon schemeIcon = new ImageIcon(Toolkit.getDefaultToolkit()
			.getImage("images/scheme.gif").getScaledInstance( //$NON-NLS-1$
					IMG_SIZE,
					IMG_SIZE,
					Image.SCALE_SMOOTH));

	static ImageIcon placedElementIcon = new ImageIcon(Toolkit.getDefaultToolkit()
			.getImage("images/placedelement.gif").getScaledInstance( //$NON-NLS-1$
					IMG_SIZE,
					IMG_SIZE,
					Image.SCALE_SMOOTH));

	static ImageIcon elementIcon = new ImageIcon(Toolkit.getDefaultToolkit()
			.getImage("images/device.gif").getScaledInstance( //$NON-NLS-1$
					IMG_SIZE,
					IMG_SIZE,
					Image.SCALE_SMOOTH));

	static ImageIcon cableIcon = new ImageIcon(Toolkit.getDefaultToolkit()
			.getImage("images/linkmode.gif").getScaledInstance( //$NON-NLS-1$
					IMG_SIZE,
					IMG_SIZE,
					Image.SCALE_SMOOTH));

	static ImageIcon pathIcon = new ImageIcon(Toolkit.getDefaultToolkit()
			.getImage("images/pathmode.gif").getScaledInstance( //$NON-NLS-1$
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
			return I18N.getString((String )object);
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
		return I18N.getString(MapEditorResourceKeys.NONAME);
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

	MapSchemeTreeNode buildSchemeTree(Scheme scheme, boolean isDragDropEnabled, boolean topological) {
		
		MapSchemeTreeNode treeNode = new MapSchemeTreeNode(null,
					scheme, 
					scheme.getName(), 
					(isDragDropEnabled) 
						? placedSchemeIcon 
						: schemeIcon,
					true);
		treeNode.setTopological(topological);

		treeNode.addChild(buildInternalSchemesTree(scheme, topological));
		treeNode.addChild(buildElementsTree(scheme, topological));
		treeNode.addChild(buildLinksTree(scheme, topological));
		treeNode.addChild(buildCablesTree(scheme, topological));
		treeNode.addChild(buildPathsTree(scheme, topological));

		return treeNode;
	}

	Item buildInternalSchemesTree(Scheme parentScheme, boolean topological) {
		try {
			MapSchemeTreeNode treeNode = new MapSchemeTreeNode(null,SCHEME_BRANCH, getObjectName(SCHEME_BRANCH), true);
			MapSchemeTreeNode childNode;
			treeNode.setTopological(topological);
	
			List compoundElements = new LinkedList();
			for (final Iterator schemeElementIterator = parentScheme.getSchemeElements(true).iterator(); schemeElementIterator.hasNext();) {
				final SchemeElement schemeElement = (SchemeElement) 
						schemeElementIterator.next();
				if (schemeElement.getScheme(false) != null)
					compoundElements.add(schemeElement);
			}
			
			if(compoundElements.size() > 0) {
				for(Iterator it = compoundElements.iterator(); it.hasNext();) {
					SchemeElement schemeElement = (SchemeElement )it.next();
					Scheme internalScheme = schemeElement.getScheme(false);
	
					if(	internalScheme.getKind() != IdlKind.CABLE_SUBNETWORK) {
						if(topological)
							childNode = buildSchemeTree(internalScheme, true, false);
						else
							childNode = buildSchemeTree(internalScheme, false, false);
					}
					else
						childNode = buildSchemeTree(internalScheme, false, topological);
					treeNode.addChild(childNode);
				}
			}
		
			return treeNode;
		} catch (final ApplicationException ae) {
			assert Log.debugMessage(ae, SEVERE);
			return null;
		}
	}

	Item buildElementsTree(Scheme scheme, boolean topological) {
		try {
			MapSchemeTreeNode treeNode = new MapSchemeTreeNode(null,ELEMENT_BRANCH, getObjectName(ELEMENT_BRANCH), true);
			MapSchemeTreeNode childNode;
			treeNode.setTopological(topological);
	
			Set compoundElements = scheme.getSchemeElements(true);
	
			if(compoundElements.size() > 0) {
				for(Iterator it = compoundElements.iterator(); it.hasNext();) {
					SchemeElement element = (SchemeElement)it.next();
					boolean allowsChildren = (element.getSchemeLinks(false).size() != 0 || element.getSchemeElements(false).size() != 0);
	
					Scheme internalScheme = element.getScheme(false);
					
					if(internalScheme != null) {
						continue;
//						childNode = buildSchemeTree(
//								internalScheme, 
//								internalScheme.getKind().equals(IdlKind.CABLE_SUBNETWORK), 
//								topological); 
					}
					else if(topological) {
						childNode = (MapSchemeTreeNode )buildElementTree(element, true, false, allowsChildren); 
					}
					else
						childNode = (MapSchemeTreeNode )buildElementTree(element, false, false, allowsChildren); 
					treeNode.addChild(childNode);
				}
			}
	
			return treeNode;
		} catch (final ApplicationException ae) {
			assert Log.debugMessage(ae, SEVERE);
			return null;
		}
	}
	
	Item buildElementTree(SchemeElement element, boolean isDragDropEnabled, boolean topological, boolean allowsChildren) {
		MapSchemeTreeNode treeNode = new MapSchemeTreeNode(null,
				element, 
				getObjectName(element), 
				(isDragDropEnabled) 
					? placedElementIcon 
					: elementIcon,
				allowsChildren);
		treeNode.setTopological(topological);

		if(allowsChildren) {
			treeNode.addChild(buildElementsTree(element, topological));
			treeNode.addChild(buildLinksTree(element, topological));
		}
		return treeNode;
	}

	Item buildElementsTree(SchemeElement schemeElement, boolean topological) {
		try {
			MapSchemeTreeNode treeNode = new MapSchemeTreeNode(
					null, ELEMENT_BRANCH,
					getObjectName(ELEMENT_BRANCH), true);
			MapSchemeTreeNode childNode;
			treeNode.setTopological(topological);

			Set compoundElements = schemeElement.getSchemeElements(false);

			if (compoundElements.size() > 0) {
				for (Iterator it = compoundElements.iterator(); it.hasNext();) {
					SchemeElement element = (SchemeElement) it.next();
					boolean allowsChildren = (
							element.getSchemeLinks(false).size() != 0 
							|| element.getSchemeElements(false).size() != 0);

					Scheme internalScheme = element.getScheme(false);
					if (internalScheme != null) {
						continue;
					} else if (topological) {
						childNode = (MapSchemeTreeNode) buildElementTree(
								element, 
								true,
								topological,
								allowsChildren);
					} else
						childNode = (MapSchemeTreeNode) buildElementTree(
								element, 
								false,
								topological,
								allowsChildren);
					treeNode.addChild(childNode);
				}
			}

			return treeNode;
		} catch (final ApplicationException ae) {
			assert Log.debugMessage(ae, SEVERE);
			return null;
		}
	}

	Item buildLinksTree(SchemeElement schemeElement, boolean topological) {
		
		try {
			MapSchemeTreeNode treeNode = new MapSchemeTreeNode(null,LINK_BRANCH, getObjectName(LINK_BRANCH), true);
			MapSchemeTreeNode childNode;
			treeNode.setTopological(topological);

			for(Iterator iter = schemeElement.getSchemeLinks(false).iterator(); iter.hasNext();) {
				SchemeLink schemeLink = (SchemeLink )iter.next();
				childNode = new MapSchemeTreeNode(null,schemeLink, getObjectName(schemeLink), false);
				treeNode.addChild(childNode);
			}
			
			return treeNode;
		} catch(ApplicationException e) {
			assert Log.debugMessage(e, SEVERE);
			return null;
		}
	}

	Item buildLinksTree(Scheme scheme, boolean topological) {
		
		MapSchemeTreeNode treeNode = new MapSchemeTreeNode(null,LINK_BRANCH, getObjectName(LINK_BRANCH), true);
		MapSchemeTreeNode childNode;
		treeNode.setTopological(topological);

		try {
			for(Iterator iter = scheme.getSchemeLinks(true).iterator(); iter.hasNext();) {
				SchemeLink schemeLink = (SchemeLink )iter.next();
				childNode = new MapSchemeTreeNode(null,schemeLink, getObjectName(schemeLink), false);
				treeNode.addChild(childNode);
			}
		} catch(ApplicationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return treeNode;
	}

	Item buildCablesTree(Scheme parentScheme, boolean topological) {
		
		MapSchemeTreeNode treeNode = new MapSchemeTreeNode(null,CABLE_BRANCH, getObjectName(CABLE_BRANCH), true);
		MapSchemeTreeNode childNode;
		treeNode.setTopological(topological);

		try {
			for (final Iterator schemeCableLinkIterator = parentScheme.getSchemeCableLinks(true).iterator(); schemeCableLinkIterator.hasNext();) {
				final SchemeCableLink schemeCableLink = (SchemeCableLink) schemeCableLinkIterator.next();
				if(topological) {
					childNode = new MapSchemeTreeNode(null,schemeCableLink, getObjectName(schemeCableLink), cableIcon, false);
					childNode.setDragDropEnabled(true);
				}
				else
					childNode = new MapSchemeTreeNode(null,schemeCableLink, getObjectName(schemeCableLink), false);
				treeNode.addChild(childNode);
			}
		} catch(ApplicationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return treeNode;
	}

	private Item buildPathsTree(final Scheme parentScheme, final boolean topological) {
		final MapSchemeTreeNode treeNode = new MapSchemeTreeNode(null, PATH_BRANCH, getObjectName(PATH_BRANCH), true);
		treeNode.setTopological(topological);

		try {
			for (final SchemePath schemePath : parentScheme.getTopologicalSchemePathsRecursively(false)) {
				MapSchemeTreeNode childNode;
				if (topological) {
					childNode = new MapSchemeTreeNode(null, schemePath, getObjectName(schemePath), pathIcon, false);
					childNode.setDragDropEnabled(true);
				} else {
					childNode = new MapSchemeTreeNode(null, schemePath, getObjectName(schemePath), false);
				}
				treeNode.addChild(childNode);
			}
		} catch(ApplicationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return treeNode;
	}
}
