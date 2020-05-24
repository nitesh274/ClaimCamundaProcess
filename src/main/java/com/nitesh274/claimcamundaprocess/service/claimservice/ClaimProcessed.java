package com.nitesh274.claimcamundaprocess.service.claimservice;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nitesh274.claimcamundaprocess.processapi.CalimProcessAdapter;

@Service("claimProcessed")
public class ClaimProcessed implements JavaDelegate{

	@Autowired
	private CalimProcessAdapter calimProcessAdapter;

	private static final Boolean SUCCESS_FLAG = true;
	private static final String RETURN_CODE = "MSA009.BL-W.OK";
	private static final String RETURN_TEXT = "claimProcesses";
	
	@Override
	public void execute(DelegateExecution execution) throws Exception {
		
		String personId = execution.getProcessBusinessKey();
		

		calimProcessAdapter.sendResponse(personId, SUCCESS_FLAG, RETURN_CODE, RETURN_TEXT,
				(String) execution.getVariable("replyTo"), (String) execution.getVariable("correlationId"));
		
	}

}
