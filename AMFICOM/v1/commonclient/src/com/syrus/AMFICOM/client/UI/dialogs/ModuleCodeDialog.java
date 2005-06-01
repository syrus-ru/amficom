package com.syrus.AMFICOM.client.UI.dialogs;

import java.awt.BorderLayout;
import java.awt.event.*;
import javax.swing.*;
import oracle.jdeveloper.layout.*;

/**
 * TODO - rebuild !
 * @version $Revision: 1.2 $, $Date: 2005/06/01 16:55:11 $
 * @author $Author: arseniy $
 * @module commonclient_v1
 */
public class ModuleCodeDialog extends JDialog
{
	private static final long serialVersionUID = 4049639009691120440L;

	private JButton buttonCancel = new JButton();
	private JButton buttonOk = new JButton();
	private JLabel jLabel2 = new JLabel();
	private JPanel jPanel1 = new JPanel();
	private JPasswordField fieldPassword = new JPasswordField();
	private XYLayout xYLayout1 = new XYLayout();

	private int retCode = 0;

	public static final int RET_OK = 1;
	public static final int RET_CANCEL = 2;

	private String stb;
	private String title;

	public ModuleCodeDialog(String stb, String module_title)
	{
		this.stb = stb;
		this.title = module_title;
		try
		{
			jbInit();
			pack();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	public int getRetCode() {
		return this.retCode;
	}

	private void jbInit() throws Exception
	{
		this.setTitle("Введите код модуля '" + this.title + "'");

		this.setResizable(false);
		this.jPanel1.setLayout(this.xYLayout1);
		this.jPanel1.addKeyListener(new ModuleCodeDialog_jPanel1_keyAdapter(this));
		this.xYLayout1.setWidth(370);
		this.xYLayout1.setHeight(75);
		this.jLabel2.setText("Код модуля");
		this.fieldPassword.addKeyListener(new ModuleCodeDialog_fieldPassword_keyAdapter(this));
		this.buttonOk.setText("Открыть модуль");
		this.buttonOk.addActionListener(new ModuleCodeDialog_buttonOk_actionAdapter(this));
		this.buttonCancel.setText("Отменить");
		this.buttonCancel.addActionListener(new ModuleCodeDialog_buttonCancel_actionAdapter(this));
		this.jPanel1.add(this.jLabel2, new XYConstraints(10, 13, -1, -1));
		this.jPanel1.add(this.fieldPassword, new XYConstraints(90, 10, 270, 24));
		this.jPanel1.add(this.buttonOk, new XYConstraints(10, 40, 140, 27));
		this.jPanel1.add(this.buttonCancel, new XYConstraints(260, 40, 100, 27));
		this.getContentPane().add(this.jPanel1, BorderLayout.CENTER);
	}

	void buttonOk_actionPerformed(ActionEvent e)
	{
		String sta = new String(this.fieldPassword.getPassword());

		if(sta.equals(this.stb))
		{
			this.retCode = RET_OK;
			dispose();
			return;
		}
		this.fieldPassword.setText("");
		JOptionPane.showMessageDialog(
				this,
				"Неправильный код",
				"Код модуля '" + this.title + "'",
				JOptionPane.ERROR_MESSAGE,
				null);
		return;
	}

	void buttonCancel_actionPerformed(ActionEvent e)
	{
		this.retCode = RET_CANCEL;
		dispose();
	}

	void fieldPassword_keyPressed(KeyEvent e)
	{
		if (e.getKeyCode() == KeyEvent.VK_ENTER)
			this.buttonOk.doClick();
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
			this.buttonCancel.doClick();
	}

	void jPanel1_keyPressed(KeyEvent e)
	{
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
			this.buttonCancel.doClick();
	}

}

class ModuleCodeDialog_buttonOk_actionAdapter implements ActionListener
{
	ModuleCodeDialog adaptee;

	ModuleCodeDialog_buttonOk_actionAdapter(ModuleCodeDialog adaptee)
	{
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e)
	{
		this.adaptee.buttonOk_actionPerformed(e);
	}
}
class ModuleCodeDialog_buttonCancel_actionAdapter implements ActionListener
{
	ModuleCodeDialog adaptee;

	ModuleCodeDialog_buttonCancel_actionAdapter(ModuleCodeDialog adaptee)
	{
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e)
	{
		this.adaptee.buttonCancel_actionPerformed(e);
	}
}

class ModuleCodeDialog_jPanel1_keyAdapter extends KeyAdapter
{
	ModuleCodeDialog adaptee;

	ModuleCodeDialog_jPanel1_keyAdapter(ModuleCodeDialog adaptee)
	{
		this.adaptee = adaptee;
	}

	public void keyPressed(KeyEvent e)
	{
		this.adaptee.jPanel1_keyPressed(e);
	}
}

class ModuleCodeDialog_fieldPassword_keyAdapter extends KeyAdapter
{
	ModuleCodeDialog adaptee;

	ModuleCodeDialog_fieldPassword_keyAdapter(ModuleCodeDialog adaptee)
	{
		this.adaptee = adaptee;
	}

	public void keyPressed(KeyEvent e)
	{
		this.adaptee.fieldPassword_keyPressed(e);
	}
}
