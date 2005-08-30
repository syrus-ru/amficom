/*-
 * $Id: UCMExportCommand.java,v 1.1 2005/08/30 08:24:46 krupenn Exp $
 *
 * Copyright ї 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.impexp.unicablemap;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Level;

import org.apache.xmlbeans.XmlError;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;

import com.syrus.AMFICOM.Client.General.Command.ExportCommand;
import com.syrus.impexp.unicablemap.objects.Cable;
import com.syrus.impexp.unicablemap.objects.CableThread;
import com.syrus.impexp.unicablemap.objects.CableType;
import com.syrus.impexp.unicablemap.objects.ChannelingItem;
import com.syrus.impexp.unicablemap.objects.Element;
import com.syrus.impexp.unicablemap.objects.MuffType;
import com.syrus.impexp.unicablemap.objects.Port;
import com.syrus.impexp.unicablemap.objects.ThreadType;
import com.syrus.util.Log;

/**
 * @author $Author: krupenn $
 * @version $Revision: 1.1 $, $Date: 2005/08/30 08:24:46 $
 * @module importUCM
 */

public abstract class UCMExportCommand extends ExportCommand {
	UniCableMapDatabase ucmDatabase;
	
	public UCMExportCommand(UniCableMapDatabase ucmDatabase) {
		this.ucmDatabase = ucmDatabase;
	}
	
	void parseObject(PrintWriter out, UniCableMapObject ucmObject) {
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
	
	protected boolean validateXml(XmlObject xml) {
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
	
	void surveyObjects(PrintWriter pw, String type) {
		Collection objs = this.ucmDatabase.getObjects(
				this.ucmDatabase.getType(type));
		
		int i = 0;
		Iterator it2 = objs.iterator();
		while (it2.hasNext()) {
			if(i++ > 100)
				break;
		UniCableMapObject ucmObject = (UniCableMapObject)it2.next();
		{
			parseObject(pw, ucmObject);
			try {
				Collection children = this.ucmDatabase.getChildren(ucmObject);
				pw.println("Children:");
				for (Iterator it = children.iterator(); it.hasNext();) {
					// get link
					UniCableMapLink ucmChild = (UniCableMapLink)it.next();
					pw.println("Тип связи: " + ucmChild.mod);
					// parse child
					parseObject(pw, ucmChild.child);
				}
				
				Collection parents = this.ucmDatabase.getParents(ucmObject);
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
		}
		System.out.println(objs.size() + " done");
		pw.flush();
	}
}
