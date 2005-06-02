/**
 * $Id: MapEditor.java,v 1.4 2005/02/07 16:09:26 krupenn Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: ������� ������������������ �������������������
 *         ���������������� �������� ���������� �����������
 *
 * ���������: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Map.Editor;

import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.Model.DefaultMapEditorApplicationModelFactory;
import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.General.Model.MapEditorApplicationModelFactory;

import com.syrus.util.Application;
import java.awt.Frame;
import java.awt.Toolkit;

import javax.swing.JDialog;
import javax.swing.LookAndFeel;
import javax.swing.UIDefaults;
import javax.swing.UIManager;

/**
 * ������ ��������� ���� ������ "�������� �������������� ����".
 * 
 * @author $Author: krupenn $
 * @version $Revision: 1.4 $, $Date: 2005/02/07 16:09:26 $
 * @module mapviewclient_v1
 */
public class MapEditor
{
	ApplicationContext aContext = new ApplicationContext();

	public MapEditor(MapEditorApplicationModelFactory factory)
	{
		if(!Environment.canRun(Environment.MODULE_MAP))
			return;

		this.aContext.setApplicationModel(factory.create());
		Frame frame = new MapEditorMainFrame(this.aContext);

		frame.setIconImage(Toolkit.getDefaultToolkit().getImage("images/main/map_mini.gif"));
		frame.setVisible(true);
	}

	public static void main(String[] args)
	{
		Application.init("mapviewclient");
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

