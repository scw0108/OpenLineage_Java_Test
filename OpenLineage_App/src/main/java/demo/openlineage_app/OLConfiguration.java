package demo.openlineage_app;

import demo.openlineage_app.service.DetailService;
import demo.openlineage_app.service.OpenLineageService;
import demo.openlineage_app.service.OwnerService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class OLConfiguration {

    @Bean
    public OpenLineageService openLineageService_owner() {
        Map<String, String> data = new HashMap<>();
        data.put("id", "INT(4) UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY");
        data.put("first_name", "VARCHAR(255)");
        data.put("last_name", "VARCHAR(255)");
        data.put("address", "VARCHAR(255)");
        data.put("city", "VARCHAR(255)");
        data.put("telephone", "VARCHAR(255)");
        return new OpenLineageService(
                "openlineage_test",
                "roleValue",
                data
        );
    }

    @Bean
    public OpenLineageService openLineageService_detail() {
        Map<String, String> data = new HashMap<>();
        data.put("id", "INT(4) UNSIGNED NOT NULL");
        data.put("first_name", "VARCHAR(255)");
        data.put("last_name", "VARCHAR(255)");
        data.put("interest", "VARCHAR(255)");
        return new OpenLineageService(
                "openlineage_test",
                "roleValue",
                data
        );
    }

    @Bean
    public OwnerService ownerService() {
        return new OwnerService(openLineageService_owner());
    }

    @Bean
    public DetailService detailService() {
        return new DetailService(openLineageService_detail());
    }

}
