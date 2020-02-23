package net.ehicks.bts;

import net.ehicks.bts.beans.BtsSystemRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import java.io.IOException;

@Component
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
        try {
            req.setAttribute("theme", btsSystemRepository.findFirstBy().getTheme());
            req.setAttribute("siteName", btsSystemRepository.findFirstBy().getInstanceName());
            chain.doFilter(req, resp);
        } finally {
        }
    }

    @Override
    public void destroy() {
        // empty
    }
}