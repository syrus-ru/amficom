/**
 * $Id: ViewMapControlsCommand.java,v 1.1 2005/04/28 13:10:59 krupenn Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: ������� ������������������ �������������������
 *         ���������������� �������� ���������� �����������
 *
 * ���������: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Map.Command.Editor;

import com.syrus.AMFICOM.Client.General.Command.Command;
import com.syrus.AMFICOM.Client.General.Command.VoidCommand;
import com.syrus.AMFICOM.Client.General.Event.OperationEvent;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.Map.Command.MapDesktopCommand;
import com.syrus.AMFICOM.Client.Map.Editor.MapEditorWindowArranger;
import com.syrus.AMFICOM.Client.Map.Operations.ControlsFrame;

import javax.swing.JDesktopPane;

/**
 * ������� ���������� ���� ���������� ������ � ������ 
 * @author $Author: krupenn $
 * @version $Revision: 1.1 $, $Date: 2005/04/28 13:10:59 $
 * @module mapviewclient_v1
 */
public class ViewMapControlsCommand extends VoidCommand
{
	ApplicationContext aContext;
	JDesktopPane desktop;
	public ControlsFrame frame;

	public ViewMapControlsCommand(JDesktopPane desktop, ApplicationContext aContext)
	{
		this.desktop = desktop;
		this.aContext = aContext;
	}

	public void execute()
	{
		this.frame = MapDesktopCommand.findControlsFrame(this.desktop);

		if(this.frame == null)
		{
			this.frame = new ControlsFrame(null, this.aContext);

			this.desktop.add(this.frame);
			
			this.aContext.getDispatcher().notify(
					new OperationEvent(this.desktop,0,MapEditorWindowArranger.EVENT_ARRANGE));
		}

		this.frame.setVisible(true);
		this.frame.setMapFrame(MapDesktopCommand.findMapFrame(this.desktop));
		setResult(Command.RESULT_OK);
	}

}
