/**
 * $Id: ViewMapAllCommand.java,v 1.4 2005/02/08 15:11:10 krupenn Exp $
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
 * @author $Author: krupenn $
 * @version $Revision: 1.4 $, $Date: 2005/02/08 15:11:10 $
 * @module mapviewclient_v1
 */
public class ViewMapAllCommand extends VoidCommand
{
	ApplicationContext aContext;
	JDesktopPane desktop;
	ApplicationModelFactory factory;

	public ViewMapAllCommand(JDesktopPane desktop, ApplicationContext aContext, ApplicationModelFactory factory)
	{
		this.desktop = desktop;
		this.aContext = aContext;
		this.factory = factory;
	}

	public void execute()
	{
		new ViewMapPropertiesCommand(this.desktop, this.aContext).execute();
		new ViewMapElementsCommand(this.desktop, this.aContext).execute();
		new ViewMapElementsBarCommand(this.desktop, this.aContext).execute();
		new ViewMapNavigatorCommand(this.desktop, this.aContext).execute();
		new ViewMapWindowCommand(this.aContext.getDispatcher(), this.desktop, this.aContext, this.factory).execute();
		setResult(Command.RESULT_OK);
	}
}
