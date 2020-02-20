//package net.ehicks.bts.handlers.admin;
//
//import net.ehicks.bts.SystemInfo;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.ResponseBody;
//import org.springframework.web.servlet.ModelAndView;
//
//import java.io.File;
//import java.io.IOException;
//import java.nio.charset.Charset;
//import java.nio.file.Files;
//import java.util.*;
//
//@Controller
//public class LogHandler
//{
//    @GetMapping("/admin/logs/form")
//    public ModelAndView showLogs()
//    {
//        File logsDir = new File(SystemInfo.INSTANCE.getLogDirectory());
//        File[] logsDirListing = logsDir.listFiles();
//        List<File> logs = logsDirListing == null ? new ArrayList<>()
//                : new ArrayList<>(Arrays.asList(logsDirListing));
//
//        logs.removeIf(file -> !file.getName().contains("bts"));
//        logs.sort((o1, o2) -> Long.compare(o2.lastModified(), o1.lastModified()));
//
//        return new ModelAndView("/webroot/admin/logs.jsp")
//                .addObject("logs", logs);
//    }
//
//    @GetMapping("/admin/logs/delete")
//    public ModelAndView deleteLog(@RequestParam String logName)
//    {
//        new File(SystemInfo.INSTANCE.getLogDirectory() + logName).delete();
//        return new ModelAndView("redirect:view?tab1=admin/logs/form");
//    }
//
//    @GetMapping("/admin/logs/viewLog")
//    @ResponseBody
//    public File viewLog(@RequestParam String logName)
//    {
//        return new File(SystemInfo.INSTANCE.getLogDirectory() + logName);
//    }
//
//    @GetMapping("/admin/logs/viewLogPretty")
//    public ModelAndView viewLogPretty(@RequestParam String logName) throws IOException
//    {
//        File file = new File(SystemInfo.INSTANCE.getLogDirectory() + logName);
//
//        Map<String, String> threadToColorMap = new HashMap<>();
//        List<List<String>> lines = new ArrayList<>();
//        for (String line : Files.readAllLines(file.toPath(), Charset.defaultCharset()))
//        {
//            try
//            {
//                parseLine(lines, line, threadToColorMap);
//            }
//            catch (Exception e)
//            {
//                parseLineRaw(lines, line);
//            }
//        }
//
//        return new ModelAndView("/webroot/admin/viewLog.jsp")
//                .addObject("lines", lines)
//                .addObject("logName", logName)
//                .addObject("threadToColorMap", threadToColorMap);
//    }
//
//    private void parseLine(List<List<String>> lines, String line, Map<String, String> threadToColorMap)
//    {
//        int dateLength = 15;
//        String date = line.substring(0, dateLength);
//        line = line.substring(dateLength + 1);
//
//        int openCount = 1;
//        int closeIndex = 1;
//        while (openCount > 0)
//        {
//            if (line.charAt(closeIndex) == ']')
//                openCount--;
//            if (line.charAt(closeIndex) == '[')
//                openCount++;
//            closeIndex++;
//        }
//        String thread = line.substring(0, closeIndex + 1);
//        String threadWithoutBrackets = thread.substring(1, thread.length() - 2);
//        if (threadWithoutBrackets.length() > 64)
//            threadWithoutBrackets = threadWithoutBrackets.substring(0, 64) + "...";
//        threadToColorMap.putIfAbsent(threadWithoutBrackets, getColorFromThread(threadWithoutBrackets));
//
//        line = line.substring(closeIndex + 1);
//        String level = line.substring(0, line.indexOf(" "));
//
//        line = line.substring(line.indexOf(" ") + 2);
//        String myClass = line.substring(0, line.indexOf(" "));
//        String classWithoutPackage = myClass.substring(myClass.lastIndexOf(".") + 1);
//
//        line = line.substring(line.indexOf(" ") + 4);
//        String message = line;
//
//        lines.add(Arrays.asList(date, threadWithoutBrackets, level, classWithoutPackage, message));
//    }
//
//    private String getColorFromThread(String thread)
//    {
//        String hash = String.valueOf(thread.hashCode() * 64);
//        int r = 0;
//        int g = 0;
//        int b = 0;
//        for (char aChar : hash.toCharArray())
//        {
//            if (hash.indexOf(aChar) % 3 == 0)
//                r += aChar;
//            if (hash.indexOf(aChar) % 3 == 1)
//                g += aChar;
//            if (hash.indexOf(aChar) % 3 == 2)
//                b += aChar;
//        }
//        r %= 255;
//        g %= 255;
//        b %= 255;
//        r /= 3;
//        g /= 3;
//        b /= 3;
//        r = 255 - r;
//        g = 255 - g;
//        b = 255 - b;
//        return String.format("#%02x%02x%02x", r, g, b);
//    }
//
//    private void parseLineRaw(List<List<String>> lines, String line)
//    {
//        lines.add(Arrays.asList("", "", "", "", line));
//    }
//}
