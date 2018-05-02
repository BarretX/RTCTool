package Charts;

import com.google.visualization.datasource.render.JsonRenderer;

public class BarChart extends Chart {
		
	public BarChart(String chartName)
	{
		chartType="BarChart";
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
		
		String AxisFormat=",\"vAxis\":{\"title\":\""+xTitle+"\"},\"hAxis\":{\"title\":\""+yTitle+"\",\"format\":\""+yAxisFormat+"\"}";
		String Legend=",\"legend\":{\"position\":\"bottom\",\"alignment\":\"center\",\"maxLines\":\"2\",\"textStyle\":{\"color\":\"#555555\"}}";
		String head="\"description\": \""+description
				+"\",\"options\":{\"title\":\""+title+"\""+",\"chartArea\": {\"left\":"+chartLeft+"}"+",\"isStacked\": "+isStacked+colors+AxisFormat+Legend+"},\"chartType\":\""+chartType+"\",\"datatable\":";
		
		String body=JsonRenderer.renderDataTable(tableData, true, true, true).toString();
		return "{"+head+body+"}";
		
	}
}