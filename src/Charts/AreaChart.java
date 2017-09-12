package Charts;

import com.google.visualization.datasource.render.JsonRenderer;

public class AreaChart extends Chart {
	
	public String isStacked= "true";
	
	public AreaChart(String chartName)
	{
		chartType="BChart";
		title=chartName;
	}
	
	public String ToJson() {
		// TODO Auto-generated method stub
		String colors="";
		if(colorList!=null&&colorList.size()!=0)
		{
			colors+=",\"colors\": [";
			for(int i=0;i<colorList.size();++i)
			{			
				colors+="\""+colorList.get(i)+"\"";
				if(i!=colorList.size()-1)
					colors+=",";
			}
			colors+="]";
		}
		
		String AxisFormat=",\"hAxis\":{\"title\":\""+xTitle+"\"},\"vAxis\":{\"title\":\""+yTitle+"\",\"format\":\""+yAxisFormat+"\"}";
		
		String head="\"description\": \""+description
				+"\",\"options\":{\"title\":\""+title+"\""+",\"isStacked\": "+isStacked+colors+AxisFormat+"},\"chartType\":\""+chartType+"\",\"datatable\":";
		
		String body=JsonRenderer.renderDataTable(tableData, true, true, true).toString();
		return "{"+head+body+"}";
		
	}
}