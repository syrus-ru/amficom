/**
 * $Id: MapToolBar.java,v 1.2 2004/09/14 14:48:51 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
 *
 * Платформа: java 1.4.1
*/
package com.syrus.AMFICOM.Client.Map.UI;

import com.syrus.AMFICOM.Client.General.Command.Command;
import com.syrus.AMFICOM.Client.General.Model.ApplicationModel;
import com.syrus.AMFICOM.Client.General.Model.ApplicationModelListener;

import com.syrus.AMFICOM.Client.General.Lang.LangModelMap;
import com.syrus.AMFICOM.Client.Map.LogicalNetLayer;
import com.syrus.AMFICOM.Client.Map.UI.NodeSizePanel;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JToggleButton;

import oracle.jdeveloper.layout.XYConstraints;
import oracle.jdeveloper.layout.XYLayout;

/**
 * Панель инструментов окна карты
 * 
 * 
 * 
 * @version $Revision: 1.2 $, $Date: 2004/09/14 14:48:51 $
 * @module map_v2
 * @author $Author: krupenn $
 * @see
 */
public class MapToolBar extends JPanel 
		implements ApplicationModelListener//, OperationListener
{
	private ApplicationModel aModel = null;

	private JButton zoomInButton = new JButton();
	private JButton zoomOutButton = new JButton();
	private JButton centerObjectButton = new JButton();

	private JToggleButton zoomToPointButton = new JToggleButton();
	private JToggleButton zoomToRect = new JToggleButton();
	private JToggleButton moveToCenter = new JToggleButton();
	private JToggleButton moveHandButton = new JToggleButton();

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

	private Dimension buttonSize = new Dimension(24, 24);
	private XYLayout xYLayout1 = new XYLayout();

	public NodeSizePanel sp;

	public final static int img_siz = 16;
	public final static int btn_siz = 24;

	public MapToolBar(LogicalNetLayer lnl)
	{
		this();
		setLogicalNetLayer(lnl);
	}
	
	public MapToolBar()
	{
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
		MapToolBar_this_actionAdapter actionAdapter =
				new MapToolBar_this_actionAdapter(this);

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

		zoomToRect.setIcon(new ImageIcon("images/zoom_area.gif"));
		zoomToRect.addActionListener(actionAdapter);
		zoomToRect.setToolTipText(LangModelMap.getString("ZoomBox"));
		zoomToRect.setPreferredSize(buttonSize);
		zoomToRect.setMaximumSize(buttonSize);
		zoomToRect.setMinimumSize(buttonSize);
		zoomToRect.setName("mapActionZoomBox");

		moveToCenter.setIcon(new ImageIcon("images/map_centr.gif"));
		moveToCenter.addActionListener(actionAdapter);
		moveToCenter.setToolTipText(LangModelMap.getString("MoveToCenter"));
		moveToCenter.setPreferredSize(buttonSize);
		moveToCenter.setMaximumSize(buttonSize);
		moveToCenter.setMinimumSize(buttonSize);
		moveToCenter.setName("mapActionMoveToCenter");

		moveHandButton.setIcon(new ImageIcon("images/hand.gif"));
		moveHandButton.addActionListener(actionAdapter);
		moveHandButton.setToolTipText(LangModelMap.getString("HandPan"));
		moveHandButton.setPreferredSize(buttonSize);
		moveHandButton.setMaximumSize(buttonSize);
		moveHandButton.setMinimumSize(buttonSize);
		moveHandButton.setName("mapActionHandPan");

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
		longitudeLabel.setText(LangModelMap.getString("Longitude"));
		longitudeField.setText("0.0000");

		this.setLayout(xYLayout1);
		this.setBorder(BorderFactory.createEtchedBorder());
		
		xYLayout1.setWidth(720);
		xYLayout1.setHeight(buttonSize.height + 5);
		this.add(zoomInButton, new XYConstraints(5, 2, 25, 25));
		this.add(zoomOutButton, new XYConstraints(30, 2, 25, 25));
		this.add(zoomToPointButton, new XYConstraints(55, 2, 25, 25));
		this.add(zoomToRect, new XYConstraints(80, 2, 25, 25));
		this.add(moveToCenter, new XYConstraints(105, 2, 25, 25));
		this.add(moveHandButton, new XYConstraints(130, 2, 25, 25));
		this.add(centerObjectButton, new XYConstraints(155, 2, 25, 25));
		this.add(showNodesButton, new XYConstraints(180, 2, 25, 25));
		this.add(showNodeLinkToggleButton, new XYConstraints(205, 2, 25, 25));
		this.add(showPhysicalToggleButton, new XYConstraints(230, 2, 25, 25));
		this.add(showCablePathToggleButton, new XYConstraints(255, 2, 25, 25));
		this.add(showTransPathToggleButton, new XYConstraints(280, 2, 25, 25));
		this.add(latitudeLabel, new XYConstraints(305, 2, 50, 25));
		this.add(latitudeTextField, new XYConstraints(355, 2, 50, 25));
		this.add(longitudeLabel, new XYConstraints(405, 2, 50, 25));
		this.add(longitudeField, new XYConstraints(455, 2, 50, 25));

		sp = new NodeSizePanel(logicalNetLayer);
		this.add(sp, new XYConstraints(515, 0, 70, 29));

		this.add(optionsButton, new XYConstraints(595, 2, 25, 25));
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

		zoomToRect.setVisible(aModel.isVisible("mapActionZoomBox"));
		zoomToRect.setEnabled(aModel.isEnabled("mapActionZoomBox"));
		zoomToRect.setSelected(aModel.isSelected("mapActionZoomBox"));

		moveToCenter.setVisible(aModel.isVisible("mapActionMoveToCenter"));
		moveToCenter.setEnabled(aModel.isEnabled("mapActionMoveToCenter"));
		moveToCenter.setSelected(aModel.isSelected("mapActionMoveToCenter"));

		moveHandButton.setVisible(aModel.isVisible("mapActionHandPan"));
		moveHandButton.setEnabled(aModel.isEnabled("mapActionHandPan"));
		moveHandButton.setSelected(aModel.isSelected("mapActionHandPan"));

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

	public void this_actionPerformed(ActionEvent e)
	{
		if(aModel == null)
			return;
		AbstractButton jb = (AbstractButton )e.getSource();
		String s = jb.getName();
		Command command = aModel.getCommand(s);
//		command = (Command )command.clone();
		command.setParameter("applicationModel", aModel);
		command.setParameter("logicalNetLayer", getLogicalNetLayer());
		command.execute();
	}
	
	public LogicalNetLayer getLogicalNetLayer()
	{
		return logicalNetLayer;
	}

	private class MapToolBar_this_actionAdapter implements java.awt.event.ActionListener
	{
		MapToolBar adaptee;
	
		MapToolBar_this_actionAdapter(MapToolBar adaptee)
		{
			this.adaptee = adaptee;
		}
	
		public void actionPerformed(ActionEvent e)
		{
			adaptee.this_actionPerformed(e);
		}
	}
}


