/**
 * $Id: ViewMapPropertiesCommand.java,v 1.6 2005/02/08 15:11:10 krupenn Exp $
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
 * @author $Author: krupenn $
 * @version $Revision: 1.6 $, $Date: 2005/02/08 15:11:10 $
 * @module mapviewclient_v1
 */
public class ViewMapPropertiesCommand extends VoidCommand
{
	ApplicationContext aContext;
	JDesktopPane desktop;
	public MapPropertyFrame frame;

	public ViewMapPropertiesCommand(JDesktopPane desktop, ApplicationContext aContext)
	{
		this.desktop = desktop;
		this.aContext = aContext;
	}

	public void execute()
	{
		this.frame = MapDesktopCommand.findMapPropertyFrame(this.desktop);

		if(this.frame == null)
		{
			this.frame = new MapPropertyFrame("", this.aContext);

			this.desktop.add(this.frame);

			Dimension dim = new Dimension(this.desktop.getWidth(), this.desktop.getHeight());
			this.frame.setLocation(dim.width * 4 / 5, dim.height / 4);
			this.frame.setSize(dim.width / 5, dim.height / 4);
		}

		this.frame.setVisible(true);
		setResult(Command.RESULT_OK);
	}

}
