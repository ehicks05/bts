package net.ehicks.bts.model

abstract class SearchForm {
    open var id = 0L
    abstract var sortColumn: String
    abstract var sortDirection: String
    var page = "1"
    abstract val endpoint: String
}