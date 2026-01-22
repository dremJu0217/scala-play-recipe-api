package models


case class Recipe(
                           id: Option[Int] = None,
                           title: String,
                           making_time: String,
                           serves: String,
                           ingredients: String,
                           cost: Long,
                           created_at: String,
                           updated_at: String
                         )