package com.saeed.multiplayer_pong.engine

import android.graphics.Paint
import android.graphics.RectF


/**
 * Created by Saeed on 24/11/2019.
 */
class Player(val paddleWidth: Int, val paddleHeight: Int, val paint: Paint) {

    val bounds: RectF = RectF(0F, 0F, paddleWidth.toFloat(), paddleHeight.toFloat())
    var score: Int = 0
    var collision: Int = 0

}