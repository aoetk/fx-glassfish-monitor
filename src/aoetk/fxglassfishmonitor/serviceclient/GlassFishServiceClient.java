package aoetk.fxglassfishmonitor.serviceclient;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.core.MediaType;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;

/**
 * The client for GlassFish API.
 * @author aoetk
 */
public class GlassFishServiceClient {

    public static final String INITIAL_URL = "http://localhost:4848/monitoring/domains/server";

    public static final String BASE_URL = "//localhost:4848/monitoring/domains/";

    public static final String EXTENSION = ".json";

    private static final GlassFishServiceClient INSTANCE = new GlassFishServiceClient();

    private Client client;

    public static GlassFishServiceClient getInstance() {
        return INSTANCE;
    }

    private GlassFishServiceClient() {
        initClient();
    }

    public GlassFishData getResource(String resouceFullName) throws ConnectFailedException {
        try {
            String urlString = BASE_URL + resouceFullName + EXTENSION;
            final WebResource wr = client.resource(new URI("http", urlString, null));
            wr.accept(MediaType.APPLICATION_JSON_TYPE);
            final ClientResponse response = wr.get(ClientResponse.class);
            if (response.getStatus() >= 300) {
                throw new ConnectFailedException(response.getStatus(), null);
            }
            return response.getEntity(new GenericType<GlassFishData>(GlassFishData.class));

        } catch (UniformInterfaceException uniformInterfaceException) {
            throw new ConnectFailedException(uniformInterfaceException.getResponse().getStatus(), uniformInterfaceException);
        } catch (ClientHandlerException clientHandlerException) {
            throw new ConnectFailedException(clientHandlerException);
        } catch (URISyntaxException ex) {
            Logger.getLogger(GlassFishServiceClient.class.getName()).log(Level.SEVERE, null, ex);
            throw new ConnectFailedException(ex);
        }
    }

    private void initClient() {
        final ClientConfig config = new DefaultClientConfig();
        config.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
        this.client = Client.create(config);
    }

}
