/**
 * $Id: MapSchemeTreeModel.java,v 1.4 2004/10/19 11:48:28 krupenn Exp $
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
import com.syrus.AMFICOM.Client.Resource.MapView.MapView;
import com.syrus.AMFICOM.Client.Resource.ObjectResource;
import com.syrus.AMFICOM.Client.Resource.ObjectResourceSorter;
import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.Client.Resource.Scheme.Scheme;
import com.syrus.AMFICOM.Client.Resource.Scheme.SchemeCableLink;
import com.syrus.AMFICOM.Client.Resource.Scheme.SchemeElement;
import com.syrus.AMFICOM.Client.Resource.Scheme.SchemeLink;
import com.syrus.AMFICOM.Client.Resource.Scheme.SchemePath;

import java.awt.Color;
import java.awt.Image;
import java.awt.Toolkit;

import java.util.ArrayList;
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
 * @version $Revision: 1.4 $, $Date: 2004/10/19 11:48:28 $
 * @module
 * @author $Author: krupenn $
 * @see
 */
public class MapSchemeTreeModel extends ObjectResourceTreeModel
{
	DataSourceInterface dsi;
	MapView mv;

	public MapSchemeTreeModel(DataSourceInterface dsi)
	{
		setDataSource(dsi);
	}

	public MapSchemeTreeModel(MapView mv, DataSourceInterface dsi)
	{
		this(dsi);
		setMapView(mv);
	}
	
