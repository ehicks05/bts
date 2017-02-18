package net.ehicks.bts.handlers;

import net.ehicks.bts.SearchResult;
import net.ehicks.bts.beans.IssueForm;
import net.ehicks.eoi.EOICache;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.ParseException;
import java.util.Enumeration;

public class DebugHandler
{
    public static void getDebugInfo(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException
    {
        long maxMemory = Runtime.getRuntime().maxMemory() ;
        long allocatedMemory = (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory());
        long presumableFreeMemory = (maxMemory - allocatedMemory);

        String info = "";
        info += String.format(    "%-20s: %4d", "Free Memory", presumableFreeMemory / 1024 / 1024);
        info += String.format("\r\n%-20s: %4d", "Allocated Memory", allocatedMemory / 1024 / 1024);
        info += String.format("\r\n%-20s: %4d", "Max Memory", maxMemory / 1024 / 1024);

        info += "\r\n['" +
                "cacheSize:" + EOICache.cache.keySet().size() +
                "','" + "hit:" + EOICache.hits.toString() +
                "','" + "miss:" + EOICache.misses.toString() +
                "','" + "keyHitObjectMiss:" + EOICache.keyHitObjectMiss.toString() +
                "','" + "keysWithNoValue:" + EOICache.getKeysWithNoValue() +
                "']";

        HttpSession session = request.getSession();
        Enumeration<String> attrNames = session.getAttributeNames();

        while (attrNames.hasMoreElements())
        {
            String attrName = attrNames.nextElement();
            Object attrValue = session.getAttribute(attrName);

            if (attrValue instanceof IssueForm)
            {
                IssueForm issueForm = (IssueForm) attrValue;
            }
            if (attrValue instanceof SearchResult)
            {
                SearchResult searchResult = (SearchResult) attrValue;

                int i = 1;
                for (Object issue : searchResult.getSearchResults())
                {
                    info += "\r\n" + i++ + ":" + issue.toString();
                }
            }

            info += "\r\n" + attrValue.toString();
        }

        info += "\r\nHttpServletRequest.getLocalAddr() :" + request.getLocalAddr()   ;
        info += "\r\nHttpServletRequest.getLocalName() :" + request.getLocalName()   ;
        info += "\r\nHttpServletRequest.getServerName():" + request.getServerName()  ;
        info += "\r\nHtppServletRequest.getLocalPort() :" + request.getLocalPort()   ;
        info += "\r\nHttpServletRequest.getServerPort():" + request.getServerPort()  ;
        info += "\r\nHttpServletRequest.getContextPath():" + request.getContextPath();

        ServletOutputStream outputStream = response.getOutputStream();
        outputStream.print(info);
    }
}
