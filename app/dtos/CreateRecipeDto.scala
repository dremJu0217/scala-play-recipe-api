package dtos

import java.time.LocalDate
import play.api.libs.json._

case class CreateRecipeDto(
                              title: String,
                              making_time: String,
                              serves: String,
                              ingredients: String,
                              cost: Long,
                            )

object CreateRecipeDto {
  implicit val format: OFormat[CreateRecipeDto] = Json.format[CreateRecipeDto]
}