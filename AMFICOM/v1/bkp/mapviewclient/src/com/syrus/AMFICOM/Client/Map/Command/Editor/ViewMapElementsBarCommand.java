/**
 * $Id: ViewMapElementsBarCommand.java,v 1.6 2004/12/22 16:38:40 krupenn Exp $
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
import com.syrus.AMFICOM.Client.Map.UI.MapElementsBarFrame;
/**todo*/		
//import com.syrus.AMFICOM.Client.Resource.MapDataSourceImage;

import java.awt.Dimension;

import javax.swing.JDesktopPane;

/**
 * Команда отображает окно компонентов карты (протоэлементов), используемых 
 * для рисования карты
 * 
 * 
 * 
 * @version $Revision: 1.6 $, $Date: 2004/12/22 16:38:40 $
 * @module
 * @author $Author: krupenn $
 * @see
 */
public class ViewMapElementsBarCommand extends VoidCommand
{
	public ApplicationContext aContext;
	public JDesktopPane desktop;
	public MapElementsBarFrame frame;

	public ViewMapElementsBarCommand()
	{
	}

	public ViewMapElementsBarCommand(JDesktopPane desktop, ApplicationContext aContext)
	{
		this.desktop = desktop;
		this.aContext = aContext;
	}

	public void execute()
	{
		if(aContext.getDataSource() == null)
			return;

/**todo*/		
//		new MapDataSourceImage(aContext.getDataSource()).loadProtoElements();
			
		frame = null;
		for(int i = 0; i < desktop.getComponents().length; i++)
		{
			try
			{
				MapElementsBarFrame comp = (MapElementsBarFrame)desktop.getComponent(i);
				// уже есть окно карты
				frame = comp;
				break;
			}
			catch(Exception ex)
			{
				// не окно карты
			}
		}

		if(frame == null)
		{
			frame = new MapElementsBarFrame(aContext);

			desktop.add(frame, JDesktopPane.PALETTE_LAYER);

			Dimension dim = new Dimension(desktop.getWidth(), desktop.getHeight());
			frame.setLocation(0, dim.height * 7 / 8);
			frame.setSize(dim.width * 4 / 5, dim.height / 8);
		}

		frame.setVisible(true);
		setResult(Command.RESULT_OK);
	}

}
