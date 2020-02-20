package net.ehicks.bts

abstract class SearchForm {
    open var id = 0L
    var sortColumn = "id"
    var sortDirection = "asc"
    var page = "1"
    abstract val endpoint: String
}