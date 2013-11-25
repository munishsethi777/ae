CREATE TABLE `questionanswers` (
  `seq`          int AUTO_INCREMENT NOT NULL PRIMARY KEY,
  `questionseq`  int,
  `title`        nvarchar(500),
  `iscorrect`    tinyint
) ENGINE = InnoDB;
