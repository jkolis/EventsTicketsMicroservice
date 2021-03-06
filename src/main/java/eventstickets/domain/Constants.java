package eventstickets.domain;

public class Constants {

    public static final String TICKET_CANCELED = "CANCELED";
    public static final String TICKET_AVAILABLE = "AVAILABLE";
    public static final String TICKET_OCCUPIED = "OCCUPIED";

    public static final String EVENT_CANCELED = "CANCELED";

    public static final String TICKET_REGULAR = "REGULAR";
    public static final String TICKET_PREMIUM = "PREMIUM";

    public static final int MAX_SEATS = 100;

    public static final String ORDERS_URI = "http://rso-mf.westeurope.cloudapp.azure.com:8181";
    public static final String TOKEN_PAYLOAD_EXP_DATE = "expirationDate";
    public static final String TOKEN_PAYLOAD_PERMISSION = "permissionId";
    public static final String TOKEN_PAYLOAD_USER = "userId";
    public static final String URL = "http://";
    public static final int ADMIN_PERM = 1;
}
