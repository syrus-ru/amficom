/**
 * $Id: ViewMapNavigatorCommand.java,v 1.4 2005/01/21 13:49:27 krupenn Exp $
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
import com.syrus.AMFICOM.Client.General.Command.ViewNavigatorCommand;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.Map.Command.MapDesktopCommand;
import com.syrus.AMFICOM.Client.Map.UI.MapSchemeTreeFrame;
import com.syrus.AMFICOM.Client.Map.UI.MapSchemeTreePanel;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.ImageIcon;
import javax.swing.JDesktopPane;

/**
 * отобразить окно привязки схем к карте 
 * 
 * 
 * 
 * @version $Revision: 1.4 $, $Date: 2005/01/21 13:49:27 $
 * @module
 * @author $Author: krupenn $
 * @see
 */
public class ViewMapNavigatorCommand extends ViewNavigatorCommand
{
	public ApplicationContext aContext;
	public JDesktopPane desktop;
	public MapSchemeTreeFrame frame;
	
	public ViewMapNavigatorCommand(JDesktopPane desktop, ApplicationContext aContext)
	{
		super(aContext.getDispatcher(), desktop, "Навигатор объектов");

		this.desktop = desktop;
		this.aContext = aContext;
	}

	public void setParameter(String field, Object value)
	{
		if(field.equals("desktop"))
			setDesktop((JDesktopPane)value);
		else
		if(field.equals("aContext"))
			setApplicationContext((ApplicationContext )value);
		else
			super.setParameter(field, value);
	}

	public void setDesktop(JDesktopPane desktop)
	{
		this.desktop = desktop;
	}

	public void setApplicationContext(ApplicationContext aContext)
	{
		this.aContext = aContext;
	}

	public void execute()
	{
		frame = MapDesktopCommand.findMapSchemeTreeFrame(desktop);

		if(frame == null)
		{
			frame = new MapSchemeTreeFrame();
			MapSchemeTreePanel panel = new MapSchemeTreePanel("", aContext);

			frame.setTitle(title);
			frame.setClosable(true);
			frame.setResizable(true);
			frame.setMaximizable(false);
			frame.setIconifiable(false);
			frame.setFrameIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/general.gif")));
			frame.getContentPane().setLayout(new BorderLayout());
			frame.getContentPane().add(panel, BorderLayout.CENTER);

			desktop.add(frame);

			Dimension dim = desktop.getSize();
			frame.setLocation(dim.width * 4 / 5, dim.height / 2);
			frame.setSize(dim.width / 5, dim.height / 2);
		}

		frame.setVisible(true);
		frame.toFront();
		frame.grabFocus();
		setResult(Command.RESULT_OK);
	}
}
