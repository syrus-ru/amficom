/**
 * $Id: MapToolBar.java,v 1.24 2005/06/06 12:57:03 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
 *
 * Платформа: java 1.4.1
*/
package com.syrus.AMFICOM.client.map.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToggleButton;

import com.syrus.AMFICOM.client.event.MapEvent;
import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.client.model.ApplicationModel;
import com.syrus.AMFICOM.client.model.ApplicationModelListener;
import com.syrus.AMFICOM.client.model.Environment;
import com.syrus.AMFICOM.client.model.MapApplicationModel;
import com.syrus.AMFICOM.client.map.LogicalNetLayer;
import com.syrus.AMFICOM.client.map.MapPropertiesManager;
import com.syrus.AMFICOM.client.resource.LangModelMap;

/**
 * Панель инструментов окна карты
 * @version $Revision: 1.24 $, $Date: 2005/06/06 12:57:03 $
 * @author $Author: krupenn $
 * @module mapviewclient_v1
 */
public final class MapToolBar extends JPanel 
		implements ApplicationModelListener
{
	private ApplicationModel aModel = null;

	private JButton zoomInButton = new JButton();
	private JButton zoomOutButton = new JButton();
	private JButton centerObjectButton = new JButton();

	private JToggleButton zoomToPointButton = new JToggleButton();
	private JToggleButton zoomToRectButton = new JToggleButton();
	private JToggleButton moveToCenterButton = new JToggleButton();
	private JToggleButton moveHandButton = new JToggleButton();

	private JToggleButton measureDistanceButton = new JToggleButton();
	private JToggleButton moveFixedButton = new JToggleButton();

	private JToggleButton showNodesButton = new JToggleButton();

	private JToggleButton showNodeLinkToggleButton = new JToggleButton();
	private JToggleButton showPhysicalToggleButton = new JToggleButton();
	private JToggleButton showCablePathToggleButton = new JToggleButton();
	private JToggleButton showTransPathToggleButton = new JToggleButton();

	private JButton optionsButton = new JButton();
	private JButton layersButton = new JButton();
	private JButton shotButton = new JButton();

	private static Dimension buttonSize = new Dimension(24, 24);

	public NodeSizePanel nodeSizePanel;
	
//	private MapPenBarPanel penp;

	private LogicalNetLayer logicalNetLayer;

	public MapToolBar(LogicalNetLayer logicalNetLayer)
	{
		this();
		setLogicalNetLayer(logicalNetLayer);
	}
	
	public MapToolBar()
	{
		super();
		try
		{
			jbInit();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void setLogicalNetLayer(LogicalNetLayer logicalNetLayer)
	{
		this.logicalNetLayer = logicalNetLayer;
		this.nodeSizePanel.setLogicalNetLayer(logicalNetLayer);
//		penp.setLogicalNetLayer(logicalNetLayer);
	}

	private void jbInit()
	{
		MapToolBarActionAdapter actionAdapter =
				new MapToolBarActionAdapter(this);

		this.zoomInButton.setIcon(new ImageIcon("images/zoom_in.gif"));
		this.zoomInButton.addActionListener(actionAdapter);
		this.zoomInButton.setToolTipText(LangModelMap.getString("ZoomIn"));
		this.zoomInButton.setPreferredSize(buttonSize);
		this.zoomInButton.setMaximumSize(buttonSize);
		this.zoomInButton.setMinimumSize(buttonSize);
		this.zoomInButton.setName(MapApplicationModel.OPERATION_ZOOM_IN);
	
		this.zoomOutButton.setIcon(new ImageIcon("images/zoom_out.gif"));
		this.zoomOutButton.addActionListener(actionAdapter);
		this.zoomOutButton.setToolTipText(LangModelMap.getString("ZoomOut"));
		this.zoomOutButton.setPreferredSize(buttonSize);
		this.zoomOutButton.setMaximumSize(buttonSize);
		this.zoomOutButton.setMinimumSize(buttonSize);
		this.zoomOutButton.setName(MapApplicationModel.OPERATION_ZOOM_OUT);

		this.zoomToPointButton.setIcon(new ImageIcon("images/zoom_to_point.gif"));
		this.zoomToPointButton.addActionListener(actionAdapter);
		this.zoomToPointButton.setToolTipText(LangModelMap.getString("ZoomToPoint"));
		this.zoomToPointButton.setPreferredSize(buttonSize);
		this.zoomToPointButton.setMaximumSize(buttonSize);
		this.zoomToPointButton.setMinimumSize(buttonSize);
		this.zoomToPointButton.setName(MapApplicationModel.OPERATION_ZOOM_TO_POINT);

		this.zoomToRectButton.setIcon(new ImageIcon("images/zoom_area.gif"));
		this.zoomToRectButton.addActionListener(actionAdapter);
		this.zoomToRectButton.setToolTipText(LangModelMap.getString("ZoomBox"));
		this.zoomToRectButton.setPreferredSize(buttonSize);
		this.zoomToRectButton.setMaximumSize(buttonSize);
		this.zoomToRectButton.setMinimumSize(buttonSize);
		this.zoomToRectButton.setName(MapApplicationModel.OPERATION_ZOOM_BOX);

		this.moveToCenterButton.setIcon(new ImageIcon("images/map_centr.gif"));
		this.moveToCenterButton.addActionListener(actionAdapter);
		this.moveToCenterButton.setToolTipText(LangModelMap.getString("MoveToCenter"));
		this.moveToCenterButton.setPreferredSize(buttonSize);
		this.moveToCenterButton.setMaximumSize(buttonSize);
		this.moveToCenterButton.setMinimumSize(buttonSize);
		this.moveToCenterButton.setName(MapApplicationModel.OPERATION_MOVE_TO_CENTER);

		this.moveHandButton.setIcon(new ImageIcon("images/hand.gif"));
		this.moveHandButton.addActionListener(actionAdapter);
		this.moveHandButton.setToolTipText(LangModelMap.getString("HandPan"));
		this.moveHandButton.setPreferredSize(buttonSize);
		this.moveHandButton.setMaximumSize(buttonSize);
		this.moveHandButton.setMinimumSize(buttonSize);
		this.moveHandButton.setName(MapApplicationModel.OPERATION_HAND_PAN);

		this.measureDistanceButton.setIcon(new ImageIcon("images/distance.gif"));
		this.measureDistanceButton.addActionListener(actionAdapter);
		this.measureDistanceButton.setToolTipText(LangModelMap.getString("MeasureDistance"));
		this.measureDistanceButton.setPreferredSize(buttonSize);
		this.measureDistanceButton.setMaximumSize(buttonSize);
		this.measureDistanceButton.setMinimumSize(buttonSize);
		this.measureDistanceButton.setName(MapApplicationModel.OPERATION_MEASURE_DISTANCE);

		this.moveFixedButton.setIcon(new ImageIcon("images/movefixed.gif"));
		this.moveFixedButton.addActionListener(actionAdapter);
		this.moveFixedButton.setToolTipText(LangModelMap.getString("MoveFixed"));
		this.moveFixedButton.setPreferredSize(buttonSize);
		this.moveFixedButton.setMaximumSize(buttonSize);
		this.moveFixedButton.setMinimumSize(buttonSize);
		this.moveFixedButton.setName(MapApplicationModel.OPERATION_MOVE_FIXED);

		this.showNodesButton.setIcon(new ImageIcon("images/nodes_visible.gif"));
		this.showNodesButton.addActionListener(actionAdapter);
		this.showNodesButton.setToolTipText(LangModelMap.getString("ViewNodes"));
		this.showNodesButton.setPreferredSize(buttonSize);
		this.showNodesButton.setMaximumSize(buttonSize);
		this.showNodesButton.setMinimumSize(buttonSize);
		this.showNodesButton.setName(MapApplicationModel.MODE_NODES);

		this.showNodeLinkToggleButton.setIcon(new ImageIcon("images/nodelinkmode.gif"));
		this.showNodeLinkToggleButton.addActionListener(actionAdapter);
		this.showNodeLinkToggleButton.setToolTipText(LangModelMap.getString("NodeLinkMode"));
		this.showNodeLinkToggleButton.setPreferredSize(buttonSize);
		this.showNodeLinkToggleButton.setMaximumSize(buttonSize);
		this.showNodeLinkToggleButton.setMinimumSize(buttonSize);
		this.showNodeLinkToggleButton.setName(MapApplicationModel.MODE_NODE_LINK);

		this.showPhysicalToggleButton.setIcon(new ImageIcon("images/linkmode.gif"));
		this.showPhysicalToggleButton.addActionListener(actionAdapter);
		this.showPhysicalToggleButton.setToolTipText(LangModelMap.getString("LinkMode"));
		this.showPhysicalToggleButton.setPreferredSize(buttonSize);
		this.showPhysicalToggleButton.setMaximumSize(buttonSize);
		this.showPhysicalToggleButton.setMinimumSize(buttonSize);
		this.showPhysicalToggleButton.setName(MapApplicationModel.MODE_LINK);

//		this.showPhysicalToggleButton.setSelected(true);// режим по умолчанию

		this.showCablePathToggleButton.setIcon(new ImageIcon("images/pathmode.gif"));
		this.showCablePathToggleButton.addActionListener(actionAdapter);
		this.showCablePathToggleButton.setToolTipText(LangModelMap.getString("CableMode"));
		this.showCablePathToggleButton.setPreferredSize(buttonSize);
		this.showCablePathToggleButton.setMaximumSize(buttonSize);
		this.showCablePathToggleButton.setMinimumSize(buttonSize);
		this.showCablePathToggleButton.setName(MapApplicationModel.MODE_CABLE_PATH);

		this.showTransPathToggleButton.setIcon(new ImageIcon("images/pathmode.gif"));
		this.showTransPathToggleButton.addActionListener(actionAdapter);
		this.showTransPathToggleButton.setToolTipText(LangModelMap.getString("PathMode"));
		this.showTransPathToggleButton.setPreferredSize(buttonSize);
		this.showTransPathToggleButton.setMaximumSize(buttonSize);
		this.showTransPathToggleButton.setMinimumSize(buttonSize);
		this.showTransPathToggleButton.setName(MapApplicationModel.MODE_PATH);

		this.centerObjectButton.setIcon(new ImageIcon("images/fit.gif"));
		this.centerObjectButton.addActionListener(actionAdapter);
		this.centerObjectButton.setToolTipText(LangModelMap.getString("CenterSelection"));
		this.centerObjectButton.setPreferredSize(buttonSize);
		this.centerObjectButton.setMaximumSize(buttonSize);
		this.centerObjectButton.setMinimumSize(buttonSize);
		this.centerObjectButton.setName(MapApplicationModel.OPERATION_CENTER_SELECTION);

		this.optionsButton.setIcon(new ImageIcon("images/options.gif"));
		this.optionsButton.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
					MapOptionsDialog mod = new MapOptionsDialog();
					mod.setLocation(((int)screen.getWidth() - mod.getWidth()) / 2, (int)(screen.getHeight() - mod.getHeight()) / 2);
					mod.setModal(true);
					mod.setVisible(true);
					if(mod.getReturnCode() == MapOptionsDialog.RET_OK)
						getLogicalNetLayer().getContext().getDispatcher().firePropertyChange(new MapEvent(this, MapEvent.NEED_REPAINT));
				}
			}); 
		this.optionsButton.setToolTipText(LangModelMap.getString("Options"));
		this.optionsButton.setPreferredSize(buttonSize);
		this.optionsButton.setMaximumSize(buttonSize);
		this.optionsButton.setMinimumSize(buttonSize);
		this.optionsButton.setName("mapViewOptions");

		this.layersButton.setIcon(new ImageIcon("images/layers.gif"));
		this.layersButton.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
					LayersDialog mod = new LayersDialog();
					mod.setLocation(((int)screen.getWidth() - mod.getWidth()) / 2, (int)(screen.getHeight() - mod.getHeight()) / 2);
					mod.setModal(true);
					mod.setVisible(true);
					if(mod.getReturnCode() == MapOptionsDialog.RET_OK)
						getLogicalNetLayer().getContext().getDispatcher().firePropertyChange(new MapEvent(this, MapEvent.NEED_REPAINT));
				}
			}); 
		this.layersButton.setToolTipText(LangModelMap.getString("Layers"));
		this.layersButton.setPreferredSize(buttonSize);
		this.layersButton.setMaximumSize(buttonSize);
		this.layersButton.setMinimumSize(buttonSize);
		this.layersButton.setName("mapViewLayers");

		this.shotButton.setToolTipText("Снимок");
		this.shotButton.setText("Снимок");
		this.shotButton.setPreferredSize(buttonSize);
		this.shotButton.setMaximumSize(buttonSize);
		this.shotButton.setMinimumSize(buttonSize);
		this.shotButton.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
					JDialog dialog = new JDialog(
							Environment.getActiveWindow(),
							true);
					Image image = getLogicalNetLayer().getMapViewer().getMapShot();
