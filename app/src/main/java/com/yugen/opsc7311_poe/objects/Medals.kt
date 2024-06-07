package com.yugen.opsc7311_poe.objects

data class Medals(
    var bronzeCnt: Int,
    var silverCnt: Int,
    var goldCnt: Int,
    var purpleCnt: Int,
    var rubyCnt: Int
)
{ constructor() : this(0, 0, 0, 0, 0)}
