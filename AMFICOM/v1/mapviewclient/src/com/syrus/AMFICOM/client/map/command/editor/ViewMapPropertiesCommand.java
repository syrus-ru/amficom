/**
 * $Id: ViewMapPropertiesCommand.java,v 1.5 2005/01/21 13:49:27 krupenn Exp $
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
import com.syrus.AMFICOM.Client.Map.Command.MapDesktopCommand;
import com.syrus.AMFICOM.Client.Map.UI.MapPropertyFrame;

import java.awt.Dimension;

import javax.swing.JDesktopPane;

/**
 * Команда отображает окно свойств элемента карты 
 * 
 * 
 * 
 * @version $Revision: 1.5 $, $Date: 2005/01/21 13:49:27 $
 * @module
 * @author $Author: krupenn $
 * @see
 */
public class ViewMapPropertiesCommand extends VoidCommand
{
	ApplicationContext aContext;
	JDesktopPane desktop;
	public MapPropertyFrame frame;

	public ViewMapPropertiesCommand()
	{
	}

	public ViewMapPropertiesCommand(JDesktopPane desktop, ApplicationContext aContext)
	{
		this.desktop = desktop;
		this.aContext = aContext;
	}

	public void execute()
	{
		frame = MapDesktopCommand.findMapPropertyFrame(desktop);

		if(frame == null)
		{
			frame = new MapPropertyFrame("", aContext);

			desktop.add(frame);

			Dimension dim = new Dimension(desktop.getWidth(), desktop.getHeight());
			frame.setLocation(dim.width * 4 / 5, dim.height / 4);
			frame.setSize(dim.width / 5, dim.height / 4);
		}

		frame.setVisible(true);
		setResult(Command.RESULT_OK);
	}

}
