package com.syrus.AMFICOM.Client.Map;

import java.util.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.tree.*;
import java.awt.dnd.*;
import java.awt.event.*;
import java.awt.datatransfer.*;

import com.syrus.AMFICOM.Client.General.Lang.*;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.Scheme.*;
import com.syrus.AMFICOM.Client.Resource.Map.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JButton;

import com.syrus.AMFICOM.Client.Map.*;

public class MapSchemeTreePanel extends JPanel 
		implements 
			OperationListener,
			DragGestureListener
{
	private BorderLayout borderLayout1 = new BorderLayout();
	JTree tree;
	JScrollPane treepane;
	DragSource dragSource = null;

	Scheme scheme;
	ApplicationContext aContext;

	public boolean perform_processing = true;
	private JPanel jPanel1 = new JPanel();
	private BorderLayout borderLayout2 = new BorderLayout();
	private JButton placeButton = new JButton();
	private JButton placeAllButton = new JButton();

	public MapSchemeTreePanel()
	{
		try
		{
			jbInit();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	public MapSchemeTreePanel(String title, ApplicationContext aContext)
	{
		this();
		setContext(aContext);
	}

	private void jbInit() throws Exception
	{
		this.setLayout(borderLayout1);

//		placeButton.setEnabled(false);
		treepane = new JScrollPane();
		jPanel1.setLayout(borderLayout2);
		placeButton.setText("Разместить");
		placeButton.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					placeButton_actionPerformed(e);
				}
			});
		placeAllButton.setText("Разместить все");
		placeAllButton.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					placeAllButton_actionPerformed(e);
				}
			});
		treepane.setAutoscrolls(true);
		this.add(treepane, BorderLayout.CENTER);
		jPanel1.add(placeButton, BorderLayout.WEST);
		jPanel1.add(placeAllButton, BorderLayout.EAST);
		this.add(jPanel1, BorderLayout.SOUTH);

		setDefaultTree();
	}

	public void setContext(ApplicationContext aContext)
	{
		Dispatcher disp;
		if(this.aContext != null)
		{
			disp = this.aContext.getDispatcher();
			if(disp != null)
			{
				disp.unregister(this, MapNavigateEvent.type);
				disp.unregister(this, "mapselectevent");
				disp.unregister(this, "mapdeselectevent");
			}
		}

		this.aContext = aContext;
		if(aContext == null)
			return;

		disp = this.aContext.getDispatcher();
		if(disp != null)
		{
			disp.register(this, MapNavigateEvent.type);
			disp.register(this, "mapselectevent");
			disp.register(this, "mapdeselectevent");
//			disp.register(this, "mapchangeevent");
//			disp.register(this, "mapselectionchangeevent");
		}
	}
	
	public void setVisible(boolean isVisible)
	{
		super.setVisible(isVisible);
		if(!isVisible)
			return;
		
		MapMainFrame mmf = (MapMainFrame )Pool.get("environment", "mapmainframe");
		if(mmf != null)
			if(mmf.isVisible())
				if(mmf.getParent().equals(this.getParent()))
				{
					MapContext mc = mmf.getMapContext();
					scheme = (Scheme )Pool.get(Scheme.typ, mc.scheme_id);
					updateTree();
				}
	}

	public void operationPerformed(OperationEvent oe )
	{
		if(oe.getActionCommand().equals("mapdeselectevent"))
			if(perform_processing)
			{
				scheme = null;
				this.setDefaultTree();
			}
		if(	oe.getActionCommand().equals("mapselectevent"))
			if(perform_processing)
			{
				MapContext mc = (MapContext )oe.getSource();
				Scheme scheme1 = (Scheme )Pool.get(Scheme.typ, mc.scheme_id);
				if(!scheme1.equals(scheme))
				{
					scheme = scheme1;
					updateTree();
				}
			}
/*
		if(	oe.getActionCommand().equals("mapchangeevent"))
			updateTree();
		if(	oe.getActionCommand().equals("mapselectionchangeevent"))
			updateTree();
*/
		if(oe.getActionCommand().equals(MapNavigateEvent.type))
		{
			MapNavigateEvent mne = (MapNavigateEvent )oe;
			if(mne.MAP_ELEMENT_SELECTED)
			{
				ObjectResource me = (ObjectResource )mne.getSource();
//				System.out.println("PROPPANEL: Selecting " + me.getId());
//				panel.setSelected(me);
//				System.out.println("PROPPANEL: " + me.getId() + " selected ok");
			}
		}
	}

	public void updateTree()
	{
		if(tree != null)
			treepane.getViewport().remove(tree);

		MyMapSchemeTreeNode root = getSchemeNodeFor(scheme);

		DefaultTreeModel myModel = new DefaultTreeModel(root);
		tree = new JTree(myModel);
		tree.setCellRenderer(new MyMapSchemeRenderer());

		dragSource = new DragSource();
		dragSource.createDefaultDragGestureRecognizer(tree, DnDConstants.ACTION_MOVE, this);

		treepane.getViewport().add(tree);
	}

	public MyMapSchemeTreeNode getSchemeNodeFor(Scheme scheme)
	{
		MyMapSchemeTreeNode root = new MyMapSchemeTreeNode("Схема - " + scheme.getName(), scheme.getId());

		MyMapSchemeTreeNode elements = new MyMapSchemeTreeNode("Узлы", "element");
		MyMapSchemeTreeNode cables = new MyMapSchemeTreeNode("Линии связи", "cable");
		MyMapSchemeTreeNode paths = new MyMapSchemeTreeNode("Пути", "path");
		MyMapSchemeTreeNode topos = new MyMapSchemeTreeNode("Топологические узлы", "topo");

		for(Iterator it = scheme.getTopLevelElements().iterator(); it.hasNext();)
		{
			SchemeElement se = (SchemeElement )it.next();
			if(se.scheme_id != null && !se.scheme_id.equals(""))
			{
				Scheme sc = (Scheme )Pool.get(Scheme.typ, se.scheme_id);
				if(sc != null)
				{
					if(sc.scheme_type.equals(Scheme.CABLESUBNETWORK))
						elements.add(getSchemeNodeFor(sc));
					else
					{
						MyMapSchemeTreeNode node = new MyMapSchemeTreeNode(se);
						elements.add(node);
					}
				}
			}
			else
			{
				MyMapSchemeTreeNode node = new MyMapSchemeTreeNode(se);
				elements.add(node);
			}
		}

		for(Iterator it = scheme.getTopLevelCableLinks().iterator(); it.hasNext();)
		{
			ObjectResource se = (ObjectResource )it.next();
			MyMapSchemeTreeNode node = new MyMapSchemeTreeNode(se);
			cables.add(node);
		}

		for(Iterator it = scheme.getTopLevelPaths().iterator(); it.hasNext();)
		{
			ObjectResource se = (ObjectResource )it.next();
			MyMapSchemeTreeNode node = new MyMapSchemeTreeNode(se);
			paths.add(node);
		}

		for(Enumeration enum = Pool.getHash(MapProtoElement.typ).elements(); enum.hasMoreElements();)
		{
			MapProtoElement mpe = (MapProtoElement )enum.nextElement();
			if(mpe.getId().equals("well"))
			{
				MyMapSchemeTreeNode node = new MyMapSchemeTreeNode(mpe);
				topos.add(node);
			}
		}

		root.add(elements);
		root.add(cables);
		root.add(paths);
//		root.add(topos);
		return root;
	}

	public void setDefaultTree()
	{
		if(tree != null)
			treepane.getViewport().remove(tree);
		tree = new JTree();
		MyMapSchemeTreeNode root = new MyMapSchemeTreeNode("Схема", "");
		MyMapSchemeTreeNode elements = new MyMapSchemeTreeNode("Узлы", "element");
		MyMapSchemeTreeNode cables = new MyMapSchemeTreeNode("Линии связи", "cable");
		MyMapSchemeTreeNode paths = new MyMapSchemeTreeNode("Пути", "path");
		MyMapSchemeTreeNode topos = new MyMapSchemeTreeNode("Топологические узлы", "topo");
		root.add(elements);
		root.add(cables);
		root.add(paths);
		root.add(topos);
		DefaultTreeModel myModel = new DefaultTreeModel(root);
		tree = new JTree(myModel);
		tree.setCellRenderer(new MyMapSchemeRenderer());

		dragSource = new DragSource();
		dragSource.createDefaultDragGestureRecognizer(tree, DnDConstants.ACTION_MOVE, this);

		treepane.getViewport().add(tree);
	}

	public void dragGestureRecognized( DragGestureEvent event)
	{
//		System.out.println("dragGestureRecognized");

		Point origin = event.getDragOrigin();
		TreePath tp = tree.getClosestPathForLocation(origin.x, origin.y);
		MyMapSchemeTreeNode mmstn = (MyMapSchemeTreeNode )tp.getLastPathComponent();
		if(mmstn.elementLabel != null)
			mmstn.elementLabel.dragGestureRecognized(event);
	}

	private void placeButton_actionPerformed(ActionEvent e)
	{
		TreePath tp = tree.getSelectionPath();
		if(tp == null)
			return;
		MyMapSchemeTreeNode mmstn = (MyMapSchemeTreeNode )tp.getLastPathComponent();
		if(mmstn == null)
			return;
		if(mmstn.elementLabel == null)
			return;
		Dispatcher disp = aContext.getDispatcher();
		if(disp == null)
			return;
		disp.notify(new OperationEvent(mmstn.elementLabel, 0, "placeElement"));
	}

	private void placeAllButton_actionPerformed(ActionEvent e)
	{
		MyMapSchemeTreeNode mmstn;
		MyMapSchemeTreeNode node;
		Enumeration enum;

		Dispatcher disp = aContext.getDispatcher();
		if(disp == null)
			return;

		// Elements
		mmstn = (MyMapSchemeTreeNode )tree.getModel().getChild(tree.getModel().getRoot(), 0);
		for(enum = mmstn.children(); enum.hasMoreElements();)
		{
			node = (MyMapSchemeTreeNode )enum.nextElement();
			disp.notify(new OperationEvent(mmstn.elementLabel, 0, "placeElement"));
		}

		// Links
		mmstn = (MyMapSchemeTreeNode )tree.getModel().getChild(tree.getModel().getRoot(), 1);
		for(enum = mmstn.children(); enum.hasMoreElements();)
		{
			node = (MyMapSchemeTreeNode )enum.nextElement();
			disp.notify(new OperationEvent(mmstn.elementLabel, 0, "placeElement"));
		}

		// Paths
		mmstn = (MyMapSchemeTreeNode )tree.getModel().getChild(tree.getModel().getRoot(), 2);
		for(enum = mmstn.children(); enum.hasMoreElements();)
		{
			node = (MyMapSchemeTreeNode )enum.nextElement();
			disp.notify(new OperationEvent(mmstn.elementLabel, 0, "placeElement"));
		}

	}

}