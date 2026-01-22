package dtos

import play.api.libs.json.{Json, OFormat}

case class RecipeResponse(
                           id: Int,
                           title: String,
                           making_time: String,
                           serves: String,
                           ingredients: String,
                           cost: Long
                         )

object RecipeResponse {
  implicit val format: OFormat[RecipeResponse] = Json.format[RecipeResponse]
}
