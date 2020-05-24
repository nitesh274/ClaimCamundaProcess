package com.nitesh274.claimcamundaprocess.service.claimservice;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nitesh274.claimcamundaprocess.processapi.CalimProcessAdapter;

@Service("errorOccured")
public class ErrorOccured implements JavaDelegate {

	@Autowired
	private CalimProcessAdapter calimProcessAdapter;
	
	private static final Boolean SUCCESS_FLAG = false;
	@Override
	public void execute(DelegateExecution execution) throws Exception {
		String personId = execution.getProcessBusinessKey();
		String returnCode = (String) execution.getVariable("errorCode");
		String returnText = (String) execution.getVariable("errorText");
		//String processName = execution.getProcessDefinitionId().substring(20);
		calimProcessAdapter.sendResponse(personId, SUCCESS_FLAG, returnCode, returnText,
				(String) execution.getVariable("replyTo"), (String) execution.getVariable("correlationId"));
		
	}

}


