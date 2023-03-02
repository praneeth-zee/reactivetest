// package com.praneeth.reactivetest.controller;

// import java.time.Duration;
// import java.util.List;
// import java.util.concurrent.CompletableFuture;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.context.annotation.Bean;
// import org.springframework.core.ParameterizedTypeReference;
// import org.springframework.core.io.ClassPathResource;
// import org.springframework.core.io.Resource;
// import org.springframework.http.HttpMethod;
// import org.springframework.http.MediaType;
// import org.springframework.http.ResponseEntity;
// import org.springframework.http.server.reactive.ServerHttpRequest;
// import org.springframework.scheduling.annotation.Async;
// import org.springframework.web.bind.annotation.DeleteMapping;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.PathVariable;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.RequestBody;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RestController;
// import org.springframework.web.client.RestTemplate;
// import org.springframework.web.reactive.function.client.WebClient;
// import org.springframework.web.reactive.function.server.RequestPredicates;
// import org.springframework.web.reactive.function.server.RouterFunction;
// import org.springframework.web.reactive.function.server.RouterFunctions;
// import org.springframework.web.reactive.function.server.ServerRequest;
// import org.springframework.web.reactive.function.server.ServerResponse;

// import com.praneeth.reactivetest.model.Employee;
// import com.praneeth.reactivetest.repository.EmployeeRepository;
// import com.praneeth.reactivetest.webclient.EmployeeWebClient;

// import io.micrometer.core.instrument.binder.http.HttpServletRequestTagsProvider;
// import reactor.core.publisher.Flux;
// import reactor.core.publisher.Mono;

// @RestController
// @RequestMapping("/employees")
// public class EmployeeController {

//     @Autowired
//     private EmployeeRepository employeeRepository;

//     @GetMapping // for /employees
//     private Flux<Employee> getAllEmployees() {
//         return employeeRepository.findAllEmployees();
//     }

//     @GetMapping("/{id}")
//     private Mono<Employee> getEmployeeById(@PathVariable String id) {
//         return employeeRepository.findEmployeeById(id);
//     }

//     // create or update employee
//     @PostMapping
//     private Mono<Employee> createEmployee(@RequestBody Employee employee) {
//         return employeeRepository.createEmployee(employee);
//     }

//     // delete employee
//     @DeleteMapping("/{id}")
//     private Mono<Void> deleteEmployee(@PathVariable String id) {
//         return employeeRepository.deleteEmployee(id);
//     }

//     @GetMapping("/test")
//     private Mono<String> test() {
//         return Mono.just("Hello");
//     }

//     // static routing

//     @Bean
//     public RouterFunction<ServerResponse> htmlRouter(@Value("classpath:/public/index.html") Resource html) {
//         return RouterFunctions.route(RequestPredicates.GET("/"),
//                 request -> ServerResponse.ok().contentType(MediaType.TEXT_HTML).bodyValue(html));
//     }

//     @Bean
//     public RouterFunction<ServerResponse> imgRouter() {
//         return RouterFunctions.resources("/img/**", new ClassPathResource("/static/img/"));
//     }

//     // error handling

//     private Mono<String> testError(ServerRequest request) {
//         try {
//             return Mono.just("Hello, " + request.queryParam("name").get());
//         } catch (Exception e) {
//             return Mono.error(e);
//         }
//     }
    
//     public Mono<ServerResponse> handleRequest(ServerRequest request) {
//         return testError(request)
//                 .onErrorReturn("Hello Stranger")
//                 .flatMap(s -> ServerResponse.ok()
//                         .contentType(MediaType.TEXT_PLAIN)
//                         .bodyValue(s));
//     }

//     // Default Return Status
//     @GetMapping(
//         value = "/ok",
//         produces = MediaType.APPLICATION_JSON_VALUE
//     )
//     public Flux<String> ok() {
//         return Flux.just("ok");
//     }

//     // Changing the Status Programmatically
//     // response.setStatusCode(HttpStatus.ACCEPTED);
//     // return Flux.just("accepted");

