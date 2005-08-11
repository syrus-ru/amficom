/**
 * $Id: MapToolTip.java,v 1.5 2005/08/11 12:43:32 arseniy Exp $
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
 * @version $Revision: 1.5 $, $Date: 2005/08/11 12:43:32 $
 * @author $Author: arseniy $
 * @module mapviewclient
 */
public class MapToolTip extends JToolTip {
	MultiRowToolTipUI toolTipUI = new MultiRowToolTipUI();

	public MapToolTip() {
		super();
		setUI(this.toolTipUI);
	}
}
