/**
 * $Id: ViewGeneralPropertiesCommand.java,v 1.2 2005/05/27 15:14:55 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 */

package com.syrus.AMFICOM.Client.Map.Command.Editor;

import java.awt.Dimension;

import javax.swing.JDesktopPane;

import com.syrus.AMFICOM.Client.Map.Command.MapDesktopCommand;
import com.syrus.AMFICOM.Client.Map.UI.MapGeneralPropertiesFrame;
import com.syrus.AMFICOM.client.model.AbstractCommand;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.model.Command;

/**
 * Команда отображает окно свойств элемента карты 
 * @author $Author: krupenn $
 * @version $Revision: 1.2 $, $Date: 2005/05/27 15:14:55 $
 * @module mapviewclient_v1
 */
public class ViewGeneralPropertiesCommand extends AbstractCommand {
	ApplicationContext aContext;

	JDesktopPane desktop;

	public MapGeneralPropertiesFrame frame;

	public ViewGeneralPropertiesCommand(
			JDesktopPane desktop,
			ApplicationContext aContext) {
		this.desktop = desktop;
		this.aContext = aContext;
	}

	public void execute() {
		this.frame = MapDesktopCommand
				.findMapGeneralPropertiesFrame(this.desktop);

		if(this.frame == null) {
			this.frame = new MapGeneralPropertiesFrame("", this.aContext);
			this.frame.setClosable(true);
			this.frame.setResizable(true);
			this.frame.setMaximizable(false);
			this.frame.setIconifiable(false);

			this.desktop.add(this.frame);

			Dimension dim = this.desktop.getSize();
			this.frame.setLocation(dim.width * 4 / 5, dim.height / 4);
			this.frame.setSize(dim.width / 5, dim.height / 4);
		}

		this.frame.setVisible(true);
		setResult(Command.RESULT_OK);
	}

}
