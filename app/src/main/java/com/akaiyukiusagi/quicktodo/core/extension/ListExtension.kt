package com.akaiyukiusagi.quicktodo.core.extension

import com.akaiyukiusagi.quicktodo.data_layer.room.entity.Task

/**
 * List<Task>の拡張関数として、指定されたidに一致するTaskを返却します。
 * 一致するTaskがない場合はnullを返却します。
 *
 * @param id 検索するTaskのid
 * @return 一致するTask、存在しない場合はnull
 */
fun List<Task>.findTaskById(id: Int): Task? {
    return this.find { it.id == id }
}