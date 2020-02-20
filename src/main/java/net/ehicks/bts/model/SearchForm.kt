package net.ehicks.bts.model

abstract class SearchForm {
    open var id = 0L
    var sortColumn = "id"
    var sortDirection = "asc"
    var page = "1"
    abstract val endpoint: String
}