//     // with REST template
    
//     @GetMapping("/slow-service-employees")
//     private Flux<Employee> slowServiceEmployees() {
//         return employeeRepository.findAllEmployees().delayElements(Duration.ofMillis(2000));
//     }

//     @GetMapping("/get-employees-slowly")
//     private Flux<Employee> getEmployeesSlowly() {
//         final String uri = "http://localhost:8080/employees/slow-service-employees";
//         RestTemplate restTemplate = new RestTemplate();
//         ResponseEntity<List<Employee>> response = restTemplate.exchange(
//             uri, HttpMethod.GET, null,
//             new ParameterizedTypeReference<List<Employee>>() {} );
//         List<Employee> result = response.getBody();
//         return Flux.fromIterable(result);
//     }
    
//     @GetMapping("/get-employees-non-blocking")
//     private Flux<Employee> getEmployeesNonBlocking() {
//         final String uri = "http://localhost:8080/employees/slow-service-employees";
//         Flux<Employee> employeeFlux = WebClient.create()
//                 .get()
//                 .uri(uri)
//                 .retrieve()
//                 .bodyToFlux(Employee.class);
//         // employeeFlux.subscribe(System.out::println);
//         return employeeFlux;
//     }

//     /*
//         To call with parameters
//         webClient.get()
//             .uri(uriBuilder - > uriBuilder
//                 .path("/products/{id}/attributes/{attributeId}")
//                 .build(2, 13))
//             .retrieve()
//             .bodyToMono(String.class)
//             .block();
        
//         verifyCalledUrl("/products/2/attributes/13");
        
//         to call with query parameters
//         webClient.get()
//             .uri(uriBuilder - > uriBuilder
//                 .path("/products/")
//                 .queryParam("name", "AndroidPhone")
//                 .queryParam("color", "black")
//                 .queryParam("deliveryDate", "13/04/2019")
//                 .build())
//             .retrieve()
//             .bodyToMono(String.class)
//             .block();
        
//         verifyCalledUrl("/products/?name=AndroidPhone&color=black&deliveryDate=13/04/2019");
        
//         other way
//         webClient.get()
//             .uri(uriBuilder - > uriBuilder
//                 .path("/products/")
//                 .queryParam("name", "{title}")
//                 .queryParam("color", "{authorId}")
//                 .queryParam("deliveryDate", "{date}")
//                 .build("AndroidPhone", "black", "13/04/2019")). .......
        
//         to pass array of values
//         .queryParam("tag[]", "Snapdragon", "NFC") -> /products/?tag%5B%5D=Snapdragon&tag%5B%5D=NFC");
//         .queryParam("category", "Phones", "Tablets")  -> /products/?category=Phones&category=Tablets
//         .queryParam("category", String.join(",", "Phones", "Tablets")) -> /products/?category=Phones,Tablets
        
//     */

//     // stream
//     @GetMapping(value = "/stream-employees", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
//     public Flux<Employee> streamEmployees() {
//         return employeeRepository.loadAllEmployeesStream();
//     }

//     @GetMapping("/async_result")
//     @Async
//     public CompletableFuture<String> getResultAsync(HttpServletRequestTagsProvider request) {
//         // sleep for 500 ms
//         try {
//             Thread.sleep(500);
//         } catch (InterruptedException e) {
//             Thread.currentThread().interrupt();
//         }
//         return CompletableFuture.completedFuture("Result is ready!");
//     }
//     // to delay output by 2.5 seconds
//     @GetMapping("/flux_result")
//     public Mono<String> getResult(ServerHttpRequest request) {
//         EmployeeWebClient client = EmployeeWebClient.create();
//         // Mono<Employee> employeeMono = client.getEmployeeById("1");
//         Mono<String> testMono = client.test();
//         testMono.subscribe(System.out::println);

//         return Mono.defer(() -> Mono.just("Result is ready!"))
//                 .delaySubscription(Duration.ofMillis(2500));
//     }
    
    

// }