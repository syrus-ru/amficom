/*
 * $Id: SetBackgroundSizeAction.java,v 1.1 2005/04/05 14:07:53 stas Exp $
 *
 * Copyright � 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme.graph.actions;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import oracle.jdeveloper.layout.VerticalFlowLayout;

import com.syrus.AMFICOM.Client.General.Event.SchemeEvent;
import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.General.UI.AComboBox;
import com.syrus.AMFICOM.client_.scheme.graph.*;
import com.syrus.AMFICOM.scheme.Scheme;

/**
 * @author $Author: stas $
 * @version $Revision: 1.1 $, $Date: 2005/04/05 14:07:53 $
 * @module schemeclient_v1
 */

public class SetBackgroundSizeAction extends AbstractAction {
	UgoTabbedPane pane;

	public SetBackgroundSizeAction(UgoTabbedPane pane) {
		super(Constants.backgroundSize);
		this.pane = pane;
	}

	public void actionPerformed(ActionEvent e) {
		Scheme scheme = pane.getCurrentPanel().getSchemeResource().getScheme();
		if (scheme != null) {
			GraphSizeFrame f = new GraphSizeFrame();
			if (f.init(scheme)) {
				scheme.setWidth(f.newsize.width);
				scheme.setHeight(f.newsize.height);
				SchemeGraph graph = pane.getGraph();
				pane.getContext().getDispatcher().notify(
						new SchemeEvent(this, graph, null, SchemeEvent.SCHEME_CHANGED));
				graph.setActualSize(f.newsize);
				graph.updateUI();
			}
		}
	}

	class GraphSizeFrame extends JDialog {
		private JRadioButton fixRButton = new JRadioButton("�������������", true);
		private JRadioButton arbRButton = new JRadioButton("������������");
		private AComboBox fixComboBox = new AComboBox();
		private JTextField xsizeTextField = new JTextField();
		private JTextField ysizeTextField = new JTextField();
		private JLabel wLabel = new JLabel("������ (��):  ");
		private JLabel hLabel = new JLabel("������ (��):  ");
		private JButton okButton = new JButton("OK");
		private JButton cancelButton = new JButton("������");
		boolean res = false;
		Dimension newsize;

		GraphSizeFrame() {
			super(Environment.getActiveWindow());

			try {
				jbInit();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		private void jbInit() throws Exception {
			setTitle("������ �����");
			setModal(true);

			Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
			Dimension frameSize = new Dimension(300, 160);
			setSize(frameSize);
			setLocation(screenSize.width / 2 - 150, screenSize.height / 2 - 80);

			ButtonGroup buttonGroup = new ButtonGroup();

			fixComboBox.addItemListener(new java.awt.event.ItemListener() {
				public void itemStateChanged(ItemEvent e) {
					fixComboBox_itemStateChanged();
				}
			});
			fixRButton.addItemListener(new java.awt.event.ItemListener() {
				public void itemStateChanged(ItemEvent e) {
					fixRButton_itemStateChanged();
				}
			});
			okButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(ActionEvent e) {
					okButton_actionPerformed();
				}
			});
			cancelButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(ActionEvent e) {
					cancelButton_actionPerformed();
				}
			});
			buttonGroup.add(fixRButton);
			buttonGroup.add(arbRButton);

			//fixComboBox.setPreferredSize(new Dimension(80, 10));
			xsizeTextField.setPreferredSize(new Dimension(50, 10));
			ysizeTextField.setPreferredSize(new Dimension(50, 10));

			JPanel leftPanel = new JPanel(new VerticalFlowLayout());
			leftPanel.add(fixRButton);
			leftPanel.add(arbRButton);

			JPanel rightPanel = new JPanel(new VerticalFlowLayout());
			rightPanel.add(fixComboBox);
			JPanel xPanel = new JPanel(new BorderLayout());
			JPanel yPanel = new JPanel(new BorderLayout());
			xPanel.add(wLabel, BorderLayout.CENTER);
			xPanel.add(xsizeTextField, BorderLayout.EAST);
			yPanel.add(hLabel, BorderLayout.CENTER);
			yPanel.add(ysizeTextField, BorderLayout.EAST);
			rightPanel.add(xPanel);
			rightPanel.add(yPanel);

