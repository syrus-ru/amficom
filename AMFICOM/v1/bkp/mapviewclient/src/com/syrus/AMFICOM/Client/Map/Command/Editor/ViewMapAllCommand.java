/**
 * $Id: ViewMapAllCommand.java,v 1.2 2004/10/19 10:41:03 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Map.Command.Editor;

import com.syrus.AMFICOM.Client.General.Command.Command;
import com.syrus.AMFICOM.Client.General.Command.VoidCommand;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.Model.ApplicationModelFactory;

import javax.swing.JDesktopPane;

/**
 * отобразить стандартный набор окон модуля "Редактор топологических схем"
 * 
 * 
 * 
 * 
 * @version $Revision: 1.2 $, $Date: 2004/10/19 10:41:03 $
 * @module
 * @author $Author: krupenn $
 * @see
 */
public class ViewMapAllCommand extends VoidCommand
{
	ApplicationContext aContext;
	JDesktopPane desktop;
	ApplicationModelFactory factory;

	public ViewMapAllCommand()
	{
	}

	public ViewMapAllCommand(JDesktopPane desktop, ApplicationContext aContext, ApplicationModelFactory factory)
	{
		this.desktop = desktop;
		this.aContext = aContext;
		this.factory = factory;
	}

	public Object clone()
	{
		return new ViewMapAllCommand(desktop, aContext, factory);
	}

	public void execute()
	{
		new ViewMapPropertiesCommand(desktop, aContext).execute();
		new ViewMapElementsCommand(desktop, aContext).execute();
		new ViewMapElementsBarCommand(desktop, aContext).execute();
		new ViewMapNavigatorCommand(desktop, aContext).execute();
		new ViewMapWindowCommand(aContext.getDispatcher(), desktop, aContext, factory).execute();
		setResult(Command.RESULT_OK);
	}
}
