/*-
* $Id: PasswordDialog.java,v 1.1 2005/11/30 13:06:50 bob Exp $
*
* Copyright ¿ 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.manager.UI;

import java.awt.FontMetrics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;

import com.syrus.AMFICOM.client.resource.I18N;

/**
 * @version $Revision: 1.1 $, $Date: 2005/11/30 13:06:50 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module manager
 */
public class PasswordDialog {

	private JPasswordField	confirmPasswordField;
	private JPasswordField	passwordField;
	
	public PasswordDialog() {
		this.initUI();
	}
	
	public static void main(String[] args) {
		new PasswordDialog();
	}
	
	private final void initUI() {
		final String okButton = 
			I18N.getString("Common.Button.OK");
		final String cancelButton = 
			I18N.getString("Common.Button.Cancel");
		
		final JPanel mainPanel = new JPanel(new GridBagLayout());
		final GridBagConstraints gbc = new GridBagConstraints();

		
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		
		this.passwordField = new JPasswordField();
		this.confirmPasswordField = new JPasswordField();
		final FontMetrics fontMetrics = this.passwordField.getFontMetrics(this.passwordField.getFont());

		gbc.gridwidth = GridBagConstraints.RELATIVE;
		gbc.insets = new Insets(fontMetrics.getHeight() / 2, 
				0, 
				fontMetrics.getHeight() / 2,
				0);
		gbc.fill = GridBagConstraints.NONE;
		gbc.weightx = 0.0;
		gbc.anchor = GridBagConstraints.EAST;
		mainPanel.add(new JLabel(I18N.getString("Common.ChangePassword.Password") + ':'), gbc);
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 1.0;
		gbc.anchor = GridBagConstraints.WEST;
		mainPanel.add(this.passwordField, gbc);

		gbc.gridwidth = GridBagConstraints.RELATIVE;
		gbc.fill = GridBagConstraints.NONE;
		gbc.weightx = 0.0;
		gbc.anchor = GridBagConstraints.EAST;
		mainPanel.add(new JLabel(I18N.getString("Common.ChangePassword.ConfirmPassword") + ':'), gbc);
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 1.0;
		gbc.anchor = GridBagConstraints.WEST;
		mainPanel.add(this.confirmPasswordField, gbc);
		
		final JOptionPane optionPane = new JOptionPane(mainPanel, 
			JOptionPane.PLAIN_MESSAGE, 
			JOptionPane.OK_CANCEL_OPTION,
			null, 
			new Object[] { 
				okButton, 
				cancelButton}, 
			null);
		
		boolean correct = false;
		do {
			final JDialog dialog = optionPane.createDialog(null, 
				I18N.getString("Common.ChangePassword.ChangingPassword"));
			dialog.setVisible(true);
			if (optionPane.getValue() == okButton) {
				final String checkPassword = checkPassword();
				correct = checkPassword == null;
				if (checkPassword != null) {
					JOptionPane.showMessageDialog(null, 
						checkPassword, 
						I18N.getString("Error"), 
						JOptionPane.ERROR_MESSAGE);
					this.clearPasswordFields();
				}
			} else {
				correct = true;
			}
		} while (!correct);
	}

	private final String checkPassword() {
		boolean correct;
		char[] password = this.passwordField.getPassword();
		final char[] confirmPassword = this.confirmPasswordField.getPassword();
		final boolean equalsLength = password.length == confirmPassword.length;
		correct = equalsLength;
		if (!correct) {
			return I18N.getString("Common.ChangePassword.PasswordIsNotConfirmed");
		}
		if (correct) {
			final boolean lengthMore3Char = password.length > 3;					
			correct = lengthMore3Char;
			if (!correct) {
				return I18N.getString("Common.ChangePassword.PasswordIsTooShort");
			}
		}
		if (correct) {
			for(int i = 0; i < password.length; i++) {
				if (password[i] != confirmPassword[i]) {
					correct = false;
					break;
				}
			}
			if (!correct) {
				return I18N.getString("Common.ChangePassword.PasswordIsNotConfirmed");
			}
			System.out.println("All correct");
		}
		return null;
	}
	
	protected void applyPassword(final char[] password) {
		// nothing
	}
	
	private final void clearPasswordFields() {
		this.passwordField.setText("");
		this.confirmPasswordField.setText("");
	}
}

