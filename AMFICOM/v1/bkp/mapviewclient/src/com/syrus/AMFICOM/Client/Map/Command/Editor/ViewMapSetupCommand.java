/**
 * $Id: ViewMapSetupCommand.java,v 1.4 2005/01/21 13:49:27 krupenn Exp $
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
import com.syrus.AMFICOM.Client.Map.Setup.ControlsFrame;

import java.awt.Dimension;

import javax.swing.JDesktopPane;

/**
 * Команда отображает окно управления слоями и поиска 
 * 
 * 
 * 
 * @version $Revision: 1.4 $, $Date: 2005/01/21 13:49:27 $
 * @module
 * @author $Author: krupenn $
 * @see
 */
public class ViewMapSetupCommand extends VoidCommand
{
	ApplicationContext aContext;
	JDesktopPane desktop;
	public ControlsFrame frame;

	public ViewMapSetupCommand()
	{
	}

	public ViewMapSetupCommand(JDesktopPane desktop, ApplicationContext aContext)
	{
		this.desktop = desktop;
		this.aContext = aContext;
	}

	public void execute()
	{
		frame = MapDesktopCommand.findControlsFrame(desktop);

		if(frame == null)
		{
			frame = new ControlsFrame(null, aContext);

			desktop.add(frame);

			Dimension dim = new Dimension(desktop.getWidth(), desktop.getHeight());
			frame.setLocation(dim.width * 4 / 5, dim.height / 2);
			frame.setSize(dim.width / 5, dim.height / 2);
		}

		frame.setVisible(true);
		frame.setMapFrame(MapDesktopCommand.findMapFrame(desktop));
		setResult(Command.RESULT_OK);
	}

}
