//////////////////////////////////////////////////////////////////////////////
// *                                                                      * //
// * Syrus Systems                                                        * //
// * Департамент Системных Исследований и Разработок                      * //
// *                                                                      * //
// * Проект: АМФИКОМ - система Автоматизированного Многофункционального   * //
// *         Интеллектуального Контроля и Объектного Мониторинга          * //
// *                                                                      * //
// *         реализация Интегрированной Системы Мониторинга               * //
// *                                                                      * //
// * Название: Диалоговое окно открытия новой сессии работы пользователя  * //
// *         с системой                                                   * //
// *                                                                      * //
// * Тип: Java 1.2.2                                                      * //
// *                                                                      * //
// * Автор: Крупенников А.В.                                              * //
// *                                                                      * //
// * Версия: 1.0                                                          * //
// * От: 1 jul 2002                                                       * //
// * Расположение: ISM\prog\java\AMFICOMConfigure\com\syrus\AMFICOM\      * //
// *        Client\Main\SessionDomainDialog.java                            * //
// *                                                                      * //
// * Среда разработки: Oracle JDeveloper 3.2.2 (Build 915)                * //
// *                                                                      * //
// * Компилятор: Oracle javac (Java 2 SDK, Standard Edition, ver 1.2.2)   * //
// *                                                                      * //
// * Статус: разработка                                                   * //
// *                                                                      * //
// * Изменения:                                                           * //
// *  Кем         Верс   Когда      Комментарии                           * //
// * -----------  ----- ---------- -------------------------------------- * //
// *                                                                      * //
// * Описание:                                                            * //
// *                                                                      * //
//////////////////////////////////////////////////////////////////////////////

package com.syrus.AMFICOM.Client.General.UI.Session;

import com.syrus.AMFICOM.Client.General.Checker;
import com.syrus.AMFICOM.Client.General.Lang.LangModel;
import com.syrus.AMFICOM.Client.General.UI.ObjectResourceComboBox;
import com.syrus.AMFICOM.Client.Resource.Object.Domain;
import com.syrus.AMFICOM.Client.General.Model.Environment;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import oracle.jdeveloper.layout.XYConstraints;
import oracle.jdeveloper.layout.XYLayout;

public class SessionDomainDialog extends JDialog
{
	JPanel jPanel1 = new JPanel();
	XYLayout xYLayout1 = new XYLayout();
	JLabel jLabel3 = new JLabel();

	ObjectResourceComboBox domainCB = new ObjectResourceComboBox("domain");

	JButton buttonOk = new JButton();
	JButton buttonHelp = new JButton();
	JButton buttonCancel = new JButton();

	public int retCode = 0;
	public final int RET_OK = 1;
	public final int RET_CANCEL = 2;

	public String domain_id = "";
	public String user_id = "";

	protected SessionDomainDialog(Frame parent, String title, boolean modal)
	{
		super(parent, title, modal);
		try
		{
			jbInit();
			pack();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public SessionDomainDialog(String user_id)
	{
		this(Environment.getActiveWindow(), LangModel.String("SessionDomainTitle"), false);
		this.user_id = user_id;
	}

	private void jbInit() throws Exception
	{
		jPanel1.setLayout(xYLayout1);
		xYLayout1.setWidth(400);
		xYLayout1.setHeight(80);

		this.setResizable(false);

		jLabel3.setText(LangModel.String("labelDomain"));

		buttonOk.setText(LangModel.String("buttonSelect"));
		buttonOk.addActionListener(
				new SessionDomainDialog_buttonOk_actionAdapter(this));
		buttonHelp.setText(LangModel.String("buttonHelp"));
		buttonHelp.addActionListener(
				new SessionDomainDialog_buttonHelp_actionAdapter(this));
		buttonCancel.setText(LangModel.String("buttonCancel"));
		buttonCancel.addActionListener(
				new SessionDomainDialog_buttonCancel_actionAdapter(this));

		getContentPane().add(jPanel1);

		jPanel1.add(jLabel3, new XYConstraints(10, 10, 120, 20));
		jPanel1.add(domainCB, new XYConstraints(130, 10, 260, 20));

		jPanel1.add(buttonOk, new XYConstraints(10, 35, 120, 27));
		jPanel1.add(buttonCancel, new XYConstraints(140, 35, 120, 27));
		jPanel1.add(buttonHelp, new XYConstraints(270, 35, 120, 27));

	}

	public void setDomain(String domain_id)
	{
		domainCB.setSelected(domain_id);
	}

	void buttonOk_actionPerformed(ActionEvent e)
	{
		try 
        {
            domain_id = (String )domainCB.getSelected();
			if(!Checker.checkObject(user_id, Domain.typ, domain_id, Checker.read))
				return;
			retCode = RET_OK;
        } 
		catch (Exception ex) 
        {
			retCode = RET_CANCEL;
            ex.printStackTrace();
        }
		dispose();
	}

	void buttonCancel_actionPerformed(ActionEvent e)
	{
		retCode = RET_CANCEL;
		dispose();
	}

	void buttonHelp_actionPerformed(ActionEvent e)
	{
		JOptionPane.showMessageDialog(
				this,
				"information title",
				"information text",
				JOptionPane.INFORMATION_MESSAGE,
				null);
	}


}

class SessionDomainDialog_buttonOk_actionAdapter
		implements java.awt.event.ActionListener{
	SessionDomainDialog adaptee;

	SessionDomainDialog_buttonOk_actionAdapter(SessionDomainDialog adaptee)
	{
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e)
	{
		adaptee.buttonOk_actionPerformed(e);
	}
}

class SessionDomainDialog_buttonCancel_actionAdapter
		implements java.awt.event.ActionListener{
	SessionDomainDialog adaptee;

	SessionDomainDialog_buttonCancel_actionAdapter(SessionDomainDialog adaptee)
	{
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e)
	{
		adaptee.buttonCancel_actionPerformed(e);
	}
}

class SessionDomainDialog_buttonHelp_actionAdapter
		implements java.awt.event.ActionListener{
	SessionDomainDialog adaptee;

	SessionDomainDialog_buttonHelp_actionAdapter(SessionDomainDialog adaptee)
	{
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e)
	{
		adaptee.buttonHelp_actionPerformed(e);
	}
}
