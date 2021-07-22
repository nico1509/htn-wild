package edu.kit.aifb.ls3.kaefer.jmonkeyld.util;

public class Utils {

	private Utils() {
		
	}

	public static String getLocalName(String s) {
		boolean slashEnding = s.endsWith("/");
		int lastIdx = slashEnding ? s.length() - 2 : s.length();
		int hashIdx = s.lastIndexOf('#', lastIdx);
		int slashIdx = s.lastIndexOf('/', lastIdx);
		int cutIdx;
		if (hashIdx > slashIdx)
			cutIdx = hashIdx;
		else
			cutIdx = slashIdx;
		return s.substring(cutIdx + 1,
				slashEnding ? s.length() - 1 : s.length());
	}

}
