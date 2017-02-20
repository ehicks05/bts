package net.ehicks.bts.handlers.settings;

import net.ehicks.bts.CommonIO;
import net.ehicks.bts.PdfCreator;
import net.ehicks.bts.Route;
import net.ehicks.bts.UserSession;
import net.ehicks.bts.beans.Subscription;
import net.ehicks.common.Common;
import net.ehicks.eoi.EOI;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SubscriptionHandler
{
    @Route(tab1 = "settings", tab2 = "subscriptions", tab3 = "", action = "form")
    public static String showSubscriptions(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException
    {
        UserSession userSession = (UserSession) request.getSession().getAttribute("userSession");

        request.setAttribute("subscriptions", Subscription.getByUserId(userSession.getUserId()));

        return "/WEB-INF/webroot/subscriptions.jsp";
    }

    @Route(tab1 = "settings", tab2 = "subscriptions", tab3 = "", action = "add")
    public static void addSubscription(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        UserSession userSession = (UserSession) request.getSession().getAttribute("userSession");

        Subscription subscription = new Subscription();
        subscription.setUserId(userSession.getUserId());
        subscription = updateSubscriptionFromRequest(subscription, request);
        EOI.insert(subscription, userSession);

        response.sendRedirect("view?tab1=settings&tab2=subscriptions&action=form");
    }

    @Route(tab1 = "settings", tab2 = "subscriptions", tab3 = "", action = "delete")
    public static void deleteSubscription(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException
    {
        UserSession userSession = (UserSession) request.getSession().getAttribute("userSession");

        Long subscriptionId = Common.stringToLong(request.getParameter("subscriptionId"));
        Subscription subscription = Subscription.getById(subscriptionId);
        if (subscription != null)
            EOI.executeDelete(subscription, userSession);

        response.sendRedirect("view?tab1=settings&tab2=subscriptions&action=form");
    }

    @Route(tab1 = "settings", tab2 = "subscriptions", tab3 = "", action = "print")
    public static void printSubscriptions(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException
    {
        UserSession userSession = (UserSession) request.getSession().getAttribute("userSession");

        List<List> subscriptionData = new ArrayList<>();
        subscriptionData.add(Arrays.asList("Object Id", "User Id", "Description"));
        for (Subscription subscription : Subscription.getByUserId(userSession.getUserId()))
            subscriptionData.add(Arrays.asList(subscription.getId(), subscription.getUserId(), subscription.getDescription()));
        File file = PdfCreator.createPdf("Me", "Subscription Report", "", subscriptionData);

        CommonIO.sendFileInResponse(response, file, true);
    }

    private static Subscription updateSubscriptionFromRequest(Subscription subscription, HttpServletRequest request)
    {
        String statusIds        = Common.arrayToString(Common.getSafeStringArray(request.getParameterValues("statusIds")));
        String severityIds      = Common.arrayToString(Common.getSafeStringArray(request.getParameterValues("severityIds")));
        String projectIds       = Common.arrayToString(Common.getSafeStringArray(request.getParameterValues("projectIds")));
        String groupIds         = Common.arrayToString(Common.getSafeStringArray(request.getParameterValues("groupIds")));
        String assigneeIds      = Common.arrayToString(Common.getSafeStringArray(request.getParameterValues("assigneeIds")));

        UserSession userSession = (UserSession) request.getSession().getAttribute("userSession");

        subscription.updateFields(userSession.getUserId(), statusIds, severityIds, projectIds, groupIds, assigneeIds);

        return subscription;
    }
}
