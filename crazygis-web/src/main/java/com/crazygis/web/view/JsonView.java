package com.crazygis.web.view;

import com.crazygis.web.utils.WebUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.web.servlet.view.AbstractView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Map;

/**
 * Created by William on 2014/11/16.
 */
public class JsonView extends AbstractView {

    public static final String DEFAULT_CONTENT_TYPE = "application/json";
    public static final String HTML_CONTENT_TYPE = "text/html";
    public static final String DEFAULT_CHAR_ENCODING = "UTF-8";

    private String encoding = DEFAULT_CHAR_ENCODING;
    private Object jsonData = null;

    public JsonView() {
        setContentType(DEFAULT_CONTENT_TYPE);

    }
    public JsonView(Object data) {
        setContentType(DEFAULT_CONTENT_TYPE);
        this.jsonData  =data;
    }



    @Override
    protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request,
                                           HttpServletResponse response) throws Exception {
        response.setCharacterEncoding(this.encoding);
        response.setContentType(getContentType());
        PrintWriter out = response.getWriter();

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ"));

        try {
            String jsonResult = objectMapper.writeValueAsString(this.jsonData);

            //进行HTML转码
            out.print(WebUtils.HTMLEncode(jsonResult));

        }catch (Exception e){
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print(e.getMessage());
        }
    }
}
