package com.syrus.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Vector;

/**
 * @deprecated use Properties
 */
public class IniFile {

	private File		file;
	private Properties	properties;
	private Vector		keys;	
	private Vector		values;

	public IniFile(File file) throws IOException {
		this.file = file;
		this.properties = new Properties();
		this.properties.load(new FileInputStream(this.file));
		findKeys();
	}

	public IniFile(String fileName) throws IOException {
		this(new File(fileName));
	}

	public Enumeration getKeyEnumeration() {
		return this.properties.keys();
	}

	/**
	 * @deprecated use getKeyEnumeration()
	 * @return
	 */
	public Vector getKeys() {
		return this.keys;
	}

	public String getValue(Object key) {
		return getValue((String) key);
	}

	public String getValue(String key) {
		return this.properties.getProperty(key);
	}

	public String getValue(String key,
			String defaultValue) {
		return this.properties.getProperty(key, defaultValue);
	}

	public boolean saveKeys() {
		try {
			this.properties.store(new FileOutputStream(this.file), null);
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
		return true;
	}

	public void setValue(String key,
			Object value) {
		this.properties.put(key, value);
	}

	/**
	 * @deprecated remove it when remove all Vectors
	 *  
	 */
	private void findKeys() {
		this.keys = new Vector();
		this.values = new Vector();
		Enumeration keys = this.properties.keys();
		while (keys.hasMoreElements()) {
			String key = (String) keys.nextElement();
			this.keys.add(key);
			this.values.add(this.properties.getProperty(key));
		}
	}

}