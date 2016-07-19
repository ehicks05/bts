package com.hicks;

import com.hicks.beans.Issue;

import java.util.List;

public class SearchResult
{
    private List<Issue> searchResults;
    private long size;
    private long resultsPerPage;
    private String page;
    private String sortColumn;
    private String sortDirection;

    public SearchResult(String pageParam, List<Issue> searchResults, String sortColumn, String sortDirection, long size, long resultsPerPage)
    {
        this.page = pageParam;
        this.searchResults = searchResults;
        this.sortColumn = sortColumn;
        this.sortDirection = sortDirection;
        this.size = size;
        this.resultsPerPage = resultsPerPage;
    }

    // Derived values
    public long getPages()
    {
        return 1 + ((size - 1) / resultsPerPage);
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

    public String getSortColumn()
    {
        return sortColumn;
    }

    public void setSortColumn(String sortColumn)
    {
        this.sortColumn = sortColumn;
    }

    public String getSortDirection()
    {
        return sortDirection;
    }

    public void setSortDirection(String sortDirection)
    {
        this.sortDirection = sortDirection;
    }
}
