/**
 * $Id: EditorDialog.java,v 1.1 2005/06/08 13:44:06 krupenn Exp $
 * Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.client.UI.dialogs;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;

import com.syrus.AMFICOM.client.UI.StorableObjectEditor;
import com.syrus.AMFICOM.client.model.Environment;
import com.syrus.AMFICOM.client.resource.LangModelGeneral;

/**
 * @version $Revision: 1.1 $
 * @author $Author: krupenn $
 * @module commonclient_v1
 */
public class EditorDialog extends JDialog
{
	protected StorableObjectEditor editor = null;
	protected JPanel buttonPanel = new JPanel();
	protected JButton acceptButton = new JButton();
	protected JButton cancelButton = new JButton();
	protected JButton helpButton = new JButton();

	protected boolean accept = false;
	protected BorderLayout borderLayout1 = new BorderLayout();
	protected  FlowLayout flowLayout1 = new FlowLayout();

	public EditorDialog(
			String title,
			boolean modal,
			Object object,
			StorableObjectEditor editor) {
		super(Environment.getActiveWindow(), title, modal);

		this.editor = editor;
		if(editor != null)
			editor.setObject(object);

		try {
			jbInit();
			pack();
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}

	void jbInit() throws Exception {
		this.setResizable(false);
		this.setSize(new Dimension(590, 400));

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension frameSize = this.getSize();
		this.setLocation(
				(screenSize.width - frameSize.width) / 2,
				(screenSize.height - frameSize.height) / 2);
		
		JComponent uiComponent = this.editor.getGUI();
		uiComponent.setPreferredSize(new Dimension(590, 400));
		this.buttonPanel.setBorder(BorderFactory.createEtchedBorder());
		this.getContentPane().setLayout(this.borderLayout1);
		this.buttonPanel.setLayout(this.flowLayout1);

		this.acceptButton.setText(LangModelGeneral.getString("Ok"));
		this.acceptButton
				.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(ActionEvent e) {
						accept();
					}
				});
		this.cancelButton.setText(LangModelGeneral.getString("Cancel"));
		this.cancelButton
				.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(ActionEvent e) {
						cancel();
					}
				});
		this.helpButton.setText(LangModelGeneral.getString("Help"));

		this.flowLayout1.setAlignment(2);

		getContentPane().add(uiComponent, BorderLayout.CENTER);
		getContentPane().add(this.buttonPanel, BorderLayout.SOUTH);
		this.buttonPanel.add(this.acceptButton, null);
		this.buttonPanel.add(this.cancelButton, null);
	}

	public boolean ifAccept() {
		return this.accept;
	}

	void accept() {
		this.editor.commitChanges();
		this.accept = true;
		this.dispose();
	}

	void cancel() {
		this.accept = false;
		this.dispose();
	}
}
