package com.syrus.AMFICOM.Client.Map.UI;

import com.syrus.AMFICOM.Client.General.Event.Dispatcher;
import com.syrus.AMFICOM.Client.General.Event.MapEvent;
import com.syrus.AMFICOM.Client.General.Event.OperationEvent;
import com.syrus.AMFICOM.Client.General.Event.OperationListener;
import com.syrus.AMFICOM.Client.General.Event.SchemeNavigateEvent;
import com.syrus.AMFICOM.Client.General.Event.StatusMessageEvent;
import com.syrus.AMFICOM.Client.General.Lang.LangModel;
import com.syrus.AMFICOM.Client.General.Lang.LangModelMap;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.Model.MapApplicationModel;
import com.syrus.AMFICOM.Client.General.UI.ObjectResourceChooserDialog;
import com.syrus.AMFICOM.Client.General.UI.ObjectResourceTreeNode;
import com.syrus.AMFICOM.Client.General.UI.UniTreePanel;
import com.syrus.AMFICOM.Client.Resource.DataSourceInterface;
import com.syrus.AMFICOM.Client.Resource.MapView.MapView;
import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.administration.AdministrationStorableObjectPool;
import com.syrus.AMFICOM.configuration.ConfigurationStorableObjectPool;
import com.syrus.AMFICOM.administration.Domain;
import com.syrus.AMFICOM.administration.DomainCondition;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.scheme.SchemeStorableObjectPool;
import com.syrus.AMFICOM.scheme.corba.*;
import com.syrus.AMFICOM.client_.general.ui_.ObjectResourceTableModel;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.Enumeration;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

