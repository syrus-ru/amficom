/**
 * $Id: MapEditor.java,v 1.2 2004/10/19 11:48:27 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Map.Editor;

import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.Model.DefaultMapEditorApplicationModelFactory;
import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.General.Model.MapEditorApplicationModelFactory;

import java.awt.Frame;
import java.awt.Toolkit;

import javax.swing.JDialog;
import javax.swing.LookAndFeel;
import javax.swing.UIDefaults;
import javax.swing.UIManager;

/**
 * Запуск основного окна модуля "Редактор топологических схем"
 * 
 * 
 * 
 * @version $Revision: 1.2 $, $Date: 2004/10/19 11:48:27 $
 * @module map_v2
 * @author $Author: krupenn $
 * @see
 */
public class MapEditor
{
	ApplicationContext aContext = new ApplicationContext();

	public MapEditor(MapEditorApplicationModelFactory factory)
	{
		if(!Environment.canRun(Environment.MODULE_MAP))
			return;

		aContext.setApplicationModel(factory.create());
		Frame frame = new MapEditorMainFrame(aContext);

		frame.setIconImage(Toolkit.getDefaultToolkit().getImage("images/main/map_mini.gif"));
		frame.setVisible(true);
	}

	public static void main(String[] args)
	{
		try
		{
			UIManager.setLookAndFeel(Environment.getLookAndFeel());
			LookAndFeel laf = UIManager.getLookAndFeel();
			UIDefaults ui = laf.getDefaults();
			ui.put(
					"InternalFrame.icon", 
					LookAndFeel.makeIcon(JDialog.class, "images/general.gif"));
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		new MapEditor(new DefaultMapEditorApplicationModelFactory());
	}
}

