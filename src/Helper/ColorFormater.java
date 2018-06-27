package Helper;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import com.ibm.icu.text.SimpleDateFormat;

public class ColorFormater
{
	public static SimpleDateFormat sdf=new SimpleDateFormat("MM/dd/yyyy");
	
	public static String RGB2String(int R,int G,int B)
	{
		String color=new String();
		color+='#';
		if(R<16)  color+='0';
		color+=Integer.toHexString(R);
		
		if(G<16) color+='0';
		color+=Integer.toHexString(G);
		
		if(B<16) color+='0';
		color+=Integer.toHexString(B);
		
		return color;
	}
	
	public static String Str2JSStr(String temp)
	{
		String JsDateStr="";	
		try
		{
			Date tempDate=sdf.parse(temp);
			Calendar tempCal=Calendar.getInstance();
			tempCal.setTime(tempDate);
			JsDateStr="Date("+Integer.toString(tempCal.get(Calendar.YEAR))+","+Integer.toString(tempCal.get(Calendar.MONTH))+","+Integer.toString(tempCal.get(Calendar.DAY_OF_MONTH))+")";
		}
		catch(ParseException e1)
		{
			System.out.println("Exception: In Str2JSStr &_& ");
		}
		return JsDateStr;		
	}
}
