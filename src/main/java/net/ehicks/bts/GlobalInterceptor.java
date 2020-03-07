package net.ehicks.bts;

import net.ehicks.bts.beans.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.UUID;

public class GlobalInterceptor extends HandlerInterceptorAdapter
{
    private static final Logger log = LoggerFactory.getLogger(GlobalInterceptor.class);

    private GlobalDataLoader globalDataLoader;
    private RequestStatsRepository requestStatsRepository;
    private Long logRequestsSlowerThanMs;

    public GlobalInterceptor(GlobalDataLoader globalDataLoader, RequestStatsRepository requestStatsRepository, Long logRequestsSlowerThanMs)
    {
        this.globalDataLoader = globalDataLoader;
        this.requestStatsRepository = requestStatsRepository;
        this.logRequestsSlowerThanMs = logRequestsSlowerThanMs;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception
    {
        request.setAttribute("requestStart", System.nanoTime());
        request.setAttribute("requestStartDate", new Date());
        request.setAttribute("requestId", UUID.randomUUID().toString());
        return super.preHandle(request, response, handler);
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception
    {
        request.setAttribute("handleTime", (System.nanoTime() - (long) request.getAttribute("requestStart")) / 1_000_000);
        long start = System.nanoTime();
        if (modelAndView != null)
            modelAndView.addAllObjects(globalDataLoader.loadData());
        request.setAttribute("postHandleTime", (System.nanoTime() - start) / 1_000_000);

        request.setAttribute("templateStart", System.nanoTime());
        super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception
    {
        long templateStart = request.getAttribute("handleTime") != null ? (long) request.getAttribute("templateStart") : 0;
        request.setAttribute("templateTime", (System.nanoTime() - templateStart) / 1_000_000);
        super.afterCompletion(request, response, handler, ex);

        String requestId = (String) request.getAttribute("requestId");
        long requestStart = (long) request.getAttribute("requestStart");
        Date requestStartDate = (Date) request.getAttribute("requestStartDate");
        long requestTime = (System.nanoTime() - requestStart) / 1_000_000;
        long handleTime = request.getAttribute("handleTime") != null ? (long) request.getAttribute("handleTime") : 0;
        long postHandleTime = request.getAttribute("postHandleTime") != null ? (long) request.getAttribute("postHandleTime") : 0;
        long templateTime = request.getAttribute("templateTime") != null ? (long) request.getAttribute("templateTime") : 0;
        HandlerMethod handlerMethod = ((HandlerMethod) handler);
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        try
        {
            requestStatsRepository.save(new RequestStats(requestId, requestStartDate, user.getUsername(),
                    handlerMethod.getShortLogMessage(), requestTime, handleTime, postHandleTime, templateTime));
        }
        catch (Exception e)
        {

        }

        if (requestTime > logRequestsSlowerThanMs)
        {
            log.info("  SlowRequest: " + requestTime + " ms. handler: " + handleTime + " ms. posthandle: " +
                    postHandleTime + " ms. template: "+ templateTime + " ms." + handlerMethod.getMethod().getName());
        }
    }
}
