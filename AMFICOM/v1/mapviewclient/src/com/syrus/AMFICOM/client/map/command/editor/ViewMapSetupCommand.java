/**
 * $Id: ViewMapSetupCommand.java,v 1.3 2004/10/19 14:10:03 krupenn Exp $
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
import com.syrus.AMFICOM.Client.Map.Setup.ControlsFrame;

import java.awt.Dimension;

import javax.swing.JDesktopPane;

/**
 * Команда отображает окно управления слоями и поиска 
 * 
 * 
 * 
 * @version $Revision: 1.3 $, $Date: 2004/10/19 14:10:03 $
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
		frame = null;
		for(int i = 0; i < desktop.getComponents().length; i++)
		{
			try
			{
				ControlsFrame comp = (ControlsFrame)desktop.getComponent(i);
				// уже есть окно карты
				frame = comp;
				break;
			}
			catch(Exception ex)
			{
			}
		}

		if(frame == null)
		{
			frame = new ControlsFrame(null, aContext);

			desktop.add(frame);

			Dimension dim = new Dimension(desktop.getWidth(), desktop.getHeight());
			frame.setLocation(dim.width * 4 / 5, dim.height / 2);
			frame.setSize(dim.width / 5, dim.height / 2);
		}

		frame.setVisible(true);
		setResult(Command.RESULT_OK);
	}

}
