package com.syrus.AMFICOM.Client.General.UI;

import com.syrus.AMFICOM.Client.General.Event.Dispatcher;
import com.syrus.AMFICOM.Client.General.Event.OperationEvent;
import com.syrus.AMFICOM.Client.General.Event.OperationListener;
import com.syrus.AMFICOM.Client.General.Event.TreeDataSelectionEvent;
import com.syrus.AMFICOM.Client.General.Lang.LangModel;
import com.syrus.AMFICOM.Client.Resource.DataSet;
import com.syrus.AMFICOM.Client.Resource.ObjectResource;
import com.syrus.AMFICOM.Client.Resource.Pool;

import java.awt.BorderLayout;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.JViewport;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.event.TreeWillExpandListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

public class ObjectResourceTreePanel extends JPanel implements OperationListener
{
	Dispatcher dispatcher;
	JScrollPane scrollPane = new JScrollPane();
	JViewport viewport = new JViewport();
	JTree tree;
	public ORMutableTreeNode root;
	public String selected_string = "";
	ORMutableTreeNode curr_type_node = new ORMutableTreeNode("empty");

	String[] filters;
	Vector path = new Vector();
	boolean send_event = false;

	public ObjectResourceTreePanel(Dispatcher dispatcher)
	{
		try
		{
			jbInit();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		init_module(dispatcher);
	}

	private void jbInit() throws Exception
	{
		setLayout(new BorderLayout());
		setBorder(BorderFactory.createLoweredBevelBorder());
		scrollPane.setViewport(viewport);
		scrollPane.setAutoscrolls(true);
		add(scrollPane, BorderLayout.CENTER);

		tree = new JTree();
		scrollPane.getViewport().add(tree);
	}

	void init_module (Dispatcher dispatcher)
	{
		this.dispatcher = dispatcher;
		dispatcher.register(this, "listselectionevent");
		dispatcher.register(this, "listdeselectionevent");
		dispatcher.register(this, "listrefreshevent");
	}

	public void operationPerformed (OperationEvent ev)
	{
		if (ev.getActionCommand().equals("listselectionevent"))
		{
			if (send_event)
				return;

			System.out.println("ORTreePanel received listselectionevent event " + ev);

			Object o = ev.getSource();
			if(o instanceof ObjectResource)
			{
				ORMutableTreeNode node_selected = findNodeAtCurrent(curr_type_node, (ObjectResource)o);
				if (node_selected != null)
				{
					tree.setSelectionPath(new TreePath (node_selected.getPath()));
				}
				else
					System.out.println("ORTreePanel can't find OR " + ((ObjectResource)o).getName() + " at " + curr_type_node);
			}
		}
		else if (ev.getActionCommand().equals("listrefreshevent"))
		{
			String path = (String)ev.getSource();
			if (curr_type_node.name.equals(path))
			{
				refreshCurrentNode();
			}
		}
		else if (ev.getActionCommand().equals("listdeselectionevent"))
		{
			tree.getSelectionModel().clearSelection();
		}
	}

	public void createTree (String rootname, String[] rootnodes)
	{
		createTree (rootname, rootnodes, new String[] {});
	}

	public void createTree (String rootname, String[] rootnodes, Hashtable[] roothashs)
	{
		createTree (rootname, rootnodes, roothashs, new String[] {});
	}

	public void createTree (String rootname, String[] rootnodes, String[] filters)
	{
		Hashtable[] hashes = new Hashtable[rootnodes.length];
		for (int i = 0; i < hashes.length; i++)
			hashes[i] = Pool.getHash(rootnodes[i]);
		createTree (rootname, rootnodes, hashes, new String[] {});
	}

	public void createTree (String rootname, String[] rootnodes, Hashtable[] rootHashs, String[] filters)
	{
		root = new ORMutableTreeNode(rootname);
		this.filters = filters;

		for (int i = 0; i < rootnodes.length; i++)
		{
			ORMutableTreeNode node = new ORMutableTreeNode(rootnodes[i]);
			root.add(node);
			Hashtable h = rootHashs[i];
			if (h != null)
			{
				Vector v = new Vector();
				for (Enumeration enum = h.elements(); enum.hasMoreElements();)
				{
					ObjectResource res = (ObjectResource)enum.nextElement();
					v.add( new ObjectResourceTreeFilter (res, createFilter(res)));
				}
				ObjectResourceTreeBuilder.addTreeNodes((ObjectResourceTreeFilter[])v.toArray(new ObjectResourceTreeFilter[0]), node);
			}
		}
		scrollPane.getViewport().remove(tree);
		tree = new JTree(root, true);
		tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);

		tree.addTreeSelectionListener(new TreeSelectionListener()
		{
			public void valueChanged(TreeSelectionEvent e)
			{
				ORMutableTreeNode node = (ORMutableTreeNode)tree.getLastSelectedPathComponent();
				if (node == null)
					return;

				Vector res = new Vector();
				Class cl;
				DataSet data = new DataSet();
				int n;

				if (node.isObjectResource())
				{
					ORMutableTreeNode parent = (ORMutableTreeNode)node.getParent();
					curr_type_node = parent;

					for (Enumeration enum = parent.children(); enum.hasMoreElements();)
						res.add( ((ORMutableTreeNode)enum.nextElement()).resourceFilter.getResource() );

					data = new DataSet (res);
					cl = node.resourceFilter.getResource().getClass();
					n =	data.indexOf(node.resourceFilter.getResource());
				}
				else
				{
					expandNode (node);
					if (node.equals(root))
						return;
					curr_type_node = node;

					for (Enumeration enum = node.children(); enum.hasMoreElements();)
						res.add( ((ORMutableTreeNode)enum.nextElement()).resourceFilter.getResource() );

					if (res.isEmpty())
						return;

					data = new DataSet (res);
					cl = ((ObjectResource)res.get(0)).getClass();
					n = -1;
				}

				OperationEvent event = new TreeDataSelectionEvent(this, data,	cl,	n);

//				System.out.println("ORTreePanel notify " + dispatcher + " with event " + event);
				send_event = true;
				selected_string = curr_type_node.name;
				dispatcher.notify(event);
				send_event = false;
			}
		});

		tree.addTreeWillExpandListener(new TreeWillExpandListener()
		{
			public void treeWillExpand(TreeExpansionEvent e)
			{
				ORMutableTreeNode node = (ORMutableTreeNode)e.getPath().getLastPathComponent();
				if (node == null)
					return;
				if (node.equals(root))
					return;
				expandNode (node);
			}
			public void treeWillCollapse(TreeExpansionEvent e)
			{
			}
		});
		scrollPane.getViewport().add(tree);
	}

