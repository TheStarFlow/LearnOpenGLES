package com.zzs.learnopengl

/**
@author  zzs
@Date 2021/12/31
@describe
 */
data class Vec3(var x: Float, var y: Float, var z: Float) {


    fun add(vec3: Vec3): Vec3 {
        return Vec3(x + vec3.x, y + vec3.y, z + vec3.z)
    }
}