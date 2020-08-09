package com.flyway.intro

import java.sql.{Connection, DriverManager}

import com.flyway.intro.db.DB
import com.typesafe.scalalogging.StrictLogging

import scala.util.{Failure, Success, Try}

object Main extends App with StrictLogging {


  private val jdbcUrl = "jdbc:postgresql://127.0.0.1:5432/postgres"
  private val user = "admin"
  private val password = "admin"
  private val driver = "org.postgresql.Driver"


  private val migrateDb = DB.migrateDb(jdbcUrl, user, password)

  migrateDb match {
    case Left(t) => t.printStackTrace()
    case Right(num) => logger.info(s"After migrate DB got number of Migrations: $num")
  }

  var connection:Connection = null


  // make connection
  Try {
    Class.forName(driver)
    connection = DriverManager.getConnection(jdbcUrl, user, password)
  }.recover {case t => t.printStackTrace(); throw t}



  /*
   get all the tables in public schema - after v1 and v2 migration should be 2 tables
   1. flyway_schema_history - internal Flyway table to keep tacking over db mutations
   2.  my_flyway_intro - the table we created at v1
   */
  Try {
    val statement = connection.createStatement()
    val resultSet = statement.executeQuery("SELECT table_name FROM information_schema.tables WHERE table_schema='public'")
    while (resultSet.next()) {
      val tableName = resultSet.getString("table_name")
      logger.info(s"Table Name: $tableName")
    }
  } match {
    case Failure(t) => t.printStackTrace()
    case Success(_) =>
  }

  /*
  get all the records form my_flyway_intro table
  * should have 3 records as - what we inserted at v2
  * */
  Try {
    val statement = connection.createStatement()
    val resultSet = statement.executeQuery("SELECT my, flyway, intro FROM MY_FLYWAY_INTRO")
    logger.info(s"Row values from table 'MY_FLYWAY_INTRO'")
    while (resultSet.next()) {
      val my = resultSet.getInt("my")
      val flyway = resultSet.getString("flyway")
      val intro = resultSet.getLong("intro")
      logger.info(s"Row values: (my, flyway, intro) = ($my, $flyway, $intro)")
    }
  } match {
    case Failure(t) => t.printStackTrace()
    case Success(_) =>
  }


  Try(connection.close())
}