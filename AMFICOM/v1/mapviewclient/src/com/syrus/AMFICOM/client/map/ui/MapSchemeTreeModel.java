/**
 * $Id: MapSchemeTreeModel.java,v 1.26 2005/07/25 12:44:09 bass Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: ������� ������������������ �������������������
 *         ���������������� �������� ���������� �����������
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
import com.syrus.AMFICOM.mapview.MapView;
import com.syrus.AMFICOM.scheme.Scheme;
import com.syrus.AMFICOM.scheme.SchemeCableLink;
import com.syrus.AMFICOM.scheme.SchemeElement;
import com.syrus.AMFICOM.scheme.SchemeLink;
import com.syrus.AMFICOM.scheme.SchemePath;
import com.syrus.AMFICOM.scheme.corba.IdlSchemePackage.IdlKind;


/**
 * ������ ������ �������� ��������� ����� � ���������. �������� �������������� 
 * � ������� �������� drag-drop. ��������� ����� ����� ���� �������,
 * � ������ ������������ ������ �������� ���������, �� ��������� �� ���
 * �������� ��������� �� �����, ��� ���� ������ �������� ���� 
 * {@link MapSchemeTreeNode#isTopological()}, ������� ����������, ��� ����� 
 * �������� ��������� �� ����� �����������. �� ����� ���������:
 * 	<li>�������� �������� ������ ��� �����
 *  <li>�������� ����, ���� ��� �� ���� CABLESUBNETWORK
 *  <li>���������� �������� ���������� ����� � ������������ � ��. 1, 2
 * 
 * <br>��������� (*) �������� ��������, ������� ����� �������� �� �����
 * <pre>
 * ��� - "���1 "
 *  |____ "����� 1"
 *  |____ "����� 2"
 * 			|____ ��������� �����
 *  				|____ (*) "����� 3"
 *					|____ "����� 4" CABLESUBNETWORK
 * 							|____ ��������� �����
 * 									|____ (*) "����� 5"
 * 									|____ "����� 6" CABLESUBNETWORK
 *                                  		...
 * 							|____ ��������� ��������
 * 									|____ (*) "e3"
 * 									|____ (*) "e4"
 * 							|____ �����
 * 									|____ "cl1"
 * 									|____ "cl2"
 * 							|____ ��������� �����
 * 									|____ (*) "cl1"
 *  								|____ (*) "cl2"
 * 							|____ ����
 *  		       					|____ "path1"
 * 									|____ "path2"
 * 			|____ ��������� ��������
 * 					|____ (*) "e1"
 * 					|____ (*) "e2"
 * 			|____ �����
 *             		|____ "cl1"
 *             		|____ "cl2"
 * 			|____ ��������� �����
 *             		|____ (*) "cl1"
 *             		|____ (*) "cl2"
 * 			|____ ����
 *             		|____ (*) "path1"
 *             		|____ (*) "path2"
 * </pre>
 * @version $Revision: 1.26 $, $Date: 2005/07/25 12:44:09 $
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

				if(	internalScheme.getKind().value() != IdlKind._CABLE_SUBNETWORK) {
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
							internalScheme.getKind().equals(IdlKind.CABLE_SUBNETWORK), 
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

		if(allowsChildren) {
			treeNode.addChild(buildElementsTree(element, isTopological));
			treeNode.addChild(buildLinksTree(element, isTopological));
		}
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

	private Item buildPathsTree(final Scheme parentScheme, final boolean topological) {
		final MapSchemeTreeNode treeNode = new MapSchemeTreeNode(null, PATH_BRANCH, getObjectName(PATH_BRANCH), true);
		treeNode.setTopological(topological);

		for (final SchemePath schemePath : parentScheme.getTopologicalPaths()) {
			MapSchemeTreeNode childNode;
			if (topological) {
				childNode = new MapSchemeTreeNode(null, schemePath, getObjectName(schemePath), pathIcon, false);
				childNode.setDragDropEnabled(true);
			} else {
				childNode = new MapSchemeTreeNode(null, schemePath, getObjectName(schemePath), false);
			}
			treeNode.addChild(childNode);
		}
		
		return treeNode;
	}
}

/**
 * @author $Author: bass $
 * @version $Revision: 1.26 $, $Date: 2005/07/25 12:44:09 $
 * @module mapviewclient
 * @deprecated Use {@link com.syrus.util.WrapperComparator} instead.
 */
@Deprecated
final class SchemeComparator implements Comparator<Scheme> {
	public int compare(final Scheme scheme1, final Scheme scheme2) {
		return scheme1.getName().compareTo(scheme2.getName());
	}

	@Override
	public boolean equals(final Object obj) {
		return (obj instanceof SchemeComparator);
	}
}
