ALTER TABLE `results`
  ADD `campaignsetseq` int;
  
ALTER TABLE `resultquestions`
  ADD `attempts` int;
  
ALTER TABLE `resultquestions`
  ADD `negativepoints` int;
  
ALTER TABLE `questions`
  ADD `negativepoints` int;
  
ALTER TABLE `questions`
  ADD `maxsecondsallowed` int;
  
ALTER TABLE `questions`
  ADD `extraattemptsallowed` int;
