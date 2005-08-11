/**
 * $Id: ViewMapControlsCommand.java,v 1.6 2005/08/11 12:43:30 arseniy Exp $
 * Syrus Systems Научно-технический центр Проект: АМФИКОМ Автоматизированный
 * МногоФункциональный Интеллектуальный Комплекс Объектного Мониторинга
 */

package com.syrus.AMFICOM.client.map.command.editor;

import java.beans.PropertyChangeEvent;

import javax.swing.JDesktopPane;

import com.syrus.AMFICOM.client.map.command.MapDesktopCommand;
import com.syrus.AMFICOM.client.map.editor.MapEditorWindowArranger;
import com.syrus.AMFICOM.client.map.operations.ControlsFrame;
import com.syrus.AMFICOM.client.model.AbstractCommand;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.model.Command;

/**
 * Команда отображает окно управления слоями и поиска
 * 
 * @author $Author: arseniy $
 * @version $Revision: 1.6 $, $Date: 2005/08/11 12:43:30 $
 * @module mapviewclient
 */
public class ViewMapControlsCommand extends AbstractCommand {
	ApplicationContext aContext;

	JDesktopPane desktop;

	public ControlsFrame frame;

	public ViewMapControlsCommand(
			JDesktopPane desktop,
			ApplicationContext aContext) {
		this.desktop = desktop;
		this.aContext = aContext;
	}

	public void execute() {
		this.frame = MapDesktopCommand.findControlsFrame(this.desktop);

		if(this.frame == null) {
			this.frame = new ControlsFrame(MapDesktopCommand.findMapFrame(this.desktop), this.aContext);

			this.desktop.add(this.frame);

			this.aContext.getDispatcher().firePropertyChange(
					new PropertyChangeEvent(
							this.desktop,
							MapEditorWindowArranger.EVENT_ARRANGE,
							null,
							null));
		}

		this.frame.setVisible(true);
		this.frame.setMapFrame(MapDesktopCommand.findMapFrame(this.desktop));
		setResult(Command.RESULT_OK);
	}

}
