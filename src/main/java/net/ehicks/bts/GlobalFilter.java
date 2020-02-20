package net.ehicks.bts;

import java.io.IOException;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;

import net.ehicks.bts.beans.BtsSystemRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@WebFilter(urlPatterns = {"/dashboard/**", "/issue/**", "/admin/**", "/settings/**", "/profile/**"})
@Order(Ordered.HIGHEST_PRECEDENCE)
public class GlobalFilter implements Filter {
    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalFilter.class);

    BtsSystemRepository btsSystemRepository;

    public GlobalFilter(BtsSystemRepository btsSystemRepository)
    {
        this.btsSystemRepository = btsSystemRepository;
    }

    @Override
    public void init(FilterConfig filterConfig) {
        // empty
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
        long time = System.currentTimeMillis();
        try {
            req.setAttribute("requestStartTime", time);
            req.setAttribute("theme", btsSystemRepository.findFirstBy().getTheme());
            chain.doFilter(req, resp);
        } finally {
            time = System.currentTimeMillis() - time;
            LOGGER.trace("{}: {} ms ", ((HttpServletRequest) req).getRequestURI(),  time);
        }
    }

    @Override
    public void destroy() {
        // empty
    }
}