package eventstickets.endpoint;

import org.springframework.http.HttpStatus;

public class ResponseErrorHandler <T>{

    public int statusCode;
    public T result;
    public boolean success;
    public ErrorContainer errorContainer;

    public ResponseErrorHandler(int statusCode, T result, boolean success, int code,
                                String message, String details, String url) {
        this.statusCode = statusCode;
        this.result = result;
        this.success = success;
        this.errorContainer = new ErrorContainer(code, message, details, url);
    }

    public class ErrorContainer {

        public int code;
        public String message;
        public String details;
        public String url;

        public ErrorContainer(int code, String message, String details, String url) {
            this.code = code;
            this.message = message;
            this.details = details;
            this.url = url;
        }
    }


}

