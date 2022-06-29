package Funcionamiento;

import java.util.Locale;

public class Utilidades {

	public static String os = System.getProperty("os.name", "unknown").toLowerCase(Locale.ROOT);

	public static boolean isWindows()
	{
		return os.contains("win");
	}

	public static boolean isMac()
	{
		return os.contains("mac");
	}

	public static boolean isUnix()
	{
		return os.contains("nux");
	}

}
