package com.satya.Utils;

import org.apache.log4j.Logger;

import com.satya.BusinessObjects.Game;
import com.satya.BusinessObjects.QuestionAnswers;
import com.satya.BusinessObjects.Questions;
import com.satya.BusinessObjects.QuestionsList;
import com.satya.xmlObjects.Category;
import com.satya.xmlObjects.Success;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class XstreamUtil {

	Logger logger = Logger.getLogger(XstreamUtil.class);

	public String generateQuestionsXML(Game game) {
		XStream xstream = new XStream(new DomDriver());
		xstream.alias("question", Questions.class);
		xstream.alias("questions", QuestionsList.class);
		xstream.useAttributeFor(QuestionsList.class, "maxSecondsAllowed");

		xstream.useAttributeFor(Questions.class, "seq");// inline value in tag
		xstream.aliasField("id", Questions.class, "seq");// rename the tag

		xstream.useAttributeFor(Questions.class, "points");
		xstream.useAttributeFor(Questions.class, "answerId");
		xstream.useAttributeFor(Questions.class, "negativePoints");
		xstream.useAttributeFor(Questions.class, "maxSecondsAllowed");
		xstream.useAttributeFor(Questions.class, "extraAttemptsAllowed");

		xstream.useAttributeFor(Questions.class, "title");
		xstream.aliasField("value", Questions.class, "title");

		xstream.useAttributeFor(Questions.class, "description");
		xstream.aliasField("hint", Questions.class, "description");

		xstream.omitField(QuestionAnswers.class, "isCorrect");
		xstream.alias("answer", QuestionAnswers.class);

		xstream.useAttributeFor(QuestionAnswers.class, "seq");
		xstream.aliasField("id", QuestionAnswers.class, "seq");

		xstream.useAttributeFor(QuestionAnswers.class, "answerTitle");
		xstream.aliasField("description", QuestionAnswers.class, "answerTitle");
		xstream.aliasField("introduction", Questions.class, "introduction");
		xstream.alias("Category", Category.class);
		xstream.alias("label", String.class);

		xstream.alias("success", Success.class);
		xstream.useAttributeFor(Success.class, "successPercentage");

		// removing questionAnswers grouping tag
		xstream.addImplicitCollection(Questions.class, "questionAnswers");
		xstream.addImplicitCollection(QuestionsList.class, "questionsList");
		xstream.addImplicitCollection(Category.class, "labels");

		xstream.registerConverter(new QuestionAnswerConverter());
		String xml = "";
		try {
			xml = xstream.toXML(game.getQuestionsListObj());
		} catch (Exception e) {
			logger.error(e);
		}
		SecurityUtil su = new SecurityUtil();
		su.init();
		xml = su.getEncryptedString(xml);
		return xml;

	}

	class QuestionAnswerConverter implements Converter {
		@Override
		public boolean canConvert(Class arg0) {
			return arg0.equals(QuestionAnswers.class);
		}

		@Override
		public Object unmarshal(HierarchicalStreamReader reader,
				UnmarshallingContext arg1) {
			QuestionAnswers qa = new QuestionAnswers();
			qa.setName(reader.getValue());
			qa.setSeq(Long.parseLong(reader.getAttribute("id")));
			qa.setAnswerTitle(reader.getAttribute("description"));
			return qa;
		}

		@Override
		public void marshal(Object object, HierarchicalStreamWriter writer,
				MarshallingContext context) {
			QuestionAnswers qa = (QuestionAnswers) object;
			writer.addAttribute("id", String.valueOf(qa.getSeq()));
			writer.addAttribute("description", qa.getAnswerTitle());
			writer.setValue(qa.getAnswerTitle());
		}
	}
}
