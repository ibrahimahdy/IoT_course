package com.saeed.multiplayer_pong.engine

import android.graphics.Paint


/**
 * Created by Saeed on 24/11/2019.
 */
class Ball(
    var radius: Int = 0,
    var paint: Paint
) {

    var cx: Float = 0.toFloat()
    var cy: Float = 0.toFloat()
    var dx: Float = 0.toFloat()
    var dy: Float = 0.toFloat()

}