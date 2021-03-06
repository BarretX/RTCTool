package Charts;

import com.google.visualization.datasource.render.JsonRenderer;

public class ComboChart extends Chart {
		
	public ComboChart(String chartName)
	{
		chartType="ComboChart";
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
		
		String Window=",\"viewWindow\":{\"min\":0}";
		String AxisFormat=",\"hAxis\":{\"title\":\""+xTitle+"\"},\"vAxis\":{\"title\":\""+yTitle+"\""+Window+",\"format\":\""+yAxisFormat+"\"}";
	
		String Legend=",\"legend\":{\"position\":\"bottom\",\"alignment\":\"center\",\"maxLines\":\"2\",\"textStyle\":{\"color\":\"#555555\"}}";
		String head="\"description\": \""+description
				+"\",\"options\":{\"title\":\""+title+"\""+",\"seriesType\": \"bars\""+",\"series\": {\""+baseLineIndex+"\": {\"type\": \"line\"}}"+colors+AxisFormat+Legend+"},\"chartType\":\""+chartType+"\",\"datatable\":";

		String body=JsonRenderer.renderDataTable(tableData, true, true, true).toString();
		return "{"+head+body+"}";
	}	
}