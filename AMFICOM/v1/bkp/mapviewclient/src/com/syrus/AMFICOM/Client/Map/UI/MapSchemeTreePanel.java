package com.syrus.AMFICOM.Client.Map.UI;

import com.syrus.AMFICOM.Client.General.Event.Dispatcher;
import com.syrus.AMFICOM.Client.General.Event.MapEvent;
import com.syrus.AMFICOM.Client.General.Event.MapNavigateEvent;
import com.syrus.AMFICOM.Client.General.Event.OperationEvent;
import com.syrus.AMFICOM.Client.General.Event.OperationListener;
import com.syrus.AMFICOM.Client.General.Event.StatusMessageEvent;
import com.syrus.AMFICOM.Client.General.Lang.LangModel;
import com.syrus.AMFICOM.Client.General.Lang.LangModelMap;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.UI.ObjectResourceChooserDialog;
import com.syrus.AMFICOM.Client.General.UI.ObjectResourceTreeNode;
import com.syrus.AMFICOM.Client.General.UI.UniTreePanel;
import com.syrus.AMFICOM.Client.Map.UI.MapSchemeTreeModel;
import com.syrus.AMFICOM.Client.Resource.DataSourceInterface;
import com.syrus.AMFICOM.Client.Resource.MapView.MapView;
import com.syrus.AMFICOM.Client.Resource.ObjectResource;
import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.Client.Resource.Scheme.Scheme;
import com.syrus.AMFICOM.Client.Resource.SchemeDataSourceImage;
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
import javax.swing.tree.TreePath;

public final class MapSchemeTreePanel extends JPanel 
		implements OperationListener
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

	public final static int img_siz = 16;
	public final static int btn_siz = 24;

	static final Dimension buttonSize = new Dimension(btn_siz, btn_siz);

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
				getScaledInstance(img_siz, img_siz, Image.SCALE_DEFAULT)));
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
				getScaledInstance(img_siz, img_siz, Image.SCALE_DEFAULT)));
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
			scroll.getViewport().remove(treePanel);
		treePanel = utp;
		if(treePanel != null)
			scroll.getViewport().add(treePanel);
	}

	protected void initTree()
	{
		model = new MapSchemeTreeModel(null, aContext.getDataSource());
		setPanel(new UniTreePanel(aContext.getDispatcher(), aContext, model));
	}

	public void updateTree(MapView mv)
	{
		model.setMapView(mv);
		treePanel.setModel(model);
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
		ObjectResourceTreeNode node;
		Enumeration enum;

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

		if(dataSource == null)
			return;

		aContext.getDispatcher().notify(new StatusMessageEvent(
				StatusMessageEvent.STATUS_MESSAGE,
				LangModelMap.getString("MapOpening")));

		new SchemeDataSourceImage(dataSource).LoadSchemes();

		ObjectResourceChooserDialog mcd = new ObjectResourceChooserDialog(com.syrus.AMFICOM.Client.Map.UI.SchemeController.getInstance(), Scheme.typ);

		List ss = Pool.getList(Scheme.typ);
		mcd.setContents(ss);

		// отфильтровываем по домену
		ObjectResourceTableModel ortm = mcd.getTableModel();
		ortm.setDomainId(aContext.getSessionInterface().getDomainId());
		ortm.restrictToDomain(true);//ф-я фильтрации схем по домену
		ortm.fireTableDataChanged();

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

			MapFrame mapFrame = MapFrame.getMapMainFrame();
			if(mapFrame == null)
			{
				System.out.println("mapviewer is NULL");
			}
			else
			{
				if(!mapFrame.getMapView().getSchemes().contains(retObj))
				{
					mapFrame.getMapView().addScheme((Scheme )retObj);
					mapFrame.getContext().getDispatcher().notify(new MapEvent(
							mapFrame.getMapView(),
							MapEvent.MAP_VIEW_CHANGED));
				}
			}
			aContext.getDispatcher().notify(new StatusMessageEvent(
					StatusMessageEvent.STATUS_MESSAGE,
					LangModel.getString("Finished")));
		}
	}
	
	private void removeFromView()
	{
	}
}