//					JPanel panel = new JPanel();
					JLabel label = new JLabel(new ImageIcon(image));
//					panel.add(label);
					dialog.getContentPane().setLayout(new BorderLayout());
					dialog.getContentPane().add(label, BorderLayout.CENTER);
					dialog.pack();
					dialog.setLocation(((int)screen.getWidth() - dialog.getWidth()) / 2, (int)(screen.getHeight() - dialog.getHeight()) / 2);

					dialog.setVisible(true);
				}
			});
	
		this.nodeSizePanel = new NodeSizePanel(this.logicalNetLayer);
//		this.penp = new MapPenBarPanel(this.logicalNetLayer);

		this.setBorder(BorderFactory.createEtchedBorder());
		this.setPreferredSize(new Dimension(-1,buttonSize.height + 10));

		JPanel innerPanel = new JPanel();
		innerPanel.setLayout(new GridBagLayout());
		
		innerPanel.add(this.moveHandButton,new GridBagConstraints(0,0,1,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(1,30,1,0),0,0));
		
		innerPanel.add(this.zoomInButton,new GridBagConstraints(1,0,1,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(1,10,1,0),0,0));
		innerPanel.add(this.zoomOutButton,new GridBagConstraints(2,0,1,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(1,0,1,0),0,0));
		innerPanel.add(this.zoomToRectButton,new GridBagConstraints(3,0,1,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(1,0,1,0),0,0));
		//		this.add(this.zoomToPointButton,new GridBagConstraints(0,0,1,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(1,0,1,0),0,0));
		
		innerPanel.add(this.moveToCenterButton,new GridBagConstraints(4,0,1,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(1,10,1,0),0,0));
		innerPanel.add(this.centerObjectButton,new GridBagConstraints(5,0,1,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(1,0,1,0),0,0));

		innerPanel.add(this.measureDistanceButton,new GridBagConstraints(6,0,1,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(1,10,1,0),0,0));
