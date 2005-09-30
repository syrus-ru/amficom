/*-
 * $$Id: WrapperedTableChooserDialog.java,v 1.12 2005/09/30 16:08:36 krupenn Exp $$
 *
 * Copyright 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.UI.dialogs;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.SystemColor;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.syrus.AMFICOM.client.UI.WrapperedTable;
import com.syrus.AMFICOM.client.UI.WrapperedTableModel;
import com.syrus.AMFICOM.client.model.Environment;
import com.syrus.AMFICOM.client.resource.LangModelMap;
import com.syrus.AMFICOM.client.resource.MapEditorResourceKeys;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.util.Wrapper;

/**
 * ����� ������������ ��� ����������� ���� �� ������� �������� � ���,
 * ����� ������������ ��� ������� ���� �� ���. 
 * � ���� ������ ������� ����� �������� ������� �������� ���������� �������.
 * ��� ����, ����� �������� ��� �����������, ������������ �������� canDelete
 *
 * @version $Revision: 1.12 $
 * @author $Author: krupenn $
 * @module commonclient_v1
 */
public class WrapperedTableChooserDialog extends JDialog {
	public static Object showChooserDialog(
			String title,
			Collection contents,
			Wrapper wrapper,
			String[] keys) {
		return showChooserDialog(title, contents, wrapper, keys, false);
	}

	public static Object showChooserDialog(
			String title,
			Collection contents,
			Wrapper wrapper,
			String[] keys,
			boolean canDelete) {

		Object returnObject = null;
		
		JPanel mainPanel = new JPanel(new BorderLayout());

		final WrapperedTableModel model = new WrapperedTableModel(wrapper, keys);
		final WrapperedTable table = new WrapperedTable(model);
		final JScrollPane scrollPane = new JScrollPane();

		model.setValues(contents);
		table.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		scrollPane.getViewport().add(table);
		scrollPane.setWheelScrollingEnabled(true);
		scrollPane.getViewport().setBackground(SystemColor.window);
		table.setBackground(SystemColor.window);

		mainPanel.add(scrollPane, BorderLayout.CENTER);

		final JButton buttonOpen = new JButton();
		final JButton buttonCancel = new JButton();
		final JButton buttonDelete = new JButton();

		buttonOpen.setText(LangModelMap.getString(MapEditorResourceKeys.BUTTON_CHOOSE));
		buttonCancel.setText(LangModelMap.getString(MapEditorResourceKeys.BUTTON_CANCEL));
		buttonDelete.setText(LangModelMap.getString(MapEditorResourceKeys.BUTTON_REMOVE));

		buttonOpen.setEnabled(false);
		buttonDelete.setEnabled(false);

		table.getSelectionModel().addListSelectionListener(
				new ListSelectionListener() {
					public void valueChanged(ListSelectionEvent e) {
						if(e.getValueIsAdjusting()) {
							return;
						}
						int row = table.getSelectedRow();
						if(row == -1) {
							buttonOpen.setEnabled(false);
							buttonDelete.setEnabled(false);
						}
						else {
							buttonOpen.setEnabled(true);
							buttonDelete.setEnabled(true);
						}
					}
				});

		Object[] options;
		if(canDelete) {
			options = new Object[] { buttonOpen, buttonCancel, buttonDelete };
		}
		else {
			options = new Object[] { buttonOpen, buttonCancel };
		}
		
		final JOptionPane optionPane = new JOptionPane(
				mainPanel, 
				JOptionPane.PLAIN_MESSAGE,
				JOptionPane.OK_CANCEL_OPTION, 
				null, 
				options);

		final JDialog dialog = optionPane.createDialog(
				Environment.getActiveWindow(), title);
		Dimension screenDim = Toolkit.getDefaultToolkit().getScreenSize();
		int width = Math.min(screenDim.width / 2, 590);
		int height = Math.min(screenDim.height / 2, 400);
		dialog.setSize(new Dimension(width, height));
		dialog.setLocation(screenDim.width / 2 - width / 2, screenDim.height / 2 - height / 2);
		dialog.setModal(true);

		buttonOpen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				optionPane.setValue(buttonOpen);
			}
		});
		buttonCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				optionPane.setValue(buttonCancel);
			}
		});
		buttonDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int row = table.getSelectedRow();
				Object obj = model.getObject(row);
				Identifier id = ((StorableObject )obj).getId();
				StorableObjectPool.delete(id);
				try {
					StorableObjectPool.flush(id, LoginManager.getUserId(), true);
					model.removeObject(obj);
					model.fireTableDataChanged();
					buttonOpen.setEnabled(false);
					buttonDelete.setEnabled(false);
				} catch(ApplicationException e1) {
					e1.printStackTrace();
				}
			}
		});
		dialog.setVisible(true);
			
		Object selectedValue = optionPane.getValue();
		if (selectedValue == buttonOpen) {
			int row = table.getSelectedRow();
			if(row != -1) {
				returnObject = model.getObject(row);
			}
		}
		return returnObject;
	}

	private WrapperedTableChooserDialog() {
		// empty
	}

}
