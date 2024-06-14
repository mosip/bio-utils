package io.mosip.kernel.biosdk.provider.test.dto;

import io.mosip.kernel.core.bioapi.model.KeyValuePair;
import io.mosip.kernel.core.bioapi.model.MatchDecision;
import io.mosip.kernel.core.bioapi.model.QualityScore;
import io.mosip.kernel.core.bioapi.model.Response;
import io.mosip.kernel.core.bioapi.spi.IBioApi;
import io.mosip.kernel.core.cbeffutil.entity.BIR;

@SuppressWarnings("deprecation")
public class SDKInstanceThree0_8 implements IBioApi {

	@Override
	public Response<QualityScore> checkQuality(BIR sampleInfo, KeyValuePair[] flags) {
		Response<QualityScore> response = new Response<>();
		QualityScore qualityScore = new QualityScore();
		qualityScore.setScore(90.0F);
		response.setResponse(qualityScore);
		response.setStatusCode(210);
		return response;
	}

	@Override
	public Response<MatchDecision[]> match(BIR sampleInfo, BIR[] galleryInfo, KeyValuePair[] flags) {
		Response<MatchDecision[]> response = new Response<>();
		MatchDecision matchDecision = new MatchDecision();
		matchDecision.setMatch(true);
		matchDecision.setAnalyticsInfo(flags);
		MatchDecision[] matchDecisions = new MatchDecision[1];
		matchDecisions[0] = matchDecision;
		response.setStatusCode(210);
		response.setResponse(matchDecisions);
		return response;
	}

	@Override
	public Response<BIR> extractTemplate(BIR sampleInfo, KeyValuePair[] flags) {
		Response<BIR> response = new Response<>();
		response.setStatusCode(210);
		response.setResponse(sampleInfo);
		return response;
	}

	@Override
	public Response<BIR[]> segment(BIR sampleInfo, KeyValuePair[] flags) {
		return null;
	}
}