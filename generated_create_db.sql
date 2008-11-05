SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL';

CREATE SCHEMA IF NOT EXISTS `spelstegen` DEFAULT CHARACTER SET latin1 COLLATE latin1_swedish_ci ;
USE `spelstegen`;

-- -----------------------------------------------------
-- Table `spelstegen`.`players`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `spelstegen`.`players` ;

CREATE  TABLE IF NOT EXISTS `spelstegen`.`players` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `name` VARCHAR(50) NULL ,
  `email` VARCHAR(50) NOT NULL ,
  `nickname` VARCHAR(50) NULL ,
  `password` VARCHAR(50) NULL ,
  `image_url` VARCHAR(50) NULL ,
  PRIMARY KEY (`id`) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `spelstegen`.`players`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `spelstegen`.`players` ;

CREATE  TABLE IF NOT EXISTS `spelstegen`.`players` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `name` VARCHAR(50) NULL ,
  `email` VARCHAR(50) NOT NULL ,
  `nickname` VARCHAR(50) NULL ,
  `password` VARCHAR(50) NULL ,
  `image_url` VARCHAR(50) NULL ,
  PRIMARY KEY (`id`) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `spelstegen`.`seasons`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `spelstegen`.`seasons` ;

CREATE  TABLE IF NOT EXISTS `spelstegen`.`seasons` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `name` VARCHAR(50) NULL ,
  `startDate` DATE NULL ,
  `endDate` DATE NULL ,
  PRIMARY KEY (`id`) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `spelstegen`.`matches`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `spelstegen`.`matches` ;

CREATE  TABLE IF NOT EXISTS `spelstegen`.`matches` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `season_id` INT NOT NULL ,
  `date` DATE NULL ,
  `player1_id` INT NULL ,
  `player2_id` INT NULL ,
  PRIMARY KEY (`id`) ,
  INDEX `fk_matches_seasons` (`season_id` ASC) ,
  INDEX `fk_matches_players` (`player1_id` ASC) ,
  INDEX `fk_matches_players1` (`player2_id` ASC) ,
  CONSTRAINT `fk_matches_seasons`
    FOREIGN KEY (`season_id` )
    REFERENCES `spelstegen`.`seasons` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_matches_players`
    FOREIGN KEY (`player1_id` )
    REFERENCES `spelstegen`.`players` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_matches_players1`
    FOREIGN KEY (`player2_id` )
    REFERENCES `spelstegen`.`players` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `spelstegen`.`sports`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `spelstegen`.`sports` ;

CREATE  TABLE IF NOT EXISTS `spelstegen`.`sports` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `name` VARCHAR(50) NULL ,
  `iconUrl` VARCHAR(50) NULL ,
  PRIMARY KEY (`id`) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `spelstegen`.`sets`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `spelstegen`.`sets` ;

CREATE  TABLE IF NOT EXISTS `spelstegen`.`sets` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `sport_id` INT NOT NULL ,
  `player1Score` INT NULL ,
  `player2Score` INT NULL ,
  `match_id` INT NOT NULL ,
  PRIMARY KEY (`id`) ,
  INDEX `fk_sets_sports` (`sport_id` ASC) ,
  INDEX `fk_sets_matches` (`match_id` ASC) ,
  CONSTRAINT `fk_sets_sports`
    FOREIGN KEY (`sport_id` )
    REFERENCES `spelstegen`.`sports` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_sets_matches`
    FOREIGN KEY (`match_id` )
    REFERENCES `spelstegen`.`matches` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `spelstegen`.`leagues`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `spelstegen`.`leagues` ;

CREATE  TABLE IF NOT EXISTS `spelstegen`.`leagues` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `name` VARCHAR(50) NULL ,
  PRIMARY KEY (`id`) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `spelstegen`.`leaguePlayers`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `spelstegen`.`leaguePlayers` ;

CREATE  TABLE IF NOT EXISTS `spelstegen`.`leaguePlayers` (
  `league_id` INT NULL ,
  `player_id` INT NULL ,
  INDEX `fk_leagueplayers_leagues` (`league_id` ASC) ,
  INDEX `fk_leagueplayers_players` (`player_id` ASC) ,
  CONSTRAINT `fk_leagueplayers_leagues`
    FOREIGN KEY (`league_id` )
    REFERENCES `spelstegen`.`leagues` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_leagueplayers_players`
    FOREIGN KEY (`player_id` )
    REFERENCES `spelstegen`.`players` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `spelstegen`.`leagueSeasons`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `spelstegen`.`leagueSeasons` ;

CREATE  TABLE IF NOT EXISTS `spelstegen`.`leagueSeasons` (
  `season_id` INT NULL ,
  `league_id` INT NULL ,
  INDEX `fk_leagueSeasons_seasons` (`season_id` ASC) ,
  INDEX `fk_leagueSeasons_leagues` (`league_id` ASC) ,
  CONSTRAINT `fk_leagueSeasons_seasons`
    FOREIGN KEY (`season_id` )
    REFERENCES `spelstegen`.`seasons` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_leagueSeasons_leagues`
    FOREIGN KEY (`league_id` )
    REFERENCES `spelstegen`.`leagues` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;



SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
