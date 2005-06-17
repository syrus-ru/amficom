/**
 * $Id: MapEditor.java,v 1.9 2005/06/17 13:21:55 peskovsky Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 */

package com.syrus.AMFICOM.client.map.editor;

import java.awt.Image;

import javax.swing.UIManager;

import com.syrus.AMFICOM.client.model.AbstractApplication;
import com.syrus.AMFICOM.client.model.DefaultMapEditorApplicationModelFactory;

/**
 * Запуск основного окна модуля "Редактор топологических схем".
 * 
 * @author $Author: peskovsky $
 * @version $Revision: 1.9 $, $Date: 2005/06/17 13:21:55 $
 * @module mapviewclient_v1
 */
//public class MapEditor {
//	ApplicationContext aContext = new ApplicationContext();
//
//	public MapEditor(MapEditorApplicationModelFactory factory) {
//		if(!Environment.canRun(Environment.MODULE_MAP))
//			return;
//
//		Application.init("mapviewclient");
//		this.aContext.setApplicationModel(factory.create());
//		this.aContext.setDispatcher(new Dispatcher());
//		Frame frame = new MapEditorMainFrame(this.aContext);
//		frame.setIconImage(Toolkit.getDefaultToolkit().getImage(
//				"images/main/map_mini.gif"));
//		frame.setVisible(true);
//	}
//
//	public static void main(String[] args) {
//		new MapEditor(new DefaultMapEditorApplicationModelFactory());
//	}
//}

public class MapEditor extends AbstractApplication
{
        public static final String APPLICATION_NAME = "mapeditor";

        public MapEditor() {
                super(MapEditor.APPLICATION_NAME);
        }
        
        protected void init() {
                super.init();           
                super.aContext.setApplicationModel(new DefaultMapEditorApplicationModelFactory().create());
                super.startMainFrame(new MapEditorMainFrame(this.aContext), (Image)
UIManager.get("images/main/map_mini.gif"));
        }

        public static void main(String[] args) {
                new MapEditor();
        }
}