	String[] createFilter (ObjectResource res)
	{
		Vector v = new Vector();
		for (Enumeration types = res.getChildTypes(); types.hasMoreElements();)
		{
			String type = (String)types.nextElement();
			for (int i=0; i < filters.length; i++)
				if (type.equals(filters[i]))
					v.add(type);
		}
		return (String[])v.toArray(new String[0]);
	}

	public void expandNode (ORMutableTreeNode node)
	{
		if (node.expanded)
			return;
		node.expanded = true;
		if (node.isObjectResource())
			return;
		if ((ORMutableTreeNode)node.getParent() == null)
			return;

		if (((ORMutableTreeNode)node.getParent()).isObjectResource())
		{
			ObjectResource parent = ((ORMutableTreeNode)node.getParent()).resourceFilter.getResource();
			Enumeration enum = parent.getChildren(((ORMutableTreeNode)node).name);

			Vector v = new Vector();
			while (enum.hasMoreElements())
			{
				ObjectResource res = (ObjectResource)enum.nextElement();
				v.add( new ObjectResourceTreeFilter (res, createFilter(res)));
			}
			ObjectResourceTreeBuilder.addTreeNodes((ObjectResourceTreeFilter[])v.toArray(new ObjectResourceTreeFilter[0]), node);
		}
	}

	public ORMutableTreeNode findNodeAtCurrent (ORMutableTreeNode startnode, ObjectResource resource)
	{
		for (int i=0; i<startnode.getChildCount(); i++)
		{
			ORMutableTreeNode node = (ORMutableTreeNode)startnode.getChildAt(i);
			if (node.isObjectResource())
				if (node.resourceFilter.getResource().equals(resource))
				{

					return node;
				}
		}
		return null;
	}

	public boolean findNode (ORMutableTreeNode startnode, ObjectResource resource)
	{
		String type = resource.getTyp();
		for (int i=0; i<startnode.getChildCount(); i++)
		{
			if (((ORMutableTreeNode)startnode.getChildAt(i)).name.equals(type))
			{
//				System.out.println("Found type \"" + type + "\" at " +  ((ORMutableTreeNode)startnode).name);
				ORMutableTreeNode node = (ORMutableTreeNode)startnode.getChildAt(i);
				expandNode(node);
				for (int j = 0; j < node.getChildCount(); j++)
				{
					ORMutableTreeNode rn = ((ORMutableTreeNode)node.getChildAt(j));
					ObjectResourceTreeFilter rf = rn.resourceFilter;
					ObjectResource r = rf.getResource();
					if (r.equals(resource))
					{
//						System.out.println("ObjectResource \"" + resource.getName() + "\" found at " + ((ORMutableTreeNode)node).name);
						path.add(node.getChildAt(j));
						path.add(node);
						path.add(node.getParent());
						return true;
					}
				}
			}
		}
		for (int i=0; i<startnode.getChildCount(); i++)
		{
			ORMutableTreeNode node = (ORMutableTreeNode)startnode.getChildAt(i);
			expandNode(node);
			if (findNode (node, resource))
			{
				path.add(startnode);
				return true;
			}
		}
			return false;
	}

