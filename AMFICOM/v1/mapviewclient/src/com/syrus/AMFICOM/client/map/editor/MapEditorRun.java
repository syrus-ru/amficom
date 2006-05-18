/*-
 * $$Id: MapEditorRun.java,v 1.12 2005/09/30 16:08:39 krupenn Exp $$
 *
 * Copyright 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.map.editor;

/**
 * Запуск приложения "Редактор топологических схем" 
 * 
 * @version $Revision: 1.12 $, $Date: 2005/09/30 16:08:39 $
 * @author $Author: krupenn $
 * @author Andrei Kroupennikov
 * @module mapviewclient
 */
public class MapEditorRun {
	private MapEditorRun() {
		//empty
	}

	public static void main(String[] args) {
		new MapEditor();
	}
}
