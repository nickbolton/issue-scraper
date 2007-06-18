package net.unicon.issueScraper;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import org.w3c.tidy.Tidy;

public class JTidyInterceptor implements IStreamInterceptor {

    private String name;
    private boolean quiet = true;
    
    public InputStream intercept(InputStream is) {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        Tidy tidy = new Tidy();
        tidy.setXHTML(true);
        tidy.parse(is, os);
        tidy.setQuiet(quiet);
        return new ByteArrayInputStream(os.toByteArray());
    }

    public boolean isQuiet() {
        return quiet;
    }

    public void setQuiet(boolean quiet) {
        this.quiet = quiet;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
