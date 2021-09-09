package nextstep.jwp.web.exceptionhandler;

import nextstep.jwp.http.HttpRequest;
import nextstep.jwp.http.HttpResponse;

public class InternalServerErrorExceptionHandler implements ExceptionHandler {

    @Override
    public void handle(HttpRequest httpRequest, HttpResponse httpResponse) {
        httpResponse.redirect("/500.html");
    }
}