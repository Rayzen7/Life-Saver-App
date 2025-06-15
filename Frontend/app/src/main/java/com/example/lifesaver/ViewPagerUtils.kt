import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2

fun ViewPager2.setCurrentItemWithDuration(item: Int, duration: Int) {
    val recyclerView = getChildAt(0) as RecyclerView
    val layoutManager = recyclerView.layoutManager as LinearLayoutManager

    val smoothScroller = object : LinearSmoothScroller(context) {
        override fun calculateTimeForScrolling(dx: Int): Int {
            return duration
        }
    }

    smoothScroller.targetPosition = item
    layoutManager.startSmoothScroll(smoothScroller)
}
