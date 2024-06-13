package io.mosip.kernel.biosdk.provider.test.dto;

import io.mosip.kernel.biometrics.entities.BIR;
import io.mosip.kernel.core.bioapi.model.KeyValuePair;
import io.mosip.kernel.core.bioapi.model.QualityScore;
import io.mosip.kernel.core.bioapi.model.Response;
import io.mosip.kernel.core.bioapi.model.Score;

public class SDKInstanceOne0_7 {
	@SuppressWarnings({ "unused" })
	public QualityScore checkQuality(BIR sampleInfo, KeyValuePair[] flags) {
		QualityScore qualityScore = new QualityScore();
		qualityScore.setScore(90.0F);
		qualityScore.setAnalyticsInfo(flags);
		qualityScore.setInternalScore(90);
		return qualityScore;
	}

	@SuppressWarnings({ "unused" })
	public Score[] match(BIR sampleInfo, BIR[] gallery, KeyValuePair[] flags) {
		Score[] scores = new Score[1];
		Score score = new Score();
		score.setAnalyticsInfo(flags);
		score.setInternalScore(60);
		score.setScaleScore(60.0F);
		scores[0] = score;
		return scores;
	}

	@SuppressWarnings({ "unused" })
	public BIR extractTemplate(BIR sampleInfo, KeyValuePair[] flags) {
		return sampleInfo;
	}

	@SuppressWarnings({ "unused" })
	public Response<BIR[]> segment(BIR sampleInfo, KeyValuePair[] flags) {
		return null;
	}
}