package eventstickets;

import eventstickets.domain.Constants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.impl.TextCodec;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Locale;

@SpringBootApplication
@RestController
@EnableSwagger2
public class Application implements Filter {

    @RequestMapping("/")
    public String hello() {
        return "Hello in my eventstickets microservice";
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }


    @Bean
    public Docket productApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("eventstickets.endpoint"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo());
    }

    private ApiInfo apiInfo() {
        ApiInfo apiInfo = new ApiInfo(
                "Mikroserwis zarządzania biletami i wydarzeniami - REST API",
                "",
                "API 4",
                "",
                "Asia Kolis",
                "API License URL", null
        );
        return apiInfo;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

//        response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
        response.setHeader("Access-Control-Allow-Origin", "*");

//        response.setHeader("Acess-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE, PUT");
        response.setHeader("Access-Control-Max-Age", "3600");
//        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Accept, X-Requested-With, Authorization");

        //authorization
        final String authHeader = request.getHeader("authorization");
        String pathToRequest = request.getServletPath();

        //To Filip microsevice, swagger
        if (pathToRequest.startsWith("/tickets/status") || pathToRequest.contains("swagger") || pathToRequest.startsWith("/v2")) {
            chain.doFilter(req, res);
            return;
        } else {
            if (authHeader == null) {
                request.getRequestDispatcher("/events/token-needed").forward(request, response);
                chain.doFilter(req, res);
                return;
            }

            final String token = authHeader.substring(7);

//            final String token2[] = token.split("\\.");
//            final String message = token2[1];
//            final String signature = token2[1];

//            String payload = TextCodec.BASE64URL.decodeToString(message);


            final String secretKey = "401b09eab3c013d4ca54922bb802bec8fd5318192b0a75f201d8b3727429090fb3" +
                    "37591abd3e44453b954555b7a0812e1081c39b740293f765eae731f5a65ed1";
            try {
                final Claims claims = Jwts.parser().setSigningKey(secretKey.getBytes("UTF-8")).parseClaimsJws(token).getBody();

//                request.setAttribute(Constants.TOKEN_PAYLOAD, payload);
                String expDate = claims.get(Constants.TOKEN_PAYLOAD_EXP_DATE, String.class);
                int permission = claims.get(Constants.TOKEN_PAYLOAD_PERMISSION, Integer.class);
                int user = claims.get(Constants.TOKEN_PAYLOAD_USER, Integer.class);

                DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSSSZZ").withLocale(Locale.ROOT);
                DateTime localDateTime = DateTime.now(DateTimeZone.forID("Poland"));
                DateTime dt = formatter.parseDateTime(expDate);

                if (!localDateTime.isBefore(dt)) {
                    request.getRequestDispatcher("/events/token-expired").forward(request, response);
                }

                request.setAttribute(Constants.TOKEN_PAYLOAD_EXP_DATE, expDate);
                request.setAttribute(Constants.TOKEN_PAYLOAD_PERMISSION, permission);
                request.setAttribute(Constants.TOKEN_PAYLOAD_USER, user);

            } catch (final io.jsonwebtoken.SignatureException e) {
//                throw new ServletException(e.getMessage());
                request.getRequestDispatcher("/events/token-not-valid").forward(request, response);
            }
        }

        chain.doFilter(req, res);

    }

    @Override
    public void destroy() {

    }
}