	public void setDataSource(DataSourceInterface dsi)
	{
		this.dsi = dsi;
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
			if(s.equals(Scheme.typ))
				return Scheme.class;
			if(s.equals(SchemeElement.typ))
				return SchemeElement.class;
			if(s.equals(SchemeLink.typ))
				return SchemeLink.class;
			if(s.equals(SchemeCableLink.typ))
				return SchemeCableLink.class;
			if(s.equals(SchemePath.typ))
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
		
		List vec = new ArrayList();
		if(node.getObject() instanceof String)
		{
			String s = (String )node.getObject();
			ObjectResource os;
			if (s.equals(Scheme.typ))
			{
				Scheme parsc = (Scheme )parent.getObject();
				ArrayList ds = new ArrayList();
				for (Iterator it = parsc.elements.iterator(); it.hasNext();)
				{
					SchemeElement el = (SchemeElement)it.next();
					if (!el.getInternalSchemeId().equals(""))
					{
						ds.add(el);
//						Scheme sc = (Scheme )Pool.get(Scheme.typ, el.getInternalSchemeId());
//						if (sc != null)
//							ds.add(sc);
					}
				}
				
				if (ds.size() > 0)
				{
					ObjectResourceSorter sorter = Scheme.getSorter();
					sorter.setDataSet(ds);
					for(Iterator it = sorter.default_sort().iterator(); it.hasNext();)
					{
//						Scheme sc = (Scheme )it.next();
						SchemeElement el = (SchemeElement )it.next();
						Scheme sc = (Scheme )Pool.get(Scheme.typ, el.getInternalSchemeId());

						if(	!sc.schemeType.equals(Scheme.CABLESUBNETWORK) )
						{
							if(parent.isTopological())
							{
								nod = new MapSchemeTreeNode(
//									sc, 
									el,
									sc.getName(), 
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
									sc.getName(), 
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
									sc.getName(), 
									true,
									ii);
							nod.setTopological(parent.isTopological());
						}
						vec.add(nod);
					}
				}
			}
			else 
			if (s.equals(SchemeElement.typ))
			{
				Object par = parent.getObject();
				ArrayList ds = new ArrayList();
				if (par instanceof Scheme
					|| (par instanceof SchemeElement
						&& ((SchemeElement )par).getInternalSchemeId().length() != 0)
					)
				{
					Scheme scheme;
					if(par instanceof Scheme)
						scheme = (Scheme )par;
					else
					{
						SchemeElement el = (SchemeElement )par;
						scheme = (Scheme )Pool.get(Scheme.typ, el.getInternalSchemeId());
					}
					
					for (Iterator it = scheme.elements.iterator(); it.hasNext();)
					{
						SchemeElement element = (SchemeElement )it.next();
						if (element.getInternalSchemeId().equals(""))
							ds.add(element);
					}
				}
				else if (par instanceof SchemeElement)
				{
					SchemeElement el = (SchemeElement )par;
					for (Iterator it = el.elementIds.iterator(); it.hasNext();)
					{
						SchemeElement element = (SchemeElement)Pool.get(SchemeElement.typ, (String)it.next());
						if (element != null)
							ds.add(element);
					}
				}
				
				if (ds.size() > 0)
				{
					ObjectResourceSorter sorter = SchemeElement.getSorter();
					sorter.setDataSet(ds);
					for(Iterator it = sorter.default_sort().iterator(); it.hasNext();)
					{
						SchemeElement element = (SchemeElement)it.next();
						boolean isFinal = (element.links.isEmpty() || element.elementIds.isEmpty());

						if (element.getInternalSchemeId().equals("")
							&& parent.isTopological())
						{
							nod = new MapSchemeTreeNode(
									element, 
									element.getName(), 
									true, 
									new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/placedelement.gif")),
									isFinal);
							nod.setDragDropEnabled(true);
						}
						else
							nod = new MapSchemeTreeNode(
									element, 
									element.getName(), 
									true, 
									new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/device.gif")),
									isFinal);
						vec.add(nod);
					}
				}
			}
			else if (s.equals(SchemeLink.typ))
			{
				Object par = parent.getObject();
				if (par instanceof Scheme
					|| (par instanceof SchemeElement
						&& ((SchemeElement )par).getInternalSchemeId().length() != 0)
					)
				{
					Scheme scheme;
					if(par instanceof Scheme)
						scheme = (Scheme )par;
					else
					{
						SchemeElement el = (SchemeElement )par;
						scheme = (Scheme )Pool.get(Scheme.typ, el.getInternalSchemeId());
					}
					
					ObjectResourceSorter sorter = SchemeLink.getSorter();
					sorter.setDataSet(scheme.links);
					for(Iterator it = sorter.default_sort().iterator(); it.hasNext();)
					{
						SchemeLink link = (SchemeLink )it.next();
						vec.add(new MapSchemeTreeNode(link, link.getName(), true, true));
					}
				}
				else if (par instanceof SchemeElement)
				{
					SchemeElement el = (SchemeElement )par;
					ObjectResourceSorter sorter = SchemeLink.getSorter();
					sorter.setDataSet(el.links);
					for(Iterator it = sorter.default_sort().iterator(); it.hasNext();)
					{
						SchemeLink link = (SchemeLink )it.next();
						vec.add(new MapSchemeTreeNode(link, link.getName(), true, true));
					}
				}
			}
			else if (s.equals(SchemeCableLink.typ))
			{
				Scheme parsc;

				if(parent.getObject() instanceof Scheme)
					parsc = (Scheme )parent.getObject();
				else
				{
					SchemeElement el = (SchemeElement )parent.getObject();
					parsc = (Scheme )Pool.get(Scheme.typ, el.getInternalSchemeId());
				}
					
				ObjectResourceSorter sorter = SchemeCableLink.getSorter();
				sorter.setDataSet(parsc.cablelinks);
				for(Iterator it = sorter.default_sort().iterator(); it.hasNext();)
				{
					SchemeCableLink link = (SchemeCableLink )it.next();
					if(parent.isTopological())
					{
						nod = new MapSchemeTreeNode(
								link, 
								link.getName(), 
								true, 
								new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/linkmode.gif")),
								true);
						nod.setDragDropEnabled(true);
					}
					else
					{
						nod = new MapSchemeTreeNode(
								link, 
								link.getName(), 
								true,
								true);
					}
					vec.add(nod);
				}
			}
			else if (s.equals(SchemePath.typ))
			{
				Scheme parsc;

				if(parent.getObject() instanceof Scheme)
					parsc = (Scheme )parent.getObject();
				else
				{
					SchemeElement el = (SchemeElement )parent.getObject();
					parsc = (Scheme )Pool.get(Scheme.typ, el.getInternalSchemeId());
				}
					
				ObjectResourceSorter sorter = SchemePath.getSorter();
				sorter.setDataSet(parsc.getTopologicalPaths());
				for(Iterator it = sorter.default_sort().iterator(); it.hasNext();)
				{
					SchemePath path = (SchemePath )it.next();
					if(parent.isTopological())
					{
						nod = new MapSchemeTreeNode(
								path, 
								path.getName(), 
								true, 
								new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/pathmode.gif")),
								true);
						nod.setDragDropEnabled(true);
					}
					else
					{
						nod = new MapSchemeTreeNode(
								path, 
								path.getName(), 
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
				ObjectResourceSorter sorter = Scheme.getSorter();
				sorter.setDataSet(schemes);
	
				for(Iterator it = sorter.default_sort().iterator(); it.hasNext();)
				{
					Scheme sc = (Scheme )it.next();

					nod = new MapSchemeTreeNode(
							sc, 
							sc.getName(), 
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
					&& ((SchemeElement )(node.getObject())).getInternalSchemeId().length() != 0)
				)
			{
				Scheme s;
				if(node.getObject() instanceof Scheme)
					s = (Scheme )node.getObject();
				else
				{
					SchemeElement el = (SchemeElement )node.getObject();
					s = (Scheme )Pool.get(Scheme.typ, el.getInternalSchemeId());
				}

				if (!s.elements.isEmpty())
				{
					boolean hasSchemes = false;
					boolean hasElements = false;
					for (Iterator it = s.elements.iterator(); it.hasNext();)
					{
						SchemeElement el = (SchemeElement )it.next();
						if (el.getInternalSchemeId().length() == 0)
						{
							hasElements = true;
							break;
						}
					}
					
					for (Iterator it = s.elements.iterator(); it.hasNext();)
					{
						SchemeElement el = (SchemeElement )it.next();
						if (el.getInternalSchemeId().length() != 0)
						{
							hasSchemes = true;
							break;
						}
					}
					
					if (hasSchemes)
						vec.add(new MapSchemeTreeNode(Scheme.typ, "Вложенные схемы", true));
					if (hasElements)
						vec.add(new MapSchemeTreeNode(SchemeElement.typ, "Вложенные элементы", true));
				}
				if (!s.links.isEmpty())
					vec.add(new MapSchemeTreeNode(SchemeLink.typ, "Линии", true));
				if (!s.cablelinks.isEmpty())
					vec.add(new MapSchemeTreeNode(SchemeCableLink.typ, "Кабели", true));
				if (!s.getTopologicalPaths().isEmpty())
					vec.add(new MapSchemeTreeNode(SchemePath.typ, "Пути", true));
			}
			else if(node.getObject() instanceof SchemeElement)
			{
				SchemeElement schel = (SchemeElement )node.getObject();
				if (!schel.getInternalSchemeId().equals(""))
				{
					Scheme scheme = (Scheme)Pool.get(Scheme.typ, schel.getInternalSchemeId());
					for (Iterator it = scheme.elements.iterator(); it.hasNext();)
					{
						SchemeElement element = (SchemeElement)it.next();
						if (element.getInternalSchemeId().equals(""))
						{
							vec.add(new MapSchemeTreeNode(
									element, 
									element.getName(), 
									true, 
									new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/device.gif")), 
									true));
						}
						else
						{
							vec.add(new MapSchemeTreeNode(
									element, 
									element.getName(), 
									true,
									new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/scheme.gif")), 
									true));
						}
					}
				}
				else
				{
					if (!schel.elementIds.isEmpty())
						vec.add(new MapSchemeTreeNode(SchemeElement.typ, "Вложенные элементы", true));
				 if (!schel.links.isEmpty())
						vec.add(new MapSchemeTreeNode(SchemeLink.typ, "Линии", true));
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

