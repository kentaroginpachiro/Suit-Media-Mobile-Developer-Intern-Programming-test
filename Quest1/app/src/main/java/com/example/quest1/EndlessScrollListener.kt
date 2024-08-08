import android.widget.AbsListView

abstract class EndlessScrollListener : AbsListView.OnScrollListener {

    private var previousTotalItemCount = 0
    private var loading = true

    override fun onScroll(view: AbsListView?, firstVisibleItem: Int, visibleItemCount: Int, totalItemCount: Int) {
        if (loading && totalItemCount > previousTotalItemCount) {
            loading = false
            previousTotalItemCount = totalItemCount
        }

        if (!loading && (firstVisibleItem + visibleItemCount >= totalItemCount)) {
            onLoadMore()
            loading = true
        }
    }

    override fun onScrollStateChanged(view: AbsListView?, scrollState: Int) {}

    abstract fun onLoadMore()
}
