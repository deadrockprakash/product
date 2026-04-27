package com.prakash.productservice.config;


import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.License;

public class OpenApiConfig {

    public OpenAPI productServiceOpenAPI() {
        return new OpenAPI()
                .info(new io.swagger.v3.oas.models.info.Info()
                        .title("Product Service API")
                        .description("API documentation for Product Service")
                        .version("1.0.0")
                        .license(new License()
                                .name("Apache")
                                .url("http://springdoc.org")));

    }

}
