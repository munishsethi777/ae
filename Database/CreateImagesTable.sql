CREATE TABLE `images` (
  `seq`            int AUTO_INCREMENT NOT NULL,
  `imagetitle`     varchar(200) CHARACTER SET `utf8` COLLATE `utf8_general_ci`,
  `imagepath`      varchar(200) CHARACTER SET `utf8` COLLATE `utf8_general_ci`,
  `imagetype`      varchar(20) CHARACTER SET `utf8` COLLATE `utf8_general_ci`,
  `imagesize`      bigint,
  `imagesavedate`  datetime,
  `projectseq`     int,
  `adminseq`       int,
  /* Keys */
  PRIMARY KEY (`seq`)
) ENGINE = InnoDB;