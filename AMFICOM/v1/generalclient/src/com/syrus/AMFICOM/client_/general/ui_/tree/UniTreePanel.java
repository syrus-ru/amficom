/*
 * $Id: UniTreePanel.java,v 1.2 2005/01/31 15:03:10 stas Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.client_.general.ui_.tree;

import java.util.*;
import java.util.List;

import java.awt.*;
import java.awt.dnd.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;

import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.General.UI.ObjectResourceCatalogActionModel;
import com.syrus.AMFICOM.client_.resource.ObjectResourceController;

/**
 * для оптимизации работы дерева вводится следующая схема разворачивания ветви
дерева:

при добавлении новых узлов (нод) тем узлам, которые являются основными для
данного типа, после создания ObjectResourceTreeNode их необходимо
зарегистрировать, то есть вызвать метод
registerSearchableNode(String criteria, ObjectResourceTreeNode tn).

Например, при добавлении в дерево группы этот метод будет вызываться так:

			ortn = new ObjectResourceTreeNode(
											OperatorGroup.typ,
											"Группы",
											true);
			vec.add(ortn);
			registerSearchableNode(OperatorGroup.typ, ortn);


Если группа добавляется в дочернюю ветвь (например, список групп в
пользователях или командах), эта функция вызываться не должна.

Таким образом, послав в дерево ListSelectionEvent с указанием OperatorGroup.typ
зарегистрированная ветвь станет выделенной.
 *
 * @author $Author: stas $
 * @version $Revision: 1.2 $, $Date: 2005/01/31 15:03:10 $
 * @module generalclient_v1
 */
