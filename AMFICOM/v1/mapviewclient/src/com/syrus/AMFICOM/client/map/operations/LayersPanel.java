/*-
 * $$Id: LayersPanel.java,v 1.26 2006/02/15 11:27:06 stas Exp $$
 *
 * Copyright 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.map.operations;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.SystemColor;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;

import com.syrus.AMFICOM.client.event.StatusMessageEvent;
import com.syrus.AMFICOM.client.map.MapConnectionException;
import com.syrus.AMFICOM.client.map.MapDataException;
import com.syrus.AMFICOM.client.map.MapException;
import com.syrus.AMFICOM.client.map.NetMapViewer;
import com.syrus.AMFICOM.client.map.SpatialLayer;
import com.syrus.AMFICOM.client.map.ui.MapFrame;
import com.syrus.AMFICOM.client.resource.I18N;
import com.syrus.AMFICOM.client.resource.MapEditorResourceKeys;
import com.syrus.util.Log;

/**
 * панель управления отображением слоев
 * 
 * @version $Revision: 1.26 $, $Date: 2006/02/15 11:27:06 $
 * @author $Author: stas $
 * @author Andrei Kroupennikov
 * @module mapviewclient
 */
public class LayersPanel extends JPanel {
	GridBagLayout gridBagLayout1 = new GridBagLayout();

	/**
	 * панель списка слоев
	 */
	private JPanel layersPanel = new JPanel();

	/**
	 * панель заголовка
	 */
	private JPanel titlePanel = new JPanel();

	/**
	 * окно карты
	 */
	MapFrame mapFrame;

	/**
	 * список CheckBox'ов для слоёв
	 */
	private List checkBoxesList = new LinkedList();

