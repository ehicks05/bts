package net.ehicks.bts;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class GlobalInterceptor extends HandlerInterceptorAdapter
{
    private GlobalDataLoader globalDataLoader;

    public GlobalInterceptor(GlobalDataLoader globalDataLoader)
    {
        this.globalDataLoader = globalDataLoader;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception
    {
        modelAndView.addAllObjects(globalDataLoader.loadData());

        super.postHandle(request, response, handler, modelAndView);
    }
}
