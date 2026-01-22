package validators

import utils.validation.Validator
import dtos.CreateRecipeDto

object RecipeValidator extends Validator {
  def validateCreate(dto: CreateRecipeDto): Map[String, String] = {
    val baseErrs =
      List(
        isNotEmpty("title", dto.title),
        isNotEmpty("making_time", dto.making_time),
        isNotEmpty("serves", dto.serves),
        isNotEmpty("ingredients", dto.ingredients),
        isNotEmpty("cost", dto.cost.toString),

      ).flatten


    (baseErrs).toMap
  }


}