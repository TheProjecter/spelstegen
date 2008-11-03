package spelstegen.client.widgets.chart;

import spelstegen.client.ChartGenerator;
import spelstegen.client.ChartGeneratorAsync;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.Image;

/**
 * Base class to generate charts
 * 
 * @author Per Mattsson
 */
public abstract class AbstractChart extends Image {

    protected ChartGeneratorAsync chartGeneratorService;
    protected AsyncCallback callback;

    public AbstractChart() {
        super();
        
        //Setup connection to chart generator service.
        chartGeneratorService = (ChartGeneratorAsync) GWT.create(ChartGenerator.class);
        ServiceDefTarget endpoint = (ServiceDefTarget)chartGeneratorService;
        String url = GWT.getModuleBaseURL() + "chartGenerator";
        endpoint.setServiceEntryPoint(url);

        // Setup the callback from the chart generator service.
        callback = new AsyncCallback() {
            /*
             * If the call was successful, we will get back the name of the chart
             * that was created and stored on the server.
             */
            public void onSuccess(Object s) {
                String chartName = (String)s;
                String imageUrl = "./displayChart?filename=" + chartName;
                setUrl(imageUrl);
            }
           
            /*
             * Something went wrong with the call. Handle the issue how you'd like.
             */
            public void onFailure(Throwable ex) {
                // do nothing for now
            }
        };
    }
}
