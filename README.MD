# Products API 


###
This is a products API allowing to manage products i.e. list them, 
get single product or perform update or create.
Application is ready to be deployment to production on Docker as 
it agregates Java and PostgreSQL.

### Prerequisites

1. Docker is installed.
2. Maven is installed.

### Installation instruction

Please run following instructions to run locally:

1. Open terminal
2. Go to project directory, e.g. `cd /Users/roche/products`
3. Build project by executing `mvn clean install`
4. Run `docker-compose up --build`
5. After a while you should see:

```$xslt
app-products-postgresql | 2020-04-16 09:15:14.356  INFO 1 --- [           main] s.d.s.w.s.ApiListingReferenceScanner     : Scanning for api listing references
app-products-postgresql | 2020-04-16 09:15:14.632  INFO 1 --- [           main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat started on port(s): 8080 (http) with context path ''
app-products-postgresql | 2020-04-16 09:15:14.635  INFO 1 --- [           main] ch.roche.products.ProductsApplication    : Started ProductsApplication in 6.527 seconds (JVM running for 7.349)
```

### Using app

After successfull installation try to invoke such endpoints:

- `POST http://localhost:8080/products` 
- `PUT http://localhost:8080/products`
- `GET http://localhost:8080/products`
- `DELETE http://localhost:8080/products/{id}`
- `GET http://localhost:8080/products/{id}`


### Documentation

Application is fully documented using Swagger annotations. 

Please go to:
 - `http://localhost:8080/v2/api-docs` to get Swagger JSON
 - `http://localhost:8080/swagger-ui.html` to get Swagger UI
  
   