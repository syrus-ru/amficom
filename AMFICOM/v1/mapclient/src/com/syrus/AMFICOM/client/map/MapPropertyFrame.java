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

package com.syrus.AMFICOM.Client.Map;

import com.syrus.AMFICOM.Client.General.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.tree.*;
import javax.swing.event.*;
import java.util.*;
import oracle.jdeveloper.layout.*;

import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.General.UI.*;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Lang.*;

public class MapPropertyFrame extends ObjectResourcePropertyFrame
		implements OperationListener
{
	MapMainFrame mmf = null;

	public MapPropertyFrame(String title)
	{
		super(title);
		jbInit();
	}

	public MapPropertyFrame(String title, ApplicationContext aContext)
	{
		super(title, aContext);
		jbInit();
	}

	public void jbInit()
	{
		this.setIconifiable(true);
		this.setFrameIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/general.gif")));
		this.setTitle(LangModelMap.getString("propertiesTitle"));
	}

	public void setContext(ApplicationContext aContext)
	{
		super.setContext(aContext);
		if(aContext == null)
			return;
		Dispatcher disp = aContext.getDispatcher();
		if(disp == null)
			return;
		disp.register(this, MapNavigateEvent.type);
		disp.register(this, "mapdeselectevent");
	}

	public void initialize()
	{
		super.initialize();
		panel.setTableModel(new MapPropertyTableModel(aContext));
	}

	public void setObjectResource(ObjectResource or)
	{
		panel.setSelected(or);
	}

	public void operationPerformed(OperationEvent oe )
	{
		super.operationPerformed(oe);
		if(oe.getActionCommand().equals("mapdeselectevent"))
		{
			panel.setSelected(null);
		}
		if(oe.getActionCommand().equals(MapNavigateEvent.type))
		{
			MapNavigateEvent mne = (MapNavigateEvent )oe;
			if(mne.MAP_ELEMENT_SELECTED)
			{
				ObjectResource me = (ObjectResource )mne.getSource();
//				System.out.println("PROPPANEL: Selecting " + me.getId());
				panel.setSelected(me);
//				System.out.println("PROPPANEL: " + me.getId() + " selected ok");
			}
		}
	}

}

class MapPropertyTableModel extends ObjectResourcePropertyTableModel
{
	ApplicationContext aContext;
	
	public MapPropertyTableModel(ApplicationContext aContext)
	{
		super(new String[] {"Свойство", "Значение"}, null);
		this.aContext = aContext;
	}

	public boolean isCellEditable(int p_row, int p_col)
	{
		
		if(!aContext.getApplicationModel().isEnabled("mapActionEditProperties"))
			return false;

		return super.isCellEditable(p_row, p_col);
	}
}