	/**
	 * обработчик изменения видимости слоя
	 */
	private ActionListener actionListener = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			LayerVisibilityCheckBox cb = (LayerVisibilityCheckBox) e.getSource();
			SpatialLayer sl = cb.getSpatialLayer();
			sl.setVisible(cb.isSelected());
			NetMapViewer netMapViewer = LayersPanel.this.mapFrame.getMapViewer();
			try {
				netMapViewer.getRenderer().refreshLayers();
				if (sl.isVisibleAtScale(netMapViewer.getMapContext().getScale())) {
					netMapViewer.repaint(true);
				}
			} catch (MapException e1) {
				LayersPanel.this.mapFrame.getContext().getDispatcher().firePropertyChange(new StatusMessageEvent(this, StatusMessageEvent.STATUS_MESSAGE, I18N.getString(MapEditorResourceKeys.ERROR_MAP_EXCEPTION_SERVER_CONNECTION)));
				Log.errorMessage(e1);
			}
		}
	};

	/**
	 * По умолчанию
	 */
	public LayersPanel(MapFrame mapFrame) {
		jbInit();
		setMapFrame(mapFrame);
	}

	private void jbInit() {
		this.setToolTipText(I18N.getString(MapEditorResourceKeys.TITLE_CONFIGURE_TOPOLOGICAL_LAYERS));

		this.setLayout(new BorderLayout());

		this.setSize(new Dimension(370, 629));

		this.titlePanel.setLayout(this.gridBagLayout1);
		this.layersPanel.setLayout(this.gridBagLayout1);

		GridBagConstraints gridbagconstraints = new GridBagConstraints();

		ImageIcon ii = new ImageIcon(Toolkit.getDefaultToolkit().createImage(
				"images/seesymbs.gif")); //$NON-NLS-1$
		JLabel jlabel = new JLabel(ii);
		gridbagconstraints.gridx = 0;
		gridbagconstraints.gridy = 0;
		gridbagconstraints.weightx = 0.0D;
		gridbagconstraints.weighty = 0.0D;
		gridbagconstraints.gridwidth = 1;
		gridbagconstraints.gridheight = 1;
		gridbagconstraints.fill = 0;
		this.titlePanel.add(jlabel, gridbagconstraints);

		Component box = Box.createHorizontalBox();
		box.setSize(32, 1);
		gridbagconstraints.gridx = 1;
		gridbagconstraints.gridy = 0;
		gridbagconstraints.weightx = 0.0D;
		gridbagconstraints.weighty = 0.0D;
		gridbagconstraints.gridwidth = 1;
		gridbagconstraints.gridheight = 1;
		gridbagconstraints.fill = 0;
		this.titlePanel.add(box, gridbagconstraints);

		JLabel jlabel2 = new JLabel(I18N.getString(MapEditorResourceKeys.LABEL_LAYERS));
		gridbagconstraints.gridx = 2;
		gridbagconstraints.gridy = 0;
		gridbagconstraints.weightx = 1.0D;
		gridbagconstraints.weighty = 0.0D;
		gridbagconstraints.gridwidth = 1;
		gridbagconstraints.gridheight = 1;
		gridbagconstraints.fill = 2;
		this.titlePanel.add(jlabel2, gridbagconstraints);

		Component strut = Box.createHorizontalStrut(5);
		gridbagconstraints.gridx = 0;
		gridbagconstraints.gridy = 1;
		gridbagconstraints.weightx = 0.0D;
		gridbagconstraints.weighty = 0.0D;
		gridbagconstraints.gridwidth = 3;
		gridbagconstraints.gridheight = 1;
		gridbagconstraints.fill = 0;
		this.titlePanel.add(strut, gridbagconstraints);

		JSeparator jseparator = new JSeparator();
		gridbagconstraints.gridx = 0;
		gridbagconstraints.gridy = 2;
		gridbagconstraints.weightx = 1.0D;
		gridbagconstraints.weighty = 0.0D;
		gridbagconstraints.gridwidth = 3;
		gridbagconstraints.gridheight = 1;
		gridbagconstraints.fill = 2;
		this.titlePanel.add(jseparator, gridbagconstraints);

		this.add(this.titlePanel, BorderLayout.NORTH);
		JScrollPane scrollPane = new JScrollPane(this.layersPanel);
		scrollPane.setAutoscrolls(true);
		this.add(scrollPane, BorderLayout.CENTER);
	}

	/**
	 * установка окна карты -> перерисовка или обнуление списка слоев
	 */
	public void setMapFrame(MapFrame mapFrame) {
		this.mapFrame = mapFrame;
		if (mapFrame != null) {
			updateList();
		} else {
			clearList();
		}
	}

	/**
	 * очистить панель со списком слоев
	 */
	public void clearList() {
		this.checkBoxesList.clear();
		this.layersPanel.removeAll();
	}

	/**
	 * обновить окно со списком слоев
	 */
	public void updateList() {
		this.checkBoxesList.clear();
		this.layersPanel.removeAll();

		try {
			GridBagConstraints gridbagconstraints = new GridBagConstraints();
			Component imageLabel = null;

			int i = 0;
			for (Iterator it = this.mapFrame.getMapViewer().getMapContext()
					.getMapConnection().getLayers().iterator(); it.hasNext();) {
				SpatialLayer sl = (SpatialLayer) it.next();

				LayerVisibilityCheckBox lvCheckBox = new LayerVisibilityCheckBox(
						sl);
				lvCheckBox.addActionListener(this.actionListener);
				lvCheckBox.setBackground(this.layersPanel.getBackground());
				lvCheckBox.setAlignmentY(0.5F);
				lvCheckBox.setAlignmentX(0.8F);

				gridbagconstraints.gridx = 0;
				gridbagconstraints.gridy = i;
				gridbagconstraints.weightx = 0.0D;
				gridbagconstraints.weighty = 0.0D;
				gridbagconstraints.gridwidth = 1;
				gridbagconstraints.gridheight = 1;
				gridbagconstraints.fill = 0;
				this.layersPanel.add(lvCheckBox, gridbagconstraints);
				this.checkBoxesList.add(lvCheckBox);

				if (imageLabel != null) {
					imageLabel.setBackground(this.layersPanel.getBackground());
					gridbagconstraints.gridx = 1;
					gridbagconstraints.gridy = i;
					gridbagconstraints.weightx = 0.0D;
					gridbagconstraints.weighty = 0.0D;
					gridbagconstraints.gridwidth = 1;
					gridbagconstraints.gridheight = 1;
					gridbagconstraints.fill = 0;
					this.layersPanel.add(imageLabel, gridbagconstraints);
					lvCheckBox.setImageLabel(imageLabel);
				}

				JLabel nameLabel = new JLabel(" " + sl.getName()); //$NON-NLS-1$
				gridbagconstraints.gridx = 2;
				gridbagconstraints.gridy = i;
				gridbagconstraints.weightx = 1.0D;
				gridbagconstraints.weighty = 1.0D;
				gridbagconstraints.gridwidth = 1;
				gridbagconstraints.gridheight = 1;
				gridbagconstraints.fill = 2;
				this.layersPanel.add(nameLabel, gridbagconstraints);
				lvCheckBox.setNameLabel(nameLabel);
				i++;
			}
		} catch (MapException e) {
			Log.debugMessage("cannot get layers", Level.WARNING); //$NON-NLS-1$
			Log.debugMessage(e, Level.WARNING);
		}

		setVisibility();
	}

	/**
	 * разместить графические элемента управления отображением слоев на панели
	 * списка слоев
	 */
	public void setVisibility() {
		if (this.mapFrame == null)
			return;
		try {
			double currentScale = this.mapFrame.getMapViewer().getMapContext()
					.getScale();
			for (Iterator it = this.checkBoxesList.iterator(); it.hasNext();) {
				LayerVisibilityCheckBox curBox = (LayerVisibilityCheckBox) it
						.next();
				SpatialLayer boxSL = curBox.getSpatialLayer();

				curBox.setSelected(boxSL.isVisible());

				if (boxSL.isVisibleAtScale(currentScale))
					curBox.getNameLabel().setForeground(SystemColor.textText);
				else
					curBox.getNameLabel().setForeground(
							SystemColor.textInactiveText);
			}

			this.revalidate();
		} catch (MapConnectionException e) {
			Log.errorMessage(e);
		} catch (MapDataException e) {
			Log.errorMessage(e);
		}
	}

	public MapFrame getMapFrame() {
		return this.mapFrame;
	}

	/**
	 * чекбокс отображения слоя
	 */
	private class LayerVisibilityCheckBox extends JCheckBox {
		SpatialLayer sl;

		private Component imageLabel = null;

		private JLabel nameLabel = null;

		public LayerVisibilityCheckBox(SpatialLayer sl) {
			super();
			this.sl = sl;
		}

		public SpatialLayer getSpatialLayer() {
			return this.sl;
		}

		/**
		 * @return Returns the imageLabel.
		 */
		public Component getImageLabel() {
			return this.imageLabel;
		}

		/**
		 * @param imageLabel The imageLabel to set.
		 */
		public void setImageLabel(Component imageLabel) {
			this.imageLabel = imageLabel;
		}

		/**
		 * @return Returns the nameLabel.
		 */
		public JLabel getNameLabel() {
			return this.nameLabel;
		}

		/**
		 * @param nameLabel The nameLabel to set.
		 */
		public void setNameLabel(JLabel nameLabel) {
			this.nameLabel = nameLabel;
		}
	}
}
