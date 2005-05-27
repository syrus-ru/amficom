
package com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI;

import java.awt.BorderLayout;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.Icon;
import javax.swing.JInternalFrame;
import javax.swing.UIManager;
import javax.swing.WindowConstants;

import com.syrus.AMFICOM.client.resource.ResourceKeys;

public class SimpleResizableFrame extends JInternalFrame {

	public ResizableLayeredPanel	panel;

	public SimpleResizableFrame() {
		this(new ResizableLayeredPanel());
	}

	public SimpleResizableFrame(ResizableLayeredPanel panel) {
		this.panel = panel;

		this.createGUI();
	}

	private void createGUI() {
		setFrameIcon((Icon) UIManager.get(ResourceKeys.ICON_GENERAL));
		this.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
		this.setResizable(true);
		this.setClosable(true);
		this.setMaximizable(true);
		this.setIconifiable(true);
		this.setDoubleBuffered(true);

		this.addPropertyChangeListener(JInternalFrame.IS_MAXIMUM_PROPERTY, new java.beans.PropertyChangeListener() {

			public void propertyChange(java.beans.PropertyChangeEvent evt) {
				if (evt.getPropertyName().equals(JInternalFrame.IS_MAXIMUM_PROPERTY)) {
					panel.resize();
				}
			}
		});
		this.addComponentListener(new ComponentAdapter() {

			public void componentResized(ComponentEvent e) {
				panel.resize();
			}
		});

		this.getContentPane().setLayout(new BorderLayout());
		this.getContentPane().add(panel, BorderLayout.CENTER);
	}

	public ScaledGraphPanel getTopGraphPanel() {
		return panel.getTopPanel();
	}

	public String getReportTitle() {
		return getTitle();
	}

	public void setGraph(	double[] y,
							double deltaX,
							boolean isReversedY,
							String id) {
		ScaledGraphPanel p = new ScaledGraphPanel(panel, y, deltaX);
		p.setColorModel(id);
		p.inversed_y = isReversedY;

		panel.setGraphPanel(p);
		repaint();
	}

	public void setGraph(	SimpleGraphPanel p,
							boolean isReversedY,
							String id) {
		p.setColorModel(id);
		panel.setGraphPanel(p);
		panel.setInversedY(isReversedY);
		repaint();
	}

	public void updScales() {
		panel.updScale2fit();
	}

	public void doDefaultCloseAction() {
		if (isMaximum())
			try {
				setMaximum(false);
			} catch (java.beans.PropertyVetoException ex) {
				ex.printStackTrace();
			}
		super.doDefaultCloseAction();
	}
}
