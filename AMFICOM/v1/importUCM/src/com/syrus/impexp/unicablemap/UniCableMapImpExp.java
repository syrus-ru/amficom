package com.syrus.impexp.unicablemap;

import com.syrus.impexp.ImportExportException;

/**
 * 
 * @author $Author: krupenn $
 * @version $Revision: 1.3 $, $Date: 2005/05/25 16:06:51 $
 * @module mapviewclient_v1
 */
public class UniCableMapImpExp 
{
	String username = "sysdba";
	String password = "masterkey";
	String host = "localhost";
	String database = "d:/My Documents/ISM/doc/Resident/tel.gdb";

	String fileName = "d:/My Documents/ISM/prog/java/AMFICOm/run/Data/ucm/testucm.esf";

	public UniCableMapImpExp()
	{
		UniCableMapDialog dialog = new UniCableMapDialog();

		dialog.setVisible(true);

//		try
//		{
//			UniCableMapDatabase ucmDatabase = new UniCableMapDatabase(
//				this.username,
//				this.password,
//				this.host,
//				this.database);
//			UniCableMapExportCommand command = new UniCableMapExportCommand(ucmDatabase, this.fileName);
////			command.execute();
//			ucmDatabase.close();
//		}
//		catch (ImportExportException e)
//		{
//			System.out.println(e.getMessage());
//		}
	}

	public static void main(String[] args)
	{
		new UniCableMapImpExp();
	}
}