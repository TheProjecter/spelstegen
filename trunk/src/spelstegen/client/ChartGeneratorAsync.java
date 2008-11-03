package spelstegen.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ChartGeneratorAsync {
    public void generatePointsHistoryChart(AsyncCallback<String> callback);
} 