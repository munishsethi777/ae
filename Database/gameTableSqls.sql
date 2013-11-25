ALTER TABLE `games`
  CHANGE `name` `title` nvarchar(200);
  
ALTER TABLE `games`
  ADD `gametemplateseq` int;
  
ALTER TABLE `games`
  ADD `projectseq` int;


ALTER TABLE `games`
  DROP `path`;

ALTER TABLE `games`
  DROP `imagepath`;

ALTER TABLE `games`
  ADD `createdon` datetime;
