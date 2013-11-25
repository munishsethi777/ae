CREATE TABLE `gametemplates` (
  `seq`          int AUTO_INCREMENT NOT NULL PRIMARY KEY,
  `name`         nvarchar(100),
  `path`         nvarchar(100),
  `imagepath`    nvarchar(100),
  `description`  nvarchar(100)
) ENGINE = InnoDB;
