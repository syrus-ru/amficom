/**
 * $Id: MapSchemeTreeModel.java,v 1.13 2005/03/24 17:03:16 bass Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Map.UI;

import java.awt.Color;
import java.awt.Image;
import java.awt.Toolkit;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.swing.ImageIcon;

import com.syrus.AMFICOM.Client.General.UI.ObjectResourceTreeModel;
import com.syrus.AMFICOM.Client.General.UI.ObjectResourceTreeNode;
import com.syrus.AMFICOM.mapview.MapView;
import com.syrus.AMFICOM.scheme.*;
import com.syrus.AMFICOM.scheme.SchemeUtils;
import com.syrus.AMFICOM.scheme.corba.SchemeKind;


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
 * @version $Revision: 1.13 $, $Date: 2005/03/24 17:03:16 $
 * @author $Author: bass $
 * @module mapviewclient_v1
 */
public class MapSchemeTreeModel extends ObjectResourceTreeModel
{
	public static final String SCHEME_BRANCH = "scheme";
	public static final String ELEMENT_BRANCH = "schemeelement";
	public static final String LINK_BRANCH = "schemelink";
	public static final String CABLE_BRANCH = "schemecablelink";
	public static final String PATH_BRANCH = "schemepath";

	MapView mapView;

	public MapSchemeTreeModel(MapView mapView)
	{
		this.mapView = mapView;
	}
	
	public void setMapView(MapView mapView)
	{
		this.mapView = mapView;
	}

	public ObjectResourceTreeNode getRoot()
	{
		MapSchemeTreeNode root;
		if(this.mapView == null)
			root =  new MapSchemeTreeNode(
				"root", 
				"Без названия", 
				true);
		else
			root =  new MapSchemeTreeNode(
				this.mapView, 
				"Вид - " + this.mapView.getName(), 
				true);
		root.setTopological(true);
		return root;
	}


	public Color getNodeTextColor(ObjectResourceTreeNode node) { return Color.BLACK; }
	public void nodeAfterSelected(ObjectResourceTreeNode node) {/*empty*/ }
	public void nodeBeforeExpanded(ObjectResourceTreeNode node) {/*empty*/ }


	public Class getNodeChildClass(ObjectResourceTreeNode node)
	{
		if(node.getObject() instanceof String)
		{
			String branchString = (String )node.getObject();
			if(branchString.equals(SCHEME_BRANCH))
				return Scheme.class;
			if(branchString.equals(ELEMENT_BRANCH))
				return SchemeElement.class;
			if(branchString.equals(LINK_BRANCH))
				return SchemeLink.class;
			if(branchString.equals(CABLE_BRANCH))
				return SchemeCableLink.class;
			if(branchString.equals(PATH_BRANCH))
				return SchemePath.class;
		}
		else if (node.getObject() instanceof Scheme)
			return Scheme.class;
		else if (node.getObject() instanceof SchemeElement)
			return SchemeElement.class;
		return null;
	}

