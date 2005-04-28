/**
 * $Id: ViewCharacteristicsCommand.java,v 1.2 2005/04/28 13:13:24 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Map.Command.Editor;

import java.awt.Dimension;

import javax.swing.JDesktopPane;

import com.syrus.AMFICOM.Client.General.Command.Command;
import com.syrus.AMFICOM.Client.General.Command.VoidCommand;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.Map.Command.MapDesktopCommand;
import com.syrus.AMFICOM.Client.Map.UI.MapCharacteristicPropertiesFrame;

/**
 * отобразить окно привязки схем к карте 
 * @author $Author: krupenn $
 * @version $Revision: 1.2 $, $Date: 2005/04/28 13:13:24 $
 * @module mapviewclient_v1
 */
public class ViewCharacteristicsCommand extends VoidCommand
{
	ApplicationContext aContext;
	JDesktopPane desktop;
	public MapCharacteristicPropertiesFrame frame;
	
	public ViewCharacteristicsCommand(JDesktopPane desktop, ApplicationContext aContext)
	{
		this.desktop = desktop;
		this.aContext = aContext;
	}

	public void execute()
	{
		this.frame = MapDesktopCommand.findMapCharacteristicsFrame(this.desktop);

		if(this.frame == null)
		{
			this.frame = new MapCharacteristicPropertiesFrame("", this.aContext);
			this.frame.setClosable(true);
			this.frame.setResizable(true);
			this.frame.setMaximizable(false);
			this.frame.setIconifiable(false);

			this.desktop.add(this.frame);

			Dimension dim = this.desktop.getSize();
			this.frame.setLocation(dim.width * 4 / 5, dim.height / 2);
			this.frame.setSize(dim.width / 5, dim.height / 2);
		}

		this.frame.setVisible(true);
		setResult(Command.RESULT_OK);
	}
}
