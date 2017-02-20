package net.ehicks.bts;

import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.*;

public class RouteLogic
{
    private static final Logger log = LoggerFactory.getLogger(RouteLogic.class);

    private static Map<RouteDescription, Method> routeMap = new HashMap<>();

    public static Map<RouteDescription, Method> getRouteMap()
    {
        return routeMap;
    }

    public static void loadRoutes()
    {
        Reflections reflections = new Reflections("net.ehicks.bts", new MethodAnnotationsScanner());

        Set<Method> handlers = reflections.getMethodsAnnotatedWith(net.ehicks.bts.Route.class);
        // include handlers with multiple routes
        handlers.addAll(reflections.getMethodsAnnotatedWith(net.ehicks.bts.Routes.class));

        for (Method handler : handlers)
        {
            net.ehicks.bts.Route[] routes = handler.getAnnotationsByType(net.ehicks.bts.Route.class);
            for (Route route : routes)
            {
                RouteDescription routeDescription = new RouteDescription(route.tab1(), route.tab2(), route.tab3(), route.action());

                if (routeMap.containsKey(routeDescription))
                    log.error("Found duplicate route description: " + routeDescription);

                routeMap.put(routeDescription, handler);
            }
        }
    }
}
