/**
 * $Id: WrapperedComboChooserDialog.java,v 1.1 2005/06/08 13:44:06 krupenn Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.client.UI.dialogs;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;

import com.syrus.AMFICOM.client.UI.ReusedGridBagConstraints;
import com.syrus.AMFICOM.client.UI.WrapperedComboBox;
import com.syrus.AMFICOM.client.UI.WrapperedListModel;
import com.syrus.AMFICOM.client.model.Environment;
import com.syrus.AMFICOM.client.resource.LangModelGeneral;

/**
 * @author $Author: krupenn $
 * @version $Revision: 1.1 $
 * @module commonclient_v1
 */
public class WrapperedComboChooserDialog extends JDialog {
	private static final Insets INSETS = new Insets(10, 10, 10, 10);

	private JLabel jLabel = new JLabel();

	private GridBagLayout gridBagLayout1 = new GridBagLayout();

	WrapperedComboBox comboBox = null;
	private WrapperedListModel model = null;

	private JButton buttonOk = new JButton();
	private JButton buttonCancel = new JButton();

	int retCode = 0;
	Object selected;
	
	public static final int RET_OK = 1;
	public static final int RET_CANCEL = 2;

	protected WrapperedComboChooserDialog(
			Frame parent,
			String title,
			boolean modal) {
		super(parent, title, modal);

		this.comboBox = new WrapperedComboBox(
				NamedObjectController.getInstance(), 
				NamedObjectController.KEY_NAME, 
				NamedObjectController.KEY_NAME);
		this.model = (WrapperedListModel )this.comboBox.getModel();

		try {
			jbInit();
			pack();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public WrapperedComboChooserDialog(Collection contents) {
		this(Environment.getActiveWindow(), LangModelGeneral.getString("SelectElement"), false);
		setContents(contents);
	}

	public WrapperedComboChooserDialog(Collection contents, Object or) {
		this(contents);
		setSelected(or);
	}

	private void jbInit() throws Exception {
		getContentPane().setLayout(this.gridBagLayout1);

		this.setResizable(false);

		this.jLabel.setText(LangModelGeneral.getString("Element"));

		this.buttonOk.setText(LangModelGeneral.getString("Choose"));
		this.buttonOk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
						WrapperedComboChooserDialog.this.selected = WrapperedComboChooserDialog.this.comboBox.getSelectedItem();
						WrapperedComboChooserDialog.this.retCode = WrapperedComboChooserDialog.RET_OK;
					} catch(Exception ex) {
						WrapperedComboChooserDialog.this.selected = null;
						WrapperedComboChooserDialog.this.retCode = WrapperedComboChooserDialog.RET_CANCEL;
						ex.printStackTrace();
					}
					dispose();
				}
			});
		this.buttonCancel.setText(LangModelGeneral.getString("Cancel"));
		this.buttonCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
					WrapperedComboChooserDialog.this.retCode = WrapperedComboChooserDialog.RET_CANCEL;
					WrapperedComboChooserDialog.this.dispose();
				}
			});

		this.jLabel.setPreferredSize(new Dimension(130, 20));
		this.comboBox.setPreferredSize(new Dimension(200, 20));
		
		getContentPane().add(this.jLabel, ReusedGridBagConstraints.get(0, 0, 2, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, INSETS, 0, 0));
		getContentPane().add(this.comboBox, ReusedGridBagConstraints.get(2, 0, 2, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, INSETS, 0, 0));

		getContentPane().add(this.buttonOk, ReusedGridBagConstraints.get(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, INSETS, 0, 0));
		getContentPane().add(this.buttonCancel, ReusedGridBagConstraints.get(3, 1, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, INSETS, 0, 0));
	}

	public void setSelected(Object or) {
		this.model.setSelectedItem(or);
	}

	public void setContents(Collection contents) {
		this.model.removeAllElements();
		this.model.addElements(contents);
	}

	public Object getSelected() {
		return this.selected;
	}

	public int getReturnCode() {
		return this.retCode;
	}

	public void setLabel(String text) {
		this.jLabel.setText(text);
	}

}

