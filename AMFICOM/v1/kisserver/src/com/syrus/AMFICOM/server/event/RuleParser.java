package com.syrus.AMFICOM.server.event;

import java.util.Vector;
import java.util.Hashtable;

class LogicParam {
	int nr;
	Vector pos = new Vector();
	String val;
}

public class RuleParser {
	String logic;
	Hashtable logicParams = new Hashtable();

	String action;
	String[] action_parameters;
	String descriptor = "";

	public RuleParser(String logic)	{
		this.logic = logic;
    this.parseRule();
	}

	private void parseRule() {
		int start = 0;
    int end;
    String temp;

		this.action = this.nextWord(logic, 0);
		start += action.length();
		start = logic.indexOf("(", start);
		end = logic.indexOf(")", start);
		this.action_parameters = this.parseParameters(logic.substring(start + 1, end));

/*
    if (this.logic.startsWith("IF")) {
      start = 3;
			this.descriptor = this.nextWord(logic, start);
			start += descriptor.length();

      temp = this.nextWord(logic, start);
      if (temp.equals("THEN")) {
				start += 5;
				action = this.nextWord(logic, start);
				start += action.length();

				start = logic.indexOf("(", start);
				end = logic.indexOf(")", start);
				this.action_parameters = this.parseParameters(logic.substring(start + 1, end));
			}
    }*/
  }

	private String nextWord(String str, int startIndex) {
		int[] d = {' ', ';', '\"', ')', '(', '.', ',', '{', '}'};

		while (str.charAt(startIndex) == ' ')
			startIndex++;
//		int endIndex = str.indexOf(d[0], startIndex);
		int endIndex = str.length();

		for (int i = 0; i< d.length; i++)
			if ((str.indexOf(d[i], startIndex) > 0) && str.indexOf(d[i], startIndex) < endIndex)
				endIndex = str.indexOf(d[i], startIndex);
		return str.substring(startIndex, endIndex);
	}
/*
	private int nextInt(String str, int startIndex) {
		String subs;
		int[] d = {' ', ';', '\"', ')', '(', '.', ',', '{', '}'};

		while (str.charAt(startIndex) == ' ')
			startIndex++;
//		System.out.println("	startIndex " + startIndex);
		int endIndex = str.indexOf(d[0], startIndex);
//		System.out.println("	endIndex " + endIndex);

		for (int i = 1; i< d.length; i++)
			if ((str.indexOf(d[i], startIndex) > 0) && str.indexOf(d[i], startIndex) < endIndex) {
				endIndex = str.indexOf(d[i], startIndex);
//				System.out.println("	endIndex " + endIndex);
			}
		subs = str.substring(startIndex, endIndex);
//		System.out.println("	substring " + subs);

		return Integer.parseInt(subs);
	}
*/
	private String[] parseParameters (String str)	{
		Vector params = new Vector();
		int d = '\"';
		int d2 = ' ';
		int st = 0;
		int end = -1;
		int tmp;
		boolean proceed = true;

		while (proceed)	{
			end = str.indexOf(',', st);
			if (end == -1)
				end = str.length();
			tmp = str.indexOf(d, st);
			if(tmp != -1 && tmp < end) {
				st += 1;
				end -= 1;
			}
			params.add(str.substring(st, end));
			if(tmp != -1 && tmp < end)
				end += 1;
			if (str.indexOf(',', end) == -1)
				proceed = false;
			st = end + 1;

/*
			st = str.indexOf(d, end + 1);
			end = str.indexOf(d, st + 1);
			params.add(str.substring(st + 1, end));

			if (str.indexOf(',', end) == -1)
				proceed = false;
*/
		}

		return (String[])params.toArray(new String[]{});
	}

	public String getAction() {
		return this.action;
	}

	public String[] getActionParameters()	{
		return this.action_parameters;
	}

	public String getActionParameter(int i)	{
			return this.action_parameters[i];
	}

	public String getDescriptor() {
		return this.descriptor;
	}
}
