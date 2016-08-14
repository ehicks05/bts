package com.hicks;

import com.hicks.beans.Issue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SearchResult
{
    private List<Issue> searchResults;
    private long size;
    private long resultsPerPage;
    private String page;

    public SearchResult(String pageParam, List<Issue> searchResults, long size, long resultsPerPage)
    {
        this.page = pageParam;
        this.searchResults = searchResults;
        this.size = size;
        this.resultsPerPage = resultsPerPage;
    }

    // Derived values
    public long getPages()
    {
        return 1 + ((size - 1) / resultsPerPage);
    }

    public List<Integer> getNavPages()
    {
        int count = 0;
        int currentPage = Integer.valueOf(page);
        List<Integer> navPages = new ArrayList<>();
        int delta = 0;

        while (count <= getPages() && count <= 10)
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
            {
                Collections.sort(navPages);
                return navPages;
            }

            delta++;
        }

        return null;
    }

    public boolean isHasNext()
    {
        return getPages() > Integer.valueOf(page);
    }

    public boolean isHasPrevious()
    {
        return Integer.valueOf(page) > 1;
    }


    // -------- Getter / Setter --------
    public List<Issue> getSearchResults()
    {
        return searchResults;
    }

    public void setSearchResults(List<Issue> searchResults)
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
