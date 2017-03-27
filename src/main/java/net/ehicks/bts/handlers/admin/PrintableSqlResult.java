package net.ehicks.bts.handlers.admin;

import java.util.List;

public class PrintableSqlResult
{
    private String sqlCommand;
    private List<Object> columnLabels;
    private List<Object> resultRows;
    private Integer rowsUpdated;
    private String error;

    public PrintableSqlResult(String sqlCommand)
    {
        this.sqlCommand = sqlCommand;
    }

    public String getSqlCommand()
    {
        return sqlCommand;
    }

    public void setSqlCommand(String sqlCommand)
    {
        this.sqlCommand = sqlCommand;
    }

    public List<Object> getColumnLabels()
    {
        return columnLabels;
    }

    public void setColumnLabels(List<Object> columnLabels)
    {
        this.columnLabels = columnLabels;
    }

    public List<Object> getResultRows()
    {
        return resultRows;
    }

    public void setResultRows(List<Object> resultRows)
    {
        this.resultRows = resultRows;
    }

    public Integer getRowsUpdated()
    {
        return rowsUpdated;
    }

    public void setRowsUpdated(Integer rowsUpdated)
    {
        this.rowsUpdated = rowsUpdated;
    }

    public String getError()
    {
        return error;
    }

    public void setError(String error)
    {
        this.error = error;
    }
}
