package aoetk.fxglassfishmonitor.model;

import org.junit.Test;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

/**
 *
 * @author aoetakashi
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
}
