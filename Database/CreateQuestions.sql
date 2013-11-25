CREATE TABLE `questions` (
  `seq`           int AUTO_INCREMENT NOT NULL PRIMARY KEY,
  `title`         nvarchar(200),
  `description`   text,
  `points`        bigint,
  `projectseq`    int,
  `createdon`     datetime,
  `lastmodified`  datetime,
  `isenabled`     tinyint
) ENGINE = InnoDB;
