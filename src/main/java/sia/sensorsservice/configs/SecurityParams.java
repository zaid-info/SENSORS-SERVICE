package sia.sensorsservice.configs;

public interface SecurityParams {

    public static final String JWT_HEADER_NAME="Authorization";
    public static final String SECRET="zbousmina@gmail.com";
    public static final long EXPIRATION= 10*24*2600*1000;
    public static final String HEADER_PREFIX="Bearer ";
}
