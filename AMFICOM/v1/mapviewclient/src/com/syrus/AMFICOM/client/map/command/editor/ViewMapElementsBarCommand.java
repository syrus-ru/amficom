/**
 * $Id: ViewMapElementsBarCommand.java,v 1.9 2005/02/08 15:11:10 krupenn Exp $
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
import com.syrus.AMFICOM.Client.Map.UI.MapElementsBarFrame;

import java.awt.Dimension;

import javax.swing.JDesktopPane;
import javax.swing.JLayeredPane;

/**
 * Команда отображает окно компонентов карты (протоэлементов), используемых 
 * для рисования карты
 * @author $Author: krupenn $
 * @version $Revision: 1.9 $, $Date: 2005/02/08 15:11:10 $
 * @module mapviewclient_v1
 */
public class ViewMapElementsBarCommand extends VoidCommand
{
	public ApplicationContext aContext;
	public JDesktopPane desktop;
	public MapElementsBarFrame frame;

	public ViewMapElementsBarCommand(JDesktopPane desktop, ApplicationContext aContext)
	{
		this.desktop = desktop;
		this.aContext = aContext;
	}

	public void execute()
	{
		if(this.aContext.getDataSource() == null)
			return;

/**todo*/		
//		new MapDataSourceImage(aContext.getDataSource()).loadProtoElements();
			
		this.frame = MapDesktopCommand.findMapElementsBarFrame(this.desktop);

		if(this.frame == null)
		{
			this.frame = new MapElementsBarFrame(this.aContext);

			this.desktop.add(this.frame, JLayeredPane.PALETTE_LAYER);

			Dimension dim = new Dimension(this.desktop.getWidth(), this.desktop.getHeight());
			this.frame.setLocation(0, dim.height * 7 / 8);
			this.frame.setSize(dim.width * 4 / 5, dim.height / 8);
		}

		this.frame.setVisible(true);
		setResult(Command.RESULT_OK);
	}

}
