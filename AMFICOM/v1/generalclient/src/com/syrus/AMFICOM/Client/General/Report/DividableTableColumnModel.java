package com.syrus.AMFICOM.Client.General.Report;

import javax.swing.table.DefaultTableColumnModel;

/**
 * <p>Title: </p>
 * <p>Description: Табличная модель с возможностью
 * задания числа вертикальных разбиений</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Syrus Systems</p>
 * @author Песковский Пётр
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
