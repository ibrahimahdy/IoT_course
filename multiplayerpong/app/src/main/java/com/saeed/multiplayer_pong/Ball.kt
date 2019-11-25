package com.saeed.multiplayer_pong

import android.graphics.RectF


/**
 * Created by Saeed on 23/11/2019.
 */

data class Velocity(var horizontal: Float = 0F, var vertical: Float = horizontal)

data class Dimen(var height: Float = 0F, var width: Float = height)

class Ball constructor(private val screenDimen: Dimen) {

    private val velocity: Velocity = Velocity()
    private val ballDimen: Dimen = Dimen()

    var rectF: RectF = RectF()

    init {
        ballDimen.width = screenDimen.width.div(100)
        ballDimen.height = ballDimen.width

        velocity.vertical = screenDimen.height.div(4)
        velocity.horizontal = velocity.vertical

        rectF = RectF()
    }

    public fun update(framesPerSecond: Long) {
        rectF.left = rectF.left.plus(velocity.horizontal.div(framesPerSecond))
        rectF.right = rectF.left.plus(ballDimen.width)
        rectF.top = rectF.top.plus(velocity.vertical.div(framesPerSecond))
        rectF.bottom = rectF.top.minus(ballDimen.height)
    }

    public fun reverseVerticalVelocity() {
        velocity.vertical = velocity.vertical
    }

    public fun reverseHorizontalVelocity() {
        velocity.horizontal = -velocity.horizontal
    }

    public fun clearObstacleOnVerticalPlane(vertical: Float) {
        rectF.bottom = vertical
        rectF.top = vertical.minus(ballDimen.height)
    }

    public fun clearObstacleOnHorizontalPlane(horizontal: Float) {
        rectF.left = horizontal
        rectF.right = horizontal.plus(ballDimen.width)
    }

    public fun reset(vertical: Float, horizontal: Float) {
        rectF.top = vertical.plus(20)
        rectF.left = horizontal.div(2)
        rectF.right = horizontal.div(2).plus(ballDimen.width)
        rectF.bottom = vertical.minus(20).minus(ballDimen.height)
    }
    
}