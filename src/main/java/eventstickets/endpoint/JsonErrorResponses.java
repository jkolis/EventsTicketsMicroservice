package eventstickets.endpoint;

public class JsonErrorResponses<T>{

    public int statusCode;
    public T result;
    public boolean success;
    public Error errorContainer;

    public JsonErrorResponses(int statusCode, T result, boolean success, int code,
                              String message, String details, String url) {
        this.statusCode = statusCode;
        this.result = result;
        this.success = success;
        this.errorContainer = new Error(code, message, details, url);
    }

    public class Error {

        public int code;
        public String message;
        public String details;
        public String url;

        public Error(int code, String message, String details, String url) {
            this.code = code;
            this.message = message;
            this.details = details;
            this.url = url;
        }
    }


}

