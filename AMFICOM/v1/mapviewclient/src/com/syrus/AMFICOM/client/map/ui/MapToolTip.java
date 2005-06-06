/**
 * $Id: MapToolTip.java,v 1.4 2005/06/06 12:20:36 krupenn Exp $
 * Syrus Systems.
 * ������-����������� �����.
 * ������: �������.
 */
package com.syrus.AMFICOM.client.map.ui;

import javax.swing.JToolTip;

import com.syrus.AMFICOM.client.UI.MultiRowToolTipUI;

/**
 * ����� ������������ ��� ����������� �����������
 * ��������� � ��������� �����
 * 
 * @version $Revision: 1.4 $, $Date: 2005/06/06 12:20:36 $
 * @author $Author: krupenn $
 * @module mapviewclient_v1
 */
public class MapToolTip extends JToolTip {
	MultiRowToolTipUI toolTipUI = new MultiRowToolTipUI();

	public MapToolTip() {
		super();
		setUI(this.toolTipUI);
	}
}
