/**
 * $Id: MapToolBar.java,v 1.4 2004/10/19 11:48:28 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
 *
 * Платформа: java 1.4.1
*/
package com.syrus.AMFICOM.Client.Map.UI;

import com.syrus.AMFICOM.Client.General.Command.Command;
import com.syrus.AMFICOM.Client.General.Lang.LangModelMap;
import com.syrus.AMFICOM.Client.General.Model.ApplicationModel;
import com.syrus.AMFICOM.Client.General.Model.ApplicationModelListener;
import com.syrus.AMFICOM.Client.Map.LogicalNetLayer;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;

import oracle.jdeveloper.layout.XYLayout;

/**
 * Панель инструментов окна карты
 * 
 * 
 * 
 * @version $Revision: 1.4 $, $Date: 2004/10/19 11:48:28 $
 * @module map_v2
 * @author $Author: krupenn $
 * @see
 */
public final class MapToolBar extends JToolBar 
		implements ApplicationModelListener//, OperationListener
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

	private JToggleButton showNodesButton = new JToggleButton();

	private JToggleButton showNodeLinkToggleButton = new JToggleButton();
	private JToggleButton showPhysicalToggleButton = new JToggleButton();
	private JToggleButton showCablePathToggleButton = new JToggleButton();
	private JToggleButton showTransPathToggleButton = new JToggleButton();

	private LogicalNetLayer logicalNetLayer;
	private JLabel latitudeLabel = new JLabel();
	private JTextField latitudeTextField = new JTextField();
	private JLabel longitudeLabel = new JLabel();
	private JTextField longitudeField = new JTextField();

	private JButton optionsButton = new JButton();

	private static Dimension buttonSize = new Dimension(24, 24);
	private static Dimension fieldSize = new Dimension(60, 24);

	public NodeSizePanel sp;
	
	private MapPenBarPanel penp;

	public MapToolBar(LogicalNetLayer lnl)
	{
		this();
		setLogicalNetLayer(lnl);
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
		sp.setLogicalNetLayer(logicalNetLayer);
		penp.setLogicalNetLayer(logicalNetLayer);
	}

	public void showLatLong (double latitude, double longitude)
	{
		try
		{
			java.text.DecimalFormat df2 = new java.text.DecimalFormat("###,##0.0000");

			latitudeTextField.setText(df2.format(latitude));
			longitudeField.setText(df2.format(longitude));
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	
	private void jbInit()
	{
		MapToolBarActionAdapter actionAdapter =
				new MapToolBarActionAdapter(this);

		zoomInButton.setIcon(new ImageIcon("images/zoom_in.gif"));
		zoomInButton.addActionListener(actionAdapter);
		zoomInButton.setToolTipText(LangModelMap.getString("ZoomIn"));
		zoomInButton.setPreferredSize(buttonSize);
		zoomInButton.setMaximumSize(buttonSize);
		zoomInButton.setMinimumSize(buttonSize);
		zoomInButton.setName("mapActionZoomIn");
	
		zoomOutButton.setIcon(new ImageIcon("images/zoom_out.gif"));
		zoomOutButton.addActionListener(actionAdapter);
		zoomOutButton.setToolTipText(LangModelMap.getString("ZoomOut"));
		zoomOutButton.setPreferredSize(buttonSize);
		zoomOutButton.setMaximumSize(buttonSize);
		zoomOutButton.setMinimumSize(buttonSize);
		zoomOutButton.setName("mapActionZoomOut");

		zoomToPointButton.setIcon(new ImageIcon("images/zoom_to_point.gif"));
		zoomToPointButton.addActionListener(actionAdapter);
		zoomToPointButton.setToolTipText(LangModelMap.getString("ZoomToPoint"));
		zoomToPointButton.setPreferredSize(buttonSize);
		zoomToPointButton.setMaximumSize(buttonSize);
		zoomToPointButton.setMinimumSize(buttonSize);
		zoomToPointButton.setName("mapActionZoomToPoint");

		zoomToRectButton.setIcon(new ImageIcon("images/zoom_area.gif"));
		zoomToRectButton.addActionListener(actionAdapter);
		zoomToRectButton.setToolTipText(LangModelMap.getString("ZoomBox"));
		zoomToRectButton.setPreferredSize(buttonSize);
		zoomToRectButton.setMaximumSize(buttonSize);
		zoomToRectButton.setMinimumSize(buttonSize);
		zoomToRectButton.setName("mapActionZoomBox");

		moveToCenterButton.setIcon(new ImageIcon("images/map_centr.gif"));
		moveToCenterButton.addActionListener(actionAdapter);
		moveToCenterButton.setToolTipText(LangModelMap.getString("MoveToCenter"));
		moveToCenterButton.setPreferredSize(buttonSize);
		moveToCenterButton.setMaximumSize(buttonSize);
		moveToCenterButton.setMinimumSize(buttonSize);
		moveToCenterButton.setName("mapActionMoveToCenter");

		moveHandButton.setIcon(new ImageIcon("images/hand.gif"));
		moveHandButton.addActionListener(actionAdapter);
		moveHandButton.setToolTipText(LangModelMap.getString("HandPan"));
		moveHandButton.setPreferredSize(buttonSize);
		moveHandButton.setMaximumSize(buttonSize);
		moveHandButton.setMinimumSize(buttonSize);
		moveHandButton.setName("mapActionHandPan");

		measureDistanceButton.setIcon(new ImageIcon("images/distance.gif"));
		measureDistanceButton.addActionListener(actionAdapter);
		measureDistanceButton.setToolTipText(LangModelMap.getString("MeasureDistance"));
		measureDistanceButton.setPreferredSize(buttonSize);
		measureDistanceButton.setMaximumSize(buttonSize);
		measureDistanceButton.setMinimumSize(buttonSize);
		measureDistanceButton.setName("mapActionMeasureDistance");

		showNodesButton.setIcon(new ImageIcon("images/nodes_visible.gif"));
		showNodesButton.addActionListener(actionAdapter);
		showNodesButton.setToolTipText(LangModelMap.getString("ViewNodes"));
		showNodesButton.setPreferredSize(buttonSize);
		showNodesButton.setMaximumSize(buttonSize);
		showNodesButton.setMinimumSize(buttonSize);
		showNodesButton.setName("mapModeViewNodes");

		showNodeLinkToggleButton.setIcon(new ImageIcon("images/nodelinkmode.gif"));
		showNodeLinkToggleButton.addActionListener(actionAdapter);
		showNodeLinkToggleButton.setToolTipText(LangModelMap.getString("NodeLinkMode"));
		showNodeLinkToggleButton.setPreferredSize(buttonSize);
		showNodeLinkToggleButton.setMaximumSize(buttonSize);
		showNodeLinkToggleButton.setMinimumSize(buttonSize);
		showNodeLinkToggleButton.setName("mapModeNodeLink");

		showPhysicalToggleButton.setIcon(new ImageIcon("images/linkmode.gif"));
		showPhysicalToggleButton.addActionListener(actionAdapter);
		showPhysicalToggleButton.setToolTipText(LangModelMap.getString("LinkMode"));
		showPhysicalToggleButton.setPreferredSize(buttonSize);
		showPhysicalToggleButton.setMaximumSize(buttonSize);
		showPhysicalToggleButton.setMinimumSize(buttonSize);
		showPhysicalToggleButton.setName("mapModeLink");

//		showPhysicalToggleButton.setSelected(true);// режим по умолчанию

		showCablePathToggleButton.setIcon(new ImageIcon("images/pathmode.gif"));
		showCablePathToggleButton.addActionListener(actionAdapter);
		showCablePathToggleButton.setToolTipText(LangModelMap.getString("CableMode"));
		showCablePathToggleButton.setPreferredSize(buttonSize);
		showCablePathToggleButton.setMaximumSize(buttonSize);
		showCablePathToggleButton.setMinimumSize(buttonSize);
		showCablePathToggleButton.setName("mapModeCablePath");

		showTransPathToggleButton.setIcon(new ImageIcon("images/pathmode.gif"));
		showTransPathToggleButton.addActionListener(actionAdapter);
		showTransPathToggleButton.setToolTipText(LangModelMap.getString("PathMode"));
		showTransPathToggleButton.setPreferredSize(buttonSize);
		showTransPathToggleButton.setMaximumSize(buttonSize);
		showTransPathToggleButton.setMinimumSize(buttonSize);
		showTransPathToggleButton.setName("mapModePath");

		centerObjectButton.setIcon(new ImageIcon("images/fit.gif"));
		centerObjectButton.addActionListener(actionAdapter);
		centerObjectButton.setToolTipText(LangModelMap.getString("CenterSelection"));
		centerObjectButton.setPreferredSize(buttonSize);
		centerObjectButton.setMaximumSize(buttonSize);
		centerObjectButton.setMinimumSize(buttonSize);
		centerObjectButton.setName("mapActionCenterSelection");

		optionsButton.setIcon(new ImageIcon("images/options.gif"));
		optionsButton.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					MapOptionsDialog mod = new MapOptionsDialog();
					mod.setModal(true);
					mod.setVisible(true);
					if(mod.getReturnCode() == MapOptionsDialog.RET_OK)
						getLogicalNetLayer().repaint();
				}
			}); 
		optionsButton.setToolTipText(LangModelMap.getString("Options"));
		optionsButton.setPreferredSize(buttonSize);
		optionsButton.setMaximumSize(buttonSize);
		optionsButton.setMinimumSize(buttonSize);
		optionsButton.setName("mapViewOptions");
	
		
		latitudeLabel.setText(LangModelMap.getString("Latitude"));
		latitudeTextField.setText("0.0000");
		latitudeTextField.setPreferredSize(fieldSize);
		latitudeTextField.setMaximumSize(fieldSize);
		latitudeTextField.setMinimumSize(fieldSize);
		longitudeLabel.setText(LangModelMap.getString("Longitude"));
		longitudeField.setText("0.0000");
		longitudeField.setPreferredSize(fieldSize);
		longitudeField.setMaximumSize(fieldSize);
		longitudeField.setMinimumSize(fieldSize);

		sp = new NodeSizePanel(logicalNetLayer);
		penp = new MapPenBarPanel(logicalNetLayer);

		this.setBorder(BorderFactory.createEtchedBorder());

		this.add(zoomInButton);
		this.add(zoomOutButton);
		this.add(zoomToPointButton);
		this.add(zoomToRectButton);
		this.addSeparator();
		this.add(moveToCenterButton);
		this.add(moveHandButton);
		this.add(centerObjectButton);
		this.add(measureDistanceButton);
		this.addSeparator();
		this.add(showNodesButton);
		this.addSeparator();
		this.add(showNodeLinkToggleButton);
		this.add(showPhysicalToggleButton);
		this.add(showCablePathToggleButton);
		this.add(showTransPathToggleButton);
		this.addSeparator();
		this.add(latitudeLabel);
		this.add(latitudeTextField);
		this.add(longitudeLabel);
		this.add(longitudeField);
		this.addSeparator();
		this.add(sp);
		this.addSeparator();
		this.add(optionsButton);
		this.addSeparator();
		this.add(penp);
	}

