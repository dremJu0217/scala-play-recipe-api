package services

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}
import java.time.{Instant, LocalDate, YearMonth}
import play.api.libs.json._
import dtos.{CreateRecipeDto,RecipeResponse}
import models.Recipe
import repositories.RecipleRepository
import utils.ApiError
import validators.RecipeValidator

@Singleton
class RecipeService @Inject()(
                                  recipeRepo: RecipleRepository,
                                )(implicit ec: ExecutionContext) {

  def create(dto: CreateRecipeDto): Future[Either[ApiError, Recipe]] = {
    val validationErrors = RecipeValidator.validateCreate(dto)
    if (validationErrors.nonEmpty)
      Future.successful(Left(ApiError.ValidationError(validationErrors)))
    else {
      val recipe = Recipe(
        title = dto.title,
        making_time = dto.making_time,
        serves = dto.serves,
        ingredients = dto.ingredients,
        cost = dto.cost,
        created_at = Instant.now().toString.replace("T"," ").substring(0, 19),
        updated_at = Instant.now().toString.replace("T"," ").substring(0, 19),
      )
      recipeRepo.create(recipe)
        .map(createdRecipe  => Right(createdRecipe ))
        .recover { case ex => Left(ApiError.InternalServerError(ex.getMessage)) }
    }
  }

  def listAll(): Future[Either[ApiError, Seq[RecipeResponse]]] =
    recipeRepo.listAll()
      .map(recipes=>  Right(recipes.map (r =>
        RecipeResponse(
          id = r.id.get,
          title = r.title,
          making_time = r.making_time,
          serves = r.serves,
          ingredients = r.ingredients,
          cost = r.cost
        )
      )))
      .recover { case ex => Left(ApiError.InternalServerError(ex.getMessage)) }

  def findById(id: Int): Future[Either[ApiError, RecipeResponse]] =
    recipeRepo.findById(id)
      .map {
        case Some(c) => Right(
          RecipeResponse(
            id = c.id.get,
            title = c.title,
            making_time = c.making_time,
            serves = c.serves,
            ingredients = c.ingredients,
            cost = c.cost

        ))
        case None => Left(ApiError.NotFound("No recipe found"))
      }
      .recover { case ex => Left(ApiError.InternalServerError(ex.getMessage)) }

  def delete(id: Int): Future[Int] =
    recipeRepo.deleteById(id)

  def update(id: Int, dto: CreateRecipeDto): Future[Either[ApiError, CreateRecipeDto]] = {
    val validationErrors = RecipeValidator.validateCreate(dto)
    if (validationErrors.nonEmpty)
      Future.successful(Left(ApiError.ValidationError(validationErrors)))
    else {
      recipeRepo.findById(id).flatMap {
        case None => Future.successful(Left(ApiError.NotFound("No recipe found")))
        case Some(existing) =>
          val merged = existing.copy(
            title = dto.title,
            making_time = dto.making_time,
            serves = dto.serves,
            ingredients = dto.ingredients,
            cost = dto.cost,
            updated_at = Instant.now().toString.replace("T"," ").substring(0, 19),
          )
          recipeRepo.update(id, merged)
            .map(_ => Right(CreateRecipeDto(
              title = merged.title,
              making_time = merged.making_time,
              serves = merged.serves,
              ingredients = merged.ingredients,
              cost = merged.cost
            )))
            .recover { case ex => Left(ApiError.InternalServerError(ex.getMessage)) }
      }
    }
  }
}