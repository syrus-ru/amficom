
package com.syrus.AMFICOM.client.UI.dialogs;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;

import com.syrus.AMFICOM.client.resource.LangModelGeneral;

/**
 * @version $Revision: 1.3 $, $Date: 2005/06/15 11:27:19 $
 * @author $Author: bob $
 * @module commonclient_v1
 */
public class ModuleCodeDialog {

	private static final long	serialVersionUID	= 4049639009691120440L;

	private JPasswordField		fieldPassword;

	private JPanel				mainPanel;

	private int					result;

	private String				stb;
	private String				title;

	public static void main(String[] args) {
		ModuleCodeDialog moduleCodeDialog = new ModuleCodeDialog("pox", "title");		
	}

	public ModuleCodeDialog(String stb, String moduleTitle) {
		this.stb = stb;
		this.title = moduleTitle;
		this.showOpenSessionDialog(null);
	}

	public int getResult() {
		return this.result;
	}

	private void showOpenSessionDialog(final JFrame frame) {
		if (this.mainPanel == null) {
			this.mainPanel = new JPanel(new GridBagLayout());
			final GridBagConstraints gbc1 = new GridBagConstraints();

			gbc1.gridwidth = GridBagConstraints.REMAINDER;

			final JPanel textFieldsPanel = new JPanel(new GridBagLayout());
			this.fieldPassword = new JPasswordField();

			{
				final GridBagConstraints gbc = new GridBagConstraints();
				gbc.gridwidth = GridBagConstraints.RELATIVE;
				gbc.fill = GridBagConstraints.NONE;
				gbc.insets = new Insets(5, 5, 5, 5);
				gbc.weightx = 0.0;
				gbc.anchor = GridBagConstraints.EAST;
				textFieldsPanel.add(new JLabel(LangModelGeneral.getString("ModuleCode.Code") + ':'), gbc);
				gbc.gridwidth = GridBagConstraints.REMAINDER;
				gbc.fill = GridBagConstraints.HORIZONTAL;
				gbc.weightx = 1.0;
				gbc.anchor = GridBagConstraints.WEST;
				textFieldsPanel.add(this.fieldPassword, gbc);
			}

			gbc1.gridwidth = GridBagConstraints.REMAINDER;

			gbc1.weightx = 1.0;
			gbc1.fill = GridBagConstraints.HORIZONTAL;
			this.mainPanel.add(textFieldsPanel, gbc1);

		}

		this.fieldPassword.requestFocus();
		final String okButton = LangModelGeneral.getString("ModuleCode.Button.ok");
		final String cancelButton = LangModelGeneral.getString("ModuleCode.Button.cancel");
		final JOptionPane optionPane = new JOptionPane(this.mainPanel, JOptionPane.PLAIN_MESSAGE,
														JOptionPane.OK_CANCEL_OPTION, null, new Object[] { okButton,
																cancelButton}, null);

		final JDialog dialog = optionPane.createDialog(frame, LangModelGeneral.getString("ModuleCode.Title") + " '"
				+ this.title + '\'');
		
		final ActionListener actionListener = new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				optionPane.setValue(okButton);
			}
		};

		this.fieldPassword.addActionListener(actionListener);

		dialog.setModal(true);

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension frameSize = dialog.getSize();
		dialog.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
		
		dialog.show();
		
		while (optionPane.getValue() == okButton) {
			if (new String(this.fieldPassword.getPassword()).equals(this.stb)) {
				this.result = JOptionPane.OK_OPTION;
				break;
			}

			this.fieldPassword.setText("");
			this.fieldPassword.setCaretPosition(0);
			JOptionPane.showMessageDialog(null, LangModelGeneral.getString("ModuleCode.IncorrectCode"),
				LangModelGeneral.getString("ModuleCode.Title") + " '" + this.title + '\'', JOptionPane.ERROR_MESSAGE,
				null);
			dialog.show();
		}

		dialog.dispose();		
	}
}
