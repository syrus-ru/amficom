/**
 * $Id: MapSchemeTreeModel.java,v 1.9 2005/02/10 11:48:39 krupenn Exp $
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
import com.syrus.AMFICOM.scheme.SchemeUtils;
import com.syrus.AMFICOM.scheme.corba.Scheme;
import com.syrus.AMFICOM.scheme.corba.SchemeCableLink;
import com.syrus.AMFICOM.scheme.corba.SchemeElement;
import com.syrus.AMFICOM.scheme.corba.SchemeLink;
import com.syrus.AMFICOM.scheme.corba.SchemePath;
import com.syrus.AMFICOM.scheme.corba.SchemePackage.Type;


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
 * @version $Revision: 1.9 $, $Date: 2005/02/10 11:48:39 $
 * @author $Author: krupenn $
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
				for (int i  = 0; i < parentScheme.schemeElements().length; i++)
				{
					SchemeElement schemeElement = parentScheme.schemeElements()[i];
					if (schemeElement.internalScheme() != null)
					{
						compoundElements.add(schemeElement);
					}
				}
				
				if (compoundElements.size() > 0)
				{
					for(Iterator it = compoundElements.iterator(); it.hasNext();)
					{
						SchemeElement schemeElement = (SchemeElement )it.next();
						Scheme internalScheme = schemeElement.internalScheme();

						if(	internalScheme.type().value() != Type._CABLE_SUBNETWORK)
						{
							if(parentNode.isTopological())
							{
								treeNode = new MapSchemeTreeNode(
									schemeElement,
									internalScheme.name(), 
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
									internalScheme.name(), 
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
									internalScheme.name(), 
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
						&& ((SchemeElement )parentObject).internalScheme() != null)
					)
				{
					Scheme scheme;
					if(parentObject instanceof Scheme)
						scheme = (Scheme )parentObject;
					else
					{
						SchemeElement schemeElement = (SchemeElement )parentObject;
						scheme = schemeElement.internalScheme();
					}
					
					for (int i = 0; i < scheme.schemeElements().length; i++)
					{
						SchemeElement element = scheme.schemeElements()[i];
						if (element.internalScheme() == null)
							compoundElements.add(element);
					}
				}
				else if (parentObject instanceof SchemeElement)
				{
					SchemeElement schemeElement = (SchemeElement )parentObject;
					for (int i = 0; i < schemeElement.schemeElements().length; i++)
					{
						SchemeElement element = schemeElement.schemeElements()[i];
						if (element != null)
							compoundElements.add(element);
					}
				}
				
				if (compoundElements.size() > 0)
				{
					for(Iterator it = compoundElements.iterator(); it.hasNext();)
					{
						SchemeElement element = (SchemeElement)it.next();
						boolean isFinal = (element.schemeLinks().length == 0 || element.schemeElements().length == 0);

						if (element.internalScheme() == null
							&& parentNode.isTopological())
						{
							treeNode = new MapSchemeTreeNode(
									element, 
									element.name(), 
									true, 
									new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/placedelement.gif")),
									isFinal);
							treeNode.setDragDropEnabled(true);
						}
						else
							treeNode = new MapSchemeTreeNode(
									element, 
									element.name(), 
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
						&& ((SchemeElement )parentObject).internalScheme() != null)
					)
				{
					Scheme scheme;
					if(parentObject instanceof Scheme)
						scheme = (Scheme )parentObject;
					else
					{
						SchemeElement schemeElement = (SchemeElement )parentObject;
						scheme = schemeElement.internalScheme();
					}
					
					for(int i = 0 ; i < scheme.schemeLinks().length; i++)
					{
						SchemeLink schemeLink = scheme.schemeLinks()[i];
						childNodes.add(new MapSchemeTreeNode(schemeLink, schemeLink.name(), true, true));
					}
				}
				else if (parentObject instanceof SchemeElement)
				{
					SchemeElement schemeElement = (SchemeElement )parentObject;
					for(int i = 0; i < schemeElement.schemeLinks().length; i++)
					{
						SchemeLink schemeLink = schemeElement.schemeLinks()[i];
						childNodes.add(new MapSchemeTreeNode(schemeLink, schemeLink.name(), true, true));
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
					parentScheme = schemeElement.internalScheme();
				}
					
				for(int i = 0 ; i < parentScheme.schemeCableLinks().length; i++)
				{
					SchemeCableLink schemeCableLink = parentScheme.schemeCableLinks()[i];
					if(parentNode.isTopological())
					{
						treeNode = new MapSchemeTreeNode(
								schemeCableLink, 
								schemeCableLink.name(), 
								true, 
								new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/linkmode.gif")),
								true);
						treeNode.setDragDropEnabled(true);
					}
					else
					{
						treeNode = new MapSchemeTreeNode(
								schemeCableLink, 
								schemeCableLink.name(), 
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
					parentScheme = schemeElement.internalScheme();
				}
					
				for(Iterator it = SchemeUtils.getTopologicalPaths(parentScheme).iterator(); it.hasNext();)
				{
					SchemePath schemePath = (SchemePath )it.next();
					if(parentNode.isTopological())
					{
						treeNode = new MapSchemeTreeNode(
								schemePath, 
								schemePath.name(), 
								true, 
								new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/pathmode.gif")),
								true);
						treeNode.setDragDropEnabled(true);
					}
					else
					{
						treeNode = new MapSchemeTreeNode(
								schemePath, 
								schemePath.name(), 
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
							scheme.name(), 
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
					&& ((SchemeElement )(objectResourceTreeNode.getObject())).internalScheme() != null)
				)
			{
				Scheme scheme;
				if(objectResourceTreeNode.getObject() instanceof Scheme)
					scheme = (Scheme )objectResourceTreeNode.getObject();
				else
				{
					SchemeElement schemeElement = (SchemeElement )objectResourceTreeNode.getObject();
					scheme = schemeElement.internalScheme();
				}

				if (scheme.schemeElements().length != 0)
				{
					boolean hasSchemes = false;
					boolean hasElements = false;
					for (int i = 0; i < scheme.schemeElements().length; i++)
					{
						SchemeElement schemeElement = scheme.schemeElements()[i];
						if (schemeElement.internalScheme() == null)
						{
							hasElements = true;
							break;
						}
					}
					
					for (int i = 0; i < scheme.schemeElements().length; i++)
					{
						SchemeElement schemeElement = scheme.schemeElements()[i];
						if (schemeElement.internalScheme() != null)
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
				if (scheme.schemeLinks().length != 0)
					childNodes.add(new MapSchemeTreeNode(LINK_BRANCH, "Линии", true));
				if (scheme.schemeCableLinks().length != 0)
					childNodes.add(new MapSchemeTreeNode(CABLE_BRANCH, "Кабели", true));
				if (!SchemeUtils.getTopologicalPaths(scheme).isEmpty())
					childNodes.add(new MapSchemeTreeNode(PATH_BRANCH, "Пути", true));
			}
			else if(objectResourceTreeNode.getObject() instanceof SchemeElement)
			{
				SchemeElement schemeElement = (SchemeElement )objectResourceTreeNode.getObject();
				if (schemeElement.internalScheme() != null)
				{
					Scheme scheme = schemeElement.internalScheme();
					for (int i = 0 ; i < scheme.schemeElements().length; i++)
					{
						SchemeElement internalElement = scheme.schemeElements()[i];
						if (internalElement.internalScheme() == null)
						{
							childNodes.add(new MapSchemeTreeNode(
									internalElement, 
									internalElement.name(), 
									true, 
									new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/device.gif")), 
									true));
						}
						else
						{
							childNodes.add(new MapSchemeTreeNode(
									internalElement, 
									internalElement.name(), 
									true,
									new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/scheme.gif")), 
									true));
						}
					}
				}
				else
				{
					if (schemeElement.schemeElements().length != 0)
						childNodes.add(new MapSchemeTreeNode(ELEMENT_BRANCH, "Вложенные элементы", true));
				 if (schemeElement.schemeLinks().length != 0)
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

