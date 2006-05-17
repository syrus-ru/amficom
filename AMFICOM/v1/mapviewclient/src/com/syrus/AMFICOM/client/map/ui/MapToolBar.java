/*-
 * $$Id: MapToolBar.java,v 1.40 2006/05/17 10:12:54 stas Exp $$
 *
 * Copyright 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
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
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.UIManager;

import com.syrus.AMFICOM.client.event.MapEvent;
import com.syrus.AMFICOM.client.map.NetMapViewer;
import com.syrus.AMFICOM.client.model.ApplicationModel;
import com.syrus.AMFICOM.client.model.ApplicationModelListener;
import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.client.model.Environment;
import com.syrus.AMFICOM.client.model.MapApplicationModel;
import com.syrus.AMFICOM.client.resource.I18N;
import com.syrus.AMFICOM.client.resource.MapEditorResourceKeys;
import com.syrus.AMFICOM.client.resource.ResourceKeys;

/**
 * ѕанель инструментов окна карты
 * 
 * @version $Revision: 1.40 $, $Date: 2006/05/17 10:12:54 $
 * @author $Author: stas $
 * @author Andrei Kroupennikov
 * @module mapviewclient
 */
public final class MapToolBar extends JPanel implements
		ApplicationModelListener {
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
	private JToggleButton showIndicationButton = new JToggleButton();

	private JToggleButton showNodeLinkToggleButton = new JToggleButton();
	private JToggleButton showPhysicalToggleButton = new JToggleButton();
	private JToggleButton showCablePathToggleButton = new JToggleButton();
	private JToggleButton showTransPathToggleButton = new JToggleButton();

	private JButton undoButton = new JButton();
	private JButton redoButton = new JButton();

	private JButton optionsButton = new JButton();
	private JButton layersButton = new JButton();
	private JButton shotButton = new JButton();

	private static Dimension buttonSize = new Dimension(24, 24);

	public NodeSizePanel nodeSizePanel;

	NetMapViewer netMapViewer;

	// private MapPenBarPanel penp;

	public MapToolBar(NetMapViewer netMapViewer) {
		super();
		this.netMapViewer = netMapViewer;
		jbInit();
		this.nodeSizePanel
				.setLogicalNetLayer(netMapViewer.getLogicalNetLayer());
		// penp.setLogicalNetLayer(logicalNetLayer);
	}

	private void jbInit() {
		MapToolBarActionAdapter actionAdapter = new MapToolBarActionAdapter(
				this);

		this.zoomInButton.setIcon(new ImageIcon("images/zoom_in.gif")); //$NON-NLS-1$
		this.zoomInButton.addActionListener(actionAdapter);
		this.zoomInButton.setToolTipText(I18N
				.getString(MapEditorResourceKeys.TOOLTIP_ZOOM_IN));
		this.zoomInButton.setPreferredSize(buttonSize);
		this.zoomInButton.setMaximumSize(buttonSize);
		this.zoomInButton.setMinimumSize(buttonSize);
		this.zoomInButton.setName(MapApplicationModel.OPERATION_ZOOM_IN);

		this.zoomOutButton.setIcon(new ImageIcon("images/zoom_out.gif")); //$NON-NLS-1$
		this.zoomOutButton.addActionListener(actionAdapter);
		this.zoomOutButton.setToolTipText(I18N
				.getString(MapEditorResourceKeys.TOOLTIP_ZOOM_OUT));
		this.zoomOutButton.setPreferredSize(buttonSize);
		this.zoomOutButton.setMaximumSize(buttonSize);
		this.zoomOutButton.setMinimumSize(buttonSize);
		this.zoomOutButton.setName(MapApplicationModel.OPERATION_ZOOM_OUT);

		this.zoomToPointButton
				.setIcon(new ImageIcon("images/zoom_to_point.gif")); //$NON-NLS-1$
		this.zoomToPointButton.addActionListener(actionAdapter);
		this.zoomToPointButton.setToolTipText(I18N
				.getString(MapEditorResourceKeys.TOOLTIP_ZOOM_TO_POINT));
		this.zoomToPointButton.setPreferredSize(buttonSize);
		this.zoomToPointButton.setMaximumSize(buttonSize);
		this.zoomToPointButton.setMinimumSize(buttonSize);
		this.zoomToPointButton
				.setName(MapApplicationModel.OPERATION_ZOOM_TO_POINT);

		this.zoomToRectButton.setIcon(new ImageIcon("images/zoom_area.gif")); //$NON-NLS-1$
		this.zoomToRectButton.addActionListener(actionAdapter);
		this.zoomToRectButton.setToolTipText(I18N
				.getString(MapEditorResourceKeys.TOOLTIP_ZOOM_BOX));
		this.zoomToRectButton.setPreferredSize(buttonSize);
		this.zoomToRectButton.setMaximumSize(buttonSize);
		this.zoomToRectButton.setMinimumSize(buttonSize);
		this.zoomToRectButton.setName(MapApplicationModel.OPERATION_ZOOM_BOX);

		this.moveToCenterButton.setIcon(new ImageIcon("images/map_centr.gif")); //$NON-NLS-1$
		this.moveToCenterButton.addActionListener(actionAdapter);
		this.moveToCenterButton.setToolTipText(I18N
				.getString(MapEditorResourceKeys.TOOLTIP_MOVE_TO_CENTER));
		this.moveToCenterButton.setPreferredSize(buttonSize);
		this.moveToCenterButton.setMaximumSize(buttonSize);
		this.moveToCenterButton.setMinimumSize(buttonSize);
		this.moveToCenterButton
				.setName(MapApplicationModel.OPERATION_MOVE_TO_CENTER);

		this.moveHandButton.setIcon(new ImageIcon("images/hand.gif")); //$NON-NLS-1$
		this.moveHandButton.addActionListener(actionAdapter);
		this.moveHandButton.setToolTipText(I18N
				.getString(MapEditorResourceKeys.TOOLTIP_HAND_PAN));
		this.moveHandButton.setPreferredSize(buttonSize);
		this.moveHandButton.setMaximumSize(buttonSize);
		this.moveHandButton.setMinimumSize(buttonSize);
		this.moveHandButton.setName(MapApplicationModel.OPERATION_HAND_PAN);

		this.measureDistanceButton
				.setIcon(new ImageIcon("images/distance.gif")); //$NON-NLS-1$
		this.measureDistanceButton.addActionListener(actionAdapter);
		this.measureDistanceButton.setToolTipText(I18N
				.getString(MapEditorResourceKeys.TOOLTIP_MEASURE_DISTANCE));
		this.measureDistanceButton.setPreferredSize(buttonSize);
		this.measureDistanceButton.setMaximumSize(buttonSize);
		this.measureDistanceButton.setMinimumSize(buttonSize);
		this.measureDistanceButton
				.setName(MapApplicationModel.OPERATION_MEASURE_DISTANCE);

		this.moveFixedButton.setIcon(new ImageIcon("images/movefixed.gif")); //$NON-NLS-1$
		this.moveFixedButton.addActionListener(actionAdapter);
		this.moveFixedButton.setToolTipText(I18N
				.getString(MapEditorResourceKeys.TOOLTIP_MOVE_FIXED));
		this.moveFixedButton.setPreferredSize(buttonSize);
		this.moveFixedButton.setMaximumSize(buttonSize);
		this.moveFixedButton.setMinimumSize(buttonSize);
		this.moveFixedButton.setName(MapApplicationModel.OPERATION_MOVE_FIXED);

		this.showNodesButton.setIcon(new ImageIcon("images/nodes_visible.gif")); //$NON-NLS-1$
		this.showNodesButton.addActionListener(actionAdapter);
		this.showNodesButton.setToolTipText(I18N
				.getString(MapEditorResourceKeys.TOOLTIP_VIEW_NODES));
		this.showNodesButton.setPreferredSize(buttonSize);
		this.showNodesButton.setMaximumSize(buttonSize);
		this.showNodesButton.setMinimumSize(buttonSize);
		this.showNodesButton.setName(MapApplicationModel.MODE_NODES);

		this.showIndicationButton.setIcon(new ImageIcon(
				"images/indication_visible.gif")); //$NON-NLS-1$
		this.showIndicationButton.addActionListener(actionAdapter);
		this.showIndicationButton.setToolTipText(I18N
				.getString(MapEditorResourceKeys.TOOLTIP_VIEW_INDICATION));
		this.showIndicationButton.setPreferredSize(buttonSize);
		this.showIndicationButton.setMaximumSize(buttonSize);
		this.showIndicationButton.setMinimumSize(buttonSize);
		this.showIndicationButton.setName(MapApplicationModel.MODE_INDICATION);

		this.showNodeLinkToggleButton.setIcon(new ImageIcon(
				"images/nodelinkmode.gif")); //$NON-NLS-1$
		this.showNodeLinkToggleButton.addActionListener(actionAdapter);
		this.showNodeLinkToggleButton.setToolTipText(I18N
				.getString(MapEditorResourceKeys.TOOLTIP_NODE_LINK_MODE));
		this.showNodeLinkToggleButton.setPreferredSize(buttonSize);
		this.showNodeLinkToggleButton.setMaximumSize(buttonSize);
		this.showNodeLinkToggleButton.setMinimumSize(buttonSize);
		this.showNodeLinkToggleButton
				.setName(MapApplicationModel.MODE_NODE_LINK);

		this.showPhysicalToggleButton.setIcon(new ImageIcon(
				"images/linkmode.gif")); //$NON-NLS-1$
		this.showPhysicalToggleButton.addActionListener(actionAdapter);
		this.showPhysicalToggleButton.setToolTipText(I18N
				.getString(MapEditorResourceKeys.TOOLTIP_PHYSICAL_LINK_MODE));
		this.showPhysicalToggleButton.setPreferredSize(buttonSize);
		this.showPhysicalToggleButton.setMaximumSize(buttonSize);
		this.showPhysicalToggleButton.setMinimumSize(buttonSize);
		this.showPhysicalToggleButton.setName(MapApplicationModel.MODE_LINK);

		// this.showPhysicalToggleButton.setSelected(true);// режим по умолчанию

		this.showCablePathToggleButton.setIcon(new ImageIcon(
				"images/cablemode.gif")); //$NON-NLS-1$
		this.showCablePathToggleButton.addActionListener(actionAdapter);
		this.showCablePathToggleButton.setToolTipText(I18N
				.getString(MapEditorResourceKeys.TOOLTIP_CABLE_MODE));
		this.showCablePathToggleButton.setPreferredSize(buttonSize);
		this.showCablePathToggleButton.setMaximumSize(buttonSize);
		this.showCablePathToggleButton.setMinimumSize(buttonSize);
		this.showCablePathToggleButton
				.setName(MapApplicationModel.MODE_CABLE_PATH);

		this.showTransPathToggleButton.setIcon(new ImageIcon(
				"images/pathmode.gif")); //$NON-NLS-1$
		this.showTransPathToggleButton.addActionListener(actionAdapter);
		this.showTransPathToggleButton
				.setToolTipText(I18N
						.getString(MapEditorResourceKeys.TOOLTIP_MEASUREMENT_PATH_MODE));
		this.showTransPathToggleButton.setPreferredSize(buttonSize);
		this.showTransPathToggleButton.setMaximumSize(buttonSize);
		this.showTransPathToggleButton.setMinimumSize(buttonSize);
		this.showTransPathToggleButton.setName(MapApplicationModel.MODE_PATH);

		this.undoButton.setIcon(new ImageIcon("images/undo.gif")); //$NON-NLS-1$
		this.undoButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				MapToolBar.this.netMapViewer.getLogicalNetLayer().undo();
			}

		});
		this.undoButton.setToolTipText(I18N
				.getString(MapEditorResourceKeys.TOOLTIP_UNDO));
		this.undoButton.setPreferredSize(buttonSize);
		this.undoButton.setMaximumSize(buttonSize);
		this.undoButton.setMinimumSize(buttonSize);

		this.redoButton.setIcon(new ImageIcon("images/redo.gif")); //$NON-NLS-1$
		this.redoButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				MapToolBar.this.netMapViewer.getLogicalNetLayer().redo();
			}

		});
		this.redoButton.setToolTipText(I18N
				.getString(MapEditorResourceKeys.TOOLTIP_REDO));
		this.redoButton.setPreferredSize(buttonSize);
		this.redoButton.setMaximumSize(buttonSize);
		this.redoButton.setMinimumSize(buttonSize);

		this.centerObjectButton.setIcon(new ImageIcon("images/fit.gif")); //$NON-NLS-1$
		this.centerObjectButton.addActionListener(actionAdapter);
		this.centerObjectButton.setToolTipText(I18N
				.getString(MapEditorResourceKeys.TOOLTIP_CENTER_SELECTION));
		this.centerObjectButton.setPreferredSize(buttonSize);
		this.centerObjectButton.setMaximumSize(buttonSize);
		this.centerObjectButton.setMinimumSize(buttonSize);
		this.centerObjectButton
				.setName(MapApplicationModel.OPERATION_CENTER_SELECTION);

		this.optionsButton.setIcon(new ImageIcon("images/options.gif")); //$NON-NLS-1$
		this.optionsButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
				MapOptionsDialog mod = new MapOptionsDialog();
				mod.setLocation(
						((int) screen.getWidth() - mod.getWidth()) / 2,
						(int) (screen.getHeight() - mod.getHeight()) / 2);
				mod.setModal(true);
				mod.setVisible(true);
				if(mod.getReturnCode() == MapOptionsDialog.RET_OK)
					MapToolBar.this.netMapViewer.getLogicalNetLayer()
							.sendMapEvent(MapEvent.NEED_REPAINT);
			}
		});
		this.optionsButton.setToolTipText(I18N
				.getString(MapEditorResourceKeys.TOOLTIP_MAP_FRAME_OPTIONS));
		this.optionsButton.setPreferredSize(buttonSize);
		this.optionsButton.setMaximumSize(buttonSize);
		this.optionsButton.setMinimumSize(buttonSize);

		this.layersButton.setIcon(new ImageIcon("images/layers.gif")); //$NON-NLS-1$
		this.layersButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
				LayersDialog mod = new LayersDialog(
						MapToolBar.this.netMapViewer);
				mod.setLocation(
						((int) screen.getWidth() - mod.getWidth()) / 2,
						(int) (screen.getHeight() - mod.getHeight()) / 2);
				mod.setModal(true);
				mod.setVisible(true);
				if(mod.getReturnCode() == MapOptionsDialog.RET_OK)
					MapToolBar.this.netMapViewer.getLogicalNetLayer()
							.sendMapEvent(MapEvent.NEED_REPAINT);
			}
		});
		this.layersButton.setToolTipText(I18N
				.getString(MapEditorResourceKeys.TOOLTIP_LAYERS));
		this.layersButton.setPreferredSize(buttonSize);
		this.layersButton.setMaximumSize(buttonSize);
		this.layersButton.setMinimumSize(buttonSize);
		this.layersButton.setName("mapViewLayers"); //$NON-NLS-1$

		this.shotButton.setToolTipText("mapShot"); //$NON-NLS-1$
		this.shotButton.setText("mapShot"); //$NON-NLS-1$
		this.shotButton.setPreferredSize(buttonSize);
		this.shotButton.setMaximumSize(buttonSize);
		this.shotButton.setMinimumSize(buttonSize);
		this.shotButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
				JDialog dialog = new JDialog(
						Environment.getActiveWindow(),
						true);
				Image image = MapToolBar.this.netMapViewer.getMapShot();
				// JPanel panel = new JPanel();
				JLabel label = new JLabel(new ImageIcon(image));
				// panel.add(label);
				dialog.getContentPane().setLayout(new BorderLayout());
				dialog.getContentPane().add(label, BorderLayout.CENTER);
				dialog.pack();
				dialog.setLocation(
						((int) screen.getWidth() - dialog.getWidth()) / 2,
						(int) (screen.getHeight() - dialog.getHeight()) / 2);

				dialog.setVisible(true);
			}
		});

		this.nodeSizePanel = new NodeSizePanel(this.netMapViewer
				.getLogicalNetLayer());
		// this.penp = new MapPenBarPanel(this.logicalNetLayer);

		this.setBorder(BorderFactory.createEtchedBorder());
		this.setPreferredSize(new Dimension(-1, buttonSize.height + 10));

		JPanel innerPanel = new JPanel();
		innerPanel.setLayout(new GridBagLayout());

		GridBagConstraints constraints = new GridBagConstraints();

		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 0;
		constraints.weighty = 0;
		constraints.anchor = GridBagConstraints.WEST;
		constraints.fill = GridBagConstraints.NONE;
		constraints.insets = new Insets(1, 30, 1, 0);
		constraints.ipadx = 0;
		constraints.ipady = 0;
		innerPanel.add(this.moveHandButton, constraints);

		constraints.gridx = 1;
		constraints.gridy = 0;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 0;
		constraints.weighty = 0;
		constraints.anchor = GridBagConstraints.WEST;
		constraints.fill = GridBagConstraints.NONE;
		constraints.insets = new Insets(1, 10, 1, 0);
		constraints.ipadx = 0;
		constraints.ipady = 0;
		innerPanel.add(this.zoomInButton, constraints);

		constraints.gridx = 2;
		constraints.gridy = 0;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 0;
		constraints.weighty = 0;
		constraints.anchor = GridBagConstraints.WEST;
		constraints.fill = GridBagConstraints.NONE;
		constraints.insets = new Insets(1, 0, 1, 0);
		constraints.ipadx = 0;
		constraints.ipady = 0;
		innerPanel.add(this.zoomOutButton, constraints);

		constraints.gridx = 3;
		constraints.gridy = 0;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 0;
		constraints.weighty = 0;
		constraints.anchor = GridBagConstraints.WEST;
		constraints.fill = GridBagConstraints.NONE;
		constraints.insets = new Insets(1, 0, 1, 0);
		constraints.ipadx = 0;
		constraints.ipady = 0;
		innerPanel.add(this.zoomToRectButton, constraints);
		// this.add(this.zoomToPointButton,new
		// GridBagConstraints(0,0,1,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new
		// Insets(1,0,1,0),0,0));

		constraints.gridx = 4;
		constraints.gridy = 0;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 0;
		constraints.weighty = 0;
		constraints.anchor = GridBagConstraints.WEST;
		constraints.fill = GridBagConstraints.NONE;
		constraints.insets = new Insets(1, 10, 1, 0);
		constraints.ipadx = 0;
		constraints.ipady = 0;
		innerPanel.add(this.moveToCenterButton, constraints);

		constraints.gridx = 5;
		constraints.gridy = 0;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 0;
		constraints.weighty = 0;
		constraints.anchor = GridBagConstraints.WEST;
		constraints.fill = GridBagConstraints.NONE;
		constraints.insets = new Insets(1, 0, 1, 0);
		constraints.ipadx = 0;
		constraints.ipady = 0;
		innerPanel.add(this.centerObjectButton, constraints);

		constraints.gridx = 6;
		constraints.gridy = 0;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 0;
		constraints.weighty = 0;
		constraints.anchor = GridBagConstraints.WEST;
		constraints.fill = GridBagConstraints.NONE;
		constraints.insets = new Insets(1, 10, 1, 0);
		constraints.ipadx = 0;
		constraints.ipady = 0;
		innerPanel.add(this.measureDistanceButton, constraints);
		// this.add(this.moveFixedButton,new
		// GridBagConstraints(7,0,1,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new
		// Insets(1,0,1,0),0,0));

		constraints.gridx = 8;
		constraints.gridy = 0;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 0;
		constraints.weighty = 0;
		constraints.anchor = GridBagConstraints.WEST;
		constraints.fill = GridBagConstraints.NONE;
		constraints.insets = new Insets(1, 20, 1, 0);
		constraints.ipadx = 0;
		constraints.ipady = 0;
		innerPanel.add(this.showNodesButton, constraints);

		constraints.gridx = 9;
		constraints.gridy = 0;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 0;
		constraints.weighty = 0;
		constraints.anchor = GridBagConstraints.WEST;
		constraints.fill = GridBagConstraints.NONE;
		constraints.insets = new Insets(1, 20, 1, 0);
		constraints.ipadx = 0;
		constraints.ipady = 0;
		innerPanel.add(this.showIndicationButton, constraints);

		constraints.gridx = 10;
		constraints.gridy = 0;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 0;
		constraints.weighty = 0;
		constraints.anchor = GridBagConstraints.WEST;
		constraints.fill = GridBagConstraints.NONE;
		constraints.insets = new Insets(1, 10, 1, 0);
		constraints.ipadx = 0;
		constraints.ipady = 0;
		innerPanel.add(this.showNodeLinkToggleButton, constraints);

		constraints.gridx = 11;
		constraints.gridy = 0;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 0;
		constraints.weighty = 0;
		constraints.anchor = GridBagConstraints.WEST;
		constraints.fill = GridBagConstraints.NONE;
		constraints.insets = new Insets(1, 0, 1, 0);
		constraints.ipadx = 0;
		constraints.ipady = 0;
		innerPanel.add(this.showPhysicalToggleButton, constraints);

		constraints.gridx = 12;
		constraints.gridy = 0;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 0;
		constraints.weighty = 0;
		constraints.anchor = GridBagConstraints.WEST;
		constraints.fill = GridBagConstraints.NONE;
		constraints.insets = new Insets(1, 0, 1, 0);
		constraints.ipadx = 0;
		constraints.ipady = 0;
		innerPanel.add(this.showCablePathToggleButton, constraints);

		constraints.gridx = 13;
		constraints.gridy = 0;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 0;
		constraints.weighty = 0;
		constraints.anchor = GridBagConstraints.WEST;
		constraints.fill = GridBagConstraints.NONE;
		constraints.insets = new Insets(1, 0, 1, 0);
		constraints.ipadx = 0;
		constraints.ipady = 0;
		innerPanel.add(this.showTransPathToggleButton, constraints);

		constraints.gridx = 14;
		constraints.gridy = 0;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 0;
		constraints.weighty = 0;
		constraints.anchor = GridBagConstraints.WEST;
		constraints.fill = GridBagConstraints.NONE;
		constraints.insets = new Insets(1, 10, 1, 0);
		constraints.ipadx = 0;
		constraints.ipady = 0;
		innerPanel.add(this.nodeSizePanel, constraints);

		constraints.gridx = 15;
		constraints.gridy = 0;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 0;
		constraints.weighty = 0;
		constraints.anchor = GridBagConstraints.WEST;
		constraints.fill = GridBagConstraints.NONE;
		constraints.insets = new Insets(1, 10, 1, 0);
		constraints.ipadx = 0;
		constraints.ipady = 0;
		innerPanel.add(this.optionsButton, constraints);

		constraints.gridx = 16;
		constraints.gridy = 0;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 0;
		constraints.weighty = 0;
		constraints.anchor = GridBagConstraints.WEST;
		constraints.fill = GridBagConstraints.NONE;
		constraints.insets = new Insets(1, 10, 1, 0);
		constraints.ipadx = 0;
		constraints.ipady = 0;
		innerPanel.add(this.layersButton, constraints);

		constraints.gridx = 17;
		constraints.gridy = 0;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 0;
		constraints.weighty = 0;
		constraints.anchor = GridBagConstraints.WEST;
		constraints.fill = GridBagConstraints.NONE;
		constraints.insets = new Insets(1, 10, 1, 0);
		constraints.ipadx = 0;
		constraints.ipady = 0;
		innerPanel.add(this.undoButton, constraints);

		constraints.gridx = 18;
		constraints.gridy = 0;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 0;
		constraints.weighty = 0;
		constraints.anchor = GridBagConstraints.WEST;
		constraints.fill = GridBagConstraints.NONE;
		constraints.insets = new Insets(1, 0, 1, 0);
		constraints.ipadx = 0;
		constraints.ipady = 0;
		innerPanel.add(this.redoButton, constraints);

		// constraints.gridx = 19;
		// constraints.gridy = 0;
		// constraints.gridwidth = 1;
		// constraints.gridheight = 1;
		// constraints.weightx = 0;
		// constraints.weighty = 0;
		// constraints.anchor = GridBagConstraints.WEST;
		// constraints.fill = GridBagConstraints.NONE;
		// constraints.insets = new Insets(1,10,1,0);
		// constraints.ipadx = 0;
		// constraints.ipady = 0;
		// innerPanel.add(this.shotButton, constraints);

		constraints.gridx = 20;
		constraints.gridy = 0;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 0;
		constraints.weighty = 0;
		constraints.anchor = GridBagConstraints.WEST;
		constraints.fill = GridBagConstraints.NONE;
		constraints.insets = UIManager.getInsets(ResourceKeys.INSETS_NULL);
		constraints.ipadx = 0;
		constraints.ipady = 0;
		innerPanel.add(Box.createHorizontalGlue(), constraints);

		this.setLayout(new BorderLayout());
		this.add(innerPanel, BorderLayout.WEST);
		// this.add(this.penp);
	}

	// ¬ключить выключить панель
	public void setEnableDisablePanel(boolean b) {
		this.aModel.setEnabled(
				MapApplicationModel.OPERATION_CENTER_SELECTION,
				b);
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
		this.aModel.setEnabled(MapApplicationModel.MODE_INDICATION, b);
		this.aModel.setEnabled(MapApplicationModel.OPERATION_HAND_PAN, b);
		this.aModel.setEnabled(
				MapApplicationModel.OPERATION_MEASURE_DISTANCE,
				b);

		this.aModel.fireModelChanged();

		this.nodeSizePanel.setEnabled(b);
	}

	public void setModel(ApplicationModel a) {
		this.aModel = a;

		Command command = this.aModel
				.getCommand(MapApplicationModel.MODE_NODES);
		command.setParameter("button", this.showNodesButton); //$NON-NLS-1$

		command = this.aModel.getCommand(MapApplicationModel.MODE_INDICATION);
		command.setParameter("button", this.showIndicationButton); //$NON-NLS-1$

		// OPERATION_HAND_PAN not accessible with descrete navigation
		// this.aModel.setAccessible(MapApplicationModel.OPERATION_HAND_PAN,
		// !MapPropertiesManager.isDescreteNavigation());

		this.aModel.setSelected(MapApplicationModel.MODE_LINK, true);
		this.aModel.setSelected(MapApplicationModel.MODE_NODES, true);
		this.aModel.setSelected(MapApplicationModel.MODE_INDICATION, true);
		this.aModel.fireModelChanged();
	}

	public ApplicationModel getModel() {
		return this.aModel;
	}

	public void modelChanged(String elementName) {
		modelChanged(new String[] { elementName
		});
	}

	public void modelChanged(String e[]) {
		this.zoomInButton.setVisible(this.aModel
				.isVisible(MapApplicationModel.OPERATION_ZOOM_IN));
		this.zoomInButton.setEnabled(this.aModel
				.isEnabled(MapApplicationModel.OPERATION_ZOOM_IN));

		this.zoomOutButton.setVisible(this.aModel
				.isVisible(MapApplicationModel.OPERATION_ZOOM_OUT));
		this.zoomOutButton.setEnabled(this.aModel
				.isEnabled(MapApplicationModel.OPERATION_ZOOM_OUT));

		this.centerObjectButton.setVisible(this.aModel
				.isVisible(MapApplicationModel.OPERATION_CENTER_SELECTION));
		this.centerObjectButton.setEnabled(this.aModel
				.isEnabled(MapApplicationModel.OPERATION_CENTER_SELECTION));

		this.zoomToPointButton.setVisible(this.aModel
				.isVisible(MapApplicationModel.OPERATION_ZOOM_TO_POINT));
		this.zoomToPointButton.setEnabled(this.aModel
				.isEnabled(MapApplicationModel.OPERATION_ZOOM_TO_POINT));
		this.zoomToPointButton.setSelected(this.aModel
				.isSelected(MapApplicationModel.OPERATION_ZOOM_TO_POINT));

		this.zoomToRectButton.setVisible(this.aModel
				.isVisible(MapApplicationModel.OPERATION_ZOOM_BOX));
		this.zoomToRectButton.setEnabled(this.aModel
				.isEnabled(MapApplicationModel.OPERATION_ZOOM_BOX));
		this.zoomToRectButton.setSelected(this.aModel
				.isSelected(MapApplicationModel.OPERATION_ZOOM_BOX));

		this.moveToCenterButton.setVisible(this.aModel
				.isVisible(MapApplicationModel.OPERATION_MOVE_TO_CENTER));
		this.moveToCenterButton.setEnabled(this.aModel
				.isEnabled(MapApplicationModel.OPERATION_MOVE_TO_CENTER));
		this.moveToCenterButton.setSelected(this.aModel
				.isSelected(MapApplicationModel.OPERATION_MOVE_TO_CENTER));

		this.moveHandButton.setVisible(this.aModel
				.isVisible(MapApplicationModel.OPERATION_HAND_PAN));
		this.moveHandButton.setEnabled(this.aModel
				.isEnabled(MapApplicationModel.OPERATION_HAND_PAN));
		this.moveHandButton.setSelected(this.aModel
				.isSelected(MapApplicationModel.OPERATION_HAND_PAN));

		this.measureDistanceButton.setVisible(this.aModel
				.isVisible(MapApplicationModel.OPERATION_MEASURE_DISTANCE));
		this.measureDistanceButton.setEnabled(this.aModel
				.isEnabled(MapApplicationModel.OPERATION_MEASURE_DISTANCE));
		this.measureDistanceButton.setSelected(this.aModel
				.isSelected(MapApplicationModel.OPERATION_MEASURE_DISTANCE));

		this.moveFixedButton.setVisible(this.aModel
				.isVisible(MapApplicationModel.OPERATION_MOVE_FIXED));
		this.moveFixedButton.setEnabled(this.aModel
				.isEnabled(MapApplicationModel.OPERATION_MOVE_FIXED));
		this.moveFixedButton.setSelected(this.aModel
				.isSelected(MapApplicationModel.OPERATION_MOVE_FIXED));

		this.showNodesButton.setVisible(this.aModel
				.isVisible(MapApplicationModel.MODE_NODES));
		this.showNodesButton.setEnabled(this.aModel
				.isEnabled(MapApplicationModel.MODE_NODES));
		this.showNodesButton.setSelected(this.aModel
				.isSelected(MapApplicationModel.MODE_NODES));

		this.showIndicationButton.setVisible(this.aModel
				.isVisible(MapApplicationModel.MODE_INDICATION));
		this.showIndicationButton.setEnabled(this.aModel
				.isEnabled(MapApplicationModel.MODE_INDICATION));
		this.showIndicationButton.setSelected(this.aModel
				.isSelected(MapApplicationModel.MODE_INDICATION));

		this.showNodeLinkToggleButton.setVisible(this.aModel
				.isVisible(MapApplicationModel.MODE_NODE_LINK));
		this.showNodeLinkToggleButton.setEnabled(this.aModel
				.isEnabled(MapApplicationModel.MODE_NODE_LINK));
		this.showNodeLinkToggleButton.setSelected(this.aModel
				.isSelected(MapApplicationModel.MODE_NODE_LINK));

		this.showPhysicalToggleButton.setVisible(this.aModel
				.isVisible(MapApplicationModel.MODE_LINK));
		this.showPhysicalToggleButton.setEnabled(this.aModel
				.isEnabled(MapApplicationModel.MODE_LINK));
		this.showPhysicalToggleButton.setSelected(this.aModel
				.isSelected(MapApplicationModel.MODE_LINK));

		this.showCablePathToggleButton.setVisible(this.aModel
				.isVisible(MapApplicationModel.MODE_CABLE_PATH));
		this.showCablePathToggleButton.setEnabled(this.aModel
				.isEnabled(MapApplicationModel.MODE_CABLE_PATH));
		this.showCablePathToggleButton.setSelected(this.aModel
				.isSelected(MapApplicationModel.MODE_CABLE_PATH));

		this.showTransPathToggleButton.setVisible(this.aModel
				.isVisible(MapApplicationModel.MODE_PATH));
		this.showTransPathToggleButton.setEnabled(this.aModel
				.isEnabled(MapApplicationModel.MODE_PATH));
		this.showTransPathToggleButton.setSelected(this.aModel
				.isSelected(MapApplicationModel.MODE_PATH));
	}

	public void buttonPressed(ActionEvent e) {
		if(this.aModel == null)
			return;
		AbstractButton jb = (AbstractButton) e.getSource();
		String s = jb.getName();
		Command command = this.aModel.getCommand(s);
		command.execute();
	}

	private class MapToolBarActionAdapter implements
			java.awt.event.ActionListener {
		MapToolBar adaptee;

		MapToolBarActionAdapter(MapToolBar adaptee) {
			this.adaptee = adaptee;
		}

		public void actionPerformed(ActionEvent e) {
			this.adaptee.buttonPressed(e);
		}
	}

}
