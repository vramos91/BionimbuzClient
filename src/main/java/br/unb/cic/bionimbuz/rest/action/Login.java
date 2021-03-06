package br.unb.cic.bionimbuz.rest.action;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;

import br.unb.cic.bionimbuz.rest.request.LoginRequest;
import br.unb.cic.bionimbuz.rest.request.RequestInfo;
import br.unb.cic.bionimbuz.rest.response.LoginResponse;

public class Login extends Action {

    private static final String REST_LOGIN_URL = "/rest/login";

    @Override
    public void setup(Client client, RequestInfo reqInfo) {
        this.target = client.target(appConfiguration.getBionimbuzAddress());
        this.request = (LoginRequest) reqInfo;
    }

    @Override
    public void prepareTarget() {
        target = target.path(REST_LOGIN_URL);
    }

    @Override
    public LoginResponse execute() {
        logAction(REST_LOGIN_URL, Login.class);

        LoginResponse response = target
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.entity(request, MediaType.APPLICATION_JSON), LoginResponse.class);

        return response;
    }

}
