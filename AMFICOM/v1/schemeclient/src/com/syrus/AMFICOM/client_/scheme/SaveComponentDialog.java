/*-
 * $Id: SaveComponentDialog.java,v 1.1 2005/04/05 14:10:45 stas Exp $
 *
 * Copyright ї 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import com.syrus.AMFICOM.Client.General.Event.TreeListSelectionEvent;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.Resource.MiscUtil;
import com.syrus.AMFICOM.Client.Schematics.Elements.ProtoElementPropsPanel;
import com.syrus.AMFICOM.configuration.*;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.scheme.*;

/**
 * @author $Author: stas $
 * @version $Revision: 1.1 $, $Date: 2005/04/05 14:10:45 $
 * @module schemeclient_v1
 */

public class SaveComponentDialog extends JDialog {
	SchemeProtoElement proto;
	ProtoElementPropsPanel componentPanel;
	ApplicationContext aContext;

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
		setTitle("Сохранение компонента");
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
		componentPanel = new ProtoElementPropsPanel(aContext);
		this.getContentPane().add(componentPanel, BorderLayout.CENTER);

		// BUTTONS
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout());

		JButton okButton = new JButton("OK");
		JButton cancelButton = new JButton("Отмена");
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
		componentPanel.init(proto1, true);
		setVisible(true);
	}

	void cancelButton_actionPerformed() {
		dispose();
	}

	void this_okButtonActionPerformed() {
		SchemeProtoGroup scheme_proto = componentPanel.getSchemeProtoGroup();
		if (scheme_proto == null) {
			JOptionPane.showMessageDialog(Environment.getActiveWindow(),
					"Не задана группа компонентов.", "Ошибка", JOptionPane.OK_OPTION);
			return;
		}
		if (!MiscUtil.validName(componentPanel.getProtoName())) {
			JOptionPane.showMessageDialog(Environment.getActiveWindow(),
					"Некорректное название компонента.", "Ошибка", JOptionPane.OK_OPTION);
			return;
		}

		proto.setParentSchemeProtoGroup(scheme_proto);
		proto.setName(componentPanel.getProtoName());
		EquipmentType eqt = proto.getEquipmentType();

		try {
			SchemeStorableObjectPool.putStorableObject(proto);
			ConfigurationStorableObjectPool.putStorableObject(eqt);
			aContext.getDispatcher().notify(
					new TreeListSelectionEvent(scheme_proto,
							TreeListSelectionEvent.REFRESH_EVENT));
			dispose();

			JOptionPane.showMessageDialog(Environment.getActiveWindow(), "Компонент "
					+ proto.getName() + " успешно сохранен", "Сообщение",
					JOptionPane.INFORMATION_MESSAGE);
		} catch (IllegalObjectEntityException ex) {
			ex.printStackTrace();
		}
	}
}
