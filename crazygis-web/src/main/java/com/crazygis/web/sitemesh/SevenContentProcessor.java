package com.crazygis.web.sitemesh;

import org.sitemesh.SiteMeshContext;
import org.sitemesh.content.Content;
import org.sitemesh.content.tagrules.TagBasedContentProcessor;
import org.sitemesh.content.tagrules.TagRuleBundle;
import org.sitemesh.content.tagrules.decorate.DecoratorTagRuleBundle;
import org.sitemesh.content.tagrules.html.CoreHtmlTagRuleBundle;
import org.sitemesh.content.tagrules.html.DivExtractingTagRuleBundle;
import org.sitemesh.webapp.WebAppContext;

import java.io.IOException;
import java.nio.CharBuffer;

/**
 * Created by William on 2014/10/14.
 */
public class SevenContentProcessor extends TagBasedContentProcessor {

    public SevenContentProcessor(){
        this(new CoreHtmlTagRuleBundle(), new DecoratorTagRuleBundle(), new ExSectionTagRuleBundle(), new DivExtractingTagRuleBundle());
    }

    public SevenContentProcessor(TagRuleBundle... tagRuleBundles) {
        super(tagRuleBundles);
    }

    @Override
    public Content build(CharBuffer data, SiteMeshContext siteMeshContext) throws IOException {

        WebAppContext appContext = (WebAppContext)siteMeshContext;
        if(appContext.getRequest().getAttribute("exception") != null){
            return null;
        }

        if(appContext.getRequest().getHeader("X-Requested-With") != null){
            if(appContext.getRequest().getHeader("X-Requested-With").equals("XMLHttpRequest")){
                //"XMLHttpRequest".equals(request.getHeader("X-Requested-With"));)
                return null;
            }
        }


        return super.build(data, siteMeshContext);
    }
}
