package net.ehicks.bts.handlers;

import net.ehicks.bts.routing.Route;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.ParseException;
import java.util.Enumeration;

public class DebugHandler
{
    @Route(tab1 = "main", tab2 = "", tab3 = "", action = "debug")
    public static void getDebugInfo(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException
    {
        String info = "session:";

        HttpSession session = request.getSession();
        Enumeration<String> attrNames = session.getAttributeNames();

        while (attrNames.hasMoreElements())
        {
            String attrName = attrNames.nextElement();
            Object attrValue = session.getAttribute(attrName);

            info += "\r\n" + attrName + ": " + attrValue.toString();
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
