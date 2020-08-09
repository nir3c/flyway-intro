package com.flyway.intro.db

import org.flywaydb.core.Flyway

import scala.util.Try

object DB {

  def migrateDb(url: String, user: String, password: String): Either[Throwable, Int] = {
    for{
      migrate <- Try(Flyway.configure().dataSource(url, user, password).load().migrate()).toEither
    } yield migrate
  }






}

