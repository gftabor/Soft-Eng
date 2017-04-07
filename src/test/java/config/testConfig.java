package config;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import config.loadConfig;

import controllers.MapController;

/**
 * Created by jasonashton on 4/6/17.
 */
public class testConfig {
    loadConfig config = new loadConfig();

    @Test
    public void testReader(){
        assertNotNull(config.startReader());
    }
}
