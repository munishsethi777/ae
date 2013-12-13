package com.satya.tests;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.satya.BusinessObjects.Game;
import com.satya.BusinessObjects.QuestionAnswers;
import com.satya.BusinessObjects.Questions;
import com.satya.BusinessObjects.QuestionsList;
import com.satya.BusinessObjects.Result;
import com.satya.BusinessObjects.Result.ResultQuestion;
import com.satya.Utils.FileUtils;
import com.satya.Utils.SecurityUtil;
import com.satya.Utils.XstreamUtil;
import com.satya.xmlObjects.Category;
import com.satya.xmlObjects.Success;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class XstreamTest {

	public void encryptAndSaveFile() throws IOException {
		String xmlfilePath = "h:/questions.xml";
		SecurityUtil su = new SecurityUtil();
		su.init();
		FileUtils fu = new FileUtils();
		String xml = fu.readFileFromFileSystem(xmlfilePath);
		String deXML = su.getEncryptedString(xml);
		fu.saveTextFile(deXML, "h:/decryptedQuestions.txt");
	}

	public void testSecurityUtil() {
		SecurityUtil su = new SecurityUtil();
		su.init();
		StringBuffer str = new StringBuffer();

		str.append("<questions maxSecondsAllowed='0'>");
		str.append("<question id='1' value='This is a question' hint='Queston Description' points='10' answerId='1' negativePoints='5' "
				+ "maxSecondsAllowed='15' extraAttemptsAllowed='2'>");
		str.append("<answer id='1' description='This is option 1'>This  is CORRECT option 1</answer>");
		str.append("<answer id='2' description='This is option 2 '>This is option 2 </answer>");
		str.append("<answer id='3' description='This is option 3'>This is option 3</answer>");
		str.append("<answer id='4' description='This is option 4'>This is option 4</answer>");
		str.append("</question>");

		str.append("<question id='2' value='This is a new question' hint='Queston Description' points='20' answerId='1' negativePoints='20' "
				+ "maxSecondsAllowed='0' extraAttemptsAllowed='0'>");
		str.append("<answer id='1' description='This is option 1'>This  is CORRECT option 1</answer>");
		str.append("<answer id='2' description='This is option 2 '>This is option 2 </answer>");
		str.append("<answer id='3' description='This is option 3'>This is option 3</answer>");
		str.append("<answer id='4' description='This is option 4'>This is option 4</answer>");
		str.append("</question>");

		str.append("<question id='3' value='This is a new question' hint='Queston Description' points='30' answerId='1' negativePoints='30' "
				+ "maxSecondsAllowed='0' extraAttemptsAllowed='0'>");
		str.append("<answer id='1' description='This is option 1'>This  is CORRECT option 1</answer>");
		str.append("<answer id='2' description='This is option 2 '>This is option 2 </answer>");
		str.append("<answer id='3' description='This is option 3'>This is option 3</answer>");
		str.append("<answer id='4' description='This is option 4'>This is option 4</answer>");
		str.append("</question>");
		str.append("</questions>");
		System.out.print(su.getEncryptedString(str.toString()));

	}

	@Test
	public void test() {

		QuestionsList questionsListObj = new QuestionsList();
		questionsListObj.setMaxSecondsAllowed(10);
		List<Questions> questionsList = new ArrayList<Questions>();

		Questions question = new Questions();
		questionsList.add(question);
		questionsListObj.setQuestionsList(questionsList);

		question.setSeq(1);
		question.setDescription("Queston Description");
		question.setTitle("This is a question");
		question.setPoints(10);
		question.setAnswerId(1);
		question.setNegativePoints(5);
		question.setMaxSecondsAllowed(15);
		question.setExtraAttemptsAllowed(1);
		question.setIntroduction("Introduction Text");
		Category ca = new Category();
		List<String> labels = new ArrayList<String>();
		labels.add("label 1");
		labels.add("label 2");
		labels.add("label 3");
		labels.add("label 4");
		ca.setLabels(labels);
		question.setCategory(ca);

		Success success = new Success();
		success.setMessage("Congratulations you are the rockstar");
		success.setSuccessPercentage(23);

		question.setSuccess(success);

		QuestionAnswers qa1 = new QuestionAnswers();
		qa1.setSeq(1);
		qa1.setAnswerTitle("This is option 1");
		qa1.setCorrect(true);
		qa1.setName("munish");

		QuestionAnswers qa2 = new QuestionAnswers();
		qa2.setSeq(2);
		qa2.setAnswerTitle("This is option 2 ");
		qa2.setCorrect(false);
		qa2.setName("munish");

		QuestionAnswers qa3 = new QuestionAnswers();
		qa3.setSeq(3);
		qa3.setAnswerTitle("This is option 3");
		qa3.setCorrect(false);
		qa3.setName("munish");

		QuestionAnswers qa4 = new QuestionAnswers();
		qa4.setSeq(4);
		qa4.setAnswerTitle("This is option 4");
		qa4.setCorrect(false);
		qa4.setName("munish");

		List<QuestionAnswers> qaList = new ArrayList<QuestionAnswers>();
		qaList.add(qa1);
		qaList.add(qa2);
		qaList.add(qa3);
		qaList.add(qa4);
		question.setQuestionAnswers(qaList);
		Game game = new Game();
		game.setQuestions(questionsList);
		game.setMaxSecondsAllowed(0);
		XstreamUtil xstreamUtil = new XstreamUtil();
		System.out.print(xstreamUtil.generateQuestionsXML(game));
	}

	public void convertToObject() {
		String xmlString = "<result userId='munishsethi' gameId='marathon' totalScore='100' totalTime='60'>"
				+ "<question id='1' timeTaken='10' selectedAnswerId='1' points='10' />"
				+ "<question id='2' timeTaken='50' selectedAnswerId='2' points='10' />"
				+ "<question id='1' timeTaken='10' selectedAnswerId='1' points='10' />"
				+ "<question id='2' timeTaken='50' selectedAnswerId='2' points='10' />"
				+ "</result>";

		XStream xstream = new XStream(new DomDriver());
		xstream.alias("result", Result.class);
		xstream.useAttributeFor(Result.class, "userId");
		xstream.useAttributeFor(Result.class, "gameId");
		xstream.useAttributeFor(Result.class, "totalScore");
		xstream.useAttributeFor(Result.class, "totalTime");

		xstream.alias("questions", List.class);
		xstream.alias("question", ResultQuestion.class);
		xstream.addImplicitCollection(Result.class, "questions");

		xstream.useAttributeFor(ResultQuestion.class, "id");
		xstream.useAttributeFor(ResultQuestion.class, "timeTaken");
		xstream.useAttributeFor(ResultQuestion.class, "selectedAnswerId");
		xstream.useAttributeFor(ResultQuestion.class, "points");

		try {
			Result result = (Result) xstream.fromXML(xmlString);
			System.out.print(result);
		} catch (Exception e) {
			System.out.print(e);
		}

	}

}
