/**
 * $Id: MapSchemeTreeModel.java,v 1.8 2005/02/01 11:34:56 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Map.UI;

import com.syrus.AMFICOM.Client.General.UI.ObjectResourceTreeModel;
import com.syrus.AMFICOM.Client.General.UI.ObjectResourceTreeNode;
import com.syrus.AMFICOM.Client.Resource.DataSourceInterface;
import com.syrus.AMFICOM.mapview.MapView;
import com.syrus.AMFICOM.Client.Resource.ObjectResource;
import com.syrus.AMFICOM.Client.Resource.ObjectResourceSorter;
import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.scheme.SchemeUtils;
import com.syrus.AMFICOM.scheme.corba.*;

import com.syrus.AMFICOM.scheme.corba.SchemePackage.Type;
import java.awt.Color;
import java.awt.Image;
import java.awt.Toolkit;

import java.util.LinkedList;
import java.util.Iterator;
import java.util.List;

import javax.swing.ImageIcon;


/**
 * Модель дерева привязки элементов схемы к топологии. Привязка осуществляется 
 * с помощью операций drag-drop. Поскольку схемы могут быть вложены,
 * в дереве присутствует полная иерархия элементов, но поскольку не все
 * элементы наносятся на карту, для узла дерева вводится флаг isTopological,
 * который обозначает, что ветвь содержит наносимые на карту подэлементы.
 * На карту наносятся:
 * 	1. Элементы верхнего уровня для схемы
 *  2. Элементы схем, если они не типа CABLESUBNETWORK
 *  3. Внутренние элементы вложеннной схемы в соответствии с пп. 1, 2
 * 
 * Структура (*) этмечены элементы, которые можно наносить на карты
 * 
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
 * 
 * @version $Revision: 1.8 $, $Date: 2005/02/01 11:34:56 $
 * @module
 * @author $Author: krupenn $
 * @see
 */
public class MapSchemeTreeModel extends ObjectResourceTreeModel
{
	public static final String SCHEME_BRANCH = "scheme";
	public static final String ELEMENT_BRANCH = "schemeelement";
	public static final String LINK_BRANCH = "schemelink";
	public static final String CABLE_BRANCH = "schemecablelink";
	public static final String PATH_BRANCH = "schemepath";

	MapView mv;

	public MapSchemeTreeModel(MapView mv)
	{
		setMapView(mv);
	}
	
	public void setMapView(MapView mv)
	{
		this.mv = mv;
	}

	public ObjectResourceTreeNode getRoot()
	{
		MapSchemeTreeNode root;
		if(mv == null)
			root =  new MapSchemeTreeNode(
				"root", 
				"Без названия", 
				true);
		else
			root =  new MapSchemeTreeNode(
				mv, 
				"Вид - " + mv.getName(), 
				true);
		root.setTopological(true);
		return root;
	}


	public Color getNodeTextColor(ObjectResourceTreeNode node) { return Color.BLACK; }
	public void nodeAfterSelected(ObjectResourceTreeNode node) { }
	public void nodeBeforeExpanded(ObjectResourceTreeNode node) { }


	public Class getNodeChildClass(ObjectResourceTreeNode node)
	{
		if(node.getObject() instanceof String)
		{
			String s = (String )node.getObject();
			if(s.equals(SCHEME_BRANCH))
				return Scheme.class;
			if(s.equals(ELEMENT_BRANCH))
				return SchemeElement.class;
			if(s.equals(LINK_BRANCH))
				return SchemeLink.class;
			if(s.equals(CABLE_BRANCH))
				return SchemeCableLink.class;
			if(s.equals(PATH_BRANCH))
				return SchemePath.class;
		}
		else if (node.getObject() instanceof Scheme)
			return Scheme.class;
		else if (node.getObject() instanceof SchemeElement)
			return SchemeElement.class;
		return null;
	}

