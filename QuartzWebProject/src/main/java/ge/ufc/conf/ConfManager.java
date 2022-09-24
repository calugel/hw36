package ge.ufc.conf;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class ConfManager {
    private static final Logger lgg = LogManager.getLogger();
    private static final String CONFIG_FILE_LOCATION = "config.properties";
    private volatile static ConfManager _singleton = null;
    private static URL url = null;
    protected long lastModified;

    private User user;

    private ConfManager(URLConnection conn) throws IOException {
        this.lastModified = conn.getLastModified();

        try (InputStream in = conn.getInputStream()) {
            Properties props = new Properties();
            props.load(conn.getInputStream());
            lgg.info("configuration reloaded");
            fillSetting(props);
        }
    }

    public static ConfManager getConfiguration() throws IOException {
        if (url == null) {
            url = ConfManager.class.getClassLoader().getResource(CONFIG_FILE_LOCATION);
        }
        if (url == null) {
            throw new IOException("Configuration file not found");
        }

        URLConnection conn = url.openConnection();

        long lastModified = conn.getLastModified();
        if (_singleton == null || lastModified > _singleton.lastModified) {
            synchronized (CONFIG_FILE_LOCATION) {
                if (_singleton == null || lastModified > _singleton.lastModified) {
                    _singleton = new ConfManager(conn);
                }
            }
        }

        return _singleton;
    }

    private void fillSetting(Properties props) {
        user = new User();
        user.setUsername(props.getProperty("username", ""));
        user.setPassword(props.getProperty("password", ""));
        user.setPath(props.getProperty("database"));
    }

    public User getUser() {
        return this.user;
    }
}
