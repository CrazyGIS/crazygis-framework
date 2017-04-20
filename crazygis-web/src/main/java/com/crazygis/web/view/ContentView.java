package com.crazygis.web.view;

import org.springframework.web.servlet.view.AbstractView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Map;

/**
 * Created by 周松 on 2015/8/19.
 */
public class ContentView extends AbstractView {
    String encoding;
    String contentText;
    public static final String HTML_CONTENT_TYPE = "text/html";

    public ContentView(String contentText) {
        this(contentText, "utf-8");
    }

    public ContentView(String contentText, String encoding) {
        this.contentText = contentText;
        this.encoding = encoding;
        setContentType(HTML_CONTENT_TYPE);
    }

    @Override
    protected void renderMergedOutputModel(Map<String, Object> map, HttpServletRequest request, HttpServletResponse response) throws Exception {
        PrintWriter out = response.getWriter();

        response.setCharacterEncoding(this.encoding);
        response.setContentType(getContentType());
        try {
            out.print(contentText);

        }catch (Exception e){
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print(e.getMessage());
        }
    }
}
