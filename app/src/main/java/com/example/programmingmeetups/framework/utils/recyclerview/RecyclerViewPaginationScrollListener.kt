package com.example.programmingmeetups.framework.utils.recyclerview

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class RecyclerViewPaginationScrollListener(
    private val adapter: RecyclerView.Adapter<*>,
    private val paginationCallback: () -> Unit
) :
    RecyclerView.OnScrollListener() {
    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        super.onScrollStateChanged(recyclerView, newState)
        val layoutManager = recyclerView.layoutManager as LinearLayoutManager
        val lastVisibleItem = layoutManager.findLastVisibleItemPosition()
        if (lastVisibleItem == adapter.itemCount || lastVisibleItem == adapter.itemCount - 1) {
            paginationCallback()
        }
    }
}