package com.syrus.AMFICOM.Client.Map.UI;

import com.syrus.AMFICOM.Client.General.Event.Dispatcher;
import com.syrus.AMFICOM.Client.General.Event.OperationEvent;
import com.syrus.AMFICOM.Client.General.Event.OperationListener;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.UI.ObjectResourceTreeNode;
import com.syrus.AMFICOM.Client.General.UI.UniTreePanel;
import com.syrus.AMFICOM.Client.Resource.ObjectResource;

import com.syrus.AMFICOM.Client.General.Event.MapEvent;
import com.syrus.AMFICOM.Client.General.Event.MapNavigateEvent;
import com.syrus.AMFICOM.Client.General.Lang.LangModelMap;
import com.syrus.AMFICOM.Client.Map.UI.MapSchemeTreeModel;
import com.syrus.AMFICOM.Client.Resource.MapView.MapView;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.Enumeration;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.tree.TreePath;

public final class MapSchemeTreePanel extends JPanel 
		implements OperationListener
{
	UniTreePanel panel = null;
	MapSchemeTreeModel model = null;

	ApplicationContext aContext;

	public boolean performProcessing = true;

	private JScrollPane scroll = new JScrollPane();
	private JPanel jPanel1 = new JPanel();
	private BorderLayout borderLayout1 = new BorderLayout();
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

		jPanel1.setLayout(borderLayout2);
		placeButton.setText(LangModelMap.getString("Place"));
		placeButton.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					placeButton_actionPerformed(e);
				}
			});
		placeAllButton.setText(LangModelMap.getString("PlaceAll"));
		placeAllButton.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					placeAllButton_actionPerformed(e);
				}
			});
		jPanel1.add(placeButton, BorderLayout.WEST);
		jPanel1.add(placeAllButton, BorderLayout.EAST);

		scroll.setWheelScrollingEnabled(true);
		scroll.setAutoscrolls(true);

		this.add(scroll, BorderLayout.CENTER);
		this.add(jPanel1, BorderLayout.SOUTH);
	}

	public void setContext(ApplicationContext aContext)
	{
		if(this.aContext != null)
			if(this.aContext.getDispatcher() != null)
			{
				Dispatcher disp = this.aContext.getDispatcher();
				disp.unregister(this, MapEvent.MAP_NAVIGATE);
				disp.unregister(this, MapEvent.MAP_VIEW_CHANGED);
			}
		this.aContext = aContext;
		if(aContext == null)
			return;
		Dispatcher disp = aContext.getDispatcher();
		if(disp == null)
			return;
		disp.register(this, MapEvent.MAP_NAVIGATE);
		disp.register(this, MapEvent.MAP_VIEW_CHANGED);

		initTree();

		updateTree(null);
	}

	public void operationPerformed(OperationEvent oe )
	{
		if(	oe.getActionCommand().equals(MapEvent.MAP_VIEW_CHANGED))
			if(performProcessing)
			{
				MapView mv = (MapView)oe.getSource();
				updateTree(mv);
			}
		if(oe.getActionCommand().equals(MapEvent.MAP_NAVIGATE))
		{
			MapNavigateEvent mne = (MapNavigateEvent )oe;
			if(mne.MAP_ELEMENT_SELECTED)
			{
				ObjectResource me = (ObjectResource )mne.getSource();
			}
		}
	}
	
	public void setPanel(UniTreePanel utp)
	{
		if(panel != null)
			scroll.getViewport().remove(panel);
		panel = utp;
		if(panel != null)
			scroll.getViewport().add(panel);
	}

	protected void initTree()
	{
		model = new MapSchemeTreeModel(null, aContext.getDataSourceInterface());
		setPanel(new UniTreePanel(aContext.getDispatcher(), aContext, model));
	}

	public void updateTree(MapView mv)
	{
		model.setMapView(mv);
		panel.setModel(model);
	}

	private void placeButton_actionPerformed(ActionEvent e)
	{
		TreePath tp = panel.getTree().getSelectionPath();
		if(tp == null)
			return;
		ObjectResourceTreeNode ortn = (ObjectResourceTreeNode )tp.getLastPathComponent();
		if(ortn == null)
			return;
		if(!ortn.isDragDropEnabled())
			return;
		Dispatcher disp = aContext.getDispatcher();
		if(disp == null)
			return;
		disp.notify(new MapEvent(ortn.getObject(), MapEvent.PLACE_ELEMENT));
	}

	private void placeAllButton_actionPerformed(ActionEvent e)
	{
		ObjectResourceTreeNode ortn;
		ObjectResourceTreeNode node;
		Enumeration enum;

		Dispatcher disp = aContext.getDispatcher();
		if(disp == null)
			return;

		// Elements
		ortn = (ObjectResourceTreeNode )panel.getTree().getSelectionPath().getLastPathComponent();
		sendPlaceEvent(ortn);
	}
	
	protected void sendPlaceEvent(ObjectResourceTreeNode ortn)
	{
		ObjectResourceTreeNode node = null;
		
		if(ortn.isDragDropEnabled())
			if(aContext.getDispatcher() != null)
				aContext.getDispatcher().notify(
					new MapEvent(ortn.getObject(), MapEvent.PLACE_ELEMENT));
		else
		{
			for(Enumeration en = ortn.children(); en.hasMoreElements();)
			{
				node = (ObjectResourceTreeNode )en.nextElement();
				sendPlaceEvent(node);
			}
		}
	}

}
