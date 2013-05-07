package aoetk.fxglassfishmonitor.model;

import org.junit.Test;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

/**
 * Test for {@link GlassFishMonitor}.
 * @author aoetk
 */
public class GlassFishMonitorTest {

    @Test
    public void testInitialize() throws Exception {
        GlassFishMonitor sut = new GlassFishMonitor();
        sut.initialize();
        ResourceHolder actual = sut.getServerResource();
        assertThat(actual.getName(), is("server"));
        assertThat(actual.childTracedProperty().get(), is(false));
        assertThat(actual.getChildStatistics().size(), is(0));
        assertThat(actual.getChildResources().size(), is(0));
    }

    @Test
    public void testTraceChildResource_server() throws Exception {
        GlassFishMonitor sut = new GlassFishMonitor();
        sut.initialize();
        ResourceHolder actual = sut.getServerResource();
        sut.traceChildResource(actual);
        assertThat(actual.getName(), is("server"));
        assertThat(actual.getChildStatistics().size(), is(0));
        assertThat(actual.childTracedProperty().get(), is(true));
        assertThat(actual.getChildResources().size(), is(9));
    }

    @Test
    public void testTraceChildResouce_updateResoucesSiblingIndex() throws Exception {
        GlassFishMonitor sut = new GlassFishMonitor();
        sut.initialize();
        ResourceHolder root = sut.getServerResource();
        sut.traceChildResource(root);
        ResourceHolder network = root.getChildResources().get(4);
        sut.traceChildResource(network);
        assertThat(network.getChildResources().size(), is(7));
        assertThat(network.siblingIndexProperty().get(), is(4));
        assertThat(root.getChildResources().get(5).siblingIndexProperty().get(), is(11));
        assertThat(root.getChildResources().get(6).siblingIndexProperty().get(), is(12));
        assertThat(root.getChildResources().get(7).siblingIndexProperty().get(), is(13));
        assertThat(root.getChildResources().get(8).siblingIndexProperty().get(), is(14));
    }

}
