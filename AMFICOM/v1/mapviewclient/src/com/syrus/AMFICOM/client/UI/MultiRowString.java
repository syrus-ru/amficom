/**
 * $Id: MultiRowString.java,v 1.1 2005/06/08 13:44:06 krupenn Exp $
 * Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.client.UI;

import com.syrus.io.SimpleStringTokenizer;

/**
 * Класс, представляющий строку, соедржещую разделителем '\n' подстроки,
 * как набор подстрок. Используется для вывода всплывающих подсказок
 * в несколько строк
 * @version $Revision: 1.1 $
 * @author $Author: krupenn $
 * @module commonclient_v1
 * @see MultiRowToolTipUI
 */
public class MultiRowString {
	int rows;
	String[] strings;

	public MultiRowString(String s) {
		parseString(s);
	}

	public void parseString(String s) {
		SimpleStringTokenizer tokenizer = new SimpleStringTokenizer(s, "\n");
		this.rows = tokenizer.countTokens();
		this.strings = new String[this.rows];
		for(int i = 0; i < this.rows; i++)
			this.strings[i] = tokenizer.nextToken();
	}

	public int getRowCount() {
		return this.rows;
	}

	public String get(int i) {
		return this.strings[i];
	}
}