//Включить выключить панель
	public void setEnableDisablePanel(boolean b)
	{
		aModel.setEnabled("mapActionCenterSelection", b);
		aModel.setEnabled("mapModeNodeLink", b);
		aModel.setEnabled("mapModeLink", b);
		aModel.setEnabled("mapModeCablePath", b);
		aModel.setEnabled("mapModePath", b);
		aModel.setEnabled("mapActionZoomIn", b);
		aModel.setEnabled("mapActionZoomOut", b);
		aModel.setEnabled("mapActionZoomToPoint", b);
		aModel.setEnabled("mapActionZoomBox", b);
		aModel.setEnabled("mapActionMoveToCenter", b);
		aModel.setEnabled("mapModeViewNodes", b);
		aModel.setEnabled("mapActionHandPan", b);
		aModel.setEnabled("mapActionMeasureDistance", b);

		aModel.fireModelChanged("");

		sp.setEnabled(b);
	}

	public void setModel(ApplicationModel a)
	{
		aModel = a;

		aModel.getCommand("mapActionCenterSelection").setParameter("applicationModel", aModel);
		aModel.getCommand("mapModeNodeLink").setParameter("applicationModel", aModel);
		aModel.getCommand("mapModeLink").setParameter("applicationModel", aModel);
		aModel.getCommand("mapModeCablePath").setParameter("applicationModel", aModel);
		aModel.getCommand("mapModePath").setParameter("applicationModel", aModel);
		aModel.getCommand("mapActionZoomIn").setParameter("applicationModel", aModel);
		aModel.getCommand("mapActionZoomOut").setParameter("applicationModel", aModel);
		aModel.getCommand("mapActionZoomToPoint").setParameter("applicationModel", aModel);
		aModel.getCommand("mapActionZoomBox").setParameter("applicationModel", aModel);
		aModel.getCommand("mapActionMoveToCenter").setParameter("applicationModel", aModel);
		aModel.getCommand("mapModeViewNodes").setParameter("applicationModel", aModel);
		aModel.getCommand("mapActionHandPan").setParameter("applicationModel", aModel);
		aModel.getCommand("mapActionMeasureDistance").setParameter("applicationModel", aModel);

		aModel.getCommand("mapActionCenterSelection").setParameter("logicalNetLayer", logicalNetLayer);
		aModel.getCommand("mapModeNodeLink").setParameter("logicalNetLayer", logicalNetLayer);
		aModel.getCommand("mapModeLink").setParameter("logicalNetLayer", logicalNetLayer);
		aModel.getCommand("mapModeCablePath").setParameter("logicalNetLayer", logicalNetLayer);
		aModel.getCommand("mapModePath").setParameter("logicalNetLayer", logicalNetLayer);
		aModel.getCommand("mapActionZoomIn").setParameter("logicalNetLayer", logicalNetLayer);
		aModel.getCommand("mapActionZoomOut").setParameter("logicalNetLayer", logicalNetLayer);
		aModel.getCommand("mapActionZoomToPoint").setParameter("logicalNetLayer", logicalNetLayer);
		aModel.getCommand("mapActionZoomBox").setParameter("logicalNetLayer", logicalNetLayer);
		aModel.getCommand("mapActionMoveToCenter").setParameter("logicalNetLayer", logicalNetLayer);
		aModel.getCommand("mapModeViewNodes").setParameter("logicalNetLayer", logicalNetLayer);
		aModel.getCommand("mapActionHandPan").setParameter("logicalNetLayer", logicalNetLayer);
		aModel.getCommand("mapActionMeasureDistance").setParameter("logicalNetLayer", logicalNetLayer);

		Command command = aModel.getCommand("mapModeViewNodes");
		command.setParameter("button", showNodesButton);

		aModel.setSelected("mapModeLink", true);
		aModel.setSelected("mapModeViewNodes", true);
		aModel.fireModelChanged("");
	}

	public ApplicationModel getModel()
	{
		return aModel;
	}

	public void modelChanged(String e[])
	{
		zoomInButton.setVisible(aModel.isVisible("mapActionZoomIn"));
		zoomInButton.setEnabled(aModel.isEnabled("mapActionZoomIn"));

		zoomOutButton.setVisible(aModel.isVisible("mapActionZoomOut"));
		zoomOutButton.setEnabled(aModel.isEnabled("mapActionZoomOut"));

		centerObjectButton.setVisible(aModel.isVisible("mapActionCenterSelection"));
		centerObjectButton.setEnabled(aModel.isEnabled("mapActionCenterSelection"));

		zoomToPointButton.setVisible(aModel.isVisible("mapActionZoomToPoint"));
		zoomToPointButton.setEnabled(aModel.isEnabled("mapActionZoomToPoint"));
		zoomToPointButton.setSelected(aModel.isSelected("mapActionZoomToPoint"));

		zoomToRectButton.setVisible(aModel.isVisible("mapActionZoomBox"));
		zoomToRectButton.setEnabled(aModel.isEnabled("mapActionZoomBox"));
		zoomToRectButton.setSelected(aModel.isSelected("mapActionZoomBox"));

		moveToCenterButton.setVisible(aModel.isVisible("mapActionMoveToCenter"));
		moveToCenterButton.setEnabled(aModel.isEnabled("mapActionMoveToCenter"));
		moveToCenterButton.setSelected(aModel.isSelected("mapActionMoveToCenter"));

		moveHandButton.setVisible(aModel.isVisible("mapActionHandPan"));
		moveHandButton.setEnabled(aModel.isEnabled("mapActionHandPan"));
		moveHandButton.setSelected(aModel.isSelected("mapActionHandPan"));

		measureDistanceButton.setVisible(aModel.isVisible("mapActionMeasureDistance"));
		measureDistanceButton.setEnabled(aModel.isEnabled("mapActionMeasureDistance"));
		measureDistanceButton.setSelected(aModel.isSelected("mapActionMeasureDistance"));

		showNodesButton.setVisible(aModel.isVisible("mapModeViewNodes"));
		showNodesButton.setEnabled(aModel.isEnabled("mapModeViewNodes"));
		showNodesButton.setSelected(aModel.isSelected("mapModeViewNodes"));

		showNodeLinkToggleButton.setVisible(aModel.isVisible("mapModeNodeLink"));
		showNodeLinkToggleButton.setEnabled(aModel.isEnabled("mapModeNodeLink"));
		showNodeLinkToggleButton.setSelected(aModel.isSelected("mapModeNodeLink"));

		showPhysicalToggleButton.setVisible(aModel.isVisible("mapModeLink"));
		showPhysicalToggleButton.setEnabled(aModel.isEnabled("mapModeLink"));
		showPhysicalToggleButton.setSelected(aModel.isSelected("mapModeLink"));

		showCablePathToggleButton.setVisible(aModel.isVisible("mapModeCablePath"));
		showCablePathToggleButton.setEnabled(aModel.isEnabled("mapModeCablePath"));
		showCablePathToggleButton.setSelected(aModel.isSelected("mapModeCablePath"));

		showTransPathToggleButton.setVisible(aModel.isVisible("mapModePath"));
		showTransPathToggleButton.setEnabled(aModel.isEnabled("mapModePath"));
		showTransPathToggleButton.setSelected(aModel.isSelected("mapModePath"));
	}

	public void buttonPressed(ActionEvent e)
	{
		if(aModel == null)
			return;
		AbstractButton jb = (AbstractButton )e.getSource();
		String s = jb.getName();
		Command command = aModel.getCommand(s);
		command.setParameter("applicationModel", aModel);
		command.setParameter("logicalNetLayer", getLogicalNetLayer());
		command.execute();
	}
	
	public LogicalNetLayer getLogicalNetLayer()
	{
		return logicalNetLayer;
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
			adaptee.buttonPressed(e);
		}
	}
}


