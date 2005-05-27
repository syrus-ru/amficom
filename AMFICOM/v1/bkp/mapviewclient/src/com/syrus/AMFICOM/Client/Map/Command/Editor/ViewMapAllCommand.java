/**
 * $Id: ViewMapAllCommand.java,v 1.12 2005/05/27 15:14:55 krupenn Exp $ Syrus
 * Systems Научно-технический центр Проект: АМФИКОМ Автоматизированный
 * МногоФункциональный Интеллектуальный Комплекс Объектного Мониторинга
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Map.Command.Editor;

import java.beans.PropertyChangeEvent;

import com.syrus.AMFICOM.Client.General.Model.MapApplicationModelFactory;
import com.syrus.AMFICOM.Client.Map.Editor.MapEditorWindowArranger;
import com.syrus.AMFICOM.client.model.AbstractCommand;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.model.Command;

import javax.swing.JDesktopPane;

/**
 * отобразить стандартный набор окон модуля "Редактор топологических схем"
 * 
 * @author $Author: krupenn $
 * @version $Revision: 1.12 $, $Date: 2005/05/27 15:14:55 $
 * @module mapviewclient_v1
 */
public class ViewMapAllCommand extends AbstractCommand {
	ApplicationContext aContext;

	JDesktopPane desktop;

	MapApplicationModelFactory factory;

	public ViewMapAllCommand(
			JDesktopPane desktop,
			ApplicationContext aContext,
			MapApplicationModelFactory factory) {
		this.desktop = desktop;
		this.aContext = aContext;
		this.factory = factory;
	}

	public void execute() {
		new ViewMapViewNavigatorCommand(this.desktop, this.aContext).execute();
		new ViewMapControlsCommand(this.desktop, this.aContext).execute();
		new ViewGeneralPropertiesCommand(this.desktop, this.aContext).execute();
		new ViewAdditionalPropertiesCommand(this.desktop, this.aContext).execute();
		new ViewCharacteristicsCommand(this.desktop, this.aContext).execute();
		new ViewMapWindowCommand(this.desktop, this.aContext, this.factory).execute();

		this.aContext.getDispatcher().firePropertyChange(
				new PropertyChangeEvent(
						this.desktop,
						MapEditorWindowArranger.EVENT_ARRANGE,
						null,
						null));

		setResult(Command.RESULT_OK);
	}
}