	public List getChildNodes(ObjectResourceTreeNode node)
	{
		MapSchemeTreeNode nod = null;
		MapSchemeTreeNode parent = (MapSchemeTreeNode )node.getParent();
		
		List vec = new LinkedList();
		if(node.getObject() instanceof String)
		{
			String s = (String )node.getObject();
			ObjectResource os;
			if (s.equals(SCHEME_BRANCH))
			{
				Scheme parsc = (Scheme )parent.getObject();
				List ds = new LinkedList();
				for (int i  = 0; i < parsc.schemeElements().length; i++)
				{
					SchemeElement el = (SchemeElement)parsc.schemeElements()[i];
					if (el.internalScheme() != null)
					{
						ds.add(el);
					}
				}
				
				if (ds.size() > 0)
				{
					for(Iterator it = ds.iterator(); it.hasNext();)
					{
//						Scheme sc = (Scheme )it.next();
						SchemeElement el = (SchemeElement )it.next();
						Scheme sc = el.internalScheme();

						if(	sc.type().value() != Type._CABLE_SUBNETWORK)
						{
							if(parent.isTopological())
							{
								nod = new MapSchemeTreeNode(
//									sc, 
									el,
									sc.name(), 
									true,
									new ImageIcon(Toolkit
										.getDefaultToolkit()
										.getImage("images/placedscheme.gif")
										.getScaledInstance(
											16, 
											16, 
											Image.SCALE_SMOOTH)));
								nod.setDragDropEnabled(true);
							}
							else
							{
								nod = new MapSchemeTreeNode(
//									sc,
									el,
									sc.name(), 
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
							nod = new MapSchemeTreeNode(
//									sc, 
									el,
									sc.name(), 
									true,
									ii);
							nod.setTopological(parent.isTopological());
						}
						vec.add(nod);
					}
				}
			}
			else 
			if (s.equals(ELEMENT_BRANCH))
			{
				Object par = parent.getObject();
				List ds = new LinkedList();
				if (par instanceof Scheme
					|| (par instanceof SchemeElement
						&& ((SchemeElement )par).internalScheme() != null)
					)
				{
					Scheme scheme;
					if(par instanceof Scheme)
						scheme = (Scheme )par;
					else
					{
						SchemeElement el = (SchemeElement )par;
						scheme = el.internalScheme();
					}
					
					for (int i = 0; i < scheme.schemeElements().length; i++)
					{
						SchemeElement element = (SchemeElement )scheme.schemeElements()[i];
						if (element.internalScheme() == null)
							ds.add(element);
					}
				}
				else if (par instanceof SchemeElement)
				{
					SchemeElement el = (SchemeElement )par;
					for (int i = 0; i < el.schemeElements().length; i++)
					{
						SchemeElement element = (SchemeElement)el.schemeElements()[i];
						if (element != null)
							ds.add(element);
					}
				}
				
				if (ds.size() > 0)
				{
					for(Iterator it = ds.iterator(); it.hasNext();)
					{
						SchemeElement element = (SchemeElement)it.next();
						boolean isFinal = (element.schemeLinks().length == 0 || element.schemeElements().length == 0);

						if (element.internalScheme() == null
							&& parent.isTopological())
						{
							nod = new MapSchemeTreeNode(
									element, 
									element.name(), 
									true, 
									new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/placedelement.gif")),
									isFinal);
							nod.setDragDropEnabled(true);
						}
						else
							nod = new MapSchemeTreeNode(
									element, 
									element.name(), 
									true, 
									new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/device.gif")),
									isFinal);
						vec.add(nod);
					}
				}
			}
			else if (s.equals(LINK_BRANCH))
			{
				Object par = parent.getObject();
				if (par instanceof Scheme
					|| (par instanceof SchemeElement
						&& ((SchemeElement )par).internalScheme() != null)
					)
				{
					Scheme scheme;
					if(par instanceof Scheme)
						scheme = (Scheme )par;
					else
					{
						SchemeElement el = (SchemeElement )par;
						scheme = el.internalScheme();
					}
					
					for(int i = 0 ; i < scheme.schemeLinks().length; i++)
					{
						SchemeLink link = (SchemeLink )scheme.schemeLinks()[i];
						vec.add(new MapSchemeTreeNode(link, link.name(), true, true));
					}
				}
				else if (par instanceof SchemeElement)
				{
					SchemeElement el = (SchemeElement )par;
					for(int i = 0; i < el.schemeLinks().length; i++)
					{
						SchemeLink link = (SchemeLink )el.schemeLinks()[i];
						vec.add(new MapSchemeTreeNode(link, link.name(), true, true));
					}
				}
			}
			else if (s.equals(CABLE_BRANCH))
			{
				Scheme parsc;

				if(parent.getObject() instanceof Scheme)
					parsc = (Scheme )parent.getObject();
				else
				{
					SchemeElement el = (SchemeElement )parent.getObject();
					parsc = el.internalScheme();
				}
					
				for(int i = 0 ; i < parsc.schemeCableLinks().length; i++)
				{
					SchemeCableLink link = (SchemeCableLink )parsc.schemeCableLinks()[i];
					if(parent.isTopological())
					{
						nod = new MapSchemeTreeNode(
								link, 
								link.name(), 
								true, 
								new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/linkmode.gif")),
								true);
						nod.setDragDropEnabled(true);
					}
					else
					{
						nod = new MapSchemeTreeNode(
								link, 
								link.name(), 
								true,
								true);
					}
					vec.add(nod);
				}
			}
			else if (s.equals(PATH_BRANCH))
			{
				Scheme parsc;

				if(parent.getObject() instanceof Scheme)
					parsc = (Scheme )parent.getObject();
				else
				{
					SchemeElement el = (SchemeElement )parent.getObject();
					parsc = el.internalScheme();
				}
					
				for(Iterator it = SchemeUtils.getTopologicalPaths(parsc).iterator(); it.hasNext();)
				{
					SchemePath path = (SchemePath )it.next();
					if(parent.isTopological())
					{
						nod = new MapSchemeTreeNode(
								path, 
								path.name(), 
								true, 
								new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/pathmode.gif")),
								true);
						nod.setDragDropEnabled(true);
					}
					else
					{
						nod = new MapSchemeTreeNode(
								path, 
								path.name(), 
								true, 
								true);
					}
					vec.add(nod);
				}
			}
		}
		else
		{
			if(node.getObject() instanceof MapView)
			{
				List schemes = mv.getSchemes();

				for(Iterator it = schemes.iterator(); it.hasNext();)
				{
					Scheme sc = (Scheme )it.next();

					nod = new MapSchemeTreeNode(
							sc, 
							sc.name(), 
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
					nod.setTopological(true);
					vec.add(nod);
				}
			}
			else
			if(node.getObject() instanceof Scheme
				|| (node.getObject() instanceof SchemeElement
					&& ((SchemeElement )(node.getObject())).internalScheme() != null)
				)
			{
				Scheme s;
				if(node.getObject() instanceof Scheme)
					s = (Scheme )node.getObject();
				else
				{
					SchemeElement el = (SchemeElement )node.getObject();
					s = el.internalScheme();
				}

				if (s.schemeElements().length != 0)
				{
					boolean hasSchemes = false;
					boolean hasElements = false;
					for (int i = 0; i < s.schemeElements().length; i++)
					{
						SchemeElement el = (SchemeElement )s.schemeElements()[i];
						if (el.internalScheme() == null)
						{
							hasElements = true;
							break;
						}
					}
					
					for (int i = 0; i < s.schemeElements().length; i++)
					{
						SchemeElement el = (SchemeElement )s.schemeElements()[i];
						if (el.internalScheme() != null)
						{
							hasSchemes = true;
							break;
						}
					}
					
					if (hasSchemes)
						vec.add(new MapSchemeTreeNode(SCHEME_BRANCH, "Вложенные схемы", true));
					if (hasElements)
						vec.add(new MapSchemeTreeNode(ELEMENT_BRANCH, "Вложенные элементы", true));
				}
				if (s.schemeLinks().length != 0)
					vec.add(new MapSchemeTreeNode(LINK_BRANCH, "Линии", true));
				if (s.schemeCableLinks().length != 0)
					vec.add(new MapSchemeTreeNode(CABLE_BRANCH, "Кабели", true));
				if (!SchemeUtils.getTopologicalPaths(s).isEmpty())
					vec.add(new MapSchemeTreeNode(PATH_BRANCH, "Пути", true));
			}
			else if(node.getObject() instanceof SchemeElement)
			{
				SchemeElement schel = (SchemeElement )node.getObject();
				if (schel.internalScheme() != null)
				{
					Scheme scheme = schel.internalScheme();
					for (int i = 0 ; i < scheme.schemeElements().length; i++)
					{
						SchemeElement element = (SchemeElement)scheme.schemeElements()[i];
						if (element.internalScheme() == null)
						{
							vec.add(new MapSchemeTreeNode(
									element, 
									element.name(), 
									true, 
									new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/device.gif")), 
									true));
						}
						else
						{
							vec.add(new MapSchemeTreeNode(
									element, 
									element.name(), 
									true,
									new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/scheme.gif")), 
									true));
						}
					}
				}
				else
				{
					if (schel.schemeElements().length != 0)
						vec.add(new MapSchemeTreeNode(ELEMENT_BRANCH, "Вложенные элементы", true));
				 if (schel.schemeLinks().length != 0)
						vec.add(new MapSchemeTreeNode(LINK_BRANCH, "Линии", true));
				}
			}
		}
		return vec;
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

