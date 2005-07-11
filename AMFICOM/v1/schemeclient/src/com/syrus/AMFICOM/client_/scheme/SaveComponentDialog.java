/*-
 * $Id: SaveComponentDialog.java,v 1.4 2005/07/11 12:31:38 stas Exp $
 *
 * Copyright � 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme;

import javax.swing.JDialog;

import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client_.scheme.ui.SchemeProtoElementGeneralPanel;
import com.syrus.AMFICOM.scheme.SchemeProtoElement;

/**
 * @author $Author: stas $
 * @version $Revision: 1.4 $, $Date: 2005/07/11 12:31:38 $
 * @module schemeclient_v1
 */

public class SaveComponentDialog extends JDialog {
	SchemeProtoElement proto;
	SchemeProtoElementGeneralPanel componentPanel;
	ApplicationContext aContext;
/*
	public SaveComponentDialog(ApplicationContext aContext) {
		super(Environment.getActiveWindow());
		this.aContext = aContext;
		try {
			jbInit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void jbInit() throws Exception {
		setTitle("���������� ����������");
		setModal(true);

		this.addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				cancelButton_actionPerformed();
			}
		});

		Dimension frameSize = new Dimension(400, 165);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setSize(frameSize);
		setLocation((screenSize.width - frameSize.width) / 2,
				(screenSize.height - frameSize.height) / 2);

		getContentPane().setLayout(new BorderLayout());
		// COMPONENT
		componentPanel = new SchemeProtoElementGeneralPanel();
		this.getContentPane().add(componentPanel, BorderLayout.CENTER);

		// BUTTONS
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout());

		JButton okButton = new JButton("OK");
		JButton cancelButton = new JButton("������");
		okButton.setPreferredSize(new Dimension(80, 25));
		cancelButton.setPreferredSize(new Dimension(80, 25));
		buttonPanel.add(okButton, FlowLayout.LEFT);
		buttonPanel.add(cancelButton, FlowLayout.CENTER);
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				this_okButtonActionPerformed();
			}
		});
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				cancelButton_actionPerformed();
			}
		});
		getContentPane().add(buttonPanel, BorderLayout.SOUTH);
	}

	public void init(SchemeProtoElement proto1) {
		this.proto = proto1;
		componentPanel.setObject(proto1);
		setVisible(true);
	}

	void cancelButton_actionPerformed() {
		dispose();
	}

	void this_okButtonActionPerformed() {
		SchemeProtoGroup scheme_proto = componentPanel.getSchemeProtoGroup();
		if (scheme_proto == null) {
			JOptionPane.showMessageDialog(Environment.getActiveWindow(),
					"�� ������ ������ �����������.", "������", JOptionPane.OK_OPTION);
			return;
		}
		if (!MiscUtil.validName(componentPanel.getProtoName())) {
			JOptionPane.showMessageDialog(Environment.getActiveWindow(),
					"������������ �������� ����������.", "������", JOptionPane.OK_OPTION);
			return;
		}

		proto.setParentSchemeProtoGroup(scheme_proto);
		proto.setName(componentPanel.getProtoName());
		EquipmentType eqt = proto.getEquipmentType();

		try {
			StorableObjectPool.putStorableObject(proto);
			StorableObjectPool.putStorableObject(eqt);
			aContext.getDispatcher().notify(
					new TreeListSelectionEvent(scheme_proto,
							TreeListSelectionEvent.REFRESH_EVENT));
			dispose();

			JOptionPane.showMessageDialog(Environment.getActiveWindow(), "��������� "
					+ proto.getName() + " ������� ��������", "���������",
					JOptionPane.INFORMATION_MESSAGE);
		} catch (IllegalObjectEntityException ex) {
			ex.printStackTrace();
		}
	}*/
}
