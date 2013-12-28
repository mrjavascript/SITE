package org.sitenv.portlets.qrda;

import javax.portlet.ActionRequest;

import org.sitenv.portlets.qrda.models.LiferayDefaultMultipartActionRequest;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.portlet.DispatcherPortlet;
import org.springframework.web.portlet.multipart.MultipartActionRequest;

public class LiferayDispatcherPortlet extends DispatcherPortlet {

	@Override
	protected ActionRequest checkMultipart(ActionRequest request)
			throws MultipartException {

		ActionRequest requestAfterCheck = super.checkMultipart(request);

		if ((requestAfterCheck instanceof MultipartActionRequest)
				&& !(requestAfterCheck instanceof LiferayDefaultMultipartActionRequest)) {
			MultipartActionRequest multipartActionRequest = (MultipartActionRequest) requestAfterCheck;
			return new LiferayDefaultMultipartActionRequest(
					multipartActionRequest);
		}

		return requestAfterCheck;
	}

}