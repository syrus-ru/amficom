/**
 * $Id: MapEditorRun.java,v 1.5 2005/06/06 10:11:37 krupenn Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: ������� ������������������ �������������������
 *         ���������������� �������� ���������� �����������
 */
package com.syrus.AMFICOM.Client.Map.Editor;

import com.syrus.AMFICOM.Client.General.Model.DefaultMapEditorApplicationModelFactory;
import com.syrus.util.Application;

/**
 * ������ ���������� "�������� �������������� ����" 
 * 
 * 
 * 
 * @version $Revision: 1.5 $, $Date: 2005/06/06 10:11:37 $
 * @module mapviewclient_v1
 * @author $Author: krupenn $
 */
public class MapEditorRun {
	private MapEditorRun() {
		//empty
	}

	public static void main(String[] args) {
		Application.init("mapviewclient");
		new MapEditor(new DefaultMapEditorApplicationModelFactory());
	}
}
