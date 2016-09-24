/**
 * Ajax to get new search results.
 * @param {string} callingElementId - Where the results will be placed.
 * @param {string} issueFormId - The issue form to get results from.
 * @param {string} newPage - The page to get from the search result.
 * @param {string} newSortColumn - Sort column.
 * @param {string} newSortDirection - Sort direction.
 */
function ajaxItems(callingElementId, issueFormId, newPage, newSortColumn, newSortDirection)
{
    var myUrl = '${pageContext.request.contextPath}/view?tab1=main&tab2=search&action=ajaxGetPageOfResults';
    var params = {};
    if (issueFormId) params.issueFormId = issueFormId;
    if (newPage) params.page = newPage;
    if (newSortColumn) params.sortColumn = newSortColumn;
    if (newSortDirection) params.sortDirection = newSortDirection;

    $.get(myUrl, params,
        function(data, textStatus, xhr)
        {
            if(textStatus == "success")
            {
                var rows = [];

                $(callingElementId).closest('.tableContainer').html(data);
            }
            if (textStatus == "error")
                alert("Error: " + xhr.status + ": " + xhr.statusText);
        });
}