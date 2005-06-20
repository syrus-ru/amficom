/*
 * $Id: WrapperedTableChooserDialog.java,v 1.2 2005/06/20 15:30:56 krupenn Exp $
 * Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ
 */

package com.syrus.AMFICOM.client.UI.dialogs;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.SystemColor;
import java.awt.Toolkit;
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
import com.syrus.AMFICOM.client.resource.LangModelGeneral;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.util.Wrapper;

/**
 * Класс используется для отображения окна со списком объектов с тем,
 * чтобы пользователь мог выбрать один из них. 
 * В окне выбора объекта можно включить функцию удаления выбранного объекта.
 * Для того, чтобы включить эту возможность, используется параметр canDelete
 *
 * @version $Revision: 1.2 $
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

		buttonOpen.setText(LangModelGeneral.getString("Choose"));
		buttonCancel.setText(LangModelGeneral.getString("Button.Cancel"));
		buttonDelete.setText(LangModelGeneral.getString("Remove"));

		buttonOpen.setEnabled(false);
		buttonDelete.setEnabled(false);

		table.getSelectionModel().addListSelectionListener(
				new ListSelectionListener() {
					public void valueChanged(ListSelectionEvent e) {
						Object or = model.getObject(table.getSelectedRow());
						if(or != null) {
							buttonOpen.setEnabled(true);
							buttonDelete.setEnabled(true);
						}
						else {
							buttonOpen.setEnabled(false);
							buttonDelete.setEnabled(false);
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

		while(true) {
			dialog.setVisible(true);

			Object selectedValue = optionPane.getValue();
	
			if (selectedValue == buttonOpen) {
				returnObject = model.getObject(table.getSelectedRow());
				break;
			}
			if (selectedValue == buttonCancel) {
				break;
			}
			if (selectedValue == buttonDelete) {
				Object obj = model.getObject(table.getSelectedRow());
				Identifier id = ((StorableObject )obj).getId();
				StorableObjectPool.delete(id);
				try {
					StorableObjectPool.flush(id, true);
					model.getValues().remove(obj);
					model.fireTableDataChanged();
					buttonOpen.setEnabled(false);
					buttonDelete.setEnabled(false);
				} catch(ApplicationException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}
		dialog.dispose();
		return returnObject;
	}

	private WrapperedTableChooserDialog() {
		// empty
	}

}
