/*-
 * $Id: UCMParser.java,v 1.1 2005/08/30 12:42:37 krupenn Exp $
 *
 * Copyright ї 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.impexp.unicablemap;

import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.apache.xmlbeans.XmlError;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;

/**
 * @author $Author: krupenn $
 * @version $Revision: 1.1 $, $Date: 2005/08/30 12:42:37 $
 * @module importUCM
 */

public final class UCMParser {
	
	private UCMParser() {
		//empty
	}
	
	static void parseObject(PrintWriter out, UniCableMapObject ucmObject) {
		out.println("Объект: " + ucmObject.text);
		out.println("\t x0 = " + ucmObject.x0 + ";\n\t y0 = " + ucmObject.y0 + ";\n\t x1 = " + ucmObject.x1 + ";\n\t y1 = " + ucmObject.y1 + ";\n\t dx = " + ucmObject.dx + ";\n\t dy = " + ucmObject.dy);
		out.println("\t msk = " + ucmObject.msk + ";\n\t ord = " + ucmObject.ord + ";\n\t state = " + ucmObject.state + ";\n\t un = " + ucmObject.un);
		out.println("\t Тип: " + ucmObject.typ.text);
		out.println("\t Параметры:");
		for(Iterator it2 = ucmObject.buf.params.iterator(); it2.hasNext();) {
			UniCableMapParameter param = (UniCableMapParameter )it2.next();
			out.println("\t    " + param.realParameter.text + " = " + param.value);
		}
	}
	
	public static boolean validateXml(XmlObject xml) {
		boolean isXmlValid = false;

		// A collection instance to hold validation error messages.
		ArrayList validationMessages = new ArrayList();

		// Validate the XML, collecting messages.
		isXmlValid = xml.validate(new XmlOptions()
				.setErrorListener(validationMessages));

		if(!isXmlValid) {
			System.out.println("Invalid XML: ");
			for(int i = 0; i < validationMessages.size(); i++) {
				XmlError error = (XmlError )validationMessages.get(i);
				System.out.println("");
				System.out.println(xml);
				System.out.println("");
				System.out.println(error.getMessage());
				System.out.println(error.getObjectLocation());
				System.out.println("Column " + error.getColumn());
				System.out.println("Line " + error.getLine());
				System.out.println("Offset " + error.getOffset());
				System.out.println("Object at cursor " + error.getCursorLocation().getObject());
				System.out.println("Source name " + error.getSourceName());
			}
		}
		return isXmlValid;
	}
	
	public static void surveyObjects(PrintWriter pw, String type, UniCableMapDatabase ucmDatabase, int count) {
		Collection<UniCableMapObject> objs = ucmDatabase.getObjects(
				ucmDatabase.getType(type));

		if(count == -1) {
			count = objs.size();
		}
		
		int i = 0;
		for (UniCableMapObject ucmObject : objs) {
			if(i++ > count)
				break;
			parseObject(pw, ucmObject);
			try {
				Collection children = ucmDatabase.getChildren(ucmObject);
				pw.println("Children:");
				for (Iterator it = children.iterator(); it.hasNext();) {
					// get link
					UniCableMapLink ucmChild = (UniCableMapLink)it.next();
					pw.println("Тип связи: " + ucmChild.mod);
					// parse child
					parseObject(pw, ucmChild.child);
				}
				
				Collection parents = ucmDatabase.getParents(ucmObject);
				pw.println("Parents:");
				for (Iterator it = parents.iterator(); it.hasNext();) {
					// get link
					UniCableMapLink ucmParent = (UniCableMapLink)it.next();
					pw.println("Тип связи: " + ucmParent.mod);
					// parse parent
					parseObject(pw, ucmParent.parent);
				}
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
		System.out.println(objs.size() + " done");
		pw.flush();
	}
}