//		this.add(this.moveFixedButton,new GridBagConstraints(7,0,1,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(1,0,1,0),0,0));

		innerPanel.add(this.showNodesButton,new GridBagConstraints(8,0,1,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(1,20,1,0),0,0));

		innerPanel.add(this.showNodeLinkToggleButton,new GridBagConstraints(9,0,1,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(1,10,1,0),0,0));
		innerPanel.add(this.showPhysicalToggleButton,new GridBagConstraints(10,0,1,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(1,0,1,0),0,0));
		innerPanel.add(this.showCablePathToggleButton,new GridBagConstraints(11,0,1,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(1,0,1,0),0,0));
		innerPanel.add(this.showTransPathToggleButton,new GridBagConstraints(12,0,1,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(1,0,1,0),0,0));

		innerPanel.add(this.nodeSizePanel,new GridBagConstraints(13,0,1,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(1,10,1,0),0,0));

		innerPanel.add(this.optionsButton,new GridBagConstraints(14,0,1,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(1,10,1,0),0,0));

		innerPanel.add(this.layersButton,new GridBagConstraints(15,0,1,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(1,10,1,0),0,0));

		innerPanel.add(this.shotButton,new GridBagConstraints(16,0,1,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(1,10,1,0),0,0));
		
		this.setLayout(new BorderLayout());
		this.add(innerPanel,BorderLayout.WEST);
