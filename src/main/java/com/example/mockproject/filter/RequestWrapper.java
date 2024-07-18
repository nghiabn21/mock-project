package com.example.mockproject.filter;

import com.example.mockproject.utils.exception.ApiRequestException;
import com.example.mockproject.utils.exception.ServiceException;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.httpclient.util.URIUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.*;
import java.io.*;
import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class RequestWrapper extends HttpServletRequestWrapper {


    Logger logger;
    Principal awesomePrincipal = null;
    Map parameters;
    byte[] postData = null;

    public RequestWrapper(HttpServletRequest request) {
        super(request);
        logger = Logger.getLogger(this.getClass());
        parameters = new HashMap();
    }

    public Principal getUserPrincipal() {
        if (awesomePrincipal == null) {
            return super.getUserPrincipal();
        }
        return awesomePrincipal;
    }

    public String getRemoteUser() {
        if (awesomePrincipal == null) {
            return super.getRemoteUser();
        }
        return awesomePrincipal.getName();
    }

    void setUserPrincipal(Principal p) {
        awesomePrincipal = p;
    }

    @Override
    public String getParameter(String name) {
        String value = super.getParameter(name);
        if (value == null) {
            value = (String) parameters.get(name);
        }
        return value;
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        logger.trace("called getInputStream");
        if (postData == null) {
            postData = IOUtils.toByteArray(super.getInputStream());
            parameters = getQueryMap(new String(postData));
        }
        logger.trace("post data read, parsed and cached: " + new String(postData));
        return new ServletInputStreamWrapper(new ByteArrayInputStream(postData));
    }


    public Map<String, String> getQueryMap(String query) {
        String[] params = query.split("&");
        Map<String, String> map = new HashMap<>();
        for (String param : params) {
            try {
                String name = URIUtil.decode(param.split("=")[0]);
                String value = URIUtil.decode(param.split("=")[1]);
                map.put(name, value);
            } catch (Exception e) {
                //   logger.error("Cannot decode request parameter: " + e.getMessage());
            }
        }
        return map;
    }
}