package com.crazygis.web.sitemesh;

import org.sitemesh.builder.SiteMeshFilterBuilder;
import org.sitemesh.config.ConfigurableSiteMeshFilter;

/**
 * Created by William on 2014/10/14.
 */
public class SevenConfigurableSiteMeshFilter extends ConfigurableSiteMeshFilter {
    public SevenConfigurableSiteMeshFilter() {
        super();
    }

    @Override
    protected void applyCustomConfiguration(SiteMeshFilterBuilder builder) {
        builder.setCustomContentProcessor(new SevenContentProcessor());
    }
}
