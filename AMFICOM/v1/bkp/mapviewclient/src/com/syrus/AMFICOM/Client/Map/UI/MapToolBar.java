/**
 * $Id: MapToolBar.java,v 1.8 2004/11/12 19:09:55 krupenn Exp $
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
import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.General.Model.MapApplicationModel;
import com.syrus.AMFICOM.Client.Map.LogicalNetLayer;

import com.syrus.AMFICOM.Client.Map.MapPropertiesManager;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.awt.geom.Point2D;
import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;

import oracle.jdeveloper.layout.XYLayout;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;

/**
 * Панель инструментов окна карты
 * 
 * 
 * 
 * @version $Revision: 1.8 $, $Date: 2004/11/12 19:09:55 $
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
	private JLabel scaleLabel = new JLabel();
	private JTextField scaleField = new JTextField();

	private JButton optionsButton = new JButton();

	private JButton shotButton = new JButton();

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
			latitudeTextField.setText(MapPropertiesManager.getCoordinatesFormat().format(latitude));
			longitudeField.setText(MapPropertiesManager.getCoordinatesFormat().format(longitude));
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	public void showScale (double scale)
	{
		try
		{
			scaleField.setText(MapPropertiesManager.getScaleFormat().format(scale));
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
		zoomInButton.setName(MapApplicationModel.OPERATION_ZOOM_IN);
	
		zoomOutButton.setIcon(new ImageIcon("images/zoom_out.gif"));
		zoomOutButton.addActionListener(actionAdapter);
		zoomOutButton.setToolTipText(LangModelMap.getString("ZoomOut"));
		zoomOutButton.setPreferredSize(buttonSize);
		zoomOutButton.setMaximumSize(buttonSize);
		zoomOutButton.setMinimumSize(buttonSize);
		zoomOutButton.setName(MapApplicationModel.OPERATION_ZOOM_OUT);

		zoomToPointButton.setIcon(new ImageIcon("images/zoom_to_point.gif"));
		zoomToPointButton.addActionListener(actionAdapter);
		zoomToPointButton.setToolTipText(LangModelMap.getString("ZoomToPoint"));
		zoomToPointButton.setPreferredSize(buttonSize);
		zoomToPointButton.setMaximumSize(buttonSize);
		zoomToPointButton.setMinimumSize(buttonSize);
		zoomToPointButton.setName(MapApplicationModel.OPERATION_ZOOM_TO_POINT);

		zoomToRectButton.setIcon(new ImageIcon("images/zoom_area.gif"));
		zoomToRectButton.addActionListener(actionAdapter);
		zoomToRectButton.setToolTipText(LangModelMap.getString("ZoomBox"));
		zoomToRectButton.setPreferredSize(buttonSize);
		zoomToRectButton.setMaximumSize(buttonSize);
		zoomToRectButton.setMinimumSize(buttonSize);
		zoomToRectButton.setName(MapApplicationModel.OPERATION_ZOOM_BOX);

		moveToCenterButton.setIcon(new ImageIcon("images/map_centr.gif"));
		moveToCenterButton.addActionListener(actionAdapter);
		moveToCenterButton.setToolTipText(LangModelMap.getString("MoveToCenter"));
		moveToCenterButton.setPreferredSize(buttonSize);
		moveToCenterButton.setMaximumSize(buttonSize);
		moveToCenterButton.setMinimumSize(buttonSize);
		moveToCenterButton.setName(MapApplicationModel.OPERATION_MOVE_TO_CENTER);

		moveHandButton.setIcon(new ImageIcon("images/hand.gif"));
		moveHandButton.addActionListener(actionAdapter);
		moveHandButton.setToolTipText(LangModelMap.getString("HandPan"));
		moveHandButton.setPreferredSize(buttonSize);
		moveHandButton.setMaximumSize(buttonSize);
		moveHandButton.setMinimumSize(buttonSize);
		moveHandButton.setName(MapApplicationModel.OPERATION_HAND_PAN);

		measureDistanceButton.setIcon(new ImageIcon("images/distance.gif"));
		measureDistanceButton.addActionListener(actionAdapter);
		measureDistanceButton.setToolTipText(LangModelMap.getString("MeasureDistance"));
		measureDistanceButton.setPreferredSize(buttonSize);
		measureDistanceButton.setMaximumSize(buttonSize);
		measureDistanceButton.setMinimumSize(buttonSize);
		measureDistanceButton.setName(MapApplicationModel.OPERATION_MEASURE_DISTANCE);

		showNodesButton.setIcon(new ImageIcon("images/nodes_visible.gif"));
		showNodesButton.addActionListener(actionAdapter);
		showNodesButton.setToolTipText(LangModelMap.getString("ViewNodes"));
		showNodesButton.setPreferredSize(buttonSize);
		showNodesButton.setMaximumSize(buttonSize);
		showNodesButton.setMinimumSize(buttonSize);
		showNodesButton.setName(MapApplicationModel.MODE_NODES);

		showNodeLinkToggleButton.setIcon(new ImageIcon("images/nodelinkmode.gif"));
		showNodeLinkToggleButton.addActionListener(actionAdapter);
		showNodeLinkToggleButton.setToolTipText(LangModelMap.getString("NodeLinkMode"));
		showNodeLinkToggleButton.setPreferredSize(buttonSize);
		showNodeLinkToggleButton.setMaximumSize(buttonSize);
		showNodeLinkToggleButton.setMinimumSize(buttonSize);
		showNodeLinkToggleButton.setName(MapApplicationModel.MODE_NODE_LINK);

		showPhysicalToggleButton.setIcon(new ImageIcon("images/linkmode.gif"));
		showPhysicalToggleButton.addActionListener(actionAdapter);
		showPhysicalToggleButton.setToolTipText(LangModelMap.getString("LinkMode"));
		showPhysicalToggleButton.setPreferredSize(buttonSize);
		showPhysicalToggleButton.setMaximumSize(buttonSize);
		showPhysicalToggleButton.setMinimumSize(buttonSize);
		showPhysicalToggleButton.setName(MapApplicationModel.MODE_LINK);

//		showPhysicalToggleButton.setSelected(true);// режим по умолчанию

		showCablePathToggleButton.setIcon(new ImageIcon("images/pathmode.gif"));
		showCablePathToggleButton.addActionListener(actionAdapter);
		showCablePathToggleButton.setToolTipText(LangModelMap.getString("CableMode"));
		showCablePathToggleButton.setPreferredSize(buttonSize);
		showCablePathToggleButton.setMaximumSize(buttonSize);
		showCablePathToggleButton.setMinimumSize(buttonSize);
		showCablePathToggleButton.setName(MapApplicationModel.MODE_CABLE_PATH);

		showTransPathToggleButton.setIcon(new ImageIcon("images/pathmode.gif"));
		showTransPathToggleButton.addActionListener(actionAdapter);
		showTransPathToggleButton.setToolTipText(LangModelMap.getString("PathMode"));
		showTransPathToggleButton.setPreferredSize(buttonSize);
		showTransPathToggleButton.setMaximumSize(buttonSize);
		showTransPathToggleButton.setMinimumSize(buttonSize);
		showTransPathToggleButton.setName(MapApplicationModel.MODE_PATH);

		centerObjectButton.setIcon(new ImageIcon("images/fit.gif"));
		centerObjectButton.addActionListener(actionAdapter);
		centerObjectButton.setToolTipText(LangModelMap.getString("CenterSelection"));
		centerObjectButton.setPreferredSize(buttonSize);
		centerObjectButton.setMaximumSize(buttonSize);
		centerObjectButton.setMinimumSize(buttonSize);
		centerObjectButton.setName(MapApplicationModel.OPERATION_CENTER_SELECTION);

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

		shotButton.setToolTipText("Снимок");
		shotButton.setText("Снимок");
		shotButton.setPreferredSize(buttonSize);
		shotButton.setMaximumSize(buttonSize);
		shotButton.setMinimumSize(buttonSize);
		shotButton.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
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

					dialog.setVisible(true);
				}
			});
	
		
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

		scaleLabel.setText(LangModelMap.getString("Scale"));
		scaleField.setText("0.0");
		scaleField.setPreferredSize(fieldSize);
		scaleField.setMaximumSize(fieldSize);
		scaleField.setMinimumSize(fieldSize);

		scaleField.addKeyListener(new KeyListener()
			{
				public void keyPressed(KeyEvent e) 
				{
					if(e.getKeyCode() == KeyEvent.VK_ENTER)
					{
						try
						{
							double scale = Double.parseDouble(scaleField.getText());
							if(scale > 0)
								getLogicalNetLayer().setScale(scale);
						}
						catch(Exception ex)
						{
						}
					}
				}
				public void keyReleased(KeyEvent e) {}
				public void keyTyped(KeyEvent e) {}
			});

		KeyListener longlatKeyListener = new KeyListener()
			{
				public void keyPressed(KeyEvent e)
				{
					if(e.getKeyCode() == KeyEvent.VK_ENTER)
					{
						try
						{
							double lon = Double.parseDouble(longitudeField.getText());
							double lat = Double.parseDouble(latitudeTextField.getText());
							getLogicalNetLayer().setCenter(
								new Point2D.Double(lat, lon));
						}
						catch(Exception ex)
						{
							System.out.println(ex.getMessage());
						}
					}
				}
				public void keyReleased(KeyEvent e) {}
				public void keyTyped(KeyEvent e) {}
			};
		latitudeTextField.addKeyListener(longlatKeyListener);
		longitudeField.addKeyListener(longlatKeyListener);

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
		this.add(scaleLabel);
		this.add(scaleField);
		this.addSeparator();
		this.add(sp);
		this.addSeparator();
		this.add(optionsButton);
		this.addSeparator();
		this.add(shotButton);
//		this.add(penp);
	}

//Включить выключить панель
	public void setEnableDisablePanel(boolean b)
	{
		aModel.setEnabled(MapApplicationModel.OPERATION_CENTER_SELECTION, b);
		aModel.setEnabled(MapApplicationModel.MODE_NODE_LINK, b);
		aModel.setEnabled(MapApplicationModel.MODE_LINK, b);
		aModel.setEnabled(MapApplicationModel.MODE_CABLE_PATH, b);
		aModel.setEnabled(MapApplicationModel.MODE_PATH, b);
		aModel.setEnabled(MapApplicationModel.OPERATION_ZOOM_IN, b);
		aModel.setEnabled(MapApplicationModel.OPERATION_ZOOM_OUT, b);
		aModel.setEnabled(MapApplicationModel.OPERATION_ZOOM_TO_POINT, b);
		aModel.setEnabled(MapApplicationModel.OPERATION_ZOOM_BOX, b);
		aModel.setEnabled(MapApplicationModel.OPERATION_MOVE_TO_CENTER, b);
		aModel.setEnabled(MapApplicationModel.MODE_NODES, b);
		aModel.setEnabled(MapApplicationModel.OPERATION_HAND_PAN, b);
		aModel.setEnabled(MapApplicationModel.OPERATION_MEASURE_DISTANCE, b);

		aModel.fireModelChanged();

		sp.setEnabled(b);
	}

	public void setModel(ApplicationModel a)
	{
		aModel = a;

		aModel.getCommand(MapApplicationModel.OPERATION_CENTER_SELECTION).setParameter("applicationModel", aModel);
		aModel.getCommand(MapApplicationModel.MODE_NODE_LINK).setParameter("applicationModel", aModel);
		aModel.getCommand(MapApplicationModel.MODE_LINK).setParameter("applicationModel", aModel);
		aModel.getCommand(MapApplicationModel.MODE_CABLE_PATH).setParameter("applicationModel", aModel);
		aModel.getCommand(MapApplicationModel.MODE_PATH).setParameter("applicationModel", aModel);
		aModel.getCommand(MapApplicationModel.OPERATION_ZOOM_IN).setParameter("applicationModel", aModel);
		aModel.getCommand(MapApplicationModel.OPERATION_ZOOM_OUT).setParameter("applicationModel", aModel);
		aModel.getCommand(MapApplicationModel.OPERATION_ZOOM_TO_POINT).setParameter("applicationModel", aModel);
		aModel.getCommand(MapApplicationModel.OPERATION_ZOOM_BOX).setParameter("applicationModel", aModel);
		aModel.getCommand(MapApplicationModel.OPERATION_MOVE_TO_CENTER).setParameter("applicationModel", aModel);
		aModel.getCommand(MapApplicationModel.MODE_NODES).setParameter("applicationModel", aModel);
		aModel.getCommand(MapApplicationModel.OPERATION_HAND_PAN).setParameter("applicationModel", aModel);
		aModel.getCommand(MapApplicationModel.OPERATION_MEASURE_DISTANCE).setParameter("applicationModel", aModel);

		aModel.getCommand(MapApplicationModel.OPERATION_CENTER_SELECTION).setParameter("logicalNetLayer", logicalNetLayer);
		aModel.getCommand(MapApplicationModel.MODE_NODE_LINK).setParameter("logicalNetLayer", logicalNetLayer);
		aModel.getCommand(MapApplicationModel.MODE_LINK).setParameter("logicalNetLayer", logicalNetLayer);
		aModel.getCommand(MapApplicationModel.MODE_CABLE_PATH).setParameter("logicalNetLayer", logicalNetLayer);
		aModel.getCommand(MapApplicationModel.MODE_PATH).setParameter("logicalNetLayer", logicalNetLayer);
		aModel.getCommand(MapApplicationModel.OPERATION_ZOOM_IN).setParameter("logicalNetLayer", logicalNetLayer);
		aModel.getCommand(MapApplicationModel.OPERATION_ZOOM_OUT).setParameter("logicalNetLayer", logicalNetLayer);
		aModel.getCommand(MapApplicationModel.OPERATION_ZOOM_TO_POINT).setParameter("logicalNetLayer", logicalNetLayer);
		aModel.getCommand(MapApplicationModel.OPERATION_ZOOM_BOX).setParameter("logicalNetLayer", logicalNetLayer);
		aModel.getCommand(MapApplicationModel.OPERATION_MOVE_TO_CENTER).setParameter("logicalNetLayer", logicalNetLayer);
		aModel.getCommand(MapApplicationModel.MODE_NODES).setParameter("logicalNetLayer", logicalNetLayer);
		aModel.getCommand(MapApplicationModel.OPERATION_HAND_PAN).setParameter("logicalNetLayer", logicalNetLayer);
		aModel.getCommand(MapApplicationModel.OPERATION_MEASURE_DISTANCE).setParameter("logicalNetLayer", logicalNetLayer);

		Command command = aModel.getCommand(MapApplicationModel.MODE_NODES);
		command.setParameter("button", showNodesButton);

		aModel.setSelected(MapApplicationModel.MODE_LINK, true);
		aModel.setSelected(MapApplicationModel.MODE_NODES, true);
		aModel.fireModelChanged();
	}

	public ApplicationModel getModel()
	{
		return aModel;
	}

	public void modelChanged(String e[])
	{
		zoomInButton.setVisible(aModel.isVisible(MapApplicationModel.OPERATION_ZOOM_IN));
		zoomInButton.setEnabled(aModel.isEnabled(MapApplicationModel.OPERATION_ZOOM_IN));

		zoomOutButton.setVisible(aModel.isVisible(MapApplicationModel.OPERATION_ZOOM_OUT));
		zoomOutButton.setEnabled(aModel.isEnabled(MapApplicationModel.OPERATION_ZOOM_OUT));

		centerObjectButton.setVisible(aModel.isVisible(MapApplicationModel.OPERATION_CENTER_SELECTION));
		centerObjectButton.setEnabled(aModel.isEnabled(MapApplicationModel.OPERATION_CENTER_SELECTION));

		zoomToPointButton.setVisible(aModel.isVisible(MapApplicationModel.OPERATION_ZOOM_TO_POINT));
		zoomToPointButton.setEnabled(aModel.isEnabled(MapApplicationModel.OPERATION_ZOOM_TO_POINT));
		zoomToPointButton.setSelected(aModel.isSelected(MapApplicationModel.OPERATION_ZOOM_TO_POINT));

		zoomToRectButton.setVisible(aModel.isVisible(MapApplicationModel.OPERATION_ZOOM_BOX));
		zoomToRectButton.setEnabled(aModel.isEnabled(MapApplicationModel.OPERATION_ZOOM_BOX));
		zoomToRectButton.setSelected(aModel.isSelected(MapApplicationModel.OPERATION_ZOOM_BOX));

		moveToCenterButton.setVisible(aModel.isVisible(MapApplicationModel.OPERATION_MOVE_TO_CENTER));
		moveToCenterButton.setEnabled(aModel.isEnabled(MapApplicationModel.OPERATION_MOVE_TO_CENTER));
		moveToCenterButton.setSelected(aModel.isSelected(MapApplicationModel.OPERATION_MOVE_TO_CENTER));

		moveHandButton.setVisible(aModel.isVisible(MapApplicationModel.OPERATION_HAND_PAN));
		moveHandButton.setEnabled(aModel.isEnabled(MapApplicationModel.OPERATION_HAND_PAN));
		moveHandButton.setSelected(aModel.isSelected(MapApplicationModel.OPERATION_HAND_PAN));

		measureDistanceButton.setVisible(aModel.isVisible(MapApplicationModel.OPERATION_MEASURE_DISTANCE));
		measureDistanceButton.setEnabled(aModel.isEnabled(MapApplicationModel.OPERATION_MEASURE_DISTANCE));
		measureDistanceButton.setSelected(aModel.isSelected(MapApplicationModel.OPERATION_MEASURE_DISTANCE));

		showNodesButton.setVisible(aModel.isVisible(MapApplicationModel.MODE_NODES));
		showNodesButton.setEnabled(aModel.isEnabled(MapApplicationModel.MODE_NODES));
		showNodesButton.setSelected(aModel.isSelected(MapApplicationModel.MODE_NODES));

		showNodeLinkToggleButton.setVisible(aModel.isVisible(MapApplicationModel.MODE_NODE_LINK));
		showNodeLinkToggleButton.setEnabled(aModel.isEnabled(MapApplicationModel.MODE_NODE_LINK));
		showNodeLinkToggleButton.setSelected(aModel.isSelected(MapApplicationModel.MODE_NODE_LINK));

		showPhysicalToggleButton.setVisible(aModel.isVisible(MapApplicationModel.MODE_LINK));
		showPhysicalToggleButton.setEnabled(aModel.isEnabled(MapApplicationModel.MODE_LINK));
		showPhysicalToggleButton.setSelected(aModel.isSelected(MapApplicationModel.MODE_LINK));

		showCablePathToggleButton.setVisible(aModel.isVisible(MapApplicationModel.MODE_CABLE_PATH));
		showCablePathToggleButton.setEnabled(aModel.isEnabled(MapApplicationModel.MODE_CABLE_PATH));
		showCablePathToggleButton.setSelected(aModel.isSelected(MapApplicationModel.MODE_CABLE_PATH));

		showTransPathToggleButton.setVisible(aModel.isVisible(MapApplicationModel.MODE_PATH));
		showTransPathToggleButton.setEnabled(aModel.isEnabled(MapApplicationModel.MODE_PATH));
		showTransPathToggleButton.setSelected(aModel.isSelected(MapApplicationModel.MODE_PATH));
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


