package com.syrus.AMFICOM.Client.General.Report;

import javax.swing.table.DefaultTableColumnModel;

/**
 * <p>Title: </p>
 * <p>Description: ��������� ������ � ������������
 * ������� ����� ������������ ���������</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Syrus Systems</p>
 * @author ���������� ϸ��
 * @version 1.0
 */

public abstract class DividableTableColumnModel extends DefaultTableColumnModel {
  private int divisionsNumber = 1;

  public DividableTableColumnModel(int divNumber)
  {
	 tableColumns.removeAllElements();
	 divisionsNumber = divNumber;
  }

  public void setDivisionsNumber(int divNumber) {
	 divisionsNumber = divNumber;
  }

  public int getDivisionsNumber() {
	 return divisionsNumber;
  }
}
