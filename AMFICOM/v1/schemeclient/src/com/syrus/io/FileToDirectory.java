package com.syrus.io;

/*
public class FileToDirectory
{
	FileInputStream fis;
	IntelStreamReader isr;

	String type;
	List elements;
	private int counter = 0;
	Map currentHash;

	static String[] hash_arguments = new String[]
	{
		"@characteristics",
		"@threads",
		"@test_type_ids",
		"@devices",
		"@links",
		"@protoelements",
		"@attributes",
		"@cabletypes",
		"@pathtypes",
		"@arguments",
		"@parameters",
		"@criterias",
		"@thresholds",
		"@eventtypes",
		"@values",
		"@analysis_types",
		"@evaluation_types",
		"@pathelements",
		"@paths",
		"@elements",
		"@mapprotos",
		"@groups",
		"@cablelinks"
	};

	public FileToDirectory (File file) throws IOException
	{
		fis = new FileInputStream(file);
		isr = new IntelStreamReader(fis, "UTF-16");
		type = getType();
		elements = readFromFile();
	}

	public Object read(String key)
	{
		return currentHash.get(key);
	}

	public boolean hasMoreElements()
	{
		return counter < elements.size();
	}

	public void nextElement()
	{
		currentHash = (Map)elements.get(counter);
		counter++;
	}

	public String getTyp()
	{
		return type;
	}

	protected List readFromFile() throws IOException
	{
		boolean continue_analyse = true;
		List vec = new ArrayList();
		Map h = new HashMap();
		while (isr.ready())
		{
			continue_analyse = true;
			String[] s = analyseString(isr.readASCIIString());
			for(int i = 0; i < hash_arguments.length; i++)
			{
				if (s[0].startsWith(hash_arguments[i]))
				{
					s = analyseString(isr.readASCIIString());
					Map ch = new HashMap();
					while (!s[0].startsWith("@end"))
					{
						ch.put(s[0], s[1]);
						s = analyseString(isr.readASCIIString());
					}
					h.put(hash_arguments[i], ch);
					continue_analyse = false;
					break;
				}
			}
			if(continue_analyse)
			{
				if (s[0].startsWith("@"))
					h.put(s[0], s[1]);
				else
				if (s[0].equals(""))
				{
					if (!h.isEmpty())
					{
						vec.add(h);
						h = new HashMap();
					}
				}
			}
		}
		return vec;
	}

	protected String getType() throws IOException
	{
		if (!isr.readASCIIString().equals("AMFICOM component description file"))
			return "";
		String[] s = analyseString (isr.readASCIIString());
		if (s[0].equals("@type"))
			return s[1];
		return "";
	}

	protected String[] analyseString (String s)
	{
		int n = s.indexOf(" ");
		if (n == -1)
			return new String[] {s, ""};
		String s1 = s.substring(0, n);
		String s2 = s.substring(n + 1, s.length());
		return new String[] {s1, s2};
	}

}
*/
