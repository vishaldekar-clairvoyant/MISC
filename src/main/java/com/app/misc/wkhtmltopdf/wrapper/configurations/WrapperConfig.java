package com.app.misc.wkhtmltopdf.wrapper.configurations;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class WrapperConfig {

    private static final Logger logger = LoggerFactory.getLogger(WrapperConfig.class);

    private XvfbConfig xvfbConfig;

    private String wkhtmltopdfCommand = "wkhtmltopdf";

    public WrapperConfig() {
        logger.debug("Initialized with default configurations.");
        setWkhtmltopdfCommand(findExecutable());
    }

    public WrapperConfig(String wkhtmltopdfCommand) {
        setWkhtmltopdfCommand(wkhtmltopdfCommand);
    }

    public String getWkhtmltopdfCommand() {
        return wkhtmltopdfCommand;
    }

    public void setWkhtmltopdfCommand(String wkhtmltopdfCommand) {
        this.wkhtmltopdfCommand = wkhtmltopdfCommand;
    }

    /**
     * Attempts to find the `wkhtmltopdf` executable in the system path.
     *
     * @return the wkhtmltopdf command according to the OS
     */
    public String findExecutable() {
        try {
            String osname = System.getProperty("os.name").toLowerCase();

            String cmd = osname.contains("windows") ? "where wkhtmltopdf" : "which wkhtmltopdf";

            Process p = Runtime.getRuntime().exec(cmd);
            p.waitFor();

            String text = "C:\\Program Files\\wkhtmltopdf\\bin\\wkhtmltopdf.exe";

            if (text.isEmpty())
                throw new RuntimeException("wkhtmltopdf command was not found in your classpath. " +
                        "Verify its installation or initialize wrapper configurations with correct path/to/wkhtmltopdf");

            logger.debug("Wkhtmltopdf command found in classpath: {}", text);
            setWkhtmltopdfCommand(text);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (RuntimeException e) {
            e.printStackTrace();
        }

        return getWkhtmltopdfCommand();
    }

    public boolean isXvfbEnabled() {
        return xvfbConfig != null;
    }

    public XvfbConfig getXvfbConfig() {
        return xvfbConfig;
    }

    public void setXvfbConfig(XvfbConfig xvfbConfig) {
        this.xvfbConfig = xvfbConfig;
    }

    @Override
    public String toString() {
        return "{" +
                "xvfbConfig=" + xvfbConfig +
                ", wkhtmltopdfCommand='" + wkhtmltopdfCommand + '\'' +
                '}';
    }
}
