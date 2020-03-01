package net.ehicks.bts.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class SearchResult<T> implements Serializable
{
    private List<T> searchResults;
    private long size;
    private long resultsPerPage;
    private String page = "1";

    public SearchResult(List<T> searchResults, long size, long resultsPerPage, String pageParam)
    {
        this.searchResults = searchResults;
        this.size = size;
        this.resultsPerPage = resultsPerPage;
        this.page = pageParam;
    }

    // Derived values
    public long getPages()
    {
        return 1 + ((size - 1) / resultsPerPage);
    }

    public List<String> getNavPages()
    {
        int count = 0;
        int currentPage = Integer.parseInt(page);
        List<Integer> navPages = new ArrayList<>();
        int delta = 0;

        while (count <= getPages() && count <= 6)
        {
            int possiblePage1 = currentPage - delta;
            int possiblePage2 = currentPage + delta;

            if (possiblePage1 > 0 && possiblePage1 <= getPages() && !navPages.contains(possiblePage1))
            {
                navPages.add(possiblePage1);
                count++;
            }
            if (possiblePage2 > 0 && possiblePage2 <= getPages() && !navPages.contains(possiblePage2))
            {
                navPages.add(possiblePage2);
                count++;
            }

            if (navPages.size() == getPages())
                break;

            delta++;
        }

        Collections.sort(navPages);
        return navPages.stream().map(String::valueOf).collect(Collectors.toList());
    }

    public boolean isHasNext()
    {
        return getPages() > Integer.parseInt(page);
    }

    public boolean isHasPrevious()
    {
        return Integer.parseInt(page) > 1;
    }

    // -------- Getter / Setter --------
    public List<T> getSearchResults()
    {
        return searchResults;
    }

    public void setSearchResults(List<T> searchResults)
    {
        this.searchResults = searchResults;
    }

    public long getSize()
    {
        return size;
    }

    public void setSize(long size)
    {
        this.size = size;
    }

    public long getResultsPerPage()
    {
        return resultsPerPage;
    }

    public void setResultsPerPage(long resultsPerPage)
    {
        this.resultsPerPage = resultsPerPage;
    }

    public String getPage()
    {
        return page;
    }

    public void setPage(String page)
    {
        this.page = page;
    }
}
