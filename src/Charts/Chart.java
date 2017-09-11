package Charts;



import com.google.visualization.datasource.datatable.DataTable;
import com.google.visualization.datasource.render.JsonRenderer;
import EagleEyeAPI.EagleEyeAPI;

public class Chart implements  IToJson,IToEagleEye {
	public String description;
	public String chartType;
	public String title;

	public DataTable tableData;
	
	@Override
	public String ToJson() {
		// TODO Auto-generated method stub
		//String head="\"description\": \""+description+"\",\"options\":{\"title\":\""+title+"\",\"hAxis\":{\"title\":\"\",\"format\":\"\"},\"vAxis\":{\"title\":\"\",\"format\":\"\"},\"combolines\":\"\",\"isStacked\":false,\"chartArea\":{}},\"chartType\":\""+chartType+"\",\"datatable\":";
		String head="\"description\": \""+description
				+"\",\"options\":{\"title\":\""+title+"\"},\"chartType\":\""+chartType+"\",\"datatable\":";
		String body=JsonRenderer.renderDataTable(tableData, true, true, true).toString();
		return "{"+head+body+"}";
	}

	@Override
	public String ToEagleEye() {
		// TODO Auto-generated method stub
		String json=ToJson();
		String output=EagleEyeAPI.createChart(json);
		return output.substring(output.length()-26, output.length()-2);
	}
	
	
}
