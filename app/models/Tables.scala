package models

import slick.jdbc.MySQLProfile.api._
import java.sql.Timestamp

class Recipes(tag: Tag) extends Table[Recipe](tag, "recipes") {
  def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
  def title = column[String]("title")
  def making_time = column[String]("making_time")
  def serves = column[String]("serves")
  def ingredients = column[String]("ingredients")
  def cost = column[Long]("cost")
  def created_at = column[String]("created_at")
  def updated_at = column[String]("updated_at")



  def * =
    (id.?, title, making_time, serves, ingredients, cost,created_at,updated_at)
      .<>(Recipe.tupled, Recipe.unapply)
}

object Tables {
  val recipes = TableQuery[Recipes]
}