
// Copyright (c) 2002 Syrus
package com.syrus.AMFICOM.Client.Map;

import com.ofx.geometry.SxDoublePoint;

import com.syrus.AMFICOM.Client.General.Command.*;
import com.syrus.AMFICOM.Client.General.Command.MapNav.CenterSelectionCommand;
import com.syrus.AMFICOM.Client.General.Command.MapNav.ZoomInCommand;
import com.syrus.AMFICOM.Client.General.Command.MapNav.ZoomOutCommand;
import com.syrus.AMFICOM.Client.Resource.Map.MapContext;
import com.syrus.AMFICOM.Client.General.Model.*;

import java.awt.*;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;

import javax.swing.*;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JToggleButton;

import oracle.jdeveloper.layout.XYConstraints;
import oracle.jdeveloper.layout.XYLayout;

public class MapToolBar extends JPanel 
		implements ApplicationModelListener
{
	public ApplicationModel aModel = null;

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
	private JToggleButton showTransPathToggleButton = new JToggleButton();

	private LogicalNetLayer logicalNetLayer;
	GridBagLayout gridBagLayout1 = new GridBagLayout();
	XYLayout xYLayout = new XYLayout();
	JLabel LatitudeLabel = new JLabel();
	public JTextField LatitudeTextField = new JTextField();
	JLabel LongitudeLabel = new JLabel();
	public JTextField LongitudeField = new JTextField();

	Dimension buttonSize = new Dimension(24, 24);
	private XYLayout xYLayout1 = new XYLayout();

	public NodeSizePanel sp;

	public final static int img_siz = 16;
	public final static int btn_siz = 24;

	public MapToolBar(LogicalNetLayer lnl)
	{
		try
		{
			logicalNetLayer = lnl;
			jbInit();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private void jbInit() throws Exception
	{
		MapToolBar_this_actionAdapter actionAdapter =
				new MapToolBar_this_actionAdapter(this);

		zoomInButton.setIcon(new ImageIcon("images/zoom_in.gif"));
		zoomInButton.addActionListener(actionAdapter);
		zoomInButton.setToolTipText("Увеличить");
		zoomInButton.setPreferredSize(buttonSize);
		zoomInButton.setMaximumSize(buttonSize);
		zoomInButton.setMinimumSize(buttonSize);
		zoomInButton.setName("mapActionZoomIn");
	
		zoomOutButton.setIcon(new ImageIcon("images/zoom_out.gif"));
		zoomOutButton.addActionListener(actionAdapter);
		zoomOutButton.setToolTipText("Уменьшить");
		zoomOutButton.setPreferredSize(buttonSize);
		zoomOutButton.setMaximumSize(buttonSize);
		zoomOutButton.setMinimumSize(buttonSize);
		zoomOutButton.setName("mapActionZoomOut");

		zoomToPointButton.setIcon(new ImageIcon("images/zoom_to_point.gif"));
		zoomToPointButton.addActionListener(actionAdapter);
		zoomToPointButton.setToolTipText("Приблизить точку");
		zoomToPointButton.setPreferredSize(buttonSize);
		zoomToPointButton.setMaximumSize(buttonSize);
		zoomToPointButton.setMinimumSize(buttonSize);
		zoomToPointButton.setName("mapActionZoomToPoint");

		zoomToRect.setIcon(new ImageIcon("images/zoom_area.gif"));
		zoomToRect.addActionListener(actionAdapter);
		zoomToRect.setToolTipText("Приблизить область");
		zoomToRect.setPreferredSize(buttonSize);
		zoomToRect.setMaximumSize(buttonSize);
		zoomToRect.setMinimumSize(buttonSize);
		zoomToRect.setName("mapActionZoomBox");

		moveToCenter.setIcon(new ImageIcon("images/map_centr.gif"));
		moveToCenter.addActionListener(actionAdapter);
		moveToCenter.setToolTipText("Приблизить к центру");
		moveToCenter.setPreferredSize(buttonSize);
		moveToCenter.setMaximumSize(buttonSize);
		moveToCenter.setMinimumSize(buttonSize);
		moveToCenter.setName("mapActionMoveToCenter");

		moveHandButton.setIcon(new ImageIcon("images/hand.gif"));
		moveHandButton.addActionListener(actionAdapter);
		moveHandButton.setToolTipText("Сдвинуть карту");
		moveHandButton.setPreferredSize(buttonSize);
		moveHandButton.setMaximumSize(buttonSize);
		moveHandButton.setMinimumSize(buttonSize);
		moveHandButton.setName("mapActionHandPan");

		showNodesButton.setIcon(new ImageIcon("images/nodes_visible.gif"));
		showNodesButton.addActionListener(actionAdapter);
		showNodesButton.setToolTipText("Отобразить узлы");
		showNodesButton.setPreferredSize(buttonSize);
		showNodesButton.setMaximumSize(buttonSize);
		showNodesButton.setMinimumSize(buttonSize);
		showNodesButton.setName("mapModeViewNodes");

		showNodeLinkToggleButton.setIcon(new ImageIcon("images/nodelinkmode.gif"));
		showNodeLinkToggleButton.addActionListener(actionAdapter);
		showNodeLinkToggleButton.setToolTipText("Режим управления фрагментами линии связи");
		showNodeLinkToggleButton.setPreferredSize(buttonSize);
		showNodeLinkToggleButton.setMaximumSize(buttonSize);
		showNodeLinkToggleButton.setMinimumSize(buttonSize);
		showNodeLinkToggleButton.setName("mapModeNodeLink");

		showPhysicalToggleButton.setIcon(new ImageIcon("images/linkmode.gif"));
		showPhysicalToggleButton.addActionListener(actionAdapter);
		showPhysicalToggleButton.setToolTipText("Режим управления линией связи");
		showPhysicalToggleButton.setPreferredSize(buttonSize);
		showPhysicalToggleButton.setMaximumSize(buttonSize);
		showPhysicalToggleButton.setMinimumSize(buttonSize);
		showPhysicalToggleButton.setName("mapModeLink");

//		showPhysicalToggleButton.setSelected(true);// режим по умолчанию

		showTransPathToggleButton.setIcon(new ImageIcon("images/pathmode.gif"));
		showTransPathToggleButton.addActionListener(actionAdapter);
		showTransPathToggleButton.setToolTipText("Режим управления путями тестирования");
		showTransPathToggleButton.setPreferredSize(buttonSize);
		showTransPathToggleButton.setMaximumSize(buttonSize);
		showTransPathToggleButton.setMinimumSize(buttonSize);
		showTransPathToggleButton.setName("mapModePath");

		centerObjectButton.setIcon(new ImageIcon("images/fit.gif"));
		centerObjectButton.addActionListener(actionAdapter);
		centerObjectButton.setToolTipText("Центрировать объект");
		centerObjectButton.setPreferredSize(buttonSize);
		centerObjectButton.setMaximumSize(buttonSize);
		centerObjectButton.setMinimumSize(buttonSize);
		centerObjectButton.setName("mapActionCenterSelection");
		
		LatitudeLabel.setText("Широта");
		LatitudeTextField.setText("0.0000");
		LongitudeLabel.setText("Долгота");
		LongitudeField.setText("0.0000");

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
		this.add(showTransPathToggleButton, new XYConstraints(255, 2, 25, 25));
		this.add(LatitudeLabel, new XYConstraints(280, 2, 50, 25));
		this.add(LatitudeTextField, new XYConstraints(330, 2, 50, 25));
		this.add(LongitudeLabel, new XYConstraints(380, 2, 50, 25));
		this.add(LongitudeField, new XYConstraints(430, 2, 50, 25));

		sp = new NodeSizePanel(logicalNetLayer);
		this.add(sp, new XYConstraints(490, 0, 70, 29));

//		setEnableDisablePanel(false);
	}

//Включить выключить панель
	public void setEnableDisablePanel(boolean b)
	{
		aModel.setEnabled("mapActionCenterSelection", b);
		aModel.setEnabled("mapModeNodeLink", b);
		aModel.setEnabled("mapModeLink", b);
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
		command = (Command )command.clone();
		command.setParameter("applicationModel", aModel);
		command.setParameter("logicalNetLayer", logicalNetLayer);
		command.execute();
	}
}

class MapToolBar_this_actionAdapter implements java.awt.event.ActionListener
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

