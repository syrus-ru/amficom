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
// * Название: Реализация серверной части интерфейса прототипа РИСД       * //
// *           (включает реализацию пакета pmServer и класса pmRISDImpl)  * //
// * Тип: Java 1.2.2                                                      * //
// *                                                                      * //
// * Автор: Крупенников А.В.                                              * //
// *                                                                      * //
// * Версия: 0.1                                                          * //
// * От: 22 jan 2002                                                      * //
// * Расположение: ISM\prog\java\AMFICOMConfigure\com\syrus\AMFICOM\      * //
// *        Client\Configure\Application\ElementCatalogDialog.java        * //
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

package com.syrus.AMFICOM.Client.General.UI;

import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.UI.ObjectResourceCatalogPanel;
import com.syrus.AMFICOM.Client.General.UI.ObjectResourceDisplayModel;
import com.syrus.AMFICOM.Client.Resource.DataSet;
import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Lang.*;

import java.awt.BorderLayout;
import java.awt.Toolkit;

import javax.swing.ImageIcon;
import javax.swing.JInternalFrame;

public class ObjectResourceCatalogFrame
		extends JInternalFrame
		implements OperationListener
{
	public ObjectResourceCatalogPanel panel;
	public ApplicationContext aContext;

	public ObjectResourceCatalogFrame(String title)
	{
		this(title, new ApplicationContext());
	}

	public ObjectResourceCatalogFrame(String title, ApplicationContext aContext)
	{
		super(title);

		try
		{
			jbInit();
			pack();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		setContext(aContext);
	}

	public void setContext(ApplicationContext aContext)
	{
		this.aContext = aContext;
		panel.setContext(aContext);
		if(aContext != null)
			aContext.getDispatcher().register(this, TreeDataSelectionEvent.type);
	}

	public void setContents(DataSet dataSet)
	{
		panel.setContents(dataSet);
	}

	public void setDisplayModel(ObjectResourceDisplayModel dmod)
	{
		panel.setDisplayModel(dmod);
	}

	public void setObjectResourceClass(Class orclass)
	{
		panel.setObjectResourceClass(orclass);
	}

	public void setActionModel(ObjectResourceCatalogActionModel orcam)
	{
		panel.setActionModel(orcam);
	}

	private void jbInit() throws Exception
	{

		this.setClosable(true);
		this.setIconifiable(true);
		this.setMaximizable(true);
		this.setResizable(true);

		this.setFrameIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/general.gif")));

		getContentPane().setLayout(new BorderLayout());

		panel = new ObjectResourceCatalogPanel(aContext);
		getContentPane().add(panel, BorderLayout.CENTER);
	}

	public void operationPerformed(OperationEvent oe )
	{
		if(oe.getActionCommand().equals(TreeDataSelectionEvent.type))
		{
			TreeDataSelectionEvent tdse = (TreeDataSelectionEvent)oe;

			Class cl = tdse.getDataClass();

			String title;
			try
			{
				java.lang.reflect.Field typField = cl.getField("typ");
				title = (String )typField.get(cl);
			}
			catch(IllegalAccessException iae)
			{
				System.out.println("Ошибка определения значения поля typ - " + iae.getMessage());
				title = "";
			}
			catch(Exception e)
			{
				System.out.println("Не найден объект ObjectResource - " + e.getMessage());
				title = "";
			}

			setTitle(LangModel.getString("node" + title));
		}
	}

	public void doDefaultCloseAction()
	{
		if (isMaximum())
		try
		{
			setMaximum(false);
		}
		catch (java.beans.PropertyVetoException ex)
		{
			ex.printStackTrace();
		}
		super.doDefaultCloseAction();
	 }
}
