/**
 * $Id: ViewAdditionalPropertiesCommand.java,v 1.4 2005/06/21 11:29:04 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 */

package com.syrus.AMFICOM.client.map.command.editor;

import java.awt.Dimension;

import javax.swing.JDesktopPane;

import com.syrus.AMFICOM.client.map.command.MapDesktopCommand;
import com.syrus.AMFICOM.client.map.ui.MapAdditionalPropertiesFrame;
import com.syrus.AMFICOM.client.model.AbstractCommand;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.client.resource.LangModelMap;

/**
 * Команда отображает окно свойств элемента карты 
 * @author $Author: krupenn $
 * @version $Revision: 1.4 $, $Date: 2005/06/21 11:29:04 $
 * @module mapviewclient_v1
 */
public class ViewAdditionalPropertiesCommand extends AbstractCommand {
	ApplicationContext aContext;

	JDesktopPane desktop;

	public MapAdditionalPropertiesFrame frame;

	public ViewAdditionalPropertiesCommand(
			JDesktopPane desktop,
			ApplicationContext aContext) {
		this.desktop = desktop;
		this.aContext = aContext;
	}

	public void execute() {
		this.frame = MapDesktopCommand
				.findMapAdditionalPropertiesFrame(this.desktop);

		if(this.frame == null) {
			this.frame = new MapAdditionalPropertiesFrame(LangModelMap.getString("AdditionalProperties"), this.aContext);
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
