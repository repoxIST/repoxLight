/*
 * Created on 23/Mar/2006
 *
 */
package pt.utl.ist.repox;

import java.io.IOException;
import java.util.Properties;


/**
 * Represents all the global available to the application. Just for convenience.
 * Not to be used for having globals that are important for the model.
 *
 * @author Nuno Freire
 */
public class RepoxConfigurationDefault extends RepoxConfiguration {

    public RepoxConfigurationDefault(Properties configurationProperties) throws IOException {
        super(configurationProperties);
    }
}
