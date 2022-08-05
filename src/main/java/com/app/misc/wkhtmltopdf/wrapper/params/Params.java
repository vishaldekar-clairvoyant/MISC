package com.app.misc.wkhtmltopdf.wrapper.params;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class Params {

    private Collection<Param> params;

    public Params() {
        this.params = new ArrayList<Param>();
    }

    public void add(Param param, Param... params) {
        this.params.add(param);
        this.params.addAll( Arrays.asList( params ) );
    }

    public List<String> getParamsAsStringList() {
        List<String> commandLine = new ArrayList<String>();

        for (Param p : params) {
            commandLine.add(p.getKey());

            for (String value : p.getValues()) {
                if (value != null) {
                    commandLine.add(value);
                }
            }
        }

        return commandLine;
    }

    @Override
    public String toString() {
      return StringUtils.join(params, "");
    }

}
