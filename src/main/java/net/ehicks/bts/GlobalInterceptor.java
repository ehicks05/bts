package net.ehicks.bts;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class GlobalInterceptor extends HandlerInterceptorAdapter
{
    private static final Logger log = LoggerFactory.getLogger(GlobalInterceptor.class);

    private GlobalDataLoader globalDataLoader;

    public GlobalInterceptor(GlobalDataLoader globalDataLoader)
    {
        this.globalDataLoader = globalDataLoader;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception
    {
        request.setAttribute("requestStart", System.nanoTime());
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
        request.setAttribute("templateTime", (System.nanoTime() - (long) request.getAttribute("templateStart")) / 1_000_000);
        super.afterCompletion(request, response, handler, ex);
        
        long requestTime = (System.nanoTime() - (long) request.getAttribute("requestStart")) / 1_000_000;
        if (requestTime > 100)
        {
            String methodName = ((HandlerMethod) handler).getMethod().getName();
            long handleTime = (long) request.getAttribute("handleTime");
            long postHandleTime = (long) request.getAttribute("postHandleTime");
            long templateTime = (long) request.getAttribute("templateTime");
            log.info("  SlowRequest: " + requestTime + " ms. handler: " + handleTime + " ms. posthandle: " + postHandleTime + " ms. template: "+ templateTime + " ms." + methodName);
        }
    }
}
