package Helper;

import java.util.Calendar;

public class Calendar_Compare
{
	public static boolean Before(Calendar a,Calendar b)
	{
		if(a.get(Calendar.YEAR)!=b.get(Calendar.YEAR))
			return a.get(Calendar.YEAR)<b.get(Calendar.YEAR)?true:false;
		
		if(a.get(Calendar.DAY_OF_YEAR)<b.get(Calendar.DAY_OF_YEAR))
			return true;
		return false;
	}
	
	public static boolean After(Calendar a,Calendar b)
	{
		if(a.get(Calendar.YEAR)!=b.get(Calendar.YEAR))
			return a.get(Calendar.YEAR)>b.get(Calendar.YEAR)?true:false;
			
		if(a.get(Calendar.DAY_OF_YEAR)>b.get(Calendar.DAY_OF_YEAR))
			return true;
		return false;
	}
	
	public static boolean Equal(Calendar a,Calendar b)
	{
		if(a.get(Calendar.YEAR)!=b.get(Calendar.YEAR))
			return false;
		
		if(a.get(Calendar.DAY_OF_YEAR)==b.get(Calendar.DAY_OF_YEAR))
			return true;
		
		return false;
	}	
}