package Charts;

import java.util.List;

import EagleEyeAPI.EagleEyeAPI;

public class ChartSets {
	public static String Create(String title,String description, List<String> ids)
	{
		String json="{\"title\": \""
				+ title
				+ "\",\"description\": \""
				+ description
				+ "\",\"charts\": [\"";
		for(int i=0;i<ids.size()-1;i++)
		{
			json+= ids.get(i)+ "\", \"";
		}
		json+= ids.get(ids.size()-1)+ "\"]}";

		String output=EagleEyeAPI.createChartSet(json);
		return output.substring(output.length()-26, output.length()-2);
	}
}
