/**
 * $Id: MapEditorRun.java,v 1.4 2005/05/27 15:14:57 krupenn Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: ������� ������������������ �������������������
 *         ���������������� �������� ���������� �����������
 */
package com.syrus.AMFICOM.Client.Map.Editor;

import com.syrus.AMFICOM.Client.General.Model.DefaultMapEditorApplicationModelFactory;

/**
 * ������ ���������� "�������� �������������� ����" 
 * 
 * 
 * 
 * @version $Revision: 1.4 $, $Date: 2005/05/27 15:14:57 $
 * @module mapviewclient_v1
 * @author $Author: krupenn $
 */
public class MapEditorRun {
	private MapEditorRun() {
		//empty
	}

	public static void main(String[] args) {
		new MapEditor(new DefaultMapEditorApplicationModelFactory());
	}
}
