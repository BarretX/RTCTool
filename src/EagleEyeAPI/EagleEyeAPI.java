package EagleEyeAPI;

import ConstVar.ConstString;
import Helper.HttpRequest;

public class EagleEyeAPI {
	public static String createChart(String json) {
		// TODO Auto-generated method stub
		String ret=	HttpRequest.sendPost(ConstString.CHART_API_URL,json);
		return ret;
	}
	public static String createChartSet(String json) {
		// TODO Auto-generated method stub
		String ret=	HttpRequest.sendPost(ConstString.CHART_SET_API_URL,json);
		return ret;
	}
	public static void deleteChart(String json) {
		// TODO Auto-generated method stub

	}
	public static void deleteChartSet(String json) {
		// TODO Auto-generated method stub

	}
	//
	public static void updateChart(String id,String json) {
		// TODO Auto-generated method stub
		HttpRequest.sendPost(ConstString.CHART_API_URL+id, json);
	}
	public static void updateChartSet(int id,String json) {
		// TODO Auto-generated method stub
		HttpRequest.sendPost(ConstString.CHART_SET_API_URL+id, json);
	}

}