public class UniTreePanel extends JPanel
		implements OperationListener, DragGestureListener
{
	DragSource dragSource = null;

	boolean send_event = false;
	Dispatcher dispatcher;
	ApplicationContext aContext;
	ObjectResourceTreeModel otm;
	ObjectResourceTreeNode root;
	JTree tree;
	TreeModel tm;
	private BorderLayout borderLayout1 = new BorderLayout();
	ObjectResourceTreeRenderer renderer = new ObjectResourceTreeRenderer();

	//ObjectResourceTreeNode stubNode = new ObjectResourceTreeNode("", "", true);

	public UniTreePanel(Dispatcher disp, ApplicationContext aContext, ObjectResourceTreeModel otm)
	{
		this.aContext = aContext;
		this.dispatcher = disp;
		this.otm = otm;
		try
		{
			jbInit();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		setModel(otm);
	}

	private void jbInit() throws Exception
	{
		dispatcher.register(this, "treelistselectionevent");
		dispatcher.register(this, "treelistdeselectionevent");
		dispatcher.register(this, "treelistrefreshevent");
		dispatcher.register(this, ContextChangeEvent.type);
		Environment.getDispatcher().register(this, ContextChangeEvent.type);
		this.setLayout(borderLayout1);

		tree = new JTree(new Hashtable());
		tree.setRootVisible(true);
		tree.addTreeWillExpandListener(new TreeWillExpandListener() {
			public void treeWillExpand(TreeExpansionEvent e) throws ExpandVetoException {
				tree_treeWillExpand(e);
			}

			public void treeWillCollapse(TreeExpansionEvent e) {

			}
		});
		tree.setCellRenderer(renderer);
		this.add(tree, BorderLayout.CENTER);
		tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);

		tree.addTreeSelectionListener(new TreeSelectionListener()
		{
			public void valueChanged(TreeSelectionEvent e)
			{
				tree_valueChanged(e);
			}
		});

		dragSource = new DragSource();
		dragSource.createDefaultDragGestureRecognizer(tree, DnDConstants.ACTION_MOVE, this);

// following line was inserted at Pp's request
		dragSource.createDefaultDragGestureRecognizer(this, DnDConstants.ACTION_MOVE, this);

	}

	public void setModel(ObjectResourceTreeModel otm)
	{
		if(otm == null)
		{
			root = new ObjectResourceTreeNode("leer", "", true);
			tm = new DefaultTreeModel(root);
			tree.setModel(tm);
			tree.updateUI();
			return;
		}
		this.otm = otm;
		root = otm.getRoot();
		this.setCursor(new Cursor(Cursor.WAIT_CURSOR));
		otm.nodeBeforeExpanded(root);
		List vec1 = otm.getChildNodes(root);
		this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		root.removeAllChildren();

		ListIterator lIt = vec1.listIterator();
		for (; lIt.hasNext();)
		{
			ObjectResourceTreeNode tn = (ObjectResourceTreeNode ) lIt.next();
			root.add(tn);
			if(!tn.isFinal())
				tn.add(new ObjectResourceTreeNode("", "", true));
		}
		tm = new DefaultTreeModel(root);
		tree.setModel(tm);
		tree.revalidate();
	}

	public JTree getTree()
	{
		return tree;
	}

	public boolean setNodeSelection(ObjectResourceTreeNode orte, Object o)
	{
		boolean selected = false;
		ObjectResourceTreeNode temp = null;
		if (orte != null)
		{
			if (orte.getObject().equals(o))
			{
				temp = orte;
				selected = true;
			}
			if (!orte.isFinal() && orte.getChildCount() != 0)
			{
				ObjectResourceTreeNode ornode = (ObjectResourceTreeNode )orte.getFirstChild();
				Object node_obj = ornode.getObject();
				if ( (node_obj instanceof String) && ((String)node_obj).equals(""))
				{
					this.setCursor(new Cursor(Cursor.WAIT_CURSOR));
					orte.removeAllChildren();
					otm.nodeBeforeExpanded(orte);
					List vec = otm.getChildNodes(orte);
					this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
					if (vec.size() == 0)
					{
						orte.isFinal = true;
					}

					ListIterator lIt = vec.listIterator();
					for (; lIt.hasNext();)
					{
						ObjectResourceTreeNode tn = (ObjectResourceTreeNode ) lIt.next();
						orte.add(tn);
						if (tn.getObject().equals(o))
						{
							temp = tn;
							selected = true;
						}
						if(!tn.isFinal())
							tn.add(new ObjectResourceTreeNode("", "", true));
					}
				}
				else
				{
					Enumeration en = orte.children();
					for (; en.hasMoreElements();)
					{
						ObjectResourceTreeNode tn = (ObjectResourceTreeNode ) en.nextElement();
						if (tn.getObject().equals(o))
						{
							temp = tn;
							selected = true;
						}
					}
				}
			}
			tree.updateUI();
			if (temp != null)
			{
				tree.scrollPathToVisible(new TreePath (temp.getPath()));
				tree.setSelectionPath(new TreePath (temp.getPath()));
			}
		}
		return selected;
	}


	public void operationPerformed (OperationEvent ev)
	{
		if (ev.getActionCommand().equals(TreeListSelectionEvent.typ))
		{
			TreeListSelectionEvent select_event = (TreeListSelectionEvent)ev;
			if(select_event.SELECT)
			{
				if (send_event)
					return;
				Object o = ev.getSource();

				if ( (select_event.search == true) && (select_event.searchAll == true) )
				{
					ObjectResourceTreeNode orte = recure_tree_exp(root, o);
					if (orte != null)
					{
						tree.scrollPathToVisible(new TreePath (orte.getPath()));
						tree.setSelectionPath(new TreePath (orte.getPath()));
					}
				}
				else if (select_event.search == false)
				{
					TreePath tp = tree.getSelectionPath();
					if (tp != null)
					{
						ObjectResourceTreeNode orte = (ObjectResourceTreeNode )tp.getLastPathComponent();
						orte = (ObjectResourceTreeNode )orte.getParent();
						setNodeSelection(orte, o);
					}
				}
				else if ( (select_event.search == true) && (select_event.searchAll == false) )
				{
					List vec = otm.getSearchableNodes(o);

					ListIterator lIt = vec.listIterator();
					for (; lIt.hasNext();)
					{
						ObjectResourceTreeNode tn = (ObjectResourceTreeNode ) lIt.next();

						if (setNodeSelection(tn, o))
							break;
					}

					if (!lIt.hasNext() || !lIt.hasPrevious())
					{
						dispatcher.notify(new TreeListSelectionEvent(o, TreeListSelectionEvent.SELECT_EVENT));
					}
				}
			}
			else if (select_event.REFRESH)
			{
				String o = (String)ev.getSource();
				TreePath tp = tree.getSelectionPath();
				boolean selected = false;
				if (tp != null)
				{
					ObjectResourceTreeNode orte = (ObjectResourceTreeNode )tp.getLastPathComponent();
					if (orte != null)
					{
						orte = (ObjectResourceTreeNode )orte.getParent();
						this.setCursor(new Cursor(Cursor.WAIT_CURSOR));
						orte.removeAllChildren();
						otm.nodeBeforeExpanded(orte);
						List vec = otm.getChildNodes(orte);
						this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
						if (vec.size() == 0)
						{
							orte.isFinal = true;
						}
						else
						{
							orte.isFinal = false;
						}

						ListIterator lIt = vec.listIterator();
						for (; lIt.hasNext();)
						{
							ObjectResourceTreeNode tn = (ObjectResourceTreeNode ) lIt.next();
							orte.add(tn);
							if (tn.getObject().equals(o))
							{
								tree.setSelectionPath(new TreePath (tn.getPath()));
								selected = true;
							}
							if(!tn.isFinal())
								tn.add(new ObjectResourceTreeNode("", "", true));
						}
						if (selected == false)
						{
							tree.updateUI();
							tree.setSelectionPath(new TreePath (orte.getPath()));
						}
					}
				}
			}
			else if (select_event.DESELECT)
			{
				tree.getSelectionModel().clearSelection();
			}
		}
		else if (ev.getActionCommand().equals(ContextChangeEvent.type))
		{
			ContextChangeEvent cce = (ContextChangeEvent )ev;
			if(cce.DOMAIN_SELECTED)
			{
				setModel(otm);
			}
		}
	}

	public void tree_valueChanged(TreeSelectionEvent e)
	{
		ObjectResourceTreeNode node = (ObjectResourceTreeNode)tree.getLastSelectedPathComponent();
		if (node == null)
			return;

		List res = new ArrayList();
		Class cl = null;
		ObjectResourceController controller = null;
		//DataSet data = new DataSet();
		ObjectResourceCatalogActionModel orcam = null;
		int n = 0;
		Object selectedObject = node.getObject();

		if (node.getObject() instanceof String)
		{
			if (node.isFinal() == false && node.getChildCount() != 0)
			{
				TreeNode treen = node.getFirstChild();
				if (treen != null)
				{
					Object o = ((ObjectResourceTreeNode )treen).getObject();
					if (o instanceof String && ((String)o).equals(""))
//		if (orte.getFirstChild().equals(stubNode))
					{
						this.setCursor(new Cursor(Cursor.WAIT_CURSOR));
						node.removeAllChildren();
						otm.nodeBeforeExpanded(node);
						List vec = otm.getChildNodes(node);
						this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
						if (vec.size() == 0)
							node.isFinal = true;

						for (Iterator it = vec.iterator(); it.hasNext();)
						{
							ObjectResourceTreeNode tn = (ObjectResourceTreeNode)it.next();

							node.add(tn);
							if(!tn.isFinal())
								tn.add(new ObjectResourceTreeNode("", "", true));
							Object obj = tn.getObject();
							if (!(obj instanceof String))
								res.add( obj );
						}
					}
					else
					{
						Enumeration en = node.children();
						for (; en.hasMoreElements();)
						{
							ObjectResourceTreeNode tn = (ObjectResourceTreeNode ) en.nextElement();
							Object obj = tn.getObject();
							if (!(obj instanceof String))
								res.add( obj );
						}
					}
				}
			}
			cl = otm.getNodeChildClass(node);
			orcam = otm.getNodeActionModel(node);
			controller = otm.getNodeChildController(node);
			n = -1;
		}
		else
		{
			if (node.isRoot())
				return;

			ObjectResourceTreeNode parent = (ObjectResourceTreeNode)node.getParent();

			Enumeration enum = parent.children();
			for(; enum.hasMoreElements();)
			{
				Object oo = ((ObjectResourceTreeNode)enum.nextElement()).getObject();
				if (!(oo instanceof String))
					res.add(oo);
			}
			cl = otm.getNodeChildClass(parent);
			orcam = otm.getNodeActionModel(parent);
			controller = otm.getNodeChildController(parent);
//					cl = node.getClass();
			selectedObject = node.getObject();
			n =	res.indexOf(selectedObject);
		}


		TreeDataSelectionEvent event = new TreeDataSelectionEvent(this, res, cl, n, selectedObject, controller);
		event.setParam(orcam);

//				System.out.println("ORTreePanel notify " + dispatcher + " with event " + event);
		send_event = true;
		dispatcher.notify(event);
		send_event = false;
	}

	void tree_treeWillExpand(TreeExpansionEvent e) throws ExpandVetoException
	{
		ObjectResourceTreeNode orte = (ObjectResourceTreeNode )e.getPath().getLastPathComponent();
		TreeNode treen = (ObjectResourceTreeNode )orte.getFirstChild();
		if (treen != null)
		{
			Object o = ((ObjectResourceTreeNode )treen).getObject();
			if (o instanceof String && ((String)o).equals(""))
//		if (orte.getFirstChild().equals(stubNode))
			{
				this.setCursor(new Cursor(Cursor.WAIT_CURSOR));
				orte.removeAllChildren();
				otm.nodeBeforeExpanded(orte);
				List vec = otm.getChildNodes(orte);
				this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
				if (vec.size() == 0)
				{
					orte.isFinal = true;
				}

				ListIterator lIt = vec.listIterator();
				for (; lIt.hasNext();)
				{
					ObjectResourceTreeNode tn = (ObjectResourceTreeNode ) lIt.next();
					orte.add(tn);
					if(!tn.isFinal())
						tn.add(new ObjectResourceTreeNode("", "", true));
//				tn.add(stubNode);
				}
			}
		}
		tree.setSelectionPath(new TreePath (orte.getPath()));
	}

	ObjectResourceTreeNode recure_tree_exp(ObjectResourceTreeNode tn, Object o)
	{
		ObjectResourceTreeNode oooo = null;
		if (tn.isFinal())
		{
			return null;
		}

		ObjectResourceTreeNode fc = (ObjectResourceTreeNode )tn.getFirstChild();

		Object obj = fc.getObject();
		if (obj instanceof String && ((String)obj).length()==0)
		{
			this.setCursor(new Cursor(Cursor.WAIT_CURSOR));
			tn.removeAllChildren();
			otm.nodeBeforeExpanded(tn);
			List vec = otm.getChildNodes(tn);
			this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			if (vec.size() == 0)
			{
				tn.isFinal = true;
			}

			ListIterator lIt = vec.listIterator();
			for (; lIt.hasNext();)
			{
				ObjectResourceTreeNode tnn = (ObjectResourceTreeNode ) lIt.next();
				tn.add(tnn);
				if(!tnn.isFinal())
					tnn.add(new ObjectResourceTreeNode("", "", true));

				if (!tnn.getObject().equals(o))
				{
					oooo = recure_tree_exp(tnn, o);
					if (oooo != null)
					{
						for(; lIt.hasNext();)
						{
							ObjectResourceTreeNode temp = ((ObjectResourceTreeNode ) lIt.next());
							tn.add(temp);
							if(!temp.isFinal())
								temp.add(new ObjectResourceTreeNode("", "", true));
						}
						break;
					}
				}
				else
				{
					oooo = tnn;
					for(; lIt.hasNext();)
					{
						ObjectResourceTreeNode temp = ((ObjectResourceTreeNode ) lIt.next());
						tn.add(temp);
						if(!temp.isFinal())
							temp.add(new ObjectResourceTreeNode("", "", true));
					}
					break;
				}
			}
		}
		else
		{
			Enumeration en = tn.children();
			for (; en.hasMoreElements();)
			{
				ObjectResourceTreeNode tnn = (ObjectResourceTreeNode ) en.nextElement();
				if (!tnn.getObject().equals(o))
				{
					oooo = recure_tree_exp(tnn, o);
					if (oooo != null)
					{
						break;
					}
				}
				else
				{
					oooo = tnn;
					break;
				}
			}
		}
		return oooo;
	}

	public void dragGestureRecognized( DragGestureEvent event)
	{
//		System.out.println("dragGestureRecognized");

		Point origin = event.getDragOrigin();
		TreePath tp = tree.getClosestPathForLocation(origin.x, origin.y);
		ObjectResourceTreeNode ortn = (ObjectResourceTreeNode )tp.getLastPathComponent();
		if(ortn.isDragDropEnabled())
		{
			ObjectResourceElementLabel orel = ortn.getElementLabel();
			if(orel != null)
				orel.dragGestureRecognized(event);
//			else
//				System.out.println("OREL is null");
		}
//		else
//			System.out.println("ORTN not draggable!");
	}

	/*ObjectResourceTreeNode refresh_tree_exp(ObjectResourceTreeNode tn, String o)
	{
		ObjectResourceTreeNode oooo = null;
		int count = tn.getChildCount();
		otm.nodeBeforeExpanded(tn);
		Enumeration enum;
		if (count == 0)
		{
			enum = otm.getChildNodes(tn).elements();
		}
		else
		{
			enum = tn.children();
		}
		for(; enum.hasMoreElements();)
		{
			ObjectResourceTreeNode ortn = (ObjectResourceTreeNode ) enum.nextElement();
			if (count == 0)
			{
				tn.add(ortn);
			}
			if (!ortn.getObject().equals(o))
			{
				oooo = refresh_tree_exp(ortn, o);
				if (oooo != null)
				{
					break;
				}
			}
			else
			{
				oooo = ortn;
				otm.nodeBeforeExpanded(ortn);
				break;
			}
		}
		return oooo;
	}*/
}
