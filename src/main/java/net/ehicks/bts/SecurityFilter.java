package net.ehicks.bts;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(urlPatterns = "/*", filterName="Security Filter")
public class SecurityFilter implements Filter
{
    @Override
    public void init(FilterConfig filterConfig) throws ServletException
    {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
    {
        try
        {
            HttpServletRequest  request  = (HttpServletRequest) servletRequest;
            HttpServletResponse response = (HttpServletResponse) servletResponse;

            boolean isResource = false;
            String servletPath = request.getServletPath();

            if (servletPath.startsWith("/signUp"))     isResource = true;
            if (servletPath.startsWith("/images/"))     isResource = true;
            if (servletPath.startsWith("/styles/"))     isResource = true;
            if (servletPath.endsWith(".css"))           isResource = true;
            if (servletPath.endsWith(".ttf"))           isResource = true;
            if (servletPath.endsWith(".jpg"))           isResource = true;
            if (servletPath.endsWith(".gif"))           isResource = true;
            if (servletPath.endsWith(".png"))           isResource = true;
            if (servletPath.endsWith(".js"))            isResource = true;
            if (servletPath.endsWith(".html"))            isResource = true;

            if (!isResource)
            {
                if (!servletPath.startsWith("/view"))
                {
                    response.sendRedirect("view?action=form");
                    return;
                }

                filterChain.doFilter(request, response);
            }
            else
            {
                // Deal with resources such as /images and /styles
                filterChain.doFilter(request, response);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void destroy()
    {
    }
}