//		this.add(this.penp);
	}

//Включить выключить панель
	public void setEnableDisablePanel(boolean b)
	{
		this.aModel.setEnabled(MapApplicationModel.OPERATION_CENTER_SELECTION, b);
		this.aModel.setEnabled(MapApplicationModel.MODE_NODE_LINK, b);
		this.aModel.setEnabled(MapApplicationModel.MODE_LINK, b);
		this.aModel.setEnabled(MapApplicationModel.MODE_CABLE_PATH, b);
		this.aModel.setEnabled(MapApplicationModel.MODE_PATH, b);
		this.aModel.setEnabled(MapApplicationModel.OPERATION_ZOOM_IN, b);
		this.aModel.setEnabled(MapApplicationModel.OPERATION_ZOOM_OUT, b);
		this.aModel.setEnabled(MapApplicationModel.OPERATION_ZOOM_TO_POINT, b);
		this.aModel.setEnabled(MapApplicationModel.OPERATION_ZOOM_BOX, b);
		this.aModel.setEnabled(MapApplicationModel.OPERATION_MOVE_TO_CENTER, b);
		this.aModel.setEnabled(MapApplicationModel.OPERATION_MOVE_FIXED, b);
		this.aModel.setEnabled(MapApplicationModel.MODE_NODES, b);
		this.aModel.setEnabled(MapApplicationModel.OPERATION_HAND_PAN, b);
		this.aModel.setEnabled(MapApplicationModel.OPERATION_MEASURE_DISTANCE, b);

		this.aModel.fireModelChanged();

		this.nodeSizePanel.setEnabled(b);
	}

	public void setModel(ApplicationModel a)
	{
		this.aModel = a;

		this.aModel.getCommand(MapApplicationModel.OPERATION_CENTER_SELECTION).setParameter("applicationModel", this.aModel);
		this.aModel.getCommand(MapApplicationModel.MODE_NODE_LINK).setParameter("applicationModel", this.aModel);
		this.aModel.getCommand(MapApplicationModel.MODE_LINK).setParameter("applicationModel", this.aModel);
		this.aModel.getCommand(MapApplicationModel.MODE_CABLE_PATH).setParameter("applicationModel", this.aModel);
		this.aModel.getCommand(MapApplicationModel.MODE_PATH).setParameter("applicationModel", this.aModel);
		this.aModel.getCommand(MapApplicationModel.OPERATION_ZOOM_IN).setParameter("applicationModel", this.aModel);
		this.aModel.getCommand(MapApplicationModel.OPERATION_ZOOM_OUT).setParameter("applicationModel", this.aModel);
		this.aModel.getCommand(MapApplicationModel.OPERATION_ZOOM_TO_POINT).setParameter("applicationModel", this.aModel);
		this.aModel.getCommand(MapApplicationModel.OPERATION_ZOOM_BOX).setParameter("applicationModel", this.aModel);
		this.aModel.getCommand(MapApplicationModel.OPERATION_MOVE_TO_CENTER).setParameter("applicationModel", this.aModel);
		this.aModel.getCommand(MapApplicationModel.MODE_NODES).setParameter("applicationModel", this.aModel);
		this.aModel.getCommand(MapApplicationModel.OPERATION_HAND_PAN).setParameter("applicationModel", this.aModel);
		this.aModel.getCommand(MapApplicationModel.OPERATION_MEASURE_DISTANCE).setParameter("applicationModel", this.aModel);
		this.aModel.getCommand(MapApplicationModel.OPERATION_MOVE_FIXED).setParameter("applicationModel", this.aModel);

		this.aModel.getCommand(MapApplicationModel.OPERATION_CENTER_SELECTION).setParameter("logicalNetLayer", this.logicalNetLayer);
		this.aModel.getCommand(MapApplicationModel.MODE_NODE_LINK).setParameter("logicalNetLayer", this.logicalNetLayer);
		this.aModel.getCommand(MapApplicationModel.MODE_LINK).setParameter("logicalNetLayer", this.logicalNetLayer);
		this.aModel.getCommand(MapApplicationModel.MODE_CABLE_PATH).setParameter("logicalNetLayer", this.logicalNetLayer);
		this.aModel.getCommand(MapApplicationModel.MODE_PATH).setParameter("logicalNetLayer", this.logicalNetLayer);
		this.aModel.getCommand(MapApplicationModel.OPERATION_ZOOM_IN).setParameter("logicalNetLayer", this.logicalNetLayer);
		this.aModel.getCommand(MapApplicationModel.OPERATION_ZOOM_OUT).setParameter("logicalNetLayer", this.logicalNetLayer);
		this.aModel.getCommand(MapApplicationModel.OPERATION_ZOOM_TO_POINT).setParameter("logicalNetLayer", this.logicalNetLayer);
		this.aModel.getCommand(MapApplicationModel.OPERATION_ZOOM_BOX).setParameter("logicalNetLayer", this.logicalNetLayer);
		this.aModel.getCommand(MapApplicationModel.OPERATION_MOVE_TO_CENTER).setParameter("logicalNetLayer", this.logicalNetLayer);
		this.aModel.getCommand(MapApplicationModel.MODE_NODES).setParameter("logicalNetLayer", this.logicalNetLayer);
		this.aModel.getCommand(MapApplicationModel.OPERATION_HAND_PAN).setParameter("logicalNetLayer", this.logicalNetLayer);
		this.aModel.getCommand(MapApplicationModel.OPERATION_MEASURE_DISTANCE).setParameter("logicalNetLayer", this.logicalNetLayer);
		this.aModel.getCommand(MapApplicationModel.OPERATION_MOVE_FIXED).setParameter("logicalNetLayer", this.logicalNetLayer);

		Command command = this.aModel.getCommand(MapApplicationModel.MODE_NODES);
		command.setParameter("button", this.showNodesButton);

		// OPERATION_HAND_PAN not accessible with descrete navigation
		this.aModel.setAccessible(MapApplicationModel.OPERATION_HAND_PAN, !MapPropertiesManager.isDescreteNavigation());

		this.aModel.setSelected(MapApplicationModel.MODE_LINK, true);
		this.aModel.setSelected(MapApplicationModel.MODE_NODES, true);
		this.aModel.fireModelChanged();
	}

	public ApplicationModel getModel()
	{
		return this.aModel;
	}

	public void modelChanged(String elementName) {
		modelChanged(new String [] { elementName });
	}

	public void modelChanged(String e[])
	{
		this.zoomInButton.setVisible(this.aModel.isVisible(MapApplicationModel.OPERATION_ZOOM_IN));
		this.zoomInButton.setEnabled(this.aModel.isEnabled(MapApplicationModel.OPERATION_ZOOM_IN));

		this.zoomOutButton.setVisible(this.aModel.isVisible(MapApplicationModel.OPERATION_ZOOM_OUT));
		this.zoomOutButton.setEnabled(this.aModel.isEnabled(MapApplicationModel.OPERATION_ZOOM_OUT));

		this.centerObjectButton.setVisible(this.aModel.isVisible(MapApplicationModel.OPERATION_CENTER_SELECTION));
		this.centerObjectButton.setEnabled(this.aModel.isEnabled(MapApplicationModel.OPERATION_CENTER_SELECTION));

		this.zoomToPointButton.setVisible(this.aModel.isVisible(MapApplicationModel.OPERATION_ZOOM_TO_POINT));
		this.zoomToPointButton.setEnabled(this.aModel.isEnabled(MapApplicationModel.OPERATION_ZOOM_TO_POINT));
		this.zoomToPointButton.setSelected(this.aModel.isSelected(MapApplicationModel.OPERATION_ZOOM_TO_POINT));

		this.zoomToRectButton.setVisible(this.aModel.isVisible(MapApplicationModel.OPERATION_ZOOM_BOX));
		this.zoomToRectButton.setEnabled(this.aModel.isEnabled(MapApplicationModel.OPERATION_ZOOM_BOX));
		this.zoomToRectButton.setSelected(this.aModel.isSelected(MapApplicationModel.OPERATION_ZOOM_BOX));

		this.moveToCenterButton.setVisible(this.aModel.isVisible(MapApplicationModel.OPERATION_MOVE_TO_CENTER));
		this.moveToCenterButton.setEnabled(this.aModel.isEnabled(MapApplicationModel.OPERATION_MOVE_TO_CENTER));
		this.moveToCenterButton.setSelected(this.aModel.isSelected(MapApplicationModel.OPERATION_MOVE_TO_CENTER));

		this.moveHandButton.setVisible(this.aModel.isVisible(MapApplicationModel.OPERATION_HAND_PAN));
		this.moveHandButton.setEnabled(this.aModel.isEnabled(MapApplicationModel.OPERATION_HAND_PAN));
		this.moveHandButton.setSelected(this.aModel.isSelected(MapApplicationModel.OPERATION_HAND_PAN));

		this.measureDistanceButton.setVisible(this.aModel.isVisible(MapApplicationModel.OPERATION_MEASURE_DISTANCE));
		this.measureDistanceButton.setEnabled(this.aModel.isEnabled(MapApplicationModel.OPERATION_MEASURE_DISTANCE));
		this.measureDistanceButton.setSelected(this.aModel.isSelected(MapApplicationModel.OPERATION_MEASURE_DISTANCE));

		this.moveFixedButton.setVisible(this.aModel.isVisible(MapApplicationModel.OPERATION_MOVE_FIXED));
		this.moveFixedButton.setEnabled(this.aModel.isEnabled(MapApplicationModel.OPERATION_MOVE_FIXED));
		this.moveFixedButton.setSelected(this.aModel.isSelected(MapApplicationModel.OPERATION_MOVE_FIXED));

		this.showNodesButton.setVisible(this.aModel.isVisible(MapApplicationModel.MODE_NODES));
		this.showNodesButton.setEnabled(this.aModel.isEnabled(MapApplicationModel.MODE_NODES));
		this.showNodesButton.setSelected(this.aModel.isSelected(MapApplicationModel.MODE_NODES));

		this.showNodeLinkToggleButton.setVisible(this.aModel.isVisible(MapApplicationModel.MODE_NODE_LINK));
		this.showNodeLinkToggleButton.setEnabled(this.aModel.isEnabled(MapApplicationModel.MODE_NODE_LINK));
		this.showNodeLinkToggleButton.setSelected(this.aModel.isSelected(MapApplicationModel.MODE_NODE_LINK));

		this.showPhysicalToggleButton.setVisible(this.aModel.isVisible(MapApplicationModel.MODE_LINK));
		this.showPhysicalToggleButton.setEnabled(this.aModel.isEnabled(MapApplicationModel.MODE_LINK));
		this.showPhysicalToggleButton.setSelected(this.aModel.isSelected(MapApplicationModel.MODE_LINK));

		this.showCablePathToggleButton.setVisible(this.aModel.isVisible(MapApplicationModel.MODE_CABLE_PATH));
		this.showCablePathToggleButton.setEnabled(this.aModel.isEnabled(MapApplicationModel.MODE_CABLE_PATH));
		this.showCablePathToggleButton.setSelected(this.aModel.isSelected(MapApplicationModel.MODE_CABLE_PATH));

		this.showTransPathToggleButton.setVisible(this.aModel.isVisible(MapApplicationModel.MODE_PATH));
		this.showTransPathToggleButton.setEnabled(this.aModel.isEnabled(MapApplicationModel.MODE_PATH));
		this.showTransPathToggleButton.setSelected(this.aModel.isSelected(MapApplicationModel.MODE_PATH));
	}

	public void buttonPressed(ActionEvent e)
	{
		if(this.aModel == null)
			return;
		AbstractButton jb = (AbstractButton )e.getSource();
		String s = jb.getName();
		Command command = this.aModel.getCommand(s);
		command.setParameter("applicationModel", this.aModel);
		command.setParameter("logicalNetLayer", getLogicalNetLayer());
		command.execute();
	}
	
	public LogicalNetLayer getLogicalNetLayer()
	{
		return this.logicalNetLayer;
	}

	private class MapToolBarActionAdapter implements java.awt.event.ActionListener
	{
		MapToolBar adaptee;
	
		MapToolBarActionAdapter(MapToolBar adaptee)
		{
			this.adaptee = adaptee;
		}
	
		public void actionPerformed(ActionEvent e)
		{
			this.adaptee.buttonPressed(e);
		}
	}

}


