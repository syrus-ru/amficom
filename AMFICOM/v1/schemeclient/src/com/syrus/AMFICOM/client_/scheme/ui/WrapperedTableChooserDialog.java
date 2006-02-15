/*-
 * $Id: WrapperedTableChooserDialog.java,v 1.2 2006/02/15 12:18:11 stas Exp $
 *
 * Copyright ї 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme.ui;

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
import com.syrus.AMFICOM.client.model.AbstractMainFrame;
import com.syrus.AMFICOM.client.resource.I18N;
import com.syrus.AMFICOM.client.resource.MapEditorResourceKeys;
import com.syrus.util.Wrapper;

/**
 * Класс используется для отображения окна со списком объектов с тем,
 * чтобы пользователь мог выбрать один из них. 
 * В окне выбора объекта можно включить функцию удаления выбранного объекта.
 * Для того, чтобы включить эту возможность, используется параметр canDelete
 *
 * @version $Revision: 1.2 $
 * @author $Author: stas $
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

		buttonOpen.setText(I18N.getString(MapEditorResourceKeys.BUTTON_CHOOSE));
		buttonCancel.setText(I18N.getString(MapEditorResourceKeys.BUTTON_CANCEL));
		buttonDelete.setText(I18N.getString(MapEditorResourceKeys.BUTTON_REMOVE));

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
				AbstractMainFrame.getActiveMainFrame(), title);
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

