-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
-- -----------------------------------------------------
-- Schema coleccion_Comics
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema coleccion_Comics
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `coleccion_Comics` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci ;
USE `coleccion_Comics` ;

-- -----------------------------------------------------
-- Table `coleccion_Comics`.`comics`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `coleccion_Comics`.`comics` (
  `nomComic` VARCHAR(100) NOT NULL,
  `nomDibujante` VARCHAR(100) NOT NULL,
  `nomEditorial` VARCHAR(45) NOT NULL,
  `nomGuionista` VARCHAR(150) NOT NULL,
  `nomVariante` VARCHAR(150) NOT NULL,
  `nomFormato` VARCHAR(45) NOT NULL,
  `numComic` VARCHAR(45) NOT NULL)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;