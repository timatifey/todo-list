package dev.timatifey.todo_list.screens.common

import android.content.Context
import android.view.MotionEvent
import android.view.VelocityTracker
import android.view.View
import android.view.ViewConfiguration
import androidx.dynamicanimation.animation.DynamicAnimation
import androidx.dynamicanimation.animation.FlingAnimation
import androidx.dynamicanimation.animation.SpringAnimation
import androidx.dynamicanimation.animation.SpringForce
import androidx.recyclerview.widget.RecyclerView
import kotlin.properties.Delegates

class ItemSwipeManager(
    context: Context,
    private val listener: SwipeListener
) : RecyclerView.OnItemTouchListener, RecyclerView.OnChildAttachStateChangeListener {

    private val animations = mutableMapOf<RecyclerView.ViewHolder, DynamicAnimation<*>>()
    private var touchSlop by Delegates.notNull<Int>()
    private var initialTouchX = 0f
    private var recyclerView: RecyclerView? = null
    private var velocityTracker: VelocityTracker? = null
    private var swipedChild: View? = null

    init {
        touchSlop = ViewConfiguration.get(context).scaledTouchSlop
    }

    fun attachToRecyclerView(recyclerView: RecyclerView) {
        this.recyclerView = recyclerView
        recyclerView.addOnItemTouchListener(this)
        recyclerView.addOnChildAttachStateChangeListener(this)
    }

    fun detachFromRecyclerView() {
        recyclerView?.removeOnItemTouchListener(this)
        recyclerView?.removeOnChildAttachStateChangeListener(this)
        for (animation in animations.values) {
            animation.cancel()
        }
        animations.clear()
        recyclerView = null
    }

    private fun animateWithFling(viewHolder: RecyclerView.ViewHolder, velocity: Float) {
        val animation = FlingAnimation(swipedChild, DynamicAnimation.TRANSLATION_X)
        animation.friction = 1f
        animation.setStartVelocity(velocity)
        animation.setMaxValue(swipedChild!!.width.toFloat())
        viewHolder.setIsRecyclable(false)
        animation.addEndListener { _, _, value, currentVelocity ->
            if (value >= recyclerView!!.width) {
                viewHolder.setIsRecyclable(true)
                listener.onSwiped(viewHolder)
            } else {
                animateWithSpring(viewHolder, currentVelocity)
            }
        }
        animations[viewHolder] = animation
        animation.start()
    }

    private fun animateWithSpring(viewHolder: RecyclerView.ViewHolder, velocity: Float) {
        val animation = SpringAnimation(viewHolder.itemView, DynamicAnimation.TRANSLATION_X)
        animation.setStartVelocity(velocity)
        val springForce = SpringForce(0f)
        springForce.dampingRatio = SpringForce.DAMPING_RATIO_LOW_BOUNCY
        springForce.stiffness = SpringForce.STIFFNESS_LOW
        animation.spring = springForce
        viewHolder.setIsRecyclable(false)
        animation.addEndListener { _, _, _, _ ->
            animations.remove(viewHolder)
            viewHolder.setIsRecyclable(true)
        }
        animations[viewHolder] = animation
        animation.start()
    }

    override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent) =
        when (e.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                initialTouchX = e.x
                velocityTracker?.recycle()
                velocityTracker = VelocityTracker.obtain()
                velocityTracker?.addMovement(e)
                false
            }
            MotionEvent.ACTION_MOVE -> {
                velocityTracker!!.addMovement(e)
                val dragged = e.x - initialTouchX > touchSlop
                if (dragged) {
                    swipedChild = rv.findChildViewUnder(e.x, e.y)
                }
                dragged
            }
            else -> false
        }

    override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {
        if (swipedChild == null) {
            return
        }
        velocityTracker!!.addMovement(e)
        when (e.actionMasked) {
            MotionEvent.ACTION_MOVE -> swipedChild!!.translationX = e.x - initialTouchX
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                val swipeViewHolder = rv.findContainingViewHolder(swipedChild!!) ?: return
                velocityTracker!!.computeCurrentVelocity(1000)
                val velocity = velocityTracker!!.xVelocity
                if (velocity > 0) {
                    animateWithFling(swipeViewHolder, velocity)
                } else {
                    animateWithSpring(swipeViewHolder, velocity)
                }
                velocityTracker!!.clear()
            }
        }
    }

    override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {
    }

    override fun onChildViewAttachedToWindow(view: View) {
    }

    override fun onChildViewDetachedFromWindow(view: View) {
        view.translationX = 0f
        val holder = recyclerView!!.getChildViewHolder(view) ?: return
        animations.remove(holder)
    }

    interface SwipeListener {
        fun onSwiped(viewHolder: RecyclerView.ViewHolder)
    }
}