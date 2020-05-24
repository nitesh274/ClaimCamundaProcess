package com.nitesh274.claimcamundaprocess.processapi;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.impl.util.json.JSONObject;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.nitesh274.claimcamundaprocess.config.ProcessApiConfig;

@Component("calimProcessAdapter")
public class CalimProcessAdapter {

	@Autowired
	private RuntimeService runtimeService;

	private static final String PROCESS_KEY_CLAIMAMOUNT = "Process_CCP_claimAmount";
	private static final String PERSON_ID = "personId";
	private final RabbitTemplate rabbitTemplate;

	public CalimProcessAdapter(final RabbitTemplate rabbitTemplate) {
		this.rabbitTemplate = rabbitTemplate;
	}

	// Listener for researchGigabitAvailability BPMN process
	@RabbitListener(queues = ProcessApiConfig.CCP_CLAIMPROCESS_QUEUE_NAME)
	public void startClaimProcess(Message message) throws UnsupportedEncodingException {
		this.startCamundaProcess(PROCESS_KEY_CLAIMAMOUNT, "MSA_001_CCP", message);
	}

	public void startCamundaProcess(String processKey, String processKeyShort, Message message)
			throws UnsupportedEncodingException {

		String body = new String(message.getBody(), "UTF-8");
		String personId = new JSONObject(body).getString(PERSON_ID);
		String claimId = new JSONObject(body).getString("claimId");
		String mode = "claimAmountProcess";

		String correlationId = message.getMessageProperties().getCorrelationId();
		String replyTo = message.getMessageProperties().getReplyTo();

		try {

			Map<String, Object> variables = new HashMap<>();
			variables.put("correlationId", correlationId);
			variables.put("replyTo", replyTo);
			variables.put("claimId", claimId);
			variables.put("mode", mode);
			runtimeService.startProcessInstanceByKey(processKey, personId, variables);

		} catch (Exception e) {

			this.sendResponse(personId, false, "MSA_001_CCP", "BPMN process couldn't be started", replyTo,
					correlationId);

		}
	}

	// Method for sending the response
	public void sendResponse(String activityId, Boolean successFlag, String returnCode, String returnText,
			String replyTo, String correlationId) {

		JSONObject response = new JSONObject();
		response.put(PERSON_ID, activityId);
		response.put("success", successFlag);
		response.put("returnCode", returnCode);
		response.put("returnText", returnText);

		String message = response.toString();

		rabbitTemplate.convertAndSend(replyTo, message, m -> {
			m.getMessageProperties().setCorrelationId("" + correlationId);
			return m;
		});

	}

}
