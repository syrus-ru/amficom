package com.syrus.AMFICOM.Client.General.UI;

import java.awt.BorderLayout;
import java.awt.event.*;
import javax.swing.*;
import oracle.jdeveloper.layout.*;

public class ModuleCodeDialog extends JDialog
{
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
		title = module_title;
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
		return retCode;
	}

	private void jbInit() throws Exception
	{
		this.setTitle("Введите код модуля '" + title + "'");

		this.setResizable(false);
		jPanel1.setLayout(xYLayout1);
		jPanel1.addKeyListener(new ModuleCodeDialog_jPanel1_keyAdapter(this));
		xYLayout1.setWidth(370);
		xYLayout1.setHeight(75);
		jLabel2.setText("Код модуля");
		fieldPassword.addKeyListener(new ModuleCodeDialog_fieldPassword_keyAdapter(this));
		buttonOk.setText("Открыть модуль");
		buttonOk.addActionListener(new ModuleCodeDialog_buttonOk_actionAdapter(this));
		buttonCancel.setText("Отменить");
		buttonCancel.addActionListener(new ModuleCodeDialog_buttonCancel_actionAdapter(this));
		jPanel1.add(jLabel2, new XYConstraints(10, 13, -1, -1));
		jPanel1.add(fieldPassword, new XYConstraints(90, 10, 270, 24));
		jPanel1.add(buttonOk, new XYConstraints(10, 40, 140, 27));
		jPanel1.add(buttonCancel, new XYConstraints(260, 40, 100, 27));
		this.getContentPane().add(jPanel1, BorderLayout.CENTER);
	}

	void buttonOk_actionPerformed(ActionEvent e)
	{
		String sta = fieldPassword.getText();

		if(sta.equals(stb))
		{
			retCode = RET_OK;
			dispose();
			return;
		}
		fieldPassword.setText("");
		JOptionPane.showMessageDialog(
				this,
				"Неправильный код",
				"Код модуля '" + title + "'",
				JOptionPane.ERROR_MESSAGE,
				null);
		return;
	}

	void buttonCancel_actionPerformed(ActionEvent e)
	{
		retCode = RET_CANCEL;
		dispose();
	}

	void fieldPassword_keyPressed(KeyEvent e)
	{
		if (e.getKeyCode() == KeyEvent.VK_ENTER)
			buttonOk.doClick();
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
			buttonCancel.doClick();
	}

	void jPanel1_keyPressed(KeyEvent e)
	{
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
			buttonCancel.doClick();
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
		adaptee.buttonOk_actionPerformed(e);
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
		adaptee.buttonCancel_actionPerformed(e);
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
		adaptee.jPanel1_keyPressed(e);
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
		adaptee.fieldPassword_keyPressed(e);
	}
}
