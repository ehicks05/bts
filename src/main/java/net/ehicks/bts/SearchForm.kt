package net.ehicks.bts

abstract class SearchForm {
    // -------- Getters / Setters ----------
    open var id = 0L
    var sortColumn = "id"
    var sortDirection = "asc"
    var page = "1"
    abstract val endpoint: String
}