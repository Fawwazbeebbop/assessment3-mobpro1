package org.d3if3051.assessment3.model

data class Scenery(
    val scenery_id: Int,
    val user_email: String,
    val judul_pemandangan: String,
    val lokasi: String,
    val image_id: String,
    val delete_hash: String,
    val created_at: String
)

data class SceneryCreate(
    val user_email: String,
    val judul_pemandangan: String,
    val lokasi: String,
    val image_id: String,
    val delete_hash: String
)