	public List getChildNodes(ObjectResourceTreeNode objectResourceTreeNode)
	{
		MapSchemeTreeNode treeNode = null;
		MapSchemeTreeNode parentNode = (MapSchemeTreeNode )objectResourceTreeNode.getParent();
		
		List childNodes = new LinkedList();
		if(objectResourceTreeNode.getObject() instanceof String)
		{
			String branchString = (String )objectResourceTreeNode.getObject();
			if (branchString.equals(SCHEME_BRANCH))
			{
				Scheme parentScheme = (Scheme )parentNode.getObject();
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

						if(	internalScheme.getSchemeKind().value() != SchemeKind._CABLE_SUBNETWORK)
						{
							if(parentNode.isTopological())
							{
								treeNode = new MapSchemeTreeNode(
									schemeElement,
									internalScheme.getName(), 
									true,
									new ImageIcon(Toolkit
										.getDefaultToolkit()
										.getImage("images/placedscheme.gif")
										.getScaledInstance(
											16, 
											16, 
											Image.SCALE_SMOOTH)));
								treeNode.setDragDropEnabled(true);
							}
							else
							{
								treeNode = new MapSchemeTreeNode(
									schemeElement,
									internalScheme.getName(), 
									true,
									new ImageIcon(Toolkit
										.getDefaultToolkit()
										.getImage("images/scheme.gif")
										.getScaledInstance(
											16, 
											16, 
											Image.SCALE_SMOOTH)));
							}
						}
						else
						{
							ImageIcon ii = 
								new ImageIcon(
									Toolkit.getDefaultToolkit()
									.getImage("images/scheme.gif")
									.getScaledInstance(
										16, 
										16, 
										Image.SCALE_SMOOTH));
							treeNode = new MapSchemeTreeNode(
									schemeElement,
									internalScheme.getName(), 
									true,
									ii);
							treeNode.setTopological(parentNode.isTopological());
						}
						childNodes.add(treeNode);
					}
				}
			}
			else 
			if (branchString.equals(ELEMENT_BRANCH))
			{
				Object parentObject = parentNode.getObject();
				List compoundElements = new LinkedList();
				if (parentObject instanceof Scheme
					|| (parentObject instanceof SchemeElement
						&& ((SchemeElement )parentObject).getScheme() != null)
					)
				{
					Scheme scheme;
					if(parentObject instanceof Scheme)
						scheme = (Scheme )parentObject;
					else
					{
						SchemeElement schemeElement = (SchemeElement )parentObject;
						scheme = schemeElement.getScheme();
					}
					
					for (int i = 0; i < scheme.getSchemeElementsAsArray().length; i++)
					{
						SchemeElement element = scheme.getSchemeElementsAsArray()[i];
						if (element.getScheme() == null)
							compoundElements.add(element);
					}
				}
				else if (parentObject instanceof SchemeElement)
				{
					SchemeElement schemeElement = (SchemeElement )parentObject;
					for (int i = 0; i < schemeElement.getSchemeElementsAsArray().length; i++)
					{
						SchemeElement element = schemeElement.getSchemeElementsAsArray()[i];
						if (element != null)
							compoundElements.add(element);
					}
				}
				
				if (compoundElements.size() > 0)
				{
					for(Iterator it = compoundElements.iterator(); it.hasNext();)
					{
						SchemeElement element = (SchemeElement)it.next();
						boolean isFinal = (element.getSchemeLinksAsArray().length == 0 || element.getSchemeElementsAsArray().length == 0);

						if (element.getScheme() == null
							&& parentNode.isTopological())
						{
							treeNode = new MapSchemeTreeNode(
									element, 
									element.getName(), 
									true, 
									new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/placedelement.gif")),
									isFinal);
							treeNode.setDragDropEnabled(true);
						}
						else
							treeNode = new MapSchemeTreeNode(
									element, 
									element.getName(), 
									true, 
									new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/device.gif")),
									isFinal);
						childNodes.add(treeNode);
					}
				}
			}
			else if (branchString.equals(LINK_BRANCH))
			{
				Object parentObject = parentNode.getObject();
				if (parentObject instanceof Scheme
					|| (parentObject instanceof SchemeElement
						&& ((SchemeElement )parentObject).getScheme() != null)
					)
				{
					Scheme scheme;
					if(parentObject instanceof Scheme)
						scheme = (Scheme )parentObject;
					else
					{
						SchemeElement schemeElement = (SchemeElement )parentObject;
						scheme = schemeElement.getScheme();
					}
					
					for(int i = 0 ; i < scheme.getSchemeLinksAsArray().length; i++)
					{
						SchemeLink schemeLink = scheme.getSchemeLinksAsArray()[i];
						childNodes.add(new MapSchemeTreeNode(schemeLink, schemeLink.getName(), true, true));
					}
				}
				else if (parentObject instanceof SchemeElement)
				{
					SchemeElement schemeElement = (SchemeElement )parentObject;
					for(int i = 0; i < schemeElement.getSchemeLinksAsArray().length; i++)
					{
						SchemeLink schemeLink = schemeElement.getSchemeLinksAsArray()[i];
						childNodes.add(new MapSchemeTreeNode(schemeLink, schemeLink.getName(), true, true));
					}
				}
			}
			else if (branchString.equals(CABLE_BRANCH))
			{
				Scheme parentScheme;

				if(parentNode.getObject() instanceof Scheme)
					parentScheme = (Scheme )parentNode.getObject();
				else
				{
					SchemeElement schemeElement = (SchemeElement )parentNode.getObject();
					parentScheme = schemeElement.getScheme();
				}
					
				for (final Iterator schemeCableLinkIterator = parentScheme.getSchemeCableLinks().iterator(); schemeCableLinkIterator.hasNext();)
				{
					final SchemeCableLink schemeCableLink = (SchemeCableLink) schemeCableLinkIterator.next();
					if(parentNode.isTopological())
					{
						treeNode = new MapSchemeTreeNode(
								schemeCableLink, 
								schemeCableLink.getName(), 
								true, 
								new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/linkmode.gif")),
								true);
						treeNode.setDragDropEnabled(true);
					}
					else
					{
						treeNode = new MapSchemeTreeNode(
								schemeCableLink, 
								schemeCableLink.getName(), 
								true,
								true);
					}
					childNodes.add(treeNode);
				}
			}
			else if (branchString.equals(PATH_BRANCH))
			{
				Scheme parentScheme;

				if(parentNode.getObject() instanceof Scheme)
					parentScheme = (Scheme )parentNode.getObject();
				else
				{
					SchemeElement schemeElement = (SchemeElement )parentNode.getObject();
					parentScheme = schemeElement.getScheme();
				}
					
				for(Iterator it = SchemeUtils.getTopologicalPaths(parentScheme).iterator(); it.hasNext();)
				{
					SchemePath schemePath = (SchemePath )it.next();
					if(parentNode.isTopological())
					{
						treeNode = new MapSchemeTreeNode(
								schemePath, 
								schemePath.getName(), 
								true, 
								new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/pathmode.gif")),
								true);
						treeNode.setDragDropEnabled(true);
					}
					else
					{
						treeNode = new MapSchemeTreeNode(
								schemePath, 
								schemePath.getName(), 
								true, 
								true);
					}
					childNodes.add(treeNode);
				}
			}
		}
		else
		{
			if(objectResourceTreeNode.getObject() instanceof MapView)
			{
				List schemes = this.mapView.getSchemes();

				for(Iterator it = schemes.iterator(); it.hasNext();)
				{
					Scheme scheme = (Scheme )it.next();

					treeNode = new MapSchemeTreeNode(
							scheme, 
							scheme.getName(), 
							true,
							new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/scheme.gif")));
/*
						nod = new MapSchemeTreeNode(
								sc, 
								sc.getName(), 
								false,
								new ImageIcon(Toolkit.getDefaultToolkit()
									.getImage("images/placedscheme.gif")
									.getScaledInstance(
										16, 
										16, 
										Image.SCALE_SMOOTH)));
*/
					treeNode.setTopological(true);
					childNodes.add(treeNode);
				}
			}
			else
			if(objectResourceTreeNode.getObject() instanceof Scheme
				|| (objectResourceTreeNode.getObject() instanceof SchemeElement
					&& ((SchemeElement )(objectResourceTreeNode.getObject())).getScheme() != null)
				)
			{
				Scheme scheme;
				if(objectResourceTreeNode.getObject() instanceof Scheme)
					scheme = (Scheme )objectResourceTreeNode.getObject();
				else
				{
					SchemeElement schemeElement = (SchemeElement )objectResourceTreeNode.getObject();
					scheme = schemeElement.getScheme();
				}

				if (scheme.getSchemeElementsAsArray().length != 0)
				{
					boolean hasSchemes = false;
					boolean hasElements = false;
					for (int i = 0; i < scheme.getSchemeElementsAsArray().length; i++)
					{
						SchemeElement schemeElement = scheme.getSchemeElementsAsArray()[i];
						if (schemeElement.getScheme() == null)
						{
							hasElements = true;
							break;
						}
					}
					
					for (int i = 0; i < scheme.getSchemeElementsAsArray().length; i++)
					{
						SchemeElement schemeElement = scheme.getSchemeElementsAsArray()[i];
						if (schemeElement.getScheme() != null)
						{
							hasSchemes = true;
							break;
						}
					}
					
					if (hasSchemes)
						childNodes.add(new MapSchemeTreeNode(SCHEME_BRANCH, "Вложенные схемы", true));
					if (hasElements)
						childNodes.add(new MapSchemeTreeNode(ELEMENT_BRANCH, "Вложенные элементы", true));
				}
				if (!scheme.getSchemeLinks().isEmpty())
					childNodes.add(new MapSchemeTreeNode(LINK_BRANCH, "Линии", true));
				if (!scheme.getSchemeCableLinks().isEmpty())
					childNodes.add(new MapSchemeTreeNode(CABLE_BRANCH, "Кабели", true));
				if (!SchemeUtils.getTopologicalPaths(scheme).isEmpty())
					childNodes.add(new MapSchemeTreeNode(PATH_BRANCH, "Пути", true));
			}
			else if(objectResourceTreeNode.getObject() instanceof SchemeElement)
			{
				SchemeElement schemeElement = (SchemeElement )objectResourceTreeNode.getObject();
				if (schemeElement.getScheme() != null)
				{
					Scheme scheme = schemeElement.getScheme();
					for (int i = 0 ; i < scheme.getSchemeElementsAsArray().length; i++)
					{
						SchemeElement internalElement = scheme.getSchemeElementsAsArray()[i];
						if (internalElement.getScheme() == null)
						{
							childNodes.add(new MapSchemeTreeNode(
									internalElement, 
									internalElement.getName(), 
									true, 
									new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/device.gif")), 
									true));
						}
						else
						{
							childNodes.add(new MapSchemeTreeNode(
									internalElement, 
									internalElement.getName(), 
									true,
									new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/scheme.gif")), 
									true));
						}
					}
				}
				else
				{
					if (schemeElement.getSchemeElementsAsArray().length != 0)
						childNodes.add(new MapSchemeTreeNode(ELEMENT_BRANCH, "Вложенные элементы", true));
				 if (schemeElement.getSchemeLinksAsArray().length != 0)
						childNodes.add(new MapSchemeTreeNode(LINK_BRANCH, "Линии", true));
				}
			}
		}
		return childNodes;
	}

	private class MapSchemeTreeNode extends ObjectResourceTreeNode
	{
		boolean topological = false;
		
		public MapSchemeTreeNode(Object obj, String name, boolean enable)
		{
			super (obj, name, enable);
		}
	
		public MapSchemeTreeNode(Object obj, String name, boolean enable, boolean isFinal)
		{
			super(obj, name, enable, isFinal);
		}
	
		public MapSchemeTreeNode(Object obj, String name, boolean enable, ImageIcon ii)
		{
			super (obj, name, enable, ii);
		}
	
		public MapSchemeTreeNode(Object obj, String name, boolean enable, ImageIcon ii, boolean isFinal)
		{
			super(obj, name, enable, ii, isFinal);
		}
	
		public void setTopological(boolean t)
		{
			this.topological = t;
		}
		
		public boolean isTopological()
		{
			return this.topological;
		}
	}
}

