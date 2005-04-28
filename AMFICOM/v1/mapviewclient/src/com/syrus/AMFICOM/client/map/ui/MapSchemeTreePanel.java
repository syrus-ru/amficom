package com.syrus.AMFICOM.Client.Map.UI;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Enumeration;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;

import com.syrus.AMFICOM.Client.General.Event.Dispatcher;
import com.syrus.AMFICOM.Client.General.Event.MapEvent;
import com.syrus.AMFICOM.Client.General.Event.OperationEvent;
import com.syrus.AMFICOM.Client.General.Event.OperationListener;
import com.syrus.AMFICOM.Client.General.Event.SchemeNavigateEvent;
import com.syrus.AMFICOM.Client.General.Lang.LangModelMap;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.Model.MapEditorApplicationModel;
import com.syrus.AMFICOM.Client.General.UI.ObjectResourceTreeNode;
import com.syrus.AMFICOM.Client.General.UI.UniTreePanel;
import com.syrus.AMFICOM.Client.Map.Command.Map.MapViewAddSchemeCommand;
import com.syrus.AMFICOM.Client.Map.Command.Map.MapViewRemoveSchemeCommand;
import com.syrus.AMFICOM.mapview.MapView;
import com.syrus.AMFICOM.scheme.*;
import com.syrus.AMFICOM.scheme.Scheme;

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

	public static final int IMG_SIZE = 16;
	public static final int BTN_SIZE = 24;

	static final Dimension BUTTON_DIMENSION = new Dimension(BTN_SIZE, BTN_SIZE);
	
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

	public MapSchemeTreePanel(ApplicationContext aContext)
	{
		this();
		setContext(aContext);
	}

	private void jbInit()
	{
		this.setLayout(this.borderLayout1);

		this.bindButtonsPanel.setLayout(this.borderLayout2);
		this.placeButton.setText(LangModelMap.getString("Place"));
		this.placeButton.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					placeSelection();
				}
			});
		this.placeAllButton.setText(LangModelMap.getString("PlaceAll"));
		this.placeAllButton.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					placeAll();
				}
			});
		this.bindButtonsPanel.add(this.placeButton, BorderLayout.WEST);
		this.bindButtonsPanel.add(this.placeAllButton, BorderLayout.EAST);

		this.menuSchemeAddToView.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/addtoview.gif").
				getScaledInstance(IMG_SIZE, IMG_SIZE, Image.SCALE_DEFAULT)));
		this.menuSchemeAddToView.setMaximumSize(BUTTON_DIMENSION);
		this.menuSchemeAddToView.setPreferredSize(BUTTON_DIMENSION);
		this.menuSchemeAddToView.setToolTipText(LangModelMap.getString(MapEditorApplicationModel.ITEM_MAP_VIEW_ADD_SCHEME));
		this.menuSchemeAddToView.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					addToView();
				}
			});

		this.menuSchemeRemoveFromView.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/removefromview.gif").
				getScaledInstance(IMG_SIZE, IMG_SIZE, Image.SCALE_DEFAULT)));
		this.menuSchemeRemoveFromView.setMaximumSize(BUTTON_DIMENSION);
		this.menuSchemeRemoveFromView.setPreferredSize(BUTTON_DIMENSION);
		this.menuSchemeRemoveFromView.setToolTipText(LangModelMap.getString(MapEditorApplicationModel.ITEM_MAP_VIEW_REMOVE_SCHEME));
		this.menuSchemeRemoveFromView.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					removeFromView();
				}
			});

		this.schemeButtonsPanel.add(this.menuSchemeAddToView);
		this.schemeButtonsPanel.add(this.menuSchemeRemoveFromView);


		this.scroll.setWheelScrollingEnabled(true);
		this.scroll.setAutoscrolls(true);

		this.add(this.scroll, BorderLayout.CENTER);
		this.add(this.bindButtonsPanel, BorderLayout.SOUTH);
		this.add(this.schemeButtonsPanel, BorderLayout.NORTH);
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
			if(this.performProcessing)
			{
				updateTree(null);
			}
		if(	oe.getActionCommand().equals(MapEvent.MAP_VIEW_SELECTED))
			if(this.performProcessing)
			{
				MapView mv = (MapView)oe.getSource();
				if(this.model == null || this.model.mapView == null || !this.model.mapView.equals(mv))
				{
					updateTree(mv);
				}
			}
		if(	oe.getActionCommand().equals(MapEvent.MAP_VIEW_CHANGED))
			if(this.performProcessing)
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
		if(this.treePanel != null)
		{
			this.scroll.getViewport().remove(this.treePanel);
			this.treePanel.getTree().removeTreeSelectionListener(this);
		}
		this.treePanel = utp;
		if(this.treePanel != null)
		{
			this.scroll.getViewport().add(this.treePanel);
			this.treePanel.getTree().addTreeSelectionListener(this);
		}
	}

	protected void initTree()
	{
//		this.model = new MapSchemeTreeModel(null);
//		setPanel(new UniTreePanel(this.aContext.getDispatcher(), this.aContext, this.model));
	}

	public void updateTree(MapView mv)
	{
		this.mapView = mv;
//		this.model.setMapView(mv);
//		this.treePanel.setModel(this.model);

		this.menuSchemeRemoveFromView.setEnabled(false);
	}

	void placeSelection()
	{
		TreePath tp = this.treePanel.getTree().getSelectionPath();
		if(tp == null)
			return;
		ObjectResourceTreeNode ortn = (ObjectResourceTreeNode )tp.getLastPathComponent();
		if(ortn == null)
			return;
		if(!ortn.isDragDropEnabled())
			return;
		Dispatcher disp = this.aContext.getDispatcher();
		if(disp == null)
			return;
		disp.notify(new MapEvent(ortn.getObject(), MapEvent.PLACE_ELEMENT));
	}

	void placeAll()
	{
		ObjectResourceTreeNode ortn;

		Dispatcher disp = this.aContext.getDispatcher();
		if(disp == null)
			return;

		// Elements
		ortn = (ObjectResourceTreeNode )this.treePanel.getTree().getSelectionPath().getLastPathComponent();
		sendPlaceEvent(ortn);
	}
	
	private void sendPlaceEvent(ObjectResourceTreeNode ortn)
	{
		ObjectResourceTreeNode node = null;
		
		if(ortn.isDragDropEnabled())
			if(this.aContext.getDispatcher() != null)
				this.aContext.getDispatcher().notify(
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

	void addToView()
	{
		if(this.mapView == null)
			return;
		
		MapViewAddSchemeCommand command = (MapViewAddSchemeCommand )
			this.aContext.getApplicationModel().getCommand(MapEditorApplicationModel.ITEM_MAP_VIEW_ADD_SCHEME);
		command.execute();
/*
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
		if(mcd.getReturnCode() == ObjectResourceChooserDialog.RET_CANCEL)
		{
			aContext.getDispatcher().notify(new StatusMessageEvent(
					StatusMessageEvent.STATUS_MESSAGE,
					LangModel.getString("Aborted")));
			return;
		}

		if(mcd.getReturnCode() == ObjectResourceChooserDialog.RET_OK)
		{
			Scheme retObj = (Scheme )mcd.getReturnObject();

			if(!mapView.getSchemes().contains(retObj))
			{
				MapViewController controller = getLogicalNetLayer().getMapViewController();
				controller.addScheme((Scheme )retObj);
				Dispatcher disp = aContext.getDispatcher();
				if(disp != null)
				{
					aContext.getDispatcher().notify(new MapEvent(
							mapView,
							MapEvent.MAP_VIEW_CHANGED));
//					getLogicalNetLayer().repaint(false);
					disp.notify(new MapEvent(this, MapEvent.NEED_REPAINT));
				}
			}
			aContext.getDispatcher().notify(new StatusMessageEvent(
					StatusMessageEvent.STATUS_MESSAGE,
					LangModel.getString("Finished")));
		}
*/
	}
	
	void removeFromView()
	{
		MapViewRemoveSchemeCommand command = (MapViewRemoveSchemeCommand )
			this.aContext.getApplicationModel().getCommand(MapEditorApplicationModel.ITEM_MAP_VIEW_REMOVE_SCHEME);
		command.execute();
/*
		ObjectResourceTreeNode node = (ObjectResourceTreeNode )
				treePanel.getTree().getSelectionPath().getLastPathComponent();
		Scheme scheme = (Scheme )node.getObject();

		MapViewController controller = getLogicalNetLayer().getMapViewController();
		controller.removeScheme(scheme);


		Dispatcher disp = aContext.getDispatcher();
		if(disp != null)
		{
			aContext.getDispatcher().notify(new MapEvent(
					mapView,
					MapEvent.MAP_VIEW_CHANGED));
//			getLogicalNetLayer().repaint(false);
			disp.notify(new MapEvent(this, MapEvent.NEED_REPAINT));
		}

		aContext.getDispatcher().notify(new StatusMessageEvent(
				StatusMessageEvent.STATUS_MESSAGE,
				LangModel.getString("Finished")));
*/
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
			Dispatcher disp = this.aContext.getDispatcher();
			if(disp != null)
			{
				if(this.mapView != null)
				{
//					getLogicalNetLayer().getMapViewController().deselectAll();
					disp.notify(new MapEvent(this, MapEvent.DESELECT_ALL));
					disp.notify(new MapEvent(this, MapEvent.SELECTION_CHANGED));
				}

				disp.notify(new SchemeNavigateEvent(
						sel, 
						msgType));
			}
		}
	}
}
