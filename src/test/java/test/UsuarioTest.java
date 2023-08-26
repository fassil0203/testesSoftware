package test;

import com.github.javafaker.Faker;
import entidade.Usuario;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;


public class UsuarioTest {
    @BeforeAll
    public static void setup(){
        RestAssured.baseURI= "https://serverest.dev/";

    }

    @Test
    public void criarUsuarioComSucesso(){

        Faker faker = new Faker();


        Usuario usuario = new Usuario(
                faker.name().fullName(),
                faker.internet().emailAddress(),
        "teste123",
        "true");

        Response response = RestAssured
                .given()
                .contentType(ContentType.JSON).log().all()
                .when()
                .body(usuario)
                .post("usuarios")
                .then()
                .statusCode(HttpStatus.SC_CREATED)
                .log().all().and().extract().response();
        JsonPath jsonpath =response.jsonPath();
        String message =jsonpath.get("message");
        Assertions.assertEquals(message,"Cadastro realizado com sucesso");



    }
    @Test
    public void testeUsuarioDuplicado (){
        Faker faker = new Faker();


        Usuario usuario = new Usuario(
                faker.name().fullName(),
                faker.internet().emailAddress(),
                "teste123",
                "true");


        given()
                .contentType(ContentType.JSON)
                .log().all()
                .when()
                .body(usuario)
                .post("usuarios")
                .then()
                .statusCode(HttpStatus.SC_CREATED)
                .log().all();

        given()
                .contentType(ContentType.JSON)
                .log().all()
                .when()
                .body(usuario)
                .post("usuarios")
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .log().all();

    }
    @Test
    public void buscarPorId(){

        Faker faker = new Faker();


        Usuario usuario = getNewUser();


        Response response = RestAssured
                .given()
                    .contentType(ContentType.JSON)
                    .when()
                        .body(usuario)
                        .post("usuarios")
                .then()
                    .statusCode(HttpStatus.SC_CREATED)
                    .log().all().and().extract().response();
        JsonPath jsonpath =response.jsonPath();
        String id =jsonpath.get("_id");

        RestAssured
                . given()
                    .contentType(ContentType.JSON)

                .when()
                     .get("usuarios/{_id}","uPpRZ3lWBvQ3UOVG")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .log().all();

        Assertions.assertEquals("","");

    }

    public Usuario getNewUser() {
        Faker faker = new Faker();
        return new Usuario(
                faker.name().fullName(),
                faker.internet().emailAddress(),
                "teste123",
                "true");


         }

    }