	public boolean findNode (ORMutableTreeNode startnode, String type)
	{
		int n = startnode.getChildCount();
		for (int i=0; i<n; i++)
		{
			if (((ORMutableTreeNode)startnode.getChildAt(i)).name.equals(type))
			{
//				System.out.println("Found type \"" + type + "\" at " +  ((ORMutableTreeNode)startnode).name);
				path.add((ORMutableTreeNode)startnode.getChildAt(i));
				path.add(startnode);
				return true;
			}
		}
		for (int i=0; i<n; i++)
		{
			ORMutableTreeNode node = (ORMutableTreeNode)startnode.getChildAt(i);
			expandNode(node);
			if (findNode (node, type));
			{
				path.add(startnode);
				return true;
			}
		}
		return false;
	}

	public void expandNodeFound()
	{
		int size = path.size();

		ORMutableTreeNode[] nodeFoundPath = new ORMutableTreeNode[size];
		for (int i = 0; i < size; i++)
			nodeFoundPath[i] = (ORMutableTreeNode)path.get(size - i - 1);

		tree.expandPath(new TreePath (nodeFoundPath).getParentPath());
		tree.setSelectionPath(new TreePath (nodeFoundPath));
		path = new Vector();
	}

	void refreshCurrentNode()
	{
		curr_type_node.removeAllChildren();
		curr_type_node.expanded = false;

		Hashtable h = Pool.getHash(curr_type_node.name);
		if (h != null)
		{
			Vector v = new Vector();
			for (Enumeration enum = h.elements(); enum.hasMoreElements();)
			{
				ObjectResource res = (ObjectResource)enum.nextElement();
				v.add( new ObjectResourceTreeFilter (res, createFilter(res)));
			}
			ObjectResourceTreeBuilder.addTreeNodes((ObjectResourceTreeFilter[])v.toArray(new ObjectResourceTreeFilter[0]), curr_type_node);
		}
		tree.updateUI();
	}
}

class ORMutableTreeNode extends DefaultMutableTreeNode
{
	public ObjectResourceTreeFilter resourceFilter;
	public String name;
	boolean isOR = false;
	boolean expanded;

	public ORMutableTreeNode (String name)
	{
		super (name, true);
		String str = LangModel.String("node" + name);
		if (!str.startsWith("ERROR"))
			super.setUserObject(str);
		this.name = name;
		isOR = false;
	}

	public ORMutableTreeNode (ObjectResourceTreeFilter resourceFilter)
	{
		super (resourceFilter.getName(), true);
		this.resourceFilter = resourceFilter;
		name = resourceFilter.getName();
		isOR = true;
	}

	public boolean isObjectResource ()
	{
		return isOR;
	}
}

class ObjectResourceTreeBuilder
{
	public static void addTreeNodes (ObjectResourceTreeFilter[] objs, ORMutableTreeNode root)
	{
		for (int i = 0; i < objs.length; i++)
		{
			ObjectResourceTreeFilter obj = objs[i];
			ORMutableTreeNode node = new ORMutableTreeNode(obj);
			Enumeration child_types = obj.getChildTypes();
			while (child_types.hasMoreElements())
			{
				String type = (String)child_types.nextElement();
				ORMutableTreeNode child_node = new ORMutableTreeNode(type);
				node.add(child_node);
			}
			if (!node.children().hasMoreElements())
				node.setAllowsChildren(false);
			root.add(node);
		}
	}

	public static void addTreeNodes (Enumeration objs, ORMutableTreeNode root)
	{
		for (; objs.hasMoreElements();)
		{
			ObjectResourceTreeFilter obj = (ObjectResourceTreeFilter)objs.nextElement();
			ORMutableTreeNode node = new ORMutableTreeNode(obj);
			Enumeration child_types = obj.getChildTypes();
			while (child_types.hasMoreElements())
			{
				String type = (String)child_types.nextElement();
				ORMutableTreeNode child_node = new ORMutableTreeNode(type);
				node.add(child_node);
			}
			if (!node.children().hasMoreElements())
				node.setAllowsChildren(false);
			root.add(node);
		}
	}
}
