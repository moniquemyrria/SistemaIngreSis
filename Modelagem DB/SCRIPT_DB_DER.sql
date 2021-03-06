SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

CREATE SCHEMA IF NOT EXISTS `dbingresis` DEFAULT CHARACTER SET utf8 ;
USE `dbingresis` ;

-- -----------------------------------------------------
-- Table `dbingresis`.`evento`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `dbingresis`.`evento` (
  `idEvento` INT(11) NOT NULL AUTO_INCREMENT ,
  `nomeEvento` VARCHAR(45) NOT NULL ,
  `localEvento` VARCHAR(45) NOT NULL ,
  `dataEvento` VARCHAR(45) NOT NULL ,
  `horarioEvento` VARCHAR(45) NOT NULL ,
  `statusEvento` VARCHAR(45) NOT NULL COMMENT 'Status: ATIVO ou DELETADO' ,
  PRIMARY KEY (`idEvento`) )
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COMMENT = 'Dados do Evento ';


-- -----------------------------------------------------
-- Table `dbingresis`.`ingresso`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `dbingresis`.`ingresso` (
  `idIngresso` INT(11) NOT NULL AUTO_INCREMENT ,
  `descricaoIngresso` VARCHAR(45) NOT NULL ,
  `idEventoIngresso` INT(11) NOT NULL ,
  `statusIngresso` VARCHAR(45) NOT NULL COMMENT 'Status: ATIVO ou DELETADO' ,
  PRIMARY KEY (`idIngresso`) ,
  INDEX `idEventoIngresso_idx` (`idEventoIngresso` ASC) ,
  CONSTRAINT `idEventoIngresso`
    FOREIGN KEY (`idEventoIngresso` )
    REFERENCES `dbingresis`.`evento` (`idEvento` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COMMENT = 'Ingresso dos Eventos';


-- -----------------------------------------------------
-- Table `dbingresis`.`lote`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `dbingresis`.`lote` (
  `idLote` INT(11) NOT NULL AUTO_INCREMENT ,
  `numeroLote` VARCHAR(45) NOT NULL ,
  `quantidadeLote` INT(11) NOT NULL ,
  `valorLote` DOUBLE NOT NULL ,
  `idIngressoLote` INT(11) NOT NULL ,
  `statusLote` VARCHAR(45) NOT NULL COMMENT 'Status: ATIVO ou DELETADO' ,
  PRIMARY KEY (`idLote`) ,
  INDEX `idIngressoLote_idx` (`idIngressoLote` ASC) ,
  CONSTRAINT `idIngressoLote`
    FOREIGN KEY (`idIngressoLote` )
    REFERENCES `dbingresis`.`ingresso` (`idIngresso` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COMMENT = 'Cadastro de Lotes dos Ingressos dos Eventos';


-- -----------------------------------------------------
-- Table `dbingresis`.`usuario`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `dbingresis`.`usuario` (
  `idUsuario` INT(11) NOT NULL AUTO_INCREMENT ,
  `loginUsuario` VARCHAR(45) NOT NULL ,
  `senhaUsuario` VARCHAR(8) NOT NULL ,
  `tipoUsuario` VARCHAR(45) NOT NULL ,
  `statusUsuario` VARCHAR(45) NOT NULL COMMENT 'Status: ATIVO ou DELETADO' ,
  PRIMARY KEY (`idUsuario`) )
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COMMENT = 'Tabela que armazena o acesso dos usuários Produtores ou Vendedores';


-- -----------------------------------------------------
-- Table `dbingresis`.`venda`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `dbingresis`.`venda` (
  `idVenda` INT(11) NOT NULL AUTO_INCREMENT ,
  `formaPagamentoVenda` VARCHAR(45) NOT NULL ,
  `valorVenda` DOUBLE NOT NULL ,
  `idEventoVenda` INT(11) NOT NULL ,
  `idIngressoVenda` INT(11) NOT NULL ,
  `idLoteVenda` INT(11) NOT NULL ,
  PRIMARY KEY (`idVenda`) ,
  INDEX `idEventoVenda_idx` (`idEventoVenda` ASC) ,
  INDEX `idIngressoVenda_idx` (`idIngressoVenda` ASC) ,
  INDEX `idLoteVenda_idx` (`idLoteVenda` ASC) ,
  CONSTRAINT `idEventoVenda`
    FOREIGN KEY (`idEventoVenda` )
    REFERENCES `dbingresis`.`evento` (`idEvento` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `idIngressoVenda`
    FOREIGN KEY (`idIngressoVenda` )
    REFERENCES `dbingresis`.`ingresso` (`idIngresso` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `idLoteVenda`
    FOREIGN KEY (`idLoteVenda` )
    REFERENCES `dbingresis`.`lote` (`idLote` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COMMENT = 'Venda dos ingressos com forma de pagamento';

USE `dbingresis` ;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
