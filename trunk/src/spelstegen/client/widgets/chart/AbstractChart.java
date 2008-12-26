package spelstegen.client.widgets.chart;

import spelstegen.client.ChartGenerator;
import spelstegen.client.ChartGeneratorAsync;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.Image;

/**
 * Base class to generate charts
 * 
 * @author Per Mattsson
 */
public abstract class AbstractChart extends Image {

    protected ChartGeneratorAsync chartGeneratorService;
    protected int width, height;

    public AbstractChart(int width, int height) {
        super();
        this.width = width;
        this.height = height;
        //Setup connection to chart generator service.
        chartGeneratorService = (ChartGeneratorAsync) GWT.create(ChartGenerator.class);
        ServiceDefTarget endpoint = (ServiceDefTarget)chartGeneratorService;
        String url = GWT.getModuleBaseURL() + "chartGenerator";
        endpoint.setServiceEntryPoint(url);
    }
}