			JPanel buttonPanel = new JPanel();
			buttonPanel.add(okButton);
			buttonPanel.add(cancelButton);

			getContentPane().setLayout(new BorderLayout());
			getContentPane().add(leftPanel, BorderLayout.WEST);
			getContentPane().add(rightPanel, BorderLayout.CENTER);
			getContentPane().add(buttonPanel, BorderLayout.SOUTH);

			fixRButton_itemStateChanged();
		}

		boolean init(Scheme scheme) {
			fixComboBox.addItem("A0 (840�� x 1188��)");
			fixComboBox.addItem("A1 (840�� x 594��)");
			fixComboBox.addItem("A2 (420�� x 594��)");
			fixComboBox.addItem("A3 (420�� x 297��)");
			fixComboBox.addItem("A4 (210�� x 297��)");

			if (scheme.getWidth() == Constants.A0.width
					&& scheme.getHeight() == Constants.A0.height)
				fixComboBox.setSelectedIndex(0);
			else if (scheme.getWidth() == Constants.A1.width
					&& scheme.getHeight() == Constants.A1.height)
				fixComboBox.setSelectedIndex(1);
			else if (scheme.getWidth() == Constants.A2.width
					&& scheme.getHeight() == Constants.A2.height)
				fixComboBox.setSelectedIndex(2);
			else if (scheme.getWidth() == Constants.A3.width
					&& scheme.getHeight() == Constants.A3.height)
				fixComboBox.setSelectedIndex(3);
			else if (scheme.getWidth() == Constants.A4.width
					&& scheme.getHeight() == Constants.A4.height)
				fixComboBox.setSelectedIndex(4);
			else {
				arbRButton.doClick();
				xsizeTextField.setText(String.valueOf(Math
						.round((scheme.getWidth() * 10) / 40d)));
				ysizeTextField.setText(String.valueOf(Math
						.round((scheme.getHeight() * 10) / 40.067)));
			}

			setVisible(true);
			return res;
		}

		void fixRButton_itemStateChanged() {
			boolean b = fixRButton.isSelected();
			fixComboBox.setEnabled(b);
			xsizeTextField.setEnabled(!b);
			ysizeTextField.setEnabled(!b);
			wLabel.setEnabled(!b);
			hLabel.setEnabled(!b);

			fixComboBox_itemStateChanged();
		}

		void okButton_actionPerformed() {
			res = true;
			if (fixRButton.isSelected()) {
				int index = fixComboBox.getSelectedIndex();
				switch (index) {
				case 0:
					newsize = Constants.A0;
					break;
				case 1:
					newsize = Constants.A1;
					break;
				case 2:
					newsize = Constants.A2;
					break;
				case 3:
					newsize = Constants.A3;
					break;
				case 4:
					newsize = Constants.A4;
					break;
				}
			} else {
				try {
					double x = Double.parseDouble(xsizeTextField.getText());
					double y = Double.parseDouble(ysizeTextField.getText());
					newsize = new Dimension((int) Math.round(x * 40) / 10, (int) Math
							.round(y * 40.067) / 10);
				} catch (NumberFormatException ex) {
					JOptionPane.showMessageDialog(Environment.getActiveWindow(),
							"�����, ���, ��� ������������ �����", "������",
							JOptionPane.OK_OPTION);
					res = false;
					return;
				}
			}

			dispose();
		}

		void cancelButton_actionPerformed() {
			dispose();
		}

		void fixComboBox_itemStateChanged() {
			int index = fixComboBox.getSelectedIndex();
			switch (index) {
			case 4:
				xsizeTextField.setText("210");
				ysizeTextField.setText("297");
				break;
			case 3:
				xsizeTextField.setText("420");
				ysizeTextField.setText("297");
				break;
			case 2:
				xsizeTextField.setText("420");
				ysizeTextField.setText("594");
				break;
			case 1:
				xsizeTextField.setText("840");
				ysizeTextField.setText("594");
				break;
			case 0:
				xsizeTextField.setText("840");
				ysizeTextField.setText("1188");
				break;
			}
		}
	}

}