public final class MapSchemeTreePanel extends JPanel 
		implements OperationListener, TreeSelectionListener
{
	UniTreePanel treePanel = null;
	MapSchemeTreeModel model = null;

	ApplicationContext aContext;

	public boolean performProcessing = true;

	private JScrollPane scroll = new JScrollPane();
	private BorderLayout borderLayout1 = new BorderLayout();
	private BorderLayout borderLayout2 = new BorderLayout();

	private JPanel bindButtonsPanel = new JPanel();
	private JButton placeButton = new JButton();
	private JButton placeAllButton = new JButton();

	private JPanel schemeButtonsPanel = new JPanel();

	JButton menuSchemeAddToView = new JButton();
	JButton menuSchemeRemoveFromView = new JButton();

	public final static int imgSize = 16;
	public final static int btnSize = 24;

	static final Dimension buttonSize = new Dimension(btnSize, btnSize);
	
	private MapView mapView = null;

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

	private void jbInit()
	{
		this.setLayout(borderLayout1);

		bindButtonsPanel.setLayout(borderLayout2);
		placeButton.setText(LangModelMap.getString("Place"));
		placeButton.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					placeSelection();
				}
			});
		placeAllButton.setText(LangModelMap.getString("PlaceAll"));
		placeAllButton.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					placeAll();
				}
			});
		bindButtonsPanel.add(placeButton, BorderLayout.WEST);
		bindButtonsPanel.add(placeAllButton, BorderLayout.EAST);

		menuSchemeAddToView.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/addtoview.gif").
				getScaledInstance(imgSize, imgSize, Image.SCALE_DEFAULT)));
		menuSchemeAddToView.setMaximumSize(buttonSize);
		menuSchemeAddToView.setPreferredSize(buttonSize);
		menuSchemeAddToView.setToolTipText(LangModelMap.getString("menuSchemeAddToView"));
		menuSchemeAddToView.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					addToView();
				}
			});

		menuSchemeRemoveFromView.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/removefromview.gif").
				getScaledInstance(imgSize, imgSize, Image.SCALE_DEFAULT)));
		menuSchemeRemoveFromView.setMaximumSize(buttonSize);
		menuSchemeRemoveFromView.setPreferredSize(buttonSize);
		menuSchemeRemoveFromView.setToolTipText(LangModelMap.getString("menuSchemeRemoveFromView"));
		menuSchemeRemoveFromView.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					removeFromView();
				}
			});

		schemeButtonsPanel.add(menuSchemeAddToView);
		schemeButtonsPanel.add(menuSchemeRemoveFromView);


		scroll.setWheelScrollingEnabled(true);
		scroll.setAutoscrolls(true);

		this.add(scroll, BorderLayout.CENTER);
		this.add(bindButtonsPanel, BorderLayout.SOUTH);
		this.add(schemeButtonsPanel, BorderLayout.NORTH);
	}

	public void setContext(ApplicationContext aContext)
	{
		if(this.aContext != null)
			if(this.aContext.getDispatcher() != null)
			{
				Dispatcher disp = this.aContext.getDispatcher();
				disp.unregister(this, MapEvent.MAP_NAVIGATE);
				disp.unregister(this, MapEvent.MAP_VIEW_CHANGED);
				disp.unregister(this, MapEvent.MAP_VIEW_SELECTED);
				disp.unregister(this, MapEvent.MAP_VIEW_CLOSED);
			}
		this.aContext = aContext;
		if(aContext == null)
			return;

		Dispatcher disp = aContext.getDispatcher();
		if(disp == null)
			return;
		disp.register(this, MapEvent.MAP_NAVIGATE);
		disp.register(this, MapEvent.MAP_VIEW_CHANGED);
		disp.register(this, MapEvent.MAP_VIEW_SELECTED);
		disp.register(this, MapEvent.MAP_VIEW_CLOSED);

		initTree();

		updateTree(null);
	}

	public void operationPerformed(OperationEvent oe )
	{
		if(	oe.getActionCommand().equals(MapEvent.MAP_VIEW_CLOSED))
			if(performProcessing)
			{
				updateTree(null);
			}
		if(	oe.getActionCommand().equals(MapEvent.MAP_VIEW_SELECTED))
			if(performProcessing)
			{
				MapView mv = (MapView)oe.getSource();
				if(model == null || model.mv == null || !model.mv.equals(mv))
				{
					updateTree(mv);
				}
			}
		if(	oe.getActionCommand().equals(MapEvent.MAP_VIEW_CHANGED))
			if(performProcessing)
			{
				MapView mv = (MapView )oe.getSource();
				updateTree(mv);
			}
//		if(oe.getActionCommand().equals(MapEvent.MAP_NAVIGATE))
//		{
//			MapNavigateEvent mne = (MapNavigateEvent )oe;
//			if(mne.isMapElementSelected())
//			{
//				ObjectResource me = (ObjectResource )mne.getSource();
//			}
//		}
	}
	
	public void setPanel(UniTreePanel utp)
	{
		if(treePanel != null)
		{
			scroll.getViewport().remove(treePanel);
			treePanel.getTree().removeTreeSelectionListener(this);
		}
		treePanel = utp;
		if(treePanel != null)
		{
			scroll.getViewport().add(treePanel);
			treePanel.getTree().addTreeSelectionListener(this);
		}
	}

	protected void initTree()
	{
		model = new MapSchemeTreeModel(null, aContext.getDataSource());
		setPanel(new UniTreePanel(aContext.getDispatcher(), aContext, model));
	}

	public void updateTree(MapView mv)
	{
		mapView = mv;
		model.setMapView(mv);
		treePanel.setModel(model);

		this.menuSchemeRemoveFromView.setEnabled(false);
	}

	private void placeSelection()
	{
		TreePath tp = treePanel.getTree().getSelectionPath();
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

	private void placeAll()
	{
		ObjectResourceTreeNode ortn;

		Dispatcher disp = aContext.getDispatcher();
		if(disp == null)
			return;

		// Elements
		ortn = (ObjectResourceTreeNode )treePanel.getTree().getSelectionPath().getLastPathComponent();
		sendPlaceEvent(ortn);
	}
	
	private void sendPlaceEvent(ObjectResourceTreeNode ortn)
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

	private void addToView()
	{
		DataSourceInterface dataSource = aContext.getDataSource();

		if(mapView == null)
			return;

		aContext.getDispatcher().notify(new StatusMessageEvent(
				StatusMessageEvent.STATUS_MESSAGE,
				LangModelMap.getString("MapOpening")));

		ObjectResourceChooserDialog mcd = new ObjectResourceChooserDialog(
				SchemeController.getInstance(), 
				ObjectEntities.SCHEME_ENTITY);

	
		try
		{
			Identifier domainId = new Identifier(
				aContext.getSessionInterface().getAccessIdentifier().domain_id);
			Domain domain = (Domain )AdministrationStorableObjectPool.getStorableObject(
					domainId,
					false);
			DomainCondition condition = new DomainCondition(
				domain,
				ObjectEntities.SCHEME_ENTITY_CODE);
			List ss = SchemeStorableObjectPool.getStorableObjectsByCondition(condition, true);
			mcd.setContents(ss);
		}
		catch (ApplicationException e)
		{
			e.printStackTrace();
			return;
		}

		mcd.setModal(true);
		mcd.setVisible(true);
		if(mcd.getReturnCode() == mcd.RET_CANCEL)
		{
			aContext.getDispatcher().notify(new StatusMessageEvent(
					StatusMessageEvent.STATUS_MESSAGE,
					LangModel.getString("Aborted")));
			return;
		}

		if(mcd.getReturnCode() == mcd.RET_OK)
		{
			Scheme retObj = (Scheme )mcd.getReturnObject();

			if(!mapView.getSchemes().contains(retObj))
			{
				mapView.addScheme((Scheme )retObj);
				aContext.getDispatcher().notify(new MapEvent(
						mapView,
						MapEvent.MAP_VIEW_CHANGED));
				mapView.getLogicalNetLayer().repaint(false);
			}
			aContext.getDispatcher().notify(new StatusMessageEvent(
					StatusMessageEvent.STATUS_MESSAGE,
					LangModel.getString("Finished")));
		}
	}
	
	private void removeFromView()
	{
		ObjectResourceTreeNode node = (ObjectResourceTreeNode )
				treePanel.getTree().getSelectionPath().getLastPathComponent();
		Scheme scheme = (Scheme )node.getObject();

		mapView.removeScheme(scheme);
		aContext.getDispatcher().notify(new MapEvent(
				mapView,
				MapEvent.MAP_VIEW_CHANGED));
		mapView.getLogicalNetLayer().repaint(false);

		aContext.getDispatcher().notify(new StatusMessageEvent(
				StatusMessageEvent.STATUS_MESSAGE,
				LangModel.getString("Finished")));
	}

	public void valueChanged(TreeSelectionEvent e)
	{
		ObjectResourceTreeNode node = (ObjectResourceTreeNode )e.getPath().getLastPathComponent();
		Object sel = null;
		long msgType = 0;
		this.menuSchemeRemoveFromView.setEnabled(node.getObject() instanceof Scheme);
		if(node.getObject() instanceof SchemeCableLink)
		{
			sel = new SchemeCableLink[] { (SchemeCableLink )node.getObject() };
			msgType = SchemeNavigateEvent.SCHEME_CABLE_LINK_SELECTED_EVENT;
		}
		else
		if(node.getObject() instanceof SchemeElement)
		{
			sel = new SchemeElement[] { (SchemeElement )node.getObject() };
			msgType = SchemeNavigateEvent.SCHEME_ELEMENT_SELECTED_EVENT;
		}
		else
		if(node.getObject() instanceof SchemePath)
		{
			sel = new SchemePath[] { (SchemePath )node.getObject() };
			msgType = SchemeNavigateEvent.SCHEME_PATH_SELECTED_EVENT;
		}
		if(sel != null)
		{
			Dispatcher disp = aContext.getDispatcher();
			if(disp != null)
			{
				if(mapView != null)
				{
					mapView.deselectAll();
					disp.notify(new MapEvent(this, MapEvent.SELECTION_CHANGED));
				}

				disp.notify(new SchemeNavigateEvent(
						sel, 
						msgType));
			}
		}
	}
}
