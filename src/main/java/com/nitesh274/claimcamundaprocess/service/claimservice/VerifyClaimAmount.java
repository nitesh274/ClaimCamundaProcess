package com.nitesh274.claimcamundaprocess.service.claimservice;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Service;

@Service("verifyClaimAmount")
public class VerifyClaimAmount implements JavaDelegate {

	@Override
	public void execute(DelegateExecution execution) throws Exception {

		String mode=(String) execution.getVariable("mode");
		
		System.out.println("mode==>> "+mode);
		execution.setVariable("paymentMethod", "account");

	}